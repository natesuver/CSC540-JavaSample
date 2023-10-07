package Ui;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import DataAccess.Dropdowns;
import Model.Owner;
import javafx.event.ActionEvent;

import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;

public class Startup extends JFrame {

	private JPanel contentPane;
	private JTextField stateField;
	private JList<?> list_1;
	
	private JButton btnNext;
	private JButton btnBack;
	//Pagination Offsets
	private int offset = 0;
	private final int maxValuesToList = 3;
	private JLabel lblTotalResults;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Startup frame = new Startup();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Startup() {
		var showDropdownExample = false;
		var showPaginationExample = true;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		JComboBox comboBox = new JComboBox();
		var result = Dropdowns.GetStates();
		for (var state : result) {
			comboBox.addItem(state);
		}
		

		
		stateField = new JTextField();
		
		stateField.setColumns(10);
		
		JButton btnFilterState = new JButton("Filter State");


		if (showDropdownExample) {
			contentPane.add(stateField);
			contentPane.add(btnFilterState);
			contentPane.add(comboBox);
		}

		
		btnFilterState.addActionListener(new ActionListener() {
		
		
		
		
		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			AddState(comboBox);
		}

			
		});

		btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				offset=offset+3;
				SetListModel(list_1);
			}
		});
		
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				offset=offset-3;
				SetListModel(list_1);
			}
		});
		
		lblTotalResults = new JLabel("New label");

	
		int totalCount = Dropdowns.GetStateCount();
		lblTotalResults.setText("We found " + totalCount + " results today");
		list_1 = new JList();
		SetListModel(list_1);
		
		if (showPaginationExample) {
			contentPane.add(lblTotalResults);
			contentPane.add(btnBack);
			contentPane.add(btnNext);
			contentPane.add(list_1);
		}



	}


	private void AddState(JComboBox comboBox) {
		//System.out.println("CLICKED");
		comboBox.removeAllItems();
		var result = Dropdowns.GetFilteredStates(stateField.getText());
		for (var state : result) {
			comboBox.addItem(state);
		}
	}
	
	private void SetListModel(JList list) {
		list.setModel(new AbstractListModel() {
			ArrayList<String> states = Dropdowns.GetPaginatedStates(offset, maxValuesToList);
			String[] values = states.toArray(new String[0]);
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
	}
}
