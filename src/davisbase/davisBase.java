package davisbase;

import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.ArrayList;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


// @author : Aashaar Panchalan

public class davisBase {

	/* This can be changed to whatever you like */
	static String prompt = "davisql> ";
	static String version = "v1.0";
	static String copyright = "©2017 Aashaar Panchalan";
	static boolean isExit = false;
	static long pageSize = 512; 

	static Scanner scanner = new Scanner(System.in).useDelimiter(";");
	
	/** ***********************************************************************
	 *  Main method
	 */
    public static void main(String[] args) {

		/* Display the welcome screen */
		splashScreen();

		/* Variable to collect user input from the prompt */
		String userCommand = ""; 

		while(!isExit) {
			System.out.print(prompt);
			/* toLowerCase() renders command case insensitive */
			userCommand = scanner.next().replace("\n", "").replace("\r", "").trim().toLowerCase();
			// userCommand = userCommand.replace("\n", "").replace("\r", "");
			parseUserCommand(userCommand);
		}
		System.out.println("Exiting...");


	}

	/** ***********************************************************************
	
	 *  
	 *  Method definitions :
	 */

	/**
	 *  Display the splash screen
	 */
	public static void splashScreen() {
		System.out.println(line("-",80));
        System.out.println("Welcome to DavisBaseLite"); // Display the string.
		System.out.println("DavisBaseLite Version " + getVersion());
		System.out.println(getCopyright());
		System.out.println("\nType \"help;\" to display supported commands.");
		System.out.println(line("-",80));
	}
	
	/**
	 * @param s The String to be repeated
	 * @param num The number of time to repeat String s.
	 * @return String A String object, which is the String s appended to itself num times.
	 */
	public static String line(String s,int num) {
		String a = "";
		for(int i=0;i<num;i++) {
			a += s;
		}
		return a;
	}
	
		/**
		 *  Help: Display supported commands
		 */
		public static void help() {
			System.out.println(line("*",80));
			System.out.println("SUPPORTED COMMANDS");
			System.out.println("All commands below are case insensitive");
			System.out.println();
			System.out.println("\tCREATE TABLE table_name (<column_name> <datatype> primary key);			Create a new table.");
			System.out.println("\tINSERT INTO table_name VALUES (<value1>, <value2>);				Insert a new record.");
			System.out.println("\tSELECT * FROM table_name;                        				Display all records in the table.");
			System.out.println("\tSELECT * FROM table_name WHERE rowid = <value>;  				Display records whose rowid is <id>.");
			System.out.println("\tDELETE FROM TABLE table_name WHERE rowid = <value>;  				Deletes records whose rowid is <id>.");
			System.out.println("\tSHOW TABLES;                           						Displays tables present in the schema.");
			System.out.println("\tDROP TABLE table_name;                           				Remove table data and its schema.");
			System.out.println("\tVERSION;                                         				Show the program version.");
			System.out.println("\tHELP;                                            				Show this help information");
			System.out.println("\tEXIT;                                            				Exit the program");
			System.out.println("\tQUIT;                                            				Exit the program");
			System.out.println();
			System.out.println();
			System.out.println(line("*",80));
		}

	/** return the DavisBase version */
	public static String getVersion() {
		return version;
	}
	
	public static String getCopyright() {
		return copyright;
	}
	
	public static void displayVersion() {
		System.out.println("DavisBaseLite Version " + getVersion());
		System.out.println(getCopyright());
	}
		
	public static void parseUserCommand (String userCommand) {
		
		/* commandTokens is an array of Strings that contains one token per array element 
		 * The first token can be used to determine the type of command 
		 * The other tokens can be used to pass relevant parameters to each command-specific
		 * method inside each case statement */
		// String[] commandTokens = userCommand.split(" ");
		ArrayList<String> commandTokens = new ArrayList<String>(Arrays.asList(userCommand.split(" ")));

		/*
		*  This switch handles a very small list of hardcoded commands of known syntax.
		*  You will want to rewrite this method to interpret more complex commands. 
		*/
		switch (commandTokens.get(0)) {
			case "show":
				showTables();
				break;
			case "select":
				//parseQueryString(userCommand);
				if (userCommand.contains("where"))
				{
					String[] tokens = userCommand.split(" ");
					String tableName = tokens[3].trim();
					List<Column> columns = fetchColumns(tableName);
					for (Column c : columns)
					{
						System.out.print("  " +c.getColumnName().toUpperCase());
						//System.out.print(" ");
					}
					System.out.println("");
					List<String> selectedColumns = new ArrayList<String>();
					selectedColumns = selectWhere(userCommand);
					for(int i=0; i<selectedColumns.size();i++)
					{
						System.out.println(selectedColumns.get(i));
					}
				}
				else
				{
					//call select function
					selectAll(userCommand);
				}
				break;
			case "drop":
				dropTable(userCommand);
				break;
			case "create":
				// First check if the input query has valid datatypes or not.  
				checkDataType(userCommand);
				//parseCreateString(userCommand);
				break;
			case "delete":
				parseDeleteString(userCommand);
				break;
			case "insert":
				parseInsertString(userCommand);
				break;
			case "help":
				help();
				break;
			case "version":
				displayVersion();
				break;
			case "exit":
				isExit = true;
				break;
			case "quit":
				isExit = true;
			default:
				System.out.println("Error in command syntax: \"" + userCommand + "\"" + " Please refer HELP for sample commands.");
				break;
		}
	}
	

