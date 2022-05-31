package it.unibs.pajc.nieels.hive;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Placement;
import it.unibs.pajc.nieels.hive.Piece.Side;

//VIEW
public abstract class HexField extends EventJComponent implements MouseMotionListener, MouseListener, MouseWheelListener {

	Hive hive; //The model
	ArrayList <Piece> visiblePieces = new ArrayList();
	ArrayList <Piece> allPieces = new ArrayList();
	
	Color backgroundColor; //FARE CHE È SCEGLIBILE DAL MENU OPZIONI DI GIOCO
	
	int height; //Putting them as class attributes will make them accessible to all class methods (not only paint component)
	int width;
	Point2D.Double origin = new Point2D.Double();
	
	//Piece selectedPiece;
	
	double pieceSize;
	double pieceSizeModifier; //PRENDERE QUESTO DA UN VALORE SETTATO NEL MENU DI OPZIONI DEL GIOCO (compreso tra MAX_SIZE_MODIFIER e MIN)
	double MIN_SIZE_MODIFIER = 0.07;
	double MAX_SIZE_MODIFIER = 0.25;
	
	double wheelSensitivity;
	public final double MIN_WHEEL_SENSITIVITY = 0.01;
	public final double MAX_WHEEL_SENSITIVITY = 0.1;
	
	HashMap <String, Image> pieceImgs = new HashMap();
	double imgSizeModifier; //PRENDERE QUESTO DA UN VALORE SETTATO NEL MENU col metodo load Settings
	
	Point mousePosition = new Point(0, 0); //we memorize it as a global variable so that I can access this data from all the class, included the paintComponent method  (so i can use these coordinates to draw something)
	Point positionOffset = new Point(0, 0);
	boolean mouseDrag = false;
	
	
	//We want to define a constructor which takes in the events listener that we'll be using to interact with the mouse
	public HexField() {
		super();
		
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addMouseWheelListener(this);

		visiblePieces = allPieces;
		
		loadSettings();
		
		this.setBackground(backgroundColor);
	}
	
	private void loadSettings() {
		backgroundColor = new Color(255, 255, 200);
		pieceSizeModifier = MAX_SIZE_MODIFIER;
		imgSizeModifier = 1.3; //PRENDERE QUESTO DA UN VALORE SETTATO NEL MENU col metodo load Settings
		wheelSensitivity = 0.03;
	}
	
