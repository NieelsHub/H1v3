package it.unibs.pajc.nieels.hive;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

//MODEL

/**
 * Modelization of a game piece's common characteristics, this is an abstract class, each piece type will
 * need further implementation of its specific behavior in other designated classes.
 * @author Nicol Stocchetti (NieelsHub)
 *
 */
public abstract class Piece {

	/**
	 * Each piece can be of two possible colors.
	 */
	enum PieceColor { BLACK, WHITE };
	
	/**
	 * This enum works as a mean to represent the six sides of a piece (hexagonal) by the relative direction in which they face.
	 * It contains methods to fetch, for each face, its opposite one and also the previous and next ones
	 * following the piece's sides in a clockwise motion.
	 */
	enum Side { 
		NORTH (0.0, -Math.sqrt(3)) { //Scaled on actual hexagon proportions
			public Side previous() {return this.NORTHWEST;}
			public Side next() {return this.NORTHEAST;}
			public Side opposite() {return this.SOUTH;}
			},
		NORTHEAST (3.0/2, -Math.sqrt(3)/2) { 
			public Side previous() {return this.NORTH;}
			public Side next() {return this.SOUTHEAST;}
			public Side opposite() {return this.SOUTHWEST;}
			},
		SOUTHEAST (3.0/2, Math.sqrt(3)/2) { 
			public Side previous() {return this.NORTHEAST;}
			public Side next() {return this.SOUTH;}
			public Side opposite() {return this.NORTHWEST;}
			},
		SOUTH (0.0, Math.sqrt(3)) { 
			public Side previous() {return this.SOUTHEAST;}
			public Side next() {return this.SOUTHWEST;}
			public Side opposite() {return this.NORTH;}
			},
		SOUTHWEST (-3.0/2, Math.sqrt(3)/2) { 
			public Side previous() {return this.SOUTH;}
			public Side next() {return this.NORTHWEST;}
			public Side opposite() {return this.NORTHEAST;}
			},
		NORTHWEST (-3.0/2, -Math.sqrt(3)/2) { 
			public Side previous() {return this.SOUTHWEST;}
			public Side next() {return this.NORTH;}
			public Side opposite() {return this.SOUTHEAST;}
			};
		
		//Position offsets that would be gained by moving from the current piece's position to another piece
		//in the direction of each specific side of this piece.
		//The center (0;0) of the coordinates is set where the first piece of the game has been placed.
		double xOffset;
		double yOffset;
		
		/**
		 * 
		 * @param xOffset the horizontal position offset that would be gained by moving from the current piece's position
		 * to another piece in the direction of each specific side of this piece (the center (0;0) of the coordinates is set
		 * where the first piece of the game has been placed), double.
		 * @param yOffset the vertical position offset that would be gained by moving from the current piece's position
		 * to another piece in the direction of each specific side of this piece (the center (0;0) of the coordinates is set
		 * where the first piece of the game has been placed), double.
		 */
		private Side (double xOffset, double yOffset) {
			this.xOffset = xOffset;
			this.yOffset = yOffset;
		}
		
		/**
		 * 
		 * @return the horizontal position offset that would be gained by moving from the current piece's position
		 * to another piece through this side, double.
		 */
		public double getXOffset() {
			return xOffset;
		}
		
		/**
		 * 
		 * @return the vertical position offset that would be gained by moving from the current piece's position
		 * to another piece through this side, double.
		 */
		public double getYOffset() {
			return yOffset;
		}
		
		//Abstract Enum methods which return previous and next side (relative to a clockwise movement)
		/**
		 * @return the side that comes before this one following a clockwise direction, Side.
		 */
		public abstract Side previous();
		
		/**
		 * @return the side that comes after this one following a clockwise direction, Side.
		 */
		public abstract Side next();
		
		//Abstract Enum method that returns the side which is opposite to the current one
		/**
		 * @return the side opposite to this side, Side.
		 */
		public abstract Side opposite();
	};
	
	//Class level counter used to give each created piece an unique id
	private static int generatedPieces;
	
	private PieceColor color;
	private int id;
	private String name;
	private Point2D.Double coordinates = new Point2D.Double(); //Piece coordinates (relative to the first piece placed in game)
	//private boolean inGame = false;
	//A piece knows for each of its sides if other pieces are linked to it
	private HashMap <Side, Piece> linkedPieces = new HashMap();
	
	/**
	 * The main piece constructor
	 * @param color the piece's color
	 * @param name the piece's name
	 */
	public Piece (PieceColor color, String name) {
		this.color = color;
		this.name = name;
		this.id = generatedPieces++;
	}

