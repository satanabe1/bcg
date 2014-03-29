package bcg.gui;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class NullComponentListener implements ComponentListener {

	public NullComponentListener() {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// System.out.println(e);
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// System.out.println(e);
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// System.out.println(e);
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// System.out.println(e);
	}

}
