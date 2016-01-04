package uoc.pfc.detectplag.html;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

import uoc.pfc.detectplag.compare.ResultComparator;

public class CreateSummary extends CreateHTML{
	private static final String OUTPUT_FIlE = "\\summary.html";
	
	public static void create(HashMap<String, ResultComparator> comparatorResultsMap,String outPath,int numWarning, int numCritical){
		int resultsNormal = 0;
		int resultsWarning = 0;
		int resultsCritical = 0;
		int resultsIguales = 0;
		
		StringBuilder htmlBuilder = createHeader("Results");
		StringBuilder tableSummary =new StringBuilder("<h1>Summary</h1><table border=1>");
		File directory = new File(outPath);
		if(!directory.exists()){
			directory.mkdirs();
		}
		htmlBuilder.append("<tr><td>Circuitos Comparados</td><td><b>Porcentaje de plagio</b></td><td><b>Detalle</b></td>");
		for(String resCompName : comparatorResultsMap.keySet()){
			htmlBuilder.append("<tr>");
			ResultComparator resComp = comparatorResultsMap.get(resCompName);
			if(resComp.getCopyPrecent()>= numCritical ){
				htmlBuilder.append("<td bgcolor=\"#FF0000\">"+resCompName+"</td>");
				htmlBuilder.append("<td bgcolor=\"#FF0000\">"+resComp.getCopyPrecent()+" %</td>");
				
				if(resComp.getCopyPrecent() == 100){
					resultsIguales++;
				}else{
					resultsCritical++;
				}
			}else if(resComp.getCopyPrecent()< numCritical && resComp.getCopyPrecent()>= numWarning){
				htmlBuilder.append("<td bgcolor=\"#FFFF00\">"+resCompName+"</td>");
				htmlBuilder.append("<td bgcolor=\"#FFFF00\">"+resComp.getCopyPrecent()+" %</td>");
				resultsWarning++;
			}else{
				htmlBuilder.append("<td>"+resCompName+"</td>");
				htmlBuilder.append("<td>"+resComp.getCopyPrecent()+" %</td>");
				resultsNormal++;
			}
			htmlBuilder.append("<td><a href=\"detail\\"+resCompName.replace("/", "").replace(".", "_")+".html\">Ver detalle</a></td>");
			htmlBuilder.append("</tr>");
		}
		tableSummary.append("<tr><td>0-24%</td><td>"+resultsNormal+"</td></tr>");
		tableSummary.append("<tr><td>25%-49%</td><td>"+resultsWarning+"</td></tr>");
		tableSummary.append("<tr><td>50%-99%</td><td>"+resultsCritical+"</td></tr>");
		tableSummary.append("<tr><td>100%</td><td>"+resultsIguales+"</td></tr>");
		tableSummary.append("</table>");
		htmlBuilder = tableSummary.append(htmlBuilder);
		htmlBuilder.append(closeHTML());
		String html = htmlBuilder.toString();
		try {
			PrintWriter out = new PrintWriter(outPath+OUTPUT_FIlE);
			out.println(html);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