	public int getId() {
		return id;
	}
	
	public PieceColor getColor() {
		return color;
	}
	
	public String getName() {
		return name;
	}

	public Point2D.Double getCoordinates() {
		return coordinates;
	}
	
/*
	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
*/
	/**
	 * 
	 * @return all the pieces linked to this piece and by which side, HashMap <Side, Piece>.
	 */
	public HashMap <Side, Piece> getLinkedPieces() {
		return linkedPieces;
	}
	
	@Override
	public String toString() {
		String string = this.name + " " + this.color + "-" + this.id + String.format(" (%.1f ; %.1f)", this.coordinates.getX(), this.coordinates.getY());
		Set<Entry<Side, Piece>> links = this.linkedPieces.entrySet();
		/*
		if(this.inGame) {
			string += " IN GAME";
		}
		*/
		for (Entry<Side, Piece> link : links) {
			Piece value = link.getValue();
			string += "\n\t" + link.getKey() + " linked to " + value.getName() + " " + value.getColor()
				+ "-" + value.id + String.format(" (%.1f ; %.1f)", value.coordinates.getX(), value.coordinates.getY());
		}		
		return string;
	}
	
	/**
	 * Checks if this piece has another piece linked to it on the specified side.
	 * @param side, the face on which we want to check for linked pieces, Side.
	 * @return the linked piece (or null if there isn't a piece linked to the chosen side), Piece.
	 */
	public Piece checkLink(Side side) {
		return linkedPieces.get(side);
	}
		
	/**
	 * Sets new position coordinates for this piece, calculated form another piece's position adding the offset
	 * of the movement from it through the specified side (no controls on previously existent links, no update
	 * of the pieces' links or inGame status)
	 * @param neighbor the piece from which to calculate the new coordinates, Piece.
	 * @param positionOnNeighbor the side on the landmark piece from where to calculate the position offset, Side.
	 */
	public void setRelativeCoordinates(Piece neighbor, Side positionOnNeighbor) {
		//Set this piece's new coordinates calculated in relation to the ones of the piece near which this piece has been placed 
		double x = neighbor.getCoordinates().getX() + positionOnNeighbor.xOffset;
		double y = neighbor.getCoordinates().getY() + positionOnNeighbor.yOffset;
		
		this.coordinates.setLocation(x, y);
	}
	
	/**
	 * Creates a link between this piece and the other specified piece on its specified side (no controls
	 * on previously existent links, no update of the global coordinates of the pieces or their inGame status).
	 * @param neighbor the other piece to link, Piece.
	 * @param positionOnNeighbor the side on the new piece where to link this piece, Side.
	 */
	public void linkTo(Piece neighbor, Side positionOnNeighbor) {
		//Link this piece to the neighbor
		neighbor.getLinkedPieces().put(positionOnNeighbor, this);
		//Link the neighbor to this piece
		this.linkedPieces.put(positionOnNeighbor.opposite(), neighbor);
	}
	
	/**
	 * Sets the inGame property to false and removes all links with other pieces.
	 */
	public void resetPosition() {
		//remove all links
		ArrayList<Side> linkedSides = new ArrayList<Side>(this.linkedPieces.keySet());
		
		Piece linkedPiece;
		for(Side side : linkedSides) {
			linkedPiece = this.linkedPieces.get(side);
			linkedPiece.getLinkedPieces().remove(side.opposite()); //removes this piece from the other piece's linked pieces list
			
			this.linkedPieces.remove(side); //removes this piece's link to the other piece
		}
		
		//this.inGame = false;
	}
	
	/**
	 * Sets the position coordinates to the specified values and removes all links with other pieces.
	 *
	public void resetPosition(Double x, Double y) {
		//remove all links
		ArrayList<Side> linkedSides = new ArrayList<Side>(this.linkedPieces.keySet());
		
		Piece linkedPiece;
		for(Side side : linkedSides) {
			linkedPiece = this.linkedPieces.get(side);
			linkedPiece.getLinkedPieces().remove(side.opposite()); //removes this piece from the other piece's linked pieces list
			
			this.linkedPieces.remove(side); //removes this piece's link to the other piece
		}
		//reset coordinates
		this.x = x;
		this.y = y;
		
		this.inGame = false;
	}
	*/
	
	//Abstract methods - each kind of piece will implement its own movement logic
	public abstract void move(Side direction);
	
	public abstract ArrayList<Side> getPossibleDirections();

}
