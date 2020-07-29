package imp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dis.IContadorFilas;


public class ContadorFilas implements IContadorFilas {

	private Connection con;
	
	public ContadorFilas(Connection con_){
		this.con = con_;
	}
	
	@Override
	public int contarTabla(String tabla_) {
		int cont=0;
		String sql = "select count(*) cantidad from "+tabla_;
		PreparedStatement ps=null;;
		ResultSet rs=null;
		try {
			ps = this.con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()){
				cont=rs.getInt("cantidad");
			}
		
		} catch (SQLException e) {	
			return -1;
		} finally {
			try {
				rs.close();
				ps.close();				
			} catch (Exception e) {
			}

			ps = null;
			rs = null;				
		}
			
		return cont;
	}

}