	/**
	 *  Stub method for dropping tables
	 *  @param dropTableString is a String of the user input
	 */
	public static void showTables() 
	{
		//System.out.println("STUB: Calling show tables to display all tables in the database");
		try {
			String workingDirectory = System.getProperty("user.dir"); // gets current working directory
			String absoluteFilePath = "";
			absoluteFilePath = workingDirectory + File.separator + "data" + File.separator + "catalog" + File.separator + "davisbase_tables.tbl";
			File file = new File(absoluteFilePath);
			/*Checking if davisbase.tables.tbl is present or not
			 * If it's not present it implies that database has no tables in the first place which is handled in the ELSE condition
			*/  
			if ((file.exists()) && (!file.isDirectory()))
			{
				RandomAccessFile catalogTableFile = new RandomAccessFile(absoluteFilePath, "rw");
				int tableCount = 0;
				if(catalogTableFile.length() > 0) // to handle exception thrown when davisbase_tables.tbl is newly created and has no data to execute readByte operation
				{
					tableCount = catalogTableFile.readByte();
				}
				// writing table data
				if(catalogTableFile.length() == 0) // to handle exception thrown when davisbase_tables.tbl is newly created and has no data to execute readByte operation
				{
					System.out.println("No tables currently present in the database.");
				}
				else
				{
					System.out.println("TABLE NAME");
					int isTableDeletedFlag = 0;
					catalogTableFile.seek(0);
					tableCount = catalogTableFile.readByte();
					for(int i=0;i<tableCount;i++)
					{
						isTableDeletedFlag = catalogTableFile.readByte();
						int attributeLength = catalogTableFile.readByte();
						byte[] bytes = new byte[attributeLength];
						catalogTableFile.read(bytes,0,attributeLength);
						String tableName = new String(bytes);
						if(isTableDeletedFlag != 1)  // check for flag after reading the record for the current table to get the pointer in position for the next table
						{
							System.out.println(tableName);
						}
					}
				}
				catalogTableFile.close();
				
			}
				else
			{
				System.out.println("No tables currently present in the database.");
				
			}
		} catch (Exception e) {
			System.out.println("Some error has occured. Please try again");
			
		}
		
	}
	
	/*
	 * Function to drop a table form the database
	*/
	public static void dropTable(String dropTableString) 
	{
		try 
		{	
			String[] tokens = dropTableString.split(" ");
			String tableName = tokens[2].trim();
			//System.out.println("Table name "+ tableName);
			if(checkTableExists(tableName)) // check if tableName.tbl is present or not
			{
				String workingDirectory = System.getProperty("user.dir");
				String filePath = workingDirectory + File.separator + "data" + File.separator + "user_data" + File.separator + tableName +".tbl";
				File fileDel = new File (filePath);
				
				if(fileDel.delete()) // file.delete() returns a boolean TRUE if executed without error hence the IF condition check
				{
					String absoluteFilePath = "";
					absoluteFilePath = workingDirectory + File.separator + "data" + File.separator + "catalog" + File.separator + "davisbase_tables.tbl";
					File file = new File(absoluteFilePath);
					// to check if the davisbase_tables.tbl exists or not - to handle the initial condition when this file is yet to be created
					if ((file.exists()) && (!file.isDirectory()))
					{
						RandomAccessFile catalogTableFile = new RandomAccessFile(absoluteFilePath, "rw");
						int tableCount = 0;
						if(catalogTableFile.length() > 0) // to handle exception thrown when davisbase_tables.tbl is newly created and has no data to execute readByte operation
						{
							tableCount = catalogTableFile.readByte();
						}
						// writing table data
						if(catalogTableFile.length() == 0) // to handle exception thrown when davisbase_tables.tbl is newly created and has no data to execute readByte operation
						{
							System.out.println("No tables currently present in the database.");
						}
						else
						{
							long recordStartLocation =0L;
							int isTableDeletedFlag = 0;
							boolean flag = false;
							catalogTableFile.seek(0);
							tableCount = catalogTableFile.readByte();
							for(int i=0;i<tableCount;i++)
							{
								recordStartLocation = catalogTableFile.getFilePointer();
								isTableDeletedFlag = catalogTableFile.readByte();
								int attributeLength = catalogTableFile.readByte();
								byte[] bytes = new byte[attributeLength];
								catalogTableFile.read(bytes,0,attributeLength);
								String currentTable = new String(bytes);
								if(tableName.equals(currentTable)) 
								{
									if(isTableDeletedFlag == 0)  // check for flag after reading the record for the current table to get the pointer in position for the next table
									{
										catalogTableFile.seek(recordStartLocation);
										catalogTableFile.writeByte(1);
										flag = true;
										deleteAllColumns(tableName); // calling function to delete all columns of the table
										break;
									}
								}
								
							}
							if(flag)
							{
								System.out.println("Table "+tableName+" deleted successfully.");
							}
							else
							{
								System.out.println("ERROR: in deleting the table. Please try again");
							}
						}
						
						catalogTableFile.close();
						
					}
						else
					{
						System.out.println("No tables currently present in the database.");
						
					}

				}
				else // if delete operation fails
				{
					System.out.println("Error in deleting the table. Please try again");
				}
				////
			}
		} 
		catch (Exception e) {
			System.out.println("Some error has occured. Please try again");
			System.out.println(e);
			
		}
	
	}
	
