import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import javax.swing.border.TitledBorder;

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;




public class Conversions extends JScrollPane {
	JPanel panel;
	
	public Conversions() {
		panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2));
		panel.add(new Work());
		panel.add(new Rotation());
		panel.add(new Momentum());
		panel.add(new Waves());
		
		this.setViewportView(panel);
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.getVerticalScrollBar().setUnitIncrement(8);
		this.getHorizontalScrollBar().setUnitIncrement(8);
	}
}

class Variable extends JComponent implements FocusListener {
	private JTextField text;
	private double value;

	public Variable(String title) {
		JLabel label = new JLabel(title);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setVerticalAlignment(SwingConstants.CENTER);
		
		text = new JTextField();
		text.addFocusListener(this);
		
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addComponent(label, 275, 275, 275)
			.addComponent(text, 250, 250, Integer.MAX_VALUE)
		);
		layout.setVerticalGroup(layout.createParallelGroup()
			.addComponent(label, 0, 25, 50)
			.addComponent(text, 0, 25, 50)
		);
		
		this.setLayout(layout);
	}
	
	public void focusGained(FocusEvent fe) {
		text.setForeground(null);
		text.selectAll();
	}
	
	public void focusLost(FocusEvent fe) {
		retrieveValue();
	}
	
	public void retrieveValue() {
		String string = text.getText();
	
		try {
			value = Double.parseDouble(string);
		} catch (Exception e) {
			if (!string.equals("")) {
				text.setForeground(Color.RED);
			}
			
			value = Double.NaN;
		}
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double inValue) {
		value = inValue;
		
		if (Double.isNaN(value)) {
			text.setText("Error");
			text.setForeground(Color.RED);
		} else {
			text.setForeground(null);
			text.setText("" + value);
		}
	}
	
	public void setAction(ActionListener al) {
		text.addActionListener(al);
	}
	
	public void reset() {
		text.setText("");
	}
		
	
	public static int countKnowns(Variable d1, Variable d2, Variable d3, Variable d4, Variable d5) {
		int out = 0;
		
		if (d1 != null && !Double.isNaN(d1.getValue())) {
			out++;
		}
		
		if (d2 != null && !Double.isNaN(d2.getValue())) {
			out++;
		}
		if (d3 != null && !Double.isNaN(d3.getValue())) {
			out++;
		}
		
		if (d4 != null && !Double.isNaN(d4.getValue())) {
			out++;
		}
		
		if (d5 != null && !Double.isNaN(d5.getValue())) {
			out++;
		}
		
		return out;
	}
	
	public static int countEmpty(Variable a, Variable b, Variable c, Variable d, Variable e, Variable f, Variable g, Variable h, Variable i, Variable j) {
		int out = 0;
	
		if (a != null && !Double.isNaN(a.getValue())) {
			out++;
		}
		
		if (b != null && !Double.isNaN(b.getValue())) {
			out++;
		}
		
		if (c != null && !Double.isNaN(c.getValue())) {
			out++;
		}
		
		if (d != null && !Double.isNaN(d.getValue())) {
			out++;
		}
		
		if (e != null && !Double.isNaN(e.getValue())) {
			out++;
		}
		
		if (f != null && !Double.isNaN(f.getValue())) {
			out++;
		}
		
		if (g != null && !Double.isNaN(g.getValue())) {
			out++;
		}
		
		if (h != null && !Double.isNaN(h.getValue())) {
			out++;
		}
		
		if (i != null && !Double.isNaN(i.getValue())) {
			out++;
		}
		
		if (j != null && !Double.isNaN(j.getValue())) {
			out++;
		}
		
		return out;
	}
}

class Rotation extends JComponent {
	private Variable d, t, v1, v2, a;

