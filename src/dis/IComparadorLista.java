package dis;

public interface IComparadorLista {
	public boolean sonIdenticas();
	public void imprimir();
	public void setPrioridad(Prioridad p);
	public void setAliases(String alias1, String alias2);
	public void setNombre(String s);
}
