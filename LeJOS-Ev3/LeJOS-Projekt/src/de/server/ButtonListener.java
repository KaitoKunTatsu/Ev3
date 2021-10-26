package de.server;

public class ButtonListener {

	private Server server;

	static boolean ENTER;
	static boolean UP;
	static boolean DOWN;
	static boolean RIGHT;
	static boolean LEFT;
	static boolean ESC;
	static boolean RECORD;
	static boolean P1;
	static boolean P2;

	public ButtonListener(Server pS) {
	server = pS;
	}

	public static int getMoveButtons() 
	{
		if (RIGHT) return 4; 
		if (LEFT) return 3;
		if (DOWN) return 1;
		if (UP) return 2;
		return -1;
	}
	
	private static void reset() 
	{
		ENTER = false;
		UP = false;
		DOWN = false;
		RIGHT = false;
		LEFT = false;
		P1 = false;
		P2 = false;
		ESC = false;
	}
	
	public static void update() 
	{
		reset();
	}
}
