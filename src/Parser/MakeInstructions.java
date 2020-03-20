package Parser;

import Keyboard.ExecutableInstructions;
import Parser.Validator.ParserExcetption;
import java.lang.reflect.InvocationTargetException;
import java.util.*;



public class MakeInstructions {
    public Execution.Instruction makeInstruction(String[] args) throws ParserExcetption, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (args.length == 0)
            throw new ParserExcetption(String.format("Line is empty: {}", args[0]));
        var instruction = getInstruction(args[0]); //name
        Execution.Instruction instance =  instruction.getDeclaredConstructor().newInstance();
        instance.init(args);
        return instance;
    }

    private Class<? extends Execution.Instruction> getInstruction(String name) throws ParserExcetption {
        var instr = availableInstructions.get(name);
        if (instr == null)
            throw new ParserExcetption(String.format("Wrong macro instruction name {}", name));
        return instr;
    }

    public void loadInstructions() {
        var exec = ExecutableInstructions.class;
        var classes = exec.getClasses();
        for (var cl : classes) {
            availableInstructions.put(cl.getSimpleName().toLowerCase(), (Class<? extends Execution.Instruction>) cl);
        }
    }

    Map<String, Class<? extends Execution.Instruction>> availableInstructions = new HashMap<String, Class<? extends Execution.Instruction>>();
}
