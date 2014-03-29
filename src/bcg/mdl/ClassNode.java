package bcg.mdl;

import java.util.HashMap;
import java.util.Map;

import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.JavaClass;

@SuppressWarnings("rawtypes")
public class ClassNode extends CallGraphNode implements java.lang.Comparable {

	private static Map<String, ClassNode> nodes = new HashMap<String, ClassNode>();

	public static ClassNode genInstance(ConstantPool pool,
			ConstantClass classref) {
		ConstantUtf8 nameref = (ConstantUtf8) pool.getConstant(classref
				.getNameIndex());
		String classname = new String(nameref.getBytes());
		classname = plain(classname);
		return ClassNode.genInstance(classname);
	}

	public static ClassNode genInstance(JavaClass klass) {
		return genInstance(klass.getClassName().replaceAll("\\.", "/"));
	}

	public static synchronized ClassNode genInstance(String classname) {
		if (!nodes.containsKey(classname)) {
			nodes.put(classname, new ClassNode(classname));
		}
		return nodes.get(classname);
	}

	public static Map<String, ClassNode> getNodes() {
		return nodes;
	}

	private ClassNode(String name) {
		super(name);
	}

	public String getClassname() {
		return getNodename();
	}

	public String toString() {
		// StringBuilder sb = new StringBuilder();
		// sb.append("Class:").append(getClassname()).append("\n");
		// for (CallGraphNode c : getChild()) {
		// sb.append("\t").append(c.toString());
		// sb.append("\n");
		// }
		// return sb.toString();

		StringBuilder sb = new StringBuilder();
		sb.append(getClassname());
		return sb.toString();
	}

	private static String plain(String classname) {
		String tmp = classname.toString();
		while (tmp.startsWith("[")) {
			if (tmp.startsWith("[L")) {
				tmp = tmp.substring(2);
			} else {
				tmp = tmp.substring(1);
			}
		}
		if (tmp.endsWith(";")) {
			tmp = tmp.substring(0, tmp.length() - 1);
		}
		return tmp;
	}

	@Override
	public int compareTo(Object o) {
		return toString().compareTo(o.toString());
	}
}
