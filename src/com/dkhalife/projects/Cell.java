package com.dkhalife.projects;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * This class represents a cell on the grid
 * 
 * @author Dany Khalife
 * 
 */
public class Cell {
	// Cells have coordinates
	int x, y;

	// Cells will be daisy chained in double links
	private Cell nextCell = null;
	private Cell previousCell = null;

	// Cells can be end points
	protected boolean endPoint = false;

	// And they can belong to a flow
	private Flow flow = null;

	/**
	 * 
	 * To construct a cell, all whats needed are its coordinates
	 * 
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * 
	 */
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 
	 * Getter for the X coordinate
	 * 
	 * @return The X coordinate
	 * 
	 */
	public int getX() {
		return x;
	}

	/**
	 * 
	 * Getter for the Y coordinate
	 * 
	 * @return The Y coordinate
	 * 
	 */
	public int getY() {
		return y;
	}

	/**
	 * 
	 * Getter for the end point flag
	 * 
	 * @return True if this cell is an end point. False otherwise.
	 * 
	 */
	public boolean isEndPoint() {
		return endPoint;
	}

	/**
	 * 
	 * Getter for the previous cell
	 * 
	 * @return The previous cell
	 * 
	 */
	public Cell getPreviousCell() {
		return previousCell;
	}

	/**
	 * 
	 * Setter for the previous Cell
	 * 
	 * param previousCell The cell to set as previous
	 * 
	 */
	public void setPreviousCell(Cell previousCell) {
		this.previousCell = previousCell;
	}

	/**
	 * 
	 * Getter for the next cell
	 * 
	 * @return The next cell
	 * 
	 */
	public Cell getNextCell() {
		return nextCell;
	}

	/**
	 * 
	 * Setter for the next cell
	 * 
	 * @param nextCell The cell to set as next
	 * 
	 */
	public void setNextCell(Cell nextCell) {
		this.nextCell = nextCell;
	}

	/**
	 * 
	 * Getter for the flow
	 * 
	 * @return The cell's flow
	 * 
	 */
	public Flow getFlow() {
		return flow;
	}

	/**
	 * 
	 * Setter for the flow
	 * 
	 * @param flow The flow to set
	 */
	public void setFlow(Flow flow) {
		this.flow = flow;
	}

	/**
	 * 
	 * This method converts the cell into printable format
	 * 
	 */
	public String toString() {
		return "{" + getX() + ";" + getY() + "}";
	}

	/**
	 * 
	 * This method links two cells to each other
	 * 
	 * @param nextCell The cell to link to
	 * 
	 */
	public void linkCell(Cell nextCell) {
		// Link this cell to the next
		this.nextCell = nextCell;

		if (nextCell != null) {
			// And link the next cell to this cell
			nextCell.previousCell = this;

			// While setting its flow to the same as this one
			nextCell.setFlow(getFlow());
		}
	}

	/**
	 * 
	 * This method draws the cell
	 * 
	 * @param g The graphics handle to use
	 * 
	 */
	public void draw(Graphics2D g) {
		// Where are we drawing?
		int startX = getX() * Grid.getRes();
		int startY = getY() * Grid.getRes();
		int endX = startX + Grid.getRes();
		int endY = startY + Grid.getRes();

		// Which image are we going to use to draw this cell
		Image im = ImageFactory.getInstance().get(getFlow().getColor());

		// Now lets check which type of cell this is

		// The cell can be a middle cell
		if (previousCell != null && nextCell != null) {
			int ddx = Math.abs(nextCell.getX() - previousCell.getX());
			int ddy = Math.abs(nextCell.getY() - previousCell.getY());

			// The cell can be a vertical link
			if (ddx == 0 && ddy != 0) {
				g.drawImage(im, startX, startY, endX, endY, 0, 80, 80, 160, null);
			}
			// Or a horizontal link
			else if (ddx != 0 && ddy == 0) { // horizontal
				g.drawImage(im, startX, startY, endX, endY, 160, 160, 240, 240, null);
			}
			// Or a corner
			else if (ddx == 1 && ddy == 1) {
				// If so, we need to find out which type of corner it is

				int d0x = previousCell.getX() - getX();
				boolean leftFirst = d0x == 1;
				boolean rightFirst = d0x == -1;

				int d0y = previousCell.getY() - getY();
				boolean downFirst = d0y == -1;
				boolean upFirst = d0y == 1;

				int d1x = nextCell.getX() - getX();
				boolean thenLeft = d1x == -1;
				boolean thenRight = d1x == 1;

				int d1y = nextCell.getY() - getY();
				boolean thenDown = d1y == 1;
				boolean thenUp = d1y == -1;

				// up then right OR left then down
				if ((upFirst && thenRight) || (leftFirst && thenDown)) {
					g.drawImage(im, startX, startY, endX, endY, 80, 0, 160, 80, null);
				}

				// right then up OR down then left
				else if ((rightFirst && thenUp) || (downFirst && thenLeft)) {
					g.drawImage(im, startX, startY, endX, endY, 160, 80, 240, 160, null);
				}

				// down then right OR left then up
				else if ((downFirst && thenRight) || (leftFirst && thenUp)) {
					g.drawImage(im, startX, startY, endX, endY, 80, 80, 160, 160, null);
				}

				// right then down OR up then left
				else if ((rightFirst && thenDown) || (upFirst && thenLeft)) {
					g.drawImage(im, startX, startY, endX, endY, 160, 0, 240, 80, null);
				}
			}
		}
		// It might also be an end (not necessarily an end point, but an end)
		else if (previousCell != null || nextCell != null) {
			Cell c = (previousCell != null) ? previousCell : nextCell;
			int dx = getX() - c.getX();
			int dy = getY() - c.getY();

			// To the right
			if (dx == -1) {
				g.drawImage(im, startX, startY, endX, endY, 80, 160, 160, 240, null);
			}
			// To the left
			else if (dx == 1) {
				g.drawImage(im, startX, startY, endX, endY, 240, 160, 320, 240, null);
			}
			// To the top
			else if (dy == 1) {
				g.drawImage(im, startX, startY, endX, endY, 0, 160, 80, 240, null);
			}
			// To the bottom
			else if (dy == -1) {
				g.drawImage(im, startX, startY, endX, endY, 0, 0, 80, 80, null);
			}
		}

		// Now, lets draw the next cell if we have one
		if (nextCell != null) {
			nextCell.draw(g);
		}
	}
}
