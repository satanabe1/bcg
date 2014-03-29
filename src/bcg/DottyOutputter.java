package bcg;

import java.util.ArrayList;
import java.util.List;

import bcg.mdl.CallGraphNode;
import bcg.mdl.ClassNode;
import bcg.mdl.MethodNode;

public class DottyOutputter implements ITextOutputter {

	public DottyOutputter() {

	}

	public String oldtext(MethodNode root) {
		StringBuilder s = new StringBuilder();
		s.append(root.getMethodName()).append("\n");
		for (CallGraphNode child : root.getOut()) {
			if (!(child instanceof MethodNode)) {
				continue;
			}
			MethodNode mc = (MethodNode) child;
			s.append("\t");
			s.append(mc.getParent().getNodename()).append("#")
					.append(mc.getNodename()).append(mc.getDesc());
			s.append("\n");
		}
		return s.toString();
	}

	@Override
	public String text(MethodNode root, int dept) {
		StringBuilder s = new StringBuilder();
		s.append("digraph callgraph {graph [compound = true, rankdir = LR, splines=polyline];");
		s.append("\n");

		s.append("subgraph ").append(quote("cluster_" + root.getNodename()));
		s.append("\n");
		s.append("{");
		s.append("\n");
		s.append("label = ").append(quote(root.getNodename()));
		s.append("\n");

		List<MethodNode> list = new ArrayList<>();
		List<MethodNode[]> paplist = new ArrayList<MethodNode[]>();

		for (CallGraphNode out : root.getOut()) {
			s.append(recursiveOutstr(root, out, dept, list, paplist));
		}
		// for (CallGraphNode out : root.getOut()) {
		// s.append("\t");
		// s.append(quote(out.getNodename()));
		// s.append("\n");
		// }

		s.append("}");
		s.append("\n");

		s.append("}");
		s.append("\n");
		return s.toString();
	}

	@Override
	public String text(ClassNode klass) {
		StringBuilder s = new StringBuilder();
		s.append("digraph callgraph {graph [compound = true, splines=polyline];");
		s.append("\n");

		s.append("subgraph ").append(quote("cluster_" + klass.getClassname()));
		s.append("\n");
		s.append("{");
		s.append("\n");
		s.append("label = ").append(quote(klass.getClassname()));
		s.append("\n");
		// s.append(klass.getClassname()).append("\n");
		for (CallGraphNode child : klass.getChild()) {
			if (!(child instanceof MethodNode)) {
				continue;
			}
			MethodNode mc = (MethodNode) child;
			s.append("\t");

			s.append(quote(mc.getMethodName() + mc.getDesc()));
			// s.append(mc.getMethodName()).append(mc.getDesc());
			s.append("\n");
		}
		s.append("}");
		s.append("\n");

		s.append("}");
		s.append("\n");
		return s.toString();
	}

	private StringBuilder recursiveOutstr(CallGraphNode caller,
			CallGraphNode callee, int dept, List<MethodNode> writtenMethodNode,
			List<MethodNode[]> wirttenCg) {
		StringBuilder s = new StringBuilder();
		MethodNode callerMethod = null;
		MethodNode calleeMethod = null;
		if (dept < 0) {
			return s;
		}
		if (!(caller instanceof MethodNode)) {
			return s;
		}
		if (!(callee instanceof MethodNode)) {
			return s;
		}
		callerMethod = (MethodNode) caller;
		calleeMethod = (MethodNode) callee;

		if (!writtenMethodNode.contains(callerMethod)) {
			s.append(writenode(callerMethod));
			writtenMethodNode.add(callerMethod);
		}
		if (!writtenMethodNode.contains(calleeMethod)) {
			s.append(writenode(calleeMethod));
			writtenMethodNode.add(calleeMethod);
		}

		s.append(quote(caller.getNodename())).append(" -> ");
		s.append(quote(callee.getNodename())).append(";").append("\n");
		for (CallGraphNode out : callee.getOut()) {
			MethodNode[] cg = new MethodNode[] { callerMethod, calleeMethod };
			if (wirttenCg.contains(cg)) {
				continue;
			}
			wirttenCg.add(cg);
			s.append(recursiveOutstr(callee, out, dept - 1, writtenMethodNode,
					wirttenCg));
		}

		return s.append("\n");
	}

	private String writenode(MethodNode method) {
		StringBuilder s = new StringBuilder();
		s.append(quote(method.getNodename()));
		s.append("[");
		s.append("label = ");
		s.append(quote(method.getNodename()));
		s.append("]");
		s.append("\n");
		return quote(method.getNodename());
	}

	private String quote(String plain) {
		return new StringBuilder().append("\"").append(plain).append("\"")
				.toString();
	}
}
