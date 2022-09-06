package it.nieels.unibs.pajc.h1v3.model;

import java.util.ArrayList;

import it.nieels.unibs.pajc.h1v3.model.Piece.PieceColor;
import it.nieels.unibs.pajc.h1v3.model.Piece.Placement;
import it.nieels.unibs.pajc.h1v3.model.Piece.Side;

/**
 * Implements the logic of the Beetle: a piece that can get on top of other pieces to block them.
 * @author Nicol Stocchetti
 *
 */
public class Beetle extends Piece {

	public final static String PIECE_NAME = "BEETLE";
	public final static boolean VERTICAL_MOVEMENT = true;

	/**
	 * The constructor, it automatically assigns the piece name and the capability to move vertically to his super Piece's attributes.
	 * @param color the color of this piece's team, PieceColor.
	 */
	public Beetle(PieceColor color) {
		super(color, VERTICAL_MOVEMENT, PIECE_NAME);
	}
	
	@Override
	public ArrayList<Placement> calcPossibleMoves() {
		ArrayList<Placement> placements = new ArrayList<Placement>();
		Piece anchorPiece;
		Side anchorSide;
		Piece basePiece;
		
		basePiece = this;
		while (basePiece != null && basePiece.getBottomPiece() != null) {
			basePiece = basePiece.getBottomPiece();
		}
		
		for(Side side : Side.values()) {
			anchorPiece = null;
			anchorSide = null;
			//System.out.println(side);
			if (basePiece.checkLink(side) == null) { //If the space on that side is free
				//If the piece is above ground level 
				if(basePiece != this) {
					anchorPiece = basePiece;
					anchorSide = side;
					
					//System.out.println(anchorPiece + " - " + anchorSide);
					
					placements.add(new Placement(anchorPiece, anchorSide));
				}
				else {
					//If the piece can slide in at ground level
					if ((basePiece.checkLink(side.previous()) == null && basePiece.checkLink(side.next()) != null) /*|| basePiece != this*/) {
						anchorPiece = basePiece.checkLink(side.next());
						anchorSide = side.next().opposite().next();
						
						//System.out.println(anchorPiece + " - " + anchorSide);
						
						placements.add(new Placement(anchorPiece, anchorSide));
					}
					
					if (basePiece.checkLink(side.previous()) != null && basePiece.checkLink(side.next()) == null) {
						anchorPiece = basePiece.checkLink(side.previous());
						anchorSide = side.previous().opposite().previous();
						
						//System.out.println(anchorPiece + " - " + anchorSide);
						
						placements.add(new Placement(anchorPiece, anchorSide));
					}
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
