package bcg;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

import bcg.mdl.ClassNode;
import bcg.mdl.MethodNode;

public class BcgParser {

	private List<JavaClass> loaded;

	public BcgParser() {
		loaded = new ArrayList<JavaClass>();
	}

	public void load(File classfile) {
		// System.out.println("Loading:" + classfile.getAbsolutePath());
		try {
			JavaClass klass = new ClassParser(classfile.getPath()).parse();
			loadConstants(klass);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void load(String jarfile, String classfile) {
		// System.out.println("Loading:" + jarfile + "(" + classfile + ")");
		try {
			JavaClass klass = new ClassParser(jarfile, classfile).parse();
			loadConstants(klass);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void parse() {
		Collections.sort(loaded);
		for (JavaClass classfile : loaded) {
			// System.out.println("Parsing:" + classfile.getClassName());
			for (Method method : classfile.getMethods()) {
				parseMethodCode(classfile, method);
			}
		}
	}

	public Map<String, MethodNode> getMethodNodes() {
		return MethodNode.getNodes();
	}

	public Map<String, ClassNode> getClassNodes() {
		return ClassNode.getNodes();
	}

	private void loadConstants(JavaClass klass) {
		if (loaded.contains(klass)) {
			return;
		}
		ConstantPool cp = klass.getConstantPool();
		for (int i = 0; i < cp.getLength(); i++) {
			Constant constant = cp.getConstant(i);
			if (constant instanceof ConstantClass) {
				ClassNode.genInstance(cp, (ConstantClass) constant);
			} else if (constant instanceof ConstantMethodref) {
				MethodNode.genInstance(cp, (ConstantMethodref) constant);
			}
		}
		loaded.add(klass);
	}

	private MethodNode parseMethodCode(JavaClass klass, Method method) {
		MethodNode methodnode = MethodNode.genInstance(klass, method);
		if (method.getCode() == null) {
			return methodnode;
		}
		List<MethodNode> invokes = new ArrayList<MethodNode>();
		byte[] bytecode = method.getCode().getCode();
		for (int counter = 0; counter < bytecode.length; counter++) {
			short opcode = (short) (bytecode[counter] < 0 ? ((short) bytecode[counter]) + 0x100
					: (short) bytecode[counter]);
			int oplen = Constants.NO_OF_OPERANDS[opcode];

			if (!Targets.contains(opcode)) {
				counter += oplen;
				continue;

			}
			short d = 2;
			short ent = (short) (bytecode[counter + d] < 0 ? ((short) bytecode[counter
					+ d]) + 0x100
					: (short) bytecode[counter + d]);
			// short ent = (short)
			ConstantPool pool = method.getConstantPool();
			ConstantCP methodref = (ConstantCP) pool.getConstant(ent);

			MethodNode invokeTarget = MethodNode.genInstance(pool, methodref);
			if (!invokes.contains(invokeTarget)) {
				invokes.add(invokeTarget);
				methodnode.addOut(invokeTarget);
				invokeTarget.addIn(methodnode);
			}

			// System.out.println("INVOKE " + invokeTarget);

			counter += oplen;
		}

		// for (MethodNode invoke : invokes) {
		// System.out.println(invoke);
		// }

		return methodnode;
	}
}

class Targets {
	private static final short[] opcodes = new short[] {
			Constants.INVOKEVIRTUAL, Constants.INVOKESPECIAL,
			Constants.INVOKEINTERFACE };

	private static final Set<Short> __opcodes = new HashSet<Short>();

	static {
		for (short op : opcodes) {
			__opcodes.add(op);
		}
	}

	public static boolean contains(short opcode) {
		return __opcodes.contains(opcode);
	}
}