	public Rotation() {
		this.setLayout(new GridLayout(0, 1, 0, 5));
		this.setBorder(new TitledBorder("Rotation"));
		
		d = new Variable("Displacement(rad)");
		t = new Variable("Time(s)");
		v1 = new Variable("Initial Velocity(rad/s)");
		v2 = new Variable("Final Velocity(rad/s)");
		a = new Variable("Acceleration(rad/s^2)");
		
		this.add(d);
		this.add(t);
		this.add(v1);
		this.add(v2);
		this.add(a);
		
		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				updateVariables();
				
				tryAcceleration();
				tryDisplacementAverage();
				trySquares();
				tryDisplacementInitial();
				tryDisplacementFinal();
			}
		};
		
		this.add(new CalculateButton(action, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				d.reset();
				t.reset();
				v1.reset();
				v2.reset();
				a.reset();
			}
		}));
		
		d.setAction(action);
		t.setAction(action);
		v1.setAction(action);
		v2.setAction(action);
		a.setAction(action);
	}
	
	public void updateVariables() {
		d.retrieveValue();
		t.retrieveValue();
		v1.retrieveValue();
		v2.retrieveValue();
		a.retrieveValue();
	}
	
	public void tryAcceleration() {
		if (Variable.countKnowns(v1, v2, a, t, null) == 3) {
			if (Double.isNaN(a.getValue())) {
				a.setValue((v2.getValue() - v1.getValue()) / t.getValue());
			} else if (Double.isNaN(t.getValue())) {
				t.setValue((v2.getValue() - v1.getValue()) / a.getValue());
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue(v2.getValue() - a.getValue() * t.getValue());
			} else if (Double.isNaN(v2.getValue())) {
				v2.setValue(a.getValue() * t.getValue() + v1.getValue());
			}
		}
	}
	
	public void tryDisplacementInitial() {
		if (Variable.countKnowns(v1, d, a, t, null) == 3) {
			if (Double.isNaN(d.getValue())) {
				d.setValue(v1.getValue() * t.getValue() + 0.5 * a.getValue() * t.getValue() * t.getValue());
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue((d.getValue() - 0.5 * a.getValue() * t.getValue() * t.getValue()) / t.getValue());
			} else if (Double.isNaN(a.getValue())) {
				a.setValue(2 * (d.getValue() - v1.getValue() * t.getValue()) / (t.getValue() * t.getValue()));
			} else if (Double.isNaN(t.getValue())) {
				t.setValue(quadraticFormula(0.5 * a.getValue(), v1.getValue(), -1 * d.getValue()));
			}
		}
	}
	
	public void tryDisplacementFinal() {
		if (Variable.countKnowns(v2, d, a, t, null) == 3) {
			if (Double.isNaN(d.getValue())) {
				d.setValue(v2.getValue() * t.getValue() - 0.5 * a.getValue() * t.getValue() * t.getValue());
			} else if (Double.isNaN(v2.getValue())) {
				v2.setValue((d.getValue() + 0.5 * a.getValue() * t.getValue() * t.getValue()) / t.getValue());
			} else if (Double.isNaN(a.getValue())) {
				a.setValue((2 * v2.getValue() * t.getValue() - d.getValue()) / (t.getValue() * t.getValue()));
			} else if (Double.isNaN(t.getValue())) {
				t.setValue(quadraticFormula(0.5 * a.getValue(), -1 * v2.getValue(), d.getValue()));
			}
		}
	}
	
	public void tryDisplacementAverage() {
		if (Variable.countKnowns(d, v1, v2, t, null) == 3) {
			if (Double.isNaN(d.getValue())) {
				d.setValue((v1.getValue() + v2.getValue()) / 2 * t.getValue());
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue(2 * d.getValue() / t.getValue() - v2.getValue());
			} else if (Double.isNaN(v2.getValue())) {
				v2.setValue(2 * d.getValue() / t.getValue() - v1.getValue());
			} else if (Double.isNaN(t.getValue())) {
				t.setValue(2 * d.getValue() / (v1.getValue() + v2.getValue()));
			}
		}
	}
	
	public void trySquares() {
		if (Variable.countKnowns(v1, v2, a, d, null) == 3) {
			if (Double.isNaN(v2.getValue())) {
				v2.setValue(Math.sqrt(v1.getValue() * v1.getValue() + 2 * a.getValue() * d.getValue()));
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue(Math.sqrt(v2.getValue() * v2.getValue() - 2 * a.getValue() * d.getValue()));
			} else if (Double.isNaN(a.getValue())) {
				a.setValue((v2.getValue() * v2.getValue() - v1.getValue() * v1.getValue()) / (2 * d.getValue()));
			} else if (Double.isNaN(d.getValue())) {
				d.setValue((v2.getValue() * v2.getValue() - v1.getValue() * v1.getValue()) / (2 * a.getValue()));
			}
		}
	}
	
	public double quadraticFormula(double a, double b, double c) {
		double x = Double.NaN;
		int addSub = (b < 0) ? -1 : 1;
	
		try {
			x = (-1 * b + (addSub * Math.sqrt((b * b) - 4 * a * c))) / (2 * a);
		} catch (Exception e) {
			
		}
		
		return x;
	}
}

