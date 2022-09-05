package it.unibs.pajc.nieels.hive;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Placement;
import it.unibs.pajc.nieels.hive.Piece.Side;

public class PnlOnlineGame extends EventJPanel {
	static final String ONLINE_GAME_TAG = "ONLINE_GAME";
	
	static final String BACK_BTN = "BACK";
	static final String PASS_BTN = "PASS";
	static final String SEND_BTN = "SEND";
	
	static final String PLAYER_TURN = "YOUR\nTURN";
	static final String OPPONENT_TURN = "OPPONENT'S\nTURN";
	
	static final String MOVE_MADE_EVENT = "MOVE_MADE";
	static final String VICTORY_EVENT = "VICTORY";
	static final String DEFEAT_EVENT = "DEFEAT";
	static final String DRAW_EVENT = "DRAW";
	
	static final Color LIGHT_BACKGROUND_COLOR_ON = new Color(255, 255, 200);
	static final Color LIGHT_BACKGROUND_COLOR_OFF = new Color(200, 200, 200);
	static final Color LIGHT_BACKGROUND_COLOR_VICTORY = new Color(150, 255, 150);
	static final Color LIGHT_BACKGROUND_COLOR_DEFEAT = new Color(255, 150, 150);
	static final Color LIGHT_BACKGROUND_COLOR_DRAW = new Color(150, 150, 255);
	
	static final Color DARK_BACKGROUND_COLOR_ON = new Color(255, 200, 0);
	static final Color DARK_BACKGROUND_COLOR_OFF = new Color(155, 155, 155);
	static final Color DARK_BACKGROUND_COLOR_VICTORY = new Color(100, 255, 100);
	static final Color DARK_BACKGROUND_COLOR_DEFEAT = new Color(255, 100, 100);
	static final Color DARK_BACKGROUND_COLOR_DRAW = new Color(100, 100, 255);
	
	private JLabel lblTitle;
	
	private JPanel pnlGame;
		private ToBePlacedField opponentPieces;
		private GameField gameField;
		private ToBePlacedField playerPieces;
	
	private JPanel pnlSidebar;
		private JPanel pnlPass;
			private Component verticalStrut;
			private JButton btnPass;
			private Component verticalStrut_1;
		
		private JPanel pnlChat;
			private JTextPane txtpnTurn;
			private JScrollPane scrlpnChat;
				//private JPanel pnlScrollPane;
					private JTextArea txtareaChat;
			private JPanel pnlWriteMsg;
				private JScrollPane scrlpnMsg;
					private JTextArea txtareaMsg;
				private JButton btnSend;
				private Component verticalStrut_4;
					
		private JPanel pnlBack;
			private Component verticalStrut_2;
			private JButton btnBack;
			private Component verticalStrut_3;
	
	
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
		pnlSidebar = new JPanel();
		pnlSidebar.setBackground(DARK_BACKGROUND_COLOR_ON);
		pnlSidebar.setBorder(new LineBorder(DARK_BACKGROUND_COLOR_ON, 10));
		this.add(pnlSidebar, BorderLayout.EAST);
		pnlSidebar.setLayout(new BorderLayout(0, 0));
		pnlSidebar.setPreferredSize(new Dimension(200, getHeight())); 
		
		
		pnlPass = new JPanel();
		pnlPass.setOpaque(false);
		pnlPass.setBackground(DARK_BACKGROUND_COLOR_ON);
		pnlSidebar.add(pnlPass, BorderLayout.NORTH);
		pnlPass.setLayout(new BorderLayout(0, 0));
		
		verticalStrut = Box.createVerticalStrut(15);
		pnlPass.add(verticalStrut, BorderLayout.NORTH);
		
		btnPass = new JButton(PASS_BTN);
		btnPass.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlPass.add(btnPass, BorderLayout.CENTER);
		
		verticalStrut_1 = Box.createVerticalStrut(15);
		pnlPass.add(verticalStrut_1, BorderLayout.SOUTH);
		
		pnlChat = new JPanel();
		pnlChat.setOpaque(false);
		pnlChat.setBackground(DARK_BACKGROUND_COLOR_ON);
		pnlSidebar.add(pnlChat, BorderLayout.CENTER);
		pnlChat.setLayout(new BorderLayout(0, 0));
		
