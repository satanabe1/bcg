package bcg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import bcg.mdl.ClassNode;
import bcg.mdl.MethodNode;

/**
 * コールグラフを生成とかをする
 * 
 * @author s.watanabe
 */
public class CgManager {

	// private String sourcepath;
	// private String classpath;

	/**
	 * クラスファイルを解析して、コールグラフを生成する
	 */
	private BcgParser parser;

	/**
	 * コンストラクタ
	 */
	public CgManager() {
		parser = new BcgParser();
	}

	// public void setSourcepath(String path) {
	// sourcepath = path;
	// }
	//
	// public void setClasspath(String path) {
	// classpath = path;
	// }

	// public void register(String classname) {
	// register(new File(classname));
	// }

	/**
	 * {@link CgManager#load(File)}で指定されたクラスファイル、またはjarファイルを解析する<br>
	 * 解析した結果、みつかった {@link ClassNode} のMapを返す
	 */
	public Map<String, ClassNode> parse() {
		parser.parse();
		// System.out.println("digraph callgraph {"
		// + "graph [compound = true, rankdir = LR, splines=polyline];");
		// System.out.println(c2text(parser.getClassNodes()));
		// System.out.println("}");

		// return parser.getMethodNodes();
		return parser.getClassNodes();
	}

	@SuppressWarnings("unused")
	private String c2text(Map<String, ClassNode> nodes) {
		StringBuilder s = new StringBuilder();
		ITextOutputter outputter = new DottyOutputter();
		Iterator<String> iter = nodes.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			ClassNode classnode = nodes.get(key);
			s.append("//===== " + key + " =====");
			s.append("\n");
			s.append(outputter.text(classnode));
		}

		try {
			FileWriter fw = new FileWriter("out.txt");
			fw.write(s.toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s.toString();
	}

	@SuppressWarnings("unused")
	private String m2text(Map<String, MethodNode> nodes) {
		ITextOutputter outputter = new DottyOutputter();
		Iterator<String> iter = nodes.keySet().iterator();
		StringBuilder s = new StringBuilder();
		while (iter.hasNext()) {
			String key = iter.next();
			MethodNode methodnode = nodes.get(key);
			s.append("===== " + key + " =====");
			s.append("\n");
			s.append(outputter.text(methodnode, 0));
		}
		return s.toString();
	}

	/**
	 * 解析対象とするクラスファイル、またはjarファイルを解析器に登録する<br>
	 * loadした後、parseしないと意味が無い
	 * 
	 * @param file
	 *            jarファイルかクラスファイル
	 * @throws IOException
	 */
	public void load(File file) throws IOException {
		if (file.isDirectory()) {
			throw new IOException();
		}
		if (file.getName().endsWith(".class")) {
			parser.load(file);
		} else if (file.getName().endsWith(".jar")) {
			for (File entry : getJarEntries(file)) {
				// parser.load(entry);
				parser.load(file.getAbsolutePath(), entry.getPath());
			}
		} else {
			throw new IOException();
		}
	}

	/**
	 * jarファイルの中からクラスファイルの一覧を取得する
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private List<File> getJarEntries(File file) throws IOException {
		List<File> entries = new ArrayList<File>();
		@SuppressWarnings("resource")
		Enumeration<JarEntry> entryEnum = new JarFile(file).entries();
		while (entryEnum.hasMoreElements()) {
			JarEntry entry = entryEnum.nextElement();
			if ((!entry.isDirectory()) && (entry.getName().endsWith(".class"))) {
				entries.add(new File(entry.getName()));
			}
		}
		return entries;
	}
}
