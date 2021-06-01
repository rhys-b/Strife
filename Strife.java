public class Strife {
	private static GUI gui;

	public static void main(String[] args) {
		try {
			javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		gui = new GUI();
	}
}
