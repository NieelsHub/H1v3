package it.unibs.pajc.nieels.hive;

import java.awt.Point;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;

import it.unibs.pajc.nieels.hive.Piece.PieceColor;
import it.unibs.pajc.nieels.hive.Piece.Side;

//VIEW
public class GameField extends HexField {

	//We want to define a constructor which takes in the events listener that we'll be using to interact with the mouse
	public GameField() {
		super();
	}

	//An override of the paintComponent() method of the JPanel will allow us to paint directly on the canvas (instead of just using pre-made components)
	@Override
	protected void paintComponent(Graphics g) {//Graphics g is an object of the class JPanel that contains a set of instruments to draw on the canvas, we'll use it for everything we need to draw
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		//Draw pieces
		drawPieces(g2, hive.getPlacedPieces());
		
		//Draw selected piece
		drawSelectedPiece(g2);
		
		//Draw mouse position
		drawCursor(g2);
	}
	/*********************************************************
	private void drawPieces(Graphics2D g2) {
		Point2D.Double boardCoords;
		Image img;
		Color color;//lo prende dal file di impostazioni grafiche
		
		//g2.translate(width/2, height/2);
		
		for (Piece piece : hive.getPlacedPieces()) {
			/*
			x = piece.getX() * pieceSize + positionOffset.getX() + width/2.0;
			y = piece.getY() * pieceSize + positionOffset.getY() + height/2.0;
			*//*
			boardCoords = modelToBoard(piece.getCoordinates(), width/2.0, height/2.0);
			
			img = pieceImgs.get(piece.getName());
			
			if (piece.getColor() == PieceColor.WHITE) {
				color = Color.GRAY;
			} else {
				color = Color.BLACK;
			}
			drawPiece(g2, color, boardCoords.getX(), boardCoords.getY(), img);
		}
		
		/*
		int horizontalPosition = (int)(pieceSize * 1.5);
		for (Piece piece : hive.getWhitesToBePlaced()) {
			img = pieceImgs.get(piece.getName());
			drawPiece(g2, Color.GRAY, horizontalPosition, (int)(height*0.90), img);
			horizontalPosition += (int)(pieceSize * 1.5);
		}
		
		horizontalPosition = (int)(pieceSize * 1.5);
		for (Piece piece : hive.getBlacksToBePlaced()) {
			img = pieceImgs.get(piece.getName());
			drawPiece(g2, Color.BLACK, horizontalPosition, (int)(height*0.10), img);
			horizontalPosition += (int)(pieceSize * 1.5);
		}
		*//*
	}**************************************/
	/**************************************
	private void drawPiece(Graphics2D g2, Color color, double x, double y, Image img) {
		int[] xPoints = {(int)(x - pieceSize), (int)(x - pieceSize * 0.5), (int)(x + pieceSize * 0.5), (int)(x + pieceSize),
						(int)(x + pieceSize * 0.5), (int)(x - pieceSize * 0.5)}; //cos(60) = 0.5
		
		int[] yPoints = {(int)y, (int)(y + pieceSize * Math.sqrt(3)/2), (int)(y + pieceSize * Math.sqrt(3)/2), (int)y,
						(int)(y - pieceSize * Math.sqrt(3)/2), (int)(y - pieceSize * Math.sqrt(3)/2)}; //sin(60) = sqrt(3)/2
		
		g2.setColor(color);
		g2.fillPolygon(xPoints, yPoints, 6);
		g2.drawImage(img, (int)(x - pieceSize * 0.5 * imgSizeModifier), (int)(y - pieceSize * 0.5 * imgSizeModifier), (int)(pieceSize * imgSizeModifier), (int)(pieceSize * imgSizeModifier), null);
	}
	**************************************/
	/**************************************
	private void drawSelectedPiece(Graphics2D g2) {
		if (selectedPiece == null)
			return;
		
		
		Point2D.Double boardCoords = modelToBoard(selectedPiece.getCoordinates(), width/2.0, height/2.0);
		double x = boardCoords.getX();
		double y = boardCoords.getY();
		
		int[] xPoints = {(int)(x - pieceSize), (int)(x - pieceSize * 0.5), (int)(x + pieceSize * 0.5), (int)(x + pieceSize),
						(int)(x + pieceSize * 0.5), (int)(x - pieceSize * 0.5)}; //cos(60) = 0.5
		
		int[] yPoints = {(int)y, (int)(y + pieceSize * Math.sqrt(3)/2), (int)(y + pieceSize * Math.sqrt(3)/2), (int)y,
						(int)(y - pieceSize * Math.sqrt(3)/2), (int)(y - pieceSize * Math.sqrt(3)/2)}; //sin(60) = sqrt(3)/2
		
		g2.setColor(Color.RED); //Fai che è sceglibile dalle impostazioni
		g2.setStroke(new BasicStroke(3));
		g2.drawPolygon(xPoints, yPoints, 6);
	}
	**************************************/
	
/**************************************
	private Point2D.Double modelToBoard(Point2D.Double modelCoords, double originX, double originY) {
		
		double x = originX + modelCoords.getX() * pieceSize + positionOffset.getX();
		double y = originY + modelCoords.getY() * pieceSize + positionOffset.getY();
		
		Point2D.Double boardCoords = new Point2D.Double();
		boardCoords.setLocation(x, y);
		
		return boardCoords;
	}
	
	
	
	private Point2D.Double boardToModel(Point2D.Double boardCoords, double originX, double originY) {
		
		double x = (boardCoords.getX() - originX - positionOffset.getX())/pieceSize;
		double y = (boardCoords.getY() - originY - positionOffset.getY())/pieceSize;
		
		Point2D.Double modelCoords = new Point2D.Double();
		modelCoords.setLocation(x, y);
		
		return modelCoords;
	}
	**************************************/
	/**************************************
	private Piece getPieceAt(double x, double y) {
		double thisDistance;
		double smallestDistance = 1;
		Piece piece = null;
		
		Point2D.Double pointCoords = boardToModel(new Point2D.Double(x, y), width/2.0, height/2.0);
		
		for(Piece hex : hive.getPlacedPieces()) {
			thisDistance = Math.sqrt(Math.pow(pointCoords.getX() - hex.getCoordinates().getX(), 2)
									+ Math.pow(pointCoords.getY() - hex.getCoordinates().getY(), 2)); //Distance between the point and the center of the piece
			
			if (thisDistance < smallestDistance) {
				smallestDistance = thisDistance;
				piece = hex;
			}
		}
		return piece;
	}
	**************************************/
	
