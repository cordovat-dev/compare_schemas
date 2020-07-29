package dis;

public interface ICheckConstraint {
	public String getName();
	public String getOwner();
	public String getTableName();
	public String getSearchCondition();
	public void setName(String name_);
	public void setOwner(String owner_);
	public void setTableName(String table_);
	public void setSearchCondition(String condition_);
	public String getSearchConditionHash();
}
