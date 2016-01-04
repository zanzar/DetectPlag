package uoc.pfc.detectplag.components;

import java.util.ArrayList;

/**
 * Entidad que representa un archivo .cir con toda la información relevante para la comparación
 * @author add
 *
 */
public class CirFile {

	private String fileName;
	private String userName;
	private String semester;
	private String numExercise;
	private String numTry;
	private String dateUpload;
	
	private ArrayList<Wire> cables = new ArrayList<Wire>();
	private ArrayList<Component> componentes = new ArrayList<Component>();
	
	/**
	 *  <username>_<semester>_<num_exercise>_<num_try>_<date_upload>.circ
	 * @param fileName
	 */
	public CirFile(String fileName) {
		this.fileName = fileName;
		String[] parseFileName = fileName.split("_");
		if(parseFileName.length >= 5){
			if(isNumeric(parseFileName[1])){
				this.userName = parseFileName[0];
				this.semester = parseFileName[1];
				this.numExercise = parseFileName[2];
				this.numTry = parseFileName[3];
				this.dateUpload = parseFileName[4];
			}else{
				//El formato del nombre contiene un "_"
				this.userName = parseFileName[0]+"_"+parseFileName[1];
				this.semester = parseFileName[2];
				this.numExercise = parseFileName[3];
				this.numTry = parseFileName[4];
				this.dateUpload = parseFileName[5];
			}
			
		}else{
			System.out.println("Error en el formato del nombre del archivo");
		}
	}

	public String getFileName() {
		return fileName;
	}


	/**
	 * Devuelve un array list con todos los cables que contiene el circuito.
	 * @return
	 */
	public ArrayList<Wire> getCables() {
		return cables;
	}

	public void setCables(Wire wire) {
		cables.add(wire);
	}
	
	/**
	 * Devuelve un array list con todos los cables que contiene el circuito.
	 * @return
	 */
	public ArrayList<Component> getComponentes() {
		return componentes;
	}

	public void setComponentes(Component componente) {
		componentes.add(componente);
	}

	public String getUserName() {
		return userName;
	}

	public String getSemester() {
		return semester;
	}

	public String getNumExercise() {
		return numExercise;
	}

	public String getNumTry() {
		return numTry;
	}

	public String getDateUpload() {
		return dateUpload;
	}

	 
	private static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	
	
}
