package it.nieels.unibs.pajc.h1v3.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

/**
 * This component provides a credits UI.
 * @author Nicol Stocchetti
 *
 */
public class PnlCredits extends ImageEventJPanel {
	public static final String CREDITS_TAG = "CREDITS";
	
	static final String TITLE_IMAGE_FILENAME = "CREDITS";
	
	public static final String BACK_BTN = "BACK";
	
	private JLabel lblTitle;
	
	private JTextPane txtpnCredits;
	
	private JPanel pnlButtons;
		private Component verticalStrut;
		private JButton btnBack;
		private Component verticalStrut_1;
		
	/**
	 * The constructor.	
	 */
	public PnlCredits() {

		setBackground(new Color(255, 200, 0));
		setLayout(new BorderLayout(0, 0));
		
		lblTitle = new JLabel();
		lblTitle.setIcon(new ImageIcon(getClass().getResource(HexField.VISUAL_RESOURCES_DIRECTORY + TITLE_IMAGE_FILENAME + ".png")));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblTitle, BorderLayout.NORTH);
		
		txtpnCredits = new JTextPane();
		txtpnCredits.setEditable(false);
		txtpnCredits.setOpaque(false);///////
		txtpnCredits.setContentType("text/html");
		txtpnCredits.setText("<br><div style=\"font-family: tahoma; text-align: center; font-size: 15px\"><strong>GAME</strong>"
							+ "<br>Developed by NieelsHub (https://github.com/NieelsHub)"
							+ "<br>SmartScroller class by Rob Camick (https://tips4java.wordpress.com/2013/03/03/smart-scrolling/)<br>"
							+ "<br><strong>ICONS</strong>"
							+ "<br>Bee, spider, mosquito, ladybug, soldier ant and grasshopper icons created by Freepik, rhinoceros beetle icon created by tulpahn on https://www.flaticon.com<br>"
							+ "<br><strong>BACKGROUND IMAGE</strong>"
							+ "<br>Designed by Freepik using images from https://www.freepik.com<br>"
							+ "<br><strong>SCREEN TITLES</strong>"
							+ "<br>Generated by NieelsHub using https://www.inkpx.com<br></div>");
		//txtpnCredits.setFont(new Font("Arial Black", Font.PLAIN, 20));
		this.add(txtpnCredits, BorderLayout.CENTER);
		
		
		pnlButtons = new JPanel();
		pnlButtons.setOpaque(false);
		pnlButtons.setBackground(new Color(255, 200, 0));
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
