package imp;

import java.util.ArrayList;
import java.util.List;

import dis.IComparadorLista;
import dis.Prioridad;

public class ComparadorLista implements IComparadorLista {

	private List<String> lista1;
	private List<String> lista2;
	private List<String> lista1not2=new ArrayList<String>();
	private List<String> lista2not1=new ArrayList<String>();;
	private String alias1;
	private String alias2;
	private String nombre;
	private Prioridad prioridad=Prioridad.AMBOS;

	public ComparadorLista(List<String> lista1_, List<String> lista2_) {
		super();
		this.lista1 = lista1_;
		this.lista2 = lista2_;
		this.comparar();
	}
	
	private void comparar() {
		for (String s: this.lista1){
			if (s== null){
				System.out.println(3000);
			}
		}
		this.lista1not2.addAll(this.lista1);
		this.lista1not2.removeAll(this.lista2);
		this.lista2not1.addAll(this.lista2);
		this.lista2not1.removeAll(this.lista1);
		this.lista1 = null;
		this.lista2 = null;			
	}

	@Override
	public boolean sonIdenticas() {
		return this.lista1not2.isEmpty() && this.lista2not1.isEmpty();
	}

	@Override
	public void imprimir() {
		if (this.prioridad == Prioridad.ORIGEN || this.prioridad == Prioridad.AMBOS){
			System.out.println();
			System.out.println(this.nombre+" que están en "+this.alias1+" pero no en "+this.alias2);
			System.out.println();
			for (String nb: this.lista1not2){
				System.out.println(nb);
			}			
		}
		
		if (this.prioridad == Prioridad.DESTINO	 || this.prioridad == Prioridad.AMBOS){
			System.out.println();
			System.out.println(this.nombre+" que están en "+this.alias2+" pero no en "+this.alias1);
			System.out.println();
			for (String nb: this.lista2not1){
				System.out.println(nb);
			}			
		}		

	}

	@Override
	public void setAliases(String alias1_, String alias2_) {
		this.alias1=alias1_;
		this.alias2=alias2_;
	}

	@Override
	public void setNombre(String s) {
		this.nombre=s;
	}

	@Override
	public void setPrioridad(Prioridad p) {
		this.prioridad=p;
	}

}
