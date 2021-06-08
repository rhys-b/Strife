import javax.swing.JSpinner;
import javax.swing.JFormattedTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;



class MySpinner extends JSpinner implements FocusListener, ChangeListener {
	JFormattedTextField text;
	double min, max;
	
	public MySpinner(double init, double inMin, double inMax, double gap) {
		min = inMin;
		max = inMax;

		setModel(new SpinnerNumberModel(init, min - 1, max + 1, gap));

		text = ((DefaultEditor) getEditor()).getTextField();
		text.addFocusListener(this);

		((JSpinner.NumberEditor) getEditor()).getFormat()
			.setMaximumFractionDigits(Integer.MAX_VALUE);

		addChangeListener(this);
	}
	

	public void focusGained(FocusEvent fe) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				text.selectAll();
			}
		});
	}


	public void stateChanged(ChangeEvent ce) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				double val = (double) getValue();

				if (val == min - 1) {
					setValue(max);
				} else if (val == max + 1) {
					setValue(min);
				}
			}
		});
	}

	
	public void focusLost(FocusEvent fe) {}
}