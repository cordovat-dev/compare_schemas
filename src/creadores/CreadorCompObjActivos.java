package creadores;

import imp.ComparadorObjetosActivos;

import java.sql.Connection;

import dis.IComparador;
import dis.ICreadorComparador;

public class CreadorCompObjActivos implements ICreadorComparador {

	@Override
	public boolean matches(String clave) {
		return "COMPARADOR_OBJETOS_ACTIVOS".equals(clave);
	}

	@Override
	public IComparador crearComparador(Connection con1_, Connection con2_) {
		return new ComparadorObjetosActivos(con1_,con2_);
	}

}