	/**
	 * Returns the model that's being used for game information by the view.
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
		
		loadImages();
	}
	
	private void loadImages() {
		String directory;
		
		pieceImgs.clear();
		
		for (Piece piece : allPieces) {
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
		//scrivi coordinate mouse in alto a sx schermo
		g2.setColor(Color.RED);
		g2.drawString(mousePosition.getX() + " " + mousePosition.getX(), 10, 10);
		
		g2.setColor(Color.BLUE);
		g2.drawString(positionOffset.getX() + " " + positionOffset.getX(), 10, 30);
		*/
	}
	
	
	void drawBoard(Graphics2D g2) {
		g2.setColor(this.getBackground());
		g2.fillRect(0, 0, width, height);	
	}
	
	
	void drawCursor(Graphics2D g2) {
		if (mousePosition != null) {
			//red pointer
			g2.setColor(Color.RED);
			g2.fillOval(mousePosition.x, mousePosition.y, 5, 5);
		}
	}
	
	
	void drawVisiblePieces(Graphics2D g2) {
		drawPieces(g2, visiblePieces);
	}
	
	
	void drawPieces(Graphics2D g2, ArrayList <Piece> pieces) {
		Point2D.Double boardCoords;
		Image img;
		Color color;//lo prende dal file di impostazioni grafiche
		
		//g2.translate(width/2, height/2);
		
		for (Piece piece : pieces) {
			/*
			x = piece.getX() * pieceSize + positionOffset.getX() + width/2.0;
			y = piece.getY() * pieceSize + positionOffset.getY() + height/2.0;
			*/
			boardCoords = modelToBoard(piece.getCoordinates());
			
			img = pieceImgs.get(piece.getName());
			
			if (piece.getColor() == PieceColor.WHITE) {
				color = Color.GRAY;
			} else {
				color = Color.BLACK;
			}
			drawPiece(g2, color, boardCoords.getX(), boardCoords.getY(), img);
		}
	}
	
	private void drawPiece(Graphics2D g2, Color color, double x, double y, Image img) {
		int[] xPoints = {(int)(x - pieceSize), (int)(x - pieceSize * 0.5), (int)(x + pieceSize * 0.5), (int)(x + pieceSize),
						(int)(x + pieceSize * 0.5), (int)(x - pieceSize * 0.5)}; //cos(60) = 0.5
		
		int[] yPoints = {(int)y, (int)(y + pieceSize * Math.sqrt(3)/2), (int)(y + pieceSize * Math.sqrt(3)/2), (int)y,
						(int)(y - pieceSize * Math.sqrt(3)/2), (int)(y - pieceSize * Math.sqrt(3)/2)}; //sin(60) = sqrt(3)/2
		
		g2.setColor(color);
		g2.fillPolygon(xPoints, yPoints, 6);
		g2.drawImage(img, (int)(x - pieceSize * 0.5 * imgSizeModifier), (int)(y - pieceSize * 0.5 * imgSizeModifier), (int)(pieceSize * imgSizeModifier), (int)(pieceSize * imgSizeModifier), null);
	}
	
	
	private void drawPieceOutline(Graphics2D g2, Color color, double x, double y) {
		
		int[] xPoints = {(int)(x - pieceSize), (int)(x - pieceSize * 0.5), (int)(x + pieceSize * 0.5), (int)(x + pieceSize),
				(int)(x + pieceSize * 0.5), (int)(x - pieceSize * 0.5)}; //cos(60) = 0.5

		int[] yPoints = {(int)y, (int)(y + pieceSize * Math.sqrt(3)/2), (int)(y + pieceSize * Math.sqrt(3)/2), (int)y,
						(int)(y - pieceSize * Math.sqrt(3)/2), (int)(y - pieceSize * Math.sqrt(3)/2)}; //sin(60) = sqrt(3)/2
		
		g2.setColor(color); 
		g2.setStroke(new BasicStroke(3));
		g2.drawPolygon(xPoints, yPoints, 6);
	}
	
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
	
	private boolean isVisible(Piece piece) {
		for( Piece visPiece : visiblePieces) {
			if (piece == visPiece) {
				return true;
			}
		}
		return false;
	}

	private Point2D.Double modelToBoard(Point2D.Double modelCoords) {
		
		double x = origin.getX() + modelCoords.getX() * pieceSize + positionOffset.getX();
		double y = origin.getY() + modelCoords.getY() * pieceSize + positionOffset.getY();
		
		Point2D.Double boardCoords = new Point2D.Double();
		boardCoords.setLocation(x, y);
		
		return boardCoords;
	}
	
	private Point2D.Double boardToModel(Point2D.Double boardCoords) {
		
		double x = (boardCoords.getX() - origin.getX() - positionOffset.getX())/pieceSize;
		double y = (boardCoords.getY() - origin.getY() - positionOffset.getY())/pieceSize;
		
		Point2D.Double modelCoords = new Point2D.Double();
		modelCoords.setLocation(x, y);
		
		return modelCoords;
	}
	
	Piece getPieceAt(double x, double y) {
		return getPieceAt(x, y, visiblePieces);
	}
	
	
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
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDrag = true;
		xVariation = e.getPoint().getX() - mousePosition.getX();
		yVariation = e.getPoint().getY() - mousePosition.getY();
		
		positionOffset.setLocation(positionOffset.getX() + xVariation, positionOffset.getY() + yVariation);
		/*
		if(Math.abs(xVariation) > 2 || Math.abs(yVariation) > 2) {
			positionOffset.setLocation(positionOffset.getX() + xVariation, positionOffset.getY() + yVariation);
		}
		*/
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
		
		mousePosition = e.getPoint(); //x and y
		
		Piece clickedPiece = getPieceAt(mousePosition.getX(), mousePosition.getY());
		Placement clickedPlacement = getPlacementAt(mousePosition.getX(), mousePosition.getY());
		
		
		if (clickedPlacement != null) {
			actionPerformed(new ActionEvent(clickedPlacement, 2, "position_selected"));
		}
		else {
			if (clickedPiece == null || clickedPiece == hive.getSelectedPiece()) {
				actionPerformed(new ActionEvent(this, 0, "no_piece_selected"));
			}
			else {
				//presenta delle possibili mosse; fire action event per dire al crontroller di far fare al model il calcolo dei possibili movimenti
				actionPerformed(new ActionEvent(clickedPiece, 1, "show_possible_positions"));	
			}
		}
		
		fireValuesChange(new ChangeEvent(this));
		
//		if(hive.getSelectedPiece() == null) {
//			hive.setSelectedPiece(clickedPiece); //I can also do this using an event, see below with the possible movements
//			
//			if (hive.getSelectedPiece() != null) {
//				//presenta delle possibili mosse; fire action event per dire al crontroller di far fare al model il calcolo dei possibili movimenti
//				actionPerformed(new ActionEvent(clickedPiece, 1, "possible_moves"));
//			}
//			fireValuesChange(new ChangeEvent(this));
//			
//		} else {
//			if(hive.getSelectedPiece() == clickedPiece) {
//				hive.setSelectedPiece(null);
//				fireValuesChange(new ChangeEvent(this));
//			} else if(/*è una delle possibili mosse presentate*/false) {
//			//////moves logic
//			} else {
//				hive.setSelectedPiece(clickedPiece);
//				fireValuesChange(new ChangeEvent(this));
//			}	

	
		
		
		
		
		
		
//		if(hive.getSelectedPiece() == clickedPiece) {
//			hive.setSelectedPiece(null);
//		} else {
//			hive.setSelectedPiece(clickedPiece); //I can also do this using an event, see below with the possible movements
//			
//			if (hive.getSelectedPiece() != null) {
//				//presenta delle possibili mosse; fire action event per dire al crontroller di far fare al model il calcolo dei possibili movimenti
//				actionPerformed(new ActionEvent(clickedPiece, 1, "possible_moves"));
//			}
//		}
//		fireValuesChange(new ChangeEvent(this));
//		
//		repaint();

		
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

