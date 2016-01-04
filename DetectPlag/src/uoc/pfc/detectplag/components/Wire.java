package uoc.pfc.detectplag.components;


/**
 * Entidad que representa el objeto Cable en un archivo de tipo .cir
 * @author add
 *
 */
public class Wire {

	private String from;
	private String to;
	
	
	
	public Wire(String from, String to) {
		this.from = from;
		this.to = to;
	}
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	
}
