package uoc.pfc.detectplag.html;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.TreeMap;

import uoc.pfc.detectplag.compare.ResultComparator;

public class CreateDetailElements extends CreateHTML{
	private static final String OUTPUT_FIlE = "\\detail";
	
	// --- Cables Iguales / Componentes Iguales / Cables posicion identica / Cables dimensión identica / Conexiones Iguales / Componentes misma pos []
	public static void create(HashMap<String, ResultComparator> comparatorResultsMap,String outPath){
		File directory = new File(outPath+OUTPUT_FIlE);
		if(!directory.exists()){
			directory.mkdir();
		}else{
			String[]entries = directory.list();
			for(String s: entries){
			    File currentFile = new File(directory.getPath(),s);
			    currentFile.delete();
			}
		}
		for(String resCompName : comparatorResultsMap.keySet()){
			ResultComparator resComp = comparatorResultsMap.get(resCompName);
			String outputFile = outPath+OUTPUT_FIlE+"\\"+resCompName.replace("/", "").replace(".", "_")+".html";
			StringBuilder htmlBuilder = createHeader("Detalle de la comparación "+resCompName);
			htmlBuilder.append("<tr><td>Circuitos Comparados</td><td>Total cables Circuito 1</td><td>Total cables Circuito 2</td><td>Total Componentes Circuito 1</td>"+
			"<td>Total Componentes Circuito 2</td><td title=\"Número de cables exactamente iguales en todos los atributos\"><b>Cables Iguales</b></td>"+
			"<td title=\"Número de componentes exactamente iguales en todos los atributos\"><b>Componentes Iguales</b></td>"+
			"<td title=\"Los cable empiezan o acaban en la misma posición\"><b>Cables con posición identica </b></td>");
			htmlBuilder.append("<td title=\"Los cable tienen la misma longitud y empizan o acaban en la misma posición\"><b>Cables con dimensión identica </b></td>");
			TreeMap<String,Double> componentesUtilizados = resComp.getAttCountingPercComponents();
			HashMap<String,Integer> numCompCir1 = resComp.getNumComponentsMapCir1();
			HashMap<String,Integer> numCompCir2 = resComp.getNumComponentsMapCir2();
			for(String compName : componentesUtilizados.keySet()){
				htmlBuilder.append("<td title=\"Número de componentes de este tipo utilizados para el diseño del circuito\"><b>Número Igual de Componentes utilizados del tipo "+compName+"</b></td>");
			}
			htmlBuilder.append("<td title=\"Número de elementos conectados del mismo modo en posiciones similares\"><b>Conexiones Iguales </b></td>");
			HashMap<String, Integer> componentesMismaPosicion = resComp.getComponentesMismaPosicion();
			
			for(String componente : componentesMismaPosicion.keySet()){
				htmlBuilder.append("<td title=\"Número de componentes conectados en la misma posición\"><b>Componente "+componente+" misma posición </b></td>");
			}
			
			htmlBuilder.append("</tr>");
			htmlBuilder.append("<tr>");
			htmlBuilder.append("<td>"+resCompName+"</td>");
			htmlBuilder.append("<td>"+resComp.getNumWiresCir1()+"</td>");
			htmlBuilder.append("<td>"+resComp.getNumWiresCir2()+"</td>");
			htmlBuilder.append("<td>"+resComp.getNumComponentsCir1()+"</td>");
			htmlBuilder.append("<td>"+resComp.getNumComponentsCir2()+"</td>");
			htmlBuilder.append("<td>"+resComp.getNumEqualWires()+"</td>");
			htmlBuilder.append("<td>"+resComp.getNumEqualComponents()+"</td>");
			
			if((resComp.getNumWiresCir1() == resComp.getNumWiresCir2()) && (resComp.getNumEqualWires() == resComp.getNumWiresCir1())){
				htmlBuilder.append("<td>"+resComp.getNumEqualWires()+"</td>");
				htmlBuilder.append("<td>"+resComp.getNumEqualWires()+"</td>");	
			}else{
				htmlBuilder.append("<td>"+resComp.getNumWiresSamePosition()+"</td>");
				htmlBuilder.append("<td>"+resComp.getNumWiresSameLength()+"</td>");	
			}
			
			for(String compName : componentesUtilizados.keySet()){
				if(numCompCir1.containsKey(compName) && numCompCir2.containsKey(compName)){
					int num1 = numCompCir1.get(compName);
					int num2 = numCompCir2.get(compName);
					if(num1 == num2){
						htmlBuilder.append("<td title=\""+num1+"\">SI</td>");
					}else{
						htmlBuilder.append("<td>NO</td>");
					}
				}else{
					htmlBuilder.append("<td>NO</td>");
				}
				
			}
			htmlBuilder.append("<td>"+resComp.getConexionesIguales()+"</td>");
			
			for(String componente : componentesMismaPosicion.keySet()){
				htmlBuilder.append("<td>"+componentesMismaPosicion.get(componente)+"</td>");
			}
			
			htmlBuilder.append("</tr>");
			htmlBuilder.append("</table>");
			htmlBuilder.append("<br>");
			htmlBuilder.append("<b>Cables Iguales : </b>Número de cables exactamente iguales en todos los atributos");
			htmlBuilder.append("<br>");
			htmlBuilder.append("<b>Componentes Iguales : </b>Número de componentes exactamente iguales en todos los atributos");
			htmlBuilder.append("<br>");
			htmlBuilder.append("<b>Cables con posición identica : </b>Los cable empiezan o acaban en la misma posición");
			htmlBuilder.append("<br>");
			htmlBuilder.append("<b>Cables con dimensión identica : </b>Los cable tienen la misma longitud y empizan o acaban en la misma posición");
			htmlBuilder.append("<br>");
			htmlBuilder.append("<b>Número Igual de Componentes utilizados del tipo ... : </b>Número de componentes de este tipo utilizados para el diseño del circuito");
			htmlBuilder.append("<br>");
			htmlBuilder.append("<b>Conexiones Iguales : </b>Número de elementos conectados del mismo modo en posiciones similares (+-5)");
			htmlBuilder.append("<br>");
			htmlBuilder.append("<b>Componente misma posición : </b>Número de componentes conectados en la misma posición");
			htmlBuilder.append(closeHTML());
			
			
			String html = htmlBuilder.toString();
			try {
				PrintWriter out = new PrintWriter(outputFile);
				out.println(html);
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		
	}
}
