package UserItem;
import java.util.*;
public class DefaultUserItem implements IUserItem {
	private LinkedHashMap<String, Object> userItemMap;
	private final int COLUMN_INDEX;

	public String getColumnName(int columnIndex) {
		ArrayList<String> keyArrayList = new ArrayList<>(userItemMap.keySet());
		return keyArrayList.get(columnIndex);
	}

	public String getColumnName(Object columnValue) {
		List<Object> keyArrayList = new LinkedList<>(userItemMap.values());
		int machIndex = -1;
		for(int i = 0; i < COLUMN_INDEX; i++) {
			if(keyArrayList.get(i).equals(columnValue)) {
				machIndex = i;
				break;
			}
		}
		List<String> columnList = new LinkedList<>(userItemMap.keySet());
		return columnList.get(machIndex);
	}

	public Object getColumnValue(String columnName) {
		return userItemMap.get(columnName);
	}

	public Object getColumnValue(int columnIndex) {
		ArrayList<String> keyArrayList = new ArrayList<>(userItemMap.keySet());
		String keyName = keyArrayList.get(columnIndex);
		return userItemMap.get(keyName);
	}

	public void putColumnValue(int columnIndex, Object columnValue) {
		String columnName = "";
		ArrayList<String> keyNameArray = new ArrayList<>(userItemMap.keySet());
		columnName = keyNameArray.get(columnIndex);
		
		userItemMap.put(columnName, columnValue);
	}
	
	public void putColumnValue(String columnName, Object columnValue) {
		userItemMap.put(columnName, columnValue);
	}

	public int getColumnIndex() {
		return COLUMN_INDEX;
	}
	
	public String toString() {
		String message = "";
		for(int i = 0; i < userItemMap.size(); i++)	{
			String columnName = getColumnName(i);
			String columnValue = getColumnValue(i).toString();
			message += "ColumnName :" + columnName + " ColumnValue :" + columnValue + "\n";
		}
		return message;
	}
	
	public DefaultUserItem(String[] columnNamesArray) {
		userItemMap = new LinkedHashMap<>();
		for(String columnName : columnNamesArray) {
			userItemMap.put(columnName, null);
		}
		COLUMN_INDEX = columnNamesArray.length;
	}	
}