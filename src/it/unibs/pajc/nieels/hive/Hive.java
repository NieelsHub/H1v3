package it.unibs.pajc.nieels.hive;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Side;

//MODEL

/**
 * Modelization of the hive (a compostiton of all the active pieces in the game)
 * @author Nicol Stocchetti
 *
 */
public class Hive {
	
	private ArrayList <Piece> placedPieces = new ArrayList();
	private ArrayList <Piece> whitesToBePlaced = new ArrayList();
	private ArrayList <Piece> blacksToBePlaced = new ArrayList();
	
	/**
	 * The Hive's constructor.
	 * @param piecesSet a map containing what kind of pieces are to be generated for the game and their number, LinkedHashMap.
	 */
	public Hive(LinkedHashMap <Class<?>, Integer> piecesSet) {
		Class<?> pieceKind;
		int numberOfPieces;
		int i;
		
		for (Entry<Class<?>, Integer> entry : piecesSet.entrySet()) {
			pieceKind = entry.getKey();
			numberOfPieces = entry.getValue();
			
			try {
				Constructor<?> constructor =  pieceKind.getConstructor(PieceColor.class);//PieceColor.class returns the Class object associated to the type PieceColor.
				for(i = 0; i < numberOfPieces; i++ ) {
					whitesToBePlaced.add((Piece)constructor.newInstance(PieceColor.WHITE));
					blacksToBePlaced.add((Piece)constructor.newInstance(PieceColor.BLACK));
				}
			}
			catch(NoSuchMethodException e) {
				System.err.println("The specified constructor doesn't exist!");
				e.printStackTrace();
			}
			catch(Exception e) {
				System.err.println("Unknown error.");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @return all the pieces that compose the hive, ArrayList<Piece>.
	 */
	public ArrayList<Piece> getPlacedPieces() {
		return placedPieces;
	}

	/**
	 * 
	 * @return all the white pieces that are yet to be placed in the hive, ArrayList<Piece>.
	 */
	public ArrayList<Piece> getWhitesToBePlaced() {
		return whitesToBePlaced;
	}

	/**
	 * 
	 * @return all the black pieces that are yet to be placed in the hive, ArrayList<Piece>.
	 */
	public ArrayList<Piece> getBlacksToBePlaced() {
		return blacksToBePlaced;
	}

	/**
	 * Positions the first piece of the game and sets the games' coordinate system at its center. If other pieces have
	 * @param piece the piece to be set as the starting point of the hive, Piece.
	 */
	public void placeFirstPiece(Piece piece) {
		if(placedPieces.isEmpty()) {
			piece.getCoordinates().setLocation(0.0, 0.0);
			//piece.setInGame(true);
			placedPieces.add(piece);
			removeFromPiecesToBePlaced(piece);
		} else {
			
		}
	}
	
	/**
	 * Places a new piece next to another one that's already part of the hive.
	 * @param piece the piece to be positioned, Piece.
	 * @param neighbor the piece next to which to be positioned, Piece.
	 * @param positionOnNeighbor the side of the neighbor on which to be positioned, Side.
	 */
	public void placeNewPiece(Piece piece, Piece neighbor, Side positionOnNeighbor) {
		//Checks if the chosen pieces are already part of the hive: the chosen neighbor must be
		//part of the hive, while the chosen piece must be free.
		if(isInHive(piece)) {
			System.err.println(piece.getName() + " " + piece.getColor() + "-" + piece.getId() + ", " + positionOnNeighbor
					+ ": The selected piece is already part of the hive, use movePiece to move it! - placement not executed.");
			return;
		}
		
		if(!isInHive(neighbor)) {
			System.err.println(piece.getName() + " " + piece.getColor() + "-" + piece.getId() + ", " + positionOnNeighbor 
					+ ": The selected neighbor (" + neighbor +") is not part of the hive! - placement not executed.");
			return;
		}
		
		//checks if the chosen neighbor already has a link on that side, in this case it's not possible to continue the placement
		if(neighbor.checkLink(positionOnNeighbor) != null) {
			System.err.println(piece.getName() + " " + piece.getColor() + "-" + piece.getId() + ", " + positionOnNeighbor
					+ ": There's already another piece in the selected place (" + neighbor.checkLink(positionOnNeighbor).toString()
					+ ") - placement not executed.");
			return;
		}
		
		//Sets the coordinates of the piece (relative to the starting piece of the hive).
		piece.setRelativeCoordinates(neighbor, positionOnNeighbor);
		
		//We also have to check and update the links with other surrounding pieces, if there are any	
		linkToSurroundingPieces(piece);
		
		//After the first two placements, checks if the surrounding pieces are all the same color (pieces can only be placed
		//next to same color pieces), if not undo the piece placement.
		if (placedPieces.size()>=2 && !checkSurroundingPiecesSameColor(piece)) {
			piece.resetPosition();
			System.err.println(piece.getName() + " " + piece.getColor() + "-" + piece.getId() + ", " + positionOnNeighbor
					+ ": The piece can't be positioned near other pieces of a different color. - placement not executed.");
			return;
		}
		
		placedPieces.add(piece);
		removeFromPiecesToBePlaced(piece);
		//piece.setInGame(true);
	}
	
	/**
	 * Checks whether a piece is currently part of the hive or not.
	 * @param piece the piece to check, Piece.
	 * @return true if the piece is in the hive, false otherwise.
	 */
	private boolean isInHive(Piece piece) {
		boolean foundPieceInHive = false;
		
		for(Piece hivePiece : placedPieces) {
			if(hivePiece == piece)
				foundPieceInHive = true;
		}
		return foundPieceInHive;
	}
	
	/**
	 * Removes a specific piece from the corresponding color's list of pieces available to be placed.
	 * @param piece the piece to remove, Piece.
	 */
	private void removeFromPiecesToBePlaced(Piece piece) {
		if (piece.getColor() == PieceColor.BLACK) {
			blacksToBePlaced.remove(piece);
		} else {
			whitesToBePlaced.remove(piece);
		}
	}
	
	/**
	 * Given a piece with valid position coordinates, finds all the surrounding pieces and creates links between them.
	 * @param piece the piece that needs to be linked to his neighbors, Piece.
	 */
	private void linkToSurroundingPieces(Piece piece) {
		double surroundingX;
		double surroundingY;
		
		//check each adjacent side of the piece's coordinates
		for(Side side : Side.values()) {
			surroundingX = piece.getCoordinates().getX() + side.xOffset;
			surroundingY = piece.getCoordinates().getY() + side.yOffset;
			//Is there a piece in the hive with this coordinates?
			for(Piece hivePiece : placedPieces) {
				if(hivePiece.getCoordinates().getX() == surroundingX && hivePiece.getCoordinates().getY() == surroundingY) {
					piece.linkTo(hivePiece, side.opposite());
				}
			}
		}
	}
	
	/**
	 * Checks whether all the surrounding pieces are of the same color as the specified piece.
	 * @param piece the piece whose surroundings are to check, Piece.
	 * @return true if the pieces are all the same color, else false, boolean.
	 */
	private boolean checkSurroundingPiecesSameColor(Piece piece) {
		for (Piece surroundingPiece : piece.getLinkedPieces().values()) {
			if (surroundingPiece.getColor() != piece.getColor()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 *
	 * @param piece
	 * @param side
	 */
	public void movePiece(Piece piece, Side side) {
		if(!isInHive(piece)) {
			System.err.println("The selected piece is not present in the hive, you have to place it first! - movement not executed.");
			return;
		}
		piece.move(side);
		linkToSurroundingPieces(piece);
	}

	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
		
		toString.append("\n - HIVE -\n");
		for(Piece piece : this.getPlacedPieces()) {
			toString.append(piece + "\n\n");
		}
		
		toString.append(" - BLACKS TO BE PLACED -\n");
		for(Piece piece : this.getBlacksToBePlaced()) {
			toString.append(piece + "\n");
		}
		
		toString.append("\n - WHITES TO BE PLACED -\n");
		for(Piece piece : this.getWhitesToBePlaced()) {
			toString.append(piece + "\n");
		}
		
		return toString.toString();
	}
	
}
