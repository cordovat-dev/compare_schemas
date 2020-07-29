package creadores;

import imp.ComparadorObjetosValidos;

import java.sql.Connection;

import dis.IComparador;
import dis.ICreadorComparador;

public class CreadorCompObjValidos implements ICreadorComparador {

	@Override
	public boolean matches(String clave) {
		return "COMPARADOR_OBJETOS_VALIDOS".equals(clave);
	}

	@Override
	public IComparador crearComparador(Connection con1_, Connection con2_) {
		return new ComparadorObjetosValidos(con1_,con2_);
	}

}
