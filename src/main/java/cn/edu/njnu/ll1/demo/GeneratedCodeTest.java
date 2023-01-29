package cn.edu.njnu.ll1.demo;

import cn.edu.njnu.ll1.demo.element.Word;
import cn.edu.njnu.ll1.demo.ll1table.TableDriver;
import cn.edu.njnu.ll1.demo.wordscanner.Scanner;
import cn.edu.njnu.ll1.parser.grammarelement.Symbol;


import java.io.*;

public class GeneratedCodeTest {
	public static void main(String[] args) {
		// Test the auto-generated code
		// Prepare word scanner
		Scanner wordScanner = new Scanner();
		String s = "(1+3.3)*4";
		wordScanner.appendBuffer(s);

		try {
			TableDriver driver = new TableDriver();
			boolean read = false;
			Word w = (Word)(wordScanner.getNext());
			while (!wordScanner.isEnd() || (wordScanner.isEnd() == true && read == false)) {
				if (read) {
					w = (Word)(wordScanner.getNext());
				}
				read = driver.next(w.getType());
			}
			while (!driver.next(Symbol.End.type));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
