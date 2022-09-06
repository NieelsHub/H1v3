package it.nieels.unibs.pajc.h1v3.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import it.nieels.unibs.pajc.h1v3.controller.*;
import it.nieels.unibs.pajc.h1v3.model.*;
import it.nieels.unibs.pajc.h1v3.utility.*;

public class PnlSettings extends ImageEventJPanel {
	public static final String SETTINGS_TAG = "SETTINGS";
	
	static final String TITLE_IMAGE_FILENAME = "SETTINGS";
	
	public static final String BACK_BTN = "BACK";
	public static final String SAVE_CHANGES_BTN = "SAVE CHANGES";
	
	static final String PIECE_NUMBER_LBL = "NUMBER OF PIECES";
	
	private JLabel lblTitle;
	
	private JPanel pnlButtons;
		private JButton btnSaveChanges;
		private Component rigidArea;
		private JButton btnBack;
		
	private JScrollPane scrollPane;
		private JPanel pnlScrollPane;
			private JLabel lblGameSettings;
			private Component verticalStrut_5;
			private JPanel pnlPiece;
				private JLabel lblPiece;
				private JComboBox comboPiece;
			
	
	public PnlSettings(XMLObject settingsXML) {
		setBackground(new Color(255, 200, 0));
		setLayout(new BorderLayout(0, 0));
		
		lblTitle = new JLabel();
		lblTitle.setIcon(new ImageIcon(getClass().getResource(HexField.VISUAL_RESOURCES_DIRECTORY + TITLE_IMAGE_FILENAME + ".png")));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblTitle, BorderLayout.NORTH);
		
		pnlButtons = new JPanel();
		pnlButtons.setOpaque(false);/////////
		pnlButtons.setBackground(new Color(255, 200, 0));
		this.add(pnlButtons, BorderLayout.SOUTH);
		
		btnSaveChanges = new JButton(SAVE_CHANGES_BTN);
		pnlButtons.add(btnSaveChanges);
		
		rigidArea = Box.createRigidArea(new Dimension(20, 15 + btnSaveChanges.getHeight() + 15));
		pnlButtons.add(rigidArea);
		
		btnBack = new JButton(BACK_BTN);
		pnlButtons.add(btnBack);
		
		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);//////////
		scrollPane.getViewport().setOpaque(false);////////
		scrollPane.setBorder(null);
		scrollPane.setBackground(new Color(255, 200, 0));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(scrollPane, BorderLayout.CENTER);
		
		pnlScrollPane = new JPanel();
		pnlScrollPane.setOpaque(false);////////
		pnlScrollPane.setBackground(new Color(255, 200, 0));
		scrollPane.setViewportView(pnlScrollPane);
		pnlScrollPane.setLayout(new BoxLayout(pnlScrollPane, BoxLayout.Y_AXIS));
		
		lblGameSettings = new JLabel(PIECE_NUMBER_LBL);
		lblGameSettings.setForeground(Color.BLACK);
		lblGameSettings.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblGameSettings.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblGameSettings.setHorizontalAlignment(SwingConstants.CENTER);
		pnlScrollPane.add(lblGameSettings);
		
		verticalStrut_5 = Box.createVerticalGlue();
		pnlScrollPane.add(verticalStrut_5);
		
		XMLElement piecesElement = settingsXML.findElements("PIECES").get(0);
		HashMap<String, String> piecesValues = piecesElement.getAttributes();
		
		String comboBoxItems[] = { "0", "1", "2", "3", "4", "5"};
		HashMap <JLabel, JComboBox> selectedValues = new HashMap <JLabel, JComboBox>();
		
		String comboBoxItemsQueenBee[] = {"1"};
		String queenBeeLabel = "";
		try {
			queenBeeLabel = (String) QueenBee.class.getDeclaredField("PIECE_NAME").get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (Entry<String, String> piece : piecesValues.entrySet()) {
			pnlPiece = new JPanel();
			pnlPiece.setOpaque(false);////////
			pnlPiece.setBackground(new Color(255, 200, 0));
			pnlScrollPane.add(pnlPiece);
			
			lblPiece = new JLabel(piece.getKey().replace('_', ' '));
			lblPiece.setForeground(Color.BLACK);
			lblPiece.setFont(new Font("Tahoma", Font.PLAIN, 15));
			pnlPiece.add(lblPiece);
			
			comboPiece = new JComboBox(comboBoxItems);
			if (piece.getKey() == queenBeeLabel) {
				comboPiece = new JComboBox(comboBoxItemsQueenBee);
			}
			comboPiece.setSelectedItem(piece.getValue());
			comboPiece.setPreferredSize(new Dimension(60, 20));
			pnlPiece.add(comboPiece);
			selectedValues.put(lblPiece, comboPiece);
		}
		
		//Listeners
		btnBack.addActionListener(e -> fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand(), e.getWhen(), e.getModifiers())));	
		
		btnSaveChanges.addActionListener(e -> {
			for( Entry<JLabel, JComboBox> sv : selectedValues.entrySet()) {
				piecesValues.put(sv.getKey().getText().replace(' ', '_'), (String)sv.getValue().getSelectedItem());
			}
			
			settingsXML.findElements("PIECES").get(0).setAttributes(piecesValues);
			XMLParser.writeDocument(settingsXML, HiveMain.SETTINGS_PATH);
			fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand(), e.getWhen(), e.getModifiers()));
		});	
	}

}
