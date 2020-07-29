package imp;
import dis.ICategoria;
import dis.IParCategoria;


public class ParCategoria implements IParCategoria {

	private ICategoria categoria1;
	private ICategoria categoria2;

	@Override
	public ICategoria getCategoria1() {
		return this.categoria1;
	}

	@Override
	public ICategoria getCategoria2() {
		return this.categoria2;
	}

	@Override
	public boolean sonIguales() {
		return (this.categoria1.getCantidad()==this.categoria2.getCantidad());
	}

	@Override
	public int getDiferencia() {
		return this.categoria1.getCantidad()-this.categoria2.getCantidad();
	}

	@Override
	public void setCategoria1(ICategoria c) {
		this.categoria1=c;
	}

	@Override
	public void setCategoria2(ICategoria c) {
		this.categoria2=c;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (o == null){
			return false;
		}
		
		if (o == this){
			return true;
		}
		
		if (!(o instanceof ParCategoria)){
			return false;
		}
		ParCategoria p = (ParCategoria)o;
		
		return p.getCategoria1().equals(this.categoria1);
	}	

}
