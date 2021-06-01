import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout;

import javax.swing.border.TitledBorder;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.Image;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;



public class TDVectors extends Mode {
	private Vector resultant;
	private double scale = 1;

	public TDVectors() {
		resultant = new Vector(this, true);
		listPanel.add(resultant.getPanel());
		listPanel.revalidate();
		
		graphics.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent mwe) {
				if (scale + Math.sqrt(scale) * mwe.getWheelRotation() * -0.1 > 0) {
					scale += Math.sqrt(scale) * mwe.getWheelRotation() * -0.1;
					graphics.repaint();
				}
			}
		});
	}

	public void addButton() {
		Vector v = new Vector(this, false);
		
		listPanel.add(v.getPanel());
		listPanel.revalidate();
		
		list.add(v);
	}
	
	public void paintGraphics(Graphics2D gg) {
		gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
	
		background(gg);
		arrows(gg);
		result(gg);
		dot(gg);
	}
	
	public void background(Graphics2D gg) {
		gg.setColor(Color.WHITE);
		gg.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
	
	public void arrows(Graphics2D gg) {
		double mag;
		Vector v;
		int unlocked = findUnlocked();
		
		gg.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
	
		for (int i = 0; i < list.size(); i++) {
			v = (Vector) list.get(i);
			mag = v.getMagnitude();
			if (mag != 0 && v.getVisible() && !(resultant.getLocked() && i == unlocked)) {
				drawArrow(gg, v, mag);
			}
		}
		
		if (resultant.getLocked() && !Double.isNaN(resultant.getDirection())) {
			drawArrow(gg, resultant, resultant.getMagnitude());
		}
	}
	
	public void drawArrow(Graphics2D gg, Vector v, double mag) {
		double rad = Math.toRadians(v.getDirection());
		int x = hx + (int) Math.round(mag * scale * Math.cos(rad));
		int y = hy - (int) Math.round(mag * scale * Math.sin(rad));
		
		gg.setColor(v.getColor());
		gg.drawLine(hx, hy, x, y);
		
		arrowHead(gg, rad, x, y);
	}
	
	public void dot(Graphics2D gg) {
		gg.setColor(Color.BLACK);
		gg.fillOval(hx - 5, hy - 5, 10, 10);
	}
	
	public void arrowHead(Graphics2D gg, double rad, int x, int y) {
		double right = rad + Math.PI / 4;
		double left = rad - Math.PI /4;
		
		int endX = (int) Math.round(x - 10 * Math.cos(right));
		int endY = (int) Math.round(y + 10 * Math.sin(right));
		
		gg.drawLine(x, y, endX, endY);
		
		endX = (int) Math.round(x - 10 * Math.cos(left));
		endY = (int) Math.round(y + 10 * Math.sin(left));
		
		gg.drawLine(x, y, endX, endY);
	}
	
	public void result(Graphics2D gg) {
		double totalX, totalY, rad;
		int index;
		TDMag m2D;
	
		if (!resultant.getLocked()) {
			drawResult(gg, resultant, countMagnitudes(-1));
		} else {
			index = findUnlocked();
			
			if (index != -1) {
				m2D = countMagnitudes(index);
				rad = Math.toRadians(resultant.getDirection());
				
				totalX = (resultant.getMagnitude() * Math.cos(rad)) - m2D.getX();
				totalY = (resultant.getMagnitude() * Math.sin(rad)) - m2D.getY();

				if (!(Double.isNaN(totalX) && Double.isNaN(totalY))) {
					drawResult(gg, (Vector) list.get(index), new TDMag(totalX, totalY));
				}
			}
		}
	}
	
	public void drawResult(Graphics2D gg, Vector v, TDMag m2D) {
		double rad = Math.atan(m2D.getY() / m2D.getX());
		double mag = Math.sqrt(m2D.getX() * m2D.getX() + m2D.getY() * m2D.getY());
		
		if (m2D.getX() < 0) {
			rad += Math.PI;
		} else if (m2D.getY() < 0) {
			rad += 2 * Math.PI;
		}
		
		if (mag < 0.00001) {
			mag = 0;
			v.setDirection(Double.NaN);
		} else {
			v.setDirection(Math.toDegrees(rad));
		}
		v.setMagnitude(mag);
		
		if (mag != 0) {
			drawArrow(gg, v, mag);
		}
	}
	
	public int findUnlocked() {
		for (int i = 0; i < list.size(); i++) {
			if (!((Vector) list.get(i)).getLocked()) {
				return i;
			}
		}
		
		return -1;
	}
	
	public TDMag countMagnitudes(int exclude) {
		double totalX = 0, totalY = 0, mag, rad;
		Vector v;
		
		for (int i = 0; i < list.size(); i++) {
			v = (Vector) list.get(i);
			
			if (i != exclude && v.getVisible()) {
				mag = v.getMagnitude();
				
				if (mag != 0) {
					rad = Math.toRadians(v.getDirection());
				
					totalX += mag * Math.cos(rad);
					totalY += mag * Math.sin(rad);
				}
			}
		}
		
		return new TDMag(totalX, totalY);
	}
	
	public void shuffle(int i) {
		for (; i < list.size(); i++) {
			((Vector) list.get(i)).shuffle();
		}
	}
}



