package it.unibs.pajc.nieels.hive;

import java.util.ArrayList;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Placement;
import it.unibs.pajc.nieels.hive.Piece.Side;

public class Beetle extends Piece {

	public final static String PIECE_NAME = "BEETLE";
	public final static boolean VERTICAL_MOVEMENT = true;

	public Beetle(PieceColor color) {
		super(color, VERTICAL_MOVEMENT, PIECE_NAME);
	}
	
	@Override
	public ArrayList<Placement> calcPossibleMoves() {
		return null;
	}
}
