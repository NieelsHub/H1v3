package it.unibs.pajc.nieels.hive;

import java.util.ArrayList;

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
	
	@Override
	public ArrayList<Placement> calcPossibleMoves() {
		return new ArrayList<Placement>();
	}
}
