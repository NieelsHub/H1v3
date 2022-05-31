package it.unibs.pajc.nieels.hive;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Placement;
import it.unibs.pajc.nieels.hive.Piece.Side;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;

import java.awt.BorderLayout;

//CONTROLLER
public class ClientMain {
	//VIEW
	private JFrame frame; //The application window, it has a content pane property, which is the area where the graphic components are put
	private JPanel contentPane;///////
	
	private JPanel pnlMainMenu;
	private JPanel pnlOptions;
	private JPanel pnlJoinGame;
	private JPanel pnlCreateGame;
	private JPanel pnlGame;

	//MODEL
	private Hive hive;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//Gets the system's look and feel
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e) {
			System.err.println(e);
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientMain window = new ClientMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		////////////////TEST (will then be on server main)
		/*
		LinkedHashMap <Class<?>, Integer> piecesSet = new LinkedHashMap();
		
		piecesSet.put(QueenBee.class, 1);
		piecesSet.put(Spider.class, 2);
		piecesSet.put(Beetle.class, 2);
		piecesSet.put(SoldierAnt.class, 3);
		piecesSet.put(Grasshopper.class, 3);
		
		Hive hive = new Hive(piecesSet);
		
		System.out.println(hive);
		
		System.out.println();
		
		hive.placeFirstPiece(hive.getBlacksToBePlaced().get(0));
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(0), hive.getPlacedPieces().get(0), Side.SOUTH);
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(0), hive.getPlacedPieces().get(1), Side.SOUTH);
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(0), hive.getPlacedPieces().get(2), Side.NORTHEAST);
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(5), hive.getPlacedPieces().get(3), Side.NORTHEAST);
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(4), hive.getPlacedPieces().get(2), Side.NORTHWEST);
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(4), hive.getPlacedPieces().get(2), Side.SOUTH);
		
		System.out.println(hive);
		
		Piece pc = hive.getPlacedPieces().get(4);
		System.out.print(pc.getName() + " " + pc.getColor() + "-" + pc.getId() + ": ");
		System.out.println(pc.getPossibleDirections());
		System.out.println();
		
		hive.movePiece(pc, Side.SOUTHWEST);
		
		System.out.println(hive);
		
		Piece pc2 = hive.getPlacedPieces().get(6);
		System.out.print(pc2.getName() + " " + pc2.getColor() + "-" + pc2.getId() + ": ");
		System.out.println(pc2.getPossibleDirections());
		System.out.println();
		
		hive.movePiece(pc2, Side.SOUTH);
		
		System.out.println(hive);
		*/
		//Do something that breaks the hive in two pieces
		
		///////////////TEST
	}

	/**
	 * Create the application.
	 */
	public ClientMain() {
		initialize(); //As a covention, the graphic components are created and put in the frame's content pane through the initialize method
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 700);
		frame.setMinimumSize(new Dimension(640, 480));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		///
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		/////
		pnlGame = new JPanel();
		frame.getContentPane().add(pnlGame);
		pnlGame.setLayout(new BorderLayout(0, 0));
		
		
		GameField gameField = new GameField(); //The View

		//////
		LinkedHashMap <Class<?>, Integer> piecesSet = new LinkedHashMap();
		piecesSet.put(QueenBee.class, 1);
		piecesSet.put(Spider.class, 2);
		piecesSet.put(Beetle.class, 2);
		piecesSet.put(SoldierAnt.class, 3);
		piecesSet.put(Grasshopper.class, 3);
		
		hive = new Hive(piecesSet);
		
		hive.placeFirstPiece(hive.getBlacksToBePlaced().get(0));
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(0), new Placement(hive.getPlacedPieces().get(0), Side.SOUTH));
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(0), new Placement(hive.getPlacedPieces().get(1), Side.SOUTH));
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(0), new Placement(hive.getPlacedPieces().get(2), Side.NORTHEAST));
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(5), new Placement(hive.getPlacedPieces().get(3), Side.NORTHEAST));
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(4), new Placement(hive.getPlacedPieces().get(2), Side.NORTHWEST));
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(4), new Placement(hive.getPlacedPieces().get(2), Side.SOUTH));
		//////////
		
		gameField.setHive(hive); //Sets the model in the view
		
		pnlGame.add(gameField, BorderLayout.CENTER);
		
		ToBePlacedField blacks = new ToBePlacedField(); //The View
		blacks.setHive(hive);
		blacks.setColor(PieceColor.BLACK);
		pnlGame.add(blacks, BorderLayout.NORTH);
		
		ToBePlacedField whites = new ToBePlacedField(); //The View
		whites.setHive(hive);
		whites.setColor(PieceColor.WHITE);
		pnlGame.add(whites, BorderLayout.SOUTH);
		
