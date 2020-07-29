package dis;

public interface IDBParmParser {
	public String getConnectionString();

	public abstract String getInstancia();

	public abstract int getPuerto();

	public abstract String getIP();

	public abstract String getUsuario();
}
