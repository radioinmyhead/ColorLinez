import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class GameColorLinezMIDlet extends MIDlet {
	ColorLinezGameCanvas clCanvas;

	public GameColorLinezMIDlet() {
		clCanvas = new ColorLinezGameCanvas();

	}

	protected void destroyApp(boolean a) throws MIDletStateChangeException {
		notifyDestroyed();

	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {
		Display d = Display.getDisplay(this);
		clCanvas.start();
		d.setCurrent(clCanvas);

	}

}
