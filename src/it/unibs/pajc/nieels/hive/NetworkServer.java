package it.unibs.pajc.nieels.hive;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkServer {
	
	public static final int DEFAULT_PORT = 1234;
	int port;
	
	public NetworkServer() {
		port = DEFAULT_PORT;
	}
	
	public NetworkServer(int port) {
		this.port = port;
	}
	
	public void start() {
		
		System.out.println("Server opening...");
		
		try(//RESOURCES
			ServerSocket server = new ServerSocket(port); //The IP is all the machine's possible IPs, the port is the specified one. This is the server's socket.
		){
			System.out.printf("Server info - IP: %s [port: %d]\n", server.getInetAddress(), server.getLocalPort());
			int id = 0;
			while(true) {//Keep waiting for new clients to connect until the server is closed!
				Socket client = server.accept(); //the method server.accept() listens for a connection to be made to the server socket and accepts it (the method blocks until a connection is made) then creates a new Socket relative to the client who made the connection and returns it. 

				//We then use streams (buffered reader, writer, etc) in the communication protocol threads to create a communication channel between the server and each client socket, in and out.
				//FUNCTIONALITY AND COMMUNICATION PROTOCOLS
				//CommunicationProtocol clientProtocol = new CommunicationProtocol(client, "CLI#"+id++);
				PlayerCommunicationProtocol clientProtocol = new PlayerCommunicationProtocol(client, "CLI#"+id++);
				Thread clientThread = new Thread(clientProtocol); //We could also use executor services here
				clientThread.start();
				
			}
					
		} catch(IOException e){
			e.printStackTrace();
		}

		System.out.println("Server closed.");
	}

}
