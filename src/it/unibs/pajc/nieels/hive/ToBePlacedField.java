package it.unibs.pajc.nieels.hive;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Side;

//VIEW
public class ToBePlacedField extends JComponent implements MouseMotionListener, MouseListener, MouseWheelListener {

	private ArrayList <Piece> pieces;
	
	Color backgroundColor = new Color(255, 255, 200);
	
	private int height; //Putting them as class attributes will make them accessible to all class methods (not only paint component)
	private int width;
	
	private int pieceSize;
	private double pieceSizeModifier = 0.7; 
	
	private HashMap <String, Image> pieceImgs = new HashMap();
	private double imgSizeModifier = 0.7; 
	
	private Point mousePosition = new Point(0, 0); //we memorize it as a global variable so that I can access this data from all the class, included the paintComponent method  (so i can use these coordinates to draw something)
	private Point positionOffset = new Point(0, 0);
	private boolean mouseDrag = false;
	
	
	//We want to define a constructor which takes in the events listener that we'll be using to interact with the mouse
	public ToBePlacedField() {
		super();
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addMouseWheelListener(this);
		this.setBackground(backgroundColor);
		
		loadSettings();
	}
	
	private void loadSettings() {
		backgroundColor = new Color(255, 255, 200);
		pieceSizeModifier = 0.7;
		imgSizeModifier = 0.7; 
	}

	@Override
	public Dimension getPreferredSize() {
		super.setPreferredSize(new Dimension(100, 100)); 
		return super.getPreferredSize();
	}

	@Override
	public Dimension getMinimumSize() {
		super.setMinimumSize(new Dimension(50, 50)); 
		return super.getMinimumSize();
	}

	/**
	 * Returns the model that's being used for game information by the view.
	 * @return the model, Hive.
	 */
	public ArrayList <Piece> getPieces() {
		return pieces;
	}

	/**
	 * Sets the model from which to generate the view.
	 * @param hive the model, Hive.
	 */
	public void setPieces(ArrayList <Piece> pieces) {
		this.pieces = pieces;
		loadImages();
	}
	
	private void loadImages() {
		String directory;
		pieceImgs.clear();
		
		for (Piece piece : pieces) {
			directory = "resources/" + piece.getName() + ".png";
			try {
				pieceImgs.put(piece.getName(), ImageIO.read(new File(directory)));
			} catch(IOException ex) {
				System.err.print(ex);
			}
		}
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
		pieceSize = (width < height) ? (int)(width*pieceSizeModifier) : (int)(height*pieceSizeModifier);
		
		//Draw board
		drawBoard(g2);
		
		/*
		//scrivi coordinate mouse in alto a sx schermo
		g2.setColor(Color.RED);
		g2.drawString(mousePosition.getX() + " " + mousePosition.getX(), 10, 10);
		
		g2.setColor(Color.BLUE);
		g2.drawString(positionOffset.getX() + " " + positionOffset.getX(), 10, 30);
		*/
		
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
		Image img;
		Color color;
		
		if (pieces == null) {
			return;
		}
		
		if (pieces.get(0) == null) {
			return;
		}
		
		if (pieces.get(0).getColor() == PieceColor.BLACK) {
			color = Color.BLACK;
		} else {
			color = Color.GRAY;
		}
		
		int horizontalPosition = (int)(pieceSize*0.75);
		for (Piece piece : pieces) {
			img = pieceImgs.get(piece.getName());
			
			x = (int)(horizontalPosition + positionOffset.getX());
			y = (int)(pieceSize*0.75 + positionOffset.getY());
			
			drawPiece(g2, color, x, y, img);
			horizontalPosition += (int)(pieceSize * 1.5);
		}
	}
	
	private void drawPiece(Graphics2D g2, Color color, int x, int y, Image img) {
		int[] xPoints = {(int)(x - pieceSize * 0.65), (int)(x - pieceSize * 0.33), (int)(x + pieceSize * 0.33), (int)(x + pieceSize * 0.65),
						(int)(x + pieceSize * 0.33), (int)(x - pieceSize * 0.33)}; 
		
		int[] yPoints = {y, (int)(y + pieceSize * 0.49), (int)(y + pieceSize * 0.49), y, (int)(y - pieceSize * 0.49), (int)(y - pieceSize * 0.49)};
		
		g2.setColor(color);
		g2.fillPolygon(xPoints, yPoints, 6);
		g2.drawImage(img, (int)(x - pieceSize * 0.5 * imgSizeModifier), (int)(y - pieceSize * 0.5 * imgSizeModifier), (int)(pieceSize * imgSizeModifier), (int)(pieceSize * imgSizeModifier), null);
	}
	
	
	
	//Events that are generated from this component to be handled by this component (the view tells itself to modify itself with events,
	//by firing events that are then re-caught by itself)
	
	double xVariation;
	double yVariation;
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDrag = true;
		xVariation = e.getPoint().getX() - mousePosition.getX();
		yVariation = e.getPoint().getY() - mousePosition.getY();
		
		if (positionOffset.getX() + xVariation < 0 &&
			positionOffset.getX() + xVariation > width - pieceSize*1.5*pieces.size()) {
			
			positionOffset.setLocation(positionOffset.getX() + xVariation, positionOffset.getY()); //Only horizontal scroll
		}
		
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
		/*Point boardPos = screenToBoard(e.getPoint());
		if(selectedPieceId == null) {
			Piece selectedPiece = game.getPieceAtPos(boardPos);
			selectedPieceId = selectedPiece != null ? selectedPiece.id : null;
		} else {
			game.move(selectedPieceId, boardPos.x, boardPos.y);
			selectedPieceId = null;
		}
		
		repaint();*/
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
		/*if (e.getUnitsToScroll() > 0) {
			pieceSizeModifier++;
		} else {
			pieceSizeModifier--;
		}
		
		if (pieceSizeModifier < MIN_SIZE_MODIFIER) {
			pieceSizeModifier = MIN_SIZE_MODIFIER;
		} else if (pieceSizeModifier > MAX_SIZE_MODIFIER) {
			pieceSizeModifier = MAX_SIZE_MODIFIER;
		}
		this.repaint();*/
	}
}
