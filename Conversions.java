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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;




public class Conversions extends JScrollPane implements ComponentListener {
	JPanel panel;

	Work work = new Work();
	Rotation rotation = new Rotation();
	Waves waves = new Waves();
	
	public Conversions() {
		panel = new JPanel();
		panel.setLayout(null);
		panel.add(work);
		panel.add(rotation);
		panel.add(waves);

		addComponentListener(this);
		
		setViewportView(panel);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		getVerticalScrollBar().setUnitIncrement(8);
		getHorizontalScrollBar().setUnitIncrement(8);
	}


	public void componentResized(ComponentEvent ce) {
		int width = Math.max(1100, getWidth() - getVerticalScrollBar().getWidth() - 3);

		work.setBounds(0, 0, width / 2, 545);
		rotation.setBounds(width / 2, 0, width / 2, 510);
		waves.setBounds(width / 2, 510, width / 2, 545);

		Dimension d = new Dimension(width, 1060);
		panel.setPreferredSize(d);
	}


	public static double quadratic(double a, double b, double c) {
		double x1 = Double.NaN, x2 = Double.NaN;

		try {
			x1 = (((-1 * b) + Math.sqrt((b * b) - (4 * a * c))) / (2 * a));
		} catch (Exception e) {}

		try {
			x2 = (((-1 * b) - Math.sqrt((b * b) - (4 * a * c))) / (2 * a));
		} catch (Exception e) {}

		if (x1 < 0 || Double.isNaN(x1)) {
			return x2;
		} else {
			return x1;
		}
	}


	public void componentHidden(ComponentEvent ce) {}
	public void componentShown(ComponentEvent ce) {}
	public void componentMoved(ComponentEvent ce) {}
}

class Variable extends JComponent implements FocusListener, KeyListener {
	private JTextField text;
	private double value;

