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

public class HiveMain {

	private JFrame frame; //The application window, it has a content pane property, which is the area where the graphic components are put
	private JPanel contentPane;///////

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
		/*
		////////////////TEST (will then be on server main)
		
		LinkedHashMap <Class<?>, Integer> piecesSet = new LinkedHashMap();
		
		piecesSet.put(QueenBee.class, 1);
		piecesSet.put(Spider.class, 2);
		piecesSet.put(Beetle.class, 2);
		piecesSet.put(SoldierAnt.class, 3);
		piecesSet.put(Grasshopper.class, 3);
		
		Hive hive = new Hive(piecesSet);
		/*
		for(Piece piece : hive.blacksToBePlaced) {
			System.out.println(piece);
		}
		
		System.out.println();
		
		for(Piece piece : hive.whitesToBePlaced) {
			System.out.println(piece);
		}
		
		System.out.println();
		
		hive.placeFirstPiece(hive.blacksToBePlaced.get(0));
		hive.placeNewPiece(hive.whitesToBePlaced.get(0), hive.hive.get(0), Side.SOUTH);
		hive.placeNewPiece(hive.whitesToBePlaced.get(0), hive.hive.get(1), Side.NORTHEAST);
		hive.placeNewPiece(hive.whitesToBePlaced.get(0), hive.hive.get(2), Side.NORTH);
		hive.placeNewPiece(hive.whitesToBePlaced.get(5), hive.hive.get(3), Side.NORTHWEST);
		hive.placeNewPiece(hive.whitesToBePlaced.get(4), hive.hive.get(0), Side.NORTHWEST);
		//hive.placeNewPiece(hive.whitesToBePlaced.get(4), hive.hive.get(1), Side.SOUTH);
		
		for(Piece piece : hive.hive) {
			System.out.println(piece + "\n");
		}
		
		Piece pc = hive.hive.get(4);
		System.out.print(pc.getName() + " - ");
		System.out.println(pc.getPossibleDirections());
		System.out.println();
		
		hive.movePiece(pc, Side.SOUTH);
		
		for(Piece piece : hive.hive) {
			System.out.println(piece + "\n");
		}
		
		/*Breaks the hive in two pieces
		Piece pc2 = hive.hive.get(6);
		System.out.print(pc2.getName() + " - ");
		System.out.println(pc2.getPossibleDirections());
		System.out.println();
		
		hive.movePiece(pc2, Side.SOUTH);
		
		for(Piece piece : hive.hive) {
			System.out.println(piece + "\n");
		}*/
		
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
		frame.getContentPane().add(gameField, BorderLayout.CENTER); //Every component must be in the frames'content pane to be visualized.
	}


}
