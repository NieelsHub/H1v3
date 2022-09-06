package it.nieels.unibs.pajc.h1v3.controller.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class CommunicationProtocol implements Runnable {
	
	private static ArrayList<CommunicationProtocol> playerList = new ArrayList();

	private Socket clientSocket;
	private String clientName;
	private PrintWriter out;
	
	public CommunicationProtocol(Socket clientSocket, String clientName) {
		this.clientSocket = clientSocket;
		this.clientName = clientName;
		playerList.add(this);
	}
	
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
					sendMsgToAllExceptSender(response);
					sendMsgToAllExceptSender(NetworkServer.ASK_FOR_MOVE);
				}
				
				else if(request.startsWith(NetworkServer.PASS)) {
					sendMsgToAllExceptSender(NetworkServer.ASK_FOR_MOVE);
				}
				
				else if(request.startsWith(NetworkServer.VICTORY)) {
					String response = request;
					sendMsgToAllExceptSender(response);
				}
				
				else if(request.startsWith(NetworkServer.DEFEAT)) {
					String response = request;
					sendMsgToAllExceptSender(response);
				}
				
				else if(request.startsWith(NetworkServer.DRAW)) {
					String response = request;
					sendMsgToAllExceptSender(response);
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
	
	
	void sendMsg(String msg) {
		this.out.println(msg);
		this.out.flush();
	}
	
	void sendMsgToAll(String msg) {
		playerList.forEach((p) -> p.sendMsg(msg));	
	}
	
	void sendMsgToAllExceptSender(String msg) {
		playerList.forEach((p) -> {if(!p.equals(this)) p.sendMsg(msg);});	
	}
	
	/*
	private void sendMsg(PlayerCommunicationProtocol sender, String msg) {
		this.out.printf("[%s]: %s\n\r", sender.clientName, msg);
		this.out.flush();
	}
	
	
	private void sendMsgToAll(PlayerCommunicationProtocol sender, String msg) {
		playerList.forEach((p) -> p.sendMsg(sender, msg));
		
	}*/
	

}