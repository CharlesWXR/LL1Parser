package cn.edu.njnu.ll1.parser.exception;

public class GrammarDescriptorException extends Exception {
	public static final String UndeclaredIdentifier = "Undeclared Identifier: ";
	public static final String MissingNonTerminals = "Missing non-terminal symbol definitions.";
	public static final String MissingTerminals = "Missing terminal symbol definitions.";
	public static final String MissingGrammarDefinition = "Missing grammar definition.";
	public static final String IllegalGrammarBeginning = "Illegal grammar beginning: ";

	public GrammarDescriptorException() {
		super();
	}

	public GrammarDescriptorException(String msg) {
		super(msg);
	}
}
