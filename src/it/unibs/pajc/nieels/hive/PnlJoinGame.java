package it.unibs.pajc.nieels.hive;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
			private JButton btnJoinAsSpectator;
			private Component horizontalGlue_1;
			
		private JPanel pnlMsg;
			private JLabel lblConnectionRefused;
		
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
		
		lblTitle = new JLabel("CHOOSE A SERVER:");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Papyrus", Font.BOLD, 30));
		lblTitle.setForeground(Color.RED);
		pnlTitle.add(lblTitle, BorderLayout.CENTER);
		
		verticalStrut_3 = Box.createVerticalStrut(15);
		pnlTitle.add(verticalStrut_3, BorderLayout.SOUTH);
		
		
		
		pnlCenter = new JPanel();
		pnlCenter.setBackground(Color.ORANGE);
		this.add(pnlCenter, BorderLayout.CENTER);
		pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
		
		
		pnlServer = new JPanel();
		pnlServer.setBackground(Color.ORANGE);
		pnlCenter.add(pnlServer);
		pnlServer.setLayout(new BoxLayout(pnlServer, BoxLayout.X_AXIS));
		
		horizontalGlue = Box.createHorizontalGlue();
		pnlServer.add(horizontalGlue);
		
		lblServer = new JLabel("SERVER: ");
		lblServer.setHorizontalAlignment(SwingConstants.CENTER);
		lblServer.setFont(new Font("Papyrus", Font.PLAIN, 15));
		lblServer.setForeground(Color.RED);
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
		lblPort.setFont(new Font("Papyrus", Font.PLAIN, 15));
		lblPort.setForeground(Color.RED);
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
		
		horizontalStrut_1 = Box.createHorizontalStrut(20);
		pnlServer.add(horizontalStrut_1);
		
		btnJoinAsSpectator = new JButton(JOIN_AS_SPECTATOR_BTN);
		btnJoinAsSpectator.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlServer.add(btnJoinAsSpectator);
		
		horizontalGlue_1 = Box.createHorizontalGlue();
		pnlServer.add(horizontalGlue_1);
		
		
		pnlMsg = new JPanel();
		pnlMsg.setBackground(Color.ORANGE);
		pnlCenter.add(pnlMsg);
		pnlMsg.setLayout(new BoxLayout(pnlMsg, BoxLayout.X_AXIS));
		
		lblConnectionRefused = new JLabel("CONNECTION REFUSED!");
		lblConnectionRefused.setHorizontalAlignment(SwingConstants.CENTER);
		lblConnectionRefused.setFont(new Font("Papyrus", Font.BOLD, 20));
		lblConnectionRefused.setForeground(Color.RED);
		pnlMsg.add(lblConnectionRefused);
		lblConnectionRefused.setVisible(false);
		
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
		
		btnJoinAsPlayer.addActionListener(e -> {
			txtfldPort.setEnabled(false);
			txtfldServer.setEnabled(false);
			btnJoinAsPlayer.setEnabled(false);
			btnJoinAsSpectator.setEnabled(false);
			lblConnectionRefused.setVisible(false);
			//lblWait.setVisible(true);
			fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand() + txtfldServer.getText() + "@" + txtfldPort.getText(), e.getWhen(), e.getModifiers()));
		});
		
		//btnJoinAsSpectator copiare uguale da AsPlayer
	}
	
	public void connectionRefused() {
		lblConnectionRefused.setVisible(true);
		txtfldPort.setEnabled(true);
		txtfldServer.setEnabled(true);
		btnJoinAsPlayer.setEnabled(true);
		btnJoinAsSpectator.setEnabled(true);
	}
}