/******************DA FARE
		if (hive.getBlacksToBePlaced().size() <= 0) {
//			setMinimumSize(new Dimension(1, 1));
//			setPreferredSize(new Dimension(1, 1));
			blacks.setVisible(false);
		}
		else {
			blacks.setVisible(true);
		}
		
		if (hive.getWhitesToBePlaced().size() <= 0) {
//			setMinimumSize(new Dimension(1, 1));
//			setPreferredSize(new Dimension(1, 1));
			whites.setVisible(false);
		}
		else {
			whites.setVisible(true);
		}
	*******************/
	
		//Listeners
		//gameField.addPropertyChangeListener("mousePosition", e -> System.out.println(e));
		
		ChangeListener repaintAllGameComponents = e -> {
										for (Component component : pnlGame.getComponents()) {
											/*
											if (component instanceof HexField && component != e.getSource()) {
												((HexField)component).selectedPiece = null;
											}
											*/
											if (component instanceof JComponent) {
												((JComponent)component).repaint();
											}
										}
									 };
		
	 
		for (Component component : pnlGame.getComponents()) {
			if (component instanceof HexField) {
				((HexField)component).addChangeListener(repaintAllGameComponents);
			}
		}
		

//		ActionListener placedPieceClicked = e -> {
//												//make model calculate possible moves
//												if (e.getSource() != null) {
//													Object p = e.getSource();
//													if (e.getActionCommand() == "show_possible_moves") {
//														if (p instanceof Piece) {
//															hive.setSelectedPiece((Piece)p);
//														}
//														ArrayList<Placement> possibleMoves = hive.calculatePossibleMoves(hive.getSelectedPiece());
//														hive.setPossiblePositions(possibleMoves);
//														System.out.println(e.getActionCommand() + ": " + ((Piece)e.getSource()).getName() + " can move on " + possibleMoves);
//														//I would trigger the possible moves showing in the view from here, if it was possible to do so without having to pass the Graphics2D object
//													} else if (e.getActionCommand() == "no_piece_selected") {
//														hive.setSelectedPiece(null);
//														hive.setPossiblePositions(null);
//														System.out.println(e.getActionCommand());
//														
//													} else if (e.getActionCommand() == "placement_selected") {
//														Object pl = e.getSource();
//														if (pl instanceof Placement) {
//															hive.movePiece(hive.getSelectedPiece(), (Placement)pl);
//														}
//														hive.setSelectedPiece(null);
//														hive.setPossiblePositions(null);
//														System.out.println(e.getActionCommand() + ": " + e.getSource());
//													}
//												}
//											 };
									 
		ActionListener pieceSelected = e -> {
													//make model calculate possible moves
													if (e.getSource() != null) {
														Object p = e.getSource();
														if (e.getActionCommand() == "show_possible_positions") {
															if (p instanceof Piece) {
																hive.setSelectedPiece((Piece)p);
															}
															
															ArrayList<Placement> possiblePositions;
															
															if (hive.getPlacedPieces().contains(p)) {
																possiblePositions = hive.calculatePossibleMoves(hive.getSelectedPiece());
															}
															else if (hive.getBlacksToBePlaced().contains(p) || hive.getWhitesToBePlaced().contains(p)){
																possiblePositions = hive.calculatePossiblePlacements(hive.getSelectedPiece());
															}
															else {
																possiblePositions = null;
															}
															hive.setPossiblePositions(possiblePositions);
															System.out.println(((Piece)e.getSource()).toStringLong());
															System.out.println(e.getActionCommand() + ": " + ((Piece)e.getSource()).getName() + " can move on " + possiblePositions);
															//I would trigger the possible moves showing in the view from here, if it was possible to do so without having to pass the Graphics2D object
														}
													}
												};
												 									 
		 
		ActionListener nothingSelected = e -> {
												if (e.getActionCommand() == "no_piece_selected") {
														hive.setSelectedPiece(null);
														hive.setPossiblePositions(null);
														System.out.println(e.getActionCommand());
												}
										  };
										  
		
		ActionListener possiblePositionSelected = e -> {
															if (e.getActionCommand() == "position_selected") {
																	Object p = e.getSource();
																	if (p instanceof Placement) {
																		if(hive.getPlacedPieces().contains(hive.getSelectedPiece())) {
																			hive.movePiece(hive.getSelectedPiece(), (Placement)p);
																		}
																		else {
																			hive.placeNewPiece(hive.getSelectedPiece(), (Placement)p);
																		}
																	}
																	hive.setSelectedPiece(null);
																	hive.setPossiblePositions(null);
																	System.out.println(e.getActionCommand() + ": " + e.getSource());
																}
														 };
											 
											 
		 for (Component component : pnlGame.getComponents()) {
				if (component instanceof HexField) {
					((HexField)component).addActionListener(pieceSelected);
					((HexField)component).addActionListener(nothingSelected);
					((HexField)component).addActionListener(possiblePositionSelected);
				}
			}
		
	}
	
}
