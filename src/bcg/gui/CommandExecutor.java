package bcg.gui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * 外部コマンドを実行するクラス
 * 
 * @author s.watanabe
 */
public class CommandExecutor {

	private List<String> command;
	private Process proc;
	private File workingdir;

	/**
	 * 外部コマンドを指定して、インスタンス生成
	 * 
	 * @param commands
	 *            外部コマンド
	 */
	public CommandExecutor(String... commands) {
		command = Arrays.asList(commands);
	}

	/**
	 * コマンドの作業ディレクトリを設定する
	 * 
	 * @param dir
	 */
	public void setWorkingdir(File dir) {
		this.workingdir = dir;
	}

	/**
	 * 外部コマンドを実行する
	 * 
	 * @param stdin
	 *            　外部コマンドに渡す標準入力
	 * @throws IOException
	 */
	public void exec(String... stdin) throws IOException {
		ProcessBuilder pb = new ProcessBuilder(command);
		if (workingdir != null) {
			pb.directory(workingdir);
		}

		proc = pb.start();
		OutputStream out = proc.getOutputStream();
		for (String str : stdin) {
			out.write(str.getBytes());
		}
		if (stdin.length != 0) {
			out.close();
		}
	}

	/**
	 * コマンドの終了まで待つ
	 */
	public void waitFor() {
		try {
			proc.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * コマンドの標準出力ストリームを取得する
	 * 
	 * @return
	 */
	public InputStream getInputStream() {
		return proc.getInputStream();
	}

	/**
	 * コマンドの標準入力するとリームを取得する
	 * 
	 * @return
	 */
	public OutputStream getOutputStream() {
		return proc.getOutputStream();
	}

	/**
	 * コマンドの標準エラー出力を取得する
	 * 
	 * @return
	 */
	public InputStream getErrorStream() {
		return proc.getErrorStream();
	}
}
