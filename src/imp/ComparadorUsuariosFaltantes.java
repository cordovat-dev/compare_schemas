package imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dis.IComparador;
import dis.Prioridad;

public class ComparadorUsuariosFaltantes implements IComparador {

	private Connection con1;
	private Connection con2;
	private List<String> lista1 = new ArrayList<String>();
	private List<String> lista2 = new ArrayList<String>();
	private List<String> lista1not2 = new ArrayList<String>();
	private List<String> lista2not1 = new ArrayList<String>();
	private String aliasBD1;
	private String aliasBD2;
	private Prioridad prioridad = Prioridad.AMBOS;

	public ComparadorUsuariosFaltantes(Connection con1_, Connection con2_){
		this.con1=con1_;
		this.con2=con2_;		
	}
	
	private List<String> armarLista(Connection con_) throws SQLException{
		List<String> l = new ArrayList<String>();
		String sql =
		"select tipo, nombre from \n"+
		"( \n"+
		"select \n"+
		"    'USUARIO' tipo, \n"+
		"    u.USERNAME nombre \n"+
		"from \n"+
		"    dba_users u, \n"+
		"    (select distinct o.OWNER  from dba_objects o) w \n"+
		"where \n"+
		"    u.USERNAME  = w.owner(+) and \n"+
		"    w.owner is null \n"+
		"union \n"+
		"select \n"+
		"    'ROL' tipo, \n"+
		"    r.ROLE nombre \n"+
		"from \n"+
		"dba_roles r \n"+
		") order by tipo, nombre \n";
		
		PreparedStatement ps = con_.prepareStatement(sql);

		ResultSet rs = ps.executeQuery();
		String s="";
		while (rs.next()){
			s=String.format("(%s) %s", rs.getString("tipo"),rs.getString("nombre"));			
			l.add(s);
		}
		
		rs.close();
		ps.close();
		ps = null;
		rs = null;			
		return l;		
	}	
	
	@Override
	public void comparar() throws SQLException {
		this.lista1=this.armarLista(this.con1);
		this.lista2=this.armarLista(this.con2);
		this.lista1not2.addAll(this.lista1);
		this.lista1not2.removeAll(this.lista2);
		this.lista2not1.addAll(this.lista2);
		this.lista2not1.removeAll(this.lista1);
		
		this.lista1 = null;
		this.lista2 = null;		
		
	}

	@Override
	public void imprimir() {
		System.out.println();
		System.out.println("##### USUARIOS(NO_OWNER)/ROLES FALTANTES");
		System.out.println();		
		
		if (this.prioridad == Prioridad.ORIGEN || this.prioridad == Prioridad.AMBOS){
			System.out.println();
			System.out.println("Usuarios(no_owner)/roles que están en "+this.aliasBD1+" pero no en "+this.aliasBD2);
			System.out.println();
			for (String nb: this.lista1not2){
				System.out.println(nb);
			}			
		}

		if (this.prioridad == Prioridad.DESTINO || this.prioridad == Prioridad.AMBOS){
			System.out.println();
			System.out.println("Usuarios(no_owner)/roles que están en "+this.aliasBD2+" pero no en "+this.aliasBD1);
			System.out.println();
			for (String nb: this.lista2not1){
				System.out.println(nb);
			}			
		}

	}

	@Override
	public void setListaEsquemas(List<String> esquemas_) {
		
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
		this.prioridad =p;
	}

}
