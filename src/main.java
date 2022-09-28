import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DataAccess.Dropdowns;
import DataAccess.Seeder;
import Ui.Startup;

public class main {

	public static void main(String[] args) throws FileNotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, SQLException {
		
		
		System.out.println("HELLO WORLD");
		var sd = new Seeder();
		//sd.seedDatabase();
		var result = Dropdowns.GetStates();
		for (var state : result) {
			System.out.println(state);
		}
		var start = new Startup();
		start.main(null);
	}



	
}


