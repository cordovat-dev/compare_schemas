package imp;

import dis.IObjetoBD;

public class ObjetoBD implements IObjetoBD {

	private String nombre;
	private String owner;
	private String tipo;
	private String estado;

	@Override
	public void setNombre(String nombre_) {
		this.nombre = nombre_;
	}

	@Override
	public void setOwner(String owner_) {
		this.owner= owner_;
	}

	@Override
	public void setTipo(String tipo_) {
		this.tipo=tipo_;
	}

	@Override
	public void setEstado(String estado_) {
		this.estado=estado_;
	}

	@Override
	public String getNombre() {
		return this.nombre;
	}

	@Override
	public String getOwner() {
		return this.owner;
	}

	@Override
	public String getTipo() {
		return this.tipo;
	}

	@Override
	public String getEstado() {
		return this.estado;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (o == null){
			return false;
		}
		
		if ( o == this ){
			return true;
		}
		
		if ( !(o instanceof ObjetoBD) ){
			return false;
		}
		
		ObjetoBD obj = (ObjetoBD) o;		
		
		return (
					obj.getOwner().equals(this.getOwner()) &&
					obj.getNombre().equals(this.getNombre()) &&
					obj.getTipo().equals(this.getTipo())
				);
	}		

}
