package imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dis.IItemPermiso;
import dis.IUsuarioRol;

public class UsuarioRol implements IUsuarioRol {

	private Connection con;
	private String tipo;
	private String nombre;
	private List<String> permisosObjetos = new ArrayList<String>();
	private List<String> privilegiosSistema = new ArrayList<String>();
	private List<String> roles = new ArrayList<String>();

	public UsuarioRol(Connection con_) {
		this.con=con_;		
	}

	@Override
	public String getNombre() {
		return this.nombre;
	}

	@Override
	public boolean isRol() {
		return this.tipo.equals("ROLE");
	}

	@Override
	public List<String> getPermisosObjetos() {
		return this.permisosObjetos;
	}

	@Override
	public List<String> getPrivilegiosSistema() {
		return this.privilegiosSistema;
	}

	@Override
	public List<String> getRoles() {
		return this.roles;
	}

	@Override
	public void CargarPermisos() throws SQLException {
		this.cargarPrivilegiosSistema();
		this.cargarPermisosObjetos();
		this.cargarRoles();
	}
	
	private void cargarRoles() throws SQLException {
		String sql=
		"select \n"+ 
		"    r.granted_role, \n"+
		"    r.grantee, \n"+
		"    r.admin_option \n"+
		"from dba_role_privs r \n"+
		"where \n"+
		"    r.GRANTEE =? \n"+
		"order by \n"+
		"    r.GRANTED_ROLE \n";
		
		this.roles.clear();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, this.nombre);
		ResultSet rs = ps.executeQuery();
		ItemGrantRol ip;
		while (rs.next()){
			ip=new ItemGrantRol();
			ip.setAdminOption(rs.getString("admin_option"));
			ip.setNombreRol(rs.getString("granted_role"));
			ip.setNombreUsuario(rs.getString("grantee"));			
			if(ip.getCanonicalString() == null){
				int i=1/0;
			}
			this.roles.add(ip.getCanonicalString());
		}		
		rs.close();
		ps.close();
		ps = null;
		rs = null;					
	}

	private void cargarPermisosObjetos() throws SQLException {
		String sql=
		"select \n"+ 
		"      tb.privilege, \n"+ 
		"      tb.owner, \n"+
		"      tb.table_name, \n"+ 
		"      tb.grantee \n"+
		"from \n"+
		"    dba_tab_privs tb \n"+ 
		"where \n"+
		"    tb.GRANTEE=? and tb.owner<>'TEMPORAL' and tb.owner not like '%SYS%' \n"+
		"order by tb.grantee, tb.owner, tb.table_name, tb.privilege \n";
		
		this.permisosObjetos.clear();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, this.nombre);
		ResultSet rs = ps.executeQuery();
		ItemPermisoObjeto po;
		while (rs.next()){
			po=new ItemPermisoObjeto();
			po.setDueno(rs.getString("owner"));
			po.setObjeto(rs.getString("table_name"));
			po.setPrivilegio(rs.getString("privilege"));
			po.setUsuario(rs.getString("grantee"));
			this.permisosObjetos.add(po.getCanonicalString());
		}		
		rs.close();
		ps.close();
		ps = null;
		rs = null;				
	}

	private void cargarPrivilegiosSistema() throws SQLException {
		String sql=
		"select \n"+ 
		"      sp.privilege, \n"+ 
		"      sp.grantee \n"+
		"from \n"+
		"    dba_sys_privs sp \n"+ 
		"where \n"+
		"    sp.grantee=? \n"+
		"order by sp.grantee, sp.privilege \n";
		
		this.privilegiosSistema.clear();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, this.nombre);
		ResultSet rs = ps.executeQuery();
		ItemPrivilegioSistema prs;
		while (rs.next()){
			prs=new ItemPrivilegioSistema();
			prs.setNombrePrivilegio(rs.getString("privilege"));
			prs.setNombreUsuario(rs.getString("grantee"));
			this.privilegiosSistema.add(prs.getCanonicalString());
		}		
		rs.close();
		ps.close();
		ps = null;
		rs = null;	
	}

	@Override
	public void setNombre(String s_) {
		this.nombre=s_;
	}

	@Override
	public String getTipo() {
		return this.tipo;
	}

	@Override
	public void setTipo(String s_) {
		this.tipo=s_;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (o == null){
			return false;
		}
		
		if ( o == this ){
			return true;
		}
		
		if ( !(o instanceof UsuarioRol) ){
			return false;
		}
		
		UsuarioRol obj = (UsuarioRol) o;		
		
		return (
					obj.getNombre().equals(this.nombre) &&
					obj.getTipo().equals(this.tipo)
				);
	}		

}
