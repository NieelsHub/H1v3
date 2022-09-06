package it.nieels.unibs.pajc.h1v3.view;

import java.awt.Point;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

import it.nieels.unibs.pajc.h1v3.model.Hive;
import it.nieels.unibs.pajc.h1v3.model.Piece;
import it.nieels.unibs.pajc.h1v3.model.Piece.PieceColor;
import it.nieels.unibs.pajc.h1v3.model.Piece.Placement;
import it.nieels.unibs.pajc.h1v3.model.Piece.Side;

//VIEW
/**
 * A generic canvas like component made for displaying and moving around the hexagonal pieces of a hive game.
 * @author Nicol Stocchetti
 *
 */
public abstract class HexField extends EventJComponent implements MouseMotionListener, MouseListener, MouseWheelListener {

	public static final String VISUAL_RESOURCES_DIRECTORY = "/it/nieels/unibs/pajc/h1v3/view/resources/" ;
	
	Hive hive; //The model
	ArrayList <Piece> visiblePieces = new ArrayList();
	ArrayList <Piece> allPieces = new ArrayList();
	
	Color backgroundColor; //Make it a choice from the settings menu?
	
	int height; //Putting them as class attributes will make them accessible to all class methods (not only paint component)
	int width;
	Point2D.Double origin = new Point2D.Double();
	
	double pieceSize;
	double pieceSizeModifier; //Make it a choice from the settings menu? (values must be between MAX_SIZE_MODIFIER and MIN)
	double MIN_SIZE_MODIFIER = 0.07;
	double MAX_SIZE_MODIFIER = 0.25;
	
	double wheelSensitivity;
	public final double MIN_WHEEL_SENSITIVITY = 0.01;
	public final double MAX_WHEEL_SENSITIVITY = 0.1;
	
	HashMap <String, Image> pieceImgs = new HashMap();
	double imgSizeModifier; //Make it a choice from the settings menu, assigned when we call loadSettings() at line 87?
	
	Point mousePosition = new Point(0, 0); //Memorizing them as global variables so that it's possible to access this data from all the class, included the paintComponent method (so that these coordinates can be used to draw something)
	Point positionOffset = new Point(0, 0);
	boolean mouseDrag = false;
	
	
	/**
	 * The component's constructor, it adds to this object the events listener that will be used to
	 * interact with the mouse (the JComponent class, from which HexField inherits, already implements the "add listener" methods).
	 */
	public HexField() {
		super();
		
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addMouseWheelListener(this);

		visiblePieces.clear();/////////////
		visiblePieces.addAll(allPieces);
		
		loadSettings();
		
		this.setBackground(backgroundColor);
	}
	
	private void loadSettings() {
		backgroundColor = new Color(255, 255, 200);
		pieceSizeModifier = MAX_SIZE_MODIFIER;
		imgSizeModifier = 1.3; //Make it a choice from the settings menu?
		wheelSensitivity = 0.03;
	}
	
	/**
	 * Returns the model that's being used for getting game information by the view.
	 * @return the model, Hive.
	 */
	public Hive getHive() {
		return hive;
	}

	/**
	 * Sets the model from which to generate the view.
	 * @param hive the model, Hive.
	 */
	public void setHive(Hive hive) {
		this.hive = hive;
		
		allPieces.clear();
		allPieces.addAll(hive.getPlacedPieces());
		allPieces.addAll(hive.getBlacksToBePlaced());
		allPieces.addAll(hive.getWhitesToBePlaced());
		
		visiblePieces.clear();/////////////
		visiblePieces.addAll(allPieces);///////////////////////////////////////////////////modifica di prova
		
		loadImages();
		this.repaint();//////////////////////////////////////////////////////modifica di prova
	}
	
	
	/**
	 * Loads the pieces' images from a given directory.
	 */
	protected void loadImages() {
		//String imgPath;
		URL imgPath;
		
		pieceImgs.clear();
		
		for (Piece piece : allPieces) {
			imgPath = getClass().getResource(VISUAL_RESOURCES_DIRECTORY + piece.getName() + ".png");
			try {
				pieceImgs.put(piece.getName(), ImageIO.read(imgPath));
			} catch(IOException ex) {
				System.err.print(ex);
			}
		}
	}

