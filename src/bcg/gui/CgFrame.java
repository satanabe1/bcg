package bcg.gui;

import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import bcg.CgManager;
import bcg.mdl.ClassNode;

public class CgFrame extends JFrame {

	private LeftPanel treepanel;
	private JScrollPane graphpane;
	private MySelectionListener selectionListener;
	private CgManager generator;

	public CgFrame() throws HeadlessException {
		init();
	}

	public CgFrame(GraphicsConfiguration gc) {
		super(gc);
		init();
	}

	public CgFrame(String title) throws HeadlessException {
		super(title);
		init();
	}

	public CgFrame(String title, GraphicsConfiguration gc) {
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

	public void openJar(File jar) {
		try {
			generator.load(jar);
			setClasses(generator.parse());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void init() {
		generator = new CgManager();

		int width = 1300;
		int height = 800;

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

		initMenubar();
	}

	private void initMenubar() {
		JMenuBar menubar = new JMenuBar();
		JMenu filemenu = new JMenu("File");
		final CgFrame me = this;
		menubar.add(filemenu);

		JMenuItem open = new JMenuItem("Open");
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(new File("."));
				fileChooser.setFileFilter(new FileFilter() {
					@Override
					public String getDescription() {
						return "*.jar";
					}

					@Override
					public boolean accept(File f) {
						return f.getName().endsWith(".jar");
					}
				});
				fileChooser.showOpenDialog(me);
				File select = fileChooser.getSelectedFile();
				me.openJar(select);
			}
		});
		filemenu.add(open);

		setJMenuBar(menubar);
	}

	private class Resizer extends NullComponentListener {

		private Component base, left, right;
		private double scalew = 0.25;
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
			int height = (int) (base.getHeight() * scaleh) - 20;
			int rightw = (int) (base.getWidth() * (1 - scalew));
			left.setBounds(0, 0, leftw, height);
			right.setBounds(leftw + 1, 0, rightw, height);
		}
	}
}