	public Variable(String title) {
		JLabel label = new JLabel(title);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setVerticalAlignment(SwingConstants.CENTER);
		
		text = new JTextField();
		text.addFocusListener(this);
		text.addKeyListener(this);
		
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
		
		if (!Double.isNaN(value)) {
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
	
	public static int countKnown(Variable[] arr) {

		int out = 0;

		for (int z = 0; z < arr.length; z++) {
			if (arr[z] != null && !Double.isNaN(arr[z].getValue())) {
				out++;
			}
		}

		return out;
	}

	public static boolean tripple(Variable a, Variable b, Variable c) {
		Variable[] vars = {a, b, c};
		if (countKnown(vars) == 2) {
			if (Double.isNaN(a.getValue())) {
				a.setValue(b.getValue() * c.getValue());
			} else if (Double.isNaN(b.getValue())) {
				b.setValue(a.getValue() / c.getValue());
			} else if (Double.isNaN(c.getValue())) {
				c.setValue(a.getValue() / b.getValue());
			}

			return true;
		}

		return false;
	}

	public void setText(String s) {
		text.setText(s);
	}


	public void keyPressed(KeyEvent ke) {
		text.setForeground(null);
	}


	public void keyReleased(KeyEvent ke) {}
	public void keyTyped(KeyEvent ke) {}
}

class Rotation extends JComponent {
	private Variable d, t, v1, v2, a, vt1, vt2, r, ac, at, T, m, Fc;

	public Rotation() {
		this.setLayout(new GridLayout(0, 1, 0, 5));
		this.setBorder(new TitledBorder("Rotation"));
		
		d = new Variable("Displacement(rad)");
		t = new Variable("Time(s)");
		v1 = new Variable("[Initial] Velocity(rad/s)");
		v2 = new Variable("Final Velocity(rad/s)");
		a = new Variable("Acceleration(rad/s\u00B2)");
		vt1 = new Variable("[Initial] Tangential Velocity(m/s)");
		vt2 = new Variable("Final Tangential Velocity(m/s)");
		r = new Variable("Radius(m)");
		ac = new Variable("Centripetal Acceleration(m/s\u00B2)");
		at = new Variable("Tangential Acceleration(m/s\u00B2)");
		T = new Variable("Initial Period(s)");
		m = new Variable("Mass(kg)");
		Fc = new Variable("Centripetal Force(N)");
		
		add(d);
		add(t);
		add(v1);
		add(v2);
		add(a);
		add(ac);
		add(vt1);
		add(vt2);
		add(r);
		add(at);
		add(T);
		add(m);
		add(Fc);
		
		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				updateVariables();
				
				Variable[] vars = {d, t, v1, v2, a, ac, vt1, vt2, r, at, T};
				int fin = -1;
				int ini;

				while (true) {
					ini = fin;
					fin = Variable.countKnown(vars);

					if (ini == fin) {
						break;
					}

					if (Variable.tripple(Fc, ac, m)) continue;
					if (Variable.tripple(vt1, v1, r)) continue;
					if (Variable.tripple(vt2, v2, r)) continue;
					if (Variable.tripple(at, r, a)) continue;

					if (Double.isNaN(v2.getValue()) && Double.isNaN(a.getValue())) {
						if (Variable.tripple(d, t, v1)) continue;
					}

					if (tryAcceleration()) continue;
					if (tryDisplacementAverage()) continue;

					if (tryDisplacementInitial()) continue;
					if (tryDisplacementFinal()) continue;
					if (tryCentripetalVelocity()) continue;
					if (tryCentripetalPeriod()) continue;
					if (tryPeriodicVelocity()) continue;

					trySquares();
				}
			}
		};
		
		this.add(new CalculateButton(action, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				d.reset();
				t.reset();
				v1.reset();
				v2.reset();
				a.reset();
				vt1.reset();
				vt2.reset();
				r.reset();
				ac.reset();
				at.reset();
				T.reset();
				Fc.reset();
				m.reset();
			}
		}));
		
		d.setAction(action);
		t.setAction(action);
		v1.setAction(action);
		v2.setAction(action);
		a.setAction(action);
		vt1.setAction(action);
		vt2.setAction(action);
		r.setAction(action);
		ac.setAction(action);
		at.setAction(action);
		T.setAction(action);
		Fc.setAction(action);
		m.setAction(action);
	}
	
	public void updateVariables() {
		d.retrieveValue();
		t.retrieveValue();
		v1.retrieveValue();
		v2.retrieveValue();
		a.retrieveValue();
		vt1.retrieveValue();
		vt2.retrieveValue();
		r.retrieveValue();
		ac.retrieveValue();
		at.retrieveValue();
		T.retrieveValue();
		Fc.retrieveValue();
		m.retrieveValue();
	}
	
	public boolean tryAcceleration() {
		Variable[] vars = {v1, v2, a, t};
		if (Variable.countKnown(vars) == 3) {
			if (Double.isNaN(a.getValue())) {
				a.setValue((v2.getValue() - v1.getValue()) / t.getValue());
			} else if (Double.isNaN(t.getValue())) {
				t.setValue((v2.getValue() - v1.getValue()) / a.getValue());
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue(v2.getValue() - a.getValue() * t.getValue());
			} else if (Double.isNaN(v2.getValue())) {
				v2.setValue(a.getValue() * t.getValue() + v1.getValue());
			}

			return true;
		}

		return false;
	}
	
	public boolean tryDisplacementInitial() {
		Variable[] vars = {v1, d, a, t};
		if (Variable.countKnown(vars) == 3) {
			if (Double.isNaN(d.getValue())) {
				d.setValue(v1.getValue() * t.getValue() + 0.5 * a.getValue() * t.getValue() * t.getValue());
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue((d.getValue() - 0.5 * a.getValue() * t.getValue() * t.getValue()) / t.getValue());
			} else if (Double.isNaN(a.getValue())) {
				a.setValue(2 * (d.getValue() - v1.getValue() * t.getValue()) / (t.getValue() * t.getValue()));
			} else if (Double.isNaN(t.getValue())) {
				t.setValue(Conversions.quadratic(0.5 * a.getValue(), v1.getValue(), -1 * d.getValue()));
			}

			return true;
		}

		return false;
	}
	
	public boolean tryDisplacementFinal() {
		Variable[] vars = {v2, d, a, t};
		if (Variable.countKnown(vars) == 3) {
			if (Double.isNaN(d.getValue())) {
				d.setValue(v2.getValue() * t.getValue() - 0.5 * a.getValue() * t.getValue() * t.getValue());
			} else if (Double.isNaN(v2.getValue())) {
				v2.setValue((d.getValue() + 0.5 * a.getValue() * t.getValue() * t.getValue()) / t.getValue());
			} else if (Double.isNaN(a.getValue())) {
				a.setValue((2 * v2.getValue() * t.getValue() - d.getValue()) / (t.getValue() * t.getValue()));
			} else if (Double.isNaN(t.getValue())) {
				t.setValue(Conversions.quadratic(-0.5 * a.getValue(), v2.getValue(), -1 * d.getValue()));
			}

			return true;
		}

		return false;
	}
	
	public boolean tryDisplacementAverage() {
		Variable[] vars = {d, v1, v2, t};
		if (Variable.countKnown(vars) == 3) {
			if (Double.isNaN(d.getValue())) {
				d.setValue((v1.getValue() + v2.getValue()) / 2 * t.getValue());
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue(2 * d.getValue() / t.getValue() - v2.getValue());
			} else if (Double.isNaN(v2.getValue())) {
				v2.setValue(2 * d.getValue() / t.getValue() - v1.getValue());
			} else if (Double.isNaN(t.getValue())) {
				t.setValue(2 * d.getValue() / (v1.getValue() + v2.getValue()));
			}

			return true;
		}

		return false;
	}
	
	public boolean trySquares() {
		Variable[] vars = {v1, v2, a, d};
		if (Variable.countKnown(vars) == 3) {
			if (Double.isNaN(v2.getValue())) {
				v2.setValue(Math.sqrt(v1.getValue() * v1.getValue() + 2 * a.getValue() * d.getValue()));
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue(Math.sqrt(v2.getValue() * v2.getValue() - 2 * a.getValue() * d.getValue()));
			} else if (Double.isNaN(a.getValue())) {
				a.setValue((v2.getValue() * v2.getValue() - v1.getValue() * v1.getValue()) / (2 * d.getValue()));
			} else if (Double.isNaN(d.getValue())) {
				d.setValue((v2.getValue() * v2.getValue() - v1.getValue() * v1.getValue()) / (2 * a.getValue()));
			}

			return true;
		}

		return false;
	}


	public boolean tryCentripetalVelocity() {
		Variable[] vars = {ac, r, vt1};
		if (Variable.countKnown(vars) == 2) {
			if (Double.isNaN(ac.getValue())) {
				ac.setValue(Math.abs((vt1.getValue() * vt1.getValue())
					/ r.getValue()));
			} else if (Double.isNaN(r.getValue())) {
				r.setValue((vt1.getValue() * vt1.getValue()) / ac.getValue());
			} else if (Double.isNaN(vt1.getValue())) {
				vt1.setValue(Math.sqrt(ac.getValue() * r.getValue()));
			}

			return true;
		}

		return false;
	}

	public boolean tryCentripetalPeriod() {
		Variable[] vars = {ac, r, T};
		if (Variable.countKnown(vars) == 2) {
			if (Double.isNaN(ac.getValue())) {
				ac.setValue(Math.abs((4 * Math.PI * Math.PI * r.getValue())
					/ (T.getValue() * T.getValue())));
			} else if (Double.isNaN(r.getValue())) {
				r.setValue((ac.getValue() * T.getValue() * T.getValue())
					/ (4 * Math.PI * Math.PI));
			} else if (Double.isNaN(T.getValue())) {
				T.setValue(Math.sqrt((4 * Math.PI * Math.PI * r.getValue())
					/ (ac.getValue())));
			}

			return true;
		}

		return false;
	}

	public boolean tryPeriodicVelocity() {
		Variable[] vars = {T, v1};

		if (Variable.countKnown(vars) == 1) {
			if (Double.isNaN(T.getValue())) {
				T.setValue(Math.PI * 2 / v1.getValue());
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue(Math.PI * 2 / T.getValue());
			}

			return true;
		}

		return false;
	}
}



class Waves extends JComponent {
	private Variable f, T, m, k, x, E, F, V, l, g, h, ha;

	public Waves() {
		this.setLayout(new GridLayout(0, 1, 0, 5));
		this.setBorder(new TitledBorder("Simple Harmonic Motion"));
		
		f = new Variable("Frequency(Hz)");
		T = new Variable("Period(s)");
		k = new Variable("Spring Constant(N/m)");
		x = new Variable("Amplitude(m)");
		E = new Variable("Mechanical Energy(J)");
		m = new Variable("Mass(kg)");
		F = new Variable("Force(N)");
		V = new Variable("Maximum Velocity(m/s)");
		l = new Variable("Length(m)");
		g = new Variable("Gravitational Acceleration(m/s\u00B2)");
		h = new Variable("Height(m)");
		ha = new Variable("Amplitude(\u00B0)");

		g.setValue(9.81);
		
		add(f);
		add(T);
		add(V);
		add(E);
		add(m);
		add(new Divider("Springs"));
		add(k);
		add(x);
		add(F);
		add(new Divider("Pendulums"));
		add(l);
		add(g);
		add(h);
		add(ha);
		
		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				updateVariables();

				int ini, fin = -1;
				Variable[] vars = {f, T, m, x, k, F, E, V, l, g, h, ha};

				simplify();

				while (true) {
					ini = fin;
					fin = Variable.countKnown(vars);

					if (ini == fin) {
						break;
					}

					if (Work.kineticEnergy(E, m, V)) continue;
					if (inverse()) continue;
					if (spring()) continue;

					if (Variable.tripple(F, x, k)) continue;
					if (Work.kineticEnergy(E, k, x)) continue;
					if (pendulum()) continue;
					if (pendulumHeight()) continue;
					if (pendulumEnergy()) continue;

					if (pendulumAngle()) continue;
				}
			}
		};
		
		this.add(new CalculateButton(action, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				f.reset();
				T.reset();
				m.reset();
				k.reset();
				x.reset();
				E.reset();
				F.reset();
				V.reset();
				l.reset();
				g.setValue(9.81);
				h.reset();
				ha.reset();
			}
		}));
		
		f.setAction(action);
		T.setAction(action);
		m.setAction(action);
		k.setAction(action);
		E.setAction(action);
		x.setAction(action);
		F.setAction(action);
		V.setAction(action);
		l.setAction(action);
		g.setAction(action);
		h.setAction(action);
		ha.setAction(action);
	}
	
	public void updateVariables() {
		f.retrieveValue();
		T.retrieveValue();
		m.retrieveValue();
		k.retrieveValue();
		x.retrieveValue();
		E.retrieveValue();
		F.retrieveValue();
		V.retrieveValue();
		l.retrieveValue();
		g.retrieveValue();
		h.retrieveValue();
		ha.retrieveValue();

		g.setValue(Math.abs(g.getValue()));
	}
	
	public boolean inverse() {
		Variable[] vars = {T, f};
		if (Variable.countKnown(vars) == 1) {
			if (Double.isNaN(T.getValue())) {
				T.setValue(1 / f.getValue());
			} else if (Double.isNaN(f.getValue())) {
				f.setValue(1 / T.getValue());
			}

			return true;
		}

		return false;
	}
	
	public boolean spring() {
		Variable[] vars = {T, k, m};
		if (Variable.countKnown(vars) == 2) {
			if (Double.isNaN(T.getValue())) {
				T.setValue(2 * Math.PI * Math.sqrt(m.getValue() / k.getValue()));
			} else if (Double.isNaN(m.getValue())) {
				m.setValue(T.getValue() * T.getValue() * k.getValue() / (4 * Math.PI * Math.PI));
			} else if (Double.isNaN(k.getValue())) {
				k.setValue(4 * Math.PI * Math.PI * m.getValue() / (T.getValue() * T.getValue()));
			}

			return true;
		}

		return false;
	}


	public boolean simplify() {
		Variable[] vars = {f, T};

		if (Variable.countKnown(vars) == 2) {
			if (1 / f.getValue() != T.getValue()) {
				f.setValue(f.getValue() / T.getValue());
				T.setValue(1 / f.getValue());
			}

			return true;
		}

		return false;
	}


	public boolean pendulum() {
		Variable[] vars = {l, g, T};

		if (Variable.countKnown(vars) == 2) {
			if (Double.isNaN(l.getValue())) {
				l.setValue((T.getValue() * T.getValue() * g.getValue())
					/ (4.0 * Math.PI * Math.PI));
			} else if (Double.isNaN(g.getValue())) {
				g.setValue((4.0 * Math.PI * Math.PI * l.getValue())
					/ (T.getValue() * T.getValue()));
			} else if (Double.isNaN(T.getValue())) {
				T.setValue(2.0 * Math.PI * Math.sqrt(l.getValue() / g.getValue()));
			}

			return true;
		}

		return false;
	}


	public boolean pendulumHeight() {
		Variable[] vars = {V, h, g};

		if (Variable.countKnown(vars) == 2) {
			if (Double.isNaN(V.getValue())) {
				V.setValue(Math.sqrt(2.0 * g.getValue() * h.getValue()));
			} else if (Double.isNaN(h.getValue())) {
				h.setValue((V.getValue() * V.getValue()) / (2.0 * g.getValue()));
			} else if (Double.isNaN(g.getValue())) {
				g.setValue((V.getValue() * V.getValue()) / (2.0 * h.getValue()));
			}

			return true;
		}

		return false;
	}


	public boolean pendulumAngle() {
		Variable[] vars = {h, l, ha};

		if (Variable.countKnown(vars) == 2) {
			if (Double.isNaN(ha.getValue())) {
				ha.setValue(Math.toDegrees(Math.acos((l.getValue()
					- h.getValue()) / l.getValue())));
			} else if (Double.isNaN(h.getValue())) {
				h.setValue(-1.0 * l.getValue() * Math.cos(Math.toRadians(
					ha.getValue())) + l.getValue());
			} else if (Double.isNaN(l.getValue())) {
				l.setValue(h.getValue() / (1.0 - Math.cos(
					Math.toRadians(ha.getValue()))));
			}

			return true;
		}

		return false;
	}


	public boolean pendulumEnergy() {
		Variable[] vars = {E, h, g, m};

		if (Variable.countKnown(vars) == 3) {
			if (Double.isNaN(E.getValue())) {
				E.setValue(m.getValue() * g.getValue() * h.getValue());
			} else if (Double.isNaN(h.getValue())) {
				h.setValue(E.getValue() / (m.getValue() * g.getValue()));
			} else if (Double.isNaN(g.getValue())) {
				g.setValue(E.getValue() / (m.getValue() * h.getValue()));
			} else if (Double.isNaN(m.getValue())) {
				m.setValue(E.getValue() / (g.getValue() * h.getValue()));
			}

			return true;
		}

		return false;
	}
}



