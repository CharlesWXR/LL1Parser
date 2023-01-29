package cn.edu.njnu.ll1.generator;

import cn.edu.njnu.ll1.exception.GrammarDescriptorException;
import cn.edu.njnu.ll1.grammarelement.GrammarContent;
import cn.edu.njnu.ll1.grammarelement.Symbol;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GrammarDescriptionScanner {
	private cn.edu.njnu.ll1.grammarelement.GrammarContent content = new GrammarContent();
	private String[] dependencies = null;

	private static final String Dependencies = "\\$DEPENDENCIES[\\s]*\\{(.+?)}";
	private static final String Terminal = "\\$VT[\\s]*\\{(.+?)}";
	private static final String NonTerminal = "\\$VN[\\s]*\\{(.+?)}";
	private static final String GrammarContent = "G\\[(.+?)][\\s]*\\{\\{((.+|[\\s])+)}}";

	private static Pattern DependenciesPattern = Pattern.compile(Dependencies, Pattern.DOTALL);
	private static Pattern TerminalPattern = Pattern.compile(Terminal, Pattern.DOTALL);
	private static Pattern NonTerminalPattern = Pattern.compile(NonTerminal, Pattern.DOTALL);
	private static Pattern GrammarContentPattern = Pattern.compile(GrammarContent, Pattern.DOTALL);

	@Override
	public String toString() {
		return "GrammarDescriptionScanner{" +
				"content=" + content.toString() +
				", dependencies=" + Arrays.toString(dependencies) +
				'}';
	}

	public void scan(String buffer) throws Exception {
		Matcher m = DependenciesPattern.matcher(buffer);
		if (m.find()) {
			String dependency = m.group(1);
			this.dependencies = Arrays.stream(dependency.split("[\\s]+"))
					.filter(s -> s != null && !s.equals(""))
					.toArray(String[]::new);
		}

		m = NonTerminalPattern.matcher(buffer);
		if (m.find()) {
			String nonTerminal = m.group(1);
			List<Symbol> nonTerminals = Arrays.stream(nonTerminal.trim().split("[\\s]+"))
					.filter(s -> s != null)
					.map(s -> new Symbol(false, s, null))
					.collect(Collectors.toList());
			for (Symbol s : nonTerminals) {
				this.content.addNonTerminalSymbol(s);
			}
		}
		else
			throw new GrammarDescriptorException(GrammarDescriptorException.MissingNonTerminals);

		m = TerminalPattern.matcher(buffer);
		String packageName = this.getClass().getPackage().getName();
		packageName = packageName.substring(0, packageName.lastIndexOf('.')) + ".wordelement.";
		if (m.find()) {
			String terminal = m.group(1);
			String[] rawTerminals = terminal.trim().split("[\n\r]+");
			for (String s : rawTerminals) {
				int index = s.lastIndexOf(',');
				String symbol = s.substring(1, index).trim();
				String type = s.substring(index + 1, s.length() - 1).trim();
				String[] typeClass = type.split("[.]");
				Class clazz = Class.forName(packageName + typeClass[0]);
				this.content.addTerminalSymbol(
						new Symbol(true,
								symbol,
								clazz.getMethod("valueOf", String.class).invoke(null, typeClass[1]))
				);
			}
		}
		else
			throw new GrammarDescriptorException(GrammarDescriptorException.MissingTerminals);

		m = GrammarContentPattern.matcher(buffer);
		if (m.find()) {
			String start = m.group(1);
			if (!this.content.setBeginning(start))
				throw new GrammarDescriptorException(GrammarDescriptorException.IllegalGrammarBeginning + start);

			String rawRules = m.group(2);
			String[] rules = rawRules.trim().split("[\n\r]+");
			for (String r : rules) {
				int index = r.indexOf(":");
				String name = r.substring(0, index).trim();
				String rule = r.substring(index + 1).trim();
				List<String> subRules = Arrays.stream(rule.split("(?![{])\\|(?![}])"))
						.map(s -> s.trim())
						.collect(Collectors.toList());
				this.content.addRules(name, subRules);
			}
		}
		else
			throw new GrammarDescriptorException(GrammarDescriptorException.MissingGrammarDefinition);

		this.content.process();
	}

	public GrammarContent getContent() {
		return this.content;
	}
}