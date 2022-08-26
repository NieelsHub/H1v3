package it.unibs.pajc.nieels.hive;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Placement;
import it.unibs.pajc.nieels.hive.Piece.Side;

//MODEL

/**
 * Modelization of the hive (a compostiton of all the pieces in the game and their relations).
 * @author Nicol Stocchetti
 *
 */
public class Hive implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Piece> placedPieces = new ArrayList<Piece>();
	private ArrayList<Piece> whitesToBePlaced = new ArrayList<Piece>();
	private ArrayList<Piece> blacksToBePlaced = new ArrayList<Piece>();
	
	private Piece selectedPiece;
	private ArrayList<Placement> possiblePositions;
	
	/**
	 * The Hive's constructor.
	 * @param piecesSet a map containing what kind of pieces are to be generated for the game and their number, LinkedHashMap.
	 */
	public Hive(LinkedHashMap <Class<?>, Integer> piecesSet) {
		Class<?> pieceKind;
		int numberOfPieces;
		int i;
		
		for (Entry<Class<?>, Integer> entry : piecesSet.entrySet()) {
			pieceKind = entry.getKey();
			numberOfPieces = entry.getValue();
			
			try {
				Constructor<?> constructor =  pieceKind.getConstructor(PieceColor.class);//PieceColor.class returns the Class object associated to the type PieceColor.
				for(i = 0; i < numberOfPieces; i++ ) {
					whitesToBePlaced.add((Piece)constructor.newInstance(PieceColor.WHITE));
					blacksToBePlaced.add((Piece)constructor.newInstance(PieceColor.BLACK));
				}
			}
			catch(NoSuchMethodException e) {
				System.err.println("The specified constructor doesn't exist!");
				e.printStackTrace();
			}
			catch(Exception e) {
				System.err.println("Unknown error.");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns the pieces which are already placed, being part of the hive.
	 * @return all the pieces that compose the hive, ArrayList<Piece>.
	 */
	public ArrayList<Piece> getPlacedPieces() {
		return placedPieces;
	}

	/**
	 * Returns all the white pieces that are yet to be placed in the hive.
	 * @return all the white pieces that are yet to be placed in the hive, ArrayList<Piece>.
	 */
	public ArrayList<Piece> getWhitesToBePlaced() {
		return whitesToBePlaced;
	}

	/**
	 * Returns all the black pieces that are yet to be placed in the hive.
	 * @return all the black pieces that are yet to be placed in the hive, ArrayList<Piece>.
	 */
	public ArrayList<Piece> getBlacksToBePlaced() {
		return blacksToBePlaced;
	}
	
	/**
	 * Returns the piece that's currently selected.
	 * @return the selected piece (or null if there's no piece selected), Piece.
	 */
	public Piece getSelectedPiece() {
		return selectedPiece;
	}

	/**
	 * Sets a selected piece.
	 * @param selectedPiece the piece to be selected (or null if there's no piece to be selected), Piece.
	 */
	public void setSelectedPiece(Piece selectedPiece) {
		this.selectedPiece = selectedPiece;
	}

	/**
	 * Returns the possible movements for the currently selected piece.
	 * @return the possible new positions for the selected piece, ArrayList<Placement>.
	 */
	public ArrayList<Placement> getPossiblePositions() {
		return possiblePositions;
	}

	/**
	 * Sets the possible movements for the currently selected piece.
	 * @param possiblePositions the possible new positions for the selected piece, ArrayList<Placement>.
	 */
	public void setPossiblePositions(ArrayList<Placement> possiblePositions) {
		this.possiblePositions = possiblePositions;
	}

	/**
	 * Positions the first piece of the game and sets the games' coordinate system at its center.
	 * @param piece the piece to be set as the starting point of the hive, Piece.
	 */
	public void placeFirstPiece(Piece piece) {
		if(placedPieces.isEmpty()) {
			piece.getCoordinates().setLocation(0.0, 0.0);
			//piece.setInGame(true);
			placedPieces.add(piece);
			removeFromPiecesToBePlaced(piece);
		} else {
			//exception if first already placed?
		}
	}
	
	/**
	 * Given a piece yet to be placed, calculates its possible placements around the hive.
	 * @param piece the piece, Piece.
	 * @return the list of possible placements, ArrayList<Placement>.
	 */
	public ArrayList<Placement> calculatePossiblePlacements(Piece piece) {
		ArrayList<Placement> placements = new ArrayList<Placement>();
		
		if (!blacksToBePlaced.contains(piece) && !whitesToBePlaced.contains(piece)) {
			return null;
		}
		
		if (placedPieces.contains(piece)) {
			//Exception?
			System.err.println(piece.getName() + " " + piece.getColor() + "-" + piece.getId()
			+ ": The selected piece is both yet to be placed AND part of the hive! - a critical error occurred.");
			return null;
		}
		
		Placement placement;
		Piece bottomPiece;
		for (Piece hivePiece : placedPieces) {
			if (hivePiece.getColor() == piece.getColor() || placedPieces.size() < 2) {
				//Pieces which are laid on top of other pieces don't have links, to check their surroundings they consider the links of the base piece of the stack under them
				bottomPiece = hivePiece;
				while (bottomPiece != null && bottomPiece.getBottomPiece() != null) {
					bottomPiece = bottomPiece.getBottomPiece();
				}
				
				for (Side side : Side.values()) {
					if (bottomPiece.checkLink(side) == null) {
						placement = new Placement(bottomPiece, side);
						if (placedPieces.size() < 2 || checkSurroundingPiecesSameColor(placement, piece.getColor())) {
							placements.add(placement);
						}
					}
				}
			}
		}
		
		//System.out.println("TO BE PLACED");
		return placements;
	}
	
	/**
	 * Places a new piece next to another one that's already part of the hive. TO DO: CHECK QUEEN BEE PLACEMENT AFTER THIRD
	 * @param piece the piece to be positioned, Piece.
	 * @param placement where to be positioned, Placement.
	 */
	public void placeNewPiece(Piece piece, Placement placement) {
		//Checks if the chosen pieces are already part of the hive: the chosen neighbor must be
		//part of the hive, while the chosen piece must be free.
		
		if(isInHive(piece)) {
			System.err.println(piece.getName() + " " + piece.getColor() + "-" + piece.getId() + ", " + placement.getPositionOnNeighbor()
					+ ": The selected piece is already part of the hive, use movePiece to move it! - placement not executed.");
			return; //exception?
		}
		
		if (!canBePlacedOnNeighbor(piece, placement)) {
			return;
		}
		
	
		if (placedPieces.size()>=2 && !checkSurroundingPiecesSameColor(placement, piece.getColor())) {
			System.err.println(piece.getName() + " " + piece.getColor() + "-" + piece.getId() + ", " + placement.getPositionOnNeighbor()
					+ ": The piece can't be positioned near other pieces of a different color. - placement not executed.");
			return;
		}
		//Sets the coordinates of the piece (relative to the starting piece of the hive).
		piece.setRelativeCoordinates(placement.getNeighbor(), placement.getPositionOnNeighbor());
		
		//We also have to check and update the links with other surrounding pieces, if there are any	
		linkToSurroundingPieces(piece);
		placedPieces.add(piece);
		removeFromPiecesToBePlaced(piece);
		//piece.setInGame(true);
	}
	
	/**
	 * Checks whether the specified piece can be placed on the neighboring piece specified by the placement on the specified side.
	 * @param piece the piece to be placed, Piece.
	 * @param placement where there's the need to check if it can be placed, Placement.
	 * @return true if the placement can be done, false otherwise, boolean.
	 */
	private boolean canBePlacedOnNeighbor(Piece piece, Placement placement) {
		try {
		if(placedPieces.size() > 0 && !isInHive(placement.getNeighbor())) {
			System.err.println(piece.getName() + " " + piece.getColor() + "-" + piece.getId() + ", " + placement.getPositionOnNeighbor() 
					+ ": The selected neighbor (" + placement.getNeighbor() +") is not part of the hive! - placement not executed.");
			return false; //exception?
		}
		//checks if the chosen neighbor already has a link on that side, in this case it's not possible to continue the placement
		if(placement.getNeighbor().checkLink(placement.getPositionOnNeighbor()) != null) {
			//Unless the placement is a lift!
			if (placement.getNeighbor() != piece) { //Is not a lift
				System.err.println(piece.getName() + " " + piece.getColor() + "-" + piece.getId() + ", " + placement.getPositionOnNeighbor()
				+ ": There's already another piece in the selected place (" + placement.getNeighbor().checkLink(placement.getPositionOnNeighbor()).toString()
				+ ") - placement not executed.");
				return false;
			}
		}
		return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("PIECE EXCEPTION: " + piece);
			return true;
		}
	}
	
	/**
	 * Checks whether a piece is currently part of the hive or not.
	 * @param piece the piece to check, Piece.
	 * @return true if the piece is in the hive, false otherwise, boolean.
	 */
	private boolean isInHive(Piece piece) {
		for(Piece hivePiece : placedPieces) {
			if(hivePiece == piece)
				return true;
		}
		return false;
	}
	
	/**
	 * Checks for a specific player color if their queen bee has already been placed in the hive.
	 * @param color the color for which to check the presence of the queen in the hive, PieceColor.
	 * @return true if the queen has already been placed, false otherwise, boolean.
	 */
	private boolean isQueenInHive(PieceColor color) {
		for(Piece hivePiece : placedPieces) {
			if(hivePiece instanceof QueenBee && hivePiece.getColor() == color)
				return true;
		}
		return false;
	}
	
	/**
	 * Removes a specific piece from the corresponding color's list of pieces available to be placed.
	 * @param piece the piece to remove, Piece.
	 */
	private void removeFromPiecesToBePlaced(Piece piece) {
		if (piece.getColor() == PieceColor.BLACK) {
			blacksToBePlaced.remove(piece);
		} else {
			whitesToBePlaced.remove(piece);
		}
	}
	
	/**
	 * Given a piece with valid position coordinates, finds all the surrounding pieces and creates links between them.
	 * @param piece the piece that needs to be linked to his neighbors, Piece.
	 */
	private void linkToSurroundingPieces(Piece piece) {
		double surroundingX;
		double surroundingY;
		double epsilon = 0.3;
		
		//check each adjacent side of the piece's coordinates
		for(Side side : Side.values()) {
			surroundingX = piece.getCoordinates().getX() + side.xOffset;
			surroundingY = piece.getCoordinates().getY() + side.yOffset;
			//Is there a piece in the base level hive (no stacks) with this coordinates?
			for(Piece hivePiece : placedPieces) {
				if(Math.abs(hivePiece.getCoordinates().getX() - surroundingX) < epsilon
				&& Math.abs(hivePiece.getCoordinates().getY() - surroundingY) < epsilon
				&& hivePiece.getBottomPiece() == null) { //base of the pile
					piece.linkTo(hivePiece, side.opposite());
				}
			}
		}
	}
	
	/**
	 * Checks whether all the surrounding pieces are of the same color as the specified piece.
	 * @param piece the piece whose surroundings are to be checked, Piece.
	 * @return true if the pieces are all the same color, else false, boolean.
	 */
	private boolean checkSurroundingPiecesSameColor(Piece piece) {
		Piece bottomPiece;
		Piece topPiece;
		
		//Pieces which are laid on top of other pieces don't have links, to check their surroundings they consider the links of the base piece of the stack under them
		bottomPiece = piece;
		while (bottomPiece != null && bottomPiece.getBottomPiece() != null) {
			bottomPiece = bottomPiece.getBottomPiece();
		}
		
		for (Piece surroundingPiece : bottomPiece.getLinkedPieces().values()) {
			topPiece = surroundingPiece;
			while (topPiece != null && topPiece.getTopPiece() != null) {
				topPiece = topPiece.getTopPiece();
			}
			if (topPiece.getColor() != piece.getColor()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks whether all the pieces surrounding a placement are of the specified color.
	 * @param placement the placement whose surroundings are to be checked, Placement.
	 * @param color the color the pieces must match, PieceColor.
	 * @return true if all the pieces match the color, else false, boolean.
	 */
	private boolean checkSurroundingPiecesSameColor(Placement placement, PieceColor color) {
		double surroundingX;
		double surroundingY;
		double epsilon = 0.3;
		
		double newPositionX = placement.getNeighbor().getCoordinates().getX() + placement.getPositionOnNeighbor().xOffset;
		double newPositionY = placement.getNeighbor().getCoordinates().getY() + placement.getPositionOnNeighbor().yOffset;
		
		//check each adjacent side of the new piece's coordinates
		for(Side side : Side.values()) {
			surroundingX = newPositionX + side.xOffset;
			surroundingY = newPositionY + side.yOffset;
			//Is there a top piece in the hive with this coordinates?
			for(Piece hivePiece : placedPieces) {
				if(Math.abs(hivePiece.getCoordinates().getX() - surroundingX) < epsilon
				&& Math.abs(hivePiece.getCoordinates().getY() - surroundingY) < epsilon
				&& hivePiece.getTopPiece() == null //is a top piece
				&& hivePiece.getColor() != color) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	//Già predisposto per essere messo come classe a parte per essere svolto parallelamente in più thread da chi lo richiama!
	/**
	 * Starting from a certain piece, checks whether all the hive pieces are connected.
	 * @param startingPiece the piece from which to start checking, Piece.
	 * @return true if the hive is all connected, else false, boolean.
	 */
	private boolean checkHiveCohesion(Piece startingPiece) {
		return checkHiveCohesion(startingPiece, new ArrayList<Piece>());
	}
	
	/**
	 * Starting from a certain piece, checks whether all the hive pieces are connected.
	 * @param startingPiece the piece from which to start checking, Piece.
	 * @param excludedPiece a piece excluded from the hive search, Piece.
	 * @return true if the hive is all connected, else false, boolean.
	 */
	private boolean checkHiveCohesion(Piece startingPiece, Piece excludedPiece) {
		ArrayList<Piece> excludedPieces = new ArrayList<Piece>();
		excludedPieces.add(excludedPiece);
		return checkHiveCohesion(startingPiece, excludedPieces);
	}
	
	/**
	 * Starting from a certain piece, checks whether all the hive pieces are connected.
	 * @param startingPiece the piece from which to start checking, Piece.
	 * @param excludedPieces the pieces excluded from the hive search, ArrayList<Piece>.
	 * @return true if the hive is all connected, else false, boolean.
	 */
	private boolean checkHiveCohesion(Piece startingPiece, ArrayList<Piece> excludedPieces) {
		ArrayList<Piece> includedPieces = new ArrayList<Piece>();
		Piece currentPiece = startingPiece;
		
		//System.out.println("EXCLUDED PIECES: " + excludedPieces);
		System.out.print("STARTING PIECE: " + startingPiece);
		
		if (excludedPieces.containsAll(placedPieces)) {
			System.err.println("I PEZZI ESCLUSI SONO TUTTO L'HIVE!");
			//?exception?
		}
		
		for (Piece piece : placedPieces) {
			if (!excludedPieces.contains(piece) && piece.getBottomPiece() == null) { //Only the pieces at the base of stacks provide linking information
				includedPieces.add(piece);
			}
		}
		
		if (excludedPieces.contains(currentPiece)) {
			currentPiece = includedPieces.get(0);
		}
		
		//System.out.println(" -> " + currentPiece);
		
		return modifiedDFS (currentPiece, currentPiece, new ArrayList <Piece>(), excludedPieces, includedPieces);
	}
	
	/**
	 * Starting from a certain piece, checks whether all the hive pieces are connected using a recursive modified version
	 * of the Depth First Search algorithm for checking graph connection.
	 * @param currentPiece the piece from which the graph search is currently moving forward, Piece.
	 * @param visitedPieces the pieces already checked so far in the past iterations, ArrayList<Piece>.
	 * @param excludedPieces the pieces excluded from the hive search, ArrayList<Piece>.
	 * @param includedPieces the pieces included in the hive search, ArrayList<Piece>.
	 * @return true if the hive graph is connected, else false, boolean.
	 */
	private boolean modifiedDFS(Piece startingPiece, Piece currentPiece, ArrayList<Piece> visitedPieces, ArrayList<Piece> excludedPieces, ArrayList<Piece> includedPieces) {
		boolean advancementDone = true;
		
		while (advancementDone) {
			
			if (Thread.interrupted()) {
		        //The task has been interrupted: https://docs.oracle.com/javase/tutorial/essential/concurrency/interrupt.html
				Thread.currentThread().interrupt(); //Resetting the consumed interrupted flag: https://stackoverflow.com/questions/60905869/understanding-thread-interruption-in-java?noredirect=1&lq=1
				//System.out.println("Thread " + Thread.currentThread().getName() + " interrupted");
				return false;
		    }
			
			visitedPieces.add(currentPiece);
			
//			System.out.println("VISITED: " + currentPiece.getName() + " " + currentPiece.getColor() + "-" + currentPiece.getId());
//			System.out.print("\tVISITED PIECES: ");
//			int i = 0;
//			for(Piece piece : visitedPieces) {
//				System.out.print(piece.getName() + " " + piece.getColor() + "-" + piece.getId() + " ");
//				i++;
//				if (i%4 == 0) {
//					System.out.print("\n\t");
//				}
//			}
//			System.out.println();
			
			advancementDone = false;
			for (Piece linkedPiece : currentPiece.getLinkedPieces().values()) {
				if (!visitedPieces.contains(linkedPiece) && !excludedPieces.contains(linkedPiece)) {
					currentPiece = linkedPiece;
					advancementDone = true;
					//System.out.println("VISITING: " + currentPiece.getName() + " " + currentPiece.getColor() + "-" + currentPiece.getId());
					break;
				}
			}
		}		
		
		//System.out.println("CHECKING COHESION...");
		
		if (visitedPieces.containsAll(includedPieces)) {
			//System.out.println("Thread " + Thread.currentThread().getName() + ": THE GRAPH STARTING FROM " + startingPiece + " IS COHESE " + System.currentTimeMillis());
			return true;
		}
		else {
			for (Piece piece : includedPieces) {
				if (!visitedPieces.contains(piece)) {
					for (Piece linkedPiece : piece.getLinkedPieces().values()) {
						if (visitedPieces.contains(linkedPiece) && !excludedPieces.contains(piece)) {
							//System.out.println("STARTING FURTHER EXPLORATION FROM " + piece.getName() + " " + piece.getColor() + "-" + piece.getId() + " THAT'S LINKED TO " + linkedPiece.getName() + " " + linkedPiece.getColor() + "-" + linkedPiece.getId());
							/*
							double tot = 0;
							for (int i = 0; i < 10000000; i ++) {
								tot = tot + Math.random();
							}
							
							System.out.println("Thread " + Thread.currentThread().getName() + ": " + tot);
							*/
							
							return modifiedDFS(startingPiece, piece, visitedPieces, excludedPieces, includedPieces);
						}
					}
					//System.out.println(piece.getName() + " " + piece.getColor() + "-" + piece.getId() + " CAN'T BE REACHED!");
				}
			}
			//System.out.println("Thread " + Thread.currentThread().getName() + ": THE GRAPH STARTING FROM " + startingPiece + " IS NOT COHESE " + System.currentTimeMillis());
			return false;
		}
	}
	
	/**
	 * Checks whether all the hive pieces are connected, exploring the hive from different starting pieces in asynchronous tasks
	 * and returning the first ending task's findings.
	 * @param piecesInterval the number of pieces in the hive after which to create a new exploring task.
	 * @return true if the hive is all connected, else false, boolean.
	 */
	private boolean checkHiveCohesionMultithread(int piecesInterval) {
		return checkHiveCohesionMultithread(piecesInterval, new ArrayList<Piece>());
	}
	
	/**
	 * Checks whether all the hive pieces are connected, exploring the hive from different starting pieces in asynchronous tasks
	 * and returning the first ending task's findings.
	 * @param piecesInterval the number of pieces in the hive after which to create a new exploring task.
	 * @param excludedPiece a piece excluded from the hive search, Piece.
	 * @return true if the hive is all connected, else false, boolean.
	 */
	private boolean checkHiveCohesionMultithread(int piecesInterval, Piece excludedPiece) {
		ArrayList<Piece> excludedPieces = new ArrayList<Piece>();
		excludedPieces.add(excludedPiece);
		return checkHiveCohesionMultithread(piecesInterval, excludedPieces);
	}
	
	/**
	 * Checks whether all the hive pieces are connected, exploring the hive from different starting pieces in asynchronous tasks
	 * and returning the first ending task's findings.
	 * @param piecesInterval the number of pieces in the hive after which to create a new exploring task.
	 * @param excludedPieces the pieces excluded from the hive search, ArrayList<Piece>.
	 * @return true if the hive is all connected, else false, boolean.
	 */
	private boolean checkHiveCohesionMultithread(int piecesInterval, ArrayList<Piece> excludedPieces) {
		ArrayList<Piece> taskStartingPieces = new ArrayList<Piece>();
		boolean result = false;
		//int numberOfTasks = placedPieces.size() / piecesInterval;
		
		for (int i = 0; i < placedPieces.size(); i += piecesInterval) {
			taskStartingPieces.add(placedPieces.get(i));
		}
		
		//System.out.println(taskStartingPieces);
		
		ExecutorService executor = Executors.newCachedThreadPool(); //Maybe make this executor a class
		//attribute so it can stay always alive and the program can work with only one executor creation, instead of
		//shutting down the executor just cancel all tasks created in this method each time.
		//This method of cancelling tasks and not shutting down the service helps in re-using the service across
		//multiple requests. In such situations you may want to shutdown the service only on shutdown of your application.
		//https://dzone.com/articles/understanding-thread-interruption-in-java
		
		ArrayList<Callable<Boolean>> tasks = new ArrayList<>();
		
		for (Piece p : taskStartingPieces) {
			tasks.add(() -> checkHiveCohesion(p, excludedPieces));
		}
		
		//System.out.println(tasks);
		
		try {
			result = executor.invokeAny(tasks);
			executor.shutdownNow();
			//System.out.println("Executor stopped " + System.currentTimeMillis());
			//System.out.printf("COHESION HAS BEEN FOUND TO BE : %b\n", result);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} finally {
			//Close the executor service
			executor.shutdownNow();
		}
		
		//System.out.println("Method finished " + System.currentTimeMillis());
		return result;
	}
	
	/**
	 * Returns all the possible placements for a piece's next movement.
	 * @param piece the piece, Piece.
	 * @return the possible placements, ArrayList<Placement>.
	 */
	public ArrayList<Placement> calculatePossibleMoves(Piece piece) {
		if (!isQueenInHive(piece.getColor())) {
			return null;
		}
		
		if (!piece.canMove()) {
			System.out.println("The selected piece is surrounded and can't move - movement not executed.");
			return null;
		}
		
		//Check if the hive will still be connected after moving the piece from its current position
		if (!checkHiveCohesionMultithread(4, piece)/*checkHiveCohesion(placedPieces.get(0), piece)*/) {
			return null;
		}
		
		return piece.calcPossibleMoves();
	}
	
	/**
	 * Moves the given piece at the given location.
	 * @param piece the piece to move, Piece.
	 * @param placement the location at which the piece must be moved, Placement.
	 */
	public void movePiece(Piece piece, Placement placement) {
		Piece newBottomPiece = null;
		Piece basePiece;
		
		if(!isInHive(piece)) {
			System.err.println("The selected piece is not part of the hive, you have to place it first! - movement not executed.");
			return;
		}
		
		if (!canBePlacedOnNeighbor(piece, placement)) {
			return;
		}
		
		if (placement.getNeighbor() == piece) {//The movement is a lift
			//Pieces which are laid on top of other pieces don't have links, to check their surroundings they consider the links of the base piece of the stack under them
			basePiece = piece;
			while (basePiece != null && basePiece.getBottomPiece() != null) {
				basePiece = basePiece.getBottomPiece();
			}
			
			newBottomPiece = basePiece.getLinkedPieces().get(placement.getPositionOnNeighbor());
			while (newBottomPiece != null && newBottomPiece.getTopPiece() != null) {
				newBottomPiece = newBottomPiece.getTopPiece();
			}
		}
		
		piece.resetLinks();
		piece.setRelativeCoordinates(placement.getNeighbor(), placement.getPositionOnNeighbor());
		if(newBottomPiece != null) {
			newBottomPiece.setTopPiece(piece);
			piece.setBottomPiece(newBottomPiece);
		}
		if (piece.getBottomPiece() == null) {
			linkToSurroundingPieces(piece);//Only base level pieces are linked to other pieces, the other pieces use the same links of their base
		}
	}

	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
		
		toString.append("\n - HIVE -\n");
		for(Piece piece : this.getPlacedPieces()) {
			toString.append(piece + "\n\n");
		}
		
		toString.append(" - BLACKS TO BE PLACED -\n");
		for(Piece piece : this.getBlacksToBePlaced()) {
			toString.append(piece + "\n");
		}
		
		toString.append("\n - WHITES TO BE PLACED -\n");
		for(Piece piece : this.getWhitesToBePlaced()) {
			toString.append(piece + "\n");
		}
		
		return toString.toString();
	}
	
}
