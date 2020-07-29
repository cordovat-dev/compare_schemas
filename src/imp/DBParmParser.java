package imp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dis.IDBParmParser;

public class DBParmParser implements IDBParmParser {

	private String str;
	private String usuario;
	private String ip;
	private int puerto;
	private String instancia;

	public DBParmParser(String str_) throws ParametroMalFormadoException {
		this.str=str_;
		this.parse();
	}

	private void parse() throws ParametroMalFormadoException {
		Pattern p = Pattern.compile("^([a-zA-Z_0-9]{2,20})@([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}):([0-9]{4})/([a-zA-Z_0-9]{2,20})$");
		Matcher m = p.matcher(this.str);
		
		m.find();
		try {
			this.usuario=m.group(1);
			this.ip=m.group(2);
			this.puerto=Integer.parseInt(m.group(3));
			this.instancia=m.group(4);
			if (!this.validarIP(this.ip)){
				throw new ParametroMalFormadoException("IP: "+this.ip);
			}
		} catch (IllegalStateException ex) {
			throw new ParametroMalFormadoException(this.str);
		}
	}

	@Override
	public String getConnectionString() {
		return null;
	}
	
	private static boolean validarIP(String ip_) {

		Pattern p = Pattern.compile("^([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})$");
		Matcher m = p.matcher(ip_);

		m.find();
		try {
			int[] numeros = new int[]{Integer.parseInt(m.group(1)),Integer.parseInt(m.group(2)),Integer.parseInt(m.group(3)),Integer.parseInt(m.group(4))};

			for (int i=0;i<numeros.length;i++){
				if (numeros[i]>255 ){return false;}
			}
			
		} catch (IllegalStateException ex) {
			return false;
		}
		
		return true;
		
	}

	@Override
	public String getUsuario() {
		return usuario;
	}

	@Override
	public String getIP() {
		return ip;
	}

	@Override
	public int getPuerto() {
		return puerto;
	}

	@Override
	public String getInstancia() {
		return instancia;
	}

}
