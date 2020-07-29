package imp;

import dis.IItemPermiso;

public class ItemGrantRol implements IItemPermiso {
	protected String nombreUsuario;
	protected String nombreRol;
	protected String adminOption;

	public String getAdminOption() {
		return adminOption;
	}

	public void setAdminOption(String adminOption_) {
		this.adminOption = adminOption_;
	}

	@Override
	public String getCanonicalString() {
		String sAdminOption="";
		if (this.adminOption.equals("YES")){
			sAdminOption = " WITH ADMIN OPTION";
		}
		String s =
		"GRANT \"%s\" TO \"%s\"%s;";
		return String.format(s, this.nombreRol, this.nombreUsuario, sAdminOption, this.nombreRol, this.nombreUsuario,sAdminOption);
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario_) {
		this.nombreUsuario = nombreUsuario_;
	}

	public String getNombreRol() {
		return nombreRol;
	}

	public void setNombreRol(String nombreRol_) {
		this.nombreRol = nombreRol_;
	}

}
