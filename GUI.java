import javax.swing.UIManager;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

import javax.imageio.ImageIO;

import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



public class GUI {
	JFrame window;
	JTabbedPane tabs;

	public GUI() {
		window = new JFrame("Strife");
		initializeWindow();
		addModes();
		window.setVisible(true);
	}
	
	private void addModes() {
		tabs.add(new Conversions(), "Dynamics");
		tabs.add(new TDVectors(), "2D Vectors");
		tabs.add(new Gravitation(), "Gravitation");
		tabs.add(new Energy(), "Energy");
	}
	
	private void initializeWindow() {
		window.setSize(1300, 700);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			window.setIconImage(ImageIO.read(GUI.class.getResourceAsStream("data/icon.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		tabs = new JTabbedPane();
		window.add(tabs);
		
		JMenuBar menuBar = new JMenuBar();
		window.setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("Menu");
		menuBar.add(menu);
		
		JMenuItem help = new JMenuItem("Help");
		help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
			InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
		menu.add(help);

		JMenuItem changelog = new JMenuItem("What's new?");
		menu.add(changelog);

		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				HtmlFrame.show("data/help.html", window);
			}
		});

		changelog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				HtmlFrame.show("data/changelog.html", window);
			}
		});
	}
}