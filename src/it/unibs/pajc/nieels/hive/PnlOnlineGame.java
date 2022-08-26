package it.unibs.pajc.nieels.hive;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Placement;
import it.unibs.pajc.nieels.hive.Piece.Side;

public class PnlOnlineGame extends EventJPanel {
	static final String ONLINE_GAME_TAG = "ONLINE_GAME";
	
	static final String BACK_BTN = "BACK";
	static final String PASS_BTN = "PASS";
	
	static final String MOVE_MADE_EVENT = "MOVE_MADE";
	
	static final Color LIGHT_BACKGROUND_COLOR_ON = new Color(255, 255, 200);
	static final Color LIGHT_BACKGROUND_COLOR_OFF = new Color(200, 200, 200);
	static final Color DARK_BACKGROUND_COLOR_ON = Color.ORANGE;
	static final Color DARK_BACKGROUND_COLOR_OFF = new Color(155, 155, 155);
	
	private JLabel lblTitle;
	
	private JPanel pnlGame;
		private ToBePlacedField opponentPieces;
		private GameField gameField;
		private ToBePlacedField playerPieces;
	
	private JPanel pnlButtons;
		private Component verticalStrut;
		private JButton btnBack;
		private Component verticalStrut_1;
		private JButton btnPass;
		private Component verticalStrut_2;
	
	
	private Hive hive;
	PieceColor playerColor;
	PieceColor opponentColor;
	
	ActionListener pieceSelected;
	ActionListener nothingSelected;
	ActionListener possiblePositionSelected;
	
	ChangeListener repaintAllGameComponents;
		