	//An override of the paintComponent() method of the JComponent allows to paint directly on the canvas (instead of only adding pre-made components).
	@Override
	protected void paintComponent(Graphics g) {//Graphics g is an object of the class JComponent that contains a set of instruments to draw on the canvas, we'll use it for everything we need to draw.
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g; //the paintComponent method and Graphics object exist since the dawn of Java, over the time a Graphics2D class, which extends Graphics, has been created to be more advanced with more functions, but we have to cast g to it in order to use it.
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //Activates antialiasing for figures drawn on canvas (vector-style)
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC); //Activates interpolation for pixel images drawn on canvas
		//https://jvm-gaming.org/t/loaded-images-wont-anti-alias/28537/9 //https://blog.idrsolutions.com/2019/01/image-scaling-options-in-java/

		//this paintComponent method will be re-executed only when the framework (java swing) will perceive something in the window has changed and so a redraw is actually needed
		//Each time a redraw is needed, the framework will generate a refresh of the components, so that they'll be redrawn
		//In case the redraw is done because the component's size changed, we want to get the new width and height
		width = getWidth();
		height = getHeight();
		origin.setLocation(width/2.0, height/2.0);
		
		pieceSize = (width < height) ? width*pieceSizeModifier : height*pieceSizeModifier;
		
		//Draw board
		drawBoard(g2);
		
