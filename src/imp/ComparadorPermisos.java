package imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dis.IComparador;
import dis.IComparadorLista;
import dis.IUsuarioRol;
import dis.Prioridad;

public class ComparadorPermisos implements IComparador {

	private String aliasBD1;
	private String aliasBD2;
	private List<String> esquemas = new ArrayList<String>();
	private Connection con1;
	private Connection con2;
	private List<IUsuarioRol> usuariosBD1;
	private List<IUsuarioRol> usuariosBD2;
	private Prioridad prioridad = Prioridad.AMBOS;

	
	public ComparadorPermisos(Connection con1_, Connection con2_) throws SQLException {
		this.con1=con1_;
		this.con2=con2_;
		this.usuariosBD1=this.armarListaUsuarios(this.con1);
		this.usuariosBD2=this.armarListaUsuarios(this.con2);
	}

	@Override
	public void comparar() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void imprimir() throws SQLException {
		System.out.println("##### PERMISOS USUARIOS/ROLES");
		IComparadorLista cl;
		List<IComparadorLista> comps; 
		IUsuarioRol u2;
		for (IUsuarioRol u: this.usuariosBD1){
			
			if (this.usuariosBD2.indexOf(u) != -1){
				comps=new ArrayList<IComparadorLista>();
				u2=this.usuariosBD2.get(this.usuariosBD2.indexOf(u));
				u.CargarPermisos();
				u2.CargarPermisos();

				cl=Fabrica.getComparadorLista(u.getPermisosObjetos(),u2.getPermisosObjetos());
				cl.setAliases(this.aliasBD1, this.aliasBD2);
				cl.setNombre("PERMISOS");
				cl.setPrioridad(this.prioridad);
				comps.add(cl);
				
				cl=Fabrica.getComparadorLista(u.getPrivilegiosSistema(),u2.getPrivilegiosSistema());
				cl.setAliases(this.aliasBD1, this.aliasBD2);
				cl.setNombre("PRIVILEGIOS");
				cl.setPrioridad(this.prioridad);
				comps.add(cl);
				
				cl=Fabrica.getComparadorLista(u.getRoles(),u2.getRoles());
				cl.setNombre("ROLES");
				cl.setAliases(this.aliasBD1, this.aliasBD2);	
				cl.setPrioridad(this.prioridad);
				comps.add(cl);
				
				if (!comps.get(0).sonIdenticas() || !comps.get(1).sonIdenticas() || !comps.get(2).sonIdenticas()){
					System.out.println();
					System.out.println("## "+u.getTipo()+": "+u.getNombre());
					for (IComparadorLista c: comps){
						if (!c.sonIdenticas()){
							c.imprimir();
						}
					}
				}
				
			}
		}
		
	}

	@Override
	public void setAliasesBD(String a1, String a2) {
		this.aliasBD1=a1;
		this.aliasBD2=a2;
	}

	@Override
	public void setListaEsquemas(List<String> esquemas_) {
		this.esquemas  = esquemas_;
	}

	@Override
	public void setListaEsquemas(String esquemas_) {
		// TODO Auto-generated method stub
		
	}
	
	private List<IUsuarioRol> armarListaUsuarios(Connection con) throws SQLException{
		String sql=
		"select \n"+
		"    t.tipo, \n"+
		"    t.nombre \n"+
		"from \n"+
		"( \n"+
		"    select \n"+
		"        'USER' as tipo, \n"+
		"        u.USERNAME as nombre \n"+
		"    from \n"+
		"        dba_users u \n"+
		"    union \n"+
		"    select \n"+
		"        'ROLE' as tipo, \n"+
		"        r.ROLE as nombre \n"+
		"    from \n"+
		"        dba_roles r \n"+
		") t \n"+
		"order by \n"+
		"    t.tipo, \n"+
		"    t.nombre \n";
		List<IUsuarioRol> l = new ArrayList<IUsuarioRol>();
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		IUsuarioRol u=null;
		while (rs.next()){
			u=Fabrica.getUsuarioRol(con);
			u.setNombre(rs.getString("nombre"));
			u.setTipo(rs.getString("tipo"));
			l.add(u);
		}
		
		rs.close();
		ps.close();
		ps = null;
		rs = null;			
		return l;		
	}

	@Override
	public void setPrioridad(Prioridad p) {
		this.prioridad =p;
	}

}
