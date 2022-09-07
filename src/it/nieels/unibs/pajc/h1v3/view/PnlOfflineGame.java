package it.nieels.unibs.pajc.h1v3.view;

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
import javax.swing.JTextPane;
import javax.swing.event.ChangeListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import it.nieels.unibs.pajc.h1v3.model.DummyPiece;
import it.nieels.unibs.pajc.h1v3.model.Hive;
import it.nieels.unibs.pajc.h1v3.model.Piece;
import it.nieels.unibs.pajc.h1v3.model.QueenBee;
import it.nieels.unibs.pajc.h1v3.model.Piece.PieceColor;
import it.nieels.unibs.pajc.h1v3.model.Piece.Placement;
import it.nieels.unibs.pajc.h1v3.model.Piece.Side;

/**
 * This component provides a offline game UI.
 * @author Nicol Stocchetti
 *
 */
public class PnlOfflineGame extends EventJPanel {
	public static final String OFFLINE_GAME_TAG = "OFFLINE_GAME";
	
	public static final String BACK_BTN = "BACK";
	public static final String PASS_BTN = "PASS";
	
	static final String WHITE_TURN = "WHITE'S\nTURN";
	static final String BLACK_TURN = "BLACK'S\nTURN";
	
	static final Color LIGHT_BACKGROUND_COLOR = new Color(255, 255, 200);
	static final Color LIGHT_BACKGROUND_COLOR_VICTORY = new Color(255, 150, 150);
	static final Color LIGHT_BACKGROUND_COLOR_ACTIVE = new Color(255, 225, 135);
	
	static final Color DARK_BACKGROUND_COLOR = new Color(255, 200, 0);
	static final Color DARK_BACKGROUND_COLOR_VICTORY = new Color(255, 100, 100);
	
	//private JLabel lblTitle;
	
	private JPanel pnlGame;
		private ToBePlacedField whites;
		private GameField gameField;
		private ToBePlacedField blacks;
	
	private JPanel pnlButtons;
		private JPanel pnlPass;
			private Component verticalStrut;
			private JButton btnPass;
			private Component verticalStrut_1;
		private JTextPane txtpnTurn;
		private JPanel pnlBack;
			private Component verticalStrut_2;
			private JButton btnBack;
			private Component verticalStrut_3;
	
	private PieceColor activeColor;
	
	ActionListener pieceSelected;
	ActionListener nothingSelected;
	ActionListener possiblePositionSelected;
	
	ChangeListener repaintAllGameComponents;
		
