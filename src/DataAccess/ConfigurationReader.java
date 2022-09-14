package DataAccess;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Reads and parses an XML file for specific tags. 
 * Refer to README.md for proper structure of the XML file
 * <p>
 * Please ask a team member for user name / password.
 */
public class ConfigurationReader {
	public static final String CONNECTION_URL = "ConnectionURL";
	public static final String DATABASE_NAME = "DBName";
	public static final String DATABASE_PORT = "Port";
	public static final String DATABASE_USERNAME = "User";
	public static final String DATABASE_PASSWORD = "PW";

	private File file;

	/**
	 * @param  filePath The file path (including name) of the file to read from.
	 * @return A new ConfigurationReader for an associated file.
	 */
	public ConfigurationReader(String filePath) {
		this.file = new File(filePath);
	}
	
	/**
	 * Retrieves the values contained in the file path that was passed into the constructor.
	 * 
	 * @param  fields A String array of fields the caller wants to pull from the XML file.
	 * @return A Hash table of String key-value pairs.  The key will be the node tag name,
	 * 			and the value will be the contents of the node.
	 */
	public Hashtable<String, String> getConfiguration(String[] fields) {
		Hashtable<String, String> configuration = new Hashtable<String, String>();
		NodeList nodes = this.getFileContents();
		
		if (nodes == null) {
			return configuration;
		}

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			String nodeTagName = this.getNodeTagName(node);
			String nodeValue = this.getNodeValue(node);

			if (Arrays.asList(fields).contains(nodeTagName)) {
				configuration.put(nodeTagName, nodeValue);
			}
		}
		
		return configuration;
	}
	
	/**
	 * Retrieves the file contents from the file that was passed into the constructor.
	 * 
	 * @return A NodeList containing the nodes parsed from the XML file.
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseConfigurationException
	 */
	private NodeList getFileContents() {
		NodeList nodes = null;

		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			nodes = builder.parse(this.file).getFirstChild().getChildNodes();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			System.out.println("Error Reading Configuration");
			e.printStackTrace();
		}
		
		return nodes;
	}
	
	/**
	 * Returns the tag name of the node passed in.
	 * 
	 * @param node The node to pull the tag name from.
	 * @return A String representing the tag name
	 * 			or a blank String if the node is not of type ELEMENT_NODE
	 */
	private String getNodeTagName(Node node) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;
			return element.getTagName();
		}
		
		return "";
	}
	
	/**
	 * Returns the value of the node passed in.
	 * 
	 * @param node The node to pull the value from.
	 * @return A String representing the value
	 * 			or a blank String if the node is not one of the allowed values.
	 */
	private String getNodeValue(Node node) {
		String elementTagName = this.getNodeTagName(node);
			
		switch (elementTagName) {
			case CONNECTION_URL:
			case DATABASE_NAME:
			case DATABASE_PORT:
			case DATABASE_USERNAME:
			case DATABASE_PASSWORD:
				return node.getTextContent();
			default:
				return "";
		}
	}
}
