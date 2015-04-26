package XML;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public interface GetDocumentFromURL
{
	public Document getDocumentFromUrl(URL getFrom) throws IOException, ParserConfigurationException, SAXException;
}
