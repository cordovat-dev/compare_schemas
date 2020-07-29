package imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dis.IComparador;
import dis.Prioridad;

public class ComparadorOF implements IComparador {

	private List<String> esquemas=new ArrayList<String>();
	private List<String> tipos;
	private Connection con1;
	private Connection con2;
	private List<IComparador> comparadores = new ArrayList<IComparador>();
	private String aliasBD1;
	private String aliasBD2;
	private Prioridad prioridad=Prioridad.AMBOS;
	
	public ComparadorOF(Connection con1_, Connection con2_){
		this.con1=con1_;
		this.con2=con2_;
	}

	@Override
	public void comparar() throws SQLException {
		if (this.tipos==null) {
			this.tipos=new ArrayList<String>();
			this.tipos.add("CONSTRAINT_O");
			this.tipos.add("CONSTRAINT_V");
			this.tipos.add("FOREIGN_KEY");
			this.tipos.add("FUNCTION");
			this.tipos.add("INDEX");
			this.tipos.add("LIBRARY");
			this.tipos.add("PACKAGE");
			this.tipos.add("PACKAGE BODY");
			this.tipos.add("PRIMARY_KEY");
			this.tipos.add("PROCEDURE");
			this.tipos.add("SEQUENCE");
			this.tipos.add("SYNONYM");
			this.tipos.add("TABLE");
			this.tipos.add("TRIGGER");
			this.tipos.add("UNIQUE_CONSTRAINT");
			this.tipos.add("VIEW");
		}
		this.comparadores=Fabrica.getComparadoresObjectosFaltantes(this.tipos, this.con1, this.con2);
		for (IComparador c: this.comparadores){
			c.setListaEsquemas(this.esquemas);
			c.setAliasesBD(this.aliasBD1, this.aliasBD2);
			c.setPrioridad(this.prioridad);
			c.comparar();
		}
	}

	@Override
	public void imprimir() throws SQLException {
		System.out.println();
		System.out.println("##### OBJETOS FALTANTES");
		System.out.println();
		for (IComparador c: this.comparadores){
			c.imprimir();
		}		
	}

	@Override
	public void setListaEsquemas(List<String> esquemas_) {
		this.esquemas=esquemas_;
	}

	@Override
	public void setAliasesBD(String a1, String a2) {
		this.aliasBD1=a1;
		this.aliasBD2=a2;
	}

	@Override
	public void setListaEsquemas(String esquemas_) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPrioridad(Prioridad p) {
		this.prioridad=p;
	}

}
