package com.dkhalife.projects;

import java.lang.reflect.InvocationTargetException;

/**
 * This class encapsulates all the ingame levels
 * 
 * @author Dany Khalife
 * 
 */
public abstract class LevelLibrary {
	// The library will contain the list of the level data
	private static Library library = new Library();

	// We also need to keep track of the current difficulty (or size) and the
	// current level for that difficulty
	private static int currentDifficulty;
	private static int currentLevel;

	/**
	 * 
	 * This method loads a specific level
	 * 
	 * @param difficulty The difficulty of the level which is also the size of
	 * the grid
	 * @param level The level number for the current difficulty
	 * 
	 * @return True if the level was found and loaded, False otherwise
	 * 
	 */
	public static boolean loadLevel(int difficulty, int level) {
		try {
			currentDifficulty = difficulty;
			currentLevel = level;

			Grid.setSize(difficulty);
			ColorFactory.resetColorPalette();

			library.getClass().getMethod("Group" + difficulty + "Level" + level).invoke(library);

			Main.getLevel().repaint();

			return true;
		} catch (IllegalArgumentException | NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
			return false;
		}
	}

	/**
	 * 
	 * This methods advances the current level In case the current difficulty
	 * has no more levels, the difficulty is increased until no more levels can
	 * be found
	 * 
	 */
	public static void nextLevel() {
		// Try next level
		if (!loadLevel(currentDifficulty, ++currentLevel)) {
			if (!loadLevel(++currentDifficulty, 1)) {
				Main.alert("You completed the game!");
			}
		}
	}

	/**
	 * 
	 * The library class is nothing but a series of methods for each difficulty
	 * Each method contains the list of starting positions for each flow
	 * 
	 * @author Dany
	 * 
	 */
	static class Library {
		public void Group5Level1() {
			Grid.addFlow(Grid.getCell(1, 1), Grid.getCell(2, 2));
		}

		public void Group5Level2() {
			Grid.addFlow(Grid.getCell(1, 1), Grid.getCell(2, 2));
			Grid.addFlow(Grid.getCell(4, 4), Grid.getCell(3, 4));
		}

		public void Group6Level1() {
			Grid.addFlow(Grid.getCell(1, 1), Grid.getCell(2, 1));
			Grid.addFlow(Grid.getCell(3, 4), Grid.getCell(3, 5));
			Grid.addFlow(Grid.getCell(5, 5), Grid.getCell(4, 5));
		}
	}
}