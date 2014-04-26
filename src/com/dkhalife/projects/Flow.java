package com.dkhalife.projects;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * This class represents a Flow
 * 
 * @author Dany Khalife
 * 
 */
public class Flow {
	// A flow has two end points
	private EndPoint e;
	private EndPoint f;

	// It also has a color
	private Color color = null;

	// And a "closed" state to determine when both endpoints are linked
	private boolean closed = false;

	/**
	 * 
	 * A flow is constructed using two cells
	 * 
	 * @param a The first end point
	 * @param b The second end point
	 * 
	 */
	public Flow(Cell a, Cell b) {
		// First we'll get a new color for this flow
		color = ColorFactory.getInstance().getColor();

		// We'll convert the cells into end points
		e = new EndPoint(a);
		f = new EndPoint(b);

		// We'll set them as part of this flow
		e.setFlow(this);
		f.setFlow(this);

		// And we'll update teh grid
		Grid.setCell(e);
		Grid.setCell(f);
	}

	/**
	 * 
	 * Getter for the flow's color
	 * 
	 * @return The flow's color
	 * 
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * 
	 * Getter for the closed state
	 * 
	 * @return True if the flow has been closed
	 * 
	 */
	public boolean isClosed() {
		return closed;
	}

	/**
	 * 
	 * Setter for the closed state
	 * 
	 * @param closed Is the flow closed ?
	 */
	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	/**
	 * 
	 * This method checks if the current flow has a start, which will indicate
	 * if the user has already clicked on it or not
	 * 
	 * @return
	 */
	public boolean hasStart() {
		return getStart() != null;
	}

	/**
	 * 
	 * Getter for the starting end point
	 * 
	 * @return The starting end point
	 */
	public EndPoint getStart() {
		if (e.isStart())
			return e;
		if (f.isStart())
			return f;

		return null;
	}

	/**
	 * 
	 * Getter for the ending point
	 * 
	 * @return The ending point
	 */
	public EndPoint getEnd() {
		if (getStart() == null) {
			return null;
		}
		else {
			if (!e.isStart()) {
				return e;
			}
			else {
				return f;
			}
		}
	}

	/**
	 * 
	 * This method draws the flow
	 * 
	 * @param g The current graphics handle
	 * 
	 */
	public void draw(Graphics2D g) {
		// To draw the flow we need to draw both ends
		// Since the cells on the flow are daisy chained, they will be drawn one
		// after the other
		e.draw(g);
		f.draw(g);
	}
}
