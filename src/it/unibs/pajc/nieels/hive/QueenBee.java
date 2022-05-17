package it.unibs.pajc.nieels.hive;

import java.util.ArrayList;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Placement;
import it.unibs.pajc.nieels.hive.Piece.Side;

public class QueenBee extends Piece {
	
	public final static String PIECE_NAME = "QUEEN BEE";
	public final static boolean VERTICAL_MOVEMENT = false;

	public QueenBee(PieceColor color) {
		super(color, VERTICAL_MOVEMENT, PIECE_NAME);
	}
	
	@Override
	public ArrayList<Placement> calcPossibleMoves() {
		return null;
	}
	
	@Override
	public void move(Placement placement) {
		// TODO Auto-generated method stub

	}

}
