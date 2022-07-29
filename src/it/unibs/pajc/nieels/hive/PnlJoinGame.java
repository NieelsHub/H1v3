package it.unibs.pajc.nieels.hive;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class PnlJoinGame extends EventJPanel {
	static final String JOIN_GAME_TAG = "JOIN_GAME";
	
	static final String CANCEL_BTN = "CANCEL";
	static final String JOIN_AS_PLAYER_BTN = "JOIN AS PLAYER";
	static final String JOIN_AS_SPECTATOR_BTN = "JOIN AS SPECTATOR";
	
	private JPanel pnlTitle;
		private Component verticalStrut_2;
		private JLabel lblTitle;
		private Component verticalStrut_3;
	
	private JPanel pnlGames;
		private JScrollPane scrollPane;
			private JPanel pnlScrollPane;
				private Component verticalStrut_4;
				private JPanel pnlServer;
					private Component horizontalGlue;
					private JLabel lblServer;
					private Component horizontalStrut;
					private JButton btnJoinAsPlayer;
					private Component horizontalStrut_1;
					private JButton btnJoinAsSpectator;
					private Component horizontalGlue_1;
				
		
	private JPanel pnlButtons;
		private Component verticalStrut;
		private JButton btnCancel;
		private Component verticalStrut_1;
	
	
	public PnlJoinGame() {
		
		setBackground(Color.ORANGE);
		setLayout(new BorderLayout(0, 0));
		
		pnlTitle = new JPanel();
		pnlTitle.setBackground(Color.ORANGE);
		this.add(pnlTitle, BorderLayout.NORTH);
		pnlTitle.setLayout(new BorderLayout(0, 0));
		
		verticalStrut_2 = Box.createVerticalStrut(15);
		pnlTitle.add(verticalStrut_2, BorderLayout.NORTH);
		
		lblTitle = new JLabel("AVAILABLE GAMES:");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Papyrus", Font.BOLD, 30));
		lblTitle.setForeground(Color.RED);
		pnlTitle.add(lblTitle, BorderLayout.CENTER);
		
		verticalStrut_3 = Box.createVerticalStrut(15);
		pnlTitle.add(verticalStrut_3, BorderLayout.SOUTH);
		
		
		
		pnlGames = new JPanel();
		pnlGames.setBackground(Color.ORANGE);
		this.add(pnlGames, BorderLayout.CENTER);
		pnlGames.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setBackground(Color.ORANGE);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pnlGames.add(scrollPane, BorderLayout.CENTER);
		
		pnlScrollPane = new JPanel();
		pnlScrollPane.setBackground(Color.ORANGE);
		scrollPane.setViewportView(pnlScrollPane);
		pnlScrollPane.setLayout(new BoxLayout(pnlScrollPane, BoxLayout.Y_AXIS));
		
		
		
		pnlButtons = new JPanel();
		pnlButtons.setBackground(Color.ORANGE);
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
		
	}
	
	public void addGame(String serverHost, int port, boolean searchingForPlayers) {
		
		verticalStrut_4 = Box.createVerticalStrut(15);
		pnlScrollPane.add(verticalStrut_4);
		
		
		
		pnlServer = new JPanel();
		pnlServer.setBackground(Color.ORANGE);
		pnlScrollPane.add(pnlServer, BorderLayout.CENTER);
		pnlServer.setLayout(new BoxLayout(pnlServer, BoxLayout.X_AXIS));
		
		horizontalGlue = Box.createHorizontalGlue();
		pnlServer.add(horizontalGlue);
		
		lblServer = new JLabel("- "+ serverHost + " on " + port + " -");
		lblServer.setHorizontalAlignment(SwingConstants.CENTER);
		lblServer.setFont(new Font("Papyrus", Font.PLAIN, 15));
		lblServer.setForeground(Color.RED);
		pnlServer.add(lblServer);
		
		horizontalStrut = Box.createHorizontalStrut(20);
		pnlServer.add(horizontalStrut);
		
		btnJoinAsPlayer = new JButton(JOIN_AS_PLAYER_BTN);
		btnJoinAsPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlServer.add(btnJoinAsPlayer);
		btnJoinAsPlayer.setEnabled(searchingForPlayers);
		
		horizontalStrut_1 = Box.createHorizontalStrut(20);
		pnlServer.add(horizontalStrut_1);
		
		btnJoinAsSpectator = new JButton(JOIN_AS_SPECTATOR_BTN);
		btnJoinAsSpectator.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlServer.add(btnJoinAsSpectator);
		
		horizontalGlue_1 = Box.createHorizontalGlue();
		pnlServer.add(horizontalGlue_1);
		
		this.repaint();
	}

}
