package de.server;


public class MainTest {

	public static void main(String[] args) {
		
		CustomThread t1 = new CustomThread();
		t1.start();
		
		Server s = new Server(1415);
		s.run();
	}

}
