package it.unibs.pajc.nieels.hive;

import java.util.ArrayList;

import it.unibs.pajc.nieels.hive.Piece.Side;

public class QueenBee extends Piece {
	
	public final static String PIECE_NAME = "QUEEN BEE";

	public QueenBee(PieceColor color) {
		super(color, PIECE_NAME);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ArrayList<Side> getPossibleDirections() {
		return null;
	}

	@Override
	public void move(Side direction) {
		// TODO Auto-generated method stub

	}

}
