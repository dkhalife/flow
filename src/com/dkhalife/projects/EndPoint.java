package com.dkhalife.projects;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * This class represents an Flow's EndPoint
 * 
 * @author Dany Khalife
 * 
 */
public class EndPoint extends Cell {
	// We need to know if this is the starting endpoint (the one the user
	// clicked on first)
	private boolean start = false;

	/**
	 * 
	 * An endpoint can be constructed using only coordinates
	 * 
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * 
	 */
	public EndPoint(int x, int y) {
		super(x, y);

		// Mark this as an endpoint
		endPoint = true;
	}

	/**
	 * 
	 * An endpoint can be created from an existing cell
	 * 
	 * @param c An existing cell
	 * 
	 */
	public EndPoint(Cell c) {
		super(c.getX(), c.getY());

		endPoint = true;
	}

	/**
	 * 
	 * Getter for the start
	 * 
	 * @return True if this is the starting endpoint
	 * 
	 */
	public boolean isStart() {
		return start;
	}

	/**
	 * 
	 * Setter for the start
	 * 
	 * @param start Is this the starting endpoint
	 * 
	 */
	public void setStart(boolean start) {
		this.start = start;
	}

	/**
	 * 
	 * This method draws an endpoint and then starts the drawing of the entire
	 * flow
	 * 
	 */
	public void draw(Graphics2D g) {
		// Where will we draw the current cell?
		int startX = getX() * Grid.getRes();
		int startY = getY() * Grid.getRes();
		int endX = startX + Grid.getRes();
		int endY = startY + Grid.getRes();

		// Get the image corresponding to a disconnected flow		
		Image im = ImageFactory.getInstance().get(getFlow().getColor());

		// The draw it
		g.drawImage(im, startX, startY, endX, endY, 240, 0, 320, 80, null);

		// Now start drawing the entire flow over it
		super.draw(g);
	}
}
