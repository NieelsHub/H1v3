package it.unibs.pajc.nieels.hive;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedHashMap;

import javax.swing.JComponent;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Side;

//VIEW
public class GameField extends JComponent implements MouseMotionListener, MouseListener, MouseWheelListener {

	private LinkedHashMap <Class<?>, Integer> piecesSet = new LinkedHashMap();
	private Hive hive; //The model
	
	private int height; //Putting them as class attributes will make them accessible to all class methods (not only paint component)
	private int width;
	
	private int pieceSize;
	private int pieceSizeModifier = 14; //PRENDERE QUESTO 14 DA UN VALORE SETTATO NEL MENU DI OPZIONI DEL GIOCO (compreso tra MAX_SIZE_MODIFIER e MIN)
	public final int MIN_SIZE_MODIFIER = 4;
	public final int MAX_SIZE_MODIFIER = 14;
	
	private Point mousePosition = new Point(0, 0); //we memorize it as a global variable so that I can access this data from all the class, included the paintComponent method  (so i can use these coordinates to draw something)
	private Point positionOffset = new Point(0, 0);
	private boolean mouseDrag = false;
	
	{
		piecesSet.put(QueenBee.class, 1);
		piecesSet.put(Spider.class, 2);
		piecesSet.put(Beetle.class, 2);
		piecesSet.put(SoldierAnt.class, 3);
		piecesSet.put(Grasshopper.class, 3);
		
		hive = new Hive(piecesSet);
		//////
		hive.placeFirstPiece(hive.getBlacksToBePlaced().get(0));
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(0), hive.getPlacedPieces().get(0), Side.SOUTH);
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(0), hive.getPlacedPieces().get(1), Side.SOUTH);
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(0), hive.getPlacedPieces().get(2), Side.NORTHEAST);
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(5), hive.getPlacedPieces().get(3), Side.NORTHEAST);
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(4), hive.getPlacedPieces().get(2), Side.NORTHWEST);
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(4), hive.getPlacedPieces().get(2), Side.SOUTH);
		//////////
	}
	
	//We want to define a constructor which takes in the events listener that we'll be using to interact with the mouse
	public GameField() {
		super();
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addMouseWheelListener(this);
		this.setBackground(new Color(255, 255, 200));
	}
	
	//An override of the paintComponent() method of the JPanel will allow us to paint directly on the canvas (instead of just using pre-made components)
	@Override
	protected void paintComponent(Graphics g) {//Graphics g is an object of the class JPanel that contains a set of instruments to draw on the canvas, we'll use it for everything we need to draw
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g; //the paintComponent method and Graphics object exist since the dawn of Java, over the time a Graphics2D class, which extends Graphics, has been created to be more advanced with more functions, but we have to cast g to it in order to use it.
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //Activates antialiasing
		
		//this paintComponent method will be re-executed only when the framework (java swing) will perceive something in the window has changed and so a redraw is actually needed
		//Each time a redraw is needed, the framework will generate a refresh of the components, so that they'll be redrawn
		//In case the redraw is done because the component's size changed, we want to get the new width and height
		width = getWidth();
		height = getHeight();
		pieceSize = (width < height) ? width/pieceSizeModifier : height/pieceSizeModifier;
		
		//Draw board
		drawBoard(g2);

		//Gives offset to pieces representation when mouse dragged to navigate the enviroment
		senseDrag(g2);//da eliminare, è solo debug
		
		//scrivi coordinate mouse in alto a sx schermo
		g2.setColor(Color.RED);
		g2.drawString(mousePosition.getX() + " " + mousePosition.getX(), 10, 10);
		
		g2.setColor(Color.BLUE);
		g2.drawString(positionOffset.getX() + " " + positionOffset.getX(), 10, 30);
		
		//senseZoom(g2);
		
		//Draw mouse position
		drawCursor(g2);
		
		//Draw pieces
		drawPieces(g2);
	}
	
	
	private void drawBoard(Graphics2D g2) {
		g2.setColor(this.getBackground());
		g2.fillRect(0, 0, width, height);
			
	}
	
	
	private void drawCursor(Graphics2D g2) {
		if (mousePosition != null) {
			//red pointer
			g2.setColor(Color.RED);
			g2.fillOval(mousePosition.x, mousePosition.y, 5, 5);
		}
		
	}
	
	private void drawPieces(Graphics2D g2) {
		int x;
		int y;
		
		for (Piece piece : hive.getPlacedPieces()) {
			x = (int)(piece.getX()*pieceSize+width*0.5 + positionOffset.getX());
			y = (int)(piece.getY()*pieceSize+height*0.5 + positionOffset.getY());
			Color color;
			
			if (piece.getColor() == PieceColor.WHITE) {
				color = Color.GRAY;
			} else {
				color = Color.BLACK;
			}
			drawPiece(g2, color, x, y);
		}
		
		int horizontalPosition = (int)(pieceSize * 1.5);
		for (Piece piece : hive.getWhitesToBePlaced()) {
			drawPiece(g2, Color.GRAY, horizontalPosition, (int)(height*0.90));
			horizontalPosition += (int)(pieceSize * 1.5);
		}
		
		horizontalPosition = (int)(pieceSize * 1.5);
		for (Piece piece : hive.getBlacksToBePlaced()) {
			drawPiece(g2, Color.BLACK, horizontalPosition, (int)(height*0.10));
			horizontalPosition += (int)(pieceSize * 1.5);
		}
	}
	
	private void drawPiece(Graphics2D g2, Color color, int x, int y /*, IMMAGINE*/) {
		int[] xPoints = {(int)(x-pieceSize*0.65), (int)(x-pieceSize*0.33), (int)(x+pieceSize*0.33), (int)(x+pieceSize*0.65),
										(int)(x+pieceSize*0.33), (int)(x-pieceSize*0.33),}; 
		
		int[] yPoints = {y, (int)(y+pieceSize*0.49), (int)(y+pieceSize*0.49), y,
				(int)(y-pieceSize*0.49), (int)(y-pieceSize*0.49)};
		
		g2.setColor(color);
		g2.fillPolygon(xPoints, yPoints, 6);
	}

	private void senseDrag(Graphics2D g2) {
		if (mouseDrag) {
			g2.setColor(Color.CYAN);
			g2.fillRect(0, 0, width, height);
		}
	}
	
	//Events
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDrag = true;
		positionOffset.setLocation(positionOffset.getX()+e.getPoint().getX()-mousePosition.getX(), positionOffset.getY()+e.getPoint().getY()-mousePosition.getY());
		mousePosition = e.getPoint();
		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePosition = e.getPoint(); //x and y
		//We need to force the component's redraw when the mouse is moved (it won't do it on his own by default because it's costly)
		this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseDrag = false;
		this.repaint();
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getUnitsToScroll() > 0) {
			pieceSizeModifier++;
		} else {
			pieceSizeModifier--;
		}
		
		if (pieceSizeModifier < MIN_SIZE_MODIFIER) {
			pieceSizeModifier = MIN_SIZE_MODIFIER;
		} else if (pieceSizeModifier > MAX_SIZE_MODIFIER) {
			pieceSizeModifier = MAX_SIZE_MODIFIER;
		}
		this.repaint();
	}

}
