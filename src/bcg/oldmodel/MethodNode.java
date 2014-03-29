package bcg.oldmodel;

import java.util.ArrayList;
import java.util.List;

public class MethodNode {

	private List<InvokeEdge> in;
	private List<InvokeEdge> out;

	public MethodNode() {
		in = new ArrayList<InvokeEdge>();
		out = new ArrayList<InvokeEdge>();
	}

	public InvokeEdge createEdge(MethodNode dist) {
		InvokeEdge edge = new InvokeEdge();
		edge.setSrc(this);
		edge.setDist(dist);

		this.addOut(edge);
		dist.addIn(edge);
		return edge;
	}

	public List<InvokeEdge> getIn() {
		return in;
	}

	public void addIn(InvokeEdge edge) {
		in.add(edge);
	}

	public List<InvokeEdge> getOut() {
		return out;
	}

	public void addOut(InvokeEdge edge) {
		out.add(edge);
	}

}
