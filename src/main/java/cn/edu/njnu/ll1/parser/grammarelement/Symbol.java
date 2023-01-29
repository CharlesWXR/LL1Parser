package cn.edu.njnu.ll1.grammarelement;


import cn.edu.njnu.ll1.wordelement.TypeEnum;

public class Symbol {
	public boolean isTerminal;
	public String identifier;
	public Object type;

	public static final Symbol Empty = new Symbol(true, "empty", TypeEnum.Empty);
	public static final Symbol End = new Symbol(true, "end", TypeEnum.End);

	@Override
	public String toString() {
		return "Symbol{" +
				"isTerminal=" + isTerminal +
				", identifier='" + identifier + '\'' +
				", type=" + type +
				'}';
	}

	@Override
	public int hashCode() {
		int res = 0;
		if (this.type != null) {
			Enum type = (Enum) this.type;
			res = type.ordinal();
		}
		res += this.identifier.hashCode();
		res *= this.isTerminal ? -1 : 1;
		return res;
	}

	public Symbol() {
	}

	public Symbol(boolean isTerminal, String identifier, Object type) {
		this.isTerminal = isTerminal;
		this.identifier = identifier;
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Symbol symbol = (Symbol) o;
		return this.isTerminal == symbol.isTerminal &&
				this.identifier.equals(symbol.identifier);
	}
}
