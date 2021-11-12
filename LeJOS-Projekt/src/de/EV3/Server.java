package de.EV3;

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
		
		public Server(int pPort)
		{
		port = pPort;
		try 
		{
			server = new ServerSocket(port);
			server.setSoTimeout(0);
		} 
		catch (Exception e) { e.printStackTrace();}
		
		}

		public void run() 
		{
			while(true) 
			{
				try 
				{
					CustomThread.printDisplay("Waiting for\n client....");
					Socket client = server.accept();
					input = new DataInputStream(client.getInputStream());
					CustomThread.printDisplay("Connected");
					while (!client.isClosed()) 
					{
						inputButton = input.readUTF();
					}
					CustomThread.printDisplay("Disconnected");
				} 
				catch (IOException e) {	CustomThread.printDisplay("Disconnected");}
			}
		}
}
