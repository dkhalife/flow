package com.dkhalife.projects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This class represents a level in the game
 * 
 * @author Dany Khalife
 * 
 */
class Level extends JPanel {
	// Eclipse generated serial ID
	private static final long serialVersionUID = -2596206883251038820L;

	// Since we are manipulating a drag and drop opreration we need to know the
	// last cell that was hovered over and if a current drag is taking place
	private boolean validDrag = false;
	private Cell lastCell = null;

	public Level() {
		// Allow focus events
		setFocusable(true);

		// Draw a thick black border around the level
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		// Listen for mouse events
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// When the mouse is pressed we need to check if we have a valid
				// drag n drop operation so first we'll the dragging to false
				validDrag = false;

				// Only left clicks are allowed
				if (SwingUtilities.isLeftMouseButton(e)) {
					// Lets set the last cell to be the one we just clicked on
					lastCell = Grid.mapToCell(e.getPoint());

					// If we clicked on a disconnected cell we have to stop
					// right here, its not a valid drag and drop operation
					if (lastCell.getFlow() == null) {
						lastCell = null;
						return;
					}

					// If we clicked on a flow that doesnt have a start yet
					if (!lastCell.getFlow().hasStart()) {
						// We'll set this cell has the starting cell
						((EndPoint) lastCell).setStart(true);
					}
					else {
						// Otherwise we most definitely clicked on a flow
						// We could have broken it, started it from the other
						// end point, or even continued it
						lastCell.getFlow().setClosed(false);

						// Case 1: We clicked on the other end point of the flow
						if (lastCell.getFlow().getEnd() == lastCell) {
							// We have to unselect everything from the start
							Cell it = lastCell.getFlow().getStart();
							while (it != null) {
								Cell temp = it.getNextCell();

								// Break both links
								it.setNextCell(null);
								it.setPreviousCell(null);

								// And reset the flow for the cell to null only
								// if it's not an end point
								if (!it.isEndPoint()) {
									it.setFlow(null);
								}

								it = temp;
							}

							// Now we need to reverse the start
							EndPoint f = lastCell.getFlow().getEnd();
							lastCell.getFlow().getStart().setStart(false);
							f.setStart(true);
						}
						else {
							// Case 2: We clicked somewhere in the middle of a
							// flow, we need to disconnect everything after this
							// dragging point (if we are continuing a previous
							// flow then there is nothing after this flow so
							// nothing will be deleted...)
							Cell it = lastCell.getNextCell();

							// Unlink this cell from the next
							lastCell.setNextCell(null);

							// Unlink the remaining cells
							while (it != null) {
								Cell temp = it.getNextCell();

								// Break both links
								it.setNextCell(null);
								it.setPreviousCell(null);

								// And reset the cell's flow only if it's not an
								// end point
								if (!it.isEndPoint()) {
									it.setFlow(null);
								}

								it = temp;
							}
						}
					}

					// If we arrived here it means that the drag operation is
					// valid so we'll set the flag to true
					validDrag = true;

					// Since we clicked somewhere we need to repaint our grid
					repaint();
				}
			}

			// A drag operation stops when the mouse has been released
			public void mouseReleased(MouseEvent e) {
				// If a dragging operation was taking place
				if (validDrag) {
					// Then we will stop it
					validDrag = false;

					// And check if the user has won or not yet
					Grid.checkWin();
				}
			}
		});

		// Listen for mouse motion events
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				// Here we listen for mouse movements while the mouse button is
				// still clicked
				// So we only need to take action when a valid drag operation is
				// taking place
				if (validDrag) {
					Cell currentCell = Grid.mapToCell(e.getPoint());

					// If we are hovering outside of the grid we'll have to
					// cancel the drag operation
					if (currentCell == null) {
						validDrag = false;
						return;
					}

					// Now we need to check if we actually crossed another cell
					if (lastCell != currentCell) {
						// IF we did, we can only go in one step and one
						// direction
						if (Math.abs(lastCell.getX() - currentCell.getX()) == 1 ^ Math.abs(lastCell.getY() - currentCell.getY()) == 1) {
							// If we did, and we crossed over an empty cell
							if (currentCell.getFlow() == null) {
								// And If the dragged flow is not closed, we
								// will add this cell to the flow
								// Otherwise, we'll just do nothing (we won't
								// stop the drag to allow the user to trace back
								// the flow even if he reached the end point)
								if (!lastCell.getFlow().isClosed()) {
									lastCell.linkCell(currentCell);
									lastCell = currentCell;
								}
							}
							else {
								// If we didn't go over an empty cell, then we
								// definitely intercepted a flow
								// Here we have two cases, it can either be the
								// current flow, or any other

								// Case 1: We intercepted the current flow
								if (currentCell.getFlow() == lastCell.getFlow()) {
									// Now if we intercepted the same flow we
									// can either be reaching another endpoint
									// Or we could actually be going over our
									// steps

									// If we are reaching the end of the flow
									if (currentCell == lastCell.getFlow().getEnd()) {
										// We are closing the flow
										lastCell.linkCell(currentCell);
										lastCell = currentCell;
										currentCell.getFlow().setClosed(true);
									}
									else {
										// Here we did not reach the end of the
										// flow yet
										// So we'll have to roll back to get to
										// the current cell againt

										Cell it = lastCell;
										while (it != currentCell) {
											Cell prev = it.getPreviousCell();

											// Break both links
											it.setPreviousCell(null);
											it.setNextCell(null);

											// And disconnect from the flow if
											// this is not an endpoint
											if (!it.isEndPoint())
												it.setFlow(null);
											// However, in the case this is an
											// endpoint, it also means we broke
											// the current flow
											// So we'll need to set the closed
											// flag as false
											else
												it.getFlow().setClosed(false);

											it = prev;
										}

										// In either cases, now we are ready to
										// unlink the current cell
										currentCell.linkCell(null);

										// And move teh drag cursor to the
										// current cell
										lastCell = currentCell;
									}
								}
								else {
									// Case 2: We intercepted another flow

									// If we touched an end point we'll forget
									// about this step
									if (currentCell.isEndPoint()) {
										return;
									}

									// Unlink previous from current
									currentCell.getPreviousCell().setNextCell(null);

									// Breaking the link also means unclosing a	closed flow
									currentCell.getFlow().setClosed(false);

									// Unlink everything else after current
									Cell it = currentCell;
									while (it != null) {
										Cell next = it.getNextCell();

										// Break both links
										it.setPreviousCell(null);
										it.setNextCell(null);

										// And remove from the flow only if it's not an end point
										if (!it.isEndPoint())
											it.setFlow(null);

										it = next;
									}

									// And now lets link the current cell and
									// previous, and step forward
									lastCell.linkCell(currentCell);
									lastCell = currentCell;
								}
							}
						}
					}
				}

				// This method may have caused lots of graphical changes so lets
				// redraw everything
				repaint();
			}
		});
	}

	/**
	 * 
	 * This method is responsible for returning the dimension to our JFrame so
	 * that the level is fully drawn and not cropped
	 * 
	 * @return The width and height of our level
	 * 
	 */
	public Dimension getPreferredSize() {
		return new Dimension(Grid.getPSize(), Grid.getPSize());
	}

	/**
	 * 
	 * This method actually prints the view every time a repaint is needed
	 * 
	 */
	protected void paintComponent(Graphics h) {
		super.paintComponent(h);

		Graphics2D g = (Graphics2D) h;

		// Draw the grid
		Grid.draw(g);
	}
}
