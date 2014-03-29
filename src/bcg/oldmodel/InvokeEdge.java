package bcg.oldmodel;

public class InvokeEdge {
	
	private MethodNode dist;
	private MethodNode src;

	public InvokeEdge() {
	}

	public MethodNode getDist() {
		return dist;
	}

	public void setDist(MethodNode dist) {
		this.dist = dist;
	}

	public MethodNode getSrc() {
		return src;
	}

	public void setSrc(MethodNode src) {
		this.src = src;
	}
}
