package it.unibs.pajc.nieels.hive;

import java.awt.Point;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
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
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Side;

//VIEW
/**
 * Canvas component which shows the actual place where the pieces are being placed and the game is being played.
 * @author Nicol Stocchetti
 *
 */
public class GameField extends HexField {

	/**
	 * The component's constructor.
	 */
	public GameField() {
		super();
	}

	@Override
	protected void paintComponent(Graphics g) {//Graphics g is an object of the class JPanel that contains a set of instruments to draw on the canvas, we'll use it for everything we need to draw.
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		visiblePieces = hive.getPlacedPieces(); //Only show placed pieces
		
		//Draw pieces
		drawVisiblePieces(g2);
		
		//Draw placements for the selected piece
		drawPossiblePositions(g2);
		
		//Draw selected piece
		drawSelectedPiece(g2);
		
		//Draw mouse position
		drawCursor(g2);
	}

}
