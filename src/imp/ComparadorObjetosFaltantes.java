package imp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dis.IComparador;
import dis.Prioridad;

public class ComparadorObjetosFaltantes implements IComparador {

	protected String tipo;
	protected Connection con1;
	protected Connection con2;
	private List<String> lista1 = new ArrayList<String>();
	private List<String> lista2 = new ArrayList<String>();
	private List<String> lista1not2 = new ArrayList<String>();
	private List<String> lista2not1 = new ArrayList<String>();
	protected List<String> esquemas =  new ArrayList<String>();
	protected String aliasBD1="BD1";
	protected String aliasBD2="BD2";
	private Prioridad prioridad = Prioridad.AMBOS;
	
	public ComparadorObjetosFaltantes(Connection con1_, Connection con2_, String tipo_) throws SQLException {
		this.tipo = tipo_;
		this.con1=con1_;
		this.con2=con2_;
	}
	
	private List<String> armarLista(Connection con_) throws SQLException{
		List<String> l = new ArrayList<String>();
		String sql = this.armarSQL();
		
		PreparedStatement ps = con_.prepareStatement(sql);
		ps.setString(1, this.tipo);
		ps.setString(2, this.tipo);
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()){
			l.add(rs.getString("objeto"));
		}
		
		rs.close();
		ps.close();
		ps = null;
		rs = null;			
		return l;		
	}
	
	@Override
	public void setListaEsquemas(List<String> esquemas_) {
			this.esquemas=esquemas_;
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
	public void comparar() throws SQLException {
		this.lista1 = this.armarLista(this.con1);
		this.lista2 = this.armarLista(this.con2);
		this.lista1not2.addAll(this.lista1);
		this.lista1not2.removeAll(this.lista2);
		this.lista2not1.addAll(this.lista2);
		this.lista2not1.removeAll(this.lista1);
		
		this.lista1 = null;
		this.lista2 = null;
		
	}	
	
	private String armarSQL() {
		
		String sql =
		"select objeto from ("+
		"select \n"+
		"	'('||object_type||') '||owner||'.'||object_name objeto \n"+
		"from all_objects o \n" +
		"where object_type = ? \n";
		if (!this.esquemas.isEmpty()){
			sql+="and o.owner in "+this.getCadenaItemsSQL(this.esquemas) +" \n";
		}
		sql+=
		"union \n"+
		"select \n"+
		"    '('||decode(c.CONSTRAINT_TYPE,'C','CHECK_CONSTRAINT','P','PRIMARY_KEY','R','FOREIGN_KEY','U','UNIQUE_CONSTRAINT','O','CONSTRAINT_O','V','CONSTRAINT_V',c.CONSTRAINT_TYPE)||') '||owner||'.'||constraint_name objeto \n"+		
		"from \n"+
		"all_constraints c \n"+
		"where decode(c.CONSTRAINT_TYPE,'C','CHECK_CONSTRAINT','P','PRIMARY_KEY','R','FOREIGN_KEY','U','UNIQUE_CONSTRAINT','O','CONSTRAINT_O','V','CONSTRAINT_V',c.CONSTRAINT_TYPE) = ? \n";
		if (!this.esquemas.isEmpty()){
			sql+="and c.owner in "+this.getCadenaItemsSQL(this.esquemas) +" \n";
		}		
		sql+=" ) m order by objeto ";
				
		return sql;
	}

	@Override
	public void imprimir() throws SQLException {
		if (this.prioridad == Prioridad.ORIGEN || this.prioridad == Prioridad.AMBOS){
			System.out.println();
			System.out.println("Objetos de tipo "+this.tipo+" que están en "+this.aliasBD1+" pero no en "+this.aliasBD2);
			System.out.println();
			for (String nb: this.lista1not2){
				System.out.println(nb);
			}			
		}
		if (this.prioridad == Prioridad.DESTINO || this.prioridad == Prioridad.AMBOS){
			System.out.println();
			System.out.println("Objetos de tipo "+this.tipo+" que están en "+this.aliasBD2+" pero no en "+this.aliasBD1);
			System.out.println();
			for (String nb: this.lista2not1){
				System.out.println(nb);
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
