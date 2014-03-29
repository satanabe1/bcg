package bcg;

import java.io.File;
import java.util.Map;

import bcg.gui.CgFrame;
import bcg.mdl.ClassNode;

public class Main {

	/**
	 * Entry Point<br>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		CgManager generator = new CgManager();
		// generator.setSourcepath(System.getProperty("java.class.path"));
		// generator.setClasspath(System.getProperty("java.class.path"));
		for (String str : args) {
			try {
				generator.load(new File(str));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// BcelReadClassSample bcs = new BcelReadClassSample(str);
			// bcs.process();
		}

		Map<String, ClassNode> classes = generator.parse();
		CgFrame gui = new CgFrame();
		gui.setClasses(classes);
		gui.setVisible(true);

	}
}
