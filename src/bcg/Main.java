package bcg;

import java.io.File;
import java.io.IOException;

import bcg.gui.CgFrame;

/**
 * 実行クラス<br>
 * 引数にjarファイルかクラスファイルを指定できる
 * 
 * @author s.watanabe
 */
public class Main {

	/**
	 * Entry Point<br>
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		CgFrame gui = new CgFrame();
		gui.setVisible(true);

		for (String str : args) {
			gui.openJar(new File(str));
		}
	}
}
