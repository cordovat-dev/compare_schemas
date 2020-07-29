package imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dis.IComparador;
import dis.ISuiteComparacion;
import dis.Prioridad;

public class SuiteComparacion implements ISuiteComparacion {

	private List<IComparador> comparadores = new ArrayList<IComparador>();
	private String aliasBD1="BD1";
	private String aliasBD2="BD2";
	private List<String> esquemas=new ArrayList<String>();
	private Prioridad prioridad=Prioridad.AMBOS;
	
	@Override
	public void addComparador(IComparador comp_) {
		comp_.setPrioridad(this.prioridad);
		this.comparadores.add(comp_);		
	}

	@Override
	public void ejecutar() throws SQLException {
		System.out.println("\n##### REPORTE COMPARATIVO "+this.aliasBD1+" - "+this.aliasBD2);
		System.out.println();
		for (IComparador c: this.comparadores){
			c.setAliasesBD(this.aliasBD1, this.aliasBD2);
			c.setListaEsquemas(this.esquemas);
			c.comparar();
			c.imprimir();
		}
		System.out.println("\n##### FIN DEL REPORTE");	
	}
	
	@Override
	public void addComparadores(List<IComparador> comps_) {
		this.comparadores.addAll(comps_);
	}
	
	@Override
	public void setAliasesBD(String a1, String a2) {
		this.aliasBD1=a1;
		this.aliasBD2=a2;
	}

	@Override
	public void setListaEsquemas(List<String> esquemas_) {
		this.esquemas=esquemas_;
	}

	@Override
	public void setListaEsquemas(String esquemas_) {
		String[] c = esquemas_.split(",");
		for (String s: c){
			this.esquemas.add(s);
		}
	}

	@Override
	public void setPrioridad(Prioridad p) {
		this.prioridad=p;
	}

}