		/*
		drawMouseCoordinates(g2);
		
		drawOffsetCoordinates(g2);
		*/
	}
	
	/**
	 * Draws the mouse coordinates in the top left corner of the screen.
	 * @param g2 Graphics2D.
	 */
	void drawMouseCoordinates(Graphics2D g2) {
		g2.setColor(Color.RED);
		g2.drawString(mousePosition.getX() + " " + mousePosition.getY(), 10, 10);
	}
	
	/**
	 * Draws the field offset coordinates in the top left corner of the screen.
	 * @param g2 Graphics2D.
	 */
	void drawOffsetCoordinates(Graphics2D g2) {
		g2.setColor(Color.BLUE);
		g2.drawString(positionOffset.getX() + " " + positionOffset.getY(), 10, 30);
	}
	
	/**
	 * Draws the background color and other fixed elements under the game pieces.
	 * @param g2 Graphics2D.
	 */
	void drawBoard(Graphics2D g2) {
		g2.setColor(this.getBackground());
		g2.fillRect(0, 0, width, height);	
	}
	
	/**
	 * Draws a red pointer at the mouse's position.
	 * @param g2 Graphics2D.
	 */
	void drawCursor(Graphics2D g2) {
		if (mousePosition != null) {
			//red pointer
			g2.setColor(Color.RED);
			g2.fillOval(mousePosition.x, mousePosition.y, 5, 5);
		}
	}
	
	/**
	 * Draws all the visible game pieces.
	 * @param g2 Graphics2D.
	 */
	void drawVisiblePieces(Graphics2D g2) {
		drawPieces(g2, visiblePieces);
	}
	
	/**
	 * Draws a series of pieces.
	 * @param g2 Graphics2D.
	 * @param pieces the pieces to draw, ArrayList <Piece>.
	 */
	void drawPieces(Graphics2D g2, ArrayList <Piece> pieces) {
		Point2D.Double boardCoords;
		Image img;
		Color color; //Make it a choice from the settings menu?
		
		//g2.translate(width/2, height/2);
		
		for (Piece piece : pieces) {
			
			if (piece.getTopPiece() == null) {///////////////
				
				boardCoords = modelToBoard(piece.getCoordinates());
				
				img = pieceImgs.get(piece.getName());
				
				if (piece.getColor() == PieceColor.WHITE) {
					color = Color.GRAY;
				} else {
					color = Color.BLACK;
				}
				drawPiece(g2, color, boardCoords.getX(), boardCoords.getY(), img);
			}//////////////
		}
	}
	
	/**
	 * Draws a game piece.
	 * @param g2 Graphics2D.
	 * @param color the color of the piece's team, PieceColor.
	 * @param x the x coordinate for the center of the piece, double.
	 * @param y the y coordinate for the center of the piece, double.
	 * @param img the image to display on the piece, Image.
	 */
	private void drawPiece(Graphics2D g2, Color color, double x, double y, Image img) {
		int[] xPoints = {(int)(x - pieceSize), (int)(x - pieceSize * 0.5), (int)(x + pieceSize * 0.5), (int)(x + pieceSize),
						(int)(x + pieceSize * 0.5), (int)(x - pieceSize * 0.5)}; //cos(60) = 0.5
		
		int[] yPoints = {(int)y, (int)(y + pieceSize * Math.sqrt(3)/2), (int)(y + pieceSize * Math.sqrt(3)/2), (int)y,
						(int)(y - pieceSize * Math.sqrt(3)/2), (int)(y - pieceSize * Math.sqrt(3)/2)}; //sin(60) = sqrt(3)/2
		
		g2.setColor(color);
		g2.fillPolygon(xPoints, yPoints, 6);
		g2.drawImage(img, (int)(x - pieceSize * 0.5 * imgSizeModifier), (int)(y - pieceSize * 0.5 * imgSizeModifier), (int)(pieceSize * imgSizeModifier), (int)(pieceSize * imgSizeModifier), null);
	}
	
	/**
	 * Draws the outline of a piece.
	 * @param g2 Graphics2D.
	 * @param color the color of the piece's team, PieceColor.
	 * @param x the x coordinate for the center of the piece, double.
	 * @param y the y coordinate for the center of the piece, double.
	 */
	private void drawPieceOutline(Graphics2D g2, Color color, double x, double y) {
		
		int[] xPoints = {(int)(x - pieceSize), (int)(x - pieceSize * 0.5), (int)(x + pieceSize * 0.5), (int)(x + pieceSize),
				(int)(x + pieceSize * 0.5), (int)(x - pieceSize * 0.5)}; //cos(60) = 0.5

		int[] yPoints = {(int)y, (int)(y + pieceSize * Math.sqrt(3)/2), (int)(y + pieceSize * Math.sqrt(3)/2), (int)y,
						(int)(y - pieceSize * Math.sqrt(3)/2), (int)(y - pieceSize * Math.sqrt(3)/2)}; //sin(60) = sqrt(3)/2
		
		g2.setColor(color); 
		g2.setStroke(new BasicStroke(3));
		g2.drawPolygon(xPoints, yPoints, 6);
	}
	
	/**
	 * Outlines the piece that's currently selected in the hive (if visible).
	 * @param g2 Graphics2D.
	 */
	void drawSelectedPiece(Graphics2D g2) {
		if (hive.getSelectedPiece() == null) {
			return;
		}
		
		if (!isVisible(hive.getSelectedPiece())) {
			return;
		}
		
		Point2D.Double boardCoords = modelToBoard(hive.getSelectedPiece().getCoordinates());
		double x = boardCoords.getX();
		double y = boardCoords.getY();
		
		drawPieceOutline(g2, Color.RED, x, y); //Fai che il colore è sceglibile dalle impostazioni
	}
	
	/**
	 * Draws the outlines of the possible new positions for the selected piece.
	 * @param g2 Graphics2D.
	 */
	void drawPossiblePositions(Graphics2D g2) {
		if (hive.getPossiblePositions() == null) {
			return;
		}
		
		for(Placement placement : hive.getPossiblePositions()) {
			double modelX = placement.getNeighbor().getCoordinates().getX() + placement.getPositionOnNeighbor().getXOffset();
			double modelY = placement.getNeighbor().getCoordinates().getY() + placement.getPositionOnNeighbor().getYOffset();
			Point2D.Double modelCoords = new Point2D.Double(modelX, modelY);
			
			Point2D.Double boardCoords = modelToBoard(modelCoords);
			double x = boardCoords.getX();
			double y = boardCoords.getY();
			
			drawPieceOutline(g2, Color.GREEN, x, y); //Fai che il colore è sceglibile dalle impostazioni
		}
	}
	
	/**
	 * Checks whether a piece is set visible or not.
	 * @param piece the piece whose visibility has to be checked, Piece.
	 * @return true if visible, else false, boolean.
	 */
	private boolean isVisible(Piece piece) {
		for( Piece visPiece : visiblePieces) {
			if (piece == visPiece) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Scales the model's coordinates to the view's dimensions.
	 * @param modelCoords the coordinates to be scaled, Point2D.Double.
	 * @return the new values, Point2D.Double.
	 */
	private Point2D.Double modelToBoard(Point2D.Double modelCoords) {
		
		double x = origin.getX() + modelCoords.getX() * pieceSize + positionOffset.getX();
		double y = origin.getY() + modelCoords.getY() * pieceSize + positionOffset.getY();
		
		Point2D.Double boardCoords = new Point2D.Double();
		boardCoords.setLocation(x, y);
		
		return boardCoords;
	}
	
	/**
	 * Scales the view's coordinates to the model's dimensions.
	 * @param boardCoords the coordinates to be scaled, Point2D.Double.
	 * @return the new values, Point2D.Double.
	 */
	private Point2D.Double boardToModel(Point2D.Double boardCoords) {
		
		double x = (boardCoords.getX() - origin.getX() - positionOffset.getX())/pieceSize;
		double y = (boardCoords.getY() - origin.getY() - positionOffset.getY())/pieceSize;
		
		Point2D.Double modelCoords = new Point2D.Double();
		modelCoords.setLocation(x, y);
		
		return modelCoords;
	}
	
	/**
	 * Finds, among the visible pieces, if there's one on the provided coordinates.
	 * @param x the x coordinate of the point, double.
	 * @param y the y coordinate of the point, double.
	 * @return the found piece (or null if there's no piece at the specified coordinates), Piece.
	 */
	Piece getPieceAt(double x, double y) {
		return getPieceAt(x, y, visiblePieces);
	}
	
	/**
	 * Finds, among the offered pieces, if there's one on the provided coordinates.
	 * @param x the x coordinate of the point, double.
	 * @param y the y coordinate of the point, double.
	 * @param pieces the pieces to check, ArrayList <Piece>.
	 * @return the found piece (or null if there's no piece at the specified coordinates), Piece.
	 */
	Piece getPieceAt(double x, double y, ArrayList <Piece> pieces) {
		double thisDistance;
		double smallestDistance = 1;
		Piece piece = null;
		
		Point2D.Double pointCoords = boardToModel(new Point2D.Double(x, y));
		
		if(pieces == null) {
			return null;
		}
		
		for(Piece hex : pieces) {
			thisDistance = Math.sqrt(Math.pow(pointCoords.getX() - hex.getCoordinates().getX(), 2)
									+ Math.pow(pointCoords.getY() - hex.getCoordinates().getY(), 2)); //Distance between the point and the center of the piece
			
			if (thisDistance < smallestDistance) {
				smallestDistance = thisDistance;
				piece = hex;
			}
		}
		
		while (piece != null && piece.getTopPiece() != null) {
			piece = piece.getTopPiece();
		}
		
		return piece;
	}
	
	/**
	 * /**
	 * Finds, among the possible new positions for the selected piece, if there's one at the provided coordinates.
	 * @param x the x coordinate of the point, double.
	 * @param y the y coordinate of the point, double.
	 * @return the found placement (or null if there's no possible position at the specified coordinates), Placement.
	 */
	Placement getPlacementAt(double x, double y) {
		double thisDistance;
		double smallestDistance = 1;
		Placement placFound = null;
		
		Point2D.Double pointCoords = boardToModel(new Point2D.Double(x, y));
		
		if(hive.getPossiblePositions() == null) {
			return null;
		}
		
		for(Placement placement : hive.getPossiblePositions()) {
			double placementX = placement.getNeighbor().getCoordinates().getX() + placement.getPositionOnNeighbor().getXOffset();
			double placementY = placement.getNeighbor().getCoordinates().getY() + placement.getPositionOnNeighbor().getYOffset();
			
			thisDistance = Math.sqrt(Math.pow(pointCoords.getX() - placementX, 2)
					+ Math.pow(pointCoords.getY() - placementY, 2)); //Distance between the point and the center of the placement
			
			if (thisDistance < smallestDistance) {
				smallestDistance = thisDistance;
				placFound = placement;
			}
		}
		return placFound;
	}
	
	
	
	//Events that are generated from this component to be handled by this component (the view tells itself to modify itself with events,
	//by firing events that are then re-caught by itself)
	
	double xVariation;
	double yVariation;
	//Dragging the mouse around will let the player move the view around the game board.
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDrag = true;
		xVariation = e.getPoint().getX() - mousePosition.getX();
		yVariation = e.getPoint().getY() - mousePosition.getY();
		
		positionOffset.setLocation(positionOffset.getX() + xVariation, positionOffset.getY() + yVariation);
		
		mousePosition = e.getPoint();
		this.repaint();
	}

	//Tracks the mouse movement
	@Override
	public void mouseMoved(MouseEvent e) {
		mousePosition = e.getPoint(); //x and y
		//We need to force the component's redraw when the mouse is moved (it won't do it on his own by default because it's costly)
		this.repaint();
	}

	//Clicking the mouse will select/deselect the game elements (pieces, positions...)
	@Override
	public void mouseClicked(MouseEvent e) {
		mousePosition = e.getPoint(); //x and y
		
		Piece clickedPiece = getPieceAt(mousePosition.getX(), mousePosition.getY());
		Placement clickedPlacement = getPlacementAt(mousePosition.getX(), mousePosition.getY());
		
		
		if (clickedPlacement != null) {
			fireActionListener(new ActionEvent(clickedPlacement, ActionEvent.ACTION_PERFORMED, "position_selected", e.getWhen(), e.getModifiersEx()));
			//Maybe it would be better for the code's integrity (respecting objects paradigm) to let THIS be the source of the event and then
			//in the controller get clickedPlacement from this as the source (need to make a getLastClickedPlacement() method)
			//Same for clickedPiece
		}
		else {
			if (clickedPiece == null || clickedPiece == hive.getSelectedPiece()) {
				fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "no_piece_selected", e.getWhen(), e.getModifiersEx()));
			}
			else {
				//show the possible moves for the selected piece by firing an action event to tell the controller
				//to make the model calculate the possible moves
				fireActionListener(new ActionEvent(clickedPiece, ActionEvent.ACTION_PERFORMED, "show_possible_positions", e.getWhen(), e.getModifiersEx()));	
			}
		}
		
		fireValuesChange(new ChangeEvent(this));	
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

	//Scrolling the mouse wheel will zoom in and out the game board.
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getUnitsToScroll() < 0) {
			pieceSizeModifier += wheelSensitivity;
		} else {
			pieceSizeModifier -= wheelSensitivity;
		}
		
		if (pieceSizeModifier < MIN_SIZE_MODIFIER) {
			pieceSizeModifier = MIN_SIZE_MODIFIER;
		} else if (pieceSizeModifier > MAX_SIZE_MODIFIER) {
			pieceSizeModifier = MAX_SIZE_MODIFIER;
		}
		this.repaint();
	}
}

