package cn.edu.njnu.ll1.grammarelement;

import cn.edu.njnu.ll1.exception.GrammarDescriptorException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrammarContent {
	private Map<String, List<GrammarSentence>> grammar = new HashMap<String, List<GrammarSentence>>();
	private Symbol beginning = new Symbol();
	private List<Symbol> terminalSymbols = new ArrayList<Symbol>();
	private List<Symbol> nonTerminalSymbols = new ArrayList<Symbol>();

	private Map<Symbol, Set<Symbol>> firsts = new HashMap<Symbol, Set<Symbol>>();
	private Map<Symbol, Set<Symbol>> follows = new HashMap<Symbol, Set<Symbol>>();

	private static final String RuleElement = "\\$\\{(.+?)}";
	private static final Pattern RuleElementPattern = Pattern.compile(RuleElement);

	@Override
	public String toString() {
		return "GrammarContent{" +
				"grammar=" + grammar.toString() +
				", beginning=" + beginning +
				", terminalSymbols=" + terminalSymbols +
				", nonTerminalSymbols=" + nonTerminalSymbols +
				'}';
	}

	public boolean setBeginning(String beginning) {
		Symbol symbol = new Symbol();
		symbol.isTerminal = false;
		symbol.identifier = beginning;

		if (!this.nonTerminalSymbols.contains(symbol))
			return false;

		this.beginning = symbol;
		return true;
	}

	public boolean addTerminalSymbol(Symbol symbol) {
		if (this.terminalSymbols.contains(symbol))
			return false;

		this.terminalSymbols.add(symbol);
		return true;
	}

	public boolean addNonTerminalSymbol(Symbol symbol) {
		if (this.nonTerminalSymbols.contains(symbol))
			return false;

		this.nonTerminalSymbols.add(symbol);
		return true;
	}

	public boolean addRules(String symbol, List<String> rules) throws Exception {
		for (Symbol s : this.nonTerminalSymbols) {
			if (s.identifier.equals(symbol)) {
				for (String rule : rules) {
					List<String> elements = new ArrayList<String>();
					Matcher matcher = RuleElementPattern.matcher(rule);
					while (matcher.find()) {
						elements.add(matcher.group(1).trim());
					}

					if (elements.size() == 0)
						return false;
					addRule(symbol, elements);
				}
				return true;
			}
		}
		return false;
	}

	private void addRule(String symbol, List<String> rule) throws Exception {
		List<GrammarSentence> target = this.grammar.get(symbol);
		if (null == target) {
			this.grammar.put(symbol, new ArrayList<GrammarSentence>());
			target = this.grammar.get(symbol);
		}

		GrammarSentence gs = new GrammarSentence();
		for (String str : rule) {
			if (!addRule(str, gs))
				throw new GrammarDescriptorException(
						GrammarDescriptorException.UndeclaredIdentifier + str);
		}
		target.add(gs);
	}

	private boolean addRule(String str, GrammarSentence gs) {
		Symbol tempSymbol = new Symbol();
		boolean found = false;

		tempSymbol.identifier = str;
		for (Symbol s : this.terminalSymbols) {
			if (s.identifier.equals(str)) {
				found = true;
				tempSymbol.isTerminal = true;
				tempSymbol.type = s.type;
				break;
			}
		}
		if (!found) {
			for (Symbol s : this.nonTerminalSymbols) {
				if (s.identifier.equals(str)) {
					found = true;
					tempSymbol.isTerminal = false;
					break;
				}
			}
		}

		if (!found)
			return false;

		gs.contents.add(tempSymbol);
		return true;
	}

	private Set<Symbol> getFirst(Symbol s) {
		if (s.isTerminal) {
			Set<Symbol> result = new HashSet<>();
			result.add(s);
			return result;
		}

		Set<Symbol> first = this.firsts.get(s);
		if (first == null) {
			first = new HashSet<Symbol>();
			this.firsts.put(s, first);
		} else
			return first;

		List<GrammarSentence> rules = this.grammar.get(s.identifier);
		if (rules == null)
			return null;

		for (GrammarSentence gs : rules) {
			for (int i = 0; i < gs.contents.size(); i++) {
				Symbol temp = gs.contents.get(i);
				if (temp.isTerminal) {
					first.add(temp);
					break;
				} else if (!s.identifier.equals(temp.identifier)) {
					Set<Symbol> recFirst = this.firsts.get(temp.identifier);
					if (recFirst == null)
						recFirst = getFirst(temp);

					if (!recFirst.contains(Symbol.Empty)) {
						first.addAll(recFirst);
						break;
					}
					if (i != gs.contents.size() - 1) {
						recFirst.remove(Symbol.Empty);
						first.addAll(recFirst);
					}
				}
			}
		}
		this.firsts.put(s, first);
		return first;
	}

	private Set<Symbol> getFollow(Symbol s) {
		Set<Symbol> follow = this.follows.get(s);
		if (follow == null) {
			follow = new HashSet<Symbol>();
			if (s.equals(this.beginning))
				follow.add(Symbol.End);

			this.follows.put(s, follow);
		} else
			return follow;

		for (Map.Entry<String, List<GrammarSentence>> entry : grammar.entrySet()) {
			List<GrammarSentence> rules = entry.getValue();
			for (GrammarSentence rule : rules) {
				boolean found = false;
				for (int i = 0; i < rule.contents.size(); i++) {
					if (found) {
						Set<Symbol> first = getFirst(rule.contents.get(i));
						if (first.contains(Symbol.Empty))
							first.remove(Symbol.Empty);
						else
							found = false;
						follow.addAll(first);
					} else if (s.equals(rule.contents.get(i))) {
						found = true;
					}
				}
				if (found) {
					if (!entry.getKey().equals(s.identifier)) {
						follow.addAll(getFollow(new Symbol(false, entry.getKey(), null)));
					}
				}
			}
		}

		this.follows.put(s, follow);
		return follow;
	}

	public Set<Symbol> getSelect(Symbol s, int index) {
		Set<Symbol> select = new HashSet<Symbol>();

		GrammarSentence gs = this.grammar.get(s.identifier).get(index);

		int i = 0;
		for (; i < gs.contents.size(); i++) {
			Symbol temp = gs.contents.get(i);
			if (temp.isTerminal && !temp.equals(Symbol.Empty)) {
				select.add(temp);
				break;
			} else {
				Set<Symbol> first = getFirst(temp);
				if (!first.contains(Symbol.Empty)) {
					select.addAll(first);
					break;
				} else {
					first.remove(Symbol.Empty);
					select.addAll(first);
				}
			}
		}
		if (i == gs.contents.size())
			select.addAll(getFollow(s));

		gs.selects = select;
		return select;
	}

	public void process() {
		processFirst();
		processFollow();
		processSelect();
	}

	private void processFirst() {
		for (Symbol s : this.nonTerminalSymbols)
			getFirst(s);
	}

	private void processFollow() {
		for (Symbol s : this.nonTerminalSymbols)
			getFollow(s);
	}

	private void processSelect() {
		for (Map.Entry<String, List<GrammarSentence>> entry : this.grammar.entrySet()) {
			List<GrammarSentence> rules = entry.getValue();
			Symbol temp = new Symbol(false, entry.getKey(), null);
			for (int i = 0; i < rules.size(); i++) {
				getSelect(temp, i);
			}
		}
	}

	public Map<String, List<GrammarSentence>> getGrammar() {
		return grammar;
	}

	public Symbol getBeginning() {
		return beginning;
	}

	public List<Symbol> getTerminalSymbols() {
		return terminalSymbols;
	}

	public List<Symbol> getNonTerminalSymbols() {
		return nonTerminalSymbols;
	}
}
