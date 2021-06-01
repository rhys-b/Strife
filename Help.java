import javax.swing.JFrame;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import javax.imageio.ImageIO;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.Scanner;



public class Help implements ActionListener {
	JFrame window;
	
	public Help(JFrame relative) {
		window = new JFrame("Help");
		window.setSize(500, 500);
		window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		window.setLocationRelativeTo(relative);
		try {
			window.setIconImage(ImageIO.read(Help.class.getResourceAsStream("data/icon.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JEditorPane editor = new JEditorPane();
		editor.setContentType("text/html");
		editor.setText(getHelpFileText());
		editor.setEditable(false);
		
		JScrollPane scroller = new JScrollPane(editor, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.getVerticalScrollBar().setUnitIncrement(8);
		window.add(scroller);
	}

	public void actionPerformed(ActionEvent ae) {
		window.setVisible(true);
	}
	
	public String getHelpFileText() {
		Scanner scanner = new Scanner(Help.class.getResourceAsStream("data/help.html"));
		String string = "";
		
		while (scanner.hasNextLine()) {
			string += scanner.nextLine();
		}
		
		return string;
	}
}