	/**
	 * The constructor, it receives a Hive to show.
	 * @param hive the hive to be shown in the game, Hive.
	 */
	public PnlOfflineGame(Hive hive) {
		
		activeColor = PieceColor.BLACK;
		
		setBackground(DARK_BACKGROUND_COLOR);
		setLayout(new BorderLayout(0, 0));
		
		/*
		lblTitle = new JLabel(ONLINE_GAME_TAG);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Papyrus", Font.BOLD, 30));
		lblTitle.setForeground(Color.RED);
		this.add(lblTitle, BorderLayout.NORTH);
		*/
		
		pnlGame = new JPanel();
		pnlGame.setBackground(LIGHT_BACKGROUND_COLOR);
		this.add(pnlGame, BorderLayout.CENTER);
		pnlGame.setLayout(new BorderLayout(0, 0));
		
		//Game area
		gameField = new GameField(); //The main game GUI component
		gameField.setHive(hive); //Sets the model in the view
		gameField.setBackground(LIGHT_BACKGROUND_COLOR);
		pnlGame.add(gameField, BorderLayout.CENTER);
		
		//Piece selection areas
		whites = new ToBePlacedField(); //The View
		whites.setHive(hive);
		whites.setColor(PieceColor.WHITE);
		whites.setBackground(LIGHT_BACKGROUND_COLOR);
		pnlGame.add(whites, BorderLayout.NORTH);
		
		blacks = new ToBePlacedField(); //The View
		blacks.setHive(hive);
		blacks.setColor(PieceColor.BLACK);
		blacks.setBackground(LIGHT_BACKGROUND_COLOR_ACTIVE);
		pnlGame.add(blacks, BorderLayout.SOUTH);
		
		
		//Button area
		pnlButtons = new JPanel();
		pnlButtons.setBackground(DARK_BACKGROUND_COLOR);
		this.add(pnlButtons, BorderLayout.EAST);
		pnlButtons.setLayout(new BorderLayout(0, 0));
		pnlButtons.setPreferredSize(new Dimension(75, getHeight())); 
		
		
		pnlPass = new JPanel();
		pnlPass.setOpaque(false);
		pnlPass.setBackground(DARK_BACKGROUND_COLOR);
		pnlButtons.add(pnlPass, BorderLayout.NORTH);
		pnlPass.setLayout(new BorderLayout(0, 0));
		
		verticalStrut = Box.createVerticalStrut(15);
		pnlPass.add(verticalStrut, BorderLayout.NORTH);
		
		btnPass = new JButton(PASS_BTN);
		btnPass.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlPass.add(btnPass, BorderLayout.CENTER);
		
		verticalStrut_1 = Box.createVerticalStrut(15);
		pnlPass.add(verticalStrut_1, BorderLayout.SOUTH);
		
		
		txtpnTurn = new JTextPane();
		txtpnTurn.setText(BLACK_TURN);
		txtpnTurn.setForeground(Color.BLACK);
		txtpnTurn.setEditable(false);
		txtpnTurn.setHighlighter(null);
		txtpnTurn.setOpaque(false);
		txtpnTurn.setFont(new Font("Tahoma", Font.BOLD, 15));
		txtpnTurn.setPreferredSize(new Dimension (200, 50));
		/////Needed to center text pane
		StyledDocument doc = txtpnTurn.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		/////
		txtpnTurn.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlButtons.add(txtpnTurn, BorderLayout.CENTER);
		
		
		pnlBack = new JPanel();
		pnlBack.setOpaque(false);
		pnlBack.setBackground(DARK_BACKGROUND_COLOR);
		pnlButtons.add(pnlBack, BorderLayout.SOUTH);
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
		repaintAllGameComponents = e -> {
											for (Component component : this.getComponents()) {
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
		
		//Action listeners
		pieceSelected = e -> {
								//make model calculate possible moves
								if (e.getSource() != null) {
									Object p = e.getSource();
									if (e.getActionCommand() == "show_possible_positions") {
										if (p instanceof Piece && ((Piece)p).getColor().equals(activeColor)) {
											hive.setSelectedPiece((Piece)p);
											
											ArrayList<Placement> possiblePositions;
											
											if (hive.getPlacedPieces().contains(p)) {
												possiblePositions = hive.calculatePossibleMoves(hive.getSelectedPiece());
											}
											else if (hive.getBlacksToBePlaced().contains(p) || hive.getWhitesToBePlaced().contains(p)){
												if (hive.getPlacedPieces().size() <= 0) {
													possiblePositions = new ArrayList<Placement> ();
													Piece dummyNeighbor = new DummyPiece(PieceColor.WHITE);
													dummyNeighbor.resetPositionCoords(0.0, 0.0+Side.NORTH.getYOffset());
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
					 

		nothingSelected = e -> {
									if (e.getActionCommand() == "no_piece_selected") {
											hive.setSelectedPiece(null);
											hive.setPossiblePositions(null);
											//System.out.println(e.getActionCommand());
									}
							  };


		possiblePositionSelected = e -> {
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
													changeActiveColor();
													//System.out.println(e.getActionCommand() + ": " + e.getSource());
													
													//VICTORY CHECK
													for(Piece piece : hive.getPlacedPieces()) {
														if(piece instanceof QueenBee && ((QueenBee)piece).isSurrounded()) {
															victory();
														}
													}
												}
											 };
		
		for (Component component : pnlGame.getComponents()) {
			if (component instanceof HexField) {
				((HexField)component).addActionListener(pieceSelected);
				((HexField)component).addActionListener(nothingSelected);
				((HexField)component).addActionListener(possiblePositionSelected);
			}
		}
		
		//BUTTONS LISTENERS
		btnBack.addActionListener(e -> fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand(), e.getWhen(), e.getModifiers())));
		
		btnPass.addActionListener(e -> {
											hive.setSelectedPiece(null);
											hive.setPossiblePositions(null);
											for (Component component : pnlGame.getComponents()) {
												if (component instanceof HexField) {
														component.repaint();;
												}
											}
											changeActiveColor();
			
										});
		
	}
	
	/**
	 * Changes the active color in the game, the one that is playing the current turn.
	 */
	private void changeActiveColor() {
		activeColor = activeColor == PieceColor.BLACK ? PieceColor.WHITE : PieceColor.BLACK;
		
		if (activeColor == PieceColor.BLACK) {
			txtpnTurn.setText(BLACK_TURN);
			txtpnTurn.setForeground(Color.BLACK);
			whites.setBackground(LIGHT_BACKGROUND_COLOR);
			blacks.setBackground(LIGHT_BACKGROUND_COLOR_ACTIVE);
		}
		else {
			txtpnTurn.setText(WHITE_TURN);
			txtpnTurn.setForeground(Color.GRAY);
			blacks.setBackground(LIGHT_BACKGROUND_COLOR);
			whites.setBackground(LIGHT_BACKGROUND_COLOR_ACTIVE);
		}
	}
	
	/**
	 * Shows the end of the game.
	 */
	public void victory() {
		gameField.removeChangeListener(repaintAllGameComponents);
		whites.removeChangeListener(repaintAllGameComponents);
		blacks.removeChangeListener(repaintAllGameComponents);
		
		whites.removeActionListener(possiblePositionSelected);
		whites.removeActionListener(pieceSelected);
		whites.removeActionListener(nothingSelected);
		
		gameField.removeActionListener(pieceSelected);
		gameField.removeActionListener(nothingSelected);
		gameField.removeActionListener(possiblePositionSelected);
					 
		blacks.removeActionListener(pieceSelected);
		blacks.removeActionListener(nothingSelected);
		blacks.removeActionListener(possiblePositionSelected);
		
		txtpnTurn.setText("GAME\nOVER");
		txtpnTurn.setForeground(Color.BLACK);
		btnPass.setEnabled(false);
		
		setBackground(DARK_BACKGROUND_COLOR_VICTORY);
		
		pnlGame.setBackground(LIGHT_BACKGROUND_COLOR_VICTORY);
		gameField.setBackground(LIGHT_BACKGROUND_COLOR_VICTORY);
		whites.setBackground(LIGHT_BACKGROUND_COLOR_VICTORY);
		blacks.setBackground(LIGHT_BACKGROUND_COLOR_VICTORY);
		
		pnlButtons.setBackground(DARK_BACKGROUND_COLOR_VICTORY);
		//YOU WON!
	}
	

}
