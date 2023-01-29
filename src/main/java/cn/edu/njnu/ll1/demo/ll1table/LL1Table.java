package cn.edu.njnu.ll1.demo.ll1table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.njnu.ll1.parser.grammarelement.Symbol;

import cn.edu.njnu.ll1.parser.wordelement.TypeEnum;
import cn.edu.njnu.ll1.demo.element.OperatorEnum;


/*
 * Auto-generated code to store LL1Table
 * Will assemble the table when class loaded
 */

public class LL1Table {
	private static Symbol beginning = new Symbol(false, "E", null);
	private static Map<Long, List<Symbol>> table = new HashMap<Long, List<Symbol>>();
	private static Map<String, Integer> nonTerminalMapper = new HashMap<String, Integer>();
	private static Map<Object, Integer> terminalMapper = new HashMap<Object, Integer>();

    static {
        // Init table section
        List<Symbol> t0 = new ArrayList<Symbol>();
        t0.add(new Symbol(false, "T", null));
        t0.add(new Symbol(false, "E1", null));
        table.put(0L, t0);

        List<Symbol> t1 = new ArrayList<Symbol>();
        t1.add(new Symbol(true, "(", cn.edu.njnu.ll1.demo.element.OperatorEnum.LBracket));
        t1.add(new Symbol(false, "E", null));
        t1.add(new Symbol(true, ")", cn.edu.njnu.ll1.demo.element.OperatorEnum.RBracket));
        table.put(17179869188L, t1);

        List<Symbol> t2 = new ArrayList<Symbol>();
        t2.add(new Symbol(true, "*", cn.edu.njnu.ll1.demo.element.OperatorEnum.Multiply));
        t2.add(new Symbol(false, "F", null));
        t2.add(new Symbol(false, "T1", null));
        table.put(12884901891L, t2);

        List<Symbol> t3 = new ArrayList<Symbol>();
        t3.add(new Symbol(false, "T", null));
        t3.add(new Symbol(false, "E1", null));
        table.put(1L, t3);

        List<Symbol> t4 = new ArrayList<Symbol>();
        t4.add(new Symbol(true, "empty", cn.edu.njnu.ll1.parser.wordelement.TypeEnum.Empty));
        table.put(12884901890L, t4);

        List<Symbol> t5 = new ArrayList<Symbol>();
        t5.add(new Symbol(false, "F", null));
        t5.add(new Symbol(false, "T1", null));
        table.put(8589934592L, t5);

        List<Symbol> t6 = new ArrayList<Symbol>();
        t6.add(new Symbol(false, "F", null));
        t6.add(new Symbol(false, "T1", null));
        table.put(8589934593L, t6);

        List<Symbol> t7 = new ArrayList<Symbol>();
        t7.add(new Symbol(true, "+", cn.edu.njnu.ll1.demo.element.OperatorEnum.Plus));
        t7.add(new Symbol(false, "T", null));
        t7.add(new Symbol(false, "E1", null));
        table.put(4294967298L, t7);

        List<Symbol> t8 = new ArrayList<Symbol>();
        t8.add(new Symbol(false, "T", null));
        t8.add(new Symbol(false, "E1", null));
        table.put(4L, t8);

        List<Symbol> t9 = new ArrayList<Symbol>();
        t9.add(new Symbol(true, "int", cn.edu.njnu.ll1.parser.wordelement.TypeEnum.Int));
        table.put(17179869184L, t9);

        List<Symbol> t10 = new ArrayList<Symbol>();
        t10.add(new Symbol(true, "empty", cn.edu.njnu.ll1.parser.wordelement.TypeEnum.Empty));
        table.put(4294967301L, t10);

        List<Symbol> t11 = new ArrayList<Symbol>();
        t11.add(new Symbol(true, "empty", cn.edu.njnu.ll1.parser.wordelement.TypeEnum.Empty));
        table.put(12884901895L, t11);

        List<Symbol> t12 = new ArrayList<Symbol>();
        t12.add(new Symbol(true, "double", cn.edu.njnu.ll1.parser.wordelement.TypeEnum.Double));
        table.put(17179869185L, t12);

        List<Symbol> t13 = new ArrayList<Symbol>();
        t13.add(new Symbol(false, "F", null));
        t13.add(new Symbol(false, "T1", null));
        table.put(8589934596L, t13);

        List<Symbol> t14 = new ArrayList<Symbol>();
        t14.add(new Symbol(true, "empty", cn.edu.njnu.ll1.parser.wordelement.TypeEnum.Empty));
        table.put(4294967303L, t14);

        List<Symbol> t15 = new ArrayList<Symbol>();
        t15.add(new Symbol(true, "empty", cn.edu.njnu.ll1.parser.wordelement.TypeEnum.Empty));
        table.put(12884901893L, t15);



        // Init non-terminal mapper section
        nonTerminalMapper.put("T", 2);
        nonTerminalMapper.put("E", 0);
        nonTerminalMapper.put("F", 4);
        nonTerminalMapper.put("E1", 1);
        nonTerminalMapper.put("T1", 3);


        // Init terminal mapper section
        terminalMapper.put(cn.edu.njnu.ll1.demo.element.OperatorEnum.Multiply, 3);
        terminalMapper.put(cn.edu.njnu.ll1.parser.wordelement.TypeEnum.Int, 0);
        terminalMapper.put(cn.edu.njnu.ll1.parser.wordelement.TypeEnum.End, 7);
        terminalMapper.put(cn.edu.njnu.ll1.demo.element.OperatorEnum.RBracket, 5);
        terminalMapper.put(cn.edu.njnu.ll1.parser.wordelement.TypeEnum.Double, 1);
        terminalMapper.put(cn.edu.njnu.ll1.demo.element.OperatorEnum.LBracket, 4);
        terminalMapper.put(cn.edu.njnu.ll1.demo.element.OperatorEnum.Plus, 2);
        terminalMapper.put(cn.edu.njnu.ll1.parser.wordelement.TypeEnum.Empty, 6);


    }

	private long getID(String nonTerminal, Object input) {
		if (nonTerminalMapper.get(nonTerminal) == null)
			return -1;
		if (terminalMapper.get(input) == null)
			return -1;

		int nonTerminalID = nonTerminalMapper.get(nonTerminal);
		int inputID = terminalMapper.get(input);
		return (long) nonTerminalID << 32 | (long) inputID;
	}

	public Symbol getBeginning() {
		return beginning;
	}

	public List<Symbol> getNext(String present, Object inputType) {
		long id = getID(present, inputType);
		if (id == -1)
			return null;

		return table.get(id);
	}
}