class Momentum extends JComponent {
	private Variable m, v, h, p, Ek, Ep;
	
	public Momentum() {
		this.setLayout(new GridLayout(0, 1, 0, 5));
		this.setBorder(new TitledBorder("Energy"));
		
		m = new Variable("Mass(kg) & Spring Constant(N/m)");
		v = new Variable("Velocity(m/s) & Amplitude(m)");
		h = new Variable("Height(m)");
		p = new Variable("Momentum(kg m/s)");
		Ek = new Variable("Kinetic Energy(J) & Spring Potential(J)");
		Ep = new Variable("Gravitational Potential Energy(J)");
		
		this.add(m);
		this.add(v);
		this.add(h);
		this.add(p);
		this.add(Ek);
		this.add(Ep);
		
		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				updateVariables();
				
				tryMomentum();
				tryKinetic();
				tryPotential();
			}
		};
		
		this.add(new CalculateButton(action, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				m.reset();
				v.reset();
				h.reset();
				p.reset();
				Ek.reset();
				Ep.reset();
			}
		}));
		
		m.setAction(action);
		v.setAction(action);
		h.setAction(action);
		p.setAction(action);
		Ek.setAction(action);
		Ep.setAction(action);
	}
	
	public void updateVariables() {
		m.retrieveValue();
		v.retrieveValue();
		h.retrieveValue();
		p.retrieveValue();
		Ek.retrieveValue();
		Ep.retrieveValue();
	}
	
	public void tryMomentum() {
		if (Variable.countKnowns(p, m, v, null, null) == 2) {
			if (Double.isNaN(p.getValue())) {
				p.setValue(m.getValue() * v.getValue());
			} else if (Double.isNaN(m.getValue())) {
				m.setValue(p.getValue() / v.getValue());
			} else if (Double.isNaN(v.getValue())) {
				v.setValue(p.getValue() / m.getValue());
			}
		}
	}
	
	public void tryKinetic() {
		if (Variable.countKnowns(Ek, m, v, null, null) == 2) {
			if (Double.isNaN(Ek.getValue())) {
				Ek.setValue(0.5 * m.getValue() * v.getValue() * v.getValue());
			} else if (Double.isNaN(m.getValue())) {
				m.setValue(2 * Ek.getValue() / (v.getValue() * v.getValue()));
			} else if (Double.isNaN(v.getValue())) {
				v.setValue(Math.sqrt(2 * Ek.getValue() / m.getValue()));
			}
		}
	}
	
	public void tryPotential() {
		if (Variable.countKnowns(Ep, m, h, null, null) == 2) {
			if (Double.isNaN(Ep.getValue())) {
				Ep.setValue(m.getValue() * 9.81 * h.getValue());
			} else if (Double.isNaN(m.getValue())) {
				m.setValue(Ep.getValue() / (9.81 * h.getValue()));
			} else if (Double.isNaN(h.getValue())) {
				h.setValue(Ep.getValue() / (9.81 * m.getValue()));
			}
		}
	}
}

class Waves extends JComponent {
	private Variable f, T, l, m, k, g;