		txtpnTurn = new JTextPane();
		txtpnTurn.setText(PLAYER_TURN);
		txtpnTurn.setForeground(Color.BLACK);
		txtpnTurn.setEditable(false);
		txtpnTurn.setHighlighter(null);
		txtpnTurn.setOpaque(false);
		txtpnTurn.setFont(new Font("Tahoma", Font.BOLD, 15));
		/////Needed to center text pane
		StyledDocument doc = txtpnTurn.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		/////
		txtpnTurn.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlChat.add(txtpnTurn, BorderLayout.NORTH);
		
		scrlpnChat = new JScrollPane();
		scrlpnChat.setOpaque(false);//////////
		scrlpnChat.getViewport().setOpaque(false);////////
		scrlpnChat.setBackground(DARK_BACKGROUND_COLOR_ON);
		scrlpnChat.setBorder(new MatteBorder(10, 0, 0, 0, DARK_BACKGROUND_COLOR_ON));
		scrlpnChat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pnlChat.add(scrlpnChat, BorderLayout.CENTER);
		
//		pnlScrollPane = new JPanel();
//		pnlScrollPane.setOpaque(false);////////
//		pnlScrollPane.setBackground(DARK_BACKGROUND_COLOR_ON);
//		scrlpnChat.setViewportView(pnlScrollPane);
//		pnlScrollPane.setLayout(new BoxLayout(pnlScrollPane, BoxLayout.Y_AXIS));
		
		
		txtareaChat = new JTextArea();
		txtareaChat.setEditable(false);
		//txtpnChat.setOpaque(false);///////
		txtareaChat.setBackground(LIGHT_BACKGROUND_COLOR_ON);
//		txtareaChat.setText("<br>\n<div style=\"font-family: tahoma; text-align: center; font-size: 15px\"><strong>GAME DEVELOPMENT</strong>"
//				+ "\n<br>NieelsHub (https://github.com/NieelsHub)<br>"
//				+ "\n<br><strong>GAME ICONS</strong>"
//				+ "\n<br>Bee, spider, mosquito, ladybug, soldier ant and grasshopper icons created by Freepik, rhinoceros beetle icon created by tulpahn on https://www.flaticon.com<br>"
//				+ "\n<br><strong>BACKGROUND IMAGE</strong>"
//				+ "\n<br>Designed by Freepik using images from https://www.freepik.com<br>"
//				+ "\n<br><strong>SCREEN TITLES</strong>"
//				+ "\n<br>Generated by NieelsHub using https://www.inkpx.com<br></div>");
//		
		txtareaChat.setFont(new Font("Tahoma", Font.PLAIN, 10));
		txtareaChat.setLineWrap(true);
	    txtareaChat.setWrapStyleWord(true);
		new SmartScroller(scrlpnChat);
		//pnlScrollPane.add(txtareaChat);
		scrlpnChat.setViewportView(txtareaChat);
		
		pnlWriteMsg = new JPanel();
		pnlWriteMsg.setOpaque(false);
		pnlWriteMsg.setBackground(DARK_BACKGROUND_COLOR_ON);
		pnlWriteMsg.setBorder(new MatteBorder(10, 0, 0, 0, DARK_BACKGROUND_COLOR_ON));
		pnlWriteMsg.setLayout(new BorderLayout(0, 0));
		pnlChat.add(pnlWriteMsg, BorderLayout.SOUTH);
		
		
		scrlpnMsg = new JScrollPane();
		scrlpnMsg.setOpaque(false);//////////
		scrlpnMsg.getViewport().setOpaque(false);////////
		scrlpnMsg.setBackground(DARK_BACKGROUND_COLOR_ON);
		scrlpnMsg.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrlpnMsg.setPreferredSize(new Dimension(200, 75));
		pnlWriteMsg.add(scrlpnMsg, BorderLayout.CENTER);
		
