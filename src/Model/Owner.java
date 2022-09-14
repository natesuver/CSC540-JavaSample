package Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import DataAccess.QueryResult;
import DataAccess.SqlHelper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * A model which represents a vehicle owner.  Corresponds to the "Owner" table.
 * @author nates
 *
 */
public class Owner {
	private SimpleStringProperty firstName = new SimpleStringProperty();
	private SimpleStringProperty lastName = new SimpleStringProperty();
	private SimpleStringProperty middleName = new SimpleStringProperty();
	private SimpleStringProperty address1 = new SimpleStringProperty();
	private SimpleStringProperty address2 = new SimpleStringProperty();
	private SimpleStringProperty city = new SimpleStringProperty();
	private SimpleStringProperty state = new SimpleStringProperty();
	private SimpleStringProperty zip = new SimpleStringProperty();
	private SimpleStringProperty username = new SimpleStringProperty();
	private SimpleStringProperty country = new SimpleStringProperty();
	private ObjectProperty<LocalDate> birthdate = new SimpleObjectProperty<>();
	public int Id=0;
	
	private DataAccess.SqlHelper _baseDa;
	
	public final String getFirstName() {
		return firstName.getValue();
	}
	public StringProperty firstNameProperty() {
	  return firstName;
	}
	public final void setFirstName(String n) {
		this.firstName.setValue(n);
	}
	
	public final String getLastName() {
      return lastName.getValue();
	}
	public StringProperty lastNameProperty() {
	  return lastName;
	}
	public final void setLastName(String n) {
		this.lastName.setValue(n);
	}
	
	public final String getMiddleName() {
      return middleName.getValue();
	}
	public StringProperty middleNameProperty() {
	  return middleName;
	}
	public final void setMiddleName(String n) {
		this.middleName.setValue(n);
	}	
		
	public final String getAddress1() {
      return address1.getValue();
	}
	public StringProperty address1Property() {
	  return address1;
	}
	public final void setAddress1(String n) {
		this.address1.setValue(n);
	}
	
	public final String getAddress2() {
      return address2.getValue();
	}
	public StringProperty address2Property() {
	  return address2;
	}
	public final void setAddress2(String n) {
		this.address2.setValue(n);
	}	
	
	public final String getCity() {
      return city.getValue();
	}
	public StringProperty cityProperty() {
	  return city;
	}
	public final void setCity(String n) {
		this.city.setValue(n);
	}		
  
	public final String getState() {
      return state.getValue();
	}
	public StringProperty stateProperty() {
	  return state;
	}
	public final void setState(String n) {
		this.state.setValue(n);
	}	
	
	public final String getZip() {
      return zip.getValue();
	}
	public StringProperty zipProperty() {
	  return zip;
	}
	public final void setZip(String n) {
		this.zip.setValue(n);
	}	
		
	public final String getCountry() {
      return country.getValue();
	}
	public StringProperty countryProperty() {
	  return country;
	}
	public final void setCountry(String n) {
		this.country.setValue(n);
	}			
	  
	public final String getUsername() {
      return username.getValue();
	}
	public StringProperty usernameProperty() {
	  return username;
	}
	public final void setUsername(String n) {
		this.username.setValue(n);
	}
	
	public final LocalDate getBirthdate() {
      return birthdate.get();
	}
	
	public ObjectProperty<LocalDate> birthdateProperty() {
	  return birthdate;
	}
	public final void setBirthdate(LocalDate d) {
		this.birthdate.set(d);
	}
	
	public Owner() {
		_baseDa = new DataAccess.SqlHelper();
	}
	
