package bcg.gui;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import bcg.DottyOutputter;
import bcg.ITextOutputter;
import bcg.mdl.CallGraphNode;
import bcg.mdl.ClassNode;
import bcg.mdl.MethodNode;
import att.grappa.Attribute;
import att.grappa.Element;
import att.grappa.Graph;
import att.grappa.GrappaAdapter;
import att.grappa.GrappaPanel;
import att.grappa.GrappaPoint;
import att.grappa.Parser;
import att.grappa.Subgraph;

public class MySelectionListener implements TreeSelectionListener {

	private JFrame frame;
	private JScrollPane graphpane;
	private int dept;
	private ITextOutputter texter;

	private TreeSelectionEvent recentevent = null;

	public MySelectionListener(JFrame frame, JScrollPane graphpane) {
		this.frame = frame;
		this.graphpane = graphpane;
		texter = new DottyOutputter();
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		recentevent = e;
		Object obj = e.getPath().getLastPathComponent();
		if (!(obj instanceof DefaultMutableTreeNode)) {
			return;
		}
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) obj;
		Object userobj = treeNode.getUserObject();
		if (userobj instanceof CallGraphNode) {
			frame.setTitle(((CallGraphNode) userobj).getNodename());
			update((CallGraphNode) userobj);
		}
	}

	public void redoEvent() {
		if (recentevent != null) {
			valueChanged(recentevent);
		}
	}

	public void setDept(int dept) {
		this.dept = dept;
	}

	private void update(CallGraphNode graphNode) {
		try {
			if (graphNode instanceof ClassNode) {
				update((ClassNode) graphNode);
			} else if (graphNode instanceof MethodNode) {
				update((MethodNode) graphNode);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void update(ClassNode klass) throws Exception {
		String dottext = texter.text(klass);
		InputStream input = execdot("twopi", dottext);
		draw(input);
	}

	private void update(MethodNode method) throws Exception {
		String dottext = texter.text(method, dept);
		InputStream input = execdot("dot", dottext);
		draw(input);
	}

	private InputStream execdot(String dotcommand, String inputtext)
			throws Exception {
		System.out.println(inputtext);

		CommandExecutor executor = new CommandExecutor(dotcommand);
		executor.exec(inputtext);
		executor.getOutputStream().close();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				executor.getInputStream(), "UTF-8"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			// System.out.println(line);
			sb.append(line.replaceAll("=([0-9]+\\.[0-9]+)", "=\"$1\""));
			sb.append("\n");
		}

		ByteArrayInputStream inputstream = new ByteArrayInputStream(sb
				.toString().getBytes("UTF-8"));
		return inputstream;
	}

	private void draw(InputStream dottextStream) throws Exception {
		Parser dotparser = new Parser(dottextStream, System.err);
		dotparser.parse();
		Graph graph = dotparser.getGraph();
		graph.setEditable(true);
		graph.setMenuable(true);

		GrappaPanel gp = new GrappaPanel(graph);
		gp.setScaleToFit(false);
		gp.addGrappaListener(new GrappaAdapterEx());

		graphpane.getViewport().setView(gp);
	}

	private class GrappaAdapterEx extends GrappaAdapter {
		@Override
		public void grappaClicked(Subgraph subg, Element elem, GrappaPoint pt,
				int modifiers, int clickCount, GrappaPanel panel) {
			if (elem == null) {
				return;
			}
			Iterator<Attribute> iter = elem.getLocalAttributePairs();
			while (iter.hasNext()) {
				Attribute attr = iter.next();
				System.out.println(attr.getName() + ":" + attr.getValue());
			}
		}
	}
}
