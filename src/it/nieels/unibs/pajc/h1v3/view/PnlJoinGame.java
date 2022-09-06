package it.nieels.unibs.pajc.h1v3.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import it.nieels.unibs.pajc.h1v3.controller.network.NetworkClient;

/**
 * This component provides a join game UI.
 * @author Nicol Stocchetti
 *
 */
public class PnlJoinGame extends ImageEventJPanel {
	public static final String JOIN_GAME_TAG = "JOIN_GAME";
	
	static final String TITLE_IMAGE_FILENAME = "JOIN GAME";
	
	public static final String CANCEL_BTN = "CANCEL";
	public static final String JOIN_AS_PLAYER_BTN = "JOIN AS PLAYER";
	public static final String JOIN_AS_SPECTATOR_BTN = "JOIN AS SPECTATOR";
	
	private JLabel lblTitle;
	
	private JPanel pnlCenter;
		private JPanel pnlServer;
			private Component horizontalGlue;
			private JLabel lblServer;
			private Component horizontalStrut;
			private JTextField txtfldServer;
			private Component horizontalStrut_2;
			private JLabel lblPort;
			private Component horizontalStrut_3;
			private JTextField txtfldPort;
			private Component horizontalStrut_4;
			private JButton btnJoinAsPlayer;
			private Component horizontalStrut_1;
//			private JButton btnJoinAsSpectator;*****************************************************************
			private Component horizontalGlue_1;
			
		private JLabel lblConnectionRefused;
		private Component verticalStrut_5;
		
	private JPanel pnlButtons;
		private Component verticalStrut;
		private JButton btnCancel;
		private Component verticalStrut_1;
	
	/**
	 * The constructor.
	 */
	public PnlJoinGame() {
		
		setBackground(new Color(255, 200, 0));
		setLayout(new BorderLayout(0, 0));
		
		lblTitle = new JLabel();
		lblTitle.setIcon(new ImageIcon(getClass().getResource(HexField.VISUAL_RESOURCES_DIRECTORY + TITLE_IMAGE_FILENAME + ".png")));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblTitle, BorderLayout.NORTH);
		
		
		
		pnlCenter = new JPanel();
		pnlCenter.setOpaque(false);
		pnlCenter.setBackground(new Color(255, 200, 0));
		pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
		this.add(pnlCenter, BorderLayout.CENTER);
		
		pnlServer = new JPanel();
		pnlServer.setOpaque(false);
		pnlServer.setBackground(new Color(255, 200, 0));
		pnlCenter.add(pnlServer);
		pnlServer.setLayout(new BoxLayout(pnlServer, BoxLayout.X_AXIS));
		
		horizontalGlue = Box.createHorizontalGlue();
		pnlServer.add(horizontalGlue);
		
		lblServer = new JLabel("SERVER: ");
		lblServer.setHorizontalAlignment(SwingConstants.CENTER);
		lblServer.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblServer.setForeground(Color.BLACK);
		pnlServer.add(lblServer);
		
		horizontalStrut = Box.createHorizontalStrut(20);
		pnlServer.add(horizontalStrut);
		
		txtfldServer = new JTextField();
		txtfldServer.setColumns(15);
		txtfldServer.setText(NetworkClient.DEFAULT_SERVER);
		txtfldServer.setBackground(Color.YELLOW);
		txtfldServer.setMaximumSize(new Dimension(50, 20));
		txtfldServer.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlServer.add(txtfldServer);
		txtfldServer.setEnabled(true);
		
		horizontalStrut_2 = Box.createHorizontalStrut(20);
		pnlServer.add(horizontalStrut_2);
		
		lblPort = new JLabel("PORT: ");
		lblPort.setHorizontalAlignment(SwingConstants.CENTER);
		lblPort.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblPort.setForeground(Color.BLACK);
		pnlServer.add(lblPort);
		
		horizontalStrut_3 = Box.createHorizontalStrut(20);
		pnlServer.add(horizontalStrut_3);
		
		txtfldPort = new JTextField();
		txtfldPort.setColumns(5);
		txtfldPort.setText(String.valueOf(NetworkClient.DEFAULT_PORT));
		txtfldPort.setBackground(Color.YELLOW);
		txtfldPort.setMaximumSize(new Dimension(50, 20));
		txtfldPort.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlServer.add(txtfldPort);
		txtfldPort.setEnabled(true);
		
		horizontalStrut_4 = Box.createHorizontalStrut(20);
		pnlServer.add(horizontalStrut_4);
		
		btnJoinAsPlayer = new JButton(JOIN_AS_PLAYER_BTN);
		btnJoinAsPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlServer.add(btnJoinAsPlayer);
		//btnJoinAsPlayer.setEnabled(searchingForPlayers);
		/***********************************************************************************************
		horizontalStrut_1 = Box.createHorizontalStrut(20);
		pnlServer.add(horizontalStrut_1);
		
		btnJoinAsSpectator = new JButton(JOIN_AS_SPECTATOR_BTN);
		btnJoinAsSpectator.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlServer.add(btnJoinAsSpectator);
		*///*********************************************************************************************
		horizontalGlue_1 = Box.createHorizontalGlue();
		pnlServer.add(horizontalGlue_1);
		

		lblConnectionRefused = new JLabel("   ");
		lblConnectionRefused.setAlignmentX(CENTER_ALIGNMENT);
		lblConnectionRefused.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblConnectionRefused.setForeground(Color.BLACK);
		pnlCenter.add(lblConnectionRefused);
		
		verticalStrut_5 = Box.createVerticalGlue();
		pnlCenter.add(verticalStrut_5);
		
		
		
		pnlButtons = new JPanel();
		pnlButtons.setOpaque(false);
		pnlButtons.setBackground(new Color(255, 200, 0));
		this.add(pnlButtons, BorderLayout.SOUTH);
		pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.Y_AXIS));
		
		verticalStrut = Box.createVerticalStrut(15);
		pnlButtons.add(verticalStrut);
		
		btnCancel = new JButton(CANCEL_BTN);
		btnCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlButtons.add(btnCancel);
		
		verticalStrut_1 = Box.createVerticalStrut(15);
		pnlButtons.add(verticalStrut_1);
		
		
		
		//Listeners
		btnCancel.addActionListener(e -> fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand(), e.getWhen(), e.getModifiers())));
		
		btnJoinAsPlayer.addActionListener(e -> {
			txtfldPort.setEnabled(false);
			txtfldServer.setEnabled(false);
			btnJoinAsPlayer.setEnabled(false);
//			btnJoinAsSpectator.setEnabled(false);******************************************************************************************************
			lblConnectionRefused.setText("   ");
			
			fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand() + txtfldServer.getText() + "@" + txtfldPort.getText(), e.getWhen(), e.getModifiers()));
		});
		
		//btnJoinAsSpectator copy from AsPlayer
	}
	
	public void connectionRefused() {
		lblConnectionRefused.setText("CONNECTION REFUSED!");
		txtfldPort.setEnabled(true);
		txtfldServer.setEnabled(true);
		btnJoinAsPlayer.setEnabled(true);
//		btnJoinAsSpectator.setEnabled(true);***********************************************************************************************************************
	}
}
