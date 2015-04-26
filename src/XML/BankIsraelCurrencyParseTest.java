package XML;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;






import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/*import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;*/
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * The class invokes the Test Units
 * 
 * @author Livne Lang & Uriel Chechique
 */
public class BankIsraelCurrencyParseTest 
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		
	}
	

	@AfterClass
	public static void tearDownAfterClass() throws Exception 
	{
		
	}

	@Before
	public void setUp() throws Exception 
	{
		
	}

	@After
	public void tearDown() throws Exception 
	{
		
	}

	/**
	 * Test whether the URL is connected or not
	 * 
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	@Test
	public void testInitialize() throws IOException, ParserConfigurationException, SAXException 
	{
		URL url = null;
		url = new URL("http://www.boi.org.il/currency.xml");
		Document document = getDocumentFromUrl(url);
		
		assertTrue(document !=null);
	}

	/**
	 * This Test checks whether our backup text is accessible or not
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public void testGetDataFromText() throws FileNotFoundException 
	{
		File f = new File("currencies.txt");
	    FileInputStream fi = new FileInputStream(f);
	    
	    	
	    	assertFalse(fi==null);
	    	
	    	
	    
	}
	
	/**
	 * This Test checks if the Inputstream is not damaged
	 * 
	 * @throws IOException
	 */
	@Test
	public void testGetDocumentFromUrl() throws IOException 
	{
		URL getFrom= new URL("www.boi.org.il/currency.xml");
		InputStream inputStream = getFrom.openConnection().getInputStream();
	
		
		assertTrue(inputStream!=null);
		
	}
	
	public Document getDocumentFromUrl(URL getFrom) throws IOException, ParserConfigurationException, SAXException
	{
		HttpURLConnection con = null;
		con = (HttpURLConnection)getFrom.openConnection();
        con.setRequestMethod("GET");
		InputStream inputStream = getFrom.openConnection().getInputStream();
		DocumentBuilderFactory documentBuildFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuildFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(inputStream);
		return document;
	}

}