	public Waves() {
		this.setLayout(new GridLayout(0, 1, 0, 5));
		this.setBorder(new TitledBorder("Oscillatory Motion"));
		
		f = new Variable("Frequency(Hz)");
		T = new Variable("Period(s)");
		k = new Variable("Spring Constant(N/m)");
		m = new Variable("Mass(kg)");
		l = new Variable("Length(m)");
		g = new Variable("Gravity (m/s^2)");
		
		g.setValue(9.81);
		
		this.add(f);
		this.add(T);
		this.add(l);
		this.add(m);
		this.add(k);
		this.add(g);
		
		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				updateVariables();
				
				inverse();
				spring();
				pendulum();
				inverse();
			}
		};
		
		this.add(new CalculateButton(action, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				f.reset();
				T.reset();
				l.reset();
				m.reset();
				k.reset();
				g.setValue(9.81);
			}
		}));
		
		f.setAction(action);
		T.setAction(action);
		l.setAction(action);
		m.setAction(action);
		k.setAction(action);
		g.setAction(action);
	}
	
	public void updateVariables() {
		f.retrieveValue();
		T.retrieveValue();
		l.retrieveValue();
		m.retrieveValue();
		k.retrieveValue();
		g.retrieveValue();
		
		if (g.getValue() < 0) {
			g.setValue(g.getValue() * -1);
		}
	}
	
	public void inverse() {
		if (Variable.countKnowns(T, f, null, null, null) == 1) {
			if (Double.isNaN(T.getValue())) {
				T.setValue(1 / f.getValue());
			} else if (Double.isNaN(f.getValue())) {
				f.setValue(1 / T.getValue());
			}
		} else if (Variable.countKnowns(T, f, null, null, null) == 2) {
			if (1 / f.getValue() != T.getValue()) {
				f.setValue(f.getValue() / T.getValue());
				T.setValue(1 / f.getValue());
			}
		}
	}
	
	public void spring() {
		if (Variable.countKnowns(T, k, m, null, null) == 2) {
			if (Double.isNaN(T.getValue())) {
				T.setValue(2 * Math.PI * Math.sqrt(m.getValue() / k.getValue()));
			} else if (Double.isNaN(m.getValue())) {
				m.setValue(T.getValue() * T.getValue() * k.getValue() / (4 * Math.PI * Math.PI));
			} else if (Double.isNaN(k.getValue())) {
				k.setValue(4 * Math.PI * Math.PI * m.getValue() / (T.getValue() * T.getValue()));
			}
		}
	}
	
	public void pendulum() {
		if (Double.isNaN(k.getValue()) && Double.isNaN(m.getValue()) && Variable.countKnowns(T, l, g, null, null) == 2) {
			if (Double.isNaN(T.getValue())) {
				T.setValue(2 * Math.PI * Math.sqrt(l.getValue() / g.getValue()));
			} else if (Double.isNaN(l.getValue())) {
				l.setValue(T.getValue() * T.getValue() * g.getValue() / (4 * Math.PI * Math.PI));
			} else if (Double.isNaN(g.getValue())) {
				g.setValue(4 * Math.PI * Math.PI * l.getValue() / (T.getValue() * T.getValue()));
			}
		}
	}
}

class Work extends JComponent {
	private Variable W, P, F, a, m, t, v, d, v1, v2;

