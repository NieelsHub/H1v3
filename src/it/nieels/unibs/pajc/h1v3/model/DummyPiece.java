package it.nieels.unibs.pajc.h1v3.model;

import java.util.ArrayList;

import it.nieels.unibs.pajc.h1v3.model.Piece.PieceColor;
import it.nieels.unibs.pajc.h1v3.model.Piece.Placement;

/**
 * Implements the logic of the DummyPiece: a piece that only serves as a placeholder that simulates other pieces.
 * @author Nicol Stocchetti
 *
 */
public class DummyPiece extends Piece {
	
	public final static String PIECE_NAME = "DUMMY_PIECE";
	public final static boolean VERTICAL_MOVEMENT = false;

	/**
	 * The constructor, it automatically assigns the piece name and the capability to move vertically to his super Piece's attributes.
	 * @param color the color of this piece's team, PieceColor.
	 */
	public DummyPiece(PieceColor color) {
		super(color, VERTICAL_MOVEMENT, PIECE_NAME);
		setDummyId();
	}
	
	//Since this piece is just a placeholder, it doesn't have a movement logic.
	@Override
	public ArrayList<Placement> calcPossibleMoves() {
		return new ArrayList<Placement>();
	}
}
