package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Hashtable;

import lexer.*;

public class Main {

	public static void main(String[] args) {
		try {
			FileReader reader = new FileReader("C:/Users/Femi/Documents/workspace/Compiler/test.txt");
			Lexer lexer = new Lexer(reader);
			Hashtable<String, Token> tokens = lexer.scan();
			int lastIndex = lexer.getTokenHashIndex();
			
			for(int i = 1; i <= lastIndex; i++)
			{
				Token token = (Token)tokens.get("token"+i);
				System.out.println("token" + i + " value: " + token.value);
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found!");
			e.printStackTrace();
		}
	}
}
