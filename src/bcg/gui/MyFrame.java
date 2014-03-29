package bcg.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import att.grappa.GrappaPanel;
import bcg.mdl.ClassNode;

public class MyFrame extends JFrame {

	private LeftPanel treepanel;
	private JScrollPane graphpane;

	// public static void main(String[] args) {
	// MyFrame frame = new MyFrame();
	// frame.setVisible(true);
	// }

	public MyFrame() throws HeadlessException {
		init();
	}

	public MyFrame(GraphicsConfiguration gc) {
		super(gc);
		init();
	}

	public MyFrame(String title) throws HeadlessException {
		super(title);
		init();
	}

	public MyFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		init();
	}

	public void setClasses(Map<String, ClassNode> classes) {
		TreeSet<ClassNode> set = new TreeSet<ClassNode>();
		set.addAll(classes.values());
		setClasses(set);
	}

	public void setClasses(Set<ClassNode> classes) {
		treepanel.setClasses(classes);
	}

	private void init() {
		int width = 1200;
		int height = 800;
		MySelectionListener selectionListener;

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(true);
		setSize(width, height);
		setLayout(null);

		graphpane = new JScrollPane();
		selectionListener = new MySelectionListener(this, graphpane);
		treepanel = new LeftPanel(selectionListener);

		add(treepanel);
		add(graphpane);

		Resizer resizer = new Resizer(this, treepanel, graphpane);
		addComponentListener(resizer);
		resizer.resize();
	}

	private class Resizer extends NullComponentListener {

		private Component base, left, right;
		private double scalew = 0.35;
		private double scaleh = 0.96;

		private Resizer(Component base, Component left, Component right) {
			this.base = base;
			this.left = left;
			this.right = right;
		}

		@Override
		public void componentResized(ComponentEvent e) {
			resize();
		}

		public void resize() {
			int leftw = (int) (base.getWidth() * scalew);
			int height = (int) (base.getHeight() * scaleh);
			int rightw = (int) (base.getWidth() * (1 - scalew));
			left.setBounds(0, 0, leftw, height);
			right.setBounds(leftw + 1, 0, rightw, height);
		}
	}
}
