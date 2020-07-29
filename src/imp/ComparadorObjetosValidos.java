package imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dis.IComparador;
import dis.IObjetoBD;
import dis.IParObjetosBD;
import dis.Prioridad;

public class ComparadorObjetosValidos implements IComparador {

	protected List<String> esquemas;
	private Connection con1;
	private Connection con2;
	private List<IObjetoBD> objectos1 = new ArrayList<IObjetoBD>();
	private List<IObjetoBD> objectos2 = new ArrayList<IObjetoBD>();
	protected List<IParObjetosBD> paresDesiguales = new ArrayList<IParObjetosBD>();
	protected String aliasBD1;
	protected String aliasBD2;
	protected Prioridad prioridad = Prioridad.AMBOS;

	public ComparadorObjetosValidos(Connection con1_, Connection con2_){
		this.con1=con1_;
		this.con2=con2_;
		
	}
	
	@Override
	public void comparar() throws SQLException {
		this.objectos1 = armarListaObjectos(this.con1);
		this.objectos2 = armarListaObjectos(this.con2);
		IParObjetosBD par=null;
		IObjetoBD obj2=null;
		for (IObjetoBD obj1: this.objectos1){
			if (this.objectos2.indexOf(obj1) != -1){
				obj2 = this.objectos2.get(this.objectos2.indexOf(obj1));
				if (!obj1.getEstado().equals(obj2.getEstado())){
					par=Fabrica.getParObjetosBD();
					par.setObjectoBD1(obj1);
					par.setObjectoBD2(obj2);
					this.paresDesiguales.add(par);
				}
			}
		}
	}	
	
	protected List<IObjetoBD> armarListaObjectos(Connection con) throws SQLException {
		List<IObjetoBD> lista= new ArrayList<IObjetoBD>();
		String sql = this.armarSQL();
		PreparedStatement ps = con.prepareStatement(sql);		
		ResultSet rs = ps.executeQuery();
		IObjetoBD o;
		while (rs.next()){
			o = Fabrica.getObjetoBD();
			o.setEstado(rs.getString("status"));
			o.setNombre(rs.getString("object_name"));
			o.setOwner(rs.getString("owner"));
			o.setTipo(rs.getString("object_type"));			
			lista.add(o);
		}
		rs.close();
		ps.close();
		ps = null;
		rs = null;			
		return lista;
	}

	@Override
	public void setListaEsquemas(List<String> esquemas_) {
		this.esquemas = esquemas_;
	}


	protected String armarSQL() {
		String sql =
		" select \n" +
		"	o.owner, o.object_type, o.object_name, o.status \n" +
		"from \n" +
		"	all_objects o \n" +
		"where \n" +
		"	o.object_type in ('FUNCTION','PACKAGE','PACKAGE BODY','PROCEDURE','TRIGGER','VIEW') \n";
		
		if (!this.esquemas.isEmpty()){
			sql+="and o.owner in "+this.getCadenaItemsSQL(this.esquemas) +" \n";
		}
		sql += "order by o.OWNER, o.OBJECT_TYPE, o.OBJECT_NAME \n";
				
		return sql;
	}
	
	public String getCadenaItemsSQL(List<String> l) {
		String retorno="( ";
		int c = 0;
		for (String s: l){
			c++;
			if (c >1){
				retorno += ",'"+s+"'";
			} else {
				retorno += "'"+s+"'";
			}
		}
		return retorno+" )";
	}

	@Override
	public void imprimir() {
		String s;
		System.out.println();
		System.out.println("##### COMPARACION OBJETOS VALIDOS/INVALIDOS");
		System.out.println();
		System.out.println("Owner\tTipo\tNombre\t"+this.aliasBD1+"\t"+this.aliasBD2);
	
		for (IParObjetosBD p: this.paresDesiguales){
			s = String.format("%s\t%s\t%s\t%s\t%s",
								p.getObjectoBD1().getOwner(),
								p.getObjectoBD1().getTipo(),
								p.getObjectoBD1().getNombre(),
								p.getObjectoBD1().getEstado(),
								p.getObjectoBD2().getEstado()
							);
			if (this.prioridad == Prioridad.ORIGEN){
				if (p.getObjectoBD1().getEstado().equals("VALID") && p.getObjectoBD2().getEstado().equals("INVALID")){
					System.out.println(s);
				}
			} else if (this.prioridad == Prioridad.DESTINO){
				if (p.getObjectoBD1().getEstado().equals("INVALID") && p.getObjectoBD2().getEstado().equals("VALID")){
					System.out.println(s);
				}				
			} else {
				System.out.println(s);
			}
			
		}
	}

	@Override
	public void setAliasesBD(String a1, String a2) {
		this.aliasBD1=a1;
		this.aliasBD2=a2;
	}

	@Override
	public void setListaEsquemas(String esquemas_) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPrioridad(Prioridad p) {
		this.prioridad  = p;
	}
	
}
