package imp;
import dis.ICategoria;


public class Categoria implements ICategoria {

	private String nombre;
	private int cantidad;

	@Override
	public void setNombre(String nombre_) {
		this.nombre= nombre_;

	}

	@Override
	public String getNombre() {
		return this.nombre;
	}

	@Override
	public void setCantidad(int cantidad_) {
		this.cantidad = cantidad_;
	}

	@Override
	public int getCantidad() {
		return this.cantidad;
	}

	@Override
	public boolean equals(Object o) {
		
		if (o == null){
			return false;
		}
		
		if (o == this){
			return true;
		}
		
		if (!(o instanceof Categoria)){
			return false;
		}
		
		Categoria c = (Categoria)o;
		
		return c.getNombre().equals(this.getNombre());
	}
	
}
