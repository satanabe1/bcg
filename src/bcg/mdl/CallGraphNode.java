package bcg.mdl;

import java.util.ArrayList;
import java.util.List;

public abstract class CallGraphNode {

	private String nodename;
	private CallGraphNode parent;
	private List<CallGraphNode> child;

	private List<CallGraphNode> in;
	private List<CallGraphNode> out;

	protected CallGraphNode(String name) {
		nodename = name;
		in = new ArrayList<CallGraphNode>();
		out = new ArrayList<CallGraphNode>();
		child = new ArrayList<CallGraphNode>();
	}

	public String getNodename() {
		return nodename;
	}

	public void setParent(CallGraphNode parent) {
		this.parent = parent;
	}

	public CallGraphNode getParent() {
		return parent;
	}

	public void addChild(CallGraphNode child) {
		this.child.add(child);
	}

	public List<CallGraphNode> getChild() {
		return child;
	}

	public List<CallGraphNode> getIn() {
		return in;
	}

	public void addIn(CallGraphNode node) {
		if (!in.contains(node)) {
			in.add(node);
		}
	}

	public List<CallGraphNode> getOut() {
		return out;
	}

	public void addOut(CallGraphNode node) {
		if (!out.contains(node)) {
			out.add(node);
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getNodename());
		return sb.toString();
	}
	
}
