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
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

public class PnlCredits extends EventJPanel {
	static final String CREDITS_TAG = "CREDITS";
	
	static final String BACK_BTN = "BACK";
	
	private JLabel lblTitle;
	
	private JTextPane txtpnCredits;
	
	private JPanel pnlButtons;
		private Component verticalStrut;
		private JButton btnBack;
		private Component verticalStrut_1;
		
		
	public PnlCredits() {

		setBackground(Color.ORANGE);
		setLayout(new BorderLayout(0, 0));
		
		lblTitle = new JLabel(CREDITS_TAG);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Papyrus", Font.BOLD, 30));
		lblTitle.setForeground(Color.RED);
		this.add(lblTitle, BorderLayout.NORTH);
		
		txtpnCredits = new JTextPane();
		txtpnCredits.setBackground(Color.ORANGE);
		txtpnCredits.setForeground(Color.RED);
		txtpnCredits.setText("Credits");
		this.add(txtpnCredits, BorderLayout.CENTER);
		
		
		pnlButtons = new JPanel();
		pnlButtons.setBackground(Color.ORANGE);
		this.add(pnlButtons, BorderLayout.SOUTH);
		pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.Y_AXIS));
		
		verticalStrut = Box.createVerticalStrut(15);
		pnlButtons.add(verticalStrut);
		
		btnBack = new JButton(BACK_BTN);
		btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlButtons.add(btnBack);
		
		verticalStrut_1 = Box.createVerticalStrut(15);
		pnlButtons.add(verticalStrut_1);
		
		//Listeners
		btnBack.addActionListener(e -> fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand(), e.getWhen(), e.getModifiers())));
		
	}

}
