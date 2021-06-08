import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Font;
import java.awt.RenderingHints;

import javax.swing.JComponent;


public class Divider extends JComponent {
	private String title;


	public Divider(String s) {
		title = s;
	}


	public void paintComponent(Graphics g) {
		Graphics2D gg = (Graphics2D) g;
		int w = getWidth();
		int h = getHeight();
		int h2 = (int) Math.round(h / 2.0);
		FontMetrics fm = gg.getFontMetrics();
		int stringWidth = fm.stringWidth(title);
		int stringHeight = fm.getHeight();

		gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);

		gg.setColor(new Color(81, 81, 81));
		gg.drawLine(20, h2, 50, h2);
		gg.drawLine(stringWidth + 60, h2, w - 20, h2);

		gg.setColor(new Color(150, 150, 150));
		gg.drawString(title, 55, (int) Math.round(h2 + (0.25 * stringHeight)));
	}
}