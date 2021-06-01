import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class GravitationHeaderLabel extends JLabel {
	public GravitationHeaderLabel(String text) {
		setText(text + "  ");
		setHorizontalAlignment(SwingConstants.RIGHT);
	}
}