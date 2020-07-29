package creadores;

import imp.ComparadorNoEncontradoException;
import imp.Fabrica;
import imp.Parfile;
import imp.SuiteComparacion;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import dis.IComparador;
import dis.ICreadorComparador;
import dis.IListaCreadores;
import dis.ISuiteComparacion;
import dis.Prioridad;

public class ListaCreadores implements IListaCreadores {
	private static volatile ListaCreadores instancia = null;
	private static Map<String,ICreadorComparador> comparadores = null;

	public ListaCreadores(){
		comparadores = new HashMap<String,ICreadorComparador>();
		comparadores.put("COMPARADOR_CANTIDAD_OBJETOS",new CreadorCompCantObjetos());
		comparadores.put("COMPARADOR_OBJETOS_FALTANTES",new CreadorCompOF());
		comparadores.put("COMPARADOR_OBJETOS_VALIDOS",new CreadorCompObjValidos());
		comparadores.put("COMPARADOR_OBJETOS_ACTIVOS",new CreadorCompObjActivos());
		comparadores.put("COMPARADOR_USUARIOS_FALTANTES",new CreadorCompUsrFaltantes());
		comparadores.put("COMPARADOR_CANTIDAD_FILAS",new CreadorCompCantFilas());
		comparadores.put("COMPARADOR_PERMISOS",new CreadorCompPermisos());
	}
	
	@Override
	public ISuiteComparacion armarSuiteComparacion(
			String pathArchivoComparadoresSoportados, Parfile parfile,
			Connection con1_, Connection con2_) throws SQLException, FileNotFoundException,
			ComparadorNoEncontradoException {

			ISuiteComparacion sc = new  SuiteComparacion();
			sc.setPrioridad(parfile.getPrioridad());
			int cont=0;
			File archivoConectoresSoportados = new File(pathArchivoComparadoresSoportados);
			Scanner scanner = new Scanner(archivoConectoresSoportados);
			while (scanner.hasNextLine()) {
				cont++;
				String nombreComparadorSoportado = scanner.nextLine();
				if (nombreComparadorSoportado.matches("^COMPARADOR_.*$")) {
					try{
						IComparador c = comparadores.get(nombreComparadorSoportado).crearComparador(con1_, con2_);
						if (parfile.existeParametro(nombreComparadorSoportado)) { sc.addComparador(c); }
					} catch (NullPointerException ex){
						
					}
				}
			}
		
		return sc;
	}
	
	public static ListaCreadores getInstance() throws SQLException{
        if (instancia == null) {
            synchronized (ListaCreadores .class){
                    if (instancia == null) {
                    	instancia = new ListaCreadores ();
                    }
          }
        }
        return instancia;
	}	

}
