package bcg.mdl;

import java.util.HashMap;
import java.util.Map;

import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

public class MethodNode extends CallGraphNode {

	private static Map<String, MethodNode> nodes = new HashMap<String, MethodNode>();

	private String name;
	private String desc;

	public static MethodNode genInstance(ConstantPool pool,
			ConstantMethodref methodref) {
		ClassNode declclass = ClassNode.genInstance(pool,
				(ConstantClass) pool.getConstant(methodref.getClassIndex()));
		ConstantNameAndType nameAndType = (ConstantNameAndType) pool
				.getConstant(methodref.getNameAndTypeIndex());
		ConstantUtf8 nameref = (ConstantUtf8) pool.getConstant(nameAndType
				.getNameIndex());
		ConstantUtf8 typeref = (ConstantUtf8) pool.getConstant(nameAndType
				.getSignatureIndex());
		return genInstance(declclass, new String(nameref.getBytes()),
				new String(typeref.getBytes()));
	}

	public static MethodNode genInstance(ConstantPool pool,
			ConstantCP invocation) {

		ClassNode declclass = ClassNode.genInstance(pool,
				(ConstantClass) pool.getConstant(invocation.getClassIndex()));
		ConstantNameAndType nameAndType = (ConstantNameAndType) pool
				.getConstant(invocation.getNameAndTypeIndex());
		ConstantUtf8 nameref = (ConstantUtf8) pool.getConstant(nameAndType
				.getNameIndex());
		ConstantUtf8 typeref = (ConstantUtf8) pool.getConstant(nameAndType
				.getSignatureIndex());
		return genInstance(declclass, new String(nameref.getBytes()),
				new String(typeref.getBytes()));
	}

	public static MethodNode genInstance(JavaClass klass, Method method) {
		ClassNode classnode = ClassNode.genInstance(klass);
		return MethodNode.genInstance(classnode, method.getName(),
				method.getSignature());
	}

	public static Map<String, MethodNode> getNodes() {
		return nodes;
	}

	private static synchronized MethodNode genInstance(ClassNode classnode,
			String methodname, String type) {
		StringBuilder nodenameBuilder = new StringBuilder();
		nodenameBuilder.append(classnode.getClassname()).append("#");
		nodenameBuilder.append(methodname).append(" <");
		nodenameBuilder.append(type).append(">");
		String nodename = nodenameBuilder.toString();
		if (nodes.containsKey(nodename)) {
			return nodes.get(nodename);
		}
		MethodNode methodnode = new MethodNode(nodename, methodname, type);
		methodnode.setParent(classnode);
		classnode.addChild(methodnode);
		nodes.put(nodename, methodnode);

		return methodnode;
	}

	public MethodNode(String nodename, String methodname, String desc) {
		super(nodename);
		this.name = methodname;
		this.desc = desc;
	}

	public String getMethodName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public String toString() {
		// return getParent().getNodename() + "#" + getNodename();
		StringBuilder sb = new StringBuilder();
		sb.append(getMethodName()).append("\t:").append(getDesc());
		return sb.toString();
	}
}
