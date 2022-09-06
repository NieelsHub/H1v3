package it.nieels.unibs.pajc.h1v3.model;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import it.nieels.unibs.pajc.h1v3.model.Piece.PieceColor;

import java.util.Set;

//MODEL

/**
 * Modelization of a game piece's common characteristics, this is an abstract class, each piece type will
 * need further implementation of its specific behavior in other designated classes.
 * @author Nicol Stocchetti (NieelsHub)
 *
 */
public abstract class Piece implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Each piece can be of two possible colors.
	 */
	public enum PieceColor { BLACK, WHITE };
	
	/**
	 * This enum works as a mean to represent the six sides of a piece (hexagonal) by the relative direction in which they face.
	 * It contains methods to fetch, for each face, its opposite one and also the previous and next ones
	 * following the piece's sides in a clockwise motion.
	 */
	public enum Side { 
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
		 * The enum constructor.
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
		 * Gets the horizontal position offset that would be gained by moving from the current piece's position
		 * to another piece through this side.
		 * @return the horizontal position offset, double.
		 */
		public double getXOffset() {
			return xOffset;
		}
		
		/**
		 * Gets the vertical position offset that would be gained by moving from the current piece's position
		 * to another piece through this side
		 * @return the vertical position offset, double.
		 */
		public double getYOffset() {
			return yOffset;
		}
		
		//Abstract Enum methods which return previous and next side (relative to a clockwise movement)
		/**
		 * Returns the side that comes before this one following a clockwise direction.
		 * @return the side, Side.
		 */
		public abstract Side previous();
		
		/**
		 * Returns the side that comes after this one following a clockwise direction.
		 * @return the side, Side.
		 */
		public abstract Side next();
		
		//Abstract Enum method that returns the side which is opposite to the current one
		/**
		 * Returns the side opposite to this side.
		 * @return the side, Side.
		 */
		public abstract Side opposite();
	};
	
	//Class level counter used to give each created piece an unique id
	private static int generatedPieces;
	
	private PieceColor color;
	private int id;
	private String name;
	private boolean verticalMovement;
	private Point2D.Double coordinates = new Point2D.Double(); //Piece coordinates (relative to the first piece placed in game)
	//private boolean inGame = false;
	//A piece knows for each of its sides if other pieces are linked to it
	private HashMap <Side, Piece> linkedPieces = new HashMap();
	private Piece topPiece;
	private Piece bottomPiece;
	
	/**
	 * The main piece constructor
	 * @param color the piece's color, PieceColor.
	 * @param verticalMovement whether the piece is able to move vertically or not, boolean.
	 * @param name the piece's name, String.
	 */
	public Piece (PieceColor color, boolean verticalMovement, String name) {
		this.color = color;
		this.verticalMovement = verticalMovement;
		this.name = name;
		this.topPiece = null;
		this.bottomPiece = null;
		this.id = generatedPieces++;
	}

	/**
	 * Returns the piece's unique id.
	 * @return the id, int.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 *  Returns the piece's color.
	 * @return the color, PieceColor.
	 */
	public PieceColor getColor() {
		return color;
	}
	
	/**
	 *  Returns the piece's name.
	 * @return the name, String.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the piece's coordinates.
	 * @return the coordinates, Point2D.Double.
	 */
	public Point2D.Double getCoordinates() {
		return coordinates;
	}
	
	/**
	 * Returns the piece on the top of this piece.
	 * @return the top piece (null if there's no top piece), Piece.
	 */
	public Piece getTopPiece() {
		return topPiece;
	}

	/**
	 * Puts a piece on top of this piece.
	 * @param topPiece the piece to put on top (null if there's no top piece), Piece.
	 */
	public void setTopPiece(Piece topPiece) {
		this.topPiece = topPiece;
	}

	/**
	 * Returns the piece under this piece.
	 * @return the bottom piece (null if there's no bottom piece), Piece.
	 */
	public Piece getBottomPiece() {
		return bottomPiece;
	}

	/**
	 * Puts a piece under this piece.
	 * @param bottomPiece the piece to put under (null if there's no bottom piece), Piece.
	 */
	public void setBottomPiece(Piece bottomPiece) {
		this.bottomPiece = bottomPiece;
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
	 * Returns the pieces linked to the sides of this piece.
	 * @return all the pieces linked to this piece and by which side, HashMap <Side, Piece>.
	 */
	public HashMap <Side, Piece> getLinkedPieces() {
		return linkedPieces;
	}
	
	/**
	 * Tells if the piece can perform vertical movements.
	 * @return true if the piece can move vertically, else false, boolean.
	 */
	public boolean isVerticalMovement() {
		return verticalMovement;
	}

	/**
	 * Sets a meaningless id, only to be used by dummy pieces.
	 */
	protected void setDummyId() {
		if (this instanceof DummyPiece) {
			this.id = -1;
		}
	}
	
	@Override
	public String toString() {
		return this.name + " " + this.color + "-" + this.id + String.format(" (%.1f ; %.1f)", this.coordinates.getX(), this.coordinates.getY());
	}
	
	/**
	 * A verbose description of the piece (more detailed than the usual toString()).
	 * @return a description of this piece, String.
	 */
	public String toStringLong() {
		String string = toString();
		Set<Entry<Side, Piece>> links = this.linkedPieces.entrySet();
		/*
		if(this.inGame) {
			string += " IN GAME";
		}
		*/
		for (Entry<Side, Piece> link : links) {
			Piece value = link.getValue();
			string += "\n\t" + link.getKey() + " linked to " + value.toString();
		}
		
		string += "\n\tTOPPING: " + this.bottomPiece + "\n\tTOPPED BY: " + this.topPiece;
		
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
	 * Checks if the piece is not surrounded enough and is still able to move (to be able to move
	 * the piece must not be under another piece and have at least 2 free consecutive sides).
	 * @return true if the piece can move, else false.
	 */
	public boolean canMove() {
		if (topPiece != null) {
			return false;
		}
		
		for (Side side : Side.values()) {
			if (checkLink(side) == null && checkLink(side.next()) == null) {
				return true;
			}
		}
		return verticalMovement;
	}
		
	/**
	 * Sets new position coordinates for this piece, calculated form another piece's position adding the offset
	 * of the movement from it through the specified side (no controls on previously existent links, no update
	 * of the pieces' links).
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
	 * on previously existent links, no update of the global coordinates of the pieces).
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
	 * Removes all links with other pieces.
	 */
	public void resetLinks() {
		//remove all links
		ArrayList<Side> linkedSides = new ArrayList<Side>(this.linkedPieces.keySet());
		
		Piece linkedPiece;
		for(Side side : linkedSides) {
			linkedPiece = this.linkedPieces.get(side);
			linkedPiece.getLinkedPieces().remove(side.opposite()); //removes this piece from the other piece's linked pieces list
			
			this.linkedPieces.remove(side); //removes this piece's link to the other piece
		}
		
		if (this.topPiece != null) {
			this.topPiece.bottomPiece = this.bottomPiece;
		}
		if (this.bottomPiece != null) {
			this.bottomPiece.topPiece = this.topPiece;
		}
		this.topPiece = null;
		this.bottomPiece = null;
		//this.inGame = false;
	}
	
	/**
	 * Sets the position coordinates to the specified arbitrary values (no controls on other pieces' coordinates or previously
	 * existent links, no update of the pieces' links).
	 * @param x the x coordinate, Double.
	 * @param y the y coordinate, Double.
	 */
	public void resetPositionCoords(Double x, Double y) {
		//reset coordinates
		this.coordinates.x = x;
		this.coordinates.y = y;
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
	
	/**
	 * Creates a perfect copy of this piece, only without links to other pieces.
	 * @return a copy of this piece, Piece.
	 */
	public Piece copy() {
		Piece copy = null;
		Class<?> pieceKind;
		
		pieceKind = this.getClass();
		
		try {
			Constructor<?> constructor =  pieceKind.getConstructor(PieceColor.class);//PieceColor.class returns the Class object associated to the type PieceColor.
			copy = (Piece)constructor.newInstance(this.color);//verticalMovement and name are automatically taken care of by this constructor
		}
		catch(NoSuchMethodException e) {
			System.err.println("The specified constructor doesn't exist!");
			e.printStackTrace();
		}
		catch(Exception e) {
			System.err.println("Unknown error.");
			e.printStackTrace();
		}
		
		copy.coordinates = this.coordinates;
		copy.id = this.id;
		
		return copy;
	}
	
	
	//Abstract methods - each kind of piece will implement its own movement logic
	
	public abstract ArrayList<Placement> calcPossibleMoves();
	
	
	//Static inner class
	/**
	 * Describes the possible positioning of a piece on a particular side of another piece.
	 * @author Nicol Stocchetti
	 *
	 */
	public static class Placement implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		private Piece neighbor;
		private Side positionOnNeighbor;
		
		/**
		 * The constructor.
		 * @param neighbor the piece to which the positioning is relative, Piece.
		 * @param side the side of the neighbor on which to possibly position new pieces, Side.
		 */
		public Placement(Piece neighbor, Side side) {
			this.neighbor = neighbor;
			this.positionOnNeighbor = side;
		}
		
		public Piece getNeighbor() {
			return neighbor;
		}
		public void setNeighbor(Piece neighbor) {
			this.neighbor = neighbor;
		}
		public Side getPositionOnNeighbor() {
			return positionOnNeighbor;
		}
		public void setPositionOnNeighbor(Side positionOnNeighbor) {
			this.positionOnNeighbor = positionOnNeighbor;
		}

		@Override
		public String toString() {
			return "(" + neighbor.getName() + " " + neighbor.getColor() + "-" + neighbor.getId() + " on " + positionOnNeighbor + ")";
		}
		
	}

}
