package it.unibs.pajc.nieels.hive;

import java.util.ArrayList;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Placement;
import it.unibs.pajc.nieels.hive.Piece.Side;

/**
 * Implements the logic of the QueenBee: the slower but most important piece, to be protected form the oppontent's attack.
 * @author Nicol Stocchetti
 *
 */
public class QueenBee extends Piece {
	
	public final static String PIECE_NAME = "QUEEN_BEE";
	public final static boolean VERTICAL_MOVEMENT = false;

	/**
	 * The constructor, it automatically assigns the piece name and the capability to move vertically to his super Piece's attributes.
	 * @param color the color of this piece's team, PieceColor.
	 */
	public QueenBee(PieceColor color) {
		super(color, VERTICAL_MOVEMENT, PIECE_NAME);
	}
	
	@Override
	public ArrayList<Placement> calcPossibleMoves() {
		ArrayList<Placement> placements = new ArrayList<Placement>();
		Piece anchorPiece;
		Side anchorSide;
		
		for(Side side : Side.values()) {
			anchorPiece = null;
			anchorSide = null;
			//System.out.println(side);
			if (this.checkLink(side) == null) { //If the space on that side is free
				if (this.checkLink(side.previous()) == null && this.checkLink(side.next()) != null) {
					anchorPiece = this.checkLink(side.next());
					anchorSide = side.next().opposite().next();
					
					//System.out.println(anchorPiece + " - " + anchorSide);
					
					placements.add(new Placement(anchorPiece, anchorSide));
				}
				
				if (this.checkLink(side.previous()) != null && this.checkLink(side.next()) == null) {
					anchorPiece = this.checkLink(side.previous());
					anchorSide = side.previous().opposite().previous();
					
					//System.out.println(anchorPiece + " - " + anchorSide);
					
					placements.add(new Placement(anchorPiece, anchorSide));
				}
			}
		}
		return placements;
	}
	
	/**
	 * Checks if the QueenBee is completely surrounded (game losing condition).
	 * @return true if the queen is surrounded, else false, boolean.
	 */
	public boolean isSurrounded() {
		return this.getLinkedPieces().entrySet().size() > 5;
	}
}
