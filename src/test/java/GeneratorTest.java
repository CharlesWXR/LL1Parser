import cn.edu.njnu.ll1.parser.generator.Generator;
import cn.edu.njnu.ll1.parser.wordelement.TypeEnum;
import org.junit.Test;

import java.io.*;

public class GeneratorTest {
	@Test
	public void testGenerator() {
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

	@Test
	public void testEnum() {
		Enum o = TypeEnum.Char;
		System.out.println(o.name());
		System.out.println(o.getClass().getName() + "." + o);
	}
}
