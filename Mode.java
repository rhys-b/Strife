import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Graphics;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.ArrayList;



abstract public class Mode extends JPanel {
	protected JPanel listPanel, sidePanel;
	protected JComponent graphics;
	protected ArrayList<Object> list;
	protected int height, width, hx, hy;
	
	private ChangeListener changeListener = new ChangeListener() {
		public void stateChanged(ChangeEvent ce) {
			graphics.repaint();
		}
	};
	
	private ActionListener actionListener = new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			graphics.repaint();
		}
	};
	
	public Mode () {
		this.setLayout(new BorderLayout());
	
		sidePanel = new JPanel();
		sidePanel.setLayout(new BorderLayout());
		this.add(sidePanel, BorderLayout.EAST);
		
		listPanel = new JPanel();
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
		
		JScrollPane sideScroll = new JScrollPane(listPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sideScroll.getVerticalScrollBar().setUnitIncrement(8);
		sidePanel.add(sideScroll);
		
		JButton add = new JButton("Add");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				addButton();
			}
		});
		sidePanel.add(add, BorderLayout.SOUTH);
		
		graphics = new JComponent() {
			public void paintComponent(Graphics g) {
				height = graphics.getHeight();
				width = graphics.getWidth();
				hx = (int) Math.round(width / 2.0);
				hy = (int) Math.round(height / 2.0);
				
				paintGraphics((Graphics2D) g);
			}
		};
		this.add(graphics);
		
		list = new ArrayList<Object>();
	}
	
	public JPanel getListPanel() {
		return listPanel;
	}
	
	public void repaintGraphics() {
		graphics.repaint();
	}
	
	public ArrayList<Object> getList() {
		return list;
	}
	
	public void addChangeRefresh(JSpinner comp) {
		comp.addChangeListener(changeListener);
	}
	
	public void addActionRefresh(JButton comp) {
		comp.addActionListener(actionListener);
	}
	
	public int listSize() {
		return list.size();
	}
	
	public void removeList(int i, int offset) {
		listPanel.remove(i + offset);
		list.remove(i);
		
		listPanel.revalidate();
		listPanel.repaint();
		
		shuffle(i);
	}
	
	public JComponent getGraphicComponent() {
		return graphics;
	}
	
	abstract public void addButton();
	abstract public void paintGraphics(Graphics2D g);
	abstract public void shuffle(int i);
}
