package it.nieels.unibs.pajc.h1v3.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

/**
 * Extension of a JPanel capable of handling action and change listeners, storing them and executing them in
 * cumulative action and change events, that has a background image which can be loaded from a resources directory.
 * @author Nicol Stocchetti
 *
 */
public class ImageEventJPanel extends EventJPanel {

	static final String BACKGROUND_IMAGE_FILENAME = "BACKGROUND";
	
	Image img;
	
	Dimension screenSize;
	int width;
	int height;
	
	/**
	 * The constructor.
	 */
	public ImageEventJPanel() {
		img = new ImageIcon(getClass().getResource(HexField.VISUAL_RESOURCES_DIRECTORY + BACKGROUND_IMAGE_FILENAME + ".png")).getImage();
	}
	
	@Override
	  protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	    
	    screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int)screenSize.getWidth();
		height = (int)screenSize.getHeight();
	    
//	    int width = getWidth();   //If you prefer the background image to resize following the window size
//		int height = getHeight();
	    
	    g.drawImage(img, 0, 0, width, height, null);
	}

}
