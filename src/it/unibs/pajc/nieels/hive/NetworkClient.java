package it.unibs.pajc.nieels.hive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NetworkClient {
	public static final String DEFAULT_SERVER = "127.0.0.1";
	public static final int DEFAULT_PORT = 1234;
	
	String serverHost;
	int port;
	
	String clientType;
	
	public NetworkClient() {
		serverHost = DEFAULT_SERVER;
		port = DEFAULT_PORT;
	}
	
	public NetworkClient(String serverHost, int port) {
		this.serverHost = serverHost;
		this.port = port;
	}
	
	public void startSpectator() {
		
		try(
				Socket client = new Socket(serverHost, port);
		) {
			
			
			this.clientType = NetworkServer.SPECTATOR_TYPE;
			
			
			
			
			ExecutorService ex = Executors.newFixedThreadPool(2);
			
			System.out.println("Client connected to server: " +
					client.getRemoteSocketAddress() + ":" +
					client.getPort());
			
			ex.submit(() -> clientToServer(client));
			ex.submit(() -> serverToClient(client));
			ex.shutdown();
			ex.awaitTermination(1, TimeUnit.DAYS);
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		

	}
	
	protected void clientToServer(Socket client) {
		
		try(
				PrintWriter out = new PrintWriter(client.getOutputStream());
		) {
			BufferedReader stdin = new BufferedReader(new InputStreamReader(
					System.in));	
			String request;
			while((request = stdin.readLine()) != null) {
				//System.out.printf("{c2S -> %s}\n", request);
				out.println(request);
				out.flush(); 
			}
			
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	
	}
	
	protected void serverToClient(Socket client) {
		
		try(
				BufferedReader in = new BufferedReader(new InputStreamReader(
						client.getInputStream()));
				
				
				
				
				PrintWriter out = new PrintWriter(client.getOutputStream());
				
				
				
				
		) {
			String response;	
			while((response = in.readLine()) != null) {
				
				
				
				if (response == NetworkServer.ASK_CLIENT_TYPE) {
					out.println(this.clientType);
					out.flush(); 
				}
				
				
				
				
				System.out.printf("\n[%s]\n>", response);
			}
			
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}

}
