package it.unibs.pajc.nieels.hive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NetworkServer {
	
	public static final int DEFAULT_PORT = 1234;
	public static final int DEFAULT_SPECTATORS_NUMBER = 10;
	
	public static final String ASK_CLIENT_TYPE = "DECLARE CLIENT TYPE";
	public static final String PLAYER_TYPE = "PLAYER";
	public static final String SPECTATOR_TYPE = "SPECTATOR";
	
	public static final String START_GAME = "START GAME";
	public static final String ASK_FOR_MOVE = "SEND MOVE";
	public static final String QUIT = "QUIT";
	public static final String CHAT = "CHAT";
	public static final String HIVE_UPDATE = "HIVE UPDATE";
	public static final String PASS = "PASS";
	public static final String VICTORY = "VICTORY";
	public static final String DEFEAT = "DEFEAT";
	public static final String DRAW = "DRAW";
	
	private int port;
	private int maxSpectatorsNumber;
	
	private ExecutorService playerExecutor;
	private ExecutorService spectatorExecutor;
	private ExecutorService gameExecutor;
	
	//private Hive hive = null;
	
	private ArrayList<CommunicationProtocol> players = new ArrayList();
	private ArrayList<CommunicationProtocol> spectators = new ArrayList();

	
	public NetworkServer() {
		this(DEFAULT_PORT, DEFAULT_SPECTATORS_NUMBER);
	}
	
	public NetworkServer(int port) {
		this(port, DEFAULT_SPECTATORS_NUMBER);
	}
	
	public NetworkServer(int port, int spectatorsNumber) {
		this.port = port;
		this.maxSpectatorsNumber = spectatorsNumber;
	}
	
	
	public void start(Hive hive) {
		
		System.out.println("SERVER - Server opening...");
		
		try(//RESOURCES
			ServerSocket server = new ServerSocket(port); //The IP is all the machine's possible IPs, the port is the specified one. This is the server's socket.
		){
			System.out.printf("\nSERVER - Server info - IP: %s [port: %d]\n", server.getInetAddress(), server.getLocalPort());
			
			players.clear();
			spectators.clear();
			
			playerExecutor = Executors.newFixedThreadPool(2);
			spectatorExecutor = Executors.newFixedThreadPool(this.maxSpectatorsNumber);
			gameExecutor = Executors.newSingleThreadExecutor();
			
			while(players.size() < 2 || spectators.size() < maxSpectatorsNumber) { //Keep waiting for new clients to connect until the capacity is full
				try {
					server.setSoTimeout(100);
					Socket client = server.accept(); //the method server.accept() listens for a connection to be made to the server socket and accepts it (the method blocks until a connection is made) then creates a new Socket relative to the client who made the connection and returns it. 
					
					//Client dump information
					System.out.printf("\nSERVER - Connected client - IP: %s [port: %d]\n", client.getInetAddress(), client.getPort());
					
					//OUT stream (we want a textual one in this case because we want to create a text based application on the server, so we use PrintWriter)
					PrintWriter out = new PrintWriter(client.getOutputStream(), true); //The local client socket provides a stream for the output of data to the client, we give it to the print writer.
																						//True is the activation of the auto flush for the output buffer, this always sends the data to the client without risking that they might be kept in the local buffer without being sent yet.
					
					//IN stream
					BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream())); //The client.getInputStream() provides a low level input stream that we wrap in higher level objects which are more complex streams that are more abstract and easier to use with formatting
					
					
					//COMMUNICATION PROTOCOL
					
					out.println(ASK_CLIENT_TYPE); //Sends the client a message that will be recognized as a request to define if they want to join as player or spectator
					
					String reply = in.readLine(); //Waits for the client's reply and saves it
					
					System.out.printf("\nSERVER - Type got: [%s]\n", reply);
						
					if (PLAYER_TYPE.equalsIgnoreCase(reply) && players.size() < 2) {
						//We start a player communication protocol thread to create a communication channel between the server this client socket, in and out, using streams (buffered reader, writer, etc).
						CommunicationProtocol playerProtocol = new CommunicationProtocol(client, "PLAYER#" + (players.size()+1));
						players.add(playerProtocol);
						playerExecutor.submit(playerProtocol);
						
						//START GAME
						if (players.size() >= 2) {
							System.out.printf("\nSERVER - Starting game...");
							gameExecutor.submit(() -> startGame(hive));
						}
					}
					
					if (SPECTATOR_TYPE.equalsIgnoreCase(reply) && spectators.size() < maxSpectatorsNumber) {
//						SpectatorCommunicationProtocol spectatorClientProtocol = new SpectatorCommunicationProtocol(client, "SPECTATOR#" + spectatorsNumber);
//						spectatorExecutor.submit(spectatorClientProtocol);
					}
					
				} catch (SocketTimeoutException e) {
					//e.printStackTrace(); It's empty because it's only used to handle the interrupt generated by the socket timeout.
					
				} catch (Exception e) {
					e.printStackTrace();
					
				} finally {
					if (Thread.interrupted()) {
				        //The task has been interrupted: https://docs.oracle.com/javase/tutorial/essential/concurrency/interrupt.html
						Thread.currentThread().interrupt(); //Resetting the consumed interrupted flag: https://stackoverflow.com/questions/60905869/understanding-thread-interruption-in-java?noredirect=1&lq=1
						close();
//						server.close();
						players.forEach((p) -> p.close());
						System.out.println("SERVER - Thread " + Thread.currentThread().getName() + " interrupted");
						break;
				    }
				}
				
			}
				
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void close() {

		try {
			System.out.println("SERVER - Attempting to shutdown spectator executor...");
			spectatorExecutor.shutdown(); //Stops accepting new tasks and shuts down the executor, trying to correctly complete all previously submitted tasks ("clean" shutdown). This method does not wait for previously submitted tasks (but not started executing) to complete execution.
			spectatorExecutor.awaitTermination(5, TimeUnit.SECONDS); //Tells this thread to wait for all the executor's tasks to complete execution before going on.
															//Blocks until all tasks have completed execution after a shutdown request, or the timeout occurs, or the current thread is interrupted, whichever happens first.
		} catch(InterruptedException e) {
			System.err.println("SERVER - Tasks interrupted.");
		} finally {
			if (!spectatorExecutor.isTerminated()) { //If there are still active tasks (timeout went out without them being able to complete)...
				System.err.println("SERVER - Cancelling non-finished tasks...");
				spectatorExecutor.shutdownNow(); //Forces the executor's termination, terminating all tasks linked to it. Attempts to stop all actively executing tasks, halts the processing of waiting tasks.
			}
			System.out.println("SERVER - Spectators shutdown completed.");
		}
		
		try {
			System.out.println("SERVER - Attempting to shutdown player executor...");
			playerExecutor.shutdown(); //Stops accepting new tasks and shuts down the executor, trying to correctly complete all previously submitted tasks ("clean" shutdown). This method does not wait for previously submitted tasks (but not started executing) to complete execution.
			playerExecutor.awaitTermination(5, TimeUnit.SECONDS); //Tells this thread to wait for all the executor's tasks to complete execution before going on.
															//Blocks until all tasks have completed execution after a shutdown request, or the timeout occurs, or the current thread is interrupted, whichever happens first.
		} catch(InterruptedException e) {
			System.err.println("SERVER - Tasks interrupted.");
		} finally {
			if (!playerExecutor.isTerminated()) { //If there are still active tasks (timeout went out without them being able to complete)...
				System.err.println("SERVER - Cancelling non-finished tasks...");
				playerExecutor.shutdownNow(); //Forces the executor's termination, terminating all tasks linked to it. Attempts to stop all actively executing tasks, halts the processing of waiting tasks.
			}
			System.out.println("SERVER - Players shutdown completed.");
		}
		
		try {
			System.out.println("SERVER - Attempting to shutdown game executor...");
			gameExecutor.shutdown(); //Stops accepting new tasks and shuts down the executor, trying to correctly complete all previously submitted tasks ("clean" shutdown). This method does not wait for previously submitted tasks (but not started executing) to complete execution.
			gameExecutor.awaitTermination(5, TimeUnit.SECONDS); //Tells this thread to wait for all the executor's tasks to complete execution before going on.
															//Blocks until all tasks have completed execution after a shutdown request, or the timeout occurs, or the current thread is interrupted, whichever happens first.
		} catch(InterruptedException e) {
			System.err.println("SERVER - Tasks interrupted.");
		} finally {
			if (!gameExecutor.isTerminated()) { //If there are still active tasks (timeout went out without them being able to complete)...
				System.err.println("SERVER - Cancelling non-finished tasks...");
				gameExecutor.shutdownNow(); //Forces the executor's termination, terminating all tasks linked to it. Attempts to stop all actively executing tasks, halts the processing of waiting tasks.
			}
			System.out.println("SERVER - Game shutdown completed.");
		}
		
		System.out.println(String.format("SERVER - Server on port %d closed.\n", port));	
	}
	
	private void startGame(Hive hive) {
//		boolean victory = false;
		
		System.out.println(hive);
		
		players.forEach((p) -> p.sendMsg(HIVE_UPDATE + Base64SerializationUtility.serializeObjectToString(hive)));
		
		players.forEach((p) -> p.sendMsg(START_GAME));
		
//		while(!victory) {
//			//.....
//		}
	}

}
