package com.dkhalife.projects;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class is the ultimate Color Factory that creates unique colors
 * 
 * @author Dany Khalife
 * 
 */
public class ColorFactory {
	// The singleton instance
	private static ColorFactory instance = null;

	// The colors that we haven't used
	private static Queue<Color> palette = new LinkedList<>();

	/**
	 * 
	 * The ColorFactory is a singleton so we need a global access point for the
	 * unique instance
	 * 
	 * @return The ColorFactory instance
	 */
	public static ColorFactory getInstance() {
		if (instance == null)
			instance = new ColorFactory();

		return instance;
	}

	/**
	 * 
	 * Once the Factory is created, we need to reset the color palette
	 * 
	 */
	private ColorFactory() {
		resetColorPalette();
	}

	/**
	 * 
	 * This method resets the color palette
	 * 
	 */
	public static void resetColorPalette() {
		// First we'll empty our queue
		palette.clear();

		// And then we'll re add all the supported colors
		palette.add(Color.RED);
		palette.add(Color.BLUE);
		palette.add(Color.GREEN);
		palette.add(Color.CYAN);
		palette.add(Color.ORANGE);
		palette.add(Color.YELLOW);
		palette.add(Color.MAGENTA);
		palette.add(Color.GRAY);
		palette.add(Color.BLACK);
		palette.add(Color.WHITE);
	}

	/**
	 * 
	 * This method is the generator for the unique colors
	 * 
	 * @return
	 */
	public Color getColor() {
		return palette.poll();
	}
}
