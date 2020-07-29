package creadores;

import imp.ComparadorUsuariosFaltantes;

import java.sql.Connection;

import dis.IComparador;
import dis.ICreadorComparador;

public class CreadorCompUsrFaltantes implements ICreadorComparador {

	@Override
	public boolean matches(String clave) {
		return "COMPARADOR_USUARIOS_FALTANTES".equals(clave);
	}

	@Override
	public IComparador crearComparador(Connection con1_, Connection con2_) {
		return new ComparadorUsuariosFaltantes(con1_,con2_);
	}

}
