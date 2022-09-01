package it.unibs.pajc.nieels.hive;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class NetworkClient {
	public static final String DEFAULT_SERVER = "127.0.0.1";
	public static final int DEFAULT_PORT = 1234;
	
	String serverHost;
	int port;
	
	String clientType;
	String clientName;
	
	Socket client;
	BufferedReader in;
	PrintWriter out;
	BufferedReader stdin;
	
	/////EVENT HANDLING/////
	protected EventListenerList listenerList = new EventListenerList();
	
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
	//////////////////
	
	public NetworkClient() throws ConnectException {
		this(DEFAULT_SERVER, DEFAULT_PORT);
	}
	
	public NetworkClient(String serverHost, int port) throws ConnectException {
		this.serverHost = serverHost;
		this.port = port;
		
		try {
			client = new Socket(serverHost, port);
			
			out = new PrintWriter(client.getOutputStream());
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
		} catch(ConnectException ex) {
			throw ex;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		stdin = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void close() {
		sendMsgToServer("CLIENT - Disconnecting...");
		System.out.println("CLIENT - Disconnecting...");
		
		try {
			if(stdin != null) {
				stdin.close();
			}
			
			if(in != null) {
				in.close();
			}
			
			if(out != null) {
				out.close();
			}
			
			if(client != null) {
				client.close();	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startPlayer() {
			this.clientType = NetworkServer.PLAYER_TYPE;
			
			System.out.printf("\nCLIENT - Connected to server - IP: %s [port: %d]\n", client.getRemoteSocketAddress(), client.getPort());
			
			ExecutorService ex = Executors.newFixedThreadPool(2);
			
			ex.submit(() -> clientToServer());
			ex.submit(() -> serverToClient());
			
			ex.shutdown();
			try {
				ex.awaitTermination(1, TimeUnit.DAYS);
			} catch (InterruptedException e) {
				//Only used to handle the interrupt
				//When we catch this exception the interrupt flag is automatically reset to false, so we manually set it back to true to keep
				//the thread's interrupt status on.
		        Thread.currentThread().interrupt();
			} finally {
				this.close();
			}
	}
	
	/*
	public void startSpectator() {
		
		try(
				Socket client = new Socket(serverHost, port);
		) {
			
			
			this.clientType = NetworkServer.SPECTATOR_TYPE;
			
			
			
			
			ExecutorService ex = Executors.newFixedThreadPool(2);
			
			System.out.printf("\nCLIENT - Connected to server - IP: %s [port: %d]\n", client.getRemoteSocketAddress(), client.getPort());
			
			ex.submit(() -> clientToServer(client));
			ex.submit(() -> serverToClient(client));
			ex.shutdown();
			ex.awaitTermination(1, TimeUnit.DAYS);
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		

	}*/
	
	protected void clientToServer() {
		String request;
		
		try {
			while((request = stdin.readLine()) != null) {
				//System.out.printf("{c2S -> %s}\n", request);
				out.println(request);
				out.flush(); 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void serverToClient() {
		String serverMsg;	
		try {
			while((serverMsg = in.readLine()) != null) {
				System.out.printf("\nCLIENT - Server request: [%s]\n", serverMsg);
				
				if(serverMsg.startsWith(NetworkServer.ASK_CLIENT_TYPE)) {
					out.println(this.clientType);
					out.flush(); 
					System.out.printf("\nCLIENT - Type declared: [%s]\n", this.clientType);
				}
				
				else if(serverMsg.startsWith(NetworkServer.ASK_FOR_MOVE)) {
					System.out.printf("\nCLIENT - Calculating next move...");
					fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, serverMsg));
				}
				
				else if(serverMsg.startsWith(NetworkServer.HIVE_UPDATE)) {
					fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, serverMsg)); //For view update
				}
				
				else if(serverMsg.startsWith(NetworkServer.START_GAME)) {
					fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, serverMsg)); //For view update
				}
				
				else if(serverMsg.startsWith(NetworkServer.VICTORY)) {
					fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, serverMsg)); //For view update
				}
				
				else if(serverMsg.startsWith(NetworkServer.DEFEAT)) {
					fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, serverMsg)); //For view update
				}
				
				else if(serverMsg.startsWith(NetworkServer.DRAW)) {
					fireActionListener(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, serverMsg)); //For view update
				}

				else {
					System.out.printf("\nCLIENT - unable to elaborate server request: [%s]", serverMsg);
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void sendMsgToServer(String msg) {
		out.println(msg);
		out.flush();
		System.out.printf("\nCLIENT - Request to server: [%s]\n", msg);
	}
	
}
