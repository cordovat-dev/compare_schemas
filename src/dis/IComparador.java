package dis;

import java.sql.SQLException;
import java.util.List;

public interface IComparador {
	public void comparar() throws SQLException;
	public void imprimir() throws SQLException;
	public void setPrioridad(Prioridad p);
	public void setAliasesBD(String a1, String a2);
	public void setListaEsquemas(List<String> esquemas_);
	public void setListaEsquemas(String esquemas_);
}
