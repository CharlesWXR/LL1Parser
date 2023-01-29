package cn.edu.njnu.ll1.parser.generator;

import cn.edu.njnu.ll1.parser.annotation.TemplateScanPath;
import cn.edu.njnu.ll1.parser.grammarelement.LL1Table;
import cn.edu.njnu.ll1.parser.grammarelement.Symbol;
import cn.edu.njnu.ll1.parser.utils.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@TemplateScanPath(".\\src\\main\\java\\cn\\edu\\njnu\\ll1\\parser\\template\\")
public class Generator {
	private static Map<String, String> Templates = new HashMap<String, String>();
	// To generate different temp variables to initialize the table
	private int tempVarCount = 0;

	static {
		// Load the templates from the path in the annotation TemplateScanPath
		TemplateScanPath scanPathAnno = Generator.class.getAnnotation(TemplateScanPath.class);
		try {
			String[] names = {"TableTemplate", "InitListTemplate", "InitNonTerminalMapper",
					"InitTableTemplate", "InitTerminalMapper", "StaticCodeTemplate",
					"TableTemplate", "TableDriverTemplate", "InitSymbolTemplate"};

			for (String name : names) {
				StringBuffer tempBuffer = new StringBuffer();
				FileInputStream in = new FileInputStream(new File(scanPathAnno.value() + name + ".txt"));
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));

				String temp = "";
				while ((temp = reader.readLine()) != null) {
					tempBuffer.append(temp + '\n');
				}

				Templates.put(name, tempBuffer.toString());
				reader.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generate(String lexBuffer) throws Exception {
		// Generate LL1Table and TableDriver

		//===========================
		//Load LL1Table template
		String template = Templates.get("TableTemplate");
		Map<String, String> templateParams = new HashMap<String, String>();

		// Init package name
		StackTraceElement[] invokers = Thread.currentThread().getStackTrace();
		Class clazz = Class.forName(invokers[2].getClassName());
		String packageName = clazz.getPackage().getName() + ".ll1table";
		templateParams.put("packageName", packageName);

		// Scan the lex file to analyse the lex
		GrammarDescriptionScanner grammarScanner = new GrammarDescriptionScanner();
		grammarScanner.scan(lexBuffer);
		// Use analyse result to init LL1Table
		LL1Table table = new LL1Table();
		table.init(grammarScanner.getContent());

		StringBuilder tempBuilder = new StringBuilder();

		// Init dependencies
		Set<String> dependencies = grammarScanner.getPackages();
		for (String dependency : dependencies) {
			tempBuilder.append("import " + dependency + ";\n");
		}
		templateParams.put("packageDependencies", tempBuilder.toString());
		tempBuilder.setLength(0);

		// Init beginning
		templateParams.put("beginning", initBeginning(table.getBeginning()));

		// Init static code section
		templateParams.put("staticCode", generateStaticCode(table));

		// Generate LL1Table and save
		String content = StringUtils.processTemplate(template, templateParams);
		save(content, "LL1Table.java");

		//=========================
		// Generate table driver and save
		String tableDriverTemplate = Templates.get("TableDriverTemplate");
		Map<String, String> driverParams = new HashMap<String, String>();
		driverParams.put("LL1TablePackage", packageName + ".LL1Table");
		content = StringUtils.processTemplate(tableDriverTemplate, driverParams);
		save(content, "TableDriver.java");
	}

	private String initBeginning(Symbol beginning) {
		// Deal with ${beginning} => InitSymbolTemplate
		String template = Templates.get("InitSymbolTemplate").trim();
		Map<String, String> params = new HashMap<String, String>();
		params.put("isTerminal", String.valueOf(beginning.isTerminal));
		params.put("identifier", beginning.identifier);
		Object o = beginning.type;
		if (o == null)
			params.put("type", "null");
		else
			params.put("type", o.getClass().getName() + "." + o);
		return StringUtils.processTemplate(template, params);
	}

	private String generateStaticCode(LL1Table table) {
		// Deal with ${staticCode}
		Map<String, String> params = new HashMap<String, String>();
		String staticCodeTemplate = Templates.get("StaticCodeTemplate");

		// Init table
		params.put("initTable", generateInitTable(table.getTable()));

		// Init non-terminal mapper
		params.put("initNonTerminalMapper", generateInitNonTerminalMapper(table.getNonTerminalMapper()));

		// Init terminal mapper
		params.put("initTerminalMapper", generateInitTerminalMapper(table.getTerminalMapper()));

		return StringUtils.processTemplate(staticCodeTemplate, params);
	}

	private String generateInitTable(Map<Long, List<Symbol>> table) {
		// Deal with ${initTable} in ${staticCode}
		String tableTemplate = Templates.get("InitTableTemplate");
		String listTemplate = Templates.get("InitListTemplate");
		StringBuilder tableInit = new StringBuilder();

		for (Map.Entry<Long, List<Symbol>> entry : table.entrySet()) {
			String varName = "t" + this.tempVarCount;
			this.tempVarCount++;

			Map<String, String> tableParams = new HashMap<String, String>();
			tableParams.put("varName", varName);
			tableParams.put("ID", entry.getKey().toString() + "L");

			StringBuilder listInit = new StringBuilder();
			Map<String, String> listParams = new HashMap<String, String>();
			listParams.put("varName", varName);
			for (Symbol symbol : entry.getValue()) {
				listParams.put("isTerminal", String.valueOf(symbol.isTerminal));
				listParams.put("identifier", symbol.identifier);

				Object type = symbol.type;
				if (type == null) {
					listParams.put("type", "null");
				} else {
					listParams.put("type", type.getClass().getName() + "." + type);
				}

				listInit.append(StringUtils.processTemplate(listTemplate, listParams));
			}

			tableParams.put("initList", listInit.toString());
			tableInit.append(StringUtils.processTemplate(tableTemplate, tableParams));
		}
		return tableInit.toString();
	}

	private String generateInitNonTerminalMapper(Map<String, Integer> nonTerminalMapper) {
		// Deal with ${initNonTerminalMapper} in ${staticCode}
		String nonTerminalMapperTemplate = Templates.get("InitNonTerminalMapper");
		StringBuilder builder = new StringBuilder();
		Map<String, String> params = new HashMap<String, String>();
		for (Map.Entry<String, Integer> entry : nonTerminalMapper.entrySet()) {
			params.put("name", entry.getKey());
			params.put("number", entry.getValue().toString());
			builder.append(StringUtils.processTemplate(nonTerminalMapperTemplate, params));
		}
		return builder.toString();
	}

	private String generateInitTerminalMapper(Map<Object, Integer> terminalMapper) {
		// Deal with ${initTerminalMapper} in ${staticCode}
		String nonTerminalMapperTemplate = Templates.get("InitTerminalMapper");
		StringBuilder builder = new StringBuilder();
		Map<String, String> params = new HashMap<String, String>();
		for (Map.Entry<Object, Integer> entry : terminalMapper.entrySet()) {
			Object o = entry.getKey();
			params.put("type", o.getClass().getName() + "." + o);
			params.put("number", entry.getValue().toString());
			builder.append(StringUtils.processTemplate(nonTerminalMapperTemplate, params));
		}
		return builder.toString();
	}

	private void save(String content, String filename) {
		try {
			FileWriter writer = new FileWriter(new File(filename));
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write(content);
			bw.close();
			System.out.println("Generated code saved to " + filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
