package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLDefeater {
	
	private String input;
	private Pattern cdataPattern;
	private Pattern symbolPattern;
	private Pattern startTagPattern;
	private Pattern endTagPattern;
	private Pattern genericTagPattern;
	private Matcher matcher;
	private ArrayList<String> symbols;
	private HashMap<String, String> htmlNameCodesMap;
	
	public XMLDefeater() {
		this.symbols = new ArrayList<String>(Arrays.asList(new String[] {"&amp"}));
		htmlNameCodesMap = new HashMap<String, String>() {{
		    put("lt", "&lt;");
		    put("gt", "&gt;");
		    put("apos", "&apos;");
		    put("quote", "&quot;");
		}};
		
		this.input = "<root attr=\"abc\">\r\n"
					+ "    <tag1>\r\n"
					+ "        valid xml\r\n"
					+ "    </tag1>\r\n"
					+ "    <tag2>\r\n"
					+ "        invalid & xml <.99 2<3 r&f\r\n"
					+ "    </tag2>\r\n"
					+ "    <tag3>\r\n"
					+ "        <![CDATA[asasdad & adas]]>\r\n"
					+ "    </tag3>\r\n"
					+ "    <tag4>\r\n"
					+ "        <![CDATA[asasdad adas]]>\r\n"
					+ "    </tag4>\r\n"
					+ "    <tag5>\r\n"
					+ "        33 &amp; 2 > ref\r\n"
					+ "    </tag5>\r\n"
					+ "</root>";
		this.cdataPattern = Pattern.compile(".*CDATA\\[.*\\].*");
		this.symbolPattern = Pattern.compile(".*\\s(&.*;).*");
		this.startTagPattern = Pattern.compile(".*<\\w+>.*");
		this.endTagPattern = Pattern.compile(".*<\\/\\w+>.*");
		this.genericTagPattern = Pattern.compile("<.*>\\s*");
	}
	
	public void getCorrectXml() {
		//split the input in order to separate the tags and contents
		String[] array = input.split("\r\n");
		
		for(int i = 0; i < array.length; i++) {
			
			this.matcher = this.cdataPattern.matcher(array[i]);
			//if it is not a cdata tag
			if(!this.matcher.matches()) {
				this.matcher = this.symbolPattern.matcher(array[i]);
				
				//if it is a symbol like "&something;"
				if(this.matcher.matches()) {
					//if the symbol is contained in the list of symbols
					if(!isSymbol(array[i])) {
						array[i].replaceAll("&", "");
						replaceSymbols(array, i);
					}
					//split in multiple elements in an array because in a line could be that a symbol and an invalid content is present
				}
				else {
					array[i] = array[i].replaceAll("&", " ");
					replaceSymbols(array, i);
				}
			}
			System.out.println(array[i]);
		}
	}
	
	private boolean isSymbol(String substr) {
		for(String symbol : this.symbols) {
			if(symbol.equalsIgnoreCase(substr))
				return true;
		}
		return false;
	}
	
	private void replaceSymbols(String[] array, int i) {
		if(!(this.startTagPattern.matcher(array[i]).matches() ||
				 this.endTagPattern.matcher(array[i]).matches() ||
				 this.genericTagPattern.matcher(array[i]).matches())) {
				array[i] = array[i].replaceAll("<", htmlNameCodesMap.get("lt"));
				array[i] = array[i].replaceAll(">", htmlNameCodesMap.get("gt"));
				array[i] = array[i].replaceAll("'", htmlNameCodesMap.get("apos"));
				array[i] = array[i].replaceAll("\"", htmlNameCodesMap.get("quote"));
			}
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public Pattern getCdataPattern() {
		return cdataPattern;
	}

	public void setCdataPattern(Pattern cdataPattern) {
		this.cdataPattern = cdataPattern;
	}

	public Pattern getSymbolPattern() {
		return symbolPattern;
	}

	public void setSymbolPattern(Pattern symbolPattern) {
		this.symbolPattern = symbolPattern;
	}

	public Pattern getStartTagPattern() {
		return startTagPattern;
	}

	public void setStartTagPattern(Pattern startTagPattern) {
		this.startTagPattern = startTagPattern;
	}

	public Pattern getEndTagPattern() {
		return endTagPattern;
	}

	public void setEndTagPattern(Pattern endTagPattern) {
		this.endTagPattern = endTagPattern;
	}

	public Pattern getGenericTagPattern() {
		return genericTagPattern;
	}

	public void setGenericTagPattern(Pattern genericTagPattern) {
		this.genericTagPattern = genericTagPattern;
	}

	public Matcher getMatcher() {
		return matcher;
	}

	public void setMatcher(Matcher matcher) {
		this.matcher = matcher;
	}

	public ArrayList<String> getSymbols() {
		return symbols;
	}

	public void setSymbols(ArrayList<String> symbols) {
		this.symbols = symbols;
	}

	public HashMap<String, String> getHtmlNameCodesMap() {
		return htmlNameCodesMap;
	}

	public void setHtmlNameCodesMap(HashMap<String, String> htmlNameCodesMap) {
		this.htmlNameCodesMap = htmlNameCodesMap;
	}
}
