package DataAccess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;

import Util.JDBCUrlBuilder;

/**
 * Rebuilds the schema and seeds the database.
 */
public class Seeder {
	private String connectionUrl;

	public Seeder() {
		String databaseName = ConfigurationReader.DATABASE_NAME;
		String databasePort = ConfigurationReader.DATABASE_PORT;
		String connectionUrl = ConfigurationReader.CONNECTION_URL;
		String databaseUsername = ConfigurationReader.DATABASE_USERNAME;
		String databasePassword = ConfigurationReader.DATABASE_PASSWORD;
		String[] fieldsToFetch = new String[5];
		
		fieldsToFetch[0] = databaseName;
		fieldsToFetch[1] = databasePort;
		fieldsToFetch[2] = databaseUsername;
		fieldsToFetch[3] = databasePassword;
		fieldsToFetch[4] = connectionUrl;
		
		Hashtable<String, String> configuration = new ConfigurationReader("config.xml").getConfiguration(fieldsToFetch);

		this.connectionUrl = JDBCUrlBuilder.build(
			configuration.get(connectionUrl),
			configuration.get(databasePort),
			configuration.get(databaseName),
			configuration.get(databaseUsername),
			configuration.get(databasePassword)
		);
	}
	
	
	/**
	 * Removes data and seeds database
	 * @return String Result of execution.  Empty string means success.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public void seedDatabase() {
		//String seedsSql = getFileContents("sql/seeds.sql");
		
		var file = "sql/seeds.sql";
		Connection connection=null;
		try {
			connection = getConnection();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    try {
				while ((line = br.readLine()) != null) {
				   runSeedStatement(line, connection);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}


	private int runSeedStatement(String seedSqlLine, Connection connection) {
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(seedSqlLine);
		} catch (Exception e) {
			System.out.println("Error connecting to database: " + e);
			return 0;
		}
		
		int rowCount;
		try {
			rowCount = statement.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error seeding the database: " + e);
			return 0;
		}
		
		return rowCount;
	}
	
	/**
	 * Gets the connection for the database to seed.
	 * 
	 * @return Connection The connection to seed with.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private Connection getConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return DriverManager.getConnection(this.connectionUrl);
	}

	private String getFileContents(String filePath) {
		try {
			return new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			System.out.println("Error reading from: " + filePath);
		}

		return "";
	}
}
