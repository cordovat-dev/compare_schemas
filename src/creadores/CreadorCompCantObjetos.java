package creadores;

import imp.ComparadorCantidadObjetos;

import java.sql.Connection;

import dis.IComparador;
import dis.ICreadorComparador;

public class CreadorCompCantObjetos implements ICreadorComparador {

	@Override
	public boolean matches(String clave) {
		return "COMPARADOR_CANTIDAD_OBJETOS".equals(clave);
	}

	@Override
	public IComparador crearComparador(Connection con1_, Connection con2_) {
		return new ComparadorCantidadObjetos(con1_,con2_);
	}

}