		txtareaMsg = new JTextArea();
		//txtareaMsg.setText("Write your message here...");
		txtareaMsg.setLineWrap(true);
	    txtareaMsg.setWrapStyleWord(true);
		txtareaMsg.setForeground(Color.BLACK);
		txtareaMsg.setEditable(true);
		//txtpnMsg.setHighlighter(null);
		//txtpnMsg.setOpaque(false);
		txtareaMsg.setBackground(LIGHT_BACKGROUND_COLOR_ON);
		txtareaMsg.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		txtareaMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
		//pnlWriteMsg.add(txtareaMsg, BorderLayout.CENTER);
		scrlpnMsg.setViewportView(txtareaMsg);
		
		btnSend = new JButton(SEND_BTN);
		btnSend.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlWriteMsg.add(btnSend, BorderLayout.EAST);
		
		pnlBack = new JPanel();
		pnlBack.setOpaque(false);
		pnlBack.setBackground(DARK_BACKGROUND_COLOR_ON);
		pnlSidebar.add(pnlBack, BorderLayout.SOUTH);
		pnlBack.setLayout(new BorderLayout(0, 0));
		
		verticalStrut_2 = Box.createVerticalStrut(15);
		pnlBack.add(verticalStrut_2, BorderLayout.NORTH);
		
		btnBack = new JButton(BACK_BTN);
		btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlBack.add(btnBack, BorderLayout.CENTER);
		
		verticalStrut_3 = Box.createVerticalStrut(15);
		pnlBack.add(verticalStrut_3, BorderLayout.SOUTH);
		
		//MOUSE LISTENERS
		//Change listeners
		createChangeListeners();
		
		//Action listeners
		createActionListeners(hive);
		
		start();
		
