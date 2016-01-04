package uoc.pfc.detectplag.html;



public class CreateHTML {

	
	public static StringBuilder createHeader(String title){
		StringBuilder builder = new StringBuilder();
		builder.append("<!DOCTYPE html>");
		builder.append("<html lang=\"en\">");
		builder.append("<head><title>Detect Plagiarism Results</title></head>");
		builder.append("<body><h1>"+title+"</h1>");
		builder.append("<table border=1>");
		return builder;
	}
	
	public static StringBuilder closeHTML(){
		StringBuilder builder = new StringBuilder();
		builder.append("</table>");
		builder.append("</body>");
		builder.append("</html>");
		return builder;
	}
}
