package dis;
import java.sql.SQLException;


public interface IContadorFilas {
	public int contarTabla(String tabla_) throws SQLException;
}
