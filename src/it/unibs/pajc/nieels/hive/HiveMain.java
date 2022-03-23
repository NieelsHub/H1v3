package it.unibs.pajc.nieels.hive;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Side;

//CONTROLLER
public class HiveMain {
	//VIEW
	private JFrame frame; //The application window, it has a content pane property, which is the area where the graphic components are put
	private JPanel contentPane;///////
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
					HiveMain window = new HiveMain();
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
	public HiveMain() {
		initialize(); //As a covention, the graphic components are created and put in the frame's content pane through the initialize method
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		///
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		frame.setContentPane(contentPane);
		/////
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
		
		frame.getContentPane().add(gameField, BorderLayout.CENTER); //Every component must be in the frames'content pane to be visualized.
		//Poi aggiungerai al content pane due aree, una sopra e una sotto, con i whites e i blacks to be placed.
	}
	
}