	/**
	 * Adds a new owner to the system, as well as sets up a new database user, and associates that user to the electric_user group.  This method uses a transaction.
	 * 
	 * @return a QueryResult
	 */
	public QueryResult Add(String loginPassword) {
		Connection conn =  null;
		PreparedStatement stmt = null;
		try {
			conn = _baseDa.GetConnection();
			conn.setAutoCommit(false);
			
			String insertSql = "INSERT INTO Owner (LastName,FirstName,MiddleName,Birthdate,Address1,Address2,"
					+ "City,State,PostalCode,Country,username) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			stmt = _baseDa.GetPreparedInsertStatement(insertSql, false); //this overload does not commit by itself, it's up to the caller to remember this step.
			stmt.setString(1, this.getLastName());
			stmt.setString(2, this.getFirstName());
			stmt.setString(3, this.getMiddleName());
			stmt.setDate(4, Date.valueOf(this.getBirthdate()));
			stmt.setString(5, this.getAddress1());
			stmt.setString(6, this.getAddress2());
			stmt.setString(7, this.getCity());
			stmt.setString(8, this.getState());
			stmt.setString(9, this.getZip());
			stmt.setString(10, this.getCountry());
			stmt.setString(11, this.getUsername());
			QueryResult result = _baseDa.ExecInsert(stmt);
			if (result.Successful()) {
				QueryResult loginResult  = SetupDatabaseUser(this.getUsername(), loginPassword);
				if (!loginResult.Successful()) return loginResult;
			}
			this.Id = result.Id; //this won't work for a remote client, so always make sure the id is set appropriately from the caller
			return result;
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			try {
				if (conn!=null) conn.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			e1.printStackTrace();
			QueryResult badResult = new QueryResult();
			badResult.ErrorMessage = e1.getMessage();
			return badResult;
		}
		finally {
			try {
				if (stmt!=null) stmt.close();
				if (conn!=null) conn.close(); //explictly calling close() here because sqlhelper will not close a connection that was marked with autocommit false.
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	/**
	 * Sets up the user in the database.  Split into multiple statements for easier testing and readability.
	 */
	private QueryResult SetupDatabaseUser(String username, String loginPassword) {
		username = username.replaceAll("[^a-zA-Z0-9]",""); //scrub out any special characters, protects from sql injection
		String statements[] = new String[3];
		statements[0] = "CREATE USER " + username + ";";
		statements[1] = "ALTER USER " + username + " WITH PASSWORD '" + loginPassword + "';";
		statements[2] ="GRANT electric_user TO " + username + ";";
		return _baseDa.ExecSql(statements, true);
	}
	
	public QueryResult Delete() {
		String deleteSql = "Delete from owner where Id = ?";
		PreparedStatement stmt = _baseDa.GetPreparedStatement(deleteSql);
		try {
			stmt.setInt(1, this.Id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return _baseDa.ExecStatement(stmt);
	}
	
	public QueryResult Update() {
		String sql = "update owner set LastName=?,FirstName=?,MiddleName=?,Birthdate=?,Address1=?,Address2=?,City=?,State=?,PostalCode=?,Country=?,username=? where Id = ?";
		PreparedStatement stmt = _baseDa.GetPreparedStatement(sql);
		try {
			stmt.setString(1, this.getLastName());
			stmt.setString(2, this.getFirstName());
			stmt.setString(3, this.getMiddleName());
			stmt.setDate(4, Date.valueOf(this.getBirthdate()));
			stmt.setString(5, this.getAddress1());
			stmt.setString(6, this.getAddress2());
			stmt.setString(7, this.getCity());
			stmt.setString(8, this.getState());
			stmt.setString(9, this.getZip());
			stmt.setString(10, this.getCountry());
			stmt.setString(11, this.getUsername());
			stmt.setInt(12, this.Id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return _baseDa.ExecStatement(stmt);
	}
	
	public static Owner Get(int id) {
		DataAccess.SqlHelper baseDa = new DataAccess.SqlHelper();
		String sql = "Select * from owner where Id=?";
		PreparedStatement stmt = baseDa.GetPreparedStatement(sql);
		Owner owner = null;
		try {
			stmt.setInt(1, id);
			owner = readOwner(stmt);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return owner;
	}

	public static ObservableList<Owner> Find(String searchTerm) {
		ObservableList<Owner> results = FXCollections.observableArrayList(); 
		DataAccess.SqlHelper baseDa = new DataAccess.SqlHelper();
		ResultSet rs=null;
		String sql = "Select * from owner where lastname like ? or firstname like ? or middlename like ?";
		PreparedStatement stmt = baseDa.GetPreparedStatement(sql);
		try {
			stmt.setString(1, "%" + searchTerm + "%");
			stmt.setString(2, "%" + searchTerm + "%");
			stmt.setString(3, "%" + searchTerm + "%");
			rs = stmt.executeQuery();
			while (rs.next()) {
				results.add(readOwnerData(rs));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				SqlHelper.CleanupResources(stmt, rs);		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return results;
	}
	
	public static Owner Get(String username) {
		DataAccess.SqlHelper baseDa = new DataAccess.SqlHelper();
		String sql = "Select * from owner where username=?";
		PreparedStatement stmt = baseDa.GetPreparedStatement(sql);
		Owner owner = null;
		try {
			stmt.setString(1, username);
			owner = readOwner(stmt);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return owner;
	}
	
	private static Owner readOwner(PreparedStatement stmt) {
		ResultSet rs=null;
		Owner own=null;
		try {
			rs = stmt.executeQuery();
			if (rs.next()) { //This select expects at most one owner, return null if owner not found
				own = readOwnerData(rs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				SqlHelper.CleanupResources(stmt, rs);				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return own;
	}

	private static Owner readOwnerData(ResultSet rs) throws SQLException {
		Owner own = new Owner();
		own.setFirstName(rs.getString("firstname"));
		own.setLastName (rs.getString("lastname"));
		own.setMiddleName (rs.getString("middlename"));
		own.setAddress1(rs.getString("address1"));
		own.setAddress2 (rs.getString("address2"));
		own.setCity (rs.getString("city"));
		own.setState(rs.getString("state"));
		own.setZip (rs.getString("postalcode"));
		own.setCountry (rs.getString("country"));
		own.Id = rs.getInt("id");
		own.setBirthdate(rs.getDate("birthdate").toLocalDate());
		own.setUsername (rs.getString("username"));
		own.setFirstName(rs.getString("firstname"));
		return own;
	}
}




