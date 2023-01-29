import cn.edu.njnu.ll1.demo.wordscanner.Scanner;
import cn.edu.njnu.ll1.parser.generator.GrammarDescriptionScanner;
import cn.edu.njnu.ll1.parser.generator.TableDriver;
import cn.edu.njnu.ll1.parser.grammarelement.Symbol;
import cn.edu.njnu.ll1.demo.element.Word;
import org.junit.Test;

import java.io.*;

public class DriverTest {
	@Test
	public void testDriver() {
		// Prepare word scanner
		Scanner wordScanner = new Scanner();
		String s = "(1+3.3)*4";
		wordScanner.appendBuffer(s);

		// Prepare grammar scanner
		GrammarDescriptionScanner g = new GrammarDescriptionScanner();
		try {
			FileInputStream in = new FileInputStream(new File("LL1.txt"));
			InputStreamReader reader = new InputStreamReader(in);
			BufferedReader bufferReader = new BufferedReader(reader);

			String buffer;
			StringBuilder content = new StringBuilder();
			while((buffer = bufferReader.readLine()) != null) {
				content.append(buffer + "\n");
			}
			bufferReader.close();
			g.scan(content.toString());

			TableDriver driver = new TableDriver();
			driver.init(g.getContent());
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
