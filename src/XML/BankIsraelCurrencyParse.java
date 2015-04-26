package XML;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//import org.apache.log4j.LogManager;
//import org.apache.log4j.spi.LoggerFactory;

/**
 * This class presents the currency bank
 * Holds a map of coin name, and rate
 * Holds the bank last update
 *
 *@author Livne Lang & Uriel Chechique
 */
public class BankIsraelCurrencyParse implements GetDocumentFromURL
{
	//static org.apache.log4j.Logger banklogger = LogManager.getLogger(BankIsraelCurrencyParse.class.getName());  // bank Logger
	Map<String,BankIsraelCurrency> currency = new HashMap<String,BankIsraelCurrency>();
	int connected = 0;
	String update;
	
	

	/**
	 * The bank constructor
	 * 
	 */
	public BankIsraelCurrencyParse() 
	{
		
		super();
		this.connected = 0;
	}



	/**
	 * This function responsible for initializing
	 * bank data.
	 * 
	 * @throws IOException
	 */
	public  void initialize() throws IOException   // used to be main()
	{
		URL url = null;
			try {
				url = new URL("http://www.boi.org.il/currency.xml");
				Document document = this.getDocumentFromUrl(url);
				NodeList check = document.getElementsByTagName("LAST_UPDATE"); // For XML Online  Validate
				
					if(check.item(0).getTextContent() != null) // Document null Check 
					{
						
		//				banklogger.info("We Got Connection From The web!");
						connected=1;
					}
				else
					{ 
					GetDataFromText();
					return;	// We Have Currencies from the text
					}
					
				Write_To_Bank(document);	 // Update The Bank
				Write_To_File(); 			// Update The File
			}
			
			catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				GetDataFromText();  // Activates Get Data From Text Instead
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			 catch (Exception e) 
			 {
				 System.out.println("XML Page is Offline Work !");
		//		 banklogger.info(" Exception: XML Page is Offline Work !");
				 GetDataFromText();  // Activates Get Data From Text Instead
			 }
			
			

	}
	

	
	
	
	/**
	 * This function reads data from file,
	 * in case web XML is offline
	 * 
	 * @throws IOException
	 */
	public void GetDataFromText() throws IOException	// Text Data Reader
	{
		 File f = new File("currencies.txt");
	     FileInputStream fi = new FileInputStream(f);
	     BufferedReader br = new BufferedReader(new InputStreamReader(fi));
	     String line,curr;
	     double _rate;
	     boolean c;
	     while ((line = br.readLine()) != null)   
	     {   	 
	    	 if(line.contains("-")) // Skip On First Line Of Date
	    	 {
	    		 update = line;  	// Enters The Date Variable
	    		 continue;
	    	 }
	    	 
	    	  curr=line.substring(0,3); 						 			// Takes Only The Name Of Coin
	    	  _rate = Double.parseDouble(line.substring(6));			   // Takes Rate Of Coin
	    	  currency.put(curr, new BankIsraelCurrency(curr, _rate) );	  // Enter To Our Currency Map 
	    	  
	    	  
	     }
	     br.close();
	     fi.close();
		
		
	}

	/* (non-Javadoc)
	 * @see XML.GetDocumentFromURL#getDocumentFromUrl(java.net.URL)
	 */
	@Override
	public org.w3c.dom.Document getDocumentFromUrl(URL getFrom) throws IOException, ParserConfigurationException, SAXException
	{
		Document document = null;
		
		try
		{
			
	    InputStream inputStream = getFrom.openConnection().getInputStream();
		DocumentBuilderFactory documentBuildFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuildFactory.newDocumentBuilder();
		document = documentBuilder.parse(inputStream);
		
		}
		
		catch (Exception e)
		{
			
			
		}
		
	return document;
	}
	
	/**
	 * This function checks whether we can 
	 * update the bank or not.
	 * 
	 * @return true/false
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	boolean Check_For_Update() throws IOException, ParserConfigurationException, SAXException
	{
		String web_date=null;
		Document document = null;
		try
		{
		
		URL url = new URL("http://www.boi.org.il/currency.xml");
		document = getDocumentFromUrl(url);
		NodeList check = document.getElementsByTagName("LAST_UPDATE"); // For XML Online  Validate
			if(check.item(0).getTextContent() != null) // Document null Check 
			{
			web_date=check.item(0).getTextContent();
			}
		}
		
		catch (Exception e) 
		 {
			 System.out.println("Offline Connection - Cant Update !");
			 return false;
		 }
		
		//update = "2014-07-15";
		if(update.equals(web_date))	// Check for equal strings
		{
			return false;
		}
		// write to bank/file
		Write_To_Bank(document);	 // Update The Bank (include update)
		Write_To_File(); 			// Update The File
		return true;

	}
	
	/**
	 * This function save currencies data to file
	 * 
	 * @throws IOException
	 */
	public void Write_To_File() throws IOException
	{
		
		// Write Currencies To txt file 
		File f = new File("currencies.txt");
	    FileInputStream fi = new FileInputStream(f);
	    BufferedReader br = new BufferedReader(new InputStreamReader(fi));
	    String file_date = br.readLine();
	    
	    if (file_date !=null)
	    {
	    	if(file_date.equals(update))
	    	{
	    		return;
	    	}
	    }
		String content = "This is the content to write into file";
		File file = new File("currencies.txt");
		// if file doesnt exists, then create it
		if (!file.exists()) 
		{
			file.createNewFile();
		}
		
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(update);   // Writes the last update
		bw.newLine();
		for (Map.Entry<String, BankIsraelCurrency> entry : currency.entrySet()) 
		{
			
		   bw.write(entry.getKey() + " :: " + entry.getValue().getRate());
		   bw.newLine();
		}

		bw.close();
	}
	
	/**
	 * This function writes currencies data 
	 * to the bank.
	 * 
	 * @param document
	 */
	public void Write_To_Bank(Document document)
	{
		NodeList dates = document.getElementsByTagName("LAST_UPDATE");
		 update = dates.item(0).getTextContent();
		
		NodeList currenciesList = document.getElementsByTagName("CURRENCY");
		for(int i = 0; i<currenciesList.getLength(); i++)
		{
			NodeList currentCurrency = currenciesList.item(i).getChildNodes();
			currency.put(currentCurrency.item(5).getTextContent(), 
					new BankIsraelCurrency
					(
							currentCurrency.item(1).getTextContent(),
							Integer.parseInt(currentCurrency.item(3).getTextContent()),
							currentCurrency.item(5).getTextContent(),
							currentCurrency.item(7).getTextContent(),
							Double.parseDouble(currentCurrency.item(9).getTextContent()),
							Double.parseDouble(currentCurrency.item(11).getTextContent())
					));
		}
			currency.put("ILS", 
				new BankIsraelCurrency
				(
						"ILS",
						1,
						"123",
						"ISRAEL",
						1,
						1
				));		
		
		
	}
}
