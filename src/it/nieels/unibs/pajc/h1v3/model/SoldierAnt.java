package it.nieels.unibs.pajc.h1v3.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Queue;

/**
 * Implements the logic of the SoldierAnt: the quickest and farthest moving piece.
 * @author Nicol Stocchetti
 *
 */
public class SoldierAnt extends Piece {

	public final static String PIECE_NAME = "SOLDIER_ANT";
	public final static boolean VERTICAL_MOVEMENT = false;

	/**
	 * The constructor, it automatically assigns the piece name and the capability to move vertically to his super Piece's attributes.
	 * @param color the color of this piece's team, PieceColor.
	 */
	public SoldierAnt(PieceColor color) {
		super(color, VERTICAL_MOVEMENT, PIECE_NAME);
	}
	
	/**
	 * Another constructor, to be used for calls by the constructors of subclasses.
	 * @param color the color of this piece's team, PieceColor.
	 * @param verticalMovement whether the piece is capable of vertical movement, boolean.
	 * @param pieceName the name of the piece type, String.
	 */
	SoldierAnt(PieceColor color, boolean verticalMovement, String pieceName) {
		super(color, verticalMovement, pieceName);
	}
	
	@Override
	public ArrayList<Placement> calcPossibleMoves() {
		ArrayList<Placement> placements;
		ArrayList<Piece> hive;
		ArrayList<Piece> hiveCopy;
		ArrayList<Piece> hiveSurroundings;
		
		//Using a BFS like algorithm to find all the hive's pieces
		hive = hiveMappingBFS(this);
		//Need to copy the actual hive to avoid working on actual pieces, thus generating side effects in the real hive
		hiveCopy = copyHive(hive);
		hiveSurroundings = generateHiveSurroundings(hiveCopy);
		placements = generatePlacements(hiveSurroundings, hive);
		
		return placements;
	}
	
	//Using a BFS like algorithm to find all the hive's pieces
	/**
	 * A Breadth First Search like algorithm that maps the entire hive starting from a single piece.
	 * @param startingPiece the piece from which to start, Piece.
	 * @return all the hive's pieces, ArrayList<Piece>.
	 */
	private ArrayList<Piece> hiveMappingBFS(Piece startingPiece) {
		ArrayList<Piece> foundPieces = new ArrayList<Piece>();
		Queue<Piece> piecesToCheck = new ArrayDeque <Piece>(40);
		Piece currentPiece;
		
		foundPieces.add(startingPiece);
		piecesToCheck.add(startingPiece);
		
		while (!piecesToCheck.isEmpty()) {
			currentPiece = piecesToCheck.remove();
			
			for (Piece piece : currentPiece.getLinkedPieces().values()) {
				if (!foundPieces.contains(piece)) {
					foundPieces.add(piece);
					piecesToCheck.add(piece);
				}
			}
		}
		
		return foundPieces;
	}
	
	/**
	 * Creates a deep copy of every piece (including links between copies) in the provided array.
	 * @param hive the array of pieces, ArrayList<Piece>.
	 * @return the array of copies, ArrayList<Piece>.
	 */
	private ArrayList<Piece> copyHive(ArrayList<Piece> hive) {
		ArrayList<Piece> hiveCopy = new ArrayList<Piece>();
		HashMap<Piece, Piece> originalAndCopy = new HashMap<Piece, Piece>();
		Piece original;
		Piece copy;
		Piece neighbor;
		Side side;
		
		for (Piece piece : hive) {
			copy = piece.copy();
			originalAndCopy.put(piece, copy);
			hiveCopy.add(copy);
		}
		
		for (Entry<Piece, Piece> pair : originalAndCopy.entrySet()) {
			original =  pair.getKey();
			copy = pair.getValue();
			
			for (Entry<Side, Piece> link : original.getLinkedPieces().entrySet()) {
				side = link.getKey();
				neighbor = link.getValue();
				
				for (Piece neighborCopy : hiveCopy) {
					if (neighbor.getId() == neighborCopy.getId()) {
						copy.linkTo(neighborCopy, side.opposite());
					}
				}
			}
		}
		return hiveCopy;
	}
	
