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
import javax.swing.SwingConstants;

public class PnlHostGame extends EventJPanel {
	static final String HOST_GAME_TAG = "HOST_GAME";
	
	static final String CANCEL_BTN = "CANCEL";
	
	private JLabel lblTitle;
	
	private JPanel pnlButtons;
		private Component verticalStrut;
		private JButton btnCancel;
		private Component verticalStrut_1;
	
	
	public PnlHostGame() {
		
		setBackground(Color.ORANGE);
		setLayout(new BorderLayout(0, 0));
		
		lblTitle = new JLabel("WAITING FOR PLAYERS...");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Papyrus", Font.BOLD, 20));
		lblTitle.setForeground(Color.RED);
		this.add(lblTitle, BorderLayout.CENTER);
		
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

}