class Vector {
	private double magnitude, direction;
	private JPanel panel;
	private int index;
	private boolean locked = true, hidden = false;
	private MySpinner mag, dir;
	private ColorButton clr;
	private JButton lock, hide;
	private ImageIcon visibleIcon, invisibleIcon, lockedIcon, unlockedIcon, closeIcon;

	public Vector(TDVectors vec, boolean resultant) {
		try {
			visibleIcon = new ImageIcon(ImageIO.read(TDVectors.class.getResourceAsStream("data/eye.png")).getScaledInstance(40, 40, Image.SCALE_SMOOTH));
			invisibleIcon = new ImageIcon(ImageIO.read(TDVectors.class.getResourceAsStream("data/eyent.png")).getScaledInstance(40, 40, Image.SCALE_SMOOTH));
			lockedIcon = new ImageIcon(ImageIO.read(TDVectors.class.getResourceAsStream("data/lock.png")).getScaledInstance(40, 40, Image.SCALE_SMOOTH));
			unlockedIcon = new ImageIcon(ImageIO.read(TDVectors.class.getResourceAsStream("data/unlock.png")).getScaledInstance(40, 40, Image.SCALE_SMOOTH));
			closeIcon = new ImageIcon(ImageIO.read(TDVectors.class.getResourceAsStream("data/close.png")).getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		index = vec.listSize();
	
		magnitude = 0;
		direction = 90;
		
		panel = new JPanel();
		GroupLayout gl = new GroupLayout(panel);
		
		mag = new MySpinner(magnitude, 0, Double.POSITIVE_INFINITY, 1.0);
		mag.setBorder(new TitledBorder("Magnitude"));
		
		dir = new MySpinner(direction, 0, 359, 1.0);
		dir.setBorder(new TitledBorder("Direction"));
		
		hide = new JButton(visibleIcon);
		hide.setToolTipText("Visible");
		
		if (resultant) {
			panel.setBorder(new TitledBorder("Resultant"));
			clr = new ColorButton(vec.getGraphicComponent(), Color.RED);
			panel.add(clr);
			
			lock = new JButton(unlockedIcon);
			panel.add(lock);
			locked = false;
			mag.setEnabled(false);
			dir.setEnabled(false);
			
			gl.setHorizontalGroup(gl.createSequentialGroup()
				.addComponent(mag, 0, 175, Integer.MAX_VALUE)
				.addComponent(dir, 0, 175, Integer.MAX_VALUE)
				.addComponent(clr, 0, 50, 50)
				.addComponent(lock, 0, 50, 50));
			gl.setVerticalGroup(gl.createParallelGroup()
				.addComponent(mag, 60, 60, 60)
				.addComponent(dir, 60, 60, 60)
				.addComponent(clr, 60, 60, 60)
				.addComponent(lock, 60, 60, 60));
		} else {			
			clr = new ColorButton(vec.getGraphicComponent());
			panel.add(clr);
			
			lock = new JButton(lockedIcon);
			panel.add(lock);
			
			JButton remove = new JButton(closeIcon);
			remove.setToolTipText("Remove vector");
			vec.addActionRefresh(remove);
			remove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					vec.removeList(index, 1);
				}
			});
			panel.add(remove);
			
			gl.setHorizontalGroup(gl.createSequentialGroup()
				.addComponent(mag, 0, 125, Integer.MAX_VALUE)
				.addComponent(dir, 0, 125, Integer.MAX_VALUE)
				.addComponent(clr, 0, 50, 50)
				.addComponent(hide, 0, 50, 50)
				.addComponent(lock, 0, 50, 50)
				.addComponent(remove, 0, 50, 50));
			gl.setVerticalGroup(gl.createParallelGroup()
				.addComponent(mag, 50, 50, 50)
				.addComponent(dir, 50, 50, 50)
				.addComponent(clr, 50, 50, 50)
				.addComponent(hide, 50, 50, 50)
				.addComponent(lock, 50, 50, 50)
				.addComponent(remove, 50, 50, 50));
		}
		
		lock.setToolTipText("Lock");
		
		panel.setLayout(gl);
		
		lock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (locked) {
					mag.setEnabled(false);
					dir.setEnabled(false);
				
					lock.setIcon(unlockedIcon);
				} else {
					mag.setEnabled(true);
					dir.setEnabled(true);
					
					lock.setIcon(lockedIcon);
				}
				
				locked = !locked;
			}
		});
		
		hide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (hidden) {
					hide.setIcon(visibleIcon);
				} else {
					hide.setIcon(invisibleIcon);
				}
				
				hidden = !hidden;
			}
		});
		
		vec.addActionRefresh(lock);
		vec.addActionRefresh(hide);
		vec.addActionRefresh(clr);
		vec.addChangeRefresh(mag);
		vec.addChangeRefresh(dir);
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public void shuffle() {
		index--;
	}
	
	public Color getColor() {
		return clr.getColor();
	}
	
	public double getMagnitude() {
		return (double) mag.getValue();
	}
	
	public double getDirection() {
		return (double) dir.getValue();
	}
	
	public void setMagnitude(double d) {
		mag.setValue(d);
	}
	
	public void setDirection(double d) {
		dir.setValue(d);
	}
	
	public boolean getLocked() {
		return locked;
	}
	
	public boolean getVisible() {
		return !hidden;
	}
}

class TDMag {
	double x, y;

	public TDMag(double inX, double inY) {
		x = inX;
		y = inY;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
}
