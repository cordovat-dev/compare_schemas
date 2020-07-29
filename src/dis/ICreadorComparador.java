package dis;

import java.sql.Connection;
import java.sql.SQLException;

public interface ICreadorComparador {
	public boolean matches(String clave);
	public IComparador crearComparador(Connection con1_, Connection con2_) throws SQLException;
}