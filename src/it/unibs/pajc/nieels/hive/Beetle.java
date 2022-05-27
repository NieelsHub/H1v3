package it.unibs.pajc.nieels.hive;

import java.util.ArrayList;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Placement;
import it.unibs.pajc.nieels.hive.Piece.Side;

public class Beetle extends Piece {

	public final static String PIECE_NAME = "BEETLE";
	public final static boolean VERTICAL_MOVEMENT = true;
	private Piece covered;

	public Beetle(PieceColor color) {
		super(color, VERTICAL_MOVEMENT, PIECE_NAME);
	}
	
	public Piece getCovered() {
		return covered;
	}

	public void setCovered(Piece covered) {
		this.covered = covered;
	}

	@Override
	public ArrayList<Placement> calcPossibleMoves() {
		ArrayList<Placement> placements = new ArrayList<Placement>();
		Piece anchorPiece;
		Side anchorSide;
		
		for(Side side : Side.values()) {
			anchorPiece = null;
			anchorSide = null;
			System.out.println(side);
			if (this.checkLink(side) == null) { //If the space on that side is free
				if (this.checkLink(side.previous()) == null && this.checkLink(side.next()) != null) {
					anchorPiece = this.checkLink(side.next());
					anchorSide = side.next().opposite().next();
					
					System.out.println(anchorPiece + " - " + anchorSide);
					
					placements.add(new Placement(anchorPiece, anchorSide));
				}
				
				if (this.checkLink(side.previous()) != null && this.checkLink(side.next()) == null) {
					anchorPiece = this.checkLink(side.previous());
					anchorSide = side.previous().opposite().previous();
					
					System.out.println(anchorPiece + " - " + anchorSide);
					
					placements.add(new Placement(anchorPiece, anchorSide));
				}
			}
			else {
				anchorPiece = this; //The beetle anchors to itself to lift up on the explored side
				anchorSide = side;
				
				placements.add(new Placement(anchorPiece, anchorSide));
			}
		}
		return placements;
	}
}
