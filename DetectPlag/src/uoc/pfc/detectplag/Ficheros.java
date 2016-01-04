package uoc.pfc.detectplag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uoc.pfc.detectplag.components.CirFile;
import uoc.pfc.detectplag.components.Component;
import uoc.pfc.detectplag.components.Wire;


/**
 * Operaciones relacionadas con los ficheros a analizar
 * @author add
 *
 */
public class Ficheros {

	private String directorio;
	private ArrayList<String> listFiles = new ArrayList<String>();
	private CirFile cirFile;
	

	public Ficheros(String directorio) {
		this.directorio = directorio;
		obtenerFicheros();
	}
	
	/**
	 * inicialización de la lista de ficheros a analizar
	 */
	private void obtenerFicheros() {
		File dir = new File(this.directorio);
		String[] ficheros = dir.list();
		if (ficheros == null || ficheros.length == 0) {
			System.out.println("No hay ficheros en el directorio especificado");
		} else {
			for (int x = 0; x < ficheros.length; x++)
				// System.out.println(ficheros[x]);
				listFiles.add(ficheros[x]);
		}

	}

	public ArrayList<String> getFiles() {
		return this.listFiles;
	}
	
	/**
	 * Parseamos todos los archivos existentes en la ubicación asignada
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public HashMap<String,CirFile> parserXML() throws SAXException, IOException,ParserConfigurationException {
		HashMap<String,CirFile> mapCirFiles = new HashMap<String,CirFile>();
		for (String fileName : listFiles) {

			cirFile = new CirFile(fileName);
			
			File circFile = new File(this.directorio + "\\" + fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(circFile);
			doc.getDocumentElement().normalize();

			//parserLib(doc);

			parserWire(doc);

			parserComponents(doc);
			
			mapCirFiles.put(fileName, cirFile);
			
			
		}
		
		return mapCirFiles;
	}

	private void parserComponents(Document doc) {

		NodeList nList = doc.getElementsByTagName("comp");
//		System.out.println("---------COMPONENTS---------");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);

//			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
//				System.out.println("lib : " + eElement.getAttribute("lib"));
//				System.out.println("name : " + eElement.getAttribute("name"));
//				System.out.println("loc : " + eElement.getAttribute("loc"));
				Component component = new Component(eElement.getAttribute("lib"), eElement.getAttribute("name"), eElement.getAttribute("loc"));
				if (nNode.hasChildNodes()) {
					parserAttributesComponent(nNode.getChildNodes(),component);
				}
				cirFile.setComponentes(component);
			}
		}
	}

	private static void parserAttributesComponent(NodeList nodeList, Component component) {
//		System.out.println("--Attributes--");
		for (int count = 0; count < nodeList.getLength(); count++) {
			Node tempNode = nodeList.item(count);
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				if (tempNode.hasAttributes()) {
					NamedNodeMap nodeMap = tempNode.getAttributes();
					for (int i = 0; i < nodeMap.getLength(); i++) {
						Node node = nodeMap.item(i);
//						System.out.println(node.getNodeName()+" : "+ node.getNodeValue());
						component.setAtributes(node.getNodeName(), node.getNodeValue());
					}
				}
			}

		}
	}

	private void parserWire(Document doc) {
		NodeList nList = doc.getElementsByTagName("wire");
//		System.out.println("---------WIRES---------");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
//			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
//				System.out.println("from : " + eElement.getAttribute("from"));
//				System.out.println("to : " + eElement.getAttribute("to"));
				Wire wire = new Wire(eElement.getAttribute("from"), eElement.getAttribute("to"));
				cirFile.setCables(wire);
			}
		}
	}

//	private void parserLib(Document doc) {
//		NodeList nList = doc.getElementsByTagName("lib");
//		System.out.println("---------LIB---------");
//		for (int temp = 0; temp < nList.getLength(); temp++) {
//			Node nNode = nList.item(temp);
//			System.out.println("\nCurrent Element :" + nNode.getNodeName());
//			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//				Element eElement = (Element) nNode;
//				//System.out.println("name : " + eElement.getAttribute("name"));
//				//System.out.println("descripcion : "+ eElement.getAttribute("desc"));
//				Lib lib = new Lib(eElement.getAttribute("name"), eElement.getAttribute("desc"));
//				cirFile.setLibrerias(lib);
//			}
//		}
//	}

	
	private void parserCircuit(Document doc) {
		NodeList nList = doc.getElementsByTagName("circuit");
		System.out.println("---------CRICUIT---------");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				System.out.println("name : " + eElement.getAttribute("name"));
			}
		}

	}
}
