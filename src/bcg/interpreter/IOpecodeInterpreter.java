package bcg.interpreter;

import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;

public interface IOpecodeInterpreter {

	void interpret(ConstantPool pool, int opecode, Constant constant);
}
