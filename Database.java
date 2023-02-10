package guessTheNumberV2;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

public class Database {
	
	private final String DB_NAME = "GNumScores.db";
	private final String DB_LOCATION = "jdbc:sqlite:C:\\Users\\paulb\\OneDrive\\Desktop\\ProjectsDatabases\\" + DB_NAME;
	private final String TABLE_NAME = "Scores";
        private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(id INTEGER PRIMARY KEY NOT NULL," + 
	                                    "name TEXT NOT NULL,score INTEGER NOT NULL,"+
	                                    "maxround INTEGER NOT NULL,datetime TEXT NOT NULL)";
	private final String INSERT = "INSERT INTO " + TABLE_NAME + " VALUES(";
	private final String DATE_TIME = "datetime('now','localtime')";
	private final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME + " ";
	private final String SELECT_MAX_ID = "SELECT MAX(id) AS maxId FROM " + TABLE_NAME;
	private final String ORDER_BY_SCORE_ASC = "ORDER BY " + TABLE_NAME + "." + "score ASC";
	private final String ORDER_BY_SCORE_DESC = "ORDER BY " + TABLE_NAME + "." + "score DESC";
	private final String DELETE_FROM_TABLE = "DELETE FROM " + TABLE_NAME + " WHERE " + TABLE_NAME + ".id = ";
	private final String SELECT_ALL_WHERE_PLAYERNAME = "SELECT * FROM " + TABLE_NAME + " WHERE name = ";
	private int maxId = 0;
	//private final String QUERY_PLAYER = "SELECT * FROM " + TABLE_NAME + " WHERE "; 
	
	private Connection conn;
	
	public void displaymaxid() {
		System.out.println(this.maxId);
	}
	
	public void displayScores(int orderValue) {
		if(this.maxId == 0)
		{
			System.out.println("List is empty");
			return;
		}
		StringBuilder selectAll = new StringBuilder(SELECT_ALL);
		switch(orderValue)
		{
		case 1 : selectAll.append(ORDER_BY_SCORE_ASC);
		break;
		case 2 : selectAll.append(ORDER_BY_SCORE_DESC);
		break;
		default : //Default order(by id)
		}
			
		try(Statement statement = conn.createStatement();
			ResultSet results = statement.executeQuery(selectAll.toString())){
			System.out.println("id,name,score,maxround,datetime");
			while(results.next())
			{
				System.out.print(results.getInt(1) + "|");
				System.out.print(results.getString(2) + "|");
				System.out.print(results.getInt(3) + "|");
			    System.out.print(results.getInt(4) + "|");
				String datetime = results.getString(5);
				StringBuilder changedDateFormat = new StringBuilder();
				changedDateFormat.append(datetime.substring(8,10) + "/");
				changedDateFormat.append(datetime.substring(5,7) + "/");
				changedDateFormat.append(datetime.substring(0,4));
				changedDateFormat.append(datetime.substring(10, datetime.length()));
				System.out.print(changedDateFormat.toString());
				changedDateFormat.setLength(0);
				System.out.println();
			}
		}
		catch(SQLException e) {
			System.out.println("Querying failed:" + e.getMessage());
	}
		
	}
	
	public void displayPlayerScores(String name) {
		if(this.maxId == 0)
		{
			System.out.println("List is empty");
			return;
		}
		StringBuilder queryStatement = new StringBuilder(SELECT_ALL_WHERE_PLAYERNAME);
		queryStatement.append("\"" + name + "\"");
		try(Statement statement = conn.createStatement();
			ResultSet scores = statement.executeQuery(queryStatement.toString())){
			System.out.println("id,name,score,maxround,datetime");
			while(scores.next())
			{
				System.out.print(scores.getInt(1) + "|");
				System.out.print(scores.getString(2) + "|");
				System.out.print(scores.getInt(3) + "|");
				System.out.print(scores.getInt(4) + "|");
				String datetime = scores.getString(5);
				StringBuilder changedDateFormat = new StringBuilder();
				changedDateFormat.append(datetime.substring(8,10) + "/");
				changedDateFormat.append(datetime.substring(5,7) + "/");
				changedDateFormat.append(datetime.substring(0,4));
				changedDateFormat.append(datetime.substring(10, datetime.length()));
				System.out.print(changedDateFormat.toString());
				changedDateFormat.setLength(0);
				System.out.println();
			}	
		}
		catch(SQLException e) {
			System.out.println("Querying failed:" + e.getMessage());
		}
	}
	
	public void clearTable() {
		try(Statement statement = conn.createStatement()){
			for(int i = 1; i <= maxId; i++)
			{
				statement.execute(DELETE_FROM_TABLE + Integer.toString(i));
			}
			System.out.println("Table cleared succesfully");
		}
		catch(SQLException e) {
			System.out.println("Clearing failed:" + e.getMessage());
		}
	}
	
	//This method is used to retrieve the last id in consequence to calculate the next one.
	private void setMaxId() {
		StringBuilder sb = new StringBuilder(SELECT_MAX_ID);
		try(Statement statement = conn.createStatement();
			ResultSet maxId = statement.executeQuery(sb.toString())){
			this.maxId = maxId.getInt("maxId");
		}
		catch(SQLException e) {
			System.out.println("Finding max id failed:" + e.getMessage());
		}
	}
	
	
	
	public void insertInTable(String name,int score,int maxround) {
		if(name.isBlank())
		{
			System.out.println("Name must not be empty");
			return;
		}
		int nextId = this.maxId + 1;
		StringBuilder insertStatement = new StringBuilder(INSERT);
		insertStatement.append(nextId + ",\"" + name + "\",");
		insertStatement.append(score + "," + maxround + "," + DATE_TIME + ")");
	
		try(Statement statement = conn.createStatement()){
			statement.execute(insertStatement.toString());
			setMaxId();
			System.out.println("Player recorded");
		}
		catch(SQLException e) {
			System.out.println("Insertion failed:" + e.getMessage());
		}
	}
	
	public void createTable() {
		try(Statement statement = conn.createStatement()){
			statement.execute(CREATE_TABLE);
			setMaxId();
		}
		catch(SQLException e) {
			System.out.println(CREATE_TABLE);
			System.out.println("Table creation failed:" + e.getMessage());
		}
	}
	
	public void openDB() {
		try{
			conn = DriverManager.getConnection(DB_LOCATION);
			setMaxId();
		}
		catch(SQLException e) {
			System.out.println("Conection failed:" + e.getMessage());
		}
	}
	
	public void closeDB() {
		try{
			conn.close();
		}
		catch(SQLException e) {
			System.out.println("Closing failed:" + e.getMessage());
		}
	}
	
	
}
