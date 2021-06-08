import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.Image;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JCheckBoxMenuItem;

import javax.swing.border.TitledBorder;

import javax.imageio.ImageIO;

import java.util.ArrayList;



public class Gravitation extends Mode implements MouseMotionListener, MouseListener, MouseWheelListener {
	private double scale = 1, convert = 50;
	private int originX = 310, originY = 310;
	private int mouseX, mouseY;
	private MySpinner massExponent, distanceExponent;

	public Gravitation() {
		graphics.addMouseMotionListener(this);
		graphics.addMouseListener(this);
		graphics.addMouseWheelListener(this);

		JPanel scalePanel = new JPanel(new GridLayout(1, 4));
		scalePanel.setPreferredSize(new Dimension(670, 30));
		sidePanel.add(scalePanel, BorderLayout.NORTH);

		GravitationHeaderLabel massLabel = new GravitationHeaderLabel("Mass \u2022 10 \u02E3");
		scalePanel.add(massLabel);

		massExponent = new MySpinner(0, -40, 100, 1);
		scalePanel.add(massExponent);

		GravitationHeaderLabel distanceLabel = new GravitationHeaderLabel("Distance \u2022 10 \u02E3 ");
		scalePanel.add(distanceLabel);

		distanceExponent = new MySpinner(0, -40, 100, 1);
		scalePanel.add(distanceExponent);

		ChangeListener changeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				repaintVectors();
			}
		};

		massExponent.addChangeListener(changeListener);
		distanceExponent.addChangeListener(changeListener);
	}

	public void shuffle(int i) {
		for (; i < list.size(); i++) {
			((Mass) list.get(i)).shuffle();
		}
	}
	
	public void paintGraphics(Graphics2D gg) {
		gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
	
		background(gg);
		masses(gg);
	}
	
	public void background(Graphics2D gg) {
		gg.setColor(Color.WHITE);
		gg.fillRect(0, 0, graphics.getWidth(), graphics.getHeight());
		
		gg.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
		gg.setColor(new Color(150, 150, 150));
		gg.drawLine(originX, 0, originX, height);
		gg.drawLine(0, originY, width, originY);
		
		gg.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
		
		for (double i = originX; i <= width; i += scale * convert) {
			gg.drawLine((int) Math.round(i), 0, (int) Math.round(i), height);
		}
		
		for (double i = originX; i >= 0; i -= scale * convert) {
			gg.drawLine((int) Math.round(i), 0, (int) Math.round(i), height);
		}
		
		for (double i = originY; i <= height; i += scale * convert) {
			gg.drawLine(0, (int) Math.round(i), width, (int) Math.round(i));
		}
		
		for (double i = originY; i >= 0; i -= scale * convert) {
			gg.drawLine(0, (int) Math.round(i), width, (int) Math.round(i));
		}
	}
	
	public void masses(Graphics2D gg) {
		for (int i = 0; i < list.size(); i++)  {
			Mass m = (Mass) list.get(i);
			gg.setColor(m.getColor());
			
			if (m.getVisible()) {
				if (m.hasMass()) {
					gg.fillOval((int) Math.round((originX + m.getX() * scale * convert) - (m.getMass() * scale * convert * 0.05)), (int) Math.round(((originY - m.getY() * scale * convert) - (m.getMass() * scale * convert * 0.05))), (int) Math.round(m.getMass() * scale * 0.1 * convert), (int) Math.round(m.getMass() * scale * 0.1 * convert));
				} else {
					int x = (int) Math.round((originX + m.getX() * scale * convert) - 5);
					int y = (int) Math.round((originY - m.getY() * scale * convert) - 5);
					
					gg.fillOval(x, y, 10, 10);
					gg.drawString("(Massless Point)", x, y - 10);
				}
			}
		}
	}
	
	public void addButton() {
		Mass m = new Mass(this);
		list.add(m);
		
		listPanel.add(m.getPanel());
		listPanel.revalidate();
		listPanel.repaint();
		
		graphics.repaint();
		repaintVectors();
	}
	
	public void mouseDragged(MouseEvent me) {
		originX -= mouseX - me.getX();
		originY -= mouseY - me.getY();
		
		mouseX = me.getX();
		mouseY = me.getY();
		
		graphics.repaint();
	}
	
	public void mousePressed(MouseEvent me) {
		mouseX = me.getX();
		mouseY = me.getY();
	}
	
	public void mouseWheelMoved(MouseWheelEvent mwe) {
		int rotation = mwe.getWheelRotation();
		boolean allowed = false;
		double increase;
		
		if (rotation < 0) {
			if (scale < 20) {
				allowed = true;
			}
		} else {
			if (scale > 0.04) {
				allowed = true;
			}
		}
		
		if (allowed) {
			increase = scale * -0.1 * mwe.getWheelRotation();
			scale += increase;
			
			if (mwe.getX() > originX) {
				originX -= (mwe.getX() - originX) / scale * increase;
			} else {
				originX += (originX - mwe.getX()) / scale * increase;
			}
			
			if (mwe.getY() > originY) {
				originY -= (mwe.getY() - originY) / scale * increase;
			} else {
				originY += (originY - mwe.getY()) / scale * increase;
			}
		
			graphics.repaint();
		}
	}
	
	public void repaintVectors() {
		for (int i = 0; i < list.size(); i++) {
			((Mass) list.get(i)).repaintVectorWindow();
		}
	}
	
	public void addToRepaintList(JComponent comp) {
		for (int i = 0; i < list.size(); i++) {
			((Mass) list.get(i)).addToRepaintList(comp);
		}
	}


	public int getMassExponent() {
		return (int) ((double) massExponent.getValue());
	}



	public int getDistanceExponent() {
		return (int) ((double) distanceExponent.getValue());
	}
	
	
	
	public void mouseMoved(MouseEvent me) {}
	public void mouseEntered(MouseEvent me) {}
	public void mouseExited(MouseEvent me) {}
	public void mouseClicked(MouseEvent me) {}
	public void mouseReleased(MouseEvent me) {}
}

