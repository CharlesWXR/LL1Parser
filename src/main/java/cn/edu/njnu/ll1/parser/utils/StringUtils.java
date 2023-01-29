package cn.edu.njnu.ll1.parser.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	// util for template rendering
	private static Pattern TemplatePattern = Pattern.compile("\\$\\{([\\w]+?)}");

	public static String processTemplate(String template, Map<String, String> mapper) {
		StringBuffer sb = new StringBuffer();
		Matcher matcher = TemplatePattern.matcher(template);

		while (matcher.find()) {
			String key = matcher.group(1);
			if (mapper.containsKey(key)) {
				matcher.appendReplacement(sb, mapper.get(key));
			}
		}
		matcher.appendTail(sb);

		return sb.toString();
	}
}