	public PnlOnlineGame(Hive hive, PieceColor playerColor) {

		this.hive = hive;
		this.playerColor = playerColor;
		
		opponentColor = playerColor == PieceColor.BLACK ? PieceColor.WHITE : PieceColor.BLACK;
		
		setBackground(DARK_BACKGROUND_COLOR_ON);
		setLayout(new BorderLayout(0, 0));
		
		/*
		lblTitle = new JLabel(ONLINE_GAME_TAG);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Papyrus", Font.BOLD, 30));
		lblTitle.setForeground(Color.RED);
		this.add(lblTitle, BorderLayout.NORTH);
		*/
		
		pnlGame = new JPanel();
		pnlGame.setBackground(LIGHT_BACKGROUND_COLOR_ON);
		this.add(pnlGame, BorderLayout.CENTER);
		pnlGame.setLayout(new BorderLayout(0, 0));
		
		//Game area
		gameField = new GameField(); //The main game GUI component
		gameField.setHive(hive); //Sets the model in the view
		gameField.setBackground(LIGHT_BACKGROUND_COLOR_ON);
		pnlGame.add(gameField, BorderLayout.CENTER);
		
		//Piece selection areas
		opponentPieces = new ToBePlacedField(); //The View
		opponentPieces.setHive(hive);
		opponentPieces.setColor(opponentColor);
		opponentPieces.setBackground(LIGHT_BACKGROUND_COLOR_ON);
		pnlGame.add(opponentPieces, BorderLayout.NORTH);
		
		playerPieces = new ToBePlacedField(); //The View
		playerPieces.setHive(hive);
		playerPieces.setColor(playerColor);
		playerPieces.setBackground(LIGHT_BACKGROUND_COLOR_ON);
		pnlGame.add(playerPieces, BorderLayout.SOUTH);
		
		
		//Button area
		pnlButtons = new JPanel();
		pnlButtons.setBackground(DARK_BACKGROUND_COLOR_ON);
		this.add(pnlButtons, BorderLayout.EAST);
		pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.Y_AXIS));
		
		verticalStrut = Box.createVerticalStrut(15);
		pnlButtons.add(verticalStrut);
		
		btnBack = new JButton(BACK_BTN);
		btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlButtons.add(btnBack);
		
		verticalStrut_1 = Box.createVerticalStrut(15);
		pnlButtons.add(verticalStrut_1);
		
		btnPass = new JButton(PASS_BTN);
		btnPass.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlButtons.add(btnPass);
		
		verticalStrut_2 = Box.createVerticalStrut(15);
		pnlButtons.add(verticalStrut_2);
		
		//MOUSE LISTENERS
		//Change listeners
		createChangeListeners();
		
		//Action listeners
		createActionListeners(hive);
		
		start();
		
		//BUTTONS LISTENERS
		btnBack.addActionListener(e -> fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand(), e.getWhen(), e.getModifiers())));
		
		btnPass.addActionListener(e -> fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand(), e.getWhen(), e.getModifiers())));
		
	}
	
	private void createChangeListeners() {
		/*ChangeListener*/ repaintAllGameComponents = e -> {
														for (Component component : this.getComponents()) {
															if (component instanceof JComponent) {
																((JComponent)component).repaint();
															}
														}
													 };

//		gameField.addChangeListener(repaintAllGameComponents);
//		opponentPieces.addChangeListener(repaintAllGameComponents);
//		playerPieces.addChangeListener(repaintAllGameComponents);
	}
	
	private void createActionListeners(Hive hive) {
		
		/*ActionListener*/ pieceSelected = e -> {
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
															if (hive.getPlacedPieces().size() <= 0) {
																possiblePositions = new ArrayList<Placement> ();
																Piece dummyNeighbor = new QueenBee(PieceColor.WHITE);
																dummyNeighbor.resetPositionCoords(0.0, 0.0+Side.NORTH.yOffset);
																possiblePositions.add(new Placement(dummyNeighbor, Side.SOUTH));
																/* OPPURE: hive.placeFirstPiece((Piece)p);*/
															}
															else {
																possiblePositions = hive.calculatePossiblePlacements(hive.getSelectedPiece());
															}
														}
														else {
															possiblePositions = null;
														}
														hive.setPossiblePositions(possiblePositions);
														//System.out.println(((Piece)e.getSource()).toStringLong());
														//System.out.println(e.getActionCommand() + ": " + ((Piece)e.getSource()).getName() + " can move on " + possiblePositions);
														//I would trigger the possible moves showing in the view from here, if it was possible to do so without having to pass the Graphics2D object
													}
												}
											};
				 									 

		/*ActionListener*/ nothingSelected = e -> {
												if (e.getActionCommand() == "no_piece_selected") {
														hive.setSelectedPiece(null);
														hive.setPossiblePositions(null);
														//System.out.println(e.getActionCommand());
												}
										  };
			  

		/*ActionListener*/ possiblePositionSelected = e -> {
															if (e.getActionCommand() == "position_selected") {
																	Object p = e.getSource();
																	if (p instanceof Placement) {
																		if(hive.getPlacedPieces().contains(hive.getSelectedPiece())) {
																			hive.movePiece(hive.getSelectedPiece(), (Placement)p);
																		}
																		else /*if (hive.getSelectedPiece() != null)*/{////
																			hive.placeNewPiece(hive.getSelectedPiece(), (Placement)p);
																		}/* else {////
																			hive.setSelectedPiece(null);
																			hive.setPossiblePositions(null);
																			return;
																		}*/
																	}
																	hive.setSelectedPiece(null);
																	hive.setPossiblePositions(null);
																	//System.out.println(e.getActionCommand() + ": " + e.getSource());
																	fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, MOVE_MADE_EVENT, e.getWhen(), e.getModifiers()));
																}
															 };

