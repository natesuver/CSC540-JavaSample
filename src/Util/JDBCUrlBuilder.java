package Util;

public class JDBCUrlBuilder {
	
	/**
	 * Builds a JDBC URL for MySQL
	 * 
	 * @param connectionUrl The database connectionUrl
	 * @param port The database port
	 * @param name The database name
	 * @param username The database user name
	 * @param password The database password
	 */
	public static String build(String connectionUrl, String port, String name, String username, String password) {
		return "jdbc:mysql://" +
				connectionUrl + ":" +
				port + "/" +
				name +
				"?user=" + username +
				"&password=" + password;
	}
}
