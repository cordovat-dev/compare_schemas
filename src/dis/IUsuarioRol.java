package dis;

import java.sql.SQLException;
import java.util.List;

public interface IUsuarioRol {
	public String getNombre();
	public void setNombre(String s_);
	public String getTipo();
	public void setTipo(String s_);
	public boolean isRol();
	public List<String> getPermisosObjetos();
	public List<String> getPrivilegiosSistema();
	public List<String> getRoles();
	public void CargarPermisos() throws SQLException;
}
