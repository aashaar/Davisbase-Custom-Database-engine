package davisbase;

public class Column {
	
	private String columnName;
	private String dataType;
	private boolean isPrimaryKey;
	private boolean isNotNullable;
	
	
	public Column() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Column(String columnName, String dataType, boolean isPrimaryKey, boolean isNotNullable) {
		super();
		this.columnName = columnName;
		this.dataType = dataType;
		this.isPrimaryKey = isPrimaryKey;
		this.isNotNullable = isNotNullable;
	}


	public String getColumnName() {
		return columnName;
	}


	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}


	public String getDataType() {
		return dataType;
	}


	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}


	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}


	public boolean isNotNullable() {
		return isNotNullable;
	}


	public void setNotNullable(boolean isNotNullable) {
		this.isNotNullable = isNotNullable;
	}


		

}
