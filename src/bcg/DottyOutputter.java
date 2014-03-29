package bcg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import bcg.mdl.CallGraphNode;
import bcg.mdl.ClassNode;
import bcg.mdl.MethodNode;

/**
 * 解析結果をdot言語にするクラス
 * 
 * @author s.watanabe
 */
public class DottyOutputter implements ITextOutputter {

	public DottyOutputter() {
	}

	@Override
	public String text(MethodNode root, int dept) {
		StringBuilder s = new StringBuilder();
		s.append("digraph callgraph {graph [compound = true, rankdir = LR, splines=polyline];");
		s.append("\n");

		List<MethodNode> checked = new ArrayList<>();
		List<MethodNode[]> cglist = new ArrayList<MethodNode[]>();
		getcg(root, dept, cglist, checked);

		for (String subgraph : genSubgraph(checked, cglist)) {
			s.append(subgraph).append("\n");
		}
		for (String edgs : genEdg(cglist)) {
			s.append(edgs).append("\n");
		}

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
			s.append("\n");
		}
		s.append("}");
		s.append("\n");

		s.append("}");
		s.append("\n");
		return s.toString();
	}

	private List<String> genSubgraph(List<MethodNode> methods,
			List<MethodNode[]> cglist) {
		Map<CallGraphNode, StringBuilder> graphs = new HashMap<CallGraphNode, StringBuilder>();
		genSubgraph(graphs, marge(methods, cglist));

		List<String> sgraphs = new ArrayList<String>();
		Iterator<CallGraphNode> iter = graphs.keySet().iterator();
		while (iter.hasNext()) {
			sgraphs.add(graphs.get(iter.next()).append("\n}").toString());
		}
		return sgraphs;
	}

	private List<MethodNode> marge(List<MethodNode> list1,
			List<MethodNode[]> list2) {
		List<MethodNode> marged = new ArrayList<MethodNode>();
		marged.addAll(list1);
		for (MethodNode[] ary : list2) {
			if (!marged.contains(ary[0])) {
				marged.add(ary[0]);
			}
			if (!marged.contains(ary[1])) {
				marged.add(ary[1]);
			}
		}
		return marged;
	}

	private void genSubgraph(Map<CallGraphNode, StringBuilder> ingraphs,
			List<MethodNode> methods) {
		for (MethodNode m : methods) {
			CallGraphNode parent = m.getParent();
			StringBuilder s = null;
			if (ingraphs.containsKey(parent)) {
				s = ingraphs.get(parent);
			} else {
				s = new StringBuilder();
				s.append("subgraph ").append(
						quote("cluster_" + parent.getNodename()));
				s.append(" {").append("\n");
				s.append("label = ").append(quote(parent.getNodename()));
				s.append("\n");
				ingraphs.put(parent, s);
			}
			s.append(writenode(m));
		}
	}

	private List<String> genEdg(List<MethodNode[]> cglist) {
		List<String> edgs = new ArrayList<String>();
		for (MethodNode[] cg : cglist) {
			StringBuilder s = new StringBuilder();
			s.append(quote(cg[0].getNodename()));
			s.append(" -> ");
			s.append(quote(cg[1].getNodename()));
			s.append("\n");
			edgs.add(s.toString());
		}
		return edgs;
	}

	private List<MethodNode[]> getcg(MethodNode root, int dept,
			List<MethodNode[]> cglist, List<MethodNode> checked) {
		if (dept < 1) {
			return Collections.emptyList();
		}
		if (checked == null) {
			checked = new ArrayList<>();
		}
		if (cglist == null) {
			cglist = new ArrayList<>();
		}
		if (checked.contains(root)) {
			return Collections.emptyList();
		} else {
			checked.add(root);
		}

		List<MethodNode[]> list = new ArrayList<MethodNode[]>();
		for (CallGraphNode out : root.getOut()) {
			if (!(out instanceof MethodNode)) {
				continue;
			}
			MethodNode callee = (MethodNode) out;
			MethodNode[] cg = new MethodNode[] { root, callee };
			if (cglist.contains(cg)) {
				continue;
			}
			cglist.add(cg);
			getcg(callee, dept - 1, cglist, checked);
		}
		return list;
	}

	private String writenode(MethodNode method) {
		StringBuilder s = new StringBuilder();
		s.append(quote(method.getNodename()));
		s.append("[");
		s.append("label = ");
		s.append(quote(method.toString()));
		s.append(",");
		s.append("id = ").append(quote(method.getNodename()));
		s.append("]");
		s.append("\n");
		return s.toString();
	}

	private String quote(String plain) {
		return new StringBuilder().append("\"").append(plain).append("\"")
				.toString();
	}
}