class Work extends JComponent {
	private Variable W, P, F, a, m, t, v, d, v1, v2, vave, p, E, angle;

	public Work() {
		this.setLayout(new GridLayout(0, 1, 0, 5));
		this.setBorder(new TitledBorder("Work & Energy"));
		
		d = new Variable("Displacement(m)");
		t = new Variable("Time(s)");
		m = new Variable("Mass(kg)");
		v1 = new Variable("[Initial] Velocity(m/s)");
		v2 = new Variable("Final Velocity(m/s)");
		v = new Variable("Change in Velocity(m/s)");
		a = new Variable("Acceleration(m/s\u00B2)");
		F = new Variable("Force(N)");
		angle = new Variable("Angle of Force(\u00B0)");
		W = new Variable("Work(J)");
		P = new Variable("Power(W)");
		vave = new Variable("Average Velocity(m/s)");
		p = new Variable("Initial Momentum(kg \u2022 m/s)");
		E = new Variable("Initial Kinetic Energy(J)");

		angle.setValue(0);
		
		this.add(d);
		this.add(t);
		this.add(v1);
		this.add(v2);
		this.add(a);
		this.add(v);
		this.add(vave);
		this.add(p);
		this.add(m);
		this.add(F);
		this.add(angle);
		this.add(W);
		this.add(P);
		this.add(E);
		
		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int ini, fin = -1;
			
				updateVariables();
				
				Variable[] vars = {d, v, v1, v2, t, m, a, F, W, P, vave, p, E};
				while (true) {
					ini = fin;
					fin = Variable.countKnown(vars);

					if (ini == fin) {
						break;
					}
				
					if (Variable.tripple(v, a, t)) continue;
					if (Variable.tripple(F, m, a)) continue;
					if (Variable.tripple(W, P, t)) continue;
					if (Variable.tripple(d, vave, t)) continue;
					if (Variable.tripple(p, v1, m)) continue;

					if (Double.isNaN(v2.getValue()) && Double.isNaN(a.getValue())) {
						if (Variable.tripple(d, t, v1)) continue;
					}

					if (velocity()) continue;
					if (tryAcceleration()) continue;
					if (tryVelocityAverage()) continue;
					if (tryDisplacementAverage()) continue;
					if (tryWork()) continue;
					if (kineticEnergy(E, m, v1)) continue;

					if (tryDisplacementInitial()) continue;
					if (tryDisplacementFinal()) continue;

					trySquares();
				}
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
				vave.reset();
				p.reset();
				angle.setValue(0);
				E.reset();
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
		vave.setAction(action);
		p.setAction(action);
		E.setAction(action);
		angle.setAction(action);
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
		vave.retrieveValue();
		p.retrieveValue();
		angle.retrieveValue();
		E.retrieveValue();
	}
	
