package imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dis.IDBParmParser;
import dis.Prioridad;

public class Parfile {
	private IDBParmParser dbParser;
	private Properties prop = new Properties();
	private String ConStr1;
	private String ConStr2;
	private Prioridad prioridad=Prioridad.AMBOS;
	private String alias1;
	private String alias2;
	private String aliasPrioridad;
	private String user1;
	private String user2;
	private List<String> esquemas;
	private String db1;
	private String db2;
	private String rutaArchivoConf;

	public Parfile(String rutaArchivoConf) throws FileNotFoundException, IOException, ParametroMalFormadoException{
		this.rutaArchivoConf=rutaArchivoConf;
		this.prop.load(new FileInputStream(rutaArchivoConf));
		this.db1=prop.getProperty("BD1","").trim();
		this.db2=prop.getProperty("BD2","").trim();
		this.dbParser = Fabrica.getDBParmParser(this.db1);
		this.ConStr1=CrearConStr(this.dbParser);
		this.user1=this.dbParser.getUsuario();
		this.dbParser = Fabrica.getDBParmParser(this.db2);
		this.ConStr2=CrearConStr(this.dbParser);
		this.user2=this.dbParser.getUsuario();
		this.esquemas=this.parseLista(this.prop.getProperty("ESQUEMAS","").trim());
		String aliases=prop.getProperty("ALIASES","BD1,BD2").trim();
		String strPrioridad=prop.getProperty("PRIORIDAD","1").trim();
		this.prioridad=parsePrioridad(strPrioridad);
		this.parseAliases(aliases);
	}

	private Prioridad parsePrioridad(String strPrioridad)
			throws ParametroMalFormadoException {
		int p;
		Prioridad pri=null;
		try {
			p= Integer.parseInt(strPrioridad);
		} catch (NumberFormatException e){
			p = -1;
		}
		if (p<0 || p>2){
			throw new ParametroMalFormadoException("Prioridad: " + this.prioridad);			
		}
		switch(p){
		case 0:
			pri= Prioridad.AMBOS;
			break;
		case 1:
			pri=Prioridad.ORIGEN;
			break;
		case 2:
			pri=Prioridad.DESTINO;
			break;	
		}
		return pri;
	} 

	private String CrearConStr(IDBParmParser dbp) {
		return String.format("jdbc:oracle:thin:@%s:%s:%s",
							dbp.getIP(), 
							dbp.getPuerto(), 
							dbp.getInstancia());
	}
		
	private void parseAliases(String s) throws ParametroMalFormadoException{
		Pattern p = Pattern.compile("^([A-Z_0-9]{3,20}),([A-Z_0-9]{3,20})$");
		Matcher m = p.matcher(s);
		
		m.find();
		try {
			this.alias1=m.group(1);
			this.alias2=m.group(2);
		} catch (IllegalStateException ex) {
			throw new ParametroMalFormadoException(s);
		}
	}

	public String getConStr1() {
		return ConStr1;
	}

	public String getConStr2() {
		return ConStr2;
	}

	public Prioridad getPrioridad() {
		return prioridad;
	}

	public String getAlias1() {
		return alias1;
	}

	public String getAlias2() {
		return alias2;
	}

	public String getUser1() {
		if (this.user1.equalsIgnoreCase("SYS")) {
			return "SYS AS SYSDBA";
		}
		return user1;
	}

	public String getUser2() {
		if (this.user2.equalsIgnoreCase("SYS")) {
			return "SYS AS SYSDBA";
		}
		return user2;
	}

	public boolean existeParametro(String clave){
		return this.prop.getProperty(clave)!=null;
	}

	private List<String> parseLista(String lista_){
		List<String> lista = new ArrayList<String>();
		
		if (lista_.equals("")){ return lista;}
		
		String s[] = lista_.split(",");
		
		for (String st : s){
			lista.add(st.trim());
		}
		return lista;
	}

	public List<String> getEsquemas() {
		return esquemas;
	}

	public String getDB1() {
		return db1;
	}

	public String getDB2() {
		return db2;
	}

	public String getAliasPrioridad() {
		String s="";
		if (this.prioridad == Prioridad.AMBOS){s= Prioridad.AMBOS.toString();}
		if (this.prioridad == Prioridad.ORIGEN){s= this.alias1;}
		if (this.prioridad == Prioridad.DESTINO){s= this.alias2;}
		return s;
	}
	
	public void dumpArchivoConf(){
	    File file = new File(this.rutaArchivoConf);

	    try {

	        Scanner sc = new Scanner(file);

	        while (sc.hasNextLine()) {
	            System.out.println(sc.nextLine());
	        }
	        sc.close();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }		
	}
	
public void dumpParemetros(){

    Enumeration<String> e =(Enumeration<String>)this.prop.propertyNames();
    while (e.hasMoreElements()) {
      String key = e.nextElement();
      System.out.println(key + "=" + prop.getProperty(key));
    }	
}
}
