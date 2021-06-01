import javax.swing.JSpinner;
import javax.swing.JFormattedTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;



class MySpinner extends JSpinner implements FocusListener {
	JFormattedTextField text;
	
	public MySpinner(double init, double min, double max, double gap) {
		setModel(new SpinnerNumberModel(init, min, max, gap));
		text = ((DefaultEditor) getEditor()).getTextField();
		text.addFocusListener(this);
	}
	
	public void focusGained(FocusEvent fe) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				text.selectAll();
			}
		});
	}
	
	public void focusLost(FocusEvent fe) {}
}