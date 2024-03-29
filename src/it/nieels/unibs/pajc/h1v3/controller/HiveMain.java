package it.nieels.unibs.pajc.h1v3.controller;

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

import it.nieels.unibs.pajc.h1v3.controller.network.*;
import it.nieels.unibs.pajc.h1v3.model.*;
import it.nieels.unibs.pajc.h1v3.model.Piece.PieceColor;
import it.nieels.unibs.pajc.h1v3.utility.*;
import it.nieels.unibs.pajc.h1v3.view.*;

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
import java.net.BindException;
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

/**
 * The starting point of the program, a controller that coordinates the model and the view in a whole, functioning app.
 * @author Nicol Stocchetti
 *
 */
public class HiveMain {
	public static final String SETTINGS_PATH = "./xml/Settings.xml";
	
	public enum ServerStatus { OFF, ON, ERROR }; //OFF = server not active; ON = server active, ERROR = error while starting;
	static ServerStatus serverStatus = ServerStatus.OFF; 
	
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
	
	private PnlHostGame pnlHostGame;
	
	private PnlJoinGame pnlJoinGame;
	
	private PnlCredits pnlCredits;
	
	private PnlOnlineGame pnlOnlineGame;
	
	private PnlOfflineGame pnlOfflineGame;
	

	/**
	 * Launches the application.
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
	 * Creates the application.
	 */
	public HiveMain() {
		initialize(); //As a covention, the graphic components are created and put in the frame's content pane through the initialize method
	}

	/**
	 * Initializes the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setIconImage(new ImageIcon(getClass().getResource(HexField.VISUAL_RESOURCES_DIRECTORY + QueenBee.PIECE_NAME + ".png")).getImage());
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
		
		loadSettings();
	}
	
	/**
	 * Creates the main menu UI.
	 */
	private void createMainMenu(){
		pnlMainMenu = new PnlMainMenu();
		
		//Listeners
		pnlMainMenu.addActionListener(e -> {
			if(!e.getActionCommand().equals(PnlMainMenu.LOCAL_GAME_BTN)) {
				return;
			}
			if (pnlOfflineGame == null) {
				createOfflineGame();
				cards.add(pnlOfflineGame, PnlOfflineGame.OFFLINE_GAME_TAG);
			}
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, PnlOfflineGame.OFFLINE_GAME_TAG);
	        //System.out.println(e.getSource());
		});
		
		pnlMainMenu.addActionListener(e -> {
			if(!e.getActionCommand().equals(PnlMainMenu.SETTINGS_BTN)) {
				return;
			}
			//Upon entering the settings checks that the Settings file exists, if not generates a default one.
			createSettings();
			cards.add(pnlSettings, PnlSettings.SETTINGS_TAG);
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
			//Upon entering the credits...
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
			 * System.exit(int i) is to be used, but it might be better putting it inside a more generic shutdown() method, including
			 * "cleanup" steps as well, like closing socket connections, file descriptors, then exiting with System.exit().
			 */
			System.out.println("Exiting...");
			System.exit(0);
		});
	}
	
	/**
	 * Creates the settings menu UI.
	 */
	private void createSettings(){
		
		loadSettings();
		
		pnlSettings = new PnlSettings(settingsXML);
		
		//Listeners
		pnlSettings.addActionListener(e -> {
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, PnlMainMenu.MAIN_MENU_TAG);
	        cards.remove(pnlSettings);
	        pnlSettings = null;
	        //System.out.println(e.getSource());
		});
	}
	
	/**
	 * Checks whether the Settings file exists, if not generates a default one.
	 */
	void loadSettings() {
		File settingsFile = new File(SETTINGS_PATH);
		if (!settingsFile.isFile()) {
			try {
				settingsFile.getParentFile().mkdirs(); 
				settingsFile.createNewFile();
				
				settingsXML = generateDefaultSettings();
				XMLParser.writeDocument(settingsXML, SETTINGS_PATH);
				System.out.println("NEW DEFAULT SETTINGS GENERATED AND LOADED.");
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
		} else {
			settingsXML = XMLParser.extractXMLObject(SETTINGS_PATH);
			System.out.println("SETTINGS LOADED.");
		}
	}
	
	/**
	 * Creates a new XMLObject representing a default setting configuration in XML.
	 * @return the XML settings, XMLObject.
	 */
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
	
	/**
	 * Creates the host game menu UI, starts the client and server threads.
	 */
	private void createHostGame(){
		loadSettings();
		
		pnlHostGame = new PnlHostGame();
		
		//Listeners
		pnlHostGame.addActionListener(e -> {
			
			if(!e.getActionCommand().equals(PnlHostGame.CANCEL_BTN)) {
				return;
			}

			if (!clientThread.isInterrupted()) {
				clientThread.interrupt();
			}
			
			if (!serverThread.isInterrupted()) {
				serverThread.interrupt();
			}

			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, PnlMainMenu.MAIN_MENU_TAG);
	        cards.remove(pnlHostGame);
	        pnlHostGame = null;
	        //System.out.println(e.getSource());
		});
		
		pnlHostGame.addActionListener(e -> {
			
			if(!e.getActionCommand().startsWith(PnlHostGame.SET_PORT_BTN)) {
				return;
			}
			
			String port = e.getActionCommand().substring(PnlHostGame.SET_PORT_BTN.length());
			//System.out.println(port);
			
			//Generate hive for this game
			loadSettings();
			hive = new Hive(extractPiecesSet());
			
			//Start server with the new hive
			
			serverStatus = ServerStatus.OFF;
			
			Runnable startServer = () -> {
											try {
												HiveMain.serverStatus = ServerStatus.ON;
												new NetworkServer(Integer.parseInt(port)).start(hive);
											} catch (Exception ex) {
												HiveMain.serverStatus = ServerStatus.ERROR;
											}
										};
			serverThread = new Thread(startServer);
			serverThread.start();
			
			int i = 0;
			while(serverStatus == ServerStatus.OFF && i < 50) {
				i++;
				try {
					Thread.currentThread().sleep(100);
				} catch (InterruptedException e1) {	}
			}
			
			if (serverStatus != ServerStatus.ON) {
				if (!clientThread.isInterrupted()) {
					clientThread.interrupt();
				}
				
				if (!serverThread.isInterrupted()) {
					serverThread.interrupt();
				}

				pnlHostGame.showPortUnavailable();
		        return;
			}
			
			Runnable startClient = () -> {
				try {
					//Preparations
					client = new NetworkClient(NetworkClient.DEFAULT_SERVER, Integer.parseInt(port));
					
					client.addActionListener(f -> {
													if(!f.getActionCommand().startsWith(NetworkServer.START_GAME)) {
														return;
													}
													if (pnlOnlineGame == null) {
														createOnlineGame(PieceColor.WHITE);
														cards.add(pnlOnlineGame, PnlOnlineGame.ONLINE_GAME_TAG);
													}
													pnlOnlineGame.pause(); //The host's client starts in wait for the other player's first move
													System.out.println(cards.getComponents().length);
													CardLayout cl = (CardLayout)(cards.getLayout());
											        cl.show(cards, PnlOnlineGame.ONLINE_GAME_TAG);
					});
			        
			        client.addActionListener(f -> {
			        								if(!f.getActionCommand().startsWith(NetworkServer.HIVE_UPDATE)) {
			        									return;
			        								}
			        								
			        								hive = (Hive)Base64SerializationUtility.deserializeObjectFromString(f.getActionCommand().substring(NetworkServer.HIVE_UPDATE.length()));
			        								//System.out.println(System.currentTimeMillis());
			        								if (pnlOnlineGame != null) {
			        									pnlOnlineGame.update(hive);
			        								}
			        								//System.out.println("\nNEW HIVE UPDATED:\n" + hive);
												});
			        
			        client.addActionListener(f -> {
													if(!f.getActionCommand().startsWith(NetworkServer.ASK_FOR_MOVE)) {
														return;
													}
													pnlOnlineGame.start();
													//System.out.println("READY TO MAKE MOVE...");
												});
			        
			        client.addActionListener(f -> {
													if(!f.getActionCommand().startsWith(NetworkServer.VICTORY)) {
														return;
													}
													hive = (Hive)Base64SerializationUtility.deserializeObjectFromString(f.getActionCommand().substring(NetworkServer.VICTORY.length()));
													if (pnlOnlineGame != null) {
														pnlOnlineGame.update(hive);
														pnlOnlineGame.showDefeat();
													}
													//System.out.println("OTHER PLAYER WON");
												});
			        
			        client.addActionListener(f -> {
													if(!f.getActionCommand().startsWith(NetworkServer.DEFEAT)) {
														return;
													}
													hive = (Hive)Base64SerializationUtility.deserializeObjectFromString(f.getActionCommand().substring(NetworkServer.DEFEAT.length()));
													if (pnlOnlineGame != null) {
														pnlOnlineGame.update(hive);
														pnlOnlineGame.showVictory();
													}
													System.out.println("YOU WIN");
												});
			        
			        client.addActionListener(f -> {
													if(!f.getActionCommand().startsWith(NetworkServer.DRAW)) {
														return;
													}
													hive = (Hive)Base64SerializationUtility.deserializeObjectFromString(f.getActionCommand().substring(NetworkServer.DRAW.length()));
													if (pnlOnlineGame != null) {
														pnlOnlineGame.update(hive);
														pnlOnlineGame.showDraw();
													}
													System.out.println("IT'S A DRAW");
												});
			        
			        client.addActionListener(f -> {
													if(!f.getActionCommand().startsWith(NetworkServer.CHAT)) {
														return;
													}
													pnlOnlineGame.displayChatMessage(f.getActionCommand().substring(NetworkServer.CHAT.length()));
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
	
	/**
	 * Creates the join game menu UI, starts the client thread.
	 */
	private void createJoinGame(){
		
		pnlJoinGame = new PnlJoinGame();
		
		//Listeners
		pnlJoinGame.addActionListener(e -> {
			
			if(!e.getActionCommand().equals(PnlJoinGame.CANCEL_BTN)) {
				return;
			}
			
			if (!clientThread.isInterrupted()) {
				clientThread.interrupt();
			}
			
			if (!serverThread.isInterrupted()) {
				serverThread.interrupt();
			}
			
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, PnlMainMenu.MAIN_MENU_TAG);
	        cards.remove(pnlJoinGame);
	        pnlJoinGame = null;
	        //System.out.println(e.getSource());
		});
		
		pnlJoinGame.addActionListener(e -> {
			
			if(!e.getActionCommand().startsWith(PnlJoinGame.JOIN_AS_PLAYER_BTN)) {
				return;
			}
			
			String serverIPAndPort = e.getActionCommand().substring(PnlJoinGame.JOIN_AS_PLAYER_BTN.length());
			String serverIP = serverIPAndPort.split("@")[0];
			String port = serverIPAndPort.split("@")[1];
//			System.out.println(e.getActionCommand());
//			System.out.println(serverIP);
//			System.out.println(port);
			
			Runnable startClient = () -> {
				try {
					//Preparations
					client = new NetworkClient(serverIP, Integer.parseInt(port));
					
					client.addActionListener(f -> {
													if(!f.getActionCommand().startsWith(NetworkServer.START_GAME)) {
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
			        								if(!f.getActionCommand().startsWith(NetworkServer.HIVE_UPDATE)) {
			        									return;
			        								}
			        								
			        								hive = (Hive)Base64SerializationUtility.deserializeObjectFromString(f.getActionCommand().substring(NetworkServer.HIVE_UPDATE.length()));
			        								if (pnlOnlineGame != null) {
			        									pnlOnlineGame.update(hive);
			        								}
			        								//System.out.println("\nNEW HIVE UPDATED:\n" + hive);
												});
			        
			        client.addActionListener(f -> {
													if(!f.getActionCommand().startsWith(NetworkServer.ASK_FOR_MOVE)) {
														return;
													}
													pnlOnlineGame.repaint();
													pnlOnlineGame.start();
													//System.out.println("READY TO MAKE MOVE...");
												});
			        
			        client.addActionListener(f -> {
													if(!f.getActionCommand().startsWith(NetworkServer.VICTORY)) {
														return;
													}
													hive = (Hive)Base64SerializationUtility.deserializeObjectFromString(f.getActionCommand().substring(NetworkServer.VICTORY.length()));
			        								if (pnlOnlineGame != null) {
			        									pnlOnlineGame.update(hive);
			        									pnlOnlineGame.showDefeat();
			        								}
													//System.out.println("OTHER PLAYER WON");
												});
			        
			        client.addActionListener(f -> {
													if(!f.getActionCommand().startsWith(NetworkServer.DEFEAT)) {
														return;
													}
													hive = (Hive)Base64SerializationUtility.deserializeObjectFromString(f.getActionCommand().substring(NetworkServer.DEFEAT.length()));
													if (pnlOnlineGame != null) {
														pnlOnlineGame.update(hive);
														pnlOnlineGame.showVictory();
													}
													//System.out.println("YOU WIN");
												});
			        
			        client.addActionListener(f -> {
													if(!f.getActionCommand().startsWith(NetworkServer.DRAW)) {
														return;
													}
													hive = (Hive)Base64SerializationUtility.deserializeObjectFromString(f.getActionCommand().substring(NetworkServer.DRAW.length()));
													if (pnlOnlineGame != null) {
														pnlOnlineGame.update(hive);
														pnlOnlineGame.showDraw();
													}
													//System.out.println("IT'S A DRAW");
												});
			        
			        client.addActionListener(f -> {
													if(!f.getActionCommand().startsWith(NetworkServer.CHAT)) {
														return;
													}
													pnlOnlineGame.displayChatMessage(f.getActionCommand().substring(NetworkServer.CHAT.length()));
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
	
		//JoinAsSpectator copy from AsPlayer
	
	}
	
	/**
	 * Creates the online game UI and behaviour.
	 * @param playerColor the color of the player's pieces.
	 */
	private void createOnlineGame(PieceColor playerColor){
		
		pnlOnlineGame = new PnlOnlineGame(hive, playerColor);
		
		//Button listeners
		
		pnlOnlineGame.addActionListener(e -> {
			
			if(!e.getActionCommand().equals(PnlOnlineGame.BACK_BTN)) {
				return;
			}
			
			if (!clientThread.isInterrupted()) {
				clientThread.interrupt();
			}
			
			if (!serverThread.isInterrupted()) {
				serverThread.interrupt();
			}
			
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, PnlMainMenu.MAIN_MENU_TAG);
	        cards.remove(pnlOnlineGame);
	        pnlOnlineGame = null;
	        if (pnlJoinGame != null) {
	        	cards.remove(pnlJoinGame);
		        pnlJoinGame = null;
	        }
	        if (pnlHostGame != null) {
	        	cards.remove(pnlHostGame);
		        pnlHostGame = null;
	        }
	        //System.out.println(e.getSource());
		});
		
		pnlOnlineGame.addActionListener(e -> {
			if(!e.getActionCommand().equals(PnlOnlineGame.PASS_BTN)) {
				return;
			}
			pnlOnlineGame.pause();
			client.sendMsgToServer(NetworkServer.PASS);
		});
		
		pnlOnlineGame.addActionListener(e -> {
			if(!e.getActionCommand().startsWith(PnlOnlineGame.SEND_BTN)) {
				return;
			}
			client.sendMsgToServer(NetworkServer.CHAT + e.getActionCommand().substring(PnlOnlineGame.SEND_BTN.length()));
		});
		
		//Event listeners
		
		pnlOnlineGame.addActionListener(e -> {
			if(!e.getActionCommand().equals(PnlOnlineGame.MOVE_MADE_EVENT)) {
				return;
			}
			
			pnlOnlineGame.pause();
			this.hive = pnlOnlineGame.getHive();
			client.sendMsgToServer(NetworkServer.HIVE_UPDATE + Base64SerializationUtility.serializeObjectToString(hive));
		});
											 
		pnlOnlineGame.addActionListener(e -> {
			if(!e.getActionCommand().equals(PnlOnlineGame.VICTORY_EVENT)) {
				return;
			}
			pnlOnlineGame.showVictory();
			this.hive = pnlOnlineGame.getHive();
			client.sendMsgToServer(NetworkServer.VICTORY + Base64SerializationUtility.serializeObjectToString(hive));
		});
		
		pnlOnlineGame.addActionListener(e -> {
			if(!e.getActionCommand().equals(PnlOnlineGame.DEFEAT_EVENT)) {
				return;
			}
			
			pnlOnlineGame.showDefeat();
			this.hive = pnlOnlineGame.getHive();
			client.sendMsgToServer(NetworkServer.DEFEAT + Base64SerializationUtility.serializeObjectToString(hive));
		});
		
		pnlOnlineGame.addActionListener(e -> {
			if(!e.getActionCommand().equals(PnlOnlineGame.DRAW_EVENT)) {
				return;
			}
			pnlOnlineGame.showDraw();
			this.hive = pnlOnlineGame.getHive();
			client.sendMsgToServer(NetworkServer.DRAW + Base64SerializationUtility.serializeObjectToString(hive));
		});
		
	}
	
	/**
	 * Creates the offline game UI and behaviour.
	 */
	private void createOfflineGame(){
		
		loadSettings();
		hive = new Hive(extractPiecesSet());
		
		pnlOfflineGame = new PnlOfflineGame(hive);
		
		pnlOfflineGame.addActionListener(e -> {
			
			if(!e.getActionCommand().equals(PnlOnlineGame.BACK_BTN)) {
				return;
			}
			
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, PnlMainMenu.MAIN_MENU_TAG);
	        cards.remove(pnlOfflineGame);
	        pnlOfflineGame = null;
	        //System.out.println(e.getSource());
		});
	}
	
	/**
	 * 
	 * Creates the credits UI.
	 *
	 */
	private void createCredits(){
		
		pnlCredits = new PnlCredits();
		
		//Listeners
		pnlCredits.addActionListener(e -> {
			CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, PnlMainMenu.MAIN_MENU_TAG);
	        System.out.println(e.getSource());
		});
	
	}
	
	/**
	 * Extracts the pieces information from the XML settings file and creates a LinkedHashMap with the same information that can be given to the Hive constructor.
	 * @return a map containing the necessary information for the Hive constructor, LinkedHashMap <Class<?>, Integer>.
	 */
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
