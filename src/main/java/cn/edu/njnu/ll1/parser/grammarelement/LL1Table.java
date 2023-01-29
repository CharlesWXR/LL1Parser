package cn.edu.njnu.ll1.parser.grammarelement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LL1Table {
	private Symbol beginning = null;
	private Map<Long, List<Symbol>> table = new HashMap<Long, List<Symbol>>();
	private Map<String, Integer> nonTerminalMapper = new HashMap<String, Integer>();
	private Map<Object, Integer> terminalMapper = new HashMap<Object, Integer>();

	public void init(GrammarContent content) {
		// Copy the lex analyse result
		this.beginning = content.getBeginning();

		// Assign a symbol with a number for quick query
		List<Symbol> terminals = content.getTerminalSymbols();
		for (int i = 0; i < terminals.size(); i++) {
			this.terminalMapper.put(terminals.get(i).type, i);
		}
		this.terminalMapper.put(Symbol.End.type, this.terminalMapper.size());

		List<Symbol> nonTerminals = content.getNonTerminalSymbols();
		for (int i = 0; i < nonTerminals.size(); i++) {
			this.nonTerminalMapper.put(nonTerminals.get(i).identifier, i);
		}

		// Convert into a table, change M(a, b) into M(Hash(a, b))
		Map<String, List<GrammarSentence>> grammar = content.getGrammar();
		for (Map.Entry<String, List<GrammarSentence>> entry : grammar.entrySet()) {
			String identifier = entry.getKey();
			List<GrammarSentence> rules = entry.getValue();
			for (GrammarSentence rule : rules) {
				for (Symbol input : rule.selects) {
					this.table.put(getID(identifier, input.type), rule.contents);
				}
			}
		}
	}

	private long getID(String nonTerminal, Object input) {
		// simply hash the table row and col as the symbols are assign with numbers
		// Hash(a, b) = f(a) << 32 | g(b) << 32
		if (this.nonTerminalMapper.get(nonTerminal) == null)
			return -1;
		if (this.terminalMapper.get(input) == null)
			return -1;

		int nonTerminalID = this.nonTerminalMapper.get(nonTerminal);
		int inputID = this.terminalMapper.get(input);
		return (long) nonTerminalID << 32 | (long) inputID;
	}

	public Symbol getBeginning() {
		return beginning;
	}

	public List<Symbol> getNext(String present, Object inputType) {
		long id = getID(present, inputType);
		if (id == -1)
			return null;

		return this.table.get(id);
	}

	public Map<Long, List<Symbol>> getTable() {
		return table;
	}

	public Map<String, Integer> getNonTerminalMapper() {
		return nonTerminalMapper;
	}

	public Map<Object, Integer> getTerminalMapper() {
		return terminalMapper;
	}
}
