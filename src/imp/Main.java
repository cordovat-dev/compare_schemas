package imp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

import creadores.ListaCreadores;
import dis.IListaCreadores;
import dis.ISuiteComparacion;

public class Main {


	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);
		Connection con1=null;
		Connection con2=null;
		String rutaPar=null;
		String rutaArchivoComparadoresSoportados=null;
		String passwdDB1=null;
		String passwdDB2=null;
		String intentoConexion=null;
		int coSalida=1;
		try {
			rutaPar=args[0];
			rutaArchivoComparadoresSoportados=args[1];
			passwdDB1=args[2];
			passwdDB2=args[3];	
			System.out.println("\nUtilitario de Comparacion de Esquemas de Oracle. Tulains Cordova. cordovat@gmail.com\n");				
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			Parfile parametros = new Parfile(rutaPar);
			intentoConexion=parametros.getDB1();
			con1=DriverManager.getConnection(parametros.getConStr1(), parametros.getUser1(),passwdDB1);
			intentoConexion=parametros.getDB2();
			con2=DriverManager.getConnection(parametros.getConStr2(), parametros.getUser2(),passwdDB2);
			IListaCreadores lc = new ListaCreadores();
			
			ISuiteComparacion sc = lc.armarSuiteComparacion(rutaArchivoComparadoresSoportados, parametros, con1, con2);
			sc.setAliasesBD(parametros.getAlias1(), parametros.getAlias2());
			parametros.dumpArchivoConf();
			System.out.println("Proridad: " + parametros.getAliasPrioridad());
			sc.setPrioridad(parametros.getPrioridad());
			sc.setListaEsquemas(parametros.getEsquemas());
			sc.ejecutar();
			coSalida=0;
			con1.close();
			con1=null;
			con2.close();
			con2=null;
		} catch (SQLException ex) {
			switch (ex.getErrorCode()) {
			case 1017:
				System.out.println("\nERROR: Clave errada para "+intentoConexion+"\nEjecucion abortada");
				break;
			case 17002:
				System.out.println("\nERROR: Cadena de conexion errada ("+intentoConexion+")\nEjecucion abortada");
				break;
			case 1031:
				System.out.println("\nERROR: Privilegios insuficientes\nEjecucion abortada");
				break;
			case 28000:
				System.out.println("\nERROR: Cuenta bloqueada ("+intentoConexion+") \nEjecucion abortada");
				break;				
			default:
				   System.out.println(ex.getErrorCode());;
			       ex.printStackTrace();
			}			
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Archivo de parametros '"+rutaPar+" no encontrado\nEjecucion abortada");
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (ParametroMalFormadoException e) {
			System.out.println("ERROR: en parámetro: " + e.getMessage());
		} catch (ComparadorNoEncontradoException e) {
			System.out.println("ERROR: "+e.getMessage());
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("usage java -jar ComparacionEsquema.jar parfile archivo_comparadores password1 password2");			
		} finally {
		       try {
					con1.close();			
					con2.close();
				} catch (Exception e) {
				}				
		}
		System.exit(coSalida);
	}

}
