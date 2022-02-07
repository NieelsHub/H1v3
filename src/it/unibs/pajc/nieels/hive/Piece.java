package it.unibs.pajc.nieels.hive;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import it.unibs.pajc.nieels.hive.Piece.Side;

import java.util.Set;


public abstract class Piece {

	enum PieceColor { BLACK, WHITE };
	
	/**
	 * Represents the sides of a piece (hexagonal) by the relative direction in which they face.
	 * Contains methods to fetch, for each face, its opposite one and also the previous and next one
	 * following the piece's sides in a clockwise motion.
	 */
	enum Side { 
		NORTH (0.0, -1.0) { 
			public Side previous() {return this.NORTHWEST;}
			public Side next() {return this.NORTHEAST;}
			public Side opposite() {return this.SOUTH;}
			},
		NORTHEAST (1.0, -0.5) { 
			public Side previous() {return this.NORTH;}
			public Side next() {return this.SOUTHEAST;}
			public Side opposite() {return this.SOUTHWEST;}
			},
		SOUTHEAST (1.0, 0.5) { 
			public Side previous() {return this.NORTHEAST;}
			public Side next() {return this.SOUTH;}
			public Side opposite() {return this.NORTHWEST;}
			},
		SOUTH (0.0, 1.0) { 
			public Side previous() {return this.SOUTHEAST;}
			public Side next() {return this.SOUTHWEST;}
			public Side opposite() {return this.NORTH;}
			},
		SOUTHWEST (-1.0, 0.5) { 
			public Side previous() {return this.SOUTH;}
			public Side next() {return this.NORTHWEST;}
			public Side opposite() {return this.NORTHEAST;}
			},
		NORTHWEST (-1.0, -0.5) { 
			public Side previous() {return this.SOUTHWEST;}
			public Side next() {return this.NORTH;}
			public Side opposite() {return this.SOUTHEAST;}
			};
		
		//Position offsets (relative to the starting piece) if we move from it to the next piece through a specific side
		double xOffset;
		double yOffset;
		
		//Constructor
		private Side (double xOffset, double yOffset) {
			this.xOffset = xOffset;
			this.yOffset = yOffset;
		}
		
		public double getXOffset() {
			return xOffset;
		}
		
		public double getYOffset() {
			return yOffset;
		}
		
		//Abstract Enum methods which return previous and next side (relative to a clockwise movement)
		public abstract Side previous();
		public abstract Side next();
		//Abstract Enum method that returns the side which is opposite to the current one
		public abstract Side opposite();
	};
	
	//Class level counter used to give each created piece an unique id
	private static int generatedPieces;
	
	private PieceColor color;
	private int id;
	private String name;
	private Double x = null; //Piece coordinates (relative to the first piece placed in game)
	private Double y = null;
	//A piece knows for each of its sides if other pieces are linked to it
	private HashMap <Side, Piece> linkedPieces = new HashMap();
	
	
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
	
	public Double getX() {
		return x;
	}
	
	public Double getY() {
		return y;
	}

	public HashMap <Side, Piece> getLinkedPieces() {
		return linkedPieces;
	}
	
	@Override
	public String toString() {
		String string = this.name + " " + this.color + "-" + this.id + String.format(" (%.1f ; %.1f)", this.x, this.y);
		Set<Entry<Side, Piece>> links = this.linkedPieces.entrySet();
		
		for (Entry<Side, Piece> link : links) {
			Piece value = link.getValue();
			string += "\n\t" + link.getKey() + " linked to " + value.getName() + " " + value.getColor()
				+ "-" + value.id + String.format(" (%.1f ; %.1f)", value.x, value.y);
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

	//Positions the first piece of the game and sets the games' coordinate system at its center
	public void setAsFirst() {
		if(this.x != null || this.y != null) { //AL POSTO DEL RETURN FARE CHE TIRA L'ECCEZIONE SE LE COORDINATE NON SONO NULL
			return;
		}
		this.x = 0.0;
		this.y = 0.0;
	}
	
	//links this piece to the specified one on the specified side and vice versa (no controls on previously existent links)
	public void linkTo(Piece neighbor, Side positionOnNeighbor) {
		//Link this piece to the neighbor
		neighbor.getLinkedPieces().put(positionOnNeighbor, this);
		//Link the neighbor to this piece
		this.linkedPieces.put(positionOnNeighbor.opposite(), neighbor);
	}
	
	//Set a new position for this piece, relative to another piece's specified side (no controls on previously existent links)
	public void setRelativePosition(Piece neighbor, Side positionOnNeighbor) {
		//Set this piece's new coordinates calculated in relation to the ones of the piece near which this piece has been placed 
		this.x = neighbor.getX() + positionOnNeighbor.xOffset;
		this.y = neighbor.getY() + positionOnNeighbor.yOffset;
	}
	
	public void removeAllLinks() {
		ArrayList<Side> linkedSides = new ArrayList<Side>(this.linkedPieces.keySet());
		
		Piece linkedPiece;
		for(Side side : linkedSides) {
			linkedPiece = this.linkedPieces.get(side);
			linkedPiece.getLinkedPieces().remove(side.opposite());
			
			this.linkedPieces.remove(side);
		}
		
		this.x = null;
		this.y = null;
	}
	
	//Abstract methods - each kind of piece will implement its own movement logic
	public abstract void move(Side direction);
	
	public abstract ArrayList<Side> getPossibleDirections();

}
