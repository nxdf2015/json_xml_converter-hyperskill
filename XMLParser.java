package converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLParser {
	private String xmlRegEx = "(?<element><(?<tag>\\w+)(?<attributes>[^<>]*\\s*)?(>\\s*(?<content>.*)?\\s*</\\k<tag>>|\\s?/>))";
	private Matcher matcherElement;
	
	



	public XMLParser(String line) {
		Pattern xmlPattern = Pattern.compile(xmlRegEx);
		matcherElement = xmlPattern.matcher(line);
		matcherElement.find();
		}
	
	
	
	public boolean hasContent() {
		String content = matcherElement.group("content");
		return !(content  == null) && !content .isEmpty();
	}
	
	public boolean hasNestedElement() {
		 
		Matcher matcherNested = Pattern.compile(xmlRegEx).matcher(content());
	    return matcherNested.find();
	}
	
	public List<String> getNestedElement(){
		if(!hasNestedElement()) 
			return List.of();
		
		Pattern patternElement = Pattern.compile("(?<element><(?<tag>\\w+)[^<>]*?(\\s*/>|>.*?</\\s*\\k<tag>\\s?>))");
		Matcher matcher = patternElement.matcher(content());
		List<String> elements = new ArrayList<>();
		while(matcher.find()) {
			elements.add(matcher.group("element"));
		}
		return elements;
	}
	
	public String tag() {
		return matcherElement.group("tag");
	}
	
	public String content() {
		String content = matcherElement.group("content");
		 
		return content == null ? "null" : matcherElement.group("content") ;
	}
	
	public boolean hasAttributes() {
		return !matcherElement.group("attributes").isEmpty();
	}
	
	public  List<Tuple<String>> getAttributes(){
		String  attributeRegEx = "(?<attribute>(?<key>\\w+)\\s*=\\s*(?<value>[^\\s]+))";
		Pattern attributePattern = Pattern.compile(attributeRegEx);
		List<Tuple<String>> attributes = new ArrayList<>();
		//System.out.println("has attribute :" + hasAttributes());
		if (hasAttributes()) {
			
			Matcher matcherAttributes = attributePattern.matcher(matcherElement.group("attributes"));
		//	System.out.println("group attributes : " + matcherElement.group("attributes"));
			while(matcherAttributes.find()) {
				attributes.add(Tuple.of(matcherAttributes.group("key"),matcherAttributes.group("value")));
			}
		}
		return attributes;
	}
	
	}