	public Work() {
		this.setLayout(new GridLayout(0, 1, 0, 5));
		this.setBorder(new TitledBorder("Work"));
		
		d = new Variable("Displacement(m)");
		t = new Variable("Time(s)");
		m = new Variable("Mass(kg)");
		v1 = new Variable("Initial Velocity(m/s)");
		v2 = new Variable("Final Velocity(m/s)");
		v = new Variable("Change in Velocity(m/s)");
		a = new Variable("Acceleration(m/s^2)");
		F = new Variable("Force(N)");
		W = new Variable("Work(J)");
		P = new Variable("Power(W)");
		
		this.add(d);
		this.add(v);
		this.add(v1);
		this.add(v2);
		this.add(t);
		this.add(m);
		this.add(a);
		this.add(F);
		this.add(W);
		this.add(P);
		
		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int ini, fin;
			
				updateVariables();
				
				fin = Variable.countEmpty(d, v, v1, v2, t, m, a, F, W, P);
				do {
					ini = fin;
				
					velocity();
					acceleration();
					tryAcceleration();
					tryDisplacementAverage();
					trySquares();
					tryDisplacementInitial();
					tryDisplacementFinal();
					force();
					work();
					power();
					
					fin = Variable.countEmpty(d, v, v1, v2, t, m, a, F, W, P);
				} while (fin - ini > 0);
			}
		};
		
		this.add(new CalculateButton(action, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				d.reset();
				v.reset();
				v1.reset();
				v2.reset();
				t.reset();
				m.reset();
				a.reset();
				W.reset();
				F.reset();
				P.reset();
			}
		}));
		
		d.setAction(action);
		v.setAction(action);
		v1.setAction(action);
		v2.setAction(action);
		t.setAction(action);
		m.setAction(action);
		a.setAction(action);
		W.setAction(action);
		P.setAction(action);
		F.setAction(action);
	}
	
	public void updateVariables() {
		d.retrieveValue();
		v.retrieveValue();
		v1.retrieveValue();
		v2.retrieveValue();
		t.retrieveValue();
		m.retrieveValue();
		a.retrieveValue();
		W.retrieveValue();
		F.retrieveValue();
		P.retrieveValue();
	}
	
	public void acceleration() {
		if (Variable.countKnowns(v, t, a, null, null) == 2) {
			if (Double.isNaN(a.getValue())) {
				a.setValue(v.getValue() / t.getValue());
			} else if (Double.isNaN(t.getValue())) {
				t.setValue(v.getValue() / a.getValue());
			} else if (Double.isNaN(v.getValue())) {
				v.setValue(a.getValue() * t.getValue());
			}
		}
	}
	
	public void force() {
		if (Variable.countKnowns(F, m, a, null, null) == 2) {
			if (Double.isNaN(F.getValue())) {
				F.setValue(m.getValue() * a.getValue());
			} else if (Double.isNaN(m.getValue())) {
				m.setValue(F.getValue() / a.getValue());
			} else if (Double.isNaN(a.getValue())) {
				a.setValue(F.getValue() / m.getValue());
			}
		}
	}
	
	public void work() {
		if (Variable.countKnowns(W, F, d, null, null) == 2) {
			if (Double.isNaN(W.getValue())) {
				W.setValue(F.getValue() * d.getValue());
			} else if (Double.isNaN(F.getValue())) {
				F.setValue(W.getValue() / d.getValue());
			} else if (Double.isNaN(d.getValue())) {
				d.setValue(W.getValue() / F.getValue());
			}
		}
	}
	
	public void power() {
		if (Variable.countKnowns(P, W, t, null, null) == 2) {
			if (Double.isNaN(P.getValue())) {
				P.setValue(W.getValue() / t.getValue());
			} else if (Double.isNaN(W.getValue())) {
				W.setValue(P.getValue() * t.getValue());
			} else if (Double.isNaN(t.getValue())) {
				t.setValue(W.getValue() / P.getValue());
			}
		}
	}
	
	public void tryAcceleration() {
		if (Variable.countKnowns(v1, v2, a, t, null) == 3) {
			if (Double.isNaN(a.getValue())) {
				a.setValue((v2.getValue() - v1.getValue()) / t.getValue());
			} else if (Double.isNaN(t.getValue())) {
				t.setValue((v2.getValue() - v1.getValue()) / a.getValue());
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue(v2.getValue() - a.getValue() * t.getValue());
			} else if (Double.isNaN(v2.getValue())) {
				v2.setValue(a.getValue() * t.getValue() + v1.getValue());
			}
		}
	}
	
	public void tryDisplacementInitial() {
		if (Variable.countKnowns(v1, d, a, t, null) == 3) {
			if (Double.isNaN(d.getValue())) {
				d.setValue(v1.getValue() * t.getValue() + 0.5 * a.getValue() * t.getValue() * t.getValue());
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue((d.getValue() - 0.5 * a.getValue() * t.getValue() * t.getValue()) / t.getValue());
			} else if (Double.isNaN(a.getValue())) {
				a.setValue(2 * (d.getValue() - v1.getValue() * t.getValue()) / (t.getValue() * t.getValue()));
			} else if (Double.isNaN(t.getValue())) {
				t.setValue(quadraticFormula(0.5 * a.getValue(), v1.getValue(), -1 * d.getValue()));
			}
		}
	}
	
	public void tryDisplacementFinal() {
		if (Variable.countKnowns(v2, d, a, t, null) == 3) {
			if (Double.isNaN(d.getValue())) {
				d.setValue(v2.getValue() * t.getValue() - 0.5 * a.getValue() * t.getValue() * t.getValue());
			} else if (Double.isNaN(v2.getValue())) {
				v2.setValue((d.getValue() + 0.5 * a.getValue() * t.getValue() * t.getValue()) / t.getValue());
			} else if (Double.isNaN(a.getValue())) {
				a.setValue((2 * v2.getValue() * t.getValue() - d.getValue()) / (t.getValue() * t.getValue()));
			} else if (Double.isNaN(t.getValue())) {
				t.setValue(quadraticFormula(0.5 * a.getValue(), -1 * v2.getValue(), d.getValue()));
			}
		}
	}
	
	public void tryDisplacementAverage() {
		if (Variable.countKnowns(d, v1, v2, t, null) == 3) {
			if (Double.isNaN(d.getValue())) {
				d.setValue((v1.getValue() + v2.getValue()) / 2 * t.getValue());
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue(2 * d.getValue() / t.getValue() - v2.getValue());
			} else if (Double.isNaN(v2.getValue())) {
				v2.setValue(2 * d.getValue() / t.getValue() - v1.getValue());
			} else if (Double.isNaN(t.getValue())) {
				t.setValue(2 * d.getValue() / (v1.getValue() + v2.getValue()));
			}
		}
	}
	
	public void trySquares() {
		if (Variable.countKnowns(v1, v2, a, d, null) == 3) {
			if (Double.isNaN(v2.getValue())) {
				v2.setValue(Math.sqrt(v1.getValue() * v1.getValue() + 2 * a.getValue() * d.getValue()));
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue(Math.sqrt(v2.getValue() * v2.getValue() - 2 * a.getValue() * d.getValue()));
			} else if (Double.isNaN(a.getValue())) {
				a.setValue((v2.getValue() * v2.getValue() - v1.getValue() * v1.getValue()) / (2 * d.getValue()));
			} else if (Double.isNaN(d.getValue())) {
				d.setValue((v2.getValue() * v2.getValue() - v1.getValue() * v1.getValue()) / (2 * a.getValue()));
			}
		}
	}
	
	public void velocity() {
		if (Variable.countKnowns(v, v1, v2, null, null) == 2) {
			if (Double.isNaN(v.getValue())) {
				v.setValue(v2.getValue() - v1.getValue());
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue(v1.getValue() + v.getValue());
			} else if (Double.isNaN(v2.getValue())) {
				v2.setValue(v1.getValue() - v.getValue());
			}
		}
	}
	
	public double quadraticFormula(double a, double b, double c) {
		double x = Double.NaN;
		int addSub = (b < 0) ? -1 : 1;
	
		try {
			x = (-1 * b + (addSub * Math.sqrt((b * b) - 4 * a * c))) / (2 * a);
		} catch (Exception e) {
			
		}
		
		return x;
	}
}

class CalculateButton extends JComponent {
	private JButton button, reset;

	public CalculateButton(ActionListener al, ActionListener resetListener) {
		this.setPreferredSize(new Dimension(0, 35));
	
		button = new JButton("Calculate");
		button.setBounds(5, 5, 150, 25);
		button.addActionListener(al);
		
		reset = new JButton("Reset");
		reset.setBounds(160, 5, 150, 25);
		reset.addActionListener(resetListener);
		
		this.add(button);
		this.add(reset);
	}
}
