package dis;

public interface IParCategoria {
	public ICategoria getCategoria1();
	public ICategoria getCategoria2();
	public void setCategoria1(ICategoria c);
	public void setCategoria2(ICategoria s);	
	public boolean sonIguales();
	public int getDiferencia();
}
