package it.unibs.pajc.nieels.hive;

import java.awt.EventQueue;
import java.util.LinkedHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Side;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
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
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(0), hive.getPlacedPieces().get(0), Side.SOUTH);
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(0), hive.getPlacedPieces().get(1), Side.SOUTH);
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(0), hive.getPlacedPieces().get(2), Side.NORTHEAST);
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(5), hive.getPlacedPieces().get(3), Side.NORTHEAST);
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(4), hive.getPlacedPieces().get(2), Side.NORTHWEST);
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(4), hive.getPlacedPieces().get(2), Side.SOUTH);
		//////////
		
		gameField.setHive(hive); //Sets the model in the view
		
		pnlGame.add(gameField, BorderLayout.CENTER);
		/*******
		ToBePlacedField blacks = new ToBePlacedField(); //The View
		blacks.setPieces(hive.getBlacksToBePlaced());
		pnlGame.add(blacks, BorderLayout.NORTH);
		
		ToBePlacedField whites = new ToBePlacedField(); //The View
		whites.setPieces(hive.getWhitesToBePlaced());
		pnlGame.add(whites, BorderLayout.SOUTH);
		***********/
		ToBePlacedField blacks = new ToBePlacedField(); //The View
		blacks.setHive(hive);
		blacks.setColor(PieceColor.BLACK);
		pnlGame.add(blacks, BorderLayout.NORTH);
		
		ToBePlacedField whites = new ToBePlacedField(); //The View
		whites.setHive(hive);
		whites.setColor(PieceColor.WHITE);
		pnlGame.add(whites, BorderLayout.SOUTH);
		
		
	}
	
}
