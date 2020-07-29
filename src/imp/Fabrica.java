package imp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dis.ICategoria;
import dis.ICheckConstraint;
import dis.IComparador;
import dis.IComparadorLista;
import dis.IContadorFilas;
import dis.IDBParmParser;
import dis.IItemPermiso;
import dis.IObjetoBD;
import dis.IParCategoria;
import dis.IParObjetosBD;
import dis.ISuiteComparacion;
import dis.IUsuarioRol;


public class Fabrica {

	public static ICategoria getCategoria() {
		return new Categoria();
	}

	public static IParCategoria getParCategoria() {
		return new ParCategoria();
	}

	private static IComparador getComparadorObjetosFaltantes2(Connection con1,
			Connection con2, String tipo) throws SQLException {
		return new ComparadorObjetosFaltantes(con1,con2,tipo);
	}
	
	
	public static IContadorFilas getContadorFilas(Connection con) {
		return new ContadorFilas(con);
	}

	public static IComparador getComparadorFilas(Connection con1, Connection con2) {
		return new ComparadorFilas(con1,con2);
	}

	public static IObjetoBD getObjetoBD() {
		return new ObjetoBD();
	}

	public static IParObjetosBD getParObjetosBD() {
		return new ParObjectosBD();
	}

	public static IComparador getComparadorValidos(
			Connection con1, Connection con2) {
		return new ComparadorObjetosValidos(con1,con2);
	}

	public static IComparador getComparadorActivos(Connection con1, Connection con2) {
		return new ComparadorObjetosActivos(con1,con2);
	}

	public static ICheckConstraint getCheckConstraint() {
		return new CheckConstraint();
	}

	public static IComparador getComparadorCheckConstraintsFaltantes(
			Connection con1, Connection con2,String t) throws SQLException {
		return new ComparadorCheckConstraintsFaltantes(con1,con2,t);
		
	}
	
	private static IComparador getComparadorCheckConstraintsFaltantes2(
			Connection con1, Connection con2, String t) throws SQLException {
		return new ComparadorCheckConstraintsFaltantes(con1,con2,t);
	}	

	public static ComparadorUsuariosFaltantes getComparadorUsuariosFaltantes(Connection con1, Connection con2) {
		return new ComparadorUsuariosFaltantes(con1,con2);
	}

	public static IComparador getComparadorCantidadObjetos(Connection c1,
			Connection c2) {
		return new ComparadorCantidadObjetos(c1,c2);
	}

	public static List<IComparador> getComparadoresObjectosFaltantes(
			List<String> listaAuxiliar, Connection con1, Connection con2) throws SQLException {
		List<IComparador> comps = new ArrayList<IComparador>();
		for (String s: listaAuxiliar){
			if (s.equals("CHECK_CONSTRAINT")){
				comps.add(Fabrica.getComparadorCheckConstraintsFaltantes2(con1,con2,s));
			} else {
				comps.add(Fabrica.getComparadorObjetosFaltantes2(con1,con2,s));
			}
		}
		return comps;
	}

	public static ISuiteComparacion getSuiteComparacion() {
		return new SuiteComparacion();
	}

	public static IUsuarioRol getUsuarioRol(Connection con) {
		return new UsuarioRol(con);
	}

	public static IComparadorLista getComparadorLista(List<String> list, List<String> list2) {
		return new ComparadorLista(list,list2);
	}

	public static IComparador getComparadorPermisos(Connection c1, Connection c2) throws SQLException {
		return new ComparadorPermisos(c1,c2);
	}

	public static IDBParmParser getDBParmParser(String str) throws ParametroMalFormadoException {
		return new DBParmParser(str);
	}

}
