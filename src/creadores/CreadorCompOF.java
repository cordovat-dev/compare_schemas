package creadores;

import imp.ComparadorOF;

import java.sql.Connection;

import dis.IComparador;
import dis.ICreadorComparador;

public class CreadorCompOF implements ICreadorComparador {

	@Override
	public boolean matches(String clave) {
		return "COMPARADOR_OBJETOS_FALTANTES".matches(clave);
	}

	@Override
	public IComparador crearComparador(Connection con1_, Connection con2_) {
		return new ComparadorOF(con1_,con2_);
	}

}
