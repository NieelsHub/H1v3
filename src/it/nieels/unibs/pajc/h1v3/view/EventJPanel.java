package it.nieels.unibs.pajc.h1v3.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * Extension of a JPanel capable of handling action and change listeners, storing them and executing them in
 * cumulative action and change events.
 * @author Nicol Stocchetti
 *
 */
public class EventJPanel extends JPanel {

	protected EventListenerList listenerList = new EventListenerList(); //Creates the list that will store all the event listeners 
																		//that will be linked to this model by the controller.
	//We can use a basic ArrayList for this too, but it's better to use EventListenerList - a particular type of List that was
	//specifically created to be used for event listeners storage and has some useful methods for handling them (see later).
	
	//Change
	public void addChangeListener(ChangeListener l) { //can be called by the controller to add a listener of the ChangeListener type
		listenerList.add(ChangeListener.class, l); //we see that the .add() method for the EventListenerList class wants to store the
	}											   //listener's class too, to be able to recognize its type and do stuff with this info.
	
	public void removeChangeListener(ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}
	
	public void fireValuesChange(ChangeEvent e) { //when called, fires all the event listeners of type ChangeListener linked to
												  //this model by the controller.
		for(ChangeListener l : listenerList.getListeners(ChangeListener.class)) //We can see that knowing the type (class) of listener
																			//in the EventListenerList is useful for selecting only
																			//some specific types of listeners among all the listener
																			//linked to this model to be fired at a certain time.
			l.stateChanged(e);
	}
	
	//Action
	public void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);
	}									
	
	public void removeActionListener(ActionListener l) {
		listenerList.remove(ActionListener.class, l);
	}
	
	public void fireActionListener(ActionEvent e) {
		for(ActionListener l : listenerList.getListeners(ActionListener.class)) {
			l.actionPerformed(e);
		}
	}

}
