import cn.edu.njnu.ll1.parser.generator.GrammarDescriptionScanner;
import cn.edu.njnu.ll1.parser.wordelement.TypeEnum;
import org.junit.Test;

import java.io.*;

public class ScannerTest {
	@Test
	public void testScanner() {
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
			System.out.println(g);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
