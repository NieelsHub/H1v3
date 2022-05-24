package it.unibs.pajc.nieels.hive;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Queue;

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
	boolean canBeReachedBFS(Piece piece, ArrayList<Piece> hiveSurroundings) {
		HashMap<Piece, HashMap<Piece, Integer>> foundPieces = new HashMap<Piece, HashMap<Piece, Integer>>();
		//HashMap<Piece, Integer> newHashMapPlaceholder = new HashMap<Piece, Integer>();
		Queue<Piece> piecesToCheck = new ArrayDeque <Piece>(40);
		Piece currentPiece;
		ArrayList<Entry<Piece,Integer>> steps = new ArrayList<Entry<Piece,Integer>>();
		//int step = 0;
		boolean exit = false;
		
		System.out.println("STARTING PIECE: " + piece);
		foundPieces.put(piece, new HashMap<Piece, Integer>());
		foundPieces.get(piece).put(piece, 0);
		piecesToCheck.add(piece);
		
		while (!piecesToCheck.isEmpty() && !exit/*&& step < 3*/) {
			currentPiece = piecesToCheck.remove();
			System.out.println("LOOKING AT " + currentPiece);
			steps.clear();
			for (Entry<Piece, Integer> entry : foundPieces.get(currentPiece).entrySet()) {
				steps.add(entry);
			} //cloning to avoid side effects while iterating on the actual entry set
			
			for (Entry<Piece, Integer> step : steps) {
				System.out.println("LOOKING AT " + step);
				
				for (Side side : Side.values()) {
					System.out.print(step + " " + side.name());
					if (currentPiece.checkLink(side.previous()) == null || currentPiece.checkLink(side.next()) == null
						|| hiveSurroundings.contains(currentPiece.checkLink(side.previous()))
						|| hiveSurroundings.contains(currentPiece.checkLink(side.next()))
						|| currentPiece.checkLink(side.previous()).getId() == this.getId() || currentPiece.checkLink(side.next()).getId() == this.getId()//the potential obstruction caused by the spider itself must be ignored
						) {
						System.out.println(" - CAN SQUEEZE THROUGH");
						if (currentPiece.checkLink(side) != null) {
							System.out.println(" - MOVING THROUGH WE FIND A VALID PIECE ");
							
							System.out.println("- DO THE PREVIOUS AND NEXT PIECE HAVE A LINKED HIVE PIECE IN COMMON?");
							for (Piece linkedPiecePrevious : currentPiece.getLinkedPieces().values()) {
								for (Piece linkedPieceNext : currentPiece.checkLink(side).getLinkedPieces().values()) {
									if (linkedPiecePrevious == linkedPieceNext && !hiveSurroundings.contains(linkedPiecePrevious) && linkedPiecePrevious.getId() != this.getId()) {
										System.out.println(" - IS IT THE SPIDER?");
										if (currentPiece.checkLink(side).getId() == this.getId()) {
											if (step.getValue() == 2) {
												System.out.println(" - YES! THIS IS A VALID PLACEMENT!");
												return true;
											}
										}
										System.out.println(" - IS THAT PIECE PART OF THE SURROUNDINGS (VALID NEXT STEPS)? ARE WE MOVING BACK FROM WHERE WE CAME (ILLEGAL MOVE)?");
										if (hiveSurroundings.contains(currentPiece.checkLink(side)) && currentPiece.checkLink(side) != step.getKey()) {
											System.out.println(" OK\n- WAS IT ALREADY FOUND BEFORE?");
											if (!foundPieces.containsKey(currentPiece.checkLink(side))) {
												System.out.println(" NO, SO ADD IT TO THE NEXT STEPS!");
												foundPieces.put(currentPiece.checkLink(side), new HashMap<Piece, Integer>());
											}
											if (!piecesToCheck.contains(currentPiece.checkLink(side))) {
												piecesToCheck.add(currentPiece.checkLink(side));
											}
											foundPieces.get(currentPiece.checkLink(side)).put(currentPiece, ((step.getValue())+1));
											System.out.println("ADDED " + currentPiece.checkLink(side) + " - " + ((step.getValue())+1));
										}
									}
								}
							}
						}
					}
				}
			}
			exit = false;
			
				for (Entry<Piece, Integer> step : steps) {
					if (step.getValue() > 20) {
						exit = true;
						break;
					}
				}
				if (!exit) {
					break;
				}
			
		}
		return false;
	}
	
	/*
	 * boolean canBeReachedBFS(Piece piece, ArrayList<Piece> hiveSurroundings) {
		HashMap<Piece, Integer> foundPieces = new HashMap<Piece, Integer>();
		Queue<Piece> piecesToCheck = new ArrayDeque <Piece>(40);
		Piece currentPiece;
		int steps = 0;
		
		System.out.println("STARTING PIECE: " + piece);
		foundPieces.put(piece, 0);
		piecesToCheck.add(piece);
		
		while (!piecesToCheck.isEmpty() && steps < 3) {
			currentPiece = piecesToCheck.remove();
			steps = foundPieces.get(currentPiece);
			
			System.out.println("LOOKING AT " + currentPiece);
			
			for (Side side : Side.values()) {
				System.out.print(steps + " " + side.name());
				if (currentPiece.checkLink(side.previous()) == null || currentPiece.checkLink(side.next()) == null
					|| hiveSurroundings.contains(currentPiece.checkLink(side.previous()))
					|| hiveSurroundings.contains(currentPiece.checkLink(side.next()))
					|| currentPiece.checkLink(side.previous()).getId() == this.getId() || currentPiece.checkLink(side.next()).getId() == this.getId()) {
					System.out.println(" - CAN SQUEEZE THROUGH");
					if (currentPiece.checkLink(side) != null) {
						System.out.println(" - THERE'S A VALID PIECE MOVING THROUGH");
						
						System.out.println("- DO THE PREVIOUS AND NEXT PIECE HAVE A LINKED HIVE PIECE IN COMMON?");
						for (Piece linkedPiecePrevious : currentPiece.getLinkedPieces().values()) {
							for (Piece linkedPieceNext : currentPiece.checkLink(side).getLinkedPieces().values()) {
								if (linkedPiecePrevious == linkedPieceNext && !hiveSurroundings.contains(linkedPiecePrevious) && linkedPiecePrevious.getId() != this.getId()) {
									System.out.println(" - IS IT THE SPIDER?");
									if (currentPiece.checkLink(side).getId() == this.getId()) {
										if (steps == 2) {
											System.out.println(" - YES! THIS IS A VALID PLACEMENT!");
											return true;
										}
									}
									System.out.println(" - IS THAT PIECE PART OF THE SURROUNDINGS (VALID NEXT STEPS)?");
									if (hiveSurroundings.contains(currentPiece.checkLink(side))) {
										System.out.println(" YES\n- WAS IT ALREADY FOUND BEFORE?");
										if (!foundPieces.containsKey(currentPiece.checkLink(side))) {
											System.out.println(" NO, SO ADD IT TO THE NEXT STEPS!");
											foundPieces.put(currentPiece.checkLink(side), steps+1);
											piecesToCheck.add(currentPiece.checkLink(side));
											System.out.println("ADDED " + currentPiece.checkLink(side) + " - " + (steps+1));
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	 */
	
}