	public boolean tryAcceleration() {
		Variable[] vars = {v1, v2, a, t};
		if (Variable.countKnown(vars) == 3) {
			if (Double.isNaN(a.getValue())) {
				a.setValue((v2.getValue() - v1.getValue()) / t.getValue());
			} else if (Double.isNaN(t.getValue())) {
				t.setValue((v2.getValue() - v1.getValue()) / a.getValue());
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue(v2.getValue() - a.getValue() * t.getValue());
			} else if (Double.isNaN(v2.getValue())) {
				v2.setValue(a.getValue() * t.getValue() + v1.getValue());
			}

			return true;
		}

		return false;
	}
	
	public boolean tryDisplacementInitial() {
		Variable[] vars = {v1, d, a, t};
		if (Variable.countKnown(vars) == 3) {
			if (Double.isNaN(d.getValue())) {
				d.setValue(v1.getValue() * t.getValue() + 0.5 * a.getValue() * t.getValue() * t.getValue());
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue((d.getValue() - 0.5 * a.getValue() * t.getValue() * t.getValue()) / t.getValue());
			} else if (Double.isNaN(a.getValue())) {
				a.setValue(2 * (d.getValue() - v1.getValue() * t.getValue()) / (t.getValue() * t.getValue()));
			} else if (Double.isNaN(t.getValue())) {
				t.setValue(Conversions.quadratic(0.5 * a.getValue(), v1.getValue(), -1 * d.getValue()));
			}

			return true;
		}

		return false;
	}
	
