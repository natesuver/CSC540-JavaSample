import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import DataAccess.Dropdowns;
import DataAccess.Seeder;

public class main {

	public static void main(String[] args) throws FileNotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, SQLException {
		// TODO Auto-generated method stub
		System.out.println("HELLO WORLD");
		var sd = new Seeder();
		//sd.seedDatabase();
		var result = Dropdowns.GetStates();
		for (var state : result) {
			System.out.println(state);
		}
	}

}
