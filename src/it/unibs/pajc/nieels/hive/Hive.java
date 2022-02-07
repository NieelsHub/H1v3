package it.unibs.pajc.nieels.hive;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Side;

public class Hive {
	
	private ArrayList <Piece> hive = new ArrayList();
	private ArrayList <Piece> whitesToBePlaced = new ArrayList();
	private ArrayList <Piece> blacksToBePlaced = new ArrayList();
	
	//Flag to see whether the first piece of the game has already been set into the game
	private boolean firstPlaced = false;
	
	public Hive(LinkedHashMap <Class<?>, Integer> piecesSet) {
		int i;
		
		for (Entry<Class<?>, Integer> entry : piecesSet.entrySet()) {
			
			Class<?> pieceKind = entry.getKey();
			int numberOfPieces = entry.getValue();
			
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
		/*
		blacksToBePlaced.add(new QueenBee(PieceColor.BLACK));
		whitesToBePlaced.add(new QueenBee(PieceColor.WHITE));
		
		for(i = 0; i < 2; i++ ) {
			blacksToBePlaced.add(new Beetle(PieceColor.BLACK));
			whitesToBePlaced.add(new Beetle(PieceColor.WHITE));
			blacksToBePlaced.add(new Spider(PieceColor.BLACK));
			whitesToBePlaced.add(new Spider(PieceColor.WHITE));
		}
		
		for(i = 0; i < 3; i++ ) {
			blacksToBePlaced.add(new SoldierAnt(PieceColor.BLACK));
			whitesToBePlaced.add(new SoldierAnt(PieceColor.WHITE));
			blacksToBePlaced.add(new Grasshopper(PieceColor.BLACK));
			whitesToBePlaced.add(new Grasshopper(PieceColor.WHITE));
		}*/
	}
	
	
	public ArrayList<Piece> getHive() {
		return hive;
	}




	public ArrayList<Piece> getWhitesToBePlaced() {
		return whitesToBePlaced;
	}




	public ArrayList<Piece> getBlacksToBePlaced() {
		return blacksToBePlaced;
	}




	//Positions the first piece of the game and sets the games' coordinate system at its center
	public void placeFirstPiece(Piece piece) {
		if(firstPlaced) {
			return;
		}
		piece.setAsFirst();
		hive.add(piece);
		this.firstPlaced = true;
		removeFromPiecesToBePlaced(piece);
	}
	
	public void placeNewPiece(Piece piece, Piece neighbor, Side positionOnNeighbor) {
		//Checks if the chosen pieces are already part of the hive: the chosen neighbor must be
		//part of the hive, while the chosen piece must be free.
		
		if(isInHive(piece)) {
			System.err.println("The selected piece is already part of the hive, use movePiece to move it!");
			return;
		}
		
		if(!isInHive(neighbor)) {
			System.err.println("The selected neighbor is not part of the hive");
			return; 
		}
		
		/*
		boolean foundPieceInHive = false;
		boolean foundNeighborInHive = false;
		
		for(Piece hivePiece : hive) {
			if(hivePiece == piece)
				foundPieceInHive = true;
			if(hivePiece == neighbor)
				foundNeighborInHive = true;
		}
		
		if(foundPieceInHive) {
			System.err.println("The selected piece is already part of the hive, use movePiece to move it!");
			return;
		}
		
		if(!foundNeighborInHive) {
			System.err.println("The selected neighbor is not part of the hive");
			return;
		}
		*/
		
		//checks if the chosen neighbor already has a link on that side, in this case it's not possible to continue the placement
		if(neighbor.checkLink(positionOnNeighbor) != null) {
			System.err.println("There's already another piece in the selected place");
			return;
		}
		
		piece.setRelativePosition(neighbor, positionOnNeighbor);
		
		//We also have to check and update the links with other surrounding pieces, if there are any	
		linkToSurroundingPieces(piece);
		
		hive.add(piece);
		removeFromPiecesToBePlaced(piece);
	}
	
	public void movePiece(Piece piece, Side side) {
		if(!isInHive(piece)) {
			System.err.println("The selected piece is not present in the hive, you have to place it first!");
			return;
		}
		piece.move(side);
		linkToSurroundingPieces(piece);
	}
	
	private void linkToSurroundingPieces(Piece piece) {
		double x;
		double y;
		
		//check each adjacent side of the piece's coordinates
		for(Side side : Side.values()) {
			x = piece.getX() + side.xOffset;
			y = piece.getY() + side.yOffset;
			//Is there a piece in the hive with this coordinates?
			for(Piece hivePiece : hive) {
				if(hivePiece.getX() == x && hivePiece.getY() == y) {
					piece.linkTo(hivePiece, side.opposite());
				}
			}
		}
	}
	
	private boolean isInHive(Piece piece) {
		boolean foundPieceInHive = false;
		
		for(Piece hivePiece : hive) {
			if(hivePiece == piece)
				foundPieceInHive = true;
		}
		return foundPieceInHive;
	}
	
	
	private void removeFromPiecesToBePlaced(Piece piece) {
		if (piece.getColor() == PieceColor.BLACK) {
			blacksToBePlaced.remove(piece);
		} else {
			whitesToBePlaced.remove(piece);
		}
	}


}
