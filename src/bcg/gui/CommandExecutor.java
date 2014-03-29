package bcg.gui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

public class CommandExecutor {

	private List<String> command;
	private Process proc;
	private File workingdir;

	public CommandExecutor(String... commands) {
		command = Arrays.asList(commands);
	}

	public void setWorkingdir(File dir) {
		this.workingdir = dir;
	}

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

	public void waitFor() {
		try {
			proc.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public InputStream getInputStream() {
		return proc.getInputStream();
	}

	public OutputStream getOutputStream() {
		return proc.getOutputStream();
	}

	public InputStream getErrorStream() {
		return proc.getErrorStream();
	}
}
