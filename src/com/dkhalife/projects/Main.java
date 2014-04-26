package com.dkhalife.projects;

/**
 * 
 * @author Dany Khalife
 * @version 1.0
 * @since December 09, 2012
 * 
 */

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * 
 * This class is responsible for creating the Game UI
 * 
 * @author Dany Khalife
 * @since 26 December 2012
 * 
 */
public class Main {
	// The window
	private static JFrame window;
	// The current level
	private static Level l;
	
	/**
	 * 
	 * The maze grid needs to know the size of the maze to be created. The size
	 * of the grid is provided via 2 user inputs.
	 * 
	 */
	public static void main(String[] args) {
		// Create the game window
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	/**
	 * 
	 * This method will create and show the window for the Maze Solver
	 * 
	 * @author Dany Khalife
	 * @param w The width of the maze
	 * @param h The height of the maze
	 * 
	 */
	private static void createAndShowGUI() {
		// Lets load the image factory
		ImageFactory.getInstance();
		
		// First, lets create a window
		window = new JFrame("Flow Free");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		// We don't want the window to be resizable, at least not for now
		window.setResizable(false);

		l = new Level();
		LevelLibrary.loadLevel(5, 1);

		// Append the panel and show the GUI
		window.add(l);
		window.pack();
	}
	
	/**
	 * Getter for the level
	 * @return The current level
	 */
	public static Level getLevel(){
		return l;
	}  
	
	/**
	 * 
	 * A JavaScript-like alert box using Swing's corresponding function
	 * 
	 * @param msg The message to display
	 * 
	 */
	protected static void alert(String msg) {
		JOptionPane.showMessageDialog(Main.window, msg);
	}
}
