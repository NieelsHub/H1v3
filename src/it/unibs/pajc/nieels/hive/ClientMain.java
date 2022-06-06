package it.unibs.pajc.nieels.hive;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.BoxLayout;
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
public class ClientMain {
	public static final String SETTINGS_PATH = "./xml/Settings.xml";
	
	XMLObject settingsXML;
	//VIEW
	private JFrame frame; //The application window, it has a content pane property, which is the area where the graphic components are put
	private JPanel contentPane;
	private JPanel cards; //a panel that uses CardLayout to show different game screens
	
	private JPanel pnlMainMenu; //Each of these panels is a different game screen
	final static String MAIN_MENU = "MAIN_MENU";
		private JLabel lblTitle;
		private JButton btnHostGame;
		private JPanel pnlButtons;
		private JButton btnJoinGame;
		private JButton btnSettings;
		private Component verticalGlue;
		private Component verticalGlue_1;
		private Component verticalGlue_2;
		private Component verticalGlue_3;
		private JButton btnLocalGame;
		private Component verticalGlue_4;
	
	private JPanel pnlSettings = new JPanel();
	private ArrayList<JPanel> pnlSettingsToRemove = new ArrayList<JPanel>();
	final static String SETTINGS = "SETTINGS";
		private JPanel panel;
		private JButton btnSaveChanges;
		private JButton btnBack;
		private JScrollPane scrollPane;
		private JPanel pnlScrollPane;
		private JPanel pnlPiece;
		private JLabel lblPiece;
		private JComboBox comboPiece;
		private JLabel lblGameSettings;
	
	private JPanel pnlJoinGame;
	private JPanel pnlHostGame;
	private JPanel pnlOfflineGame;
	//private ArrayList<JPanel> pnlOfflineGameToRemove = new ArrayList<JPanel>();
	final static String OFFLINE_GAME = "OFFLINE_GAME";
	
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
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		cards = new JPanel(new CardLayout());
		frame.getContentPane().add(cards);
		
		createMainMenu();
		cards.add(pnlMainMenu, MAIN_MENU);
		
		//createOfflineGame();
		//cards.add(pnlOfflineGame, OFFLINE_GAME);
		
		createSettings();
		cards.add(pnlSettings, SETTINGS);
		pnlSettingsToRemove.add(pnlSettings);
	}
	
	private void createMainMenu(){
		pnlMainMenu = new JPanel();
		pnlMainMenu.setBackground(Color.ORANGE);
		pnlMainMenu.setLayout(new BorderLayout(0, 0));
		
		lblTitle = new JLabel("H1V3");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Papyrus", Font.BOLD, 90));
		lblTitle.setForeground(Color.RED);
		pnlMainMenu.add(lblTitle, BorderLayout.NORTH);
		
		pnlButtons = new JPanel();
		pnlButtons.setBackground(Color.ORANGE);
		pnlMainMenu.add(pnlButtons, BorderLayout.CENTER);
		pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.Y_AXIS));
		
		verticalGlue_3 = Box.createVerticalGlue();
		pnlButtons.add(verticalGlue_3);
		
		btnHostGame = new JButton("HOST GAME");
		btnHostGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlButtons.add(btnHostGame);
		
		
		verticalGlue = Box.createVerticalGlue();
		pnlButtons.add(verticalGlue);
		
		btnJoinGame = new JButton("JOIN GAME");
		btnJoinGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlButtons.add(btnJoinGame);
		
		verticalGlue_1 = Box.createVerticalGlue();
		pnlButtons.add(verticalGlue_1);
		
		btnLocalGame = new JButton("LOCAL GAME");
		btnLocalGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlButtons.add(btnLocalGame);
		
		verticalGlue_4 = Box.createVerticalGlue();
		pnlButtons.add(verticalGlue_4);
		
		btnSettings = new JButton("SETTINGS");
		btnSettings.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlButtons.add(btnSettings);
		
		verticalGlue_2 = Box.createVerticalGlue();
		pnlButtons.add(verticalGlue_2);
		
		
		//Listeners
		btnHostGame.addActionListener(e -> {
			if (pnlOfflineGame == null) {
				createOfflineGame();
				cards.add(pnlOfflineGame, OFFLINE_GAME);
			}
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, OFFLINE_GAME);
		});
		
		btnSettings.addActionListener(e -> {
			//Upon entering the settings check that the Settings file exists, if not generate a default one.
			createSettings();
			cards.add(pnlSettings, SETTINGS);
			pnlSettingsToRemove.add(pnlSettings);
			System.out.println(cards.getComponents().length);
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, SETTINGS);
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
				System.out.println("NUOVE OPZIONI CREATE");
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
		} else {
			settingsXML = XMLParser.extractXMLObject(SETTINGS_PATH);
			System.out.println("OPZIONI CARICATE");
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
	
	private void createSettings(){
		
		loadSettings();
		
		pnlSettings = new JPanel();
		pnlSettings.setBackground(Color.ORANGE);
		pnlSettings.setLayout(new BorderLayout(0, 0));
		
		lblTitle = new JLabel("SETTINGS");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Papyrus", Font.BOLD, 90));
		lblTitle.setForeground(Color.RED);
		pnlSettings.add(lblTitle, BorderLayout.NORTH);
		
		panel = new JPanel();
		panel.setBackground(Color.ORANGE);
		pnlSettings.add(panel, BorderLayout.SOUTH);
		
		btnSaveChanges = new JButton("SAVE CHANGES");
		panel.add(btnSaveChanges);
		
		btnBack = new JButton("BACK");
		panel.add(btnBack);
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setBackground(Color.ORANGE);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pnlSettings.add(scrollPane, BorderLayout.CENTER);
		
		pnlScrollPane = new JPanel();
		pnlScrollPane.setBackground(Color.ORANGE);
		scrollPane.setViewportView(pnlScrollPane);
		pnlScrollPane.setLayout(new BoxLayout(pnlScrollPane, BoxLayout.Y_AXIS));
		
		lblGameSettings = new JLabel("Game Settings");
		lblGameSettings.setForeground(Color.RED);
		lblGameSettings.setFont(new Font("Papyrus", Font.PLAIN, 20));
		lblGameSettings.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblGameSettings.setHorizontalAlignment(SwingConstants.CENTER);
		pnlScrollPane.add(lblGameSettings);
		
		XMLElement piecesElement = settingsXML.findElements("PIECES").get(0);
		HashMap<String, String> piecesValues = piecesElement.getAttributes();
		
		String comboBoxItems[] = { "0", "1", "2", "3", "4", "5"};
		HashMap <JLabel, JComboBox> selectedValues = new HashMap <JLabel, JComboBox>();
		
		for (Entry<String, String> piece : piecesValues.entrySet()) {
			pnlPiece = new JPanel();
			pnlPiece.setBackground(Color.ORANGE);
			pnlScrollPane.add(pnlPiece);
			
			lblPiece = new JLabel(piece.getKey());
			pnlPiece.add(lblPiece);
			
			comboPiece = new JComboBox(comboBoxItems);
			comboPiece.setSelectedItem(piece.getValue());
			comboPiece.setPreferredSize(new Dimension(40, 20));
			pnlPiece.add(comboPiece);
			selectedValues.put(lblPiece, comboPiece);
		}
		
		//Listeners
		btnBack.addActionListener(e -> {
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, MAIN_MENU);
	        removeSettingsPanelsFromCards();
		});	
		
		btnSaveChanges.addActionListener(e -> {
			for( Entry<JLabel, JComboBox> sv : selectedValues.entrySet()) {
				piecesValues.put(sv.getKey().getText(), (String)sv.getValue().getSelectedItem());
			}
			
			settingsXML.findElements("PIECES").get(0).setAttributes(piecesValues);
			System.out.println(piecesValues);
			System.out.println(piecesElement);
			System.out.println(settingsXML.findElements("PIECES").get(0));
			XMLParser.writeDocument(settingsXML, SETTINGS_PATH);
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, MAIN_MENU);
	        removeSettingsPanelsFromCards();
		});	
	}
	
	public void removeSettingsPanelsFromCards() {
		for (JPanel pnl : pnlSettingsToRemove) {
			cards.remove(pnl);
		}
		/*
		Component[] cardComponents = cards.getComponents();
		int deleteAt = -1;
		int settingsPanels = 0;
		System.out.println("PROVO A RIMUOVERE I VECCHI PANNELLI...");
		do {
			for (int k = 0; k < cardComponents.length; k++) {
			    if (cardComponents[k] instanceof JPanel && ((JPanel) cardComponents[k]).getName() == SETTINGS) {
			        deleteAt = k;
			        settingsPanels++;
			    }
			}
			if (deleteAt > 0) {
			    cards.remove(deleteAt);
			    System.out.println("RIMOSSO!");
			}
		}
		while (settingsPanels > 0);
		*/
	}
	
	private void createOfflineGame(){
		loadSettings();
		
		pnlOfflineGame = new JPanel();
		pnlOfflineGame.setLayout(new BorderLayout(0, 0));
		
		GameField gameField = new GameField(); //The main game GUI component
		//FURTHER IMPLEMEMNTATION:
		//https://stackoverflow.com/questions/205573/at-runtime-find-all-classes-in-a-java-application-that-extend-a-base-class
		//////
		String pieceName;
		HashMap<String, String> piecesQuantity = settingsXML.findElements("PIECES").get(0).getAttributes();
		ArrayList<Class<?>> bugTypes = new ArrayList<Class<?>>(Arrays.asList(QueenBee.class, Spider.class, Beetle.class, SoldierAnt.class, Grasshopper.class));
		LinkedHashMap <Class<?>, Integer> piecesSet = new LinkedHashMap();
		
		for (Entry<String, String> quantity : piecesQuantity.entrySet()) {
			for (Class<?> bug : bugTypes) {
				try {
					pieceName = (String) bug.getDeclaredField("PIECE_NAME").get(null);
					
					if(pieceName.equals(quantity.getKey())) {
						piecesSet.put(bug, Integer.parseInt(quantity.getValue()));
					}
					
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		/*
		piecesSet.put(QueenBee.class, 1);
		piecesSet.put(Spider.class, 2);
		piecesSet.put(Beetle.class, 2);
		piecesSet.put(SoldierAnt.class, 3);
		piecesSet.put(Grasshopper.class, 3);
		*/
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
		
	 
		for (Component component : pnlOfflineGame.getComponents()) {
			if (component instanceof HexField) {
				((HexField)component).addChangeListener(repaintAllGameComponents);
			}
		}
		

//				ActionListener placedPieceClicked = e -> {
//														//make model calculate possible moves
//														if (e.getSource() != null) {
//															Object p = e.getSource();
//															if (e.getActionCommand() == "show_possible_moves") {
//																if (p instanceof Piece) {
//																	hive.setSelectedPiece((Piece)p);
//																}
//																ArrayList<Placement> possibleMoves = hive.calculatePossibleMoves(hive.getSelectedPiece());
//																hive.setPossiblePositions(possibleMoves);
//																System.out.println(e.getActionCommand() + ": " + ((Piece)e.getSource()).getName() + " can move on " + possibleMoves);
//																//I would trigger the possible moves showing in the view from here, if it was possible to do so without having to pass the Graphics2D object
//															} else if (e.getActionCommand() == "no_piece_selected") {
//																hive.setSelectedPiece(null);
//																hive.setPossiblePositions(null);
//																System.out.println(e.getActionCommand());
//																
//															} else if (e.getActionCommand() == "placement_selected") {
//																Object pl = e.getSource();
//																if (pl instanceof Placement) {
//																	hive.movePiece(hive.getSelectedPiece(), (Placement)pl);
//																}
//																hive.setSelectedPiece(null);
//																hive.setPossiblePositions(null);
//																System.out.println(e.getActionCommand() + ": " + e.getSource());
//															}
//														}
//													 };
									 
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
											 
											 
		 for (Component component : pnlOfflineGame.getComponents()) {
				if (component instanceof HexField) {
					((HexField)component).addActionListener(pieceSelected);
					((HexField)component).addActionListener(nothingSelected);
					((HexField)component).addActionListener(possiblePositionSelected);
				}
			}
	}
	
}