//		opponentPieces.addActionListener(nothingSelected);
//								 
//		gameField.addActionListener(pieceSelected);
//		gameField.addActionListener(nothingSelected);
//		gameField.addActionListener(possiblePositionSelected);
//					 
//		playerPieces.addActionListener(pieceSelected);
//		playerPieces.addActionListener(nothingSelected);
//		playerPieces.addActionListener(possiblePositionSelected);
	}
	
	public void pause() {
		gameField.removeChangeListener(repaintAllGameComponents);
		opponentPieces.removeChangeListener(repaintAllGameComponents);
		playerPieces.removeChangeListener(repaintAllGameComponents);
		
		opponentPieces.removeActionListener(nothingSelected);
		
		gameField.removeActionListener(pieceSelected);
		gameField.removeActionListener(nothingSelected);
		gameField.removeActionListener(possiblePositionSelected);
					 
		playerPieces.removeActionListener(pieceSelected);
		playerPieces.removeActionListener(nothingSelected);
		playerPieces.removeActionListener(possiblePositionSelected);
		
		
		setBackground(DARK_BACKGROUND_COLOR_OFF);
		
		pnlGame.setBackground(LIGHT_BACKGROUND_COLOR_OFF);
		gameField.setBackground(LIGHT_BACKGROUND_COLOR_OFF);
		opponentPieces.setBackground(LIGHT_BACKGROUND_COLOR_OFF);
		playerPieces.setBackground(LIGHT_BACKGROUND_COLOR_OFF);
		
		pnlButtons.setBackground(DARK_BACKGROUND_COLOR_OFF);
	}
	
	public void start() {
		gameField.addChangeListener(repaintAllGameComponents);
		opponentPieces.addChangeListener(repaintAllGameComponents);
		playerPieces.addChangeListener(repaintAllGameComponents);
		
		opponentPieces.addActionListener(nothingSelected);
		 
		gameField.addActionListener(pieceSelected);
		gameField.addActionListener(nothingSelected);
		gameField.addActionListener(possiblePositionSelected);
					 
		playerPieces.addActionListener(pieceSelected);
		playerPieces.addActionListener(nothingSelected);
		playerPieces.addActionListener(possiblePositionSelected);
		
		setBackground(DARK_BACKGROUND_COLOR_ON);
		
		pnlGame.setBackground(LIGHT_BACKGROUND_COLOR_ON);
		gameField.setBackground(LIGHT_BACKGROUND_COLOR_ON);
		opponentPieces.setBackground(LIGHT_BACKGROUND_COLOR_ON);
		playerPieces.setBackground(LIGHT_BACKGROUND_COLOR_ON);
		
		pnlButtons.setBackground(DARK_BACKGROUND_COLOR_ON);
	}
	
	public void update(Hive hive) {
		this.hive = hive;
		
		playerPieces.setHive(hive);
		opponentPieces.setHive(hive);
		gameField.setHive(hive);
		
		/*this.remove(pnlGame);
		pnlGame = new JPanel();
		pnlGame.setBackground(LIGHT_BACKGROUND_COLOR_ON);
		this.add(pnlGame, BorderLayout.CENTER);
		pnlGame.setLayout(new BorderLayout(0, 0));
		
		//Game area
				
				gameField = new GameField(); //The main game GUI component
				gameField.setHive(hive); //Sets the model in the view
				gameField.setBackground(LIGHT_BACKGROUND_COLOR_ON);
				pnlGame.add(gameField, BorderLayout.CENTER);
				
				//Piece selection areas
				opponentPieces = new ToBePlacedField(); //The View
				opponentPieces.setHive(hive);
				opponentPieces.setColor(opponentColor);
				opponentPieces.setBackground(LIGHT_BACKGROUND_COLOR_ON);
				pnlGame.add(opponentPieces, BorderLayout.NORTH);
				
				playerPieces = new ToBePlacedField(); //The View
				playerPieces.setHive(hive);
				playerPieces.setColor(playerColor);
				playerPieces.setBackground(LIGHT_BACKGROUND_COLOR_ON);
				pnlGame.add(playerPieces, BorderLayout.SOUTH);
		
				repaint();*/
		//Mouse listeners
		createChangeListeners();
		
		//Action Listeners
		createActionListeners(hive);			
		
		//repaint();
	}
	
	public Hive getHive() {
		return hive;
	}

}