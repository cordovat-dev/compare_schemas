package imp;

import java.sql.Connection;

import dis.IParObjetosBD;
import dis.Prioridad;

public class ComparadorObjetosActivos extends ComparadorObjetosValidos {

	public ComparadorObjetosActivos(Connection con1_, Connection con2_) {
		super(con1_, con2_);
	}
	
	protected String armarSQL() {
		String sql =				
		"select \n"+
		"    owner, object_type, object_name, status \n"+ 
		"from \n"+
		"    ( \n"+
		"        select t.OWNER, 'TRIGGER' object_type, t.TRIGGER_NAME object_name , t.STATUS from all_triggers t where 1=1 \n";
		
		if (!this.esquemas.isEmpty()){
			sql+="and t.owner in "+this.getCadenaItemsSQL(this.esquemas) +" \n";
		}
		
		sql+=
		"        union \n"+
		"        select c.OWNER, 'CONSTRAINT' object_type, c.CONSTRAINT_NAME object_name, c.STATUS  from all_constraints c where 1=1 \n";
		
		if (!this.esquemas.isEmpty()){
			sql+="and c.owner in "+this.getCadenaItemsSQL(this.esquemas) +" \n";
		}		
		
		sql+=
		"    		) \n"+
		"order by \n"+
		"object_type,  owner, object_name \n";		
				
		return sql;
	}	

	@Override
	public void imprimir() {
		String s;
		System.out.println();
		System.out.println("##### COMPARACION OBJETOS ACTIVOS/INACTIVOS");
		System.out.println();
		System.out.println("Owner\tTipo\tNombre\t"+this.aliasBD1+"\t"+this.aliasBD2);
	
		for (IParObjetosBD p: this.paresDesiguales){
			s = String.format("%s\t%s\t%s\t%s\t%s",
								p.getObjectoBD1().getOwner(),
								p.getObjectoBD1().getTipo(),
								p.getObjectoBD1().getNombre(),
								p.getObjectoBD1().getEstado(),
								p.getObjectoBD2().getEstado()
							);
			if (this.prioridad == Prioridad.ORIGEN){
				if (p.getObjectoBD1().getEstado().equals("ENABLED") && p.getObjectoBD2().getEstado().equals("DISABLED")){
					System.out.println(s);
				}
			} else if (this.prioridad == Prioridad.DESTINO){
				if (p.getObjectoBD1().getEstado().equals("DISABLED") && p.getObjectoBD2().getEstado().equals("ENABLED")){
					System.out.println(s);
				}				
			} else {
				System.out.println(s);
			}
		}
	}	
}
