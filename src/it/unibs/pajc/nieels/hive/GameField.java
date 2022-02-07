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
import java.util.LinkedHashMap;

import javax.swing.JComponent;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;

public class GameField extends JComponent implements MouseMotionListener, MouseListener {

	LinkedHashMap <Class<?>, Integer> piecesSet = new LinkedHashMap();
	Hive hive; //The model
	
	private int height; //Putting them as class attributes will make them accessible to all class methods (not only paint component)
	private int width;
	private int pieceSize;
	Point mousePosition; //we memorize it as a global variable so that I can access this data from all the class, included the paintComponent method  (so i can use these coordinates to draw something)
	
	{
		piecesSet.put(QueenBee.class, 1);
		piecesSet.put(Spider.class, 2);
		piecesSet.put(Beetle.class, 2);
		piecesSet.put(SoldierAnt.class, 3);
		piecesSet.put(Grasshopper.class, 3);
		
		hive = new Hive(piecesSet);
	}
	
	//We want to define a constructor which takes in the events listener that we'll be using to interact with the mouse
	public GameField() {
		super();
		addMouseMotionListener(this);
		addMouseListener(this);
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
		
		//Draw board
		drawBoard(g2);
		
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
		for (Piece piece : hive.getHive()) {
			drawPiece(g2, piece);
		}
		
		Double horizontalPosition = 1.0;
		for (Piece piece : hive.getWhitesToBePlaced()) {
			drawUnplacedPiece(g2, piece, horizontalPosition, height*0.90);
			horizontalPosition++;
		}
		
		horizontalPosition = 1.0;
		for (Piece piece : hive.getBlacksToBePlaced()) {
			drawUnplacedPiece(g2, piece, horizontalPosition, height*0.10);
			horizontalPosition++;
		}
	}
	
	private void drawPiece(Graphics2D g2, Piece piece) {
		drawUnplacedPiece(g2, piece, piece.getX(), piece.getY());
	}
	
	private void drawUnplacedPiece(Graphics2D g2, Piece piece, Double x, Double y) {
		pieceSize = 5;//(width < height) ? width/10 : height/10;
		
		if (piece.getColor() == PieceColor.WHITE) {
			g2.setColor(Color.WHITE);
		} else {
			g2.setColor(Color.BLACK);
		}
		
		g2.fillOval(((int)(x*10))*pieceSize, ((int)(y*10))*pieceSize, pieceSize, pieceSize);
		
		System.out.println(piece);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		//We don't implement it because we won't use it
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