	public static void parseDeleteString(String queryString) {
		//System.out.println("STUB: Calling parseDeleteString(String s) to delete record");
		//System.out.println("Parsing the string:\"" + queryString + "\"");
		try 
			{
			String[] tokens = queryString.split(" ");
			String tableName = tokens[3].trim();
			if(checkTableExists(tableName))
			{
				//System.out.println("Table name "+ tableName);
				String condition = queryString.substring(queryString.indexOf("where") + 5, queryString.length()).trim();
				String[] conditionArray = condition.replaceAll("\\s+", "") .split("=");
				//System.out.println(conditionArray[0]);
				//System.out.println(conditionArray[1]);
				
				String workingDirectory = System.getProperty("user.dir");
				String absoluteFilePath = workingDirectory + File.separator + "data" + File.separator + "user_data" + File.separator + tableName +".tbl";
				RandomAccessFile tableFile = new RandomAccessFile(absoluteFilePath, "rw");
				List<Column> columns = fetchColumns(tableName);
				
				// ****************************** paging code starts *****************************
				int firstByteLocation = 0;
				int numRecords = 0;
				int nextNodeLocation = 0;
				ArrayList<Short> recordStartPointers = new ArrayList<Short>();
				
				do 
				{
					tableFile.seek(firstByteLocation);
					numRecords = tableFile.readByte();
					tableFile.readShort();
					nextNodeLocation = tableFile.readInt();
					for(int i=0; i<numRecords;i++) 
					{
						recordStartPointers.add(Short.valueOf(tableFile.readShort()));
					}
					if (nextNodeLocation != -1)
					{
						firstByteLocation = firstByteLocation +512; // increments of 512 for pointers of the file
					}
				}
				while(nextNodeLocation != -1);
				
				// ******************************* paging code end *****************************
				
				Boolean recordFound = false;
				for(int j=0; j<recordStartPointers.size();j++)
				{
				//	System.out.println(( recordStartPointers.get(j)).shortValue());
					tableFile.seek(( recordStartPointers.get(j)).shortValue());
					int isDeleted = tableFile.readByte(); // reading the isDeleted byte of the record
					Boolean deleteFlag = false;
					String value = "";
					if (isDeleted == 0) // record is present and not deleted previously.
					{	
						for (int i=0; i<columns.size();i++)
						{
							String dataType= columns.get(i).getDataType();
							switch(dataType)
							{
								case "tinyint":
									value = "" + tableFile.readByte() ;
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										deleteFlag = true;
									}
									//System.out.print("  " + tableFile.readByte());
									break;
								case "smallint":
									value = "" + tableFile.readShort();
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										deleteFlag = true;
									}
									//System.out.print("  " + tableFile.readShort());
									break;
								case "int":
									value = "" + tableFile.readInt();
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										deleteFlag = true;
									}
									//System.out.print("  " + tableFile.readInt());
									break;
								case "bigint":
									value = "" + tableFile.readLong();
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										deleteFlag = true;
									}
									//System.out.print("  " + tableFile.readLong());
									break;
								case "real":
									value = "" + tableFile.readFloat();
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										deleteFlag = true;
									}
									//System.out.print("  " + tableFile.readFloat());
									break;
								case "double":
									value = "" + tableFile.readDouble();
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										deleteFlag = true;
									}
									//System.out.print("  " + tableFile.readDouble());
									break;
								case "datetime":
									value = "" + convertDateTimeToString(tableFile.readLong());
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										deleteFlag = true;
									}
									//System.out.print("  " + convertDateTimeToString(tableFile.readLong()));
									break;
								case "date":
									value = "" + convertDateToString(tableFile.readLong());
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										deleteFlag = true;
									}
									//System.out.print("  " + convertDateToString(tableFile.readLong()));
									break;
								default:									
									int attributeLength = tableFile.readByte();
									byte[] bytes = new byte[attributeLength];
									tableFile.read(bytes,0,attributeLength);
									String dataString = new String(bytes);
									//System.out.print("  "+ dataString);
									value = dataString;
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										deleteFlag = true;
									}
							}
							
						}
						if(deleteFlag)
						{
							//TODO : update DeleteByte of the record
							tableFile.seek(( recordStartPointers.get(j)).shortValue());
							tableFile.writeByte(1);
							System.out.println("Record deleted successfully");
							recordFound = true;
						}
						
					}
									
				}
				if(!recordFound)
				{
					System.out.println("Record not found");
				}
	
				tableFile.close();
		
			}
		}
		catch (Exception e)
		{
			System.out.println("Error in deleting record");
			System.out.println(e);
		}
	}
	
	public static void parseInsertString(String queryString) {
		//System.out.println("STUB: Calling parseInsertString(String s) to insert record");
		//System.out.println("Parsing the string:\"" + queryString + "\"");
		try 
		{
			boolean errorFlag = false;
			String errorMsg = "";
			int recordLength = 1; // initialize it to 1 to include for the isDeleted flag of the record
			ArrayList<String> createTableTokens = new ArrayList<String>(Arrays.asList(queryString.split(" ")));
			String columnInfo = queryString.substring(queryString.indexOf("(") + 1, queryString.length() - 1);
			//System.out.println("Result: "+ columnInfo);
			String[] values = columnInfo.replaceAll("\''","null").replaceAll("\'","").trim().replaceAll("\\s+","").split(",");
			/*// just for printing :
			System.out.println("Table Name: "+createTableTokens.get(3));
			for(int i=0; i<values.length;i++)
			{
				System.out.println("---"+values[i]);
			}*/
			
			String tableName = createTableTokens.get(2);
			if(checkTableExists(tableName))
			{
				List<Column> columnList = new java.util.ArrayList<Column>();
				columnList = fetchColumns(tableName);
				/*//check if fetchColumn is working properly
				for (Column c : columnList)
				{
					System.out.println(c.getColumnName() + " - " + c.getDataType()+ " PrimaryKey- "+c.isPrimaryKey()+ "notNullable -"+ c.isNotNullable());
				}*/
				
				if(values.length == columnList.size()) // Checking the values in INSERT query are no. of columns of the table or not, if not--> Reject insertion
				{
					int i = 0;
					while (i<values.length)
					{
						if(columnList.get(i).isPrimaryKey() || columnList.get(i).isNotNullable())
						{
							
							if(values[i] == null || values[i] == "null" || values[i].equals("null"))
							{
								errorFlag = true;
								errorMsg = "ERROR: "+columnList.get(i).getColumnName()+ " cannot be null.";
							}
							if(columnList.get(i).isPrimaryKey())
							{
								String queryStringCheckPK= "select * from "+tableName+" where "+columnList.get(i).getColumnName()+"="+ values[i];
								//System.out.println(queryStringCheckPK);
								List<String> selectedColumns = new ArrayList<String>();
								selectedColumns = selectWhere(queryStringCheckPK);
								if (selectedColumns.size()>0)
								{
									errorFlag = true;
									errorMsg = "ERROR: Primary key already exists in the table.";
								}
							}
						}
						
						if(!errorFlag)
						{
							if(columnList.get(i).getDataType().equals("tinyint"))
							{
								recordLength++;
							}
							else if(columnList.get(i).getDataType().equals("smallint"))
							{
								recordLength += 2;
							}
							else if(columnList.get(i).getDataType().equals("int"))
							{
								recordLength += 4;
							}
							else if(columnList.get(i).getDataType().equals("bigint"))
							{
								recordLength += 8;
							}
							else if(columnList.get(i).getDataType().equals("real"))
							{
								recordLength += 4;
							}
							else if(columnList.get(i).getDataType().equals("double"))
							{
								recordLength += 8;
							}
							else if(columnList.get(i).getDataType().equals("datetime"))
							{
								recordLength += 8;
							}
							else if(columnList.get(i).getDataType().equals("date"))
							{
								recordLength += 8;
							}
							else
							{
								recordLength += values[i].length() + 1; 
								// adding 1 since to store the length of the record
							}
							// use  recordLength to check if page has size for this record or not. if not move to new page.
						}
						
						i++;
					} //while loop ends
					
					if(!errorFlag)
					{
						String workingDirectory = System.getProperty("user.dir");
						String absoluteFilePath = workingDirectory + File.separator + "data" + File.separator + "user_data" + File.separator + tableName +".tbl";
						RandomAccessFile tableFile = new RandomAccessFile(absoluteFilePath, "rw");
						
						int firstByteLocation = 0;
						int numRecords = 0;
						int startLocation = 0;
						int nextNodeLocation = 0;
						int lastPointerLocation = 0;
						int difference =0;
		
						while (recordLength > difference)
						{
							
							tableFile.seek(firstByteLocation);
							numRecords = tableFile.readByte();
							startLocation = tableFile.readShort();
							nextNodeLocation = tableFile.readInt();
							while(nextNodeLocation != -1)
							{
								firstByteLocation = firstByteLocation +512;
								tableFile.seek(firstByteLocation);
								numRecords = tableFile.readByte();
								startLocation = tableFile.readShort();
								nextNodeLocation = tableFile.readInt();
								
							}
							lastPointerLocation = (((int) tableFile.getFilePointer()) + (numRecords*2));   // LONG datatype used instead of INT
							difference = startLocation - lastPointerLocation;
							if (recordLength > difference)
							{
								tableFile.seek(firstByteLocation); // Initial condition : tableFile.seek(0)
								tableFile.readByte();
								tableFile.readShort();
								tableFile.writeInt((int)tableFile.length()) ;
								//firstByteLocation = (int) pageSize +512; // ************************* firstByteLocation +512
								firstByteLocation = firstByteLocation +512; // increments of 512 for pointers of the file
								tableFile.setLength(tableFile.length() *2L);  // pageSize doubles
								//System.out.println(tableFile.length());
								// startLocation = (int) tableFile.length(); or startlocation +512;
								tableFile.seek(firstByteLocation);
								//System.out.println(tableFile.getFilePointer());
								tableFile.readByte();
								//System.out.println(tableFile.getFilePointer());
								//tableFile.writeShort(firstByteLocation + 512);
								tableFile.writeShort((short)tableFile.length());
								//System.out.println(tableFile.getFilePointer());
								// re-calculating in case overflow occurred
								tableFile.seek(firstByteLocation); //initial position of pointer is always -1
								numRecords = tableFile.readByte();
								startLocation = tableFile.readShort();
								tableFile.writeInt(-1);
								lastPointerLocation = (((int) tableFile.getFilePointer()) + (numRecords*2));   // LONG datatype used instead of INT
								difference = startLocation - lastPointerLocation;
								
								
							}
							
						}
						
							// If no overflow:  insert code starts
							int insertLocation = startLocation - recordLength ; //+1
							tableFile.seek(insertLocation);
							tableFile.writeByte(0); // Initialize the isDeleted flag of the record to 0
							i=0; 
							while (i<values.length)
							{
								if(columnList.get(i).getDataType().equals("tinyint"))
								{
									tableFile.writeByte(Byte.parseByte(values[i]));
								}
								else if(columnList.get(i).getDataType().equals("smallint"))
								{
									tableFile.writeInt(Short.parseShort(values[i]));
								}
								else if(columnList.get(i).getDataType().equals("int"))
								{
									tableFile.writeInt(Integer.parseInt(values[i]));
								}
								else if(columnList.get(i).getDataType().equals("bigint"))
								{
									tableFile.writeLong(Long.parseLong(values[i]));
								}
								else if(columnList.get(i).getDataType().equals("real"))
								{
									tableFile.writeFloat(Float.parseFloat(values[i]));
								}
								else if(columnList.get(i).getDataType().equals("double"))
								{
									tableFile.writeDouble(Double.parseDouble(values[i]));
								}
								else if(columnList.get(i).getDataType().equals("datetime"))
								{
									tableFile.writeLong(Long.parseLong(values[i]));
								}
								else if(columnList.get(i).getDataType().equals("date"))
								{
									tableFile.writeLong(convertStringToDate(values[i]));
								}
								else
								{
									tableFile.writeByte(values[i].length());
									tableFile.writeBytes(values[i]);
								}
							
								i++;
							} // while loop ends
							
							// code to increment the no. of records in the page/file after inserting a new record				
							numRecords++;
							tableFile.seek(firstByteLocation);
							tableFile.writeByte(numRecords); 
							
							/*startLocation = insertLocation - 1;
							tableFile.writeShort(startLocation);*/
							tableFile.writeShort(insertLocation);
							//tableFile.seek(0); // <<<---- this line 
							/*tableFile.seek(7+((numRecords-1)*2)); // remove hardcoded 7 -  start from startLocation and read the 7 bytes <<<---- this line 
							for(int m=0;m<(7+((numRecords-1)*2));m++)
							{
								tableFile.readByte();
							}*/
							tableFile.seek(lastPointerLocation);
							//tableFile.writeShort(startLocation);
							tableFile.writeShort(insertLocation);
							tableFile.close();
							System.out.println("Record inserted successfully in the table.");
						
					}
					else
					{
						System.out.println(errorMsg);
					}
					
					
				}
				else
				{
					System.out.println("ERROR: No. of attributes in input query do not match with no. of columns of the table.");
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("ERROR : Insertion of record failed due to an error.");
			System.out.println(e.getStackTrace());
			System.out.println(e);
		}
		
	}
	
	public static List<String> selectWhere(String queryString) 
	{
		//System.out.println("STUB: Calling parseQueryString(String s) to process queries for SELECT statement");
		//System.out.println("Parsing the string:\"" + queryString + "\"");
		List<String> selectedColumns = new ArrayList<String>();
		try
		{
			//System.out.println("STUB: Call SelectWhere method");
			String[] tokens = queryString.split(" ");
			String tableName = tokens[3].trim();
			
			//System.out.println("Table name "+ tableName);
			if(checkTableExists(tableName))
			{
				String condition = queryString.substring(queryString.indexOf("where") + 5, queryString.length()).trim();
				String[] conditionArray = condition.replaceAll("\\s+", "").split("=");
				//System.out.println(conditionArray[0]);
				//System.out.println(conditionArray[1]);
				
				String workingDirectory = System.getProperty("user.dir");
				String absoluteFilePath = workingDirectory + File.separator + "data" + File.separator + "user_data" + File.separator + tableName +".tbl";
				RandomAccessFile tableFile = new RandomAccessFile(absoluteFilePath, "rw");
				List<Column> columns = fetchColumns(tableName);
				/*for (Column c : columns)
				{
					System.out.print("  " +c.getColumnName().toUpperCase());
					//System.out.print(" ");
				}
				System.out.println("");*/
				//System.out.println("=========");
				// ****************************** paging code starts *****************************
				int firstByteLocation = 0;
				int numRecords = 0;
				int nextNodeLocation = 0;
				ArrayList<Short> recordStartPointers = new ArrayList<Short>();
				
				do 
				{
					tableFile.seek(firstByteLocation);
					numRecords = tableFile.readByte();
					tableFile.readShort();
					nextNodeLocation = tableFile.readInt();
					for(int i=0; i<numRecords;i++) 
					{
						recordStartPointers.add(Short.valueOf(tableFile.readShort()));
					}
					if (nextNodeLocation != -1)
					{
						firstByteLocation = firstByteLocation +512; // increments of 512 for pointers of the file
					}
				}
				while(nextNodeLocation != -1);
				
				// ******************************* paging code end *****************************
				
				for(int j=0; j<recordStartPointers.size();j++)
				{
				//	System.out.println(( recordStartPointers.get(j)).shortValue());
					tableFile.seek(( recordStartPointers.get(j)).shortValue());
					int isDeleted = tableFile.readByte(); // reading the isDeleted byte of the record
					if(isDeleted == 0)
					{
						Boolean printFlag = false;
						String outputString = "  ";
						String value = "";
						for (int i=0; i<columns.size();i++)
						{
							String dataType= columns.get(i).getDataType();
							switch(dataType)
							{
								case "tinyint":
									value = "" + tableFile.readByte() ;
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										printFlag = true;
									}
									outputString = outputString + " " + value;
									//System.out.print("  " + tableFile.readByte());
									break;
								case "smallint":
									value = "" + tableFile.readShort();
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										printFlag = true;
									}
									outputString = outputString + " " + value;									
									//System.out.print("  " + tableFile.readShort());
									break;
								case "int":
									value = "" + tableFile.readInt();
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										printFlag = true;
									}
									outputString = outputString + " " + value;									
									//System.out.print("  " + tableFile.readInt());
									break;
								case "bigint":
									value = "" + tableFile.readLong();
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										printFlag = true;
									}
									outputString = outputString + " " + value;
									//System.out.print("  " + tableFile.readLong());
									break;
								case "real":
									value = "" + tableFile.readFloat();
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										printFlag = true;
									}
									outputString = outputString + " " + value;
									//System.out.print("  " + tableFile.readFloat());
									break;
								case "double":
									value = "" + tableFile.readDouble();
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										printFlag = true;
									}
									outputString = outputString + " " + value;
									//System.out.print("  " + tableFile.readDouble());
									break;
								case "datetime":
									value = "" + convertDateTimeToString(tableFile.readLong());
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										printFlag = true;
									}
									outputString = outputString + " " + value;
									//System.out.print("  " + convertDateTimeToString(tableFile.readLong()));
									break;
								case "date":
									value = "" + convertDateToString(tableFile.readLong());
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										printFlag = true;
									}
									outputString = outputString + " " + value;
									//System.out.print("  " + convertDateToString(tableFile.readLong()));
									break;
								default:									
									int attributeLength = tableFile.readByte();
									byte[] bytes = new byte[attributeLength];
									tableFile.read(bytes,0,attributeLength);
									String dataString = new String(bytes);
									//System.out.print("  "+ dataString);
									value = dataString;
									if (columns.get(i).getColumnName().equals(conditionArray[0]) && value.equals(conditionArray[1])) 
									{
										printFlag = true;
									}
									outputString = outputString + "  " + value;
							}
							
							
						}
						if(printFlag)
						{
							//System.out.println(outputString);
							selectedColumns.add(outputString) ; 
						}
					}
				}
				tableFile.close();
				
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return selectedColumns;
	}
	
	public static void selectAll(String queryString) 
	{
		//code for--> SELECT * FROM table_name:
		try
		{
			
			//System.out.println("STUB: Call just the Select method");
			String[] tokens = queryString.split(" ");
			String tableName = tokens[3].trim();
			//System.out.println("Table name "+ tableName);
			if(tableName.equals("davisbase_tables")) // to handle : SELECT * FROM davisbase_tables;
			{
				showTables();
			}
			else if(checkTableExists(tableName)) // for the user tables
			{
				String workingDirectory = System.getProperty("user.dir");
				String absoluteFilePath = workingDirectory + File.separator + "data" + File.separator + "user_data" + File.separator + tableName +".tbl";
				RandomAccessFile tableFile = new RandomAccessFile(absoluteFilePath, "rw");
				List<Column> columns = fetchColumns(tableName);
				for (Column c : columns)
				{
					System.out.print("  " +c.getColumnName().toUpperCase());
					//System.out.print(" ");
				}
				System.out.println("");
				//System.out.println("=========");
				
				// ****************************** paging code starts *****************************
				int firstByteLocation = 0;
				int numRecords = 0;
				int nextNodeLocation = 0;
				ArrayList<Short> recordStartPointers = new ArrayList<Short>();
				
				do 
				{
					tableFile.seek(firstByteLocation);
					numRecords = tableFile.readByte();
					tableFile.readShort();
					nextNodeLocation = tableFile.readInt();
					for(int i=0; i<numRecords;i++) 
					{
						recordStartPointers.add(Short.valueOf(tableFile.readShort()));
					}
					if (nextNodeLocation != -1)
					{
						firstByteLocation = firstByteLocation +512; // increments of 512 for pointers of the file
					}
				}
				while(nextNodeLocation != -1);
				
				// ******************************* paging code end *****************************

				for(int j=0; j<recordStartPointers.size();j++)
				{
				//	System.out.println(( recordStartPointers.get(j)).shortValue());
					tableFile.seek(( recordStartPointers.get(j)).shortValue());
					int isDeleted = tableFile.readByte(); // reading the isDeleted byte of the record
					if(isDeleted == 0)
					{
						for (int i=0; i<columns.size();i++)
						{
							String dataType= columns.get(i).getDataType();
							switch(dataType)
							{
								case "tinyint":
									System.out.print("  " + tableFile.readByte());
									break;
								case "smallint":
									System.out.print("  " + tableFile.readShort());
									break;
								case "int":
									System.out.print("  " + tableFile.readInt());
									break;
								case "bigint":
									System.out.print("  " + tableFile.readLong());
									break;
								case "real":
									System.out.print("  " + tableFile.readFloat());
									break;
								case "double":
									System.out.print("  " + tableFile.readDouble());
									break;
								case "datetime":
									System.out.print("  " + convertDateTimeToString(tableFile.readLong()));
									break;
								case "date":
									System.out.print("  " + convertDateToString(tableFile.readLong()));
									break;
								default:
									int attributeLength = tableFile.readByte();
									byte[] bytes = new byte[attributeLength];
									tableFile.read(bytes,0,attributeLength);
									String dataString = new String(bytes);
									System.out.print("  "+ dataString);
							}
							
						}
						System.out.println();
					}
				}
				tableFile.close();
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		
	
	}
	
	public static void parseCreateString(String createTableString) {
		
		//System.out.println("STUB: Calling your method to create a table");
		//System.out.println("Parsing the string:\"" + createTableString + "\"");
		ArrayList<String> createTableTokens = new ArrayList<String>(Arrays.asList(createTableString.split(" ")));

		/* Define table file name */
		String tableName = createTableTokens.get(2);
		String tableFileName = createTableTokens.get(2) + ".tbl";
		//System.out.println("Table filename: "+tableFileName);
		
		try 
		{
			String workingDirectory = System.getProperty("user.dir"); // gets current working directory
			String absoluteFilePath = "";
			absoluteFilePath = workingDirectory + File.separator + "data" + File.separator + "user_data" + File.separator + tableFileName;
			File file = new File(absoluteFilePath);
			
			// If tablename already exists --> Reject creation
			if ((file.exists()) && (!file.isDirectory())) 
			{
				System.out.println("ERROR: Table with name '"+tableName+"' already exists in the database. Please try again with a different name.");
				
			}
			else // tablename does not exist already --> Create the table
			{
				try
				{
					RandomAccessFile tableFile = new RandomAccessFile(absoluteFilePath, "rw");
					tableFile.setLength(pageSize);
					tableFile.seek(0);
					tableFile.writeByte(0); // initial no. of records
					tableFile.writeShort(512); // initial pointer location i.e. end of file
					tableFile.writeInt(-1); // address of next leaf -->  decimal(-1) = hex(FF FF FF FF)
					
					tableFile.close();
								
					//To enter table details in catalog tables
					
					// Code to insert a row in davisbase_tables.tbl
					
					String filePath = workingDirectory + File.separator + "data" + File.separator + "catalog" + File.separator + "davisbase_tables.tbl";
					//System.out.println("Catalog tables filepath : " + filePath);
					
					RandomAccessFile catalogTableFile = new RandomAccessFile(filePath, "rw");
					catalogTableFile.seek(0);
					int tableCount = 0;
					if(catalogTableFile.length() > 0) // to handle exception thrown when davisbase_tables.tbl is newly created and has no data to execute readByte operation
					{
						tableCount = catalogTableFile.readByte();
					}
					// writing table data
					if(catalogTableFile.length() == 0) // to handle exception thrown when davisbase_tables.tbl is newly created and has no data to execute readByte operation
					{
						catalogTableFile.seek(catalogTableFile.length()+1L);
					}
					else
					{
						catalogTableFile.seek(catalogTableFile.length());
					}
					catalogTableFile.writeByte(0); // Delete flag of the table
					catalogTableFile.writeByte(tableName.length()); // length of table name
					catalogTableFile.write(tableName.getBytes()); // table name
					// to write incremented table count in the davisbase_tables.tbl
					catalogTableFile.seek(0); 
					tableCount++;
					catalogTableFile.writeByte(tableCount);
					catalogTableFile.close();
					
					
					//Code to insert a row in davisbase_columns.tbl
					
					createTableString = createTableString.replace('(', '@').replace(',', '@').replace(')', ' ').trim();
					List<String> colList = new ArrayList<String>();
					colList = Arrays.asList(createTableString.split("\\@"));
					
					filePath = workingDirectory + File.separator + "data" + File.separator + "catalog" + File.separator + "davisbase_columns.tbl";
					//System.out.println("Catalog columns filepath : " + filePath);
					RandomAccessFile catalogColumnFile = new RandomAccessFile(filePath, "rw");
					catalogColumnFile.seek(catalogColumnFile.length());
					//for (String col1 : colList)
					for(int i=0; i<colList.size();i++)
					{ 
						String col1 = colList.get(i);
						col1 = col1.trim();
						if(!col1.contains("create")) // to remove the 1st array element containing string "create table <tablename>"
						{
							if ( (!col1.isEmpty()) && (col1 != null)) 
							{
								//catalogColumnFile.writeByte(0);
								if (col1.contains("primary key")) 
								{
									col1 = col1.replace("primary key", "primarykey"); // remove space between words to enable parsing later
								}
								if (col1.contains("not nullable")) 
								{
									col1 = col1.replace("not nullable", "notnullable"); //// remove space between words to enable parsing later
								}
								catalogColumnFile.writeByte(0); // isDeletedFlag of the column
								String columnData = tableName + "@"	+ col1.replaceAll("  ", " ").replaceAll(" ", "@").trim();
								catalogColumnFile.writeByte(columnData.length());
								catalogColumnFile.writeBytes(columnData);
							}
						}
					}
					catalogColumnFile.close();
					System.out.println("Table created successfully.");
				}
				catch(Exception e) 
				{
					e.printStackTrace();
					System.out.println(e);
				}
			}
		} 
		catch (Exception e) {
			System.out.println("Some error has occured. Please try again");
		}
		
		
		
		
		/*  Code to create a .tbl file to contain table data */
		
	}
	
	public static void deleteAllColumns (String tableName)
	{
		try
		{
			int isColumnDeleted=0;
			String workingDirectory = System.getProperty("user.dir"); // gets current working directory
			String filePath = workingDirectory + File.separator + "data" + File.separator + "catalog" + File.separator + "davisbase_columns.tbl";
			
			File file = new File(filePath);
			// to check if the davisbase_tables.tbl exists or not - to handle the initial condition when this file is yet to be created
			if ((file.exists()) && (!file.isDirectory()))
			{
				RandomAccessFile catalogColumnFile = new RandomAccessFile(filePath, "rw");
				long fileLength = catalogColumnFile.length();
				long recordStartLocation =0L;
					while (catalogColumnFile.getFilePointer() < fileLength) 
					{
						recordStartLocation = catalogColumnFile.getFilePointer();
						isColumnDeleted = catalogColumnFile.readByte();
						byte length = catalogColumnFile.readByte();
						byte[] bytes = new byte[length];
						catalogColumnFile.read(bytes, 0, bytes.length);
						String[] column = new String(bytes).replaceAll("@", " ").split(" ");
						if ((column[0].equals(tableName)))
						{
							if(isColumnDeleted == 0)  // check for flag after reading the record for the current table to get the pointer in position for the next table
							{
								catalogColumnFile.seek(recordStartLocation);
								catalogColumnFile.writeByte(1);
								// reading the column again to take the pointer to end of the record to read next record
								catalogColumnFile.readByte();
								catalogColumnFile.read(bytes, 0, bytes.length);
							}
						}
					}
			}
			else
			{
				System.out.println("No tables currently present in the database.");
			}
			
			
		}
		catch (Exception e)
		{
			System.out.println("Error in deleting the table. Please try again");
			//System.out.println(e);
		}
	}
		
	public static List<Column> fetchColumns(String tableName) {
		List<Column> columnsList = new java.util.ArrayList<Column>();
		try {
			
			// TODO: check if table is present, if not abort here and throw an error
			int isDeleted =0;
			String workingDirectory = System.getProperty("user.dir"); // gets current working directory
			String filePath = workingDirectory + File.separator + "data" + File.separator + "catalog" + File.separator + "davisbase_columns.tbl";
			//System.out.println("Catalog columns filepath : " + filePath);
			RandomAccessFile catalogColumnFile = new RandomAccessFile(filePath, "rw");
			long fileLength = catalogColumnFile.length();
				while (catalogColumnFile.getFilePointer() < fileLength) 
				{
					isDeleted = catalogColumnFile.readByte();
					byte length = catalogColumnFile.readByte();
					byte[] bytes = new byte[length];
					catalogColumnFile.read(bytes, 0, bytes.length);
					String[] column = new String(bytes).replaceAll("@", " ").split(" ");
					if ((column[0].equals(tableName)))
					{
						Column col = new Column();
						col.setColumnName(column[1]);
						col.setDataType(column[2]);
						if (column.length == 4) 
						{
							if (column[3].equals("primarykey")) {
								col.setPrimaryKey(true);
								col.setNotNullable(false);
							}
							else if (column[3].equals("notnullable")) {
								col.setNotNullable(true);
								col.setPrimaryKey(false);
							}
							else
							{
								col.setNotNullable(false);
								col.setPrimaryKey(false);
							}
						}
						if(isDeleted == 0)
						{
							columnsList.add(col);
						}
					}
				}
				catalogColumnFile.close();
			
		} catch (Exception e) {
			System.out.println("Error");
		}

		return columnsList;
	}
		
	public static String convertDateTimeToString(long date) 
	{
		String datePattern = "YYYY-MM-DD_hh:mm:ss";
		SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
		Date d = new Date(date);
		return dateFormat.format(d);
	}
	
	public static String convertDateToString(long date) 
	{
		String datePattern = "MM:dd:yyyy";
		SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
		Date d = new Date(date);
		return dateFormat.format(d);
	}

	public static long convertStringToDate(String dateString) 
	{
		String datePattern = "MM:dd:yyyy";
		SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
		try {
			Date date = dateFormat.parse(dateString);
			return date.getTime();
		} catch (Exception e) {
			System.out.println(e);
		}
		return new Date().getTime();
	}
		
	public static boolean checkTableExists(String tableName)
	{
		try {
			String workingDirectory = System.getProperty("user.dir"); // gets current working directory
			String absoluteFilePath = "";
			absoluteFilePath = workingDirectory + File.separator + "data" + File.separator + "user_data" + File.separator + tableName+ ".tbl";
			File file = new File(absoluteFilePath);
			if ((file.exists()) && (!file.isDirectory()))
			{
				//System.out.println(tableName+" exists");
				return true;
			}
				else
			{
				System.out.println("ERROR: Table '" + tableName + "' does not exist in the database.");
				return false;
			}
		} catch (Exception e) {
			System.out.println("Some error has occured. Please try again");
			return false;
		}

		
	}
	
	public static void checkDataType (String createTableString) 
	{
		String condition = createTableString.substring(createTableString.indexOf("(")+1, createTableString.indexOf(")")).trim();
		String[] conditionArray = condition.trim().split(",");
		String [] dataTypes = {"tinyint","smallint","int","bigint","real","double","datetime","date", "text"};
		Boolean errorFlag = false;
		for(int i=0; i<conditionArray.length;i++)
		{
			String [] testarray = conditionArray[i].trim().split("\\s+");
			
			//System.out.println(testarray[1]);
			if (!Arrays.asList(dataTypes).contains(testarray[1]))
			{
				errorFlag = true;
			}
			
		}
		
		if(errorFlag) // one or more than datatypes are invalid. refer the array "dataTypes"
		{
			System.out.println("ERROR: Invalid datatype in the query. Please try again");
		}
		else // datatypes are fine, call create function
		{
			parseCreateString(createTableString);
			//System.out.println("Datatypes OK!");
		}
	}

}
