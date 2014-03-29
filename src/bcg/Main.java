package bcg;

import java.io.File;
import java.io.IOException;

import bcg.gui.CgFrame;

public class Main {

	/**
	 * Entry Point<br>
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		// CgManager generator = new CgManager();
		// for (String str : args) {
		// try {
		// generator.load(new File(str));
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		// }

		// Map<String, ClassNode> classes = generator.parse();
		CgFrame gui = new CgFrame();
		// gui.setClasses(classes);
		gui.setVisible(true);

		for (String str : args) {
			gui.openJar(new File(str));
		}
	}
}
