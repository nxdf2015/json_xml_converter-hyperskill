package converter;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



public class Converter {
	 public String expandXML(String line){
	        return expandXML(line,"");
	    }
       
	    public String expandXML(String line, String path) {

	       XMLParser parser = new XMLParser(line);
	       StringBuilder builder = new StringBuilder();
	       
	       path += (path.isEmpty()? "" : ", ") + parser.tag();
	       builder.append("Element:")
	       .append(System.lineSeparator())
	       .append("path = "+path+System.lineSeparator());
	       List<Tuple<String>>  attributes = parser.getAttributes();
	       
	      // System.out.println("attributes : " + attributes);
	        
	       if(parser.hasNestedElement()) {
	    	   if(!attributes.isEmpty()) {
				  builder.append("attributes:\n");
				   for (Tuple<String> attribute  : attributes) {
					   builder.append(attribute.first() + " = " + attribute.last())
							   .append(System.lineSeparator());
				   }
			   }
	    	   
	    	  
	    	   for(String nestedContent : parser.getNestedElement()) {
	    	   builder.append(expandXML(nestedContent,path));
	    	   }
	       }
	       else {
	    	   String valueContent = parser.content().equals("null")   ? "null" : "\""+parser.content()+"\"";
	    	   builder.append("value =  "+ valueContent  +System.lineSeparator());
			   if(!attributes.isEmpty()) {
				  builder.append("attributes:\n");
				   for (Tuple<String> attribute  : attributes) {
					   builder.append(attribute.first() + " = " + attribute.last())
							   .append(System.lineSeparator());
				   }
			   }
	    	   
	       }
	       
	       
	       return builder.toString();
	    }
}
