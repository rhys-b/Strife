import javax.swing.JFrame;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import javax.imageio.ImageIO;


public class HtmlFrame {
	public static void show(String path, JFrame parent) {
		JFrame window = new JFrame();
		window.setSize(500, 500);
		window.setLocationRelativeTo(parent);

		JEditorPane view = new JEditorPane();
		view.setEditable(false);
		view.setContentType("text/html");
		try {
			window.setIconImage(ImageIO.read(
				HtmlFrame.class.getResourceAsStream("data/icon.png")));
			view.read(HtmlFrame.class.getResourceAsStream(path), null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		JScrollPane scroller = new JScrollPane(view, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.getVerticalScrollBar().setUnitIncrement(10);
		window.add(scroller);

		window.setVisible(true);
	}
}