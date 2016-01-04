package uoc.pfc.detectplag.compare;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

public class ResultComparator {

	private int numWiresCir1;
	private int numWiresCir2;
	private int numComponentsCir1;
	private int numComponentsCir2;
	private int numEqualWires;
	private int numEqualComponents;
	private String comparatorName;
	private double equalComparatorPerc;
	private HashMap<String,Integer> numComponentsMapCir1;
	private HashMap<String,Integer> numComponentsMapCir2;
	private TreeMap<String,Integer> attCountingComponents;
	private TreeMap<String,Double> attCountingPercComponents;
	private HashMap<String,Integer> componentesMismaPosicion;
	private int numWiresSamePosition;
	private int numWiresSameLength;
	private int conexionesIguales;
	private HashMap<String,String> totalConexionesCir1;
	private HashMap<String,String> totalConexionesCir2;
	
	private double copyPrecent;
	
	
	public ResultComparator(String name){
		comparatorName = name;
	}
	
	

	/**
	 * Devuelve un Hash Map con todos los componentes del circuito 1 y el númeor que hay de cada tipo
	 * @return
	 */
	public HashMap<String, Integer> getNumComponentsMapCir2() {
		return numComponentsMapCir2;
	}



	public void setNumComponentsMapCir2(HashMap<String, Integer> numComponentsMapCir2) {
		this.numComponentsMapCir2 = numComponentsMapCir2;
	}



	/**
	 * Devuelve un Hash Map con todos los componentes del circuito 1 y el númeor que hay de cada tipo
	 * @return
	 */
	public HashMap<String, Integer> getNumComponentsMapCir1() {
		return numComponentsMapCir1;
	}



	public void setNumComponentsMapCir1(HashMap<String, Integer> numComponents) {
		this.numComponentsMapCir1 = numComponents;
	}



	public TreeMap<String, Double> getAttCountingPercComponents() {
		return attCountingPercComponents;
	}


	public void setAttCountingPercComponents(TreeMap<String, Double> attCountingPerc) {
		this.attCountingPercComponents = attCountingPerc;
	}



	public int getNumEqualWires() {
		return numEqualWires;
	}

	public void setNumEqualWires(int numEqualWires) {
		this.numEqualWires = numEqualWires;
	}

	public int getNumEqualComponents() {
		return numEqualComponents;
	}

	public void setNumEqualComponents(int numEqualComponents) {
		this.numEqualComponents = numEqualComponents;
	}

	public String getComparatorName() {
		return comparatorName;
	}

	public double getEqualComparatorPerc() {
		return equalComparatorPerc;
	}

	public void setEqualComparatorPerc(double equalComparatorPerc) {
		this.equalComparatorPerc = equalComparatorPerc;
	}

	public int getNumWiresCir1() {
		return numWiresCir1;
	}

	public void setNumWiresCir1(int numWiresCir1) {
		this.numWiresCir1 = numWiresCir1;
	}

	public int getNumWiresCir2() {
		return numWiresCir2;
	}

	public void setNumWiresCir2(int numWiresCir2) {
		this.numWiresCir2 = numWiresCir2;
	}

	public int getNumComponentsCir1() {
		return numComponentsCir1;
	}

	public void setNumComponentsCir1(int numComponentsCir1) {
		this.numComponentsCir1 = numComponentsCir1;
	}

	public int getNumComponentsCir2() {
		return numComponentsCir2;
	}

	public void setNumComponentsCir2(int numComponentsCir2) {
		this.numComponentsCir2 = numComponentsCir2;
	}
	
	public HashMap<String, Integer> getComponentesMismaPosicion() {
		return componentesMismaPosicion;
	}

	public void setComponentesMismaPosicion(
			HashMap<String, Integer> componentesMismaPosicion) {
		this.componentesMismaPosicion = componentesMismaPosicion;
	}

	public int getNumWiresSamePosition() {
		return numWiresSamePosition;
	}

	public void setNumWiresSamePosition(int numWiresSamePosition) {
		this.numWiresSamePosition = numWiresSamePosition;
	}

	public int getNumWiresSameLength() {
		return numWiresSameLength;
	}

	public void setNumWiresSameLength(int numWiresSameLength) {
		this.numWiresSameLength = numWiresSameLength;
	}

	public int getConexionesIguales() {
		return conexionesIguales;
	}

	public void setConexionesIguales(int conexionesIguales) {
		this.conexionesIguales = conexionesIguales;
	}

	
	public HashMap<String, String> getTotalConexionesCir1() {
		return totalConexionesCir1;
	}



