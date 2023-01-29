package cn.edu.njnu.ll1.generator;

import cn.edu.njnu.ll1.grammarelement.GrammarContent;
import cn.edu.njnu.ll1.grammarelement.LL1Table;
import cn.edu.njnu.ll1.grammarelement.Symbol;

import java.util.ArrayList;
import java.util.List;

public class TableDriver {
	private LL1Table table = new LL1Table();
	List<Symbol> symbolStack = new ArrayList<Symbol>();

	public void init(GrammarContent grammarContent) {
		this.table.init(grammarContent);
		this.symbolStack.clear();
		this.symbolStack.add(Symbol.End);
		this.symbolStack.add(table.getBeginning());
	}

	public void init() {
		this.symbolStack.clear();
		this.symbolStack.add(Symbol.End);
		this.symbolStack.add(table.getBeginning());
	}


}
