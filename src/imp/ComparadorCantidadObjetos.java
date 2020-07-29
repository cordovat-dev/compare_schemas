package imp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dis.ICategoria;
import dis.IComparador;
import dis.IParCategoria;
import dis.Prioridad;


public class ComparadorCantidadObjetos implements IComparador {
	
	List<ICategoria> categorias1 = new ArrayList<ICategoria>();
	List<ICategoria> categorias2 = new ArrayList<ICategoria>();
	List<IParCategoria> pares = new ArrayList<IParCategoria>();
	private List<String> esquemas=new ArrayList<String>();
	private Connection con1;
	private Connection con2;
	private String aliasBD1="BD1";
	private String aliasBD2="BD2";
	private Prioridad prioridad = Prioridad.AMBOS;

	public ComparadorCantidadObjetos(Connection con1_, Connection con2_){
		this.con1=con1_;
		this.con2=con2_;
	}
	
	@Override
	public void setListaEsquemas(List<String> esquemas_) {
		this.esquemas = esquemas_;
	}
	
	@Override
	public void setListaEsquemas(String esquemas_) {
		String[] c = esquemas_.split(",");
		for (String s: c){
			this.esquemas.add(s);
		}
	}	

	@Override
	public void comparar() throws SQLException {
		this.categorias1 = this.Procesar(this.con1);
		this.categorias2 = this.Procesar(this.con2);
		armarPares(this.categorias1, this.categorias2);
		armarPares(this.categorias2, this.categorias1);
	}

	private void armarPares(List<ICategoria> cats1, List<ICategoria> cats2) {
		ICategoria c2;
		IParCategoria par;
		for (ICategoria c1: cats1){
			par = Fabrica.getParCategoria();
			if (cats2.indexOf(c1) != -1){
				c2 = cats2.get(cats2.indexOf(c1));
			} else {
				c2 = Fabrica.getCategoria();
				c2.setNombre(c1.getNombre());
				c2.setCantidad(0);
			}
			
			par.setCategoria1(c1);
			par.setCategoria2(c2);
			
			if (this.pares.indexOf(par) != -1){
				
			} else { 
				this.pares.add(par);
			}
				
		}
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
	
	private List<ICategoria> Procesar(Connection con_) throws SQLException {
		List<ICategoria> lista= new ArrayList<ICategoria>();
		String sql = this.armarSQL();
		PreparedStatement ps = con_.prepareStatement(sql);		
		ResultSet rs = ps.executeQuery();
		
		ICategoria c;
		while (rs.next()){
			c = Fabrica.getCategoria();
			c.setNombre(rs.getString("object_type"));
			c.setCantidad(rs.getInt("cantidad"));
			lista.add(c);
		}
		rs.close();
		ps.close();
		ps = null;
		rs = null;			
		return lista;
	}

	private String armarSQL() {
		String sql =
		"select \n"+
		"    o.OBJECT_TYPE, \n"+
		"    count(*) cantidad \n"+
		"from all_objects o \n" +
		"where 1=1 \n";
		if (!this.esquemas.isEmpty()){
			sql+="and o.owner in "+this.getCadenaItemsSQL(this.esquemas) +" \n";
		}
		sql += "group by o.object_type \n"+
		"union \n"+
		"select \n"+
		"    decode(c.CONSTRAINT_TYPE,'C','CHECK_CONSTRAINT','P','PRIMARY_KEY','R','FOREIGN_KEY','U','UNIQUE_CONSTRAINT','O','CONSTRAINT_O','V','CONSTRAINT_V',c.CONSTRAINT_TYPE) object_type, \n"+
		"    count(*) cantidad \n"+
		"from \n"+
		"all_constraints c \n"+
		"where 1=1 \n";
		if (!this.esquemas.isEmpty()){
			sql+="and c.owner in "+this.getCadenaItemsSQL(this.esquemas) +" \n";
		}		
		sql+="group by decode(c.CONSTRAINT_TYPE,'C','CHECK_CONSTRAINT','P','PRIMARY_KEY','R','FOREIGN_KEY','U','UNIQUE_CONSTRAINT','O','CONSTRAINT_O','V','CONSTRAINT_V',c.CONSTRAINT_TYPE)";
				
		return sql;
	}

	@Override
	public void imprimir() throws SQLException {
		String s="";
		System.out.println("##### CANTIDAD DE OBJETOS POR TIPO");
		System.out.println();
		System.out.println("Tipo\t"+this.aliasBD1+"\t"+this.aliasBD2+"\tDiff");
		for (IParCategoria par: this.pares){
			s=String.format("%s\t%d\t%d\t%d", 
							par.getCategoria1().getNombre(),
							par.getCategoria1().getCantidad(),
							par.getCategoria2().getCantidad(),
							par.getDiferencia()
							);			
			System.out.println(s);			
		}		
	}

	@Override
	public void setAliasesBD(String a1, String a2) {
		this.aliasBD1=a1;
		this.aliasBD2=a2;
	}

	@Override
	public void setPrioridad(Prioridad p) {
		this.prioridad  = p;		
	}

}
