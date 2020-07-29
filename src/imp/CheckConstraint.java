package imp;

import dis.ICheckConstraint;

public class CheckConstraint implements ICheckConstraint, Comparable<CheckConstraint>{

	private String owner;
	private String name;
	private String table;
	private String hashCondition;
	private String condition;

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getOwner() {
		return this.owner;
	}

	@Override
	public String getTableName() {
		return this.table;
	}

	@Override
	public void setName(String name_) {
		this.name=name_;
	}

	@Override
	public void setOwner(String owner_) {
		this.owner=owner_;
	}

	@Override
	public void setTableName(String table_) {
		this.table=table_;
	}

	@Override
	public String getSearchConditionHash() {
		return this.hashCondition;
	}
	

	@Override
	public String getSearchCondition() {
		return this.condition;
	}

	@Override
	public void setSearchCondition(String condition_) {
		this.condition=condition_;
		this.condition=this.condition.replace("\"", "");
	}

	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CheckConstraint c = (CheckConstraint)o;
		if (
				this.getOwner().equals(c.getOwner()) && 
				this.getTableName().equals(c.getTableName()) &&
				this.getSearchCondition().equals(c.getSearchCondition())
			) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int compareTo(CheckConstraint c) {
		if (c == null ) return -1;
		
		if (this.equals(c)) {return 0;};
		
		String nombreCanonicoThis = this.getOwner()+"."+this.getTableName()+"."+this.getName();
		String nombreCanonicoThat = c.getOwner()+"."+c.getTableName()+"."+c.getName();
		
		return nombreCanonicoThis.compareTo(nombreCanonicoThat);
	}
	
	@Override
	public String toString(){
		String s="(CHECK_CONSTRAINT) %s.%s [%s]";
		String c;
		if (this.condition.length()>30){
			c=this.condition.substring(0, 30);
		} else {
			c=this.condition;
		}
		c=this.condition;
		return String.format(s, this.owner, this.name, c);
	}
	

}
