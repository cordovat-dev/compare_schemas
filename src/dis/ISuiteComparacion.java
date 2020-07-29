package dis;

import java.sql.SQLException;
import java.util.List;

public interface ISuiteComparacion {
	public void addComparador(IComparador comp_);
	public void addComparadores(List<IComparador> comps_);
	public void setAliasesBD(String a1, String a2);
	public void setListaEsquemas(List<String> esquemas_);
	public void setListaEsquemas(String esquemas_);
	public void setPrioridad(Prioridad p);
	public void ejecutar() throws SQLException;
}
