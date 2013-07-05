package lexer;

public class Token {
	
	public String name, value, type;
	
	public Token(String n) {
		name = n;
	}
	
	public Token(String n, String v) {
		name = n;
		value = v;
	}
	
	public Token(String n, String v, String t) {
		name = n;
		value = v;
		type = t;
	}
}
