package XML;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;


/**
 * The Class represents the GUI surface,
 * allow user to convert coins, see currencies table.
 * 
 * @author Livne Lang, Uriel Chechique & Liana Nechmad
 */
public class Client implements CurrencyInterface, Runnable
{
	static Logger logger = Logger.getLogger(Client.class);  		  // Main Logger
	static Logger titlelogger = Logger.getLogger("Title Logger");	 // title Logger
	private  BankIsraelCurrencyParse bank ;
	private JFrame frame;
	private JTextField textField1;
	private JTextField textField2;
	private JButton convert1;
	private JButton convert2;
	private JButton btnClearFields;
	private JLabel lblUpdate;
	private JComboBox comboBox1,comboBox2;
	private JTable table;
	private JScrollPane jps;
	private ComboListener clistener;
	private TextListener tlistener;
	private ConvertListener convertlistener;
	private ClearListener clearlistener;
	private String c1,c2;
	private double sum;
	private int k;       // declaring # of updates
	private int logged_in = 0;
	private Image backgroundImage;											 // new 
	URL image = getClass().getClassLoader().getResource("XML/b2.jpg");	 	// new
	private ImageIcon im = new ImageIcon(image);

	
	
	
	/**
	 * Main Thread Program
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException 
	{
		
		Client client = new Client();
		client.start();
		Thread update_session = new Thread(client);
		update_session.run();
	}


	/**
	 * Client Constructor,
	 * initialize the bank,currencies table.
	 *  
	 * @see UpdateTable()
	 * @see BankIsraelCurrencyParse#initialize()
	 * @throws IOException
	 */
	public Client() throws IOException 		// Client Constructor
	{
		
		logger.info("Client In Construction .. ");
		bank = new BankIsraelCurrencyParse();
		bank.initialize();
		String col[] = {"Currency", "Rate"};
		Object[][] rowdata=new Object[15][2];
		rowdata = UpdateTable();	// Uses data from bank
		table = new JTable(rowdata,col);
		jps = new JScrollPane(table);
	}

	
	/**
	 * This method runs all swing components,
	 * and then loads the JFrame to the screen.
	 * 
	 * @throws IOException
	 * @return void
	 */
	private void start() throws IOException
	{
		
		frame = new JFrame();
		frame.setTitle("Livne, Uriel & Liana Currency Exchange Application");
		frame.setBounds(100, 100, 450, 300);
		frame.setSize(600,350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new JLabel(im));
		frame.getContentPane().setLayout(null);
		
		logger.info(frame.getTitle()+" Started"); 		// Logs The Application Title
		
		
		JLabel lblConvertFrom = new JLabel("Convert From");
		lblConvertFrom.setBackground(Color.BLUE);
		lblConvertFrom.setBounds(25, 11, 77, 14);
		frame.getContentPane().add(lblConvertFrom);
		
		comboBox1 = new JComboBox();
		comboBox1.setModel(new DefaultComboBoxModel(new String[] {"JPY", "JOD", "CAD", "AUD", "ZAR", "GBP", "DKK", "NOK", "SEK", "CHF", "EUR", "EGP", "USD", "LBP","ILS"}));
		comboBox1.setSelectedIndex(10);
		clistener = new ComboListener();
		comboBox1.addActionListener(clistener);
		comboBox1.setBounds(25, 36, 107, 20);
		c1 = (String) comboBox1.getSelectedItem();
		frame.getContentPane().add(comboBox1);
		
		comboBox2 = new JComboBox();
		comboBox2.setModel(new DefaultComboBoxModel(new String[] {"JPY", "JOD", "CAD", "AUD", "ZAR", "GBP", "DKK", "NOK", "SEK", "CHF", "EUR", "EGP", "USD", "LBP","ILS"}));
		comboBox2.setSelectedIndex(12);
		comboBox2.addActionListener(clistener);
		comboBox2.setBounds(186, 36, 107, 20);
		c2 = (String) comboBox2.getSelectedItem();
		frame.getContentPane().add(comboBox1);
		frame.getContentPane().add(comboBox2);
		
		JLabel lblConvertTo = new JLabel("Convert To");
		lblConvertTo.setBackground(Color.BLUE);
		lblConvertTo.setBounds(186, 11, 77, 14);
		frame.getContentPane().add(lblConvertTo);
		
		textField1 = new JTextField();
		tlistener = new TextListener();
		textField1.addActionListener(tlistener);
		textField1.setBounds(25, 67, 107, 23);
		//sum = Double.parseDouble(textField1.getText());
		frame.getContentPane().add(textField1);
		textField1.setColumns(10);
		
		textField2 = new JTextField();
		textField2.addActionListener(tlistener);
		textField2.setColumns(10);
		textField2.setBounds(186, 67, 107, 20);
	//	sum = Double.parseDouble(textField2.getText());
		frame.getContentPane().add(textField2);
		
		convert1 = new JButton("Convert ->");
		convertlistener = new ConvertListener();
		convert1.addActionListener(convertlistener);
		convert1.setBounds(25, 98, 107, 23);
		frame.getContentPane().add(convert1);
		
		convert2 = new JButton("<- Convert ");
		convert2.addActionListener(convertlistener);
		convert2.setBounds(186, 98, 107, 23);
		frame.getContentPane().add(convert2);
		
		
		jps.setBounds(353, 11, 221, 249);  // JScrollPane For Table
		frame.getContentPane().add(jps);
		
		lblUpdate = new JLabel("Last Update :  "+ bank.update);
		lblUpdate.setBounds(35, 260, 165, 49);
		frame.getContentPane().add(lblUpdate);
		
		btnClearFields = new JButton("Clear Fields"); 	// Set Clear Button To Frame
		clearlistener = new ClearListener();
		btnClearFields.addActionListener(clearlistener);
		btnClearFields.setBounds(102, 132, 117, 40);
		frame.getContentPane().add(btnClearFields);
		
		frame.addWindowListener(new FrameListener());
		
	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		logged_in = 1;
	}
	
	
	
