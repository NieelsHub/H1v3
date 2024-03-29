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
import javax.swing.SwingConstants;

/**
 * This component provides a main menu UI.
 * @author Nicol Stocchetti
 *
 */
public class PnlMainMenu extends ImageEventJPanel {
	public static final String MAIN_MENU_TAG = "MAIN_MENU";
	
	static final String TITLE_IMAGE_FILENAME = "H1V3";
	
	public static final String SETTINGS_BTN = "SETTINGS";
	public static final String LOCAL_GAME_BTN = "LOCAL GAME";
	public static final String JOIN_GAME_BTN = "JOIN GAME";
	public static final String HOST_GAME_BTN = "HOST GAME";
	public static final String CREDITS_BTN = "CREDITS";
	public static final String EXIT_BTN = "EXIT";
	
	private JLabel lblTitle;
	
	private JPanel pnlButtons;
		private Component verticalGlue;
		private JButton btnHostGame;
		private Component verticalGlue_1;
		private JButton btnJoinGame;
		private Component verticalGlue_2;
		private JButton btnLocalGame;
		private Component verticalGlue_3;
		private JButton btnSettings;
		private Component verticalGlue_4;
		private JButton btnCredits;
		private Component verticalGlue_5;
	
	private JPanel pnlExit;
		private Component verticalStrut;
		private JButton btnExit;
		private Component verticalStrut_1;
	
	/**
	 * The constructor.
	 */
	public PnlMainMenu() {
		setBackground(new Color(255, 200, 0));
		setLayout(new BorderLayout(0, 0));
		
		lblTitle = new JLabel();
		lblTitle.setIcon(new ImageIcon(getClass().getResource(HexField.VISUAL_RESOURCES_DIRECTORY + TITLE_IMAGE_FILENAME + ".png")));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblTitle, BorderLayout.NORTH);
		
		
		
		pnlButtons = new JPanel();
		pnlButtons.setOpaque(false);
		pnlButtons.setBackground(new Color(255, 200, 0));
		this.add(pnlButtons, BorderLayout.CENTER);
		pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.Y_AXIS));
		
		verticalGlue = Box.createVerticalGlue();
		pnlButtons.add(verticalGlue);
		
		btnHostGame = new JButton(HOST_GAME_BTN);
		btnHostGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlButtons.add(btnHostGame);
		
		verticalGlue_1 = Box.createVerticalGlue();
		pnlButtons.add(verticalGlue_1);
		
		btnJoinGame = new JButton(JOIN_GAME_BTN);
		btnJoinGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlButtons.add(btnJoinGame);
		
		verticalGlue_2 = Box.createVerticalGlue();
		pnlButtons.add(verticalGlue_2);
		
		btnLocalGame = new JButton(LOCAL_GAME_BTN);
		btnLocalGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlButtons.add(btnLocalGame);
		
		verticalGlue_3 = Box.createVerticalGlue();
		pnlButtons.add(verticalGlue_3);
		
		btnSettings = new JButton(SETTINGS_BTN);
		btnSettings.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlButtons.add(btnSettings);
		
		verticalGlue_4 = Box.createVerticalGlue();
		pnlButtons.add(verticalGlue_4);
		
		btnCredits = new JButton(CREDITS_BTN);
		btnCredits.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlButtons.add(btnCredits);
		
		verticalGlue_5 = Box.createVerticalGlue();
		pnlButtons.add(verticalGlue_5);
		
		
		
		pnlExit = new JPanel();
		pnlExit.setOpaque(false);
		pnlExit.setBackground(new Color(255, 200, 0));
		this.add(pnlExit, BorderLayout.SOUTH);
		pnlExit.setLayout(new BoxLayout(pnlExit, BoxLayout.Y_AXIS));
		
		verticalStrut = Box.createVerticalStrut(15);
		pnlExit.add(verticalStrut);
		
		btnExit = new JButton(EXIT_BTN);
		btnExit.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlExit.add(btnExit);
		
		verticalStrut_1 = Box.createVerticalStrut(15);
		pnlExit.add(verticalStrut_1);
		
		
		//Listeners
		btnLocalGame.addActionListener(e -> fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand(), e.getWhen(), e.getModifiers())));
		
		btnSettings.addActionListener(e -> fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand(), e.getWhen(), e.getModifiers())));
		
		btnHostGame.addActionListener(e -> fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand(), e.getWhen(), e.getModifiers())));
		
		btnJoinGame.addActionListener(e -> fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand(), e.getWhen(), e.getModifiers())));
		
		btnCredits.addActionListener(e -> fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand(), e.getWhen(), e.getModifiers())));
		
		btnExit.addActionListener(e -> fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand(), e.getWhen(), e.getModifiers())));
	}

}