	/**
	 * Creates a new, different hive made from pieces that completely surround the existent hive.
	 * @param hive the hive to surround, ArrayList<Piece>.
	 * @return the hive surroundings, ArrayList<Piece>.
	 */
	private ArrayList<Piece> generateHiveSurroundings(ArrayList<Piece> hive) {
		ArrayList<Piece> hiveCopy = new ArrayList<Piece>();
		ArrayList<Piece> surroundings = new ArrayList<Piece>();
		ArrayList<Piece> hiveAndSurroundings = new ArrayList<Piece>();
		Piece piece;
		
		for (Piece hivePiece : hive) {
			if (hivePiece.getId() != this.getId()) {
				hiveCopy.add(hivePiece);
			}
		}
		
		for (Piece hivePiece : hiveCopy) {
			for (Side hiveSide : Side.values()) {
				if (hivePiece.checkLink(hiveSide) == null) {
					piece = new DummyPiece(PieceColor.WHITE);
					piece.linkTo(hivePiece, hiveSide); //So that we have at least one placement relative to the hive
					
					piece.setRelativeCoordinates(hivePiece, hiveSide);
					
					//Link with other pieces of surroundings and hive
					hiveAndSurroundings.clear();
					hiveAndSurroundings.addAll(hive);
					hiveAndSurroundings.addAll(surroundings);
					
					linkToSurroundingPieces(piece, hiveAndSurroundings);
					
					surroundings.add(piece);
				}
			}
		}
		
		return surroundings;
	}
	
	/**
	 * Links a given piece to the pieces that are near him according to their coordinates.
	 * @param piece the piece to be linked with the others, Piece.
	 * @param surroundingPieces the pieces to check for proximity, ArrayList<Piece>.
	 */
	private void linkToSurroundingPieces(Piece piece, ArrayList<Piece> surroundingPieces) {
		double surroundingX;
		double surroundingY;
		double epsilon = 0.3;
		
		//check each adjacent side of the piece's coordinates
		for(Side side : Side.values()) {
			surroundingX = piece.getCoordinates().getX() + side.xOffset;
			surroundingY = piece.getCoordinates().getY() + side.yOffset;
			//Is there a piece in the surrounding pieces with this coordinates?
			for(Piece surroundingPiece : surroundingPieces) {
				if(Math.abs(surroundingPiece.getCoordinates().getX() - surroundingX) < epsilon && Math.abs(surroundingPiece.getCoordinates().getY() - surroundingY) < epsilon ) {
					piece.linkTo(surroundingPiece, side.opposite());
				}
			}
		}
	}
	
	/**
	 * Calculates and returns all the possible moves this ant can make.
	 * @param possiblePlacements the surroundings of the hive, ArrayList<Piece>.
	 * @param hive the original hive, on which the placements will be created, ArrayList<Piece>.
	 * @return hte possible placements of the ant, ArrayList<Placement>.
	 */
	private ArrayList<Placement> generatePlacements(ArrayList<Piece> possiblePlacements, ArrayList<Piece> hive){
		ArrayList<Placement> placements = new ArrayList<Placement>();
		Piece linkedPiece;
		boolean placementFound;
		
		for (Piece piece : possiblePlacements) {
			placementFound = false;
			if (canBeReachedBFS(piece, possiblePlacements)) {
				for (Side side : Side.values()) {
					linkedPiece = piece.checkLink(side);
					if (linkedPiece != null && !possiblePlacements.contains(linkedPiece)) {
						for (Piece hivePiece : hive) {
							if (hivePiece.getId() == linkedPiece.getId() /*&& hivePiece != this*/) {
								placements.add(new Placement(hivePiece, side.opposite()));
								placementFound = true;
								break;
							}
						}
					}
					if (placementFound) {
						break;
					}
				}
			}
			
		}

		return placements;		
	}
	
	/**
	 * Checks if a certain position can be reached by the ant (an ant can move in a position if there are at least two consecutive
	 * free sides that act as a passageway at each step following the hive surroundings).
	 * @param piece the piece that is in the position to be reached by the ant, Piece.
	 * @param hiveSurroundings the hive surroundings correspond to the positions the ant can take around the hive, ArrayList<Piece>.
	 * @return true if the position can be reached, else false, boolean.
	 */
	boolean canBeReachedBFS(Piece piece, ArrayList<Piece> hiveSurroundings) {
		ArrayList<Piece> foundPieces = new ArrayList<Piece>();
		Queue<Piece> piecesToCheck = new ArrayDeque <Piece>(40);
		Piece currentPiece;
		
		foundPieces.add(piece);
		piecesToCheck.add(piece);
		
		while (!piecesToCheck.isEmpty()) {
			currentPiece = piecesToCheck.remove();
			
			for (Side side : Side.values()) {
				if (currentPiece.checkLink(side.previous()) == null || currentPiece.checkLink(side.previous()) == null
					|| hiveSurroundings.contains(currentPiece.checkLink(side.previous()))
					|| hiveSurroundings.contains(currentPiece.checkLink(side.next()))) {
					
					if (currentPiece.checkLink(side) != null) {
						if (currentPiece.checkLink(side).getId() == this.getId()) {
							return true;
						}
						
						if (hiveSurroundings.contains(currentPiece.checkLink(side))) {
							if (!foundPieces.contains(currentPiece.checkLink(side))) {
								foundPieces.add(currentPiece.checkLink(side));
								piecesToCheck.add(currentPiece.checkLink(side));
							}
						}
					}
					
					
				}
			}
		}
		return false;
	}
}
