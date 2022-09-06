package it.nieels.unibs.pajc.h1v3.controller.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Creates a specific server communication protocol for each client that connects to a server, that is,
 * how the server interprets and responds to client requests.
 * @author Nicol Stocchetti
 *
 */
public class CommunicationProtocol implements Runnable {
	
	private static ArrayList<CommunicationProtocol> playerList = new ArrayList();

	private Socket clientSocket;
	private String clientName;
	private PrintWriter out;
	
	/**
	 * The constructor.
	 * @param clientSocket the connected client, Socket.
	 * @param clientName the connected client's name, String.
	 */
	public CommunicationProtocol(Socket clientSocket, String clientName) {
		this.clientSocket = clientSocket;
		this.clientName = clientName;
		playerList.add(this);
	}
	
	/**
	 * closes the connection with this client.
	 */
	public void close() {
		
		sendMsgToAll(String.format("Client %s disconnected.", clientName));
		
		if(out != null)
			out.close();

		try {
			if(clientSocket != null)
				clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		playerList.remove(this);
		
		System.out.println(String.format("\nSERVER - Client %s disconnected.", clientName));
	}
	
	/**
	 * Starts the communication protocol.
	 */
	public void run() {
		try(
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		){
			//Setup
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			System.out.printf("\nSERVER - Started client %s [%d] (name: %s)\n", clientSocket.getInetAddress(), clientSocket.getPort(), clientName);
			
			///sendMsgToAll("CLIENT " + clientName + " - Connected"); chat message
			
			//Communication protocol
			String request;
			while((request = in.readLine()) != null) {
				System.out.printf("\nSERVER - Client %s request: [%s]\n", clientName, request);
				
				if(request.startsWith(NetworkServer.QUIT)) {
					System.out.printf("\nSERVER - Disconnecting client %s...", clientName);
					sendMsg(String.format("\nSERVER - Disconnecting client %s...", clientName));
					break;
				}
				
				else if(request.startsWith(NetworkServer.CHAT)) {
					String response = String.format("%s[%s]: %s", NetworkServer.CHAT, clientName, request.substring(NetworkServer.CHAT.length()));
					sendMsgToAll(response);
				}
				
				else if(request.startsWith(NetworkServer.HIVE_UPDATE)) {
					String response = request;
					sendMsgToAllExceptThis(response);
					sendMsgToAllExceptThis(NetworkServer.ASK_FOR_MOVE);
				}
				
				else if(request.startsWith(NetworkServer.PASS)) {
					sendMsgToAllExceptThis(NetworkServer.ASK_FOR_MOVE);
				}
				
				else if(request.startsWith(NetworkServer.VICTORY)) {
					String response = request;
					sendMsgToAllExceptThis(response);
				}
				
				else if(request.startsWith(NetworkServer.DEFEAT)) {
					String response = request;
					sendMsgToAllExceptThis(response);
				}
				
				else if(request.startsWith(NetworkServer.DRAW)) {
					String response = request;
					sendMsgToAllExceptThis(response);
				}
				
				else {
					System.out.printf("\nSERVER - unable to elaborate request: [%s]", request);
				}
				
			}
			
			
		} catch(IOException ex) {
			
		} finally {
			this.close();
		}
	}
	
	/**
	 * Sends a message from the server to this client.
	 * @param msg the message, String.
	 */
	void sendMsg(String msg) {
		this.out.println(msg);
		this.out.flush();
	}
	
	/**
	 * Sends a message from the server to all the connected clients.
	 * @param msg the message, String.
	 */
	void sendMsgToAll(String msg) {
		playerList.forEach((p) -> p.sendMsg(msg));	
	}
	
	/**
	 * Sends a message from the server to all the connected clients, except this one.
	 * @param msg the message, String.
	 */
	void sendMsgToAllExceptThis(String msg) {
		playerList.forEach((p) -> {if(!p.equals(this)) p.sendMsg(msg);});	
	}
	
}