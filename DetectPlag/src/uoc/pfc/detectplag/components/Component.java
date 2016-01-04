package uoc.pfc.detectplag.components;

import java.util.ArrayList;

/**
 * Entidad que representa el objeto Component en un archivo de tipo .cir
 * @author add
 *
 */
public class Component {


	private String lib;
	private String name;
	private String location;
	private ArrayList<Attributes> atributes = new ArrayList<Attributes>();
	
	
	public Component(String lib, String name, String location) {
		
		this.lib = lib;
		this.name = name;
		this.location = location;
	}
	public String getLib() {
		return lib;
	}
	public void setLib(String lib) {
		this.lib = lib;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public ArrayList<Attributes> getAtributes() {
		return atributes;
	}
	public void setAtributes(String name, String value) {
		
		Attributes attribute = new Attributes(name, value);
		atributes.add(attribute);
		
	}
	
	
	
}
