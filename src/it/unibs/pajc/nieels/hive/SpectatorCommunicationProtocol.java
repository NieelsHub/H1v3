package it.unibs.pajc.nieels.hive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class SpectatorCommunicationProtocol implements Runnable {
	
	private static ArrayList<SpectatorCommunicationProtocol> playerList = new ArrayList();

	private Socket clientSocket;
	private String clientName;
	private PrintWriter out;
	
	public SpectatorCommunicationProtocol(Socket clientSocket, String clientName) {
		this.clientSocket = clientSocket;
		this.clientName = clientName;
		playerList.add(this);
	}
	
	public void close() {
		if(out != null)
			out.close();
		
		playerList.remove(this);
		
		sendMsgToAll(this, "ha abbadonatao la conversazione");
	}
	
	public void run() {
		try(
				
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		) {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			System.out.printf("\nClient connesso: %s [%d] - Name: %s\n", 
					clientSocket.getInetAddress(), clientSocket.getPort(), clientName);
			
			sendMsg(this, "Buongiorno, il tuo nome è: " + clientName);
			
			// protocollo di comunicazione
			String request;
			while((request = in.readLine()) != null) {
				System.out.printf("\nRichiesta ricevuta: %s [%s]", request, clientName);
				
				if("quit".equals(request)) {
					System.out.printf("\nRichiesta di uscire dal servziio... [%s]\n", clientName);
					break;
				}
				
				String response = request; //request.toUpperCase();
				sendMsgToAll(this, response);
			}
			
			
		} catch(IOException ex) {
			
		} finally {
			this.close();
		}
		
		System.out.printf("\nSessione terminata, client: %s\n", clientName);
		
	}
	
	private void sendMsg(SpectatorCommunicationProtocol sender, String msg) {
		this.out.printf("[%s]: %s\n\r", sender.clientName, msg);
		this.out.flush();
	}
	
	
	private void sendMsgToAll(SpectatorCommunicationProtocol sender, String msg) {
		
		playerList.forEach((p) -> p.sendMsg(sender, msg));
		
	}
	

}