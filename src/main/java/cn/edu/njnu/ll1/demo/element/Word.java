package cn.edu.njnu.ll1.demo.element;

import cn.edu.njnu.ll1.parser.wordelement.EnumBase;

public class Word {
	protected EnumBase type;
	protected Object content;

	public Word(EnumBase type, Object content) {
		this.type = type;
		this.content = content;
	}

	public EnumBase getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Word{" +
				"type=" + type +
				", content=" + content +
				'}';
	}

	public void setType(EnumBase type) {
		this.type = type;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
}
