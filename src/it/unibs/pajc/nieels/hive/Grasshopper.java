package it.unibs.pajc.nieels.hive;

import java.util.ArrayList;

import it.unibs.pajc.nieels.hive.Piece.Side;

public class Grasshopper extends Piece {
	
	public final static String PIECE_NAME = "GRASSHOPPER";

	public Grasshopper(PieceColor color) {
		super(color, PIECE_NAME);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<Side> getPossibleDirections() {
		ArrayList<Side> directions = new ArrayList<Side>(this.getLinkedPieces().keySet());
		return directions;
	}
	
	@Override
	public void move(Side direction) {
		//Check that side is a valid direction for the movement
		if (this.checkLink(direction) == null) {
			System.err.println("The selected direction is not valid! - movement not executed.");
			return; //eccezione
		}
		
		Piece currentPiece = this;
		Piece nextPiece = currentPiece.checkLink(direction);
		while (nextPiece != null) {
			currentPiece = nextPiece;
			nextPiece = currentPiece.checkLink(direction);
		}
		
		this.resetPosition();
		
		this.setRelativeCoordinates(currentPiece, direction);
	}

}
