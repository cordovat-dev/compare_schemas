package imp;

import dis.IObjetoBD;
import dis.IParObjetosBD;

public class ParObjectosBD implements IParObjetosBD {

	private IObjetoBD obj1;
	private IObjetoBD obj2;

	@Override
	public void setObjectoBD1(IObjetoBD o) {
		this.obj1 = o;
	}

	@Override
	public void setObjectoBD2(IObjetoBD o) {
		this.obj2 = o;
	}

	@Override
	public IObjetoBD getObjectoBD1() {
		return this.obj1;
	}

	@Override
	public IObjetoBD getObjectoBD2() {
		return this.obj2;
	}

}
