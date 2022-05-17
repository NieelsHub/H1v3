package it.unibs.pajc.nieels.hive;

import java.util.ArrayList;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Placement;
import it.unibs.pajc.nieels.hive.Piece.Side;

public class Spider extends SoldierAnt {

	public final static String PIECE_NAME = "SPIDER";
	public final static boolean VERTICAL_MOVEMENT = false;

	public Spider(PieceColor color) {
		super(color, VERTICAL_MOVEMENT, PIECE_NAME);
	}
	
	@Override 
	ArrayList<Placement> generatePlacements(ArrayList<Piece> possiblePlacements, ArrayList<Piece> hive){
		ArrayList<Placement> placements = super.generatePlacements(possiblePlacements, hive);
		placements.clear();
		placements.add(new Placement(this, Side.NORTH));
		return placements;		
	}
	
	@Override
	public void move(Placement placement) {
		// TODO Auto-generated method stub

	}

}
