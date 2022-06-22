package it.unibs.pajc.nieels.hive;


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Placement;
import it.unibs.pajc.nieels.hive.Piece.Side;

//VIEW
/**
 * Canvas component which shows a player's pieces that are yet to be placed in the hive.
 * @author Nicol Stocchetti
 *
 */
public class ToBePlacedField extends HexField  {

	/**
	 * The component's constructor.
	 */
	public ToBePlacedField() {
		super();
		MIN_SIZE_MODIFIER = 0.3;
		MAX_SIZE_MODIFIER = 0.5;
		pieceSizeModifier = MAX_SIZE_MODIFIER;
		setMinimumSize(new Dimension(50, 50));
		setPreferredSize(new Dimension(100, 100));
	}
	
	/**
	 * Sets which color pieces are to be displayed.
	 * @param color the chosen color, PieceColor.
	 */
	public void setColor(PieceColor color) {
		if (hive == null) {
			//exception?
		}
		if (color == PieceColor.BLACK) {
			visiblePieces = hive.getBlacksToBePlaced();
		} else if (color == PieceColor.WHITE) {
			visiblePieces = hive.getWhitesToBePlaced();
		} else {
			//exception?
		}	
		
	}
	
	//An override of the paintComponent() method of the JPanel will allow us to paint directly on the canvas (instead of just using pre-made components)
	@Override
	protected void paintComponent(Graphics g) {//Graphics g is an object of the class JPanel that contains a set of instruments to draw on the canvas, we'll use it for everything we need to draw
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g; //the paintComponent method and Graphics object exist since the dawn of Java, over the time a Graphics2D class, which extends Graphics, has been created to be more advanced with more functions, but we have to cast g to it in order to use it.
		
		origin.setLocation(1.2*pieceSize, height/2.0);
		
		//Draw pieces
		drawAlignedPieces(g2);
		
		//Draw selected piece
		drawSelectedPiece(g2);
		
		//Draw mouse position
		drawCursor(g2);
	}
	
	
	
	double pieceDistance = 2.2; //relative to unscaled model proportions
	
	/**
	 * Draws all the pieces that are yet to be placed in the hive in a row.
	 * @param g2 Graphics2D.
	 */
	private void drawAlignedPieces(Graphics2D g2) {
		if (visiblePieces == null) {
			return;
		}
		
		if (visiblePieces.size() <= 0) {
			//SHOW "SKIP TURN" BUTTON IF THERE ARE NO POSSIBLE MOVEMENTS
			return;
		}
		/*
		if (visiblePieces.get(0) == null) {
			return;
		}
		*/
		
		double horizontalPosition = 0; //first piece of the list is considered the origin of the system
		double verticalPosition = 0;
		for (Piece piece : visiblePieces) {
			/*
			x = horizontalPosition * pieceSize + positionOffset.getX();
			y = verticalPosition * pieceSize + positionOffset.getY();
			*/
			
			piece.getCoordinates().setLocation(horizontalPosition, verticalPosition);
			
			horizontalPosition += pieceDistance;
		}
		//Once the coordinates placement relative to the unscaled model proportions is done, the super's draw pieces will
		//scale everything to the board's actual proportions (window frame, and so on).
		super.drawVisiblePieces(g2);
	}
	
	
	
	//Events that are generated from this component to be handled by this component (the view tells itself to modify itself with events,
	//by firing events that are then re-caught by itself)
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDrag = true;
		xVariation = e.getPoint().getX() - mousePosition.getX();
		yVariation = e.getPoint().getY() - mousePosition.getY();
		
		if (positionOffset.getX() + xVariation < 0 //can't increment the global x position with a positive x offset (it would mean moving forward beyond the origin for the position of the first piece)
				&&	
				(positionOffset.getX() + xVariation > - (origin.getX() + (pieceDistance * pieceSize) * (visiblePieces.size() - 1) + origin.getX() - width) //can't go on after the last piece is shown
						||
				xVariation > 0) //can still go back if needed even when the last piece is already fully shown
			) { 
			
			positionOffset.setLocation(positionOffset.getX() + xVariation, positionOffset.getY()); //Only horizontal scroll
		}
		
		mousePosition = e.getPoint();
		this.repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		mousePosition = e.getPoint(); //x and y
		
		Piece clickedPiece = getPieceAt(mousePosition.getX(), mousePosition.getY());
		
		if (clickedPiece == null || clickedPiece == hive.getSelectedPiece()) {
			fireActionListener(new ActionEvent(this, 0, "no_piece_selected"));
		}
		else {
			fireActionListener(new ActionEvent(clickedPiece, 1, "show_possible_positions"));	
		}
		
		fireValuesChange(new ChangeEvent(this));
	}
}