	public void setTotalConexionesCir1(HashMap<String, String> totalConexionesCir1) {
		this.totalConexionesCir1 = totalConexionesCir1;
	}



	public HashMap<String, String> getTotalConexionesCir2() {
		return totalConexionesCir2;
	}



	public void setTotalConexionesCir2(HashMap<String, String> totalConexionesCir2) {
		this.totalConexionesCir2 = totalConexionesCir2;
	}



	public void createSummary() {
		
		if(equalComparatorPerc == 100){ //Evaluamos si es una copia exacta (metrica con mas peso)
			copyPrecent=100;
		}else{
			//Evaluamos el total del número de componentes iguales
			double valAux = 0;
			for(Entry<String,Double> entry : attCountingPercComponents.entrySet()) {
				Double value = entry.getValue();
				valAux += value;
			}
			double numCompIguales = valAux/attCountingPercComponents.size();
			
			//Evaluamos el total del número de componentes con posicion identica
			double numComponentesPosIdentica = 0;
			double totalTiposCompMismaPos = 0;
			for(Entry<String, Integer> entry : componentesMismaPosicion.entrySet()){
				String componentName = entry.getKey();
				Integer numComponentes = entry.getValue();
				
				int numCompCir1 = numComponentsMapCir1.get(componentName);
				int numCompCir2 = numComponentsMapCir2.get(componentName);
				
				double auxNumCompMismaPos = (double)(numComponentes*2) / (numCompCir1+numCompCir2)*(double)100;
				numComponentesPosIdentica += auxNumCompMismaPos;
				totalTiposCompMismaPos++;
			}
			if(numComponentesPosIdentica !=0){
				numComponentesPosIdentica = (double)numComponentesPosIdentica / (double)totalTiposCompMismaPos;
			}
			
			//Evaluamos el total del número de cables con posicion identica (en inicio o final)
			double numCablesPosIdentica = (double)((double)(numWiresSamePosition*2)/(double)(numWiresCir1+numWiresCir2))*(double)100;
			//Evaluamos el total del número de cables con longitud identica en posicion coincidente (en inicio o final)
			double numCablesLongitudIdentica = (double)((double)(numWiresSameLength*2)/(double)(numWiresCir1+numWiresCir2))*(double)100;
			//Evaluamos el numero total de conexiones iguales en posiciones similares
			double numConexionesIguales = (double)((double)(conexionesIguales*2)/(double)(totalConexionesCir1.size()+totalConexionesCir2.size()))*(double)100;
			
			if(numEqualWires == 0 && numEqualComponents == 0){
				//Si el número de componentes o cables exactamente iguales es 0 el peso de las metricas es uno
				double resNumCompIguales = numCompIguales*5;
				double resNumComponentesPosIdentica = numComponentesPosIdentica*20;
				double resNumCablesPosIdentica = numCablesPosIdentica*20;
				double resNumCablesLongitudIdentica = numCablesLongitudIdentica*30;
				double resNumConexionesIguales = numConexionesIguales*25;
//				copyPrecent = (double)(numCompIguales+numComponentesPosIdentica+numCablesPosIdentica+numCablesLongitudIdentica)/(double)4;
				copyPrecent = (double)(resNumCompIguales+resNumComponentesPosIdentica+resNumCablesPosIdentica+resNumCablesLongitudIdentica+resNumConexionesIguales)/(double)100;
			}else{
				//Si el número de componentes o cables exactamente iguales es diferente de 0 el peso de las metricas es otro
				double resEqualComparatorPerc = equalComparatorPerc*50;
				double resNumCompIguales = numCompIguales*2.5;
				double resNumComponentesPosIdentica = numComponentesPosIdentica*10;
				double resNumCablesPosIdentica = numCablesPosIdentica*10;
				double resNumCablesLongitudIdentica = numCablesLongitudIdentica*15;
				double resNumConexionesIguales = numConexionesIguales*12.5;
//				copyPrecent = (double)(numCompIguales+numComponentesPosIdentica+numCablesPosIdentica+numCablesLongitudIdentica)/(double)4;
				copyPrecent = (double)(resEqualComparatorPerc+resNumCompIguales+resNumComponentesPosIdentica+resNumCablesPosIdentica+resNumCablesLongitudIdentica+resNumConexionesIguales)/(double)100;
			}
			
		}
	}

	public double getCopyPrecent() {
		return copyPrecent;
	}



	public void setAttCountingComponents(TreeMap<String,Integer> compareNumberComponents) {
		this.attCountingComponents = compareNumberComponents;
		
	}
	
	
}
