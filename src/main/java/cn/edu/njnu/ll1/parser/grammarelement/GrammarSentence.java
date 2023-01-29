package cn.edu.njnu.ll1.parser.grammarelement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GrammarSentence {
	// Simply to attach a rule to select-set for LL1table process
	public List<Symbol> contents = new ArrayList<Symbol>();
	public Set<Symbol> selects = new HashSet<Symbol>();

	public boolean add(Symbol s) {
		return contents.add(s);
	}
}