		//BUTTONS LISTENERS
		btnBack.addActionListener(e -> fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand(), e.getWhen(), e.getModifiers())));
		
		btnPass.addActionListener(e -> fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand(), e.getWhen(), e.getModifiers())));
		
		btnSend.addActionListener(e -> {
										fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand() + txtareaMsg.getText().replace("\n", "\\n"), e.getWhen(), e.getModifiers()));
										txtareaMsg.setText("");
										});
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
														if (p instanceof Piece && ((Piece)p).getColor().equals(playerColor)) {
															hive.setSelectedPiece((Piece)p);
															
															ArrayList<Placement> possiblePositions;
															
															if (hive.getPlacedPieces().contains(p)) {
																possiblePositions = hive.calculatePossibleMoves(hive.getSelectedPiece());
															}
															else if (hive.getBlacksToBePlaced().contains(p) || hive.getWhitesToBePlaced().contains(p)){
																if (hive.getPlacedPieces().size() <= 0) {
																	possiblePositions = new ArrayList<Placement> ();
																	Piece dummyNeighbor = new DummyPiece(PieceColor.WHITE);
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
														else {
															hive.setSelectedPiece(null);
															hive.setPossiblePositions(null);
														}
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
																	
																	//VICTORY CHECKS
																	boolean victory = false;
																	boolean defeat = false;
																	
																	for(Piece piece : hive.getPlacedPieces()) {
																		if(piece instanceof QueenBee && ((QueenBee)piece).isSurrounded()) {
																			if(piece.getColor().equals(opponentColor)) {
																				victory = true;
//																				fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, VICTORY_EVENT, e.getWhen(), e.getModifiers()));
																			}
																			else {
																				defeat = true;
//																				fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, DEFEAT_EVENT, e.getWhen(), e.getModifiers()));
																			}
//																			return;
																		}
																	}
																	
																	if (victory) {
																		if (defeat) {
																			fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, DRAW_EVENT, e.getWhen(), e.getModifiers()));
																		} else {
																			fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, VICTORY_EVENT, e.getWhen(), e.getModifiers()));
																		}
																		return;
																		
																	} else if (defeat) {
																		fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, DEFEAT_EVENT, e.getWhen(), e.getModifiers()));
																		return;
																	}
																	
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
		
		opponentPieces.removeActionListener(pieceSelected);
		opponentPieces.removeActionListener(nothingSelected);
		
		gameField.removeActionListener(pieceSelected);
		gameField.removeActionListener(nothingSelected);
		gameField.removeActionListener(possiblePositionSelected);
					 
		playerPieces.removeActionListener(pieceSelected);
		playerPieces.removeActionListener(nothingSelected);
		playerPieces.removeActionListener(possiblePositionSelected);
		
		txtpnTurn.setText(OPPONENT_TURN);
		btnPass.setEnabled(false);
		
		setBackground(DARK_BACKGROUND_COLOR_OFF);
		
		pnlGame.setBackground(LIGHT_BACKGROUND_COLOR_OFF);
		gameField.setBackground(LIGHT_BACKGROUND_COLOR_OFF);
		opponentPieces.setBackground(LIGHT_BACKGROUND_COLOR_OFF);
		playerPieces.setBackground(LIGHT_BACKGROUND_COLOR_OFF);
		
		//pnlSidebar.setBackground(DARK_BACKGROUND_COLOR_OFF);
	}
	
	public void start() {
		gameField.addChangeListener(repaintAllGameComponents);
		opponentPieces.addChangeListener(repaintAllGameComponents);
		playerPieces.addChangeListener(repaintAllGameComponents);
		
		opponentPieces.addActionListener(pieceSelected);
		opponentPieces.addActionListener(nothingSelected);
		 
		gameField.addActionListener(pieceSelected);
		gameField.addActionListener(nothingSelected);
		gameField.addActionListener(possiblePositionSelected);
					 
		playerPieces.addActionListener(pieceSelected);
		playerPieces.addActionListener(nothingSelected);
		playerPieces.addActionListener(possiblePositionSelected);
		
		txtpnTurn.setText(PLAYER_TURN);
		btnPass.setEnabled(true);
		
		setBackground(DARK_BACKGROUND_COLOR_ON);
		
		pnlGame.setBackground(LIGHT_BACKGROUND_COLOR_ON);
		gameField.setBackground(LIGHT_BACKGROUND_COLOR_ON);
		opponentPieces.setBackground(LIGHT_BACKGROUND_COLOR_ON);
		playerPieces.setBackground(LIGHT_BACKGROUND_COLOR_ON);
		
		//pnlSidebar.setBackground(DARK_BACKGROUND_COLOR_ON);
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
	
	public void showVictory() {
		pause();
		
		setBackground(DARK_BACKGROUND_COLOR_VICTORY);
		
		pnlGame.setBackground(LIGHT_BACKGROUND_COLOR_VICTORY);
		gameField.setBackground(LIGHT_BACKGROUND_COLOR_VICTORY);
		opponentPieces.setBackground(LIGHT_BACKGROUND_COLOR_VICTORY);
		playerPieces.setBackground(LIGHT_BACKGROUND_COLOR_VICTORY);
		
		pnlSidebar.setBackground(DARK_BACKGROUND_COLOR_VICTORY);

		txtpnTurn.setText("YOU\nWIN!");
	}
	
	public void showDefeat() {
		pause();
		
		setBackground(DARK_BACKGROUND_COLOR_DEFEAT);
		
		pnlGame.setBackground(LIGHT_BACKGROUND_COLOR_DEFEAT);
		gameField.setBackground(LIGHT_BACKGROUND_COLOR_DEFEAT);
		opponentPieces.setBackground(LIGHT_BACKGROUND_COLOR_DEFEAT);
		playerPieces.setBackground(LIGHT_BACKGROUND_COLOR_DEFEAT);
		
		pnlSidebar.setBackground(DARK_BACKGROUND_COLOR_DEFEAT);
		
		txtpnTurn.setText("YOU\nLOSE!");
	}
	
	public void showDraw() {
		pause();
		
		setBackground(DARK_BACKGROUND_COLOR_DRAW);
		
		pnlGame.setBackground(LIGHT_BACKGROUND_COLOR_DRAW);
		gameField.setBackground(LIGHT_BACKGROUND_COLOR_DRAW);
		opponentPieces.setBackground(LIGHT_BACKGROUND_COLOR_DRAW);
		playerPieces.setBackground(LIGHT_BACKGROUND_COLOR_DRAW);
		
		pnlSidebar.setBackground(DARK_BACKGROUND_COLOR_DRAW);
		
		txtpnTurn.setText("IT'S A\nDRAW!");
	}
	
	public Hive getHive() {
		return hive;
	}
	
	public void displayChatMessage(String msg) {
		txtareaChat.append(msg.replace("\\n", "\n") + "\n\n");
	}

}