	public boolean tryDisplacementFinal() {
		Variable[] vars = {v2, d, a, t};
		if (Variable.countKnown(vars) == 3) {
			if (Double.isNaN(d.getValue())) {
				d.setValue(v2.getValue() * t.getValue() - 0.5 * a.getValue() * t.getValue() * t.getValue());
			} else if (Double.isNaN(v2.getValue())) {
				v2.setValue((d.getValue() + 0.5 * a.getValue() * t.getValue() * t.getValue()) / t.getValue());
			} else if (Double.isNaN(a.getValue())) {
				a.setValue((2 * v2.getValue() * t.getValue() - d.getValue()) / (t.getValue() * t.getValue()));
			} else if (Double.isNaN(t.getValue())) {
				t.setValue(Conversions.quadratic(-0.5 * a.getValue(), -1 * v2.getValue(), d.getValue()));
			}

			return true;
		}

		return false;
	}
	
	public boolean tryDisplacementAverage() {
		Variable[] vars = {d, v1, v2, t};
		if (Variable.countKnown(vars) == 3) {
			if (Double.isNaN(d.getValue())) {
				d.setValue((v1.getValue() + v2.getValue()) / 2 * t.getValue());
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue(2 * d.getValue() / t.getValue() - v2.getValue());
			} else if (Double.isNaN(v2.getValue())) {
				v2.setValue(2 * d.getValue() / t.getValue() - v1.getValue());
			} else if (Double.isNaN(t.getValue())) {
				t.setValue(2 * d.getValue() / (v1.getValue() + v2.getValue()));
			}

			return true;
		}

		return false;
	}
	
