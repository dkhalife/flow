package com.dkhalife.projects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

/**
 * This class represents the grid of the level
 * 
 * @author Dany Khalife
 * 
 */
public abstract class Grid {
	// We need to keep track of the size of the grid in cells and in grids
	private static int size;
	private static int pSize = 500;

	// We also need to keep track of the cell's dimensions
	private static int res = 0;

	// The grid consists of a 2D matrix of cells
	private static Vector<Vector<Cell>> grid;

	// We also keep track of all the flows on the grid
	private static Vector<Flow> flows = new Vector<>();

	// And we also need a flag to see if the user had a previous unsuccessfil
	// finish
	private static boolean unsuccessfulFinish = false;

	/**
	 * 
	 * Getter for the size of the grid (in cells)
	 * 
	 * @return The size of the grid (in cells)
	 * 
	 */
	public static int getSize() {
		return size;
	}

	/**
	 * 
	 * Geter for the size of the grid (in pixels)
	 * 
	 * @return The size of the grid (in pixels)
	 * 
	 */
	public static int getPSize() {
		return pSize;
	}

	/**
	 * 
	 * Getter for the cell dimensions (aka resolution)
	 * 
	 * @return The cell's dimensions
	 * 
	 */
	public static int getRes() {
		return res;
	}

	/**
	 * 
	 * Setter for the grid's dimensions (in cells)
	 * 
	 * @param size The grid's dimensions (in cells)
	 * 
	 */
	public static void setSize(int size) {
		// We'll keep track of the new cells's size
		Grid.size = size;

		// We'll also recalculate our resolution
		res = Grid.pSize / size;

		// If the grid is not initialized
		if (grid == null) {
			// We'll create it
			grid = new Vector<>(size);
		}
		else {
			// Otherwise we simply need to empty it
			grid.clear();
		}

		// We'll also reset the flows
		flows.clear();

		// And fill the grid with cells
		for (int j = 0; j < size; ++j) {
			grid.add(new Vector<Cell>(size));

			for (int i = 0; i < size; ++i) {
				grid.get(j).add(new Cell(i, j));
			}
		}
	}

	/**
	 * 
	 * This method allows adding a new flow to the grid
	 * 
	 * @param c1 The first cell representing the first end point
	 * @param c2 The second cell representing the second end point
	 * 
	 */
	public static void addFlow(Cell c1, Cell c2) {
		flows.add(new Flow(c1, c2));
	}

	/**
	 * 
	 * Getter for the cells
	 * 
	 * @param x The cell's X coordinate
	 * @param y The cell's Y coordinate
	 * 
	 * @return The corresponding cell, if it exits, Null otherwise
	 * 
	 */
	public static Cell getCell(int x, int y) {
		// Does the cell exists?
		if (x < 0 || y < 0 || x >= size || y >= size)
			return null;

		// Return it
		return grid.get(y).get(x);
	}

	/**
	 * 
	 * Setter for the cells
	 * 
	 * @param c The cell to set
	 * 
	 */
	public static void setCell(Cell c) {
		grid.get(c.getY()).set(c.getX(), c);
	}

	/**
	 * 
	 * This method maps a point to cell
	 * 
	 * @param p The point to map
	 * @return The corresponding cell on the grid
	 * 
	 */
	public static Cell mapToCell(Point p) {
		// Scale both coordinates down to the grid
		int col = (int) (Math.floor(p.getX() / res));
		int row = (int) (Math.floor(p.getY() / res));

		// And get the corresponding cell
		return getCell(col, row);
	}

	/**
	 * 
	 * This method calculates the total coverage of the grid
	 * 
	 * @return The coverage of the flows on the maze in percentage
	 * 
	 */
	public static double calculateCoverage() {
		// We'll start at 0
		int totalCovered = 0;

		// And loop over all the cells
		for (Vector<Cell> t : grid) {
			for (Cell c : t) {
				// If this cell is empty we'll skip it
				if (c.getFlow() == null)
					continue;

				// If this is the end of the flow and the flow is not closed
				// we'll not count this cell either
				if (c.isEndPoint() && !((EndPoint) c).isStart() && !c.getFlow().isClosed())
					continue;

				++totalCovered;
			}
		}

		// Now let's break it down with the math
		double coverage = Math.round(10000 * totalCovered / (size * size)) / 100;

		// And show it
		System.out.println("Total coverage is now: " + coverage);

		return coverage;
	}

	/**
	 * 
	 * This method checks to see if the player actually won
	 * 
	 */
	public static void checkWin() {
		// We'll assume all the flows are closed
		boolean allFlowsConnected = true;

		// If we find a flow that is not closed, we'll break the assumption
		for (Flow f : flows) {
			if (!f.isClosed()) {
				allFlowsConnected = false;
				break;
			}
		}

		if (allFlowsConnected) {
			// If all flows are indeed connected, we need to check the coverage
			if (calculateCoverage() == 100) {
				// If we covered all the grid we won
				Main.alert("You won the level!");

				// Lets load the next level
				LevelLibrary.nextLevel();

				// And reset the unsuccessful flag
				unsuccessfulFinish = false;
			}
			else {
				// Otherwise we'll alert the user and mark this as unsuccessful
				// so we won't alert him again
				if (!unsuccessfulFinish) {
					Main.alert("Although you did connect all the flows, the grid is not totally covered. Try again.");
					unsuccessfulFinish = true;
				}
			}
		}
		else {
			// If we didn't close all flows we still need to calculate the
			// coverage of the maze
			calculateCoverage();
		}
	}

	/**
	 * 
	 * This method draws the grod
	 * 
	 * @param g The current graphics handle
	 * 
	 */
	public static void draw(Graphics2D g) {
		// First we'll color each cell with a ligh background corresponding to
		// its flow's color
		for (Vector<Cell> t : grid) {
			for (Cell c : t) {
				if (c.getFlow() != null && c.getFlow().hasStart()) {
					// If this is an end point and we haven't closed its flow
					// we'll not color it
					if (c.getFlow().getEnd() == c && !c.getFlow().isClosed()) {
						continue;
					}

					g.setColor(c.getFlow().getColor().darker());
					g.fillRect(c.getX() * res, c.getY() * res, res, res);
				}
			}
		}

		// Then we'll draw the flows on top of the backgrounds
		for (Flow f : flows) {
			f.draw(g);
		}

		// And then we'll draw the grid over everything
		g.setStroke(new BasicStroke(2));
		g.setColor(Color.GRAY);

		for (int i = 0; i < size; ++i) {
			g.drawLine(i * res, 0, i * res, pSize);
		}

		for (int j = 0; j < size; ++j) {
			g.drawLine(0, j * res, pSize, j * res);
		}
	}
}
