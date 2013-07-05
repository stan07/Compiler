package lexer;

import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.NoSuchElementException;

public class Lexer {
	private FileReader reader;
	private char peek = ' ';
	private Hashtable<String, String> keywords;
	private Hashtable<String, Token> tokens;
	private int hashIndex = 0;
	
	public Lexer(FileReader reader) {
		this.reader = reader;
		tokens = new Hashtable<String, Token>();
		keywords = new Hashtable<String, String>(); 
		
		keywords.put("digit", "int");
		keywords.put("decimal", "double");
		keywords.put("letter", "String");
		keywords.put("bool", "boolean");
		keywords.put("true", "true");
		keywords.put("false", "false");
		keywords.put("given", "if");
		keywords.put("then", "{");
		keywords.put("else", "else");
		keywords.put("still", "while");
		keywords.put("since", "for");
		keywords.put("done", "}");
	}
	
	public int getTokenHashIndex() {
		return hashIndex;
	}
	
	public Hashtable<String, String> getKeyHashtable() {
		return keywords;
	}
	
	private boolean nextChar(char c) throws IOException {
		peek = (char)reader.read();
		
		if(peek != c)
			return false;
		
		peek = ' ';
		return true;
	}
	
	public Hashtable<String, Token> scan() {
		try {
			peek = (char)reader.read();

			while(peek != (char)-1)
			{
				while(Character.isWhitespace(peek))
					peek = (char)reader.read();
						
				if(peek == '_')
				{
					StringBuffer buffer = new StringBuffer();
					do
					{
						buffer.append(peek);
						peek = (char)reader.read();
					}while(Character.isLetter(peek));
						
					String varname = buffer.toString();
					tokens.put("token"+(++hashIndex), new Token("id", varname));
				}
					
				else if(Character.isDigit(peek))
				{
					int digit = 0;
					do
					{
						digit = 10 * digit + Character.digit(peek, 10);
						peek = (char)reader.read();
					}while(Character.isDigit(peek));
						
					if(peek != '.')
						tokens.put("token"+(++hashIndex), new Token("number", String.valueOf(digit), "digit"));
						
					else
					{
						double decimal = digit, d = 10;
						for(;;)
						{
							peek = (char)reader.read();
							if(!Character.isDigit(peek))
								break;
							decimal = decimal + Character.digit(peek, 10) / d;
							d = d * 10;
						}
						tokens.put("token"+(++hashIndex), new Token("number", String.valueOf(decimal), "decimal"));
					}
				}
					
				else if(Character.isLetter(peek))
				{
					StringBuffer buffer = new StringBuffer();
					do
					{	
						buffer.append(peek);
						peek = (char)reader.read();
					}while(Character.isLetter(peek));
					
					String word = buffer.toString();
					String value = (String)keywords.get(word);
					
					if(value != null)
						tokens.put("token"+(++hashIndex), new Token(word, value));
						
					else
						tokens.put("token"+(++hashIndex), new Token("word", word));
				}
					
				else
				{
					switch(peek){
						
					case '{':
						tokens.put("token"+(++hashIndex), new Token("begin", "{"));
						break;
							
					case '}':
						tokens.put("token"+(++hashIndex), new Token("end", "}"));
						break;
							
					case ';':
						tokens.put("token"+(++hashIndex), new Token("endline", ";"));
						break;
						
					case '+':
						tokens.put("token"+(++hashIndex), new Token("arithop", "+"));
						break;
						
					case '-':
						tokens.put("token"+(++hashIndex), new Token("arithop", "-"));
						break;
							
					case '*':
						tokens.put("token"+(++hashIndex), new Token("arithop", "*"));
						break;
							
					case '/':
						tokens.put("token"+(++hashIndex), new Token("arithop", "/"));
						break;
							
					case '%':
						tokens.put("token"+(++hashIndex), new Token("arithop", "%"));
						break;
							
					case '&':
						tokens.put("token"+(++hashIndex), new Token("logop", "&"));
						break;
							
					case '|':
						tokens.put("token"+(++hashIndex), new Token("logop", "|"));
						break;
						
					case '=':
						if(nextChar('='))
							tokens.put("token"+(++hashIndex), new Token("relop", "=="));
						else
							tokens.put("token"+(++hashIndex), new Token("assop", "="));
						break;
						
					case '!':
						if(nextChar('='))
						{
							tokens.put("token"+(++hashIndex), new Token("relop", "!="));
							break;
						}
						else
							throw new NoSuchElementException();
							
					case '<':
						if(nextChar('='))
							tokens.put("token"+(++hashIndex), new Token("relop", "<="));
						else
							tokens.put("token"+(++hashIndex), new Token("relop", "<"));
						break;
							
					case '>':
						if(nextChar('='))
							tokens.put("token"+(++hashIndex), new Token("relop", ">="));
						else
							tokens.put("token"+(++hashIndex), new Token("relop", ">"));
						break;
						
					default:
						throw new NoSuchElementException();
					}
					peek = (char)reader.read();
				}
			}
		} catch (IOException e) {
			System.err.println("Could not read file...");
			e.printStackTrace();
		} catch (NoSuchElementException e)
		{
			System.err.println("No such token exists...");
			e.printStackTrace();
		} 
		
		return tokens; 
	}
}