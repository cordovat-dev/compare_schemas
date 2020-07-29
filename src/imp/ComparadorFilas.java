package imp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dis.IComparador;
import dis.IContadorFilas;
import dis.Prioridad;


public class ComparadorFilas implements IComparador {

	private IContadorFilas contadorFilas1;
	private IContadorFilas contadorFilas2;
	private List<String> esquemas;
	private List<String> tablas;
	private Connection con1;
	private Connection con2;
	private String aliasBD1="BD1";
	private String aliasBD2="BD2";
	private Prioridad prioridad = Prioridad.AMBOS;

	public ComparadorFilas(Connection con1_, Connection con2_){
		this.con1=con1_;
		this.con2=con2_;
		this.contadorFilas1=Fabrica.getContadorFilas(this.con1);
		this.contadorFilas2=Fabrica.getContadorFilas(this.con2);		
	}

	@Override
	public void setListaEsquemas(List<String> esquemas_) {
		this.esquemas=esquemas_;
	}

	@Override
	public void comparar() throws SQLException {
		this.tablas = armarListaTablas(this.con1);
	}
	
	private List<String> armarListaTablas(Connection conn) throws SQLException {
		List<String> l = new ArrayList<String>();
		String sql = "select owner||'.'||object_name tabla from all_objects where object_type = 'TABLE'";
		if (!this.esquemas.isEmpty()){
			sql+=" and owner in "+this.getCadenaItemsSQL(this.esquemas);
		}
		sql+=" order by owner, object_name";		
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()){
			l.add(rs.getString("tabla"));
		}
		
		rs.close();
		ps.close();
		ps = null;
		rs = null;			
		return l;	
	}
	public String getCadenaItemsSQL(List<String> l) {
		String retorno="( ";
		int c = 0;
		for (String s: l){
			c++;
			if (c >1){
				retorno += ",'"+s+"'";
			} else {
				retorno += "'"+s+"'";
			}
		}
		return retorno+" )";
	}
	@Override
	public void imprimir() throws SQLException {
		System.out.println();
		System.out.println("##### CANTIDAD DE FILAS");
		System.out.println();
		int a,b;
		System.out.println("Tabla\t"+this.aliasBD1+"\t"+this.aliasBD2+"\tDiff");
		for (String tabla: this.tablas){
			System.out.print(tabla+"\t");
			a=this.contadorFilas1.contarTabla(tabla);
			System.out.print(a+"\t");
			b=this.contadorFilas2.contarTabla(tabla);			
			System.out.println(String.format("%d\t%d", b,a-b));
		}
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
		this.prioridad   = p;
	}	

}
