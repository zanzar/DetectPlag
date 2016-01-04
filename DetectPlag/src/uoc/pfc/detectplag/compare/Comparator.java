package uoc.pfc.detectplag.compare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import uoc.pfc.detectplag.components.CirFile;
import uoc.pfc.detectplag.components.Component;
import uoc.pfc.detectplag.components.Wire;

/**
 * El core del algoritmo de comparación se implementa en esta clase
 * @author Artur
 *
 */
public class Comparator {
	
	/**
	 * Comparamos el número de cables identicos entre los dos circuitos
	 * Dos cables son idénticos cuando los parámetros from y to coinciden
	 * @param cirFile1
	 * @param cirFile2
	 */
	public static int makeCompareEqualWires(CirFile cirFile1,CirFile cirFile2) {
		int result = 0;
		
		ArrayList<Wire> cablesCir1 = cirFile1.getCables();
		ArrayList<Wire> cablesCir2 = cirFile2.getCables();
		
		for(Wire wireCir1 : cablesCir1){
			for(Wire wireCir2 : cablesCir2){
				if(wireCir1.getFrom().equals(wireCir2.getFrom())){
					if(wireCir1.getTo().equals(wireCir2.getTo())){
						result ++;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Comparamos el número de componentes identicos entre los dos circuitos
	 * Dos componentes son idénticos cuando la posición en la que se encuentran conincide y
	 * el tipo de componente es el mismo
	 * @param cirFilePivot
	 * @param cirFileToCompare
	 */
	public static int makeCompareEqualComponents(CirFile cirFile1,CirFile cirFile2) {
		int result = 0;
		
		ArrayList<Component> componentsCir1 = cirFile1.getComponentes();
		ArrayList<Component> componentsCir2 = cirFile2.getComponentes();
		
		for(Component compCir1 : componentsCir1){
			for(Component compCir2 : componentsCir2){
				if(compCir1.getLocation().equals(compCir2.getLocation())){
					if(compCir1.getName().equals(compCir2.getName())){
						if(compCir1.getAtributes().size() == compCir2.getAtributes().size()){
							result ++;
						}
						
					}
				}
			}
		}
		
		return result;
		
	}
	
	/**
	 * Método que determina el grado de similitur en % entre los dos circuitos
	 * en base a distintos parametros calculados previamente
	 * (solo tiene sentido si se han aplicado previamente todos los metodos makeCompareEqualWires y makeCompareEqualComponents de esta classe)
	 * @param resultComparator
	 * @return
	 */
	public static double makeEqualComparatorPerc(ResultComparator resultComparator){
		double responseValue=0;
		
		int equalWires = resultComparator.getNumEqualWires();
		int equalComp = resultComparator.getNumEqualComponents();
		
		int totalWiresCir1 = resultComparator.getNumWiresCir1();
		int totalWiresCir2 = resultComparator.getNumWiresCir2();
		
		int totalComponentsCir1 = resultComparator.getNumComponentsCir1();
		int totalComponentsCir2 = resultComparator.getNumComponentsCir2();
		
		if(totalComponentsCir1 == totalComponentsCir2 && totalWiresCir1 == totalWiresCir2 && equalWires == totalWiresCir1 && equalComp==totalComponentsCir1){
			//Se trata de circuitos exactamente iguales en número de componentes y número de cables, 
			//ademas de que todos los cables y componentes son exactamente iguales, por tanto es un plagio identico de cricuitos
			//y se descarta cualquier otra técnica de comparación
			responseValue = 100;
		}else{
			double avarageTotalWires = (double)(totalWiresCir1+totalWiresCir2)/2;
			double avarageTotalComponents = (double)(totalComponentsCir1+totalComponentsCir2)/2;
			
			
			double percEqualWires = 100-((double)(avarageTotalWires-equalWires)/avarageTotalWires)*100;
			double percEqualComponents =100- ((double)(avarageTotalComponents-equalComp)/avarageTotalComponents)*100;
			
			responseValue = (percEqualWires + percEqualComponents)/2;
		}
		
		return responseValue;
	}
	
	
	
	
	/**
	 * Este método se encarga de contar el número de componentes
	 * del mismo tipo entre los circuitos, circuitos mas similiares tendran 
	 * el mismo número de componentes del mismo tipo
	 * El resultado final se expresa en % donde un número cercano al 100
	 * se entiende como un alto grado de comparación y 0 indica que no compraten 
	 * ese componente en concreto
	 * @param cirFile1
	 * @param cirFile2
	 * @return
	 */
	public static TreeMap<String,Double> compareNumberComponentsPerc(CirFile cirFile1,CirFile cirFile2){
		TreeMap<String,Double> totalResultMap = new TreeMap<String,Double>();
		
		HashMap<String,Integer> resultMapCir1 = numCircuitComponents(cirFile1);
		HashMap<String,Integer> resultMapCir2 = numCircuitComponents(cirFile2);
		
		for (Map.Entry<String, Integer> entry : resultMapCir1.entrySet()) {
		    String componentType = entry.getKey();
		    int valCir1 = entry.getValue();
		    if(resultMapCir2.containsKey(componentType)){
		    	int valCir2 = resultMapCir2.get(componentType);
		    	resultMapCir2.remove(componentType);
		    	double res;
		    	if(valCir1 > valCir2){
		    		res = (((double)valCir2 / (double)valCir1)*(double)100);
		    		
		    	}else{
		    		res = (((double)valCir1 / (double)valCir2)*(double)100);
		    	}
		    	totalResultMap.put(componentType, res);
		    }else{
		    	totalResultMap.put(componentType, (double) 0);
		    }

		}
		
		if(!resultMapCir2.isEmpty()){
			for (Map.Entry<String, Integer> entry : resultMapCir2.entrySet()) {
				String componentType = entry.getKey();
				totalResultMap.put(componentType, (double) 0);
			}
		}
		
		return totalResultMap;
	}
	
	public static HashMap<String,Integer> numCircuitComponents(CirFile cirFile){
		HashMap<String,Integer> resultMapCir = new HashMap<String,Integer>();
		ArrayList<Component> componentsListCir = cirFile.getComponentes();
		for(Component component : componentsListCir){
			if(resultMapCir.containsKey(component.getName())){
				int num = resultMapCir.get(component.getName());
				num += 1;
				resultMapCir.put(component.getName(), num);
			}else{
				resultMapCir.put(component.getName(), 1);
			}
		}
		return resultMapCir;
	}
	
	
	/**
	 * Determina si la posición de los componentes del mismo tipo del circuito es identica
	 */
	public static HashMap<String,Integer> equalPositionComponents(CirFile cirFile1,CirFile cirFile2){
		HashMap<String,Integer> resultCompare = new HashMap<String,Integer>();
		ArrayList<Component> arrComponentesCir1 = cirFile1.getComponentes();
		ArrayList<Component> arrComponentesCir2 = cirFile2.getComponentes();
		
		for(Component componentCir1 : arrComponentesCir1){
			for(Component componentCir2 : arrComponentesCir2){
				if(componentCir1.getName().equals(componentCir2.getName())){
					if(componentCir1.getLocation().equals(componentCir2.getLocation())){
						if(resultCompare.containsKey(componentCir1.getName())){
							int resultCount = resultCompare.get(componentCir1.getName());
							resultCount++;
							resultCompare.put(componentCir1.getName(), resultCount);
						}else{
							resultCompare.put(componentCir1.getName(), 1);
						}
					}
				}
			}
		}
		return resultCompare;
	}
	
	/**
	 * Determina si los cable empiezan o acaban en la misma posición
	 * @param cirFile1
	 * @param cirFile2
	 * @return
	 */
	public static int equalWiresPosition(CirFile cirFile1,CirFile cirFile2){
		int totalEqPosWires = 0;
		ArrayList<Wire> arrCablesCir1 = cirFile1.getCables();
		ArrayList<Wire> arrCablesCir2 = cirFile2.getCables();
		
		for(Wire wireCir1 : arrCablesCir1){
			for(Wire wireCir2 : arrCablesCir2){
				if(wireCir1.getTo().equals(wireCir2.getTo()) || wireCir1.getFrom().equals(wireCir2.getFrom())){
					totalEqPosWires ++;
				}
			}
		}
		
		
		return totalEqPosWires;
	}
	
	/**
	 * Determina si la longitud de los cables son iguales para aquellos en los que coincida la posición de inicio o final
	 * @param cirFile1
	 * @param cirFile2
	 * @return
	 */
	public static int equalWiresLength(CirFile cirFile1,CirFile cirFile2){
		int totalEqLengthWires = 0;
		ArrayList<Wire> arrCablesCir1 = cirFile1.getCables();
		ArrayList<Wire> arrCablesCir2 = cirFile2.getCables();
		
		for(Wire wireCir1 : arrCablesCir1){
			for(Wire wireCir2 : arrCablesCir2){
				String toWire1 = wireCir1.getTo();
				String fromWire1 = wireCir1.getFrom();
				String toWire2 = wireCir2.getTo();
				String fromWire2 = wireCir2.getFrom();
				
				//Calculamos la distancia del cable 1
				String auxX1 = fromWire1.split(",")[0].replace("(", "");
				String auxX2 = toWire1.split(",")[0].replace("(", "");
				String auxY1 = fromWire1.split(",")[1].replace(")", "");
				String auxY2 = toWire1.split(",")[1].replace(")", "");
				int x1 = Integer.parseInt(auxX1);
				int x2 = Integer.parseInt(auxX2);
				int y1 = Integer.parseInt(auxY1);
				int y2 = Integer.parseInt(auxY2);
				
				double aux1 = Math.pow((x2-x1), 2);
				double aux2 = Math.pow((y2-y1), 2);
				double longitudWire1 = Math.sqrt((aux1+aux2));
				
				//Calculamos la distancia del cable 2
				auxX1 = fromWire2.split(",")[0].replace("(", "");
				auxX2 = toWire2.split(",")[0].replace("(", "");
				auxY1 = fromWire2.split(",")[1].replace(")", "");
				auxY2 = toWire2.split(",")[1].replace(")", "");
				x1 = Integer.parseInt(auxX1);
				x2 = Integer.parseInt(auxX2);
				y1 = Integer.parseInt(auxY1);
				y2 = Integer.parseInt(auxY2);
				
				aux1 = Math.pow((x2-x1), 2);
				aux2 = Math.pow((y2-y1), 2);
				double longitudWire2 = Math.sqrt((aux1+aux2));
				if((longitudWire1-longitudWire2) == 0 && (toWire1.equals(toWire2) || fromWire1.equals(fromWire2))){
					totalEqLengthWires++;
				}
			}
		}
		
		return totalEqLengthWires;
	}
	
	/**
	 * Comprueba si los elementos que forman el circuito estan conectados de la misma manera en posiciones similares (+-5)
	 */
	public static int checkCircuitDesign(CirFile cirFile1,CirFile cirFile2){
		
		HashMap<String,String> conexionResultCir1 = getTotalConexiones(cirFile1);
		HashMap<String,String> conexionResultCir2 = getTotalConexiones(cirFile2);
		int conexionesIguales = 0;
		
		//Miramos si los elementos estan conectados del mismo modo
		for(String conexionCir1 : conexionResultCir1.keySet()){
			for(String conexionCir2 : conexionResultCir2.keySet()){
				if(conexionCir1.equals(conexionCir2)){
					//Miramos si lo elementos conectados del mismo modo coinciden en posiciones similares
					String locConexion1 = conexionResultCir1.get(conexionCir1);
					String locConexion2 = conexionResultCir2.get(conexionCir2);
					int locCon1X = Integer.parseInt(locConexion1.split("/")[0]);
					int locCon1Y = Integer.parseInt(locConexion1.split("/")[1]);
					
					int locCon2X = Integer.parseInt(locConexion2.split("/")[0]);
					int locCon2Y = Integer.parseInt(locConexion2.split("/")[1]);
					
					//Si la posicion de las conexiones en X son similares (variación de +- 5)
					boolean condicion1 = false;
					if(locCon1X == locCon2X){
						condicion1 = true;
					}else if(locCon1X < locCon2X){
						if((locCon2X-locCon1X) < 5){
							condicion1 = true;
						}
					}else if(locCon1X > locCon2X){
						if((locCon1X-locCon2X) < 5){
							condicion1 = true;
						}
					}
					
					//Si la posicion de las conexiones en Y son similares (variación de +- 5)
					boolean condicion2 = false;
					if(locCon1Y == locCon2Y){
						condicion2 = true;
					}else if(locCon1Y < locCon2Y){
						if((locCon2Y-locCon1Y) < 5){
							condicion2 = true;
						}
					}else if(locCon1Y > locCon2Y){
						if((locCon1Y-locCon2Y) < 5){
							condicion2 = true;
						}
					}
					
					//Si se cumplen alguno de los dos criterios tenemos un caso de similitud en la conexion de elementos en posciones similares
					if(condicion1 || condicion2){
						conexionesIguales++;
					}
				}
			}
		}
		return conexionesIguales;
	}
	
	public static HashMap<String, String> getTotalConexiones(CirFile cirFile) {
		HashMap<String, String> conexionResultCir = new HashMap<String, String>();
		ArrayList<Wire> arrCablesCir = cirFile.getCables();
		ArrayList<Component> arrComponentesCir = cirFile.getComponentes();
		
		// Recorremos el circuito 1 buscando las posiciones de conexion de los
		// elementos
		for (Wire wireCir : arrCablesCir) {
			String[] from = wireCir.getFrom().split(",");
			String[] to = wireCir.getTo().split(",");
			int fromX = Integer.parseInt(from[0].replace("(", ""));
			int fromY = Integer.parseInt(from[1].replace(")", ""));
			int toX = Integer.parseInt(to[0].replace("(", ""));
			int toY = Integer.parseInt(to[1].replace(")", ""));
			for (Component componentCir : arrComponentesCir) {
				String[] loc = componentCir.getLocation().split(",");
				int locX = Integer.parseInt(loc[0].replace("(", ""));
				int locY = Integer.parseInt(loc[1].replace(")", ""));
				if (fromX == locX || fromY == locY) {
					conexionResultCir.put(componentCir.getName() + "-wire",fromX + "/" + fromY);
				} else if (toX == locX || toY == locY) {
					conexionResultCir.put("wire-" + componentCir.getName(),toX + "/" + toY);
				}
			}
		}

		return conexionResultCir;
	}

}