	public boolean trySquares() {
		Variable[] vars = {v1, v2, a, d};
		if (Variable.countKnown(vars) == 3) {
			if (Double.isNaN(v2.getValue())) {
				v2.setValue(Math.sqrt(v1.getValue() * v1.getValue() + 2 * a.getValue() * d.getValue()));
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue(Math.sqrt(v2.getValue() * v2.getValue() - 2 * a.getValue() * d.getValue()));
			} else if (Double.isNaN(a.getValue())) {
				a.setValue((v2.getValue() * v2.getValue() - v1.getValue() * v1.getValue()) / (2 * d.getValue()));
			} else if (Double.isNaN(d.getValue())) {
				d.setValue((v2.getValue() * v2.getValue() - v1.getValue() * v1.getValue()) / (2 * a.getValue()));
			}

			return true;
		}

		return false;
	}
	
	public boolean velocity() {
		Variable[] vars = {v, v1, v2};
		if (Variable.countKnown(vars) == 2) {
			if (Double.isNaN(v.getValue())) {
				v.setValue(v2.getValue() - v1.getValue());
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue(v2.getValue() - v.getValue());
			} else if (Double.isNaN(v2.getValue())) {
				v2.setValue(v1.getValue() + v.getValue());
			}

			return true;
		}

		return false;
	}


	public boolean tryVelocityAverage() {
		Variable[] vars = {v1, v2, vave};

		if (Variable.countKnown(vars) == 2) {
			if (Double.isNaN(vave.getValue())) {
				vave.setValue((v1.getValue() + v2.getValue()) / 2);
			} else if (Double.isNaN(v1.getValue())) {
				v1.setValue(2 * vave.getValue() - v2.getValue());
			} else if (Double.isNaN(v2.getValue())) {
				v2.setValue(2 * vave.getValue() - v1.getValue());
			}

			return true;
		}

		return false;
	}


	public boolean tryWork() {
		Variable[] vars = {W, F, angle, d};

		if (Variable.countKnown(vars) == 3) {
			if (Double.isNaN(W.getValue())) {
				W.setValue(F.getValue() * d.getValue() *
					Math.cos(Math.toRadians(angle.getValue())));
			} else if (Double.isNaN(F.getValue())) {
				F.setValue(W.getValue() / (d.getValue() *
					Math.cos(Math.toRadians(angle.getValue()))));
			} else if (Double.isNaN(angle.getValue())) {
				angle.setValue(Math.toDegrees(Math.acos(W.getValue() /
					(F.getValue() * d.getValue()))));
			} else if (Double.isNaN(d.getValue())) {
				d.setValue(W.getValue() / (F.getValue() *
					Math.cos(Math.toRadians(angle.getValue()))));
			}

			return true;
		}

		return false;
	}


	public static boolean kineticEnergy(Variable En, Variable ma, Variable ve) {
		Variable[] vars = {En, ma, ve};

		if (Variable.countKnown(vars) == 2) {
			if (Double.isNaN(En.getValue())) {
				En.setValue(0.5 * ma.getValue() * ve.getValue() * ve.getValue());
			} else if (Double.isNaN(ma.getValue())) {
				ma.setValue(2.0 * En.getValue() / (ve.getValue() * ve.getValue()));
			} else if (Double.isNaN(ve.getValue())) {
				ve.setValue(Math.sqrt(2.0 * En.getValue() / ma.getValue()));
			}

			return true;
		}

		return false;
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
