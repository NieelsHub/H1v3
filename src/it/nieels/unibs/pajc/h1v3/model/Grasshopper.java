package it.nieels.unibs.pajc.h1v3.model;

import java.util.ArrayList;

import it.nieels.unibs.pajc.h1v3.model.Piece.Placement;
import it.nieels.unibs.pajc.h1v3.model.Piece.Side;

/**
 * Implements the logic of the Grasshopper: a piece that can jump over other pieces.
 * @author Nicol Stocchetti
 *
 */
public class Grasshopper extends Piece {
	
	public final static String PIECE_NAME = "GRASSHOPPER";
	public final static boolean VERTICAL_MOVEMENT = true;

	/**
	 * The constructor, it automatically assigns the piece name and the capability to move vertically to his super Piece's attributes.
	 * @param color the color of this piece's team, PieceColor.
	 */
	public Grasshopper(PieceColor color) {
		super(color, VERTICAL_MOVEMENT, PIECE_NAME);
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
}
