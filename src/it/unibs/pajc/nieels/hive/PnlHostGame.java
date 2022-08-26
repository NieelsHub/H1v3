package it.unibs.pajc.nieels.hive;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PnlHostGame extends EventJPanel {
	static final String HOST_GAME_TAG = "HOST_GAME";
	
	static final String CANCEL_BTN = "CANCEL";
	static final String SET_PORT_BTN = "HOST";
	
	private JPanel pnlCenter;
		private JPanel pnlPort;
			private Component horizontalGlue;
			private JLabel lblchoosePort;
			private Component horizontalStrut;
			private JTextField txtfldPort;
			private Component horizontalStrut_1;
			private JButton btnSetPort;
			private Component horizontalGlue_1;
		//private Component verticalStrut_4;
		private JLabel lblWait;
		private Component verticalStrut_5;
	
	private JPanel pnlButtons;
		private Component verticalStrut;
		private JButton btnCancel;
		private Component verticalStrut_1;
	
	
	public PnlHostGame() {
		
		setBackground(Color.ORANGE);
		setLayout(new BorderLayout(0, 0));
		
		
		pnlCenter = new JPanel();
		pnlCenter.setBackground(Color.ORANGE);
		pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
		this.add(pnlCenter, BorderLayout.CENTER);
		
		pnlPort = new JPanel();
		pnlPort.setBackground(Color.ORANGE);
		pnlCenter.add(pnlPort, BorderLayout.CENTER);
		pnlPort.setLayout(new BoxLayout(pnlPort, BoxLayout.X_AXIS));
		
		horizontalGlue = Box.createHorizontalGlue();
		pnlPort.add(horizontalGlue);
		
		lblchoosePort = new JLabel("CHOOSE A PORT FOR YOUR SERVER: ");
		lblchoosePort.setHorizontalAlignment(SwingConstants.CENTER);
		lblchoosePort.setFont(new Font("Papyrus", Font.PLAIN, 15));
		lblchoosePort.setForeground(Color.RED);
		pnlPort.add(lblchoosePort);
		
		horizontalStrut = Box.createHorizontalStrut(20);
		pnlPort.add(horizontalStrut);
		
		txtfldPort = new JTextField();
		txtfldPort.setColumns(5);
		txtfldPort.setText(String.valueOf(NetworkServer.DEFAULT_PORT));
		txtfldPort.setBackground(Color.YELLOW);
		txtfldPort.setMaximumSize(new Dimension(50, 20));
		txtfldPort.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlPort.add(txtfldPort);
		txtfldPort.setEnabled(true);
		
		horizontalStrut_1 = Box.createHorizontalStrut(20);
		pnlPort.add(horizontalStrut_1);
		
		btnSetPort = new JButton(SET_PORT_BTN);
		btnSetPort.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlPort.add(btnSetPort);
		btnSetPort.setEnabled(true);
		
		horizontalGlue_1 = Box.createHorizontalGlue();
		pnlPort.add(horizontalGlue_1);
		
//		verticalStrut_4 = Box.createVerticalStrut(15);
//		pnlCenter.add(verticalStrut_4);
		
		lblWait = new JLabel("WAITING FOR PLAYERS...");
		lblWait.setHorizontalAlignment(SwingConstants.CENTER);
		lblWait.setFont(new Font("Papyrus", Font.BOLD, 20));
		lblWait.setForeground(Color.RED);
		pnlCenter.add(lblWait);
		lblWait.setVisible(false);
		
		verticalStrut_5 = Box.createVerticalGlue();
		pnlCenter.add(verticalStrut_5);
		
		
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
		
		btnSetPort.addActionListener(e -> {
			txtfldPort.setEnabled(false);
			btnSetPort.setEnabled(false);
			lblWait.setVisible(true);
			fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand() + txtfldPort.getText(), e.getWhen(), e.getModifiers()));
		});	
	}

}
