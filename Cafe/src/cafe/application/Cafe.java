package cafe.application;

import cafe.gui.MainWindow;

public class Cafe {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.setProperty("sun.java2d.d3d",        "false");
			System.setProperty("sun.java2d.ddoffscreen","false");
			System.setProperty("sun.java2d.noddraw",    "true");
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
