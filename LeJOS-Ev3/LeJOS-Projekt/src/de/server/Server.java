package de.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static boolean read = false;
	
	private ServerSocket server;
	private int port;
	public static String inputButton = "0;0;0";
	DataInputStream input;
	Socket client;
	
	 // Erstellen eines Servers auf dem EV3, um Inputs des interface zu erhalten
	public Server(int pPort)
	{
	port = pPort;
	try 
	{
		server = new ServerSocket(port);
		server.setSoTimeout(200000);
	} 
	catch (Exception e) { e.printStackTrace();}
	
	}

	public void run() 
	{
		while(true) 
		{
			try 
			{
				if (client == null) 
				{
					client = server.accept();
					input = new DataInputStream(client.getInputStream());
					System.out.println("Connected!");
				}
				inputButton = input.readUTF();
				
			} 
			catch (IOException e) {e.printStackTrace();}
		}
	}
}
