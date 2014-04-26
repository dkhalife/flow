package com.dkhalife.projects;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * This class is the ultimate Image Factory that loads all the Flow Graphics
 * 
 * @author Dany Khalife
 * 
 */
public class ImageFactory {
	// This class is a singleton so we need to keep track of its unique instance
	private static ImageFactory instance = null;

	// We'll also keep all sprites for caching
	private Image red, green, blue, yellow, cyan, magenta, white, gray, black, orange;

	/**
	 * 
	 * Since this class is a singleton we need to provide a global access point
	 * for the unique instance
	 * 
	 * @return The singleton instance
	 * 
	 */
	public static ImageFactory getInstance() {
		if (instance == null)
			instance = new ImageFactory();

		return instance;
	}

	/**
	 * 
	 * Once the factory is constructed we'll need to load all the graphics into
	 * the cache
	 * 
	 */
	private ImageFactory() {
		red = Toolkit.getDefaultToolkit().getImage("res/images/red.png");
		green = Toolkit.getDefaultToolkit().getImage("res/images/green.png");
		blue = Toolkit.getDefaultToolkit().getImage("res/images/blue.png");
		yellow = Toolkit.getDefaultToolkit().getImage("res/images/yellow.png");
		cyan = Toolkit.getDefaultToolkit().getImage("res/images/cyan.png");
		magenta = Toolkit.getDefaultToolkit().getImage("res/images/magenta.png");
		white = Toolkit.getDefaultToolkit().getImage("res/images/white.png");
		gray = Toolkit.getDefaultToolkit().getImage("res/images/gray.png");
		black = Toolkit.getDefaultToolkit().getImage("res/images/black.png");
		orange = Toolkit.getDefaultToolkit().getImage("res/images/orange.png");
	}

	/**
	 * 
	 * This method returns the appropriate image according to the color provided
	 * 
	 * @param c The color of the flow
	 * @return The corresponding image for that color
	 * 
	 */
	public Image get(Color c) {
		if (c == Color.RED)
			return red;

		if (c == Color.GREEN)
			return green;

		if (c == Color.BLUE)
			return blue;

		if (c == Color.YELLOW)
			return yellow;

		if (c == Color.CYAN)
			return cyan;

		if (c == Color.MAGENTA)
			return magenta;

		if (c == Color.GRAY)
			return gray;

		if (c == Color.ORANGE)
			return orange;

		if (c == Color.WHITE)
			return white;

		if (c == Color.BLACK)
			return black;

		return null;

	}
}
