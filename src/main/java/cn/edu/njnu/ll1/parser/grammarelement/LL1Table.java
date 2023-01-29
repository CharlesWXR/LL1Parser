package cn.edu.njnu.ll1.grammarelement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LL1Table {
	private Symbol beginning = null;
	private Map<Long, List<Symbol>> table = new HashMap<Long, List<Symbol>>();
	private Map<String, Integer> nonTerminalMapper = new HashMap<String, Integer>();
	private Map<Object, Integer> terminalMapper = new HashMap<Object, Integer>();
	private boolean initialed = false;

	public void init(GrammarContent content) {
		this.beginning = content.getBeginning();

		List<Symbol> terminals = content.getTerminalSymbols();
		for (int i = 0; i < terminals.size(); i++) {
			this.terminalMapper.put(terminals.get(i).type, i);
		}

		List<Symbol> nonTerminals = content.getNonTerminalSymbols();
		for (int i = 0; i < nonTerminals.size(); i++) {
			this.nonTerminalMapper.put(nonTerminals.get(i).identifier, i);
		}

		Map<String, List<GrammarSentence>> grammar = content.getGrammar();
		for (Map.Entry<String, List<GrammarSentence>> entry : grammar.entrySet()) {
			String identifier = entry.getKey();
			List<GrammarSentence> rules = entry.getValue();
			for (GrammarSentence rule : rules) {
				for (Symbol input : rule.selects) {
					this.table.put(getID(identifier, input), rule.contents);
				}
			}
		}
	}

	private long getID(String nonTerminal, Symbol input) {
		if (this.nonTerminalMapper.get(nonTerminal) == null)
			return -1;
		if (this.terminalMapper.get(input.type) == null
			&& this.nonTerminalMapper.get(input.identifier) == null)
			return -1;

		int nonTerminalID = this.nonTerminalMapper.get(nonTerminal);
		int inputID = input.isTerminal ?
				this.terminalMapper.get(input.type) : this.nonTerminalMapper.get(input.identifier);
		return (long)nonTerminalID << 32 | (long) inputID;
	}

	public Symbol getBeginning() {
		return beginning;
	}

	public List<Symbol> getNext(String present, Symbol input) {
		long id = getID(present, input);
		if (id == -1)
			return null;

		return this.table.get(id);
	}
}
