import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import java.awt.image.BufferedImage;

import java.util.ArrayList;



public class ColorButton extends JButton implements ActionListener, ComponentListener {
	private Color color;
	private ArrayList<JComponent> comps = new ArrayList<JComponent>();

	public ColorButton(JComponent inComp) {
		color = Color.BLACK;
		init(inComp);
	}
	
	public ColorButton(JComponent inComp, Color c) {
		color = c;
		init(inComp);
	}
	
	public void init(JComponent inComp) {
		this.addActionListener(this);
		this.addComponentListener(this);
		comps.add(inComp);
		this.setToolTipText("Colour");
	}
	
	public void actionPerformed(ActionEvent ae) {
		Color c = JColorChooser.showDialog(this, "Pick a Colour", color);
		
		if (c != null) {
			iconizer(c);
			repaintComponents();
		}
	}
	
	private void iconizer(Color c) {
		BufferedImage b = new BufferedImage(this.getWidth() - 10, this.getHeight() - 10, BufferedImage.TYPE_INT_RGB);
		Graphics g = b.createGraphics();
		
		g.setColor(c);
		g.fillRect(0, 0, this.getWidth() - 10, this.getHeight() - 10);
		
		this.setIcon(new ImageIcon(b));
		color = c;
	}
	
	public void componentResized(ComponentEvent ce) {
		iconizer(color);
	}
	
	public Color getColor() {
		return color;
	}
	
	public void addComponent(JComponent comp) {
		comps.add(comp);
	}
	
	public void repaintComponents() {
		for (int i = 0; i < comps.size(); i++) {
			comps.get(i).repaint();
		}
	}
	
	public void componentHidden(ComponentEvent ce) {}
	public void componentMoved(ComponentEvent ce) {}
	public void componentShown(ComponentEvent ce) {}
}
