package it.unibs.pajc.nieels.hive;

import java.util.ArrayList;

import it.unibs.pajc.nieels.hive.Piece.Placement;
import it.unibs.pajc.nieels.hive.Piece.Side;

public class Grasshopper extends Piece {
	
	public final static String PIECE_NAME = "GRASSHOPPER";

	public Grasshopper(PieceColor color) {
		super(color, PIECE_NAME);
		setVerticalMovement(true);
	}

	@Override
	public ArrayList<Placement> calcPossibleMoves() {
		ArrayList<Placement> placements = new ArrayList<Placement>();
		ArrayList<Side> directions = new ArrayList<Side>(this.getLinkedPieces().keySet());
		
		for(Side direction : directions) {
			Piece currentPiece = this;
			Piece nextPiece = currentPiece.checkLink(direction);
			while (nextPiece != null) {
				currentPiece = nextPiece;
				nextPiece = currentPiece.checkLink(direction);
			}
			placements.add(new Placement(currentPiece, direction));
		}
		return placements;
	}
	
	@Override
	public void move(Placement placement) {
		//VA FATTO NELL' HIVE
		
	}

}