class Mass {
	private int index;
	private JPanel panel;
	private ImageIcon visibleIcon, invisibleIcon, vectorIcon, closeIcon;
	private boolean isVisible = true;
	private ColorButton clr;
	private MySpinner mass, x, y;
	private JComponent vectorComponent;

	public Mass(Gravitation grav) {
		try {
			visibleIcon = new ImageIcon(ImageIO.read(Mass.class.getResourceAsStream("data/eye.png")).getScaledInstance(40, 40, Image.SCALE_SMOOTH));
			invisibleIcon = new ImageIcon(ImageIO.read(Mass.class.getResourceAsStream("data/eyent.png")).getScaledInstance(40, 40, Image.SCALE_SMOOTH));
			vectorIcon = new ImageIcon(ImageIO.read(Mass.class.getResourceAsStream("data/vectors.png")).getScaledInstance(40, 40, Image.SCALE_SMOOTH));
			closeIcon = new ImageIcon(ImageIO.read(Mass.class.getResourceAsStream("data/close.png")).getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		index = grav.listSize();
		
		panel = new JPanel();
		
		x = new MySpinner(0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1);
		x.setBorder(new TitledBorder("X Position"));
		
		y = new MySpinner(0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1);
		y.setBorder(new TitledBorder("Y Position"));
		
		mass = new MySpinner(10, 0, Double.POSITIVE_INFINITY, 1);
		mass.setBorder(new TitledBorder("Mass"));
		
		JButton visible = new JButton(visibleIcon);
		visible.setToolTipText("Visible");
		clr = new ColorButton(grav.getGraphicComponent());
		JButton vectors = new JButton(vectorIcon);
		vectors.setToolTipText("Show gravitational vectors");
		JButton remove = new JButton(closeIcon);
		remove.setToolTipText("Remove object");
		
		GroupLayout layout = new GroupLayout(panel);
		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addComponent(x, 150, 150, 150)
			.addComponent(y, 150, 150, 150)
			.addComponent(mass, 150, 150, 150)
			.addComponent(clr, 50, 50, 50)
			.addComponent(vectors, 50, 50, 50)
			.addComponent(visible, 50, 50, 50)
			.addComponent(remove, 50, 50, 50)
		);
		
		layout.setVerticalGroup(layout.createParallelGroup()
			.addComponent(x, 50, 50, 50)
			.addComponent(y, 50, 50, 50)
			.addComponent(mass, 50, 50, 50)
			.addComponent(clr, 50, 50, 50)
			.addComponent(vectors, 50, 50, 50)
			.addComponent(visible, 50, 50, 50)
			.addComponent(remove, 50, 50, 50)
		);
		
		panel.setLayout(layout);
		
		visible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (isVisible) {
					visible.setIcon(invisibleIcon);
				} else {
					visible.setIcon(visibleIcon);
				}
			
				isVisible = !isVisible;
			}
		});
		
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				grav.removeList(index, 0);
			}
		});
		
		grav.addChangeRefresh(x);
		grav.addChangeRefresh(y);
		grav.addChangeRefresh(mass);
		grav.addActionRefresh(visible);
		grav.addActionRefresh(remove);
		
		ChangeListener changeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				grav.repaintVectors();
			}
		};
		
		x.addChangeListener(changeListener);
		y.addChangeListener(changeListener);
		mass.addChangeListener(changeListener);
		
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				grav.repaintVectors();
			}
		};
		
		remove.addActionListener(actionListener);
		visible.addActionListener(actionListener);
		
		JFrame vectorFrame = new JFrame("Gravitation");
		vectorFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		vectorFrame.setLayout(new BorderLayout());
		vectorFrame.setResizable(false);
		try {
			vectorFrame.setIconImage(ImageIO.read(Gravitation.class.getResourceAsStream("data/icon.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		JMenuBar menuBar = new JMenuBar();
		vectorFrame.setJMenuBar(menuBar);

		JMenu menu = new JMenu("Menu");
		menuBar.add(menu);

		JCheckBoxMenuItem onTop = new JCheckBoxMenuItem("Always visible");
		menu.add(onTop);

		onTop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				vectorFrame.setAlwaysOnTop(onTop.isSelected());
			}
		});
		
		JPanel resultantPanel = new JPanel();
		resultantPanel.setLayout(new GridLayout(1, 2));
		resultantPanel.setBorder(new TitledBorder("Resultant"));
		vectorFrame.add(resultantPanel, BorderLayout.NORTH);
		
		JTextField resultantDirection = new JTextField();
		resultantDirection.setEditable(false);
		resultantPanel.add(resultantDirection);
		
		JTextField resultantMagnitude = new JTextField();
		resultantMagnitude.setEditable(false);
		resultantPanel.add(resultantMagnitude);
		
		vectorComponent = new JComponent() {
			public void paintComponent(Graphics g) {
				Graphics2D gg = (Graphics2D) g;
				
				gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
									RenderingHints.VALUE_ANTIALIAS_ON);
				
				background(gg);
				updateText(vectorGraphics(gg));
				dot(gg);
			}
			
			public void background(Graphics2D gg) {
				gg.setColor(Color.WHITE);
				gg.fillRect(0, 0, this.getWidth(), this.getHeight());
			}
			
			public GravVector vectorGraphics(Graphics2D gg) {
				double G = 0.0000000000667408;
				Mass m;
				double relativeX, relativeY, radius, force, theta, forceX, forceY, maxX = 0, maxY = 0;
				double localX = (double) x.getValue(), localY = (double) y.getValue(), localMass = (double) mass.getValue(), otherMass;
				double totalX = 0, totalY = 0;
				ArrayList<GravVector> vecList = new ArrayList<GravVector>();
				double scale;

				if (localMass == 0) {
					localMass = 1;
				} else {
					localMass *= Math.pow(10, grav.getMassExponent());
				}
				
				for (int i = 0; i < grav.getList().size(); i++) {
					m = (Mass) grav.getList().get(i);
					
					if (i != index && m.hasMass() && m.getVisible()) {
						relativeX = m.getX() - localX;
						relativeY = m.getY() - localY;
						
						if (!(relativeX == 0 && relativeY == 0)) {
							radius = Math.sqrt(relativeX * relativeX + relativeY * relativeY)
									* Math.pow(10, grav.getDistanceExponent());
							force = (G * localMass * m.getMass()
									* Math.pow(10, grav.getMassExponent()))
									/ (radius * radius);
							
							theta = Math.atan(relativeY / relativeX);
							if (relativeX < 0) {
								theta += Math.PI;
							}
							
							forceX = force * Math.cos(theta);
							forceY = force * Math.sin(theta);
							
							totalX += forceX;
							totalY += forceY;
							
							vecList.add(new GravVector(forceX, forceY, theta, m.getColor(), force, false));
							
							maxX = Math.max(maxX, Math.abs(forceX));
							maxY = Math.max(maxY, Math.abs(forceY));
						}
					}
				}
				
				maxX = Math.max(maxX, Math.abs(totalX));
				maxY = Math.max(maxY, Math.abs(totalY));
				
				scale = (maxX > maxY) ? (230.0 / maxX) : (230.0 / maxY);
				
				for (int i = 0; i < vecList.size(); i++) {
					drawArrow(gg, vecList.get(i).getX(), vecList.get(i).getY(), vecList.get(i).getColor(), scale, vecList.get(i).getDirection());
				}
				
				theta = Math.atan(totalY / totalX);
				if (totalX < 0) {
					theta += Math.PI;
				} else if (totalY < 0) {
					theta += Math.PI * 2;
				}
				
				if (!Double.isNaN(totalX) && !Double.isNaN(totalY)) {
					drawArrow(gg, totalX, totalY, Color.RED, scale, theta);
				}
				
				return new GravVector(totalX, totalY, Math.atan(totalY / totalX), Color.BLACK, Math.sqrt(totalX * totalX + totalY * totalY), (double) mass.getValue() == 0);
			}
			
			public void drawArrow(Graphics2D gg, double x, double y, Color c, double scale, double direction) {
				if (!Double.isNaN(x) && !Double.isNaN(y) && !(x == 0 && y == 0)) {
					int endX = (int) Math.round(250 + x * scale);
					int endY = (int) Math.round(250 - y * scale);
				
					gg.setColor(c);
					gg.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
					gg.drawLine(250, 250, endX, endY);
					
					direction += Math.PI * 1.25;
					gg.drawLine(endX, endY, (int) Math.round(endX + 10 * Math.cos(direction)), (int) Math.round(endY - 10 * Math.sin(direction)));
					
					direction -= Math.PI * 0.5;
					gg.drawLine(endX, endY, (int) Math.round(endX + 10 * Math.cos(direction)), (int) Math.round(endY - 10 * Math.sin(direction)));
				}
			}
			
			public void dot(Graphics2D gg) {
				gg.setColor(Color.BLACK);
				gg.fillOval(245, 245, 10, 10);
			}
			
			public void updateText(GravVector v) {
				double degrees = Math.toDegrees(v.getDirection());
				String unit = v.hasMass() ? "N" : "N/kg";
				
				if (Math.abs(degrees) < 0.0001) {
					degrees = 0;
				}
				
				if (v.getX() < 0) {
					degrees += 180;
				} else if (v.getY() < 0) {
					degrees += 360;
				}
			
				resultantDirection.setText(degrees + "\u00B0");
				resultantMagnitude.setText(v.getMagnitude() + unit);
			}
		};
		vectorComponent.setPreferredSize(new Dimension(500, 500));
		vectorFrame.add(vectorComponent);
		vectorFrame.pack();
		vectorFrame.setLocationRelativeTo(null);
		
		vectors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				vectorFrame.setVisible(true);
			}
		});
		
		ArrayList<Object> massList = grav.getList();
		
		for (int i = 0; i < massList.size(); i++) {
			clr.addComponent(((Mass) massList.get(i)).getVectorComponent());
		}
		
		grav.addToRepaintList(vectorComponent);
	}
	
	public void shuffle() {
		index--;
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public Color getColor() {
		return clr.getColor();
	}
	
	public double getMass() {
		return (double) mass.getValue();
	}
	
	public double getX() {
		return (double) x.getValue();
	}
	
	public double getY() {
		return (double) y.getValue();
	}
	
	public boolean getVisible() {
		return isVisible;
	}
	
	public boolean hasMass() {
		return (double) mass.getValue() > 0;
	}
	
	public void repaintVectorWindow() {
		vectorComponent.repaint();
	}
	
	public void addToRepaintList(JComponent comp) {
		clr.addComponent(comp);
	}
	
	public JComponent getVectorComponent() {
		return vectorComponent;
	}
}



class GravVector {
	private double x, y, theta, force;
	private Color color;
	private boolean isMassless;
	
	public GravVector(double inX, double inY, double inTheta, Color inColor, double inForce, boolean inIsMassless) {
		x = inX;
		y = inY;
		theta = inTheta;
		color = inColor;
		force = inForce;
		isMassless = inIsMassless;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getDirection() {
		return theta;
	}
	
	public Color getColor() {
		return color;
	}
	
	public double getMagnitude() {
		return force;
	}
	
	public boolean hasMass() {
		return !isMassless;
	}
}
