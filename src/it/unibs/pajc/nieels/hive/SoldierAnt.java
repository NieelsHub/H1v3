package it.unibs.pajc.nieels.hive;

import java.util.ArrayList;

import it.unibs.pajc.nieels.hive.Piece.Placement;
import it.unibs.pajc.nieels.hive.Piece.Side;

public class SoldierAnt extends Piece {

	public final static String PIECE_NAME = "SOLDIER ANT";

	public SoldierAnt(PieceColor color) {
		super(color, PIECE_NAME);
		// TODO Auto-generated constructor stub
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
