package cn.edu.njnu.ll1.demo;

import cn.edu.njnu.ll1.parser.generator.Generator;

import java.io.*;

public class Generate {
	public static void main(String[] args) {
		// Generate code by grammar description
		try {
			FileInputStream in = new FileInputStream(new File("LL1.txt"));
			InputStreamReader reader = new InputStreamReader(in);
			BufferedReader bufferReader = new BufferedReader(reader);

			String buffer;
			StringBuilder content = new StringBuilder();
			while ((buffer = bufferReader.readLine()) != null) {
				content.append(buffer + "\n");
			}
			bufferReader.close();
			Generator g = new Generator();
			g.generate(content.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
