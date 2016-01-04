package uoc.pfc.detectplag;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import uoc.pfc.detectplag.compare.Comparator;
import uoc.pfc.detectplag.compare.ResultComparator;
import uoc.pfc.detectplag.components.CirFile;
import uoc.pfc.detectplag.html.CreateDetailElements;
import uoc.pfc.detectplag.html.CreateSummary;
import uoc.pfc.detectplag.ui.MainWindow;


public class Principal {
	
	private static HashMap<String, Boolean> isPivotalMap = new HashMap<String, Boolean>();
	private static HashMap<String,ResultComparator> comparatorResultsMap = new HashMap<String,ResultComparator>();
	private static HashMap<String,CirFile> maxTryByStudentMap = new HashMap<String,CirFile>();
	private static MainWindow win = new MainWindow();
	

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		win.addWindowListener(
				new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		win.setVisible(true);
	}
	
	public static void startCompare(String pathIn, String pathOut,int numWarning, int numCritical) throws SAXException, IOException, ParserConfigurationException{
		if(pathIn != null){
			win.changeStateLablel("Comparing....");
			Ficheros ficheros = new Ficheros(pathIn);
			if(!ficheros.getFiles().isEmpty() && ficheros.getFiles().size() != 1){
				HashMap<String,CirFile> mapCirFiles = ficheros.parserXML();
				
				//Filtramos para obtener aquellos ejericios con numTry mas alto
				for(String fileName : mapCirFiles.keySet()){
					CirFile cirFile = mapCirFiles.get(fileName);
					if(maxTryByStudentMap.containsKey(cirFile.getUserName())){
						CirFile cirFileInMap = maxTryByStudentMap.get(cirFile.getUserName());
						if(Integer.parseInt(cirFile.getNumTry()) > Integer.parseInt(cirFileInMap.getNumTry())){
							maxTryByStudentMap.put(cirFile.getUserName(), cirFileInMap);
						}
					}else{
						maxTryByStudentMap.put(cirFile.getUserName(), cirFile);
					}
				}
				
				initPivotal(maxTryByStudentMap);
				
				for(String fileName : isPivotalMap.keySet()){
					boolean isPivotal = isPivotalMap.get(fileName);
					if(!isPivotal){
						isPivotalMap.put(fileName, true);//marcamos el archivo sobre el que pivotamos
						CirFile cirFilePivot = maxTryByStudentMap.get(fileName);
						for(String fileNametoCompare : maxTryByStudentMap.keySet()){
							if(!cirFilePivot.getFileName().equals(fileNametoCompare) && !isPivotalMap.get(fileNametoCompare)){ // evitamos la comparacion contra nosotros mismos y contra archivos ya comparados
								CirFile cirFileToCompare = 	maxTryByStudentMap.get(fileNametoCompare);
								
								// Solamente comparamos ficheros con el numExercise igual
								if(cirFilePivot.getNumExercise().equals(cirFileToCompare.getNumExercise())){
									///// ------- Empezamos el proceso de comparación -----------///////
									ResultComparator resultComparator = new ResultComparator(cirFilePivot.getFileName()+"/"+cirFileToCompare.getFileName());
									
									//----- Comparamos circuitos identicos ---//
									resultComparator.setNumWiresCir1(cirFilePivot.getCables().size());
									resultComparator.setNumWiresCir2(cirFileToCompare.getCables().size());
									resultComparator.setNumComponentsCir1(cirFilePivot.getComponentes().size());
									resultComparator.setNumComponentsCir2(cirFileToCompare.getComponentes().size());
									resultComparator.setNumEqualWires(Comparator.makeCompareEqualWires(cirFilePivot,cirFileToCompare));
									resultComparator.setNumEqualComponents(Comparator.makeCompareEqualComponents(cirFilePivot,cirFileToCompare));
									resultComparator.setEqualComparatorPerc(Comparator.makeEqualComparatorPerc(resultComparator));
									
									//----- Att Count. Components ---//
									resultComparator.setNumComponentsMapCir1(Comparator.numCircuitComponents(cirFilePivot));
									resultComparator.setNumComponentsMapCir2(Comparator.numCircuitComponents(cirFileToCompare));
									resultComparator.setAttCountingPercComponents(Comparator.compareNumberComponentsPerc(cirFilePivot, cirFileToCompare));
									
									//----- Componentes iguales con la misma posición ----//
									resultComparator.setComponentesMismaPosicion(Comparator.equalPositionComponents(cirFilePivot, cirFileToCompare));
									
									//----- Cables con la misma posición de inicio o final -------//
									resultComparator.setNumWiresSamePosition(Comparator.equalWiresPosition(cirFilePivot, cirFileToCompare));
									
									//----- Cables con la misma longitud-------//
									resultComparator.setNumWiresSameLength(Comparator.equalWiresLength(cirFilePivot, cirFileToCompare));
									
									//----- Comprueba si los diseños son similares ------//
									resultComparator.setTotalConexionesCir1(Comparator.getTotalConexiones(cirFilePivot));
									resultComparator.setTotalConexionesCir2(Comparator.getTotalConexiones(cirFileToCompare));
									resultComparator.setConexionesIguales(Comparator.checkCircuitDesign(cirFilePivot, cirFileToCompare));
									
									//------ Crear Resumen de la deteccion (resultado final)--------//
									resultComparator.createSummary();
									
									comparatorResultsMap.put(resultComparator.getComparatorName(), resultComparator);
									
								}else{
									System.out.println("Los ejercicios no son iguales, no los comparamos");
								}
							}
						}
					}
				}
				showFinalResult(comparatorResultsMap,pathOut,numWarning,numCritical);
			}else{
				System.out.println("No se pueden parsear los archivos porque el directorio esta vacío o contiene un solo archivo");
			}
		}else{
			System.out.println("Se necesita un path");
		}
		
		win.changeStateLablel("Done!!!!");
	}
	
	private static void showFinalResult(HashMap<String, ResultComparator> comparatorResultsMap,String pathOut,int numWarning,int numCritical) {
		CreateSummary.create(comparatorResultsMap, pathOut, numWarning, numCritical);
		CreateDetailElements.create(comparatorResultsMap, pathOut);
	}

/**
 * Iniciamos la colección que marcará los archivos que han pivotado como comparadores
 * @param mapParserCirFiles
 */
	private static void initPivotal(HashMap<String,CirFile> mapParserCirFiles) {
		isPivotalMap.clear();
		for(String fileName : mapParserCirFiles.keySet()){
			isPivotalMap.put(fileName, false);
		}
	}
	
}
