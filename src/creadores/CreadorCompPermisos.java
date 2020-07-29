package creadores;

import imp.ComparadorPermisos;

import java.sql.Connection;
import java.sql.SQLException;

import dis.IComparador;
import dis.ICreadorComparador;

public class CreadorCompPermisos implements ICreadorComparador {

	@Override
	public boolean matches(String clave) {
		return "COMPARADOR_PERMISOS".equals(clave);
	}

	@Override
	public IComparador crearComparador(Connection con1_, Connection con2_) throws SQLException {
		return new ComparadorPermisos(con1_,con2_);
	}

}
