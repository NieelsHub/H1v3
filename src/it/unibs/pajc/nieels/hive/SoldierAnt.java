package it.unibs.pajc.nieels.hive;

import java.lang.reflect.Constructor;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Queue;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Placement;
import it.unibs.pajc.nieels.hive.Piece.Side;

public class SoldierAnt extends Piece {

	public final static String PIECE_NAME = "SOLDIER ANT";
	public final static boolean VERTICAL_MOVEMENT = false;

	public SoldierAnt(PieceColor color) {
		super(color, VERTICAL_MOVEMENT, PIECE_NAME);
	}
	
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
		
		/*
		for (Piece hivePiece : hive) {
			for (Side side : Side.values()) {
				if (hivePiece.checkLink(side) == null) {
					placement = new Placement(hivePiece, side);
					if (canBeReachedBFS(placement)) {
						placements.add(placement);
					}
				}
			}
		}
		*/
		
		return placements;
	}
	
	//Using a BFS like algorithm to find all the hive's pieces
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
	
	//Creates a deep copy of every piece in the provided array
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
	
	
	private ArrayList<Piece> generateHiveSurroundings(ArrayList<Piece> hive) {
		ArrayList<Piece> surroundings = new ArrayList<Piece>();
		ArrayList<Piece> hiveAndSurroundings = new ArrayList<Piece>();
		Piece piece;
		
		for (Piece hivePiece : hive) {
			for (Side hiveSide : Side.values()) {
				if (hivePiece.checkLink(hiveSide) == null) {
					piece = new QueenBee(PieceColor.WHITE);
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
	
	
	ArrayList<Placement> generatePlacements(ArrayList<Piece> possiblePlacements, ArrayList<Piece> hive){
		ArrayList<Placement> placements = new ArrayList<Placement>();
		Piece linkedPiece;
		boolean placementFound;
		
		for (Piece piece : possiblePlacements) {
			placementFound = false;
			if (canBeReachedBFS(piece, possiblePlacements)) {
				for (Side side : Side.values()) {
					linkedPiece = piece.checkLink(side);//check what happens if this == null
					if (linkedPiece != null && !possiblePlacements.contains(linkedPiece)) {
						for (Piece hivePiece : hive) {
							if (hivePiece.getId() == linkedPiece.getId()) {
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
	
	
	
	
	//An ant can move in a position if there are at least two 
	
	private boolean canBeReachedBFS(Piece piece, ArrayList<Piece> hiveSurroundings) {
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
			
			/*
			for (Piece pieccc : currentPiece.getLinkedPieces().values()) {
				if (!foundPieces.contains(pieccc)) {
					foundPieces.add(pieccc);
					piecesToCheck.add(pieccc);
				}
			}*/
		}
		
		return false;
	}
	
	/*
	private boolean canBeReachedBFS(Placement placement) {
		int surroundingPieces;
		double surroundingX;
		double surroundingY;
		double epsilon = 0.3;
		
		double placementX = placement.getNeighbor().getCoordinates().getX() + placement.getPositionOnNeighbor().xOffset;
		double placementY = placement.getNeighbor().getCoordinates().getY() + placement.getPositionOnNeighbor().yOffset;
		/*
		//check each adjacent side of the new piece's coordinates
		for(Side side : Side.values()) {
			surroundingX = placementX + side.xOffset;
			surroundingY = placementY + side.yOffset;
			//Is there a piece in the hive with this coordinates?
			for(Piece hivePiece : placedPieces) {
				if(Math.abs(hivePiece.getCoordinates().getX() - surroundingX) < epsilon &&
					Math.abs(hivePiece.getCoordinates().getY() - surroundingY) < epsilon &&
					hivePiece.getColor() != color) {
					return false;
				}
			}
		}*/
		/*return true;
	}
	*/
	
	@Override
	public void move(Placement placement) {
		// TODO Auto-generated method stub

	}

}