	/**
	 * This function reads currencies from our bank.
	 * 
	 * @return String[][]
	 */
	public String[][] UpdateTable()  // Gets The Updated Currencies
	{
		String[][] rowData = new String[15][2];			
		int i=0,j=0;

		for (Map.Entry<String, BankIsraelCurrency> entry : this.bank.currency.entrySet()) 
		{
			rowData[i][j] = entry.getKey()+ " - "+ entry.getValue().getCurrencyName();
			rowData[i][j+1] = Double.toString(entry.getValue().getRate());
			i++;
			j=0;	
		}		
		return rowData;
	}	

	/**
	 * This class reacts to the ComboListener,
	 * as it implements the ActionListener
	 *
	 */
	private class ComboListener implements ActionListener					// Implements ComboBox Listener
	{

		@Override
		public void actionPerformed(ActionEvent event) {
			
			Object c = (Object)event.getSource();
			
			if(c == comboBox1)
			{
				JComboBox cb = (JComboBox) event.getSource();    	  // Gives Us a JComboBox copy to play with 
				c1=(String) cb.getSelectedItem();					 // sets First Currency
			}
			
			if(c == comboBox2)
			{
				JComboBox cb = (JComboBox) event.getSource();    // Gives Us a JComboBox copy to play with 
				c2=(String) cb.getSelectedItem();				// sets First Currency
				//textField2.setText(c2);
			}
		}
	}
	

	
	/**
	 * This class reacts to the FrameListener,
	 * as it implements the WindowAdapter
	 *
	 */
	private class FrameListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		  {
			
