package creadores;

import imp.ComparadorFilas;

import java.sql.Connection;

import dis.IComparador;
import dis.ICreadorComparador;

public class CreadorCompCantFilas implements ICreadorComparador {

	@Override
	public boolean matches(String clave) {
		return "COMPARADOR_CANTIDAD_FILAS".equals(clave);
	}

	@Override
	public IComparador crearComparador(Connection con1_, Connection con2_) {
		return new ComparadorFilas(con1_,con2_);
	}

}
