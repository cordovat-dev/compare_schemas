package imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dis.ICheckConstraint;

public class ComparadorCheckConstraintsFaltantes extends ComparadorObjetosFaltantes {

	private List<ICheckConstraint> lista1 = new ArrayList<ICheckConstraint>();
	private List<ICheckConstraint> lista2 = new ArrayList<ICheckConstraint>();
	private List<ICheckConstraint> lista1not2 = new ArrayList<ICheckConstraint>();
	private List<ICheckConstraint> lista2not1 = new ArrayList<ICheckConstraint>();
	private List<String> lista1not2Str = new ArrayList<String>();
	private List<String> lista2not1Str = new ArrayList<String>();

	public ComparadorCheckConstraintsFaltantes(Connection con1_, Connection con2_, String tipo_) throws SQLException {
		super(con1_,con2_,"CHECK_CONSTRAINT");
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
		
		for (ICheckConstraint c: this.lista1not2){
			this.lista1not2Str.add(c.toString());
		}
		
		for (ICheckConstraint c: this.lista2not1){
			this.lista2not1Str.add(c.toString());
		}
		
	}

	@Override
	public void setListaEsquemas(List<String> esquemas_) {
		this.esquemas=esquemas_;
	}
	
	private List<ICheckConstraint> armarLista(Connection con_) throws SQLException{
		List<ICheckConstraint> l = new ArrayList<ICheckConstraint>();
		
		String sql = this.armarSQL();
		
		PreparedStatement ps = con_.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		ICheckConstraint c = null;
		
		while (rs.next()){
			c = Fabrica.getCheckConstraint();
			c.setName(rs.getString("constraint_name"));
			c.setOwner(rs.getString("owner"));
			c.setTableName(rs.getString("table_name"));
			c.setSearchCondition(rs.getString("search_condition"));
			l.add(c);
		}
		
		rs.close();
		ps.close();
		ps = null;
		rs = null;			
		return l;		
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
	
	private String armarSQL() {
		String sql=
		"select \n"+ 
		"    c.constraint_name, \n"+
		"    c.owner, \n"+
		"    c.table_name, \n"+
		"    c.search_condition \n"+
		"from \n"+
		"    dba_constraints c \n"+
		"where \n"+
		"    c.CONSTRAINT_TYPE = 'C' \n";		
		
		if (!this.esquemas.isEmpty()){
			sql+="and c.owner in "+this.getCadenaItemsSQL(this.esquemas) +" \n";
		}	
		
		return sql;
	}

}
