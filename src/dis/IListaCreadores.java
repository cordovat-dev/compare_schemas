package dis;

import imp.ComparadorNoEncontradoException;
import imp.Parfile;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

public interface IListaCreadores {
	public ISuiteComparacion armarSuiteComparacion(
			String pathArchivoConectoresSoportados, Parfile parfile,
			Connection con1, Connection con2) throws SQLException, FileNotFoundException,
			ComparadorNoEncontradoException;
}
