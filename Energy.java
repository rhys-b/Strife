import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Font;
import java.awt.RenderingHints;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.ArrayList;



public class Energy extends JPanel implements MouseMotionListener, MouseListener {
	private JPanel sidePanel;
	private JComponent graphicsComponent;
	private boolean mouseOnStart = false, draggingFromStart = false;
	private ArrayList<Point> list;
	private double scale = 1, potentialEnergy = 0;
	private JLabel potentialLabel, kineticLabel, speedLabel;
	private boolean running = false, paused = false;
	private int x1, x2, y1, y2;
	private JButton start, reset, stop;
	private MySpinner frictionSpinner;
	private JSlider scaleSlider;

	public Energy() {
		this.setLayout(new BorderLayout());
		resetList();
		
		makeGraphics();
		graphicsComponent.addMouseMotionListener(this);
		graphicsComponent.addMouseListener(this);
		this.add(graphicsComponent);
		
		createSidePanel();
		this.add(sidePanel, BorderLayout.EAST);
	}
	
	public void makeGraphics() {
		graphicsComponent = new JComponent() {
			private Graphics2D gg;
		
			public void paintComponent(Graphics g) {
				gg = (Graphics2D) g;
				gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
									RenderingHints.VALUE_ANTIALIAS_ON);
				
				background();
				heightMarks();
				startPlatform();
				lines();
				
				if (running) {
					cart();
				}
			}
			
			public void background() {
				gg.setColor(Color.WHITE);
				gg.fillRect(0, 0, this.getWidth(), this.getHeight());
			}
			
			public void heightMarks() {
				int textWidth;
				String string;
				
				gg.setFont(gg.getFont().deriveFont(20f));
				FontMetrics metrics = gg.getFontMetrics();
			
				gg.setColor(new Color(200, 200, 200));
				gg.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
				
				gg.drawLine(0, 50, this.getWidth(), 50);
				for (int i = 150; i < this.getHeight(); i += 100) {
					string = roundTenth(((i - 50) / 100) * scale) + "m";
					textWidth = metrics.stringWidth(string);
					gg.drawLine(textWidth + 10, i, this.getWidth(), i);
					gg.drawString(string, 5, (int) Math.round(i + metrics.getHeight() * 0.5 - 5));
				}
			}
			
			public void startPlatform() {
				gg.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
				gg.drawLine(60, 50, 60, 35);
				
				gg.setColor(Color.GREEN);
				gg.fillRect(58, 25, 15, 10);
				
				if (mouseOnStart) {
					gg.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
					gg.setColor(new Color(255, 220, 5));
					gg.drawOval(95, 75, 10, 10);
					gg.drawOval(90, 70, 20, 20);
				}
			}
			
			public void lines() {
				Point pa, pb = list.get(0);
			
				gg.setColor(Color.BLACK);
				gg.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
			
				for (int i = 1; i < list.size(); i++) {
					pa = pb;
					pb = list.get(i);
					gg.drawLine(pa.x, pa.y, pb.x, pb.y);
				}
			}
			
			public void cart() {
				gg.setColor(Color.BLUE);
				gg.setStroke(new BasicStroke(20, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
				gg.drawLine(x1, y1, x2, y2);
			}
		};
	}
	
	public void mouseMoved(MouseEvent me) {
		if (!running) {
			if (new Point(100, 80).distance(me.getX(), me.getY()) <= 10) {
				mouseOnStart = true;
				graphicsComponent.repaint();
			} else if (mouseOnStart) {
				mouseOnStart = false;
				graphicsComponent.repaint();
			}
		}
	}
	
	public void mouseDragged(MouseEvent me) {
		if (mouseOnStart && !running) {
			draggingFromStart = true;
			mouseOnStart = false;
			resetList();
		}
		
		if (draggingFromStart) {
			list.add(new Point(me.getX(), me.getY()));
			graphicsComponent.repaint();
		}
	}
	
	public void mousePressed(MouseEvent me) {
		if (draggingFromStart && !running) {
			list.add(new Point(me.getX(), me.getY()));
			graphicsComponent.repaint();
		}
	}
	
	public void createSidePanel() {
		sidePanel = new JPanel();
		sidePanel.setLayout(null);
		sidePanel.setPreferredSize(new Dimension(200, 1));
		
		start = new JButton("Start");
		start.setBounds(5, 5, 190, 30);
		sidePanel.add(start);
		
		reset = new JButton("Reset");
		reset.setBounds(100, 35, 95, 30);
		sidePanel.add(reset);
		
		stop = new JButton("Stop");
		stop.setBounds(5, 35, 95, 30);
		sidePanel.add(stop);
		
		JLabel scaleLabel = new JLabel("Scale");
		scaleLabel.setBounds(5, 80, 60, 40);
		sidePanel.add(scaleLabel);
		
		scaleSlider = new JSlider(1, 28, 10);
		scaleSlider.setBounds(70, 80, 125, 40);
		scaleSlider.setMinorTickSpacing(1);
		scaleSlider.setPaintTicks(true);
		sidePanel.add(scaleSlider);
		
		JLabel frictionLabel = new JLabel("Friction");
		frictionLabel.setBounds(5, 140, 95, 25);
		sidePanel.add(frictionLabel);
		
		frictionSpinner = new MySpinner(0, 0, 2, 0.01);
		frictionSpinner.setBounds(100, 140, 95, 25);
		((MySpinner.NumberEditor) frictionSpinner.getEditor()).getFormat()
			.setMaximumFractionDigits(3);
		sidePanel.add(frictionSpinner);
		
		JLabel note = new JLabel("Assume 1kg mass");
		note.setFont(note.getFont().deriveFont(Font.ITALIC));
		note.setHorizontalAlignment(SwingConstants.CENTER);
		note.setBounds(5, 180, 190, 25);
		sidePanel.add(note);
		
		JLabel speedLabel2 = new JLabel("Speed");
		speedLabel2.setBounds(5, 210, 75, 25);
		sidePanel.add(speedLabel2);
		
		speedLabel = new JLabel("0.000m/s");
		speedLabel.setBounds(90, 210, 105, 25);
		speedLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		sidePanel.add(speedLabel);
		
		JLabel potentialLabel2 = new JLabel("Potential");
		potentialLabel2.setBounds(5, 240, 75, 25);
		sidePanel.add(potentialLabel2);
		
		calculateStartingPotential();
		potentialLabel = new JLabel("0.000J");
		potentialLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		potentialLabel.setBounds(90, 240, 105, 25);
		sidePanel.add(potentialLabel);
		
		JLabel kineticLabel2 = new JLabel("Kinetic");
		kineticLabel2.setBounds(5, 275, 75, 25);
		sidePanel.add(kineticLabel2);
		
		kineticLabel = new JLabel("0.000J");
		kineticLabel.setBounds(90, 275, 105, 25);
		kineticLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		sidePanel.add(kineticLabel);
		
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				resetList();
				draggingFromStart = false;
				graphicsComponent.repaint();
				resetLabels();
			}
		});
		
		scaleSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				if (!running) {
					refreshScale();
				}
			}
		});
		
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (running) {
					if (paused) {
						paused = false;
						start.setText("Pause");
					} else {
						paused = true;
						start.setText("Start");
					}
				} else {
					setRunning(true);
				}
			}
		});
		
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				setRunning(false);
			}
		});
	}
	
	public String roundTenth(double d) {
		return String.format("%.1f", d);
	}
	
	public String roundThousanth(double d) {
		return String.format("%.3f", d);
	}
	
	public void simulate() {
		new Thread(new Runnable() {
			private int[] next = new int[2];
		
			public void run() {
				DoublePoint p1 = null, p2 = null;
				double speed = 0, angle, mu = (double) frictionSpinner.getValue(), fOfG, fOfF;
				double dblX1, dblX2, dblY1, dblY2;
				double ag = 0.2725 / scale;
				
				next[0] = 1;
				next[1] = 1;
				
				dblX2 = x2 = 68;
				dblY2 = y2 = 56;
				
				dblX1 = x1 = 100;
				dblY1 = y1 = 80;
				
				while (running) {
					if (!paused) {
						graphicsComponent.repaint();
						
						angle = Math.atan((dblY2 - dblY1) / (dblX2 - dblX1));
						fOfG = ag * Math.sin(angle);
						fOfF = ag * mu * Math.cos(angle);
						if (speed < 0) {
							fOfF *= -1;
						}
						
						if (dblX1 > dblX2) {
							speed += fOfG - fOfF;
						} else {
							speed -= fOfG + fOfF;
						}
						
						if (speed >= 0) {
							p1 = nextPosition(dblX1, dblY1, 0, speed);
						} else if (speed < 0) {
							p1 = lastPosition(dblX1, dblY1, 0, speed * -1);
						}
						
						if (speed >= 0) {
							p2 = nextPosition(dblX2, dblY2, 1, speed);
						} else if (speed < 0) {
							p2 = lastPosition(dblX2, dblY2, 1, speed * -1);
						}
						
						if (p2 != null && p1 != null) {
							dblX1 = p1.x;
							dblY1 = p1.y;
							
							dblX2 = p2.x;
							dblY2 = p2.y;
						} else if (speed < 0) {
							speed = 0;
						}
					
						x1 = (int) Math.round(dblX1);
						x2 = (int) Math.round(dblX2);
						y1 = (int) Math.round(dblY1);
						y2 = (int) Math.round(dblY2);
						
						double realSpeed = calculateSpeed(speed);
						
						potentialLabel.setText(roundThousanth(calculatePotential((dblY1 + dblY2) / 2.0)) + "J");
						kineticLabel.setText(roundThousanth(calculateKinetic(realSpeed)) + "J");
						speedLabel.setText(roundThousanth(realSpeed) + "m/s");
					}
					
					try {
						Thread.sleep(17);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			public DoublePoint nextPosition(double x, double y, int index, double speed) {
				double diffX, diffY, angle;
			
				DoublePoint tail = new DoublePoint(x, y);
				DoublePoint head = new DoublePoint(list.get(next[index]));
				
				while (tail.distance(head) < speed) {
					next[index]++;
					speed -= tail.distance(head);
					
					tail = head;
					
					if (list.size() > next[index]) {
						head = new DoublePoint(list.get(next[index]));
					} else {
						setRunning(false);
						return null;
					}
				}
				
				diffX = head.x - tail.x;
				diffY = head.y - tail.y;
				angle = Math.atan(1.0 * diffY / diffX);
				if (diffX < 0) {
					angle += Math.PI;
				} else if (diffY < 0) {
					angle += Math.PI * 2;
				}
				
				return new DoublePoint(speed * Math.cos(angle) + tail.x, speed * Math.sin(angle) + tail.y);
			}
			
			public DoublePoint lastPosition(double x, double y, int index, double speed) {
				double diffX, diffY, angle;
			
				DoublePoint tail = new DoublePoint(x, y), head;
				
				if (next[index] - 1 >= 0) {
					head = new DoublePoint(list.get(next[index] - 1));
				} else {
					return null;
				}
				
				while (tail.distance(head) < speed) {
					next[index]--;
					speed -= tail.distance(head);
					
					tail = head;
					
					if (next[index] - 1 >= 0) {
						head = new DoublePoint(list.get(next[index] - 1));
					} else {
						return null;
					}
				}
				
				diffX = head.x - tail.x;
				diffY = head.y - tail.y;
				angle = Math.atan(1.0 * diffY / diffX);
				if (diffX < 0) {
					angle += Math.PI;
				} else if (diffY < 0) {
					angle += Math.PI * 2;
				}
				
				return new DoublePoint(speed * Math.cos(angle) + tail.x, speed * Math.sin(angle) + tail.y);
			}
		}).start();
	}
	
	public void resetList() {
		list = new ArrayList<Point>();
		list.add(new Point(60, 50));
		list.add(new Point(100, 80));
	}
	
	public void setRunning(boolean b) {
		running = b;
		
		if (running) {
			start.setText("Pause");
			reset.setEnabled(false);
			calculateStartingPotential();
			simulate();
		} else {
			start.setText("Start");
			refreshScale();
			graphicsComponent.repaint();
			reset.setEnabled(true);
			paused = false;
		}
	}
	
	public void refreshScale() {
		int value = scaleSlider.getValue();
	
		if (value <= 10) {
			scale = value / 10.0;
		} else if (value <= 19) {
			scale = value - 9;
		} else {
			scale = ((value - 19) * 10) + 10;
		}
		
		graphicsComponent.repaint();
	}
	
	public void calculateStartingPotential() {
		int max = 0;
		
		for (int i = 0; i < list.size(); i++) {
			max = Math.max(max, list.get(i).y);
		}
		
		potentialEnergy = (max - 50) / 100.0 * scale * 9.81;
	}
	
	public double calculatePotential(double height) {
		return potentialEnergy - ((height - 50) / 100.0 * scale * 9.81);
	}
	
	public double calculateKinetic(double speed) {
		return 0.5 * speed * speed;
	}
	
	public double calculateSpeed(double s) {
		return Math.abs(s / 100.0 * scale * 60);
	}
	
	public void resetLabels() {
		speedLabel.setText("0.000m/s");
		kineticLabel.setText("0.000J");
		potentialLabel.setText("0.000J");
	}
	
	
	public void mouseReleased(MouseEvent me) {}
	public void mouseExited(MouseEvent me) {}
	public void mouseClicked(MouseEvent me) {}
	public void mouseEntered(MouseEvent me) {}
}

class DoublePoint {
	public double x, y;
	
	public DoublePoint(double inX, double inY) {
		x = inX;
		y = inY;
	}
	
	public DoublePoint(Point p) {
		x = p.x;
		y = p.y;
	}
	
	public double distance(DoublePoint p) {
		double diffX = p.x - x;
		double diffY = p.y - y;
	
		return Math.sqrt(diffX * diffX + diffY * diffY);
	}
}
