package it.nieels.unibs.pajc.h1v3.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import it.nieels.unibs.pajc.h1v3.model.Piece.PieceColor;
import it.nieels.unibs.pajc.h1v3.model.Piece.Placement;
import it.nieels.unibs.pajc.h1v3.model.Piece.Side;

import java.util.Queue;

/**
 * Implements the logic of the Spider: it inherits from the SoldierAnt because it has a similar movement logic.
 * @author Nicol Stocchetti
 *
 */
public class Spider extends SoldierAnt {

	public final static String PIECE_NAME = "SPIDER";
	public final static boolean VERTICAL_MOVEMENT = false;


	/**
	 * The constructor, it automatically assigns the piece name and the capability to move vertically to his super Piece's attributes.
	 * @param color the color of this piece's team, PieceColor.
	 */
	public Spider(PieceColor color) {
		super(color, VERTICAL_MOVEMENT, PIECE_NAME);
	}
	
	@Override
	boolean canBeReachedBFS(Piece piece, ArrayList<Piece> hiveSurroundings) {
		ArrayList <Step> foundSteps = new ArrayList <Step>();
		Queue<Step> stepsToCheck = new ArrayDeque <Step>();
		Step currentStep;
		Step nextStep;
		Piece nextPiece;
		boolean foundEquivalent;
		
		//System.out.println("STARTING PIECE: " + piece);
		nextStep = new Step(piece, piece, 0);
		foundSteps.add(nextStep);
		stepsToCheck.add(nextStep);
		
		while (!stepsToCheck.isEmpty()) {
			currentStep = stepsToCheck.remove();
			
			//System.out.println("LOOKING AT " + currentStep);
			
			for (Side side : Side.values()) {
				if (currentStep.piece.checkLink(side.previous()) == null || currentStep.piece.checkLink(side.next()) == null
					|| hiveSurroundings.contains(currentStep.piece.checkLink(side.previous()))
					|| hiveSurroundings.contains(currentStep.piece.checkLink(side.next()))
					|| currentStep.piece.checkLink(side.previous()).getId() == this.getId()
					|| currentStep.piece.checkLink(side.next()).getId() == this.getId()) {
					
					//System.out.println(" - CAN SQUEEZE THROUGH");
					
					nextPiece = currentStep.piece.checkLink(side);
					if (nextPiece != null) {
						
						//System.out.println(" - THERE'S A VALID PIECE MOVING THROUGH");
						
						//System.out.println("- DO THIS STEP (FROM WHICH I WANT TO MOVE FORWARD) AND THE NEXT HAVE AT LEAST A LINKED HIVE PIECE IN COMMON?");
						for (Piece linkedPieceCurrentStep : currentStep.piece.getLinkedPieces().values()) {
							for (Piece linkedPieceNextStep : nextPiece.getLinkedPieces().values()) {
								if (linkedPieceCurrentStep == linkedPieceNextStep && !hiveSurroundings.contains(linkedPieceCurrentStep) && linkedPieceCurrentStep.getId() != this.getId()) {
									//System.out.println(" - YES, THERE'S A COMMON PIECE");
									//System.out.println(" - IS THE NEXT PIECE THE SPIDER?");
									if (nextPiece.getId() == this.getId()) {
										//System.out.println("YES\n- IS IT AT A RIGHT DISTANCE?");
										if (currentStep.stepsTaken == 2) {
											//System.out.println(" - YES! THIS IS A VALID PLACEMENT!");
											return true;
										}
									}
									//System.out.println(" - IS THE NEXT PIECE PART OF THE HIVE SURROUNDINGS (VALID NEXT STEPS)? ARE WE MOVING BACK FROM WHERE WE CAME (ILLEGAL MOVE)? ARE WE UNDER 3 STEPS?");
									if (hiveSurroundings.contains(nextPiece) && nextPiece != currentStep.previousPiece && currentStep.stepsTaken+1 < 3 ) {
										//System.out.println(" YES\n- WAS AN EQUIVALENT STEP ALREADY FOUND BEFORE?");
										nextStep = new Step(nextPiece, currentStep.piece, currentStep.stepsTaken+1);
										
										foundEquivalent = false;
										for(Step foundStep : foundSteps) {
											if (foundStep.piece == nextStep.piece && foundStep.stepsTaken == nextStep.stepsTaken) {
												foundEquivalent = true;
												//System.out.println(" YES, WON'T ADD THIS TO THE NEXT STEPS");
											}
										}
										
										if (!foundEquivalent) {
											//System.out.println(" NO, SO ADD IT TO THE NEXT STEPS!");
											foundSteps.add(nextStep);
											stepsToCheck.add(nextStep);
											//System.out.println("ADDED " + nextStep);
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
	
	//Inner class
	/**
	 * Describes a step in the Spider's movement across the hive, with a current piece (position), a previous piece (position) and
	 * the number of steps already taken to get there.
	 * @author Nicol Stocchetti
	 *
	 */
	public class Step {
		private Piece piece;
		private Piece previousPiece; //or Step previousStep?
		private int stepsTaken;
		
		/**
		 * The Step's constructor.
		 * @param piece the current position, Piece.
		 * @param previousPiece the previous position (from which the spider came), Piece.
		 * @param stepsTaken the number of steps already taken to get at this position, int.
		 */
		public Step(Piece piece, Piece previousPiece, int stepsTaken) {
			this.piece = piece;
			this.previousPiece = previousPiece;
			this.stepsTaken = stepsTaken;
		}
		
		@Override
		public String toString() {
			return piece.toString() + " from " + previousPiece.toString() + " at step " + stepsTaken;
		}
	}
}