	//Events that are generated from this component to be handled by this component (the view tells itself to modify itself with events,
	//by firing events that are then re-caught by itself)
	/******************************************************
	double xVariation;
	double yVariation;
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDrag = true;
		xVariation = e.getPoint().getX() - mousePosition.getX();
		yVariation = e.getPoint().getY() - mousePosition.getY();
		
		positionOffset.setLocation(positionOffset.getX() + xVariation, positionOffset.getY() + yVariation);
		/*
		if(Math.abs(xVariation) > 2 || Math.abs(yVariation) > 2) {
			positionOffset.setLocation(positionOffset.getX() + xVariation, positionOffset.getY() + yVariation);
		}
		*//***********************************************
		mousePosition = e.getPoint();
		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePosition = e.getPoint(); //x and y
		//We need to force the component's redraw when the mouse is moved (it won't do it on his own by default because it's costly)
		this.repaint();
	}

	Piece selectedPiece;
	@Override
	public void mouseClicked(MouseEvent e) {
		/*Point boardPos = screenToBoard(e.getPoint());
		if(selectedPieceId == null) {
			Piece selectedPiece = game.getPieceAtPos(boardPos);
			selectedPieceId = selectedPiece != null ? selectedPiece.id : null;
		} else {
			game.move(selectedPieceId, boardPos.x, boardPos.y);
			selectedPieceId = null;
		}
		
		repaint();*/ /**********************************************+
		
		mousePosition = e.getPoint(); //x and y
		
		if(selectedPiece == null) {
			selectedPiece = getPieceAt(mousePosition.getX(), mousePosition.getY());
			
			if (selectedPiece != null) {
				//presenta delle possibili mosse;
			}
		} else {
			if(/*è una delle possibili mosse presentate*//*************************false) {
			//////moves logic
			} else {
				selectedPiece = getPieceAt(mousePosition.getX(), mousePosition.getY());
			}
			
		}
		
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseDrag = false;
		this.repaint();	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getUnitsToScroll() > 0) {
			pieceSizeModifier += wheelSensitivity;
		} else {
			pieceSizeModifier -= wheelSensitivity;
		}
		
		if (pieceSizeModifier < MIN_SIZE_MODIFIER) {
			pieceSizeModifier = MIN_SIZE_MODIFIER;
		} else if (pieceSizeModifier > MAX_SIZE_MODIFIER) {
			pieceSizeModifier = MAX_SIZE_MODIFIER;
		}
		this.repaint();
	}******************************************/
}
