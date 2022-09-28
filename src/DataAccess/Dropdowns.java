package DataAccess;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Dropdowns {
	public static ArrayList<String> GetStates() {
		return GetDropdownItems("Select * from state");
	}
	public static int GetStateCount() {
		return GetCount("Select count(*) from state");
	}
	public static ArrayList<String> GetFilteredStates(String filterCriteria) {
		return GetDropdownItems("Select * from state where name like '%" + filterCriteria + "%'"); //THIS IS VERY BAD NEVER DO THIS.
	}
	public static ArrayList<String> GetPaginatedStates(int offset, int limit) {
		return GetDropdownItems("Select * from state LIMIT " + offset + ", "+limit);
	}
	public static ArrayList<String> GetCountries() {
		return GetDropdownItems("Select * from country");
	}

	public static ArrayList<String> GetMakes() {
		return GetDropdownItems("Select * from vehiclemake");
	}

	public static ArrayList<String> GetModels() {
		return GetDropdownItems("Select * from vehiclemodel");
	}
	
	private static int GetCount(String sqlString) {
		ArrayList<String> results = new ArrayList<String>(); 
		DataAccess.SqlHelper sql = new DataAccess.SqlHelper();
		PreparedStatement stmt = sql.GetPreparedStatement(sqlString);
		ResultSet rs;
		try {
			rs = stmt.executeQuery();
			rs.next();
			return rs.getInt(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	
	private static ArrayList<String> GetDropdownItems(String sqlString) {
		ArrayList<String> results = new ArrayList<String>(); 
		DataAccess.SqlHelper sql = new DataAccess.SqlHelper();
		PreparedStatement stmt = sql.GetPreparedStatement(sqlString);
		ResultSet rs;
		try {
			rs = stmt.executeQuery();
			while (rs.next()) {
				results.add(rs.getString("name"));
			}
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
}
