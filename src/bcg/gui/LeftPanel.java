package bcg.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import bcg.mdl.CallGraphNode;
import bcg.mdl.ClassNode;
import bcg.mdl.MethodNode;

public class LeftPanel extends JPanel {

	private DefaultMutableTreeNode root;
	private JTree tree;
	private JScrollPane treepane;
	private Set<ClassNode> classes = Collections.emptySet();
	private JComboBox<Integer> depth = new JComboBox<Integer>(new Integer[] {
			1, 2, 3, 4, 5 });
	private MySelectionListener selectionListener;

	public LeftPanel(MySelectionListener listener) {
		this.selectionListener = listener;
		init();
	}

	// public LeftPanel(LayoutManager layout) {
	// super(layout);
	// }
	//
	// public LeftPanel(boolean isDoubleBuffered) {
	// super(isDoubleBuffered);
	// }
	//
	// public LeftPanel(LayoutManager layout, boolean isDoubleBuffered) {
	// super(layout, isDoubleBuffered);
	// }

	public void setClasses(Set<ClassNode> classes) {
		this.classes = classes;
		reloadTreePane();
	}

	private void init() {
		setBackground(Color.black);
		setLayout(new BorderLayout());

		treepane = new JScrollPane();
		depth.addItemListener(new DeptChangeListener(selectionListener));
		depth.selectWithKeyChar('3');
		add(depth, BorderLayout.NORTH);

		reloadTreePane();
		add(treepane, BorderLayout.CENTER);
	}

	private void reloadTreePane() {
		root = new DefaultMutableTreeNode();
		root.setUserObject("Classes :      ");

		if (tree != null) {
			tree.removeTreeSelectionListener(selectionListener);
		}
		tree = new JTree(root);
		for (ClassNode klass : classes) {
			if (excludes(klass.getClassname())) {
				continue;
			}
			DefaultMutableTreeNode cnode = new DefaultMutableTreeNode(klass);
			for (CallGraphNode graphnode : klass.getChild()) {
				if (!(graphnode instanceof MethodNode)) {
					continue;
				}
				DefaultMutableTreeNode mnode = new DefaultMutableTreeNode(
						graphnode);
				cnode.add(mnode);
			}
			root.add(cnode);
		}
		root.setUserObject("Classes : " + root.getChildCount());

		treepane.getViewport().setView(tree);
		tree.addTreeSelectionListener(selectionListener);

	}

	private boolean excludes(String classname) {
		if (classname.startsWith("java/")) {
			return true;
		}
		switch (classname) {
		case "Z":// boolean
			return true;
		case "C":// char
			return true;
		case "B":// byte
			return true;
		case "S":// short
			return true;
		case "I":// int
			return true;
		case "J":// long
			return true;
		case "F":// float
			return true;
		case "D":// double
			return true;
		case "V":// void
			return true;
		case "":
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @author s.watanabe
	 */
	private class DeptChangeListener implements ItemListener {
		private MySelectionListener listener;

		public DeptChangeListener(MySelectionListener treeListener) {
			this.listener = treeListener;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			listener.setDept((Integer) e.getItem());
			listener.redoEvent();
		}
	}
}
