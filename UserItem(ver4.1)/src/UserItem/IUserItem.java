package UserItem;
public interface IUserItem {
	String getColumnName(int columnIndex);
	String getColumnName(Object columnValue);
	Object getColumnValue(String columnName);
	Object getColumnValue(int columnIndex);
	void putColumnValue(int columnIndex, Object columnValue);
	void putColumnValue(String columnName, Object columnValue);
	int getColumnIndex();
}