package it.unibs.pajc.nieels.hive;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JComboBox;
import java.awt.Rectangle;

//CONTROLLER
public class HiveMain {
	public static final String SETTINGS_PATH = "./xml/Settings.xml";
	
	//MODEL
	private Hive hive;
	XMLObject settingsXML;
	
	NetworkClient client;
	Thread serverThread = new Thread();
	Thread clientThread = new Thread();
	
	//VIEW
	private JFrame frame; //The application window, it has a content pane property, which is the area where the graphic components are put
	private JPanel contentPane;
	private JPanel cards; //a panel that uses CardLayout to show different game screens
	
	private PnlMainMenu pnlMainMenu; //Each of these panels is a different game screen
	
	private PnlSettings pnlSettings;
	private ArrayList<JPanel> pnlSettingsToRemove = new ArrayList<JPanel>();
	
	private PnlHostGame pnlHostGame;
	
	private PnlJoinGame pnlJoinGame;
	
	private PnlCredits pnlCredits;
	
	
	
	
	
	private PnlOnlineGame pnlOnlineGame;
	//private ArrayList<JPanel> pnlOfflineGameToRemove = new ArrayList<JPanel>();
	final static String ONLINE_GAME = "ONLINE_GAME";
		
	
	
	
	
	
	private JPanel pnlOfflineGame;
	//private ArrayList<JPanel> pnlOfflineGameToRemove = new ArrayList<JPanel>();
	final static String OFFLINE_GAME = "OFFLINE_GAME";
		
	///////////////////
	
	
	
	
	
	

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
		frame.setIconImage(new ImageIcon(HexField.VISUAL_RESOURCES_DIRECTORY + "/" + QueenBee.PIECE_NAME + ".png").getImage());
		frame.setTitle("H1v3 - By NieelsHub");
		frame.setBounds(100, 100, 1000, 700);
		frame.setMinimumSize(new Dimension(640, 480));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		cards = new JPanel(new CardLayout());
		frame.getContentPane().add(cards);
		
		createMainMenu();
		cards.add(pnlMainMenu, PnlMainMenu.MAIN_MENU_TAG);
		
		//createOfflineGame();
		//cards.add(pnlOfflineGame, OFFLINE_GAME);
		
		createSettings();
		cards.add(pnlSettings, PnlSettings.SETTINGS_TAG);
		pnlSettingsToRemove.add(pnlSettings);
	}
	
	private void createMainMenu(){
		pnlMainMenu = new PnlMainMenu();
		
		//Listeners
		pnlMainMenu.addActionListener(e -> {
			if(!e.getActionCommand().equals(PnlMainMenu.LOCAL_GAME_BTN)) {
				return;
			}
			if (pnlOfflineGame == null) {
				createOfflineGame();
				cards.add(pnlOfflineGame, OFFLINE_GAME);
			}
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, OFFLINE_GAME);
	        //System.out.println(e.getSource());
		});
		
		pnlMainMenu.addActionListener(e -> {
			if(!e.getActionCommand().equals(PnlMainMenu.SETTINGS_BTN)) {
				return;
			}
			//Upon entering the settings check that the Settings file exists, if not generate a default one.
			createSettings();
			cards.add(pnlSettings, PnlSettings.SETTINGS_TAG);
			pnlSettingsToRemove.add(pnlSettings);
			System.out.println(cards.getComponents().length);
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, PnlSettings.SETTINGS_TAG);
	        //System.out.println(e.getSource());
		});
		
		pnlMainMenu.addActionListener(e -> {
			if(!e.getActionCommand().equals(PnlMainMenu.HOST_GAME_BTN)) {
				return;
			}
			//Upon entering the game hosting...
			if (pnlHostGame == null) {
				createHostGame();
				cards.add(pnlHostGame, PnlHostGame.HOST_GAME_TAG);
			}
			System.out.println(cards.getComponents().length);
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, PnlHostGame.HOST_GAME_TAG);
	        //System.out.println(e.getSource());
		});
		
		pnlMainMenu.addActionListener(e -> {
			if(!e.getActionCommand().equals(PnlMainMenu.JOIN_GAME_BTN)) {
				return;
			}
			//Upon entering the game joining...
			if (pnlJoinGame == null) {
				createJoinGame();
				cards.add(pnlJoinGame, PnlJoinGame.JOIN_GAME_TAG);
			}
			System.out.println(cards.getComponents().length);
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, PnlJoinGame.JOIN_GAME_TAG);
	        //System.out.println(e.getSource());
		});
		
		pnlMainMenu.addActionListener(e -> {
			if(!e.getActionCommand().equals(PnlMainMenu.CREDITS_BTN)) {
				return;
			}
			//Upon entering the game joining...
			if (pnlCredits == null) {
				createCredits();
				cards.add(pnlCredits, PnlCredits.CREDITS_TAG);
			}
			System.out.println(cards.getComponents().length);
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, PnlCredits.CREDITS_TAG);
	        //System.out.println(e.getSource());
		});
		
		pnlMainMenu.addActionListener(e -> {
			if(!e.getActionCommand().equals(PnlMainMenu.EXIT_BTN)) {
				return;
			}
			
			/*
			 * System.exit(int i) is to be used, but I would include it inside a more generic shutdown() method, where you would
			 * include "cleanup" steps as well, closing socket connections, file descriptors, then exiting with System.exit(x).
			 */
			System.out.println("Exiting...");
			System.exit(0);
		});
	}
	
	private void createSettings(){
		
		loadSettings();
		
		pnlSettings = new PnlSettings(settingsXML);
		
		//Listeners
		pnlSettings.addActionListener(e -> {
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, PnlMainMenu.MAIN_MENU_TAG);
	        removeSettingsPanelsFromCards();
	        //System.out.println(e.getSource());
		});
	}
	
	void loadSettings() {
		File settingsFile = new File(SETTINGS_PATH);
		if (!settingsFile.isFile()) {
			try {
				settingsFile.getParentFile().mkdirs(); 
				settingsFile.createNewFile();
				
				settingsXML = generateDefaultSettings();
				XMLParser.writeDocument(settingsXML, SETTINGS_PATH);
				System.out.println("NEW DEFAULT SETTINGS GENERATED AND LOADED");
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
		} else {
			settingsXML = XMLParser.extractXMLObject(SETTINGS_PATH);
			System.out.println("SETTINGS LOADED");
		}
	}
	
	XMLObject generateDefaultSettings() {
		XMLObject defaultSettings;
		XMLElement rootElement = new XMLElement("SETTINGS");
		XMLElement pieces = new XMLElement("PIECES");
		
		rootElement.addText("\n\t");
		rootElement.addChildElement(pieces);
		pieces.addAttribute(QueenBee.PIECE_NAME, "1");
		pieces.addAttribute(Spider.PIECE_NAME, "2");
		pieces.addAttribute(Beetle.PIECE_NAME, "2");
		pieces.addAttribute(SoldierAnt.PIECE_NAME, "3");
		pieces.addAttribute(Grasshopper.PIECE_NAME, "3");
		rootElement.addText("\n");
		
		ArrayList<String> newLine = new ArrayList<String>();
		newLine.add("\n");
		
		defaultSettings = new XMLObject(null, null, newLine, rootElement, null);
		return defaultSettings;
	}
	
	public void removeSettingsPanelsFromCards() {
		for (JPanel pnl : pnlSettingsToRemove) {
			cards.remove(pnl);
		}
	}
	
	private void createHostGame(){
		loadSettings();
		
		pnlHostGame = new PnlHostGame();
		
		//Listeners
		pnlHostGame.addActionListener(e -> {
			
			if(!e.getActionCommand().equals(PnlHostGame.CANCEL_BTN)) {
				return;
			}
			
			if (!serverThread.isInterrupted()) {
				serverThread.interrupt();
			}
			
			if (!clientThread.isInterrupted()) {
				clientThread.interrupt();
			}

			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, PnlMainMenu.MAIN_MENU_TAG);
	        cards.remove(pnlHostGame);
	        pnlHostGame = null;
	        System.out.println(e.getSource());
		});
		
		pnlHostGame.addActionListener(e -> {
			
			if(!e.getActionCommand().contains(PnlHostGame.SET_PORT_BTN)) {
				return;
			}
			
			String port = e.getActionCommand().substring(PnlHostGame.SET_PORT_BTN.length());
			//System.out.println(port);
			loadSettings();
			hive = new Hive(extractPiecesSet());
			Runnable startServer = () -> new NetworkServer(Integer.parseInt(port)).start(hive);
			serverThread = new Thread(startServer);
			serverThread.start();
			
			/*Runnable startClient = () -> {
											try {
												client = new NetworkClient(NetworkClient.DEFAULT_SERVER, Integer.parseInt(port));
												client.startPlayer();
											} catch(ConnectException ex) {
												pnlJoinGame.connectionRefused();
											}
			};*/
			Runnable startClient = () -> {
				try {
					//Preparations
					client = new NetworkClient(NetworkClient.DEFAULT_SERVER, Integer.parseInt(port));
					
					client.addActionListener(f -> {
													if(!f.getActionCommand().contains(NetworkServer.START_GAME)) {
														return;
													}
													if (pnlOnlineGame == null) {
														createOnlineGame(PieceColor.WHITE);
														cards.add(pnlOnlineGame, PnlOnlineGame.ONLINE_GAME_TAG);
													}
													pnlOnlineGame.pause();
													System.out.println(cards.getComponents().length);
													CardLayout cl = (CardLayout)(cards.getLayout());
											        cl.show(cards, PnlOnlineGame.ONLINE_GAME_TAG);
					});
			        
			        client.addActionListener(f -> {
			        								if(!f.getActionCommand().contains(NetworkServer.HIVE_UPDATE)) {
			        									return;
			        								}
			        								
			        								hive = (Hive)Base64SerializationUtility.deserializeObjectFromString(f.getActionCommand().substring(NetworkServer.HIVE_UPDATE.length()));
			        								System.out.println(System.currentTimeMillis());
			        								if (pnlOnlineGame != null) {
			        									pnlOnlineGame.update(hive);
			        									/*cards.remove(pnlOnlineGame);
			        									createOnlineGame(PieceColor.WHITE);
														cards.add(pnlOnlineGame, PnlOnlineGame.ONLINE_GAME_TAG);
														CardLayout cl = (CardLayout)(cards.getLayout());
												        cl.show(cards, PnlOnlineGame.ONLINE_GAME_TAG);*/
			        								}
			        								System.out.println("\nNEW HIVE UPDATED:\n" + hive);
												});
			        
			        client.addActionListener(f -> {
						if(!f.getActionCommand().contains(NetworkServer.ASK_FOR_MOVE)) {
							return;
						}
						pnlOnlineGame.repaint();
						pnlOnlineGame.start();
						System.out.println("READY TO MAKE MOVE...");
					});
			        
					//Actual continuous alive thread process
					client.startPlayer();
					
				} catch(ConnectException ex) {
					pnlJoinGame.connectionRefused();
				}
			};
			
			clientThread = new Thread(startClient);
			clientThread.start();
			
	        System.out.println(e.getSource());
		});
	
	}
	
	private void createJoinGame(){
		
		pnlJoinGame = new PnlJoinGame();
		
		//Listeners
		pnlJoinGame.addActionListener(e -> {
			
			if(!e.getActionCommand().equals(PnlJoinGame.CANCEL_BTN)) {
				return;
			}
			
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, PnlMainMenu.MAIN_MENU_TAG);
	        cards.remove(pnlJoinGame);
	        pnlJoinGame = null;
	        System.out.println(e.getSource());
		});
		
		pnlJoinGame.addActionListener(e -> {
			
			if(!e.getActionCommand().contains(PnlJoinGame.JOIN_AS_PLAYER_BTN)) {
				return;
			}
			
			String serverIPAndPort = e.getActionCommand().substring(PnlJoinGame.JOIN_AS_PLAYER_BTN.length());
			String serverIP = serverIPAndPort.split("@")[0];
			String port = serverIPAndPort.split("@")[1];
			System.out.println(e.getActionCommand());
			System.out.println(serverIP);
			System.out.println(port);
			
			/****OLD***
			Runnable startClient = () -> {
											try {
												client = new NetworkClient(serverIP, Integer.parseInt(port));
												client.startPlayerChat();
											} catch(ConnectException ex) {
												pnlJoinGame.connectionRefused();
											}
			};
				
			clientThread = new Thread(startClient);
			clientThread.start();
			
			if (pnlOnlineGame == null) {
				createOnlineGame();
				cards.add(pnlOnlineGame, PnlOnlineGame.ONLINE_GAME_TAG);
			}
			System.out.println(cards.getComponents().length);
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, PnlOnlineGame.ONLINE_GAME_TAG);
			*********/
			
			Runnable startClient = () -> {
				try {
					//Preparations
					client = new NetworkClient(serverIP, Integer.parseInt(port));
					
					client.addActionListener(f -> {
													if(!f.getActionCommand().contains(NetworkServer.START_GAME)) {
														return;
													}
													if (pnlOnlineGame == null) {
														createOnlineGame(PieceColor.BLACK);
														cards.add(pnlOnlineGame, PnlOnlineGame.ONLINE_GAME_TAG);
													}
													System.out.println(cards.getComponents().length);
													CardLayout cl = (CardLayout)(cards.getLayout());
											        cl.show(cards, PnlOnlineGame.ONLINE_GAME_TAG);
												});
			        
			        client.addActionListener(f -> {
			        								if(!f.getActionCommand().contains(NetworkServer.HIVE_UPDATE)) {
			        									return;
			        								}
			        								
			        								hive = (Hive)Base64SerializationUtility.deserializeObjectFromString(f.getActionCommand().substring(NetworkServer.HIVE_UPDATE.length()));
			        								if (pnlOnlineGame != null) {
			        									pnlOnlineGame.update(hive);
			        									/*cards.remove(pnlOnlineGame);
			        									createOnlineGame(PieceColor.BLACK);
														cards.add(pnlOnlineGame, PnlOnlineGame.ONLINE_GAME_TAG);
														CardLayout cl = (CardLayout)(cards.getLayout());
												        cl.show(cards, PnlOnlineGame.ONLINE_GAME_TAG);*/
			        								}
			        								System.out.println("\nNEW HIVE UPDATED:\n" + hive);
												});
			        
			        client.addActionListener(f -> {
						if(!f.getActionCommand().contains(NetworkServer.ASK_FOR_MOVE)) {
							return;
						}
						pnlOnlineGame.repaint();
						pnlOnlineGame.start();
						System.out.println("READY TO MAKE MOVE...");
					});
			        
					//Actual continuous alive thread process
					client.startPlayer();
					
				} catch(ConnectException ex) {
					pnlJoinGame.connectionRefused();
				}
			};
			
			clientThread = new Thread(startClient);
			clientThread.start();

	        System.out.println(e.getSource());
		});
	
		//JoinAsSpectator copiare uguale da AsPlayer
	
	}
	
	private void createOnlineGame(PieceColor playerColor){
		
		pnlOnlineGame = new PnlOnlineGame(hive, playerColor);
		
		//Listeners
		
		pnlOnlineGame.addActionListener(e -> {
			
			if(!e.getActionCommand().equals(PnlOnlineGame.BACK_BTN)) {
				return;
			}
			
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, PnlMainMenu.MAIN_MENU_TAG);
	        cards.remove(pnlOnlineGame);
	        pnlOnlineGame = null;
	        System.out.println(e.getSource());
		});
		
		/*
		ChangeListener repaintAllGameComponents = e -> {
										for (Component component : pnlOnlineGame.getComponents()) {
											if (component instanceof JComponent) {
												((JComponent)component).repaint();
											}
										}
									 };
		
	 
		for (Component component : pnlOnlineGame.getComponents()) {
			if (component instanceof HexField) {
				((HexField)component).addChangeListener(repaintAllGameComponents);
			}
		}
		*/
		/*
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
														if (hive.getPlacedPieces().size() <= 0) {
															possiblePositions = new ArrayList<Placement> ();
															Piece dummyNeighbor = new QueenBee(PieceColor.WHITE);
															dummyNeighbor.resetPositionCoords(0.0, 0.0+Side.NORTH.yOffset);
															possiblePositions.add(new Placement(dummyNeighbor, Side.SOUTH));
															/* OPPURE: hive.placeFirstPiece((Piece)p);*/
														/*}
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
										};*/
												 									 
		/* 
		ActionListener nothingSelected = e -> {
												if (e.getActionCommand() == "no_piece_selected") {
														hive.setSelectedPiece(null);
														hive.setPossiblePositions(null);
														//System.out.println(e.getActionCommand());
												}
										  };*/
										  
	/***********************************	QUI FARO' MANDARE IL NUOVO HIVE SERIALIZZATO DA QUESTO CLIENT AL SERVER E DAL SERVER A TUTTI I CLIENT
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
																	//System.out.println(e.getActionCommand() + ": " + e.getSource());
																}
														 };**********************/
		pnlOnlineGame.addActionListener(e -> {
												if(!e.getActionCommand().equals(PnlOnlineGame.MOVE_MADE_EVENT)) {
													return;
												}
												
												pnlOnlineGame.pause();
												this.hive = pnlOnlineGame.getHive();
												client.sendMsgToServer(NetworkServer.HIVE_UPDATE + Base64SerializationUtility.serializeObjectToString(hive));
											});
											 
		/*									 
		 for (Component component : pnlOnlineGame.getComponents()) {
				if (component instanceof HexField) {
					((HexField)component).addActionListener(pieceSelected);
					((HexField)component).addActionListener(nothingSelected);
					((HexField)component).addActionListener(possiblePositionSelected);
				}
			}*/
	}
	
	private void createOfflineGame(){
		
		loadSettings();
		
		pnlOfflineGame = new JPanel();
		pnlOfflineGame.setLayout(new BorderLayout(0, 0));
		
		GameField gameField = new GameField(); //The main game GUI component
		
		/*
		piecesSet.put(QueenBee.class, 1);
		piecesSet.put(Spider.class, 2);
		piecesSet.put(Beetle.class, 2);
		piecesSet.put(SoldierAnt.class, 3);
		piecesSet.put(Grasshopper.class, 3);
		*/
		
		hive = new Hive(extractPiecesSet());
		/*
		hive.placeFirstPiece(hive.getBlacksToBePlaced().get(0));
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(0), new Placement(hive.getPlacedPieces().get(0), Side.SOUTH));
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(0), new Placement(hive.getPlacedPieces().get(1), Side.SOUTH));
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(0), new Placement(hive.getPlacedPieces().get(2), Side.NORTHEAST));
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(5), new Placement(hive.getPlacedPieces().get(3), Side.NORTHEAST));
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(4), new Placement(hive.getPlacedPieces().get(2), Side.NORTHWEST));
		hive.placeNewPiece(hive.getWhitesToBePlaced().get(4), new Placement(hive.getPlacedPieces().get(2), Side.SOUTH));
		*/
		//////////
		
		gameField.setHive(hive); //Sets the model in the view
		
		pnlOfflineGame.add(gameField, BorderLayout.CENTER);
		
		//Piece selection areas
		ToBePlacedField blacks = new ToBePlacedField(); //The View
		blacks.setHive(hive);
		blacks.setColor(PieceColor.BLACK);
		pnlOfflineGame.add(blacks, BorderLayout.NORTH);
		
		ToBePlacedField whites = new ToBePlacedField(); //The View
		whites.setHive(hive);
		whites.setColor(PieceColor.WHITE);
		pnlOfflineGame.add(whites, BorderLayout.SOUTH);
		
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
		ChangeListener repaintAllGameComponents = e -> {
										for (Component component : pnlOfflineGame.getComponents()) {
											if (component instanceof JComponent) {
												((JComponent)component).repaint();
											}
										}
									 };
		
	 
		for (Component component : pnlOfflineGame.getComponents()) {
			if (component instanceof HexField) {
				((HexField)component).addChangeListener(repaintAllGameComponents);
			}
		}
		
		
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
												 									 
		 
		ActionListener nothingSelected = e -> {
												if (e.getActionCommand() == "no_piece_selected") {
														hive.setSelectedPiece(null);
														hive.setPossiblePositions(null);
														//System.out.println(e.getActionCommand());
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
																	//System.out.println(e.getActionCommand() + ": " + e.getSource());
																}
														 };
											 
											 
		 for (Component component : pnlOfflineGame.getComponents()) {
				if (component instanceof HexField) {
					((HexField)component).addActionListener(pieceSelected);
					((HexField)component).addActionListener(nothingSelected);
					((HexField)component).addActionListener(possiblePositionSelected);
				}
			}
	}
	
	private void createCredits(){
		
		pnlCredits = new PnlCredits();
		
		//Listeners
		pnlCredits.addActionListener(e -> {
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, PnlMainMenu.MAIN_MENU_TAG);
	        //removeSettingsPanelsFromCards();
	        System.out.println(e.getSource());
		});
	
	}
	
	private LinkedHashMap <Class<?>, Integer> extractPiecesSet() {
		//FURTHER IMPLEMENTATION:
		//https://stackoverflow.com/questions/205573/at-runtime-find-all-classes-in-a-java-application-that-extend-a-base-class
		//////
		String pieceName;
		HashMap<String, String> piecesAndTheirQuantity = settingsXML.findElements("PIECES").get(0).getAttributes();
		ArrayList<Class<?>> bugTypes = new ArrayList<Class<?>>(Arrays.asList(QueenBee.class, Spider.class, Beetle.class, SoldierAnt.class, Grasshopper.class));
		LinkedHashMap <Class<?>, Integer> piecesSet = new LinkedHashMap();
		
		for (Entry<String, String> pieceAndQuantity : piecesAndTheirQuantity.entrySet()) {
			for (Class<?> bug : bugTypes) {
				try {
					pieceName = (String) bug.getDeclaredField("PIECE_NAME").get(null);
					
					if(pieceName.equals(pieceAndQuantity.getKey())) {
						piecesSet.put(bug, Integer.parseInt(pieceAndQuantity.getValue()));
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if (piecesSet.get(QueenBee.class) == null || piecesSet.get(QueenBee.class) != 1) {
			piecesSet.put(QueenBee.class, 1); //The Queen Bee must always be one
		}
		
		return piecesSet;		
	}
}