			logged_in = 0;
		    System.exit(0);
		  }	
	}
	
	

	/**
	 * This class reacts to the TextListener,
	 * as it implements the ActionListener
	 */
	private class TextListener implements ActionListener      			  		// Implements TextField Listener
	{

		@Override
		public void actionPerformed(ActionEvent event) {
			
			Object c = (Object)event.getSource();
			if(c == textField1)
			{
				JTextField tf = (JTextField) event.getSource();     	  // Gives Us a TextField copy to play with 
				sum=Double.parseDouble(tf.getText());					 // Gives us the sum user want to exchange
			}
			
			if(c == textField2)
			{
				JTextField tf = (JTextField) event.getSource();      // Gives Us a TextField copy to play with 
				sum=Double.parseDouble(tf.getText());				// Gives us the sum user want to exchange
			}
		}
	}
	
	/**
	 * This class reacts to the ConvertListener,
	 * as it implements the ActionListener
	 */
	private class ConvertListener implements ActionListener     // Implements ConvertListener 
	{

		@Override
		public void actionPerformed(ActionEvent event) {
			
		
			
			
			Object c = (Object)event.getSource();
			if(c == convert1)						// Identify Left Convert Button
			{
				double result=0;				
				try
				{
					if(Integer.parseInt(textField1.getText())<0 )
					{
						JOptionPane.showMessageDialog(frame,"Please Enter Positive Numbers ");
						
					}
					
					
					if(check_input(textField1.getText()) ==false)			  // Check For Bad Input From User
					{
					
					throw new Exception("Bad Input");
					}
				}
				
				catch(Exception exc)									  // Catch Bad Input
				{
					textField1.setText("Only Numbers !");
				    System.out.println("Error: " + exc.getMessage());	// Print Error Source
				}
				
				sum = Double.parseDouble(textField1.getText());
				double rate = bank.currency.get(c1).getRate();  		// We got The exchanged Rate
				double dividerate = bank.currency.get(c2).getRate();   // We got The changed Rate
				result = convert(sum,rate,dividerate);
				textField2.setText(String.valueOf(result));
			}
			
			if(c == convert2)									// Identify Right Convert Button
			{
				double result=0;
				
				if(Integer.parseInt(textField2.getText())<0 )
				{
					JOptionPane.showMessageDialog(frame,"Please Enter Positive Numbers ");
					
				}
				
				try
				{
					if(check_input(textField2.getText()) ==false)				   // Check For Bad Input From User
					{
					throw new Exception("Bad Input");
					}
				}
				
				catch(Exception exc)									  	  // Catch Bad Input
				{
					textField2.setText("Only Numbers !");
				    System.out.println("Error: " + exc.getMessage());		// Print Error Source
				}
				sum = Double.parseDouble(textField2.getText());
				double rate = bank.currency.get(c2).getRate();  		// We got The exchanged Rate
				double dividerate = bank.currency.get(c1).getRate();   // We got The changed Rate
				result = convert(sum,rate,dividerate);				  // Send Data To Function
				textField1.setText(String.valueOf(result));
			}
		}
	}
	
	
	/**
	 * This class reacts to the ClearListener,
	 * as it implements the ActionListener
	 */
	private class ClearListener implements ActionListener 		// Implements ClearListener
	{
		@Override
		public void actionPerformed(ActionEvent event) {
			
			Object c = (Object)event.getSource();
			if(c == btnClearFields)
			{
				textField1.setText("");		 // Clear Left TextField
				textField2.setText("");		// Clear Right TextField
			}
		}
	}
	
	
	/**
	 * This function checks the user input.
	 * 
	 * @return TRUE/FALSE
	 */
	public boolean check_input(String input1)		// Check If User Input Is only numeral ! 
	{
		for (char c : input1.toCharArray())
	    {
	        if (!Character.isDigit(c) && (c!='.')) return false;
	    }
		return true;		// If Its Only Numbers ( Including Dots )
	}

	
	/**
	 * This function converts user money.
	 * 
	 */
	public double convert(double sum, double rate, double divide_rate)	// Convert Function Implemented
	{
		double result=0;					   // result = 0
		result = rate*sum;					  // Multiply in sum user entered/wants to exchange
		result /= divide_rate;				 // Divide in the changed rate
		return result;						// returns exchanged money
	}
	
	
	
	/**
	 * 1.This function runs our separated Thread.
	 * 2. keep on updating from the web
	 * 
	 * @see BankIsraelCurrencyParse#initialize()
	 * @see #UpdateTable()
	 * @see BankIsraelCurrencyParse#Check_For_Update()
	 */
	public void run()
	{
		while(logged_in == 1)
		{
			try
			{
				Thread.sleep(5000);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			
			try {
				System.out.println("------- Update Session #"+k+" -------");
				logger.info("------- Update Session #"+k+" -------");
				k++;
				if(bank.Check_For_Update() == true)
				{
					this.UpdateTable();	   // Update The GUI Table
					lblUpdate.setText("Last Update :  "+ bank.update);
					logger.info("Currencies Updated !");
				}
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (ParserConfigurationException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (SAXException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
	}
	
	
	
	}
	
