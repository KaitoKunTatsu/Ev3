package de.server;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import de.KaitoLib.Utils.*;

public class CustomThread extends Thread
{
	// Klasse einer eigenen Bibliothek zum Umgang mit Arrays
	ArrayUtils arrUt = new ArrayUtils();
	
	EV3LargeRegulatedMotor motorA = new EV3LargeRegulatedMotor(MotorPort.A);
    EV3LargeRegulatedMotor motorB = new EV3LargeRegulatedMotor(MotorPort.B);    
    
    EV3TouchSensor ts = new EV3TouchSensor(SensorPort.S2);
    SampleProvider tsProvider = ts.getTouchMode();
    float[] tsSample = new float[1];
    
    EV3GyroSensor gyro = new EV3GyroSensor(SensorPort.S1);
    SampleProvider gyroProvider = gyro.getAngleAndRateMode();
    float[] gyroSample = new float[gyroProvider.sampleSize()];
    float[] tempSample = new float[gyroProvider.sampleSize()];
    
	int counter = 1, angle, recVal;
	String button;
	boolean recording = false;
    String[] recArr = new String[10];
    long[] recTime = new long[10];
    int[] recAngle = new int[10];
    
    /**
     * 	Methode zur Rotation des EV3
     * 
     * 	@param angle	Rotationswinkel für Drehungen
     * 	@param back		Wird auf diese Methode im Zuge des rückwärts-Abspielens zugegriffen
     *	@param motor	Der Motor, der der Blickrichtung des EV3 nach der Drehung entgegen liegt und zur Rotation bewegt wird
     * */
    public void rotate(RegulatedMotor motor, boolean back, int angle) 
    {
    	// Start des übergebenen Motors
    	motor.setSpeed(340);
    	if (!back) { motor.forward(); }
    	else { motor.backward(); }
        
        // Gyro Messung vor der Drehung
    	gyroProvider.fetchSample(gyroSample, 0);
    	gyroProvider.fetchSample(tempSample, 0);
    	// Wiederholung bis der Winkel des EV3 den Wert von "angle" von der Messung zuvor entfernt ist
    	while ((tempSample[0] - gyroSample[0])*(tempSample[0] - gyroSample[0]) < (angle)*(angle)) 
    	{
        	tsProvider.fetchSample(tsSample, 0);
        	if (tsSample[0] == 1) 
        	{
        		movement("DOWN", false, angle);
        		Delay.msDelay(250);
        		movement("ENTER", false, angle);
	        	return;
	        }
    		gyroProvider.fetchSample(tempSample, 0); 	
    	}
        motor.stop(false);
    }
    
    public static void printDisplay(String str) 
    {
    	System.out.println(str);
    }

    /**
     * 	Diese Methode regelt reine Bewegung des Roboters in Abhängigkeit der Inputs.
     *
     * 	- 	geradeaus fahren
     * 	- 	rückwärts fahren
     * 	- 	links Drehung
     * 	- 	rechts Drehung
     * 	- 	anhalten
     * 
     * 	@param back		Wird auf diese Methode im Zuge des rückwärts-Abspielens zugegriffen
     * 	@param move		Befehl für die auszuführende Bewegung in Textform
     * 	@param angle	Rotationswinkel für Drehungen
     * */
    private void movement(String move, boolean back, int angle) 
    {
        switch(move) 
        {
        // anhalten
        case "ENTER":
        	motorA.stop(true);
        	motorB.stop(false);
        	break;
        // Drehung nach links
        case "LEFT":
            motorB.stop(false);
            rotate(motorA, back, angle);
            break;
        // Drehung nach rechts
        case "RIGHT":
            motorA.stop(false);
            rotate(motorB, back, angle);
            break;
        // geradeaus fahren
        case "UP":
        	if (back) 
        	{
        		movement("DOWN",false,angle);
        		return;
        	}
            motorA.setSpeed(340);
            motorB.setSpeed(340);
            motorA.forward();
            motorB.forward();
            break;
        // rückwärts fahren
        case "DOWN":
        	if (back) 
        	{
                movement("UP", false, angle);
                return;
        	}
            motorA.setSpeed(340);
            motorB.setSpeed(340);
            motorA.backward();
            motorB.backward();
            break;
        default:
            break;
        }
    }

    /**
     * 	Der Methodenaufruf spielt das aufgenommene Array ab, respektive bewegt den EV3 erneut wie zwischen Start und Ende der Aufnahme.
     * 	Ist backwards true, wird zum Anfangspunkt zurück navigiert
     * 
     * 	@param backwards
     * */
    private void play(boolean backwards) 
    {
		int c;
		if (backwards) { c = recArr.length-1; }
		else { c = 0; }
		    
		try {
		    while(c >= 0 && c < recArr.length) 
		    {
		    	tsProvider.fetchSample(tsSample, 0);
		    	if (tsSample[0] == 1) 
	        	{
	        		movement("DOWN", false, angle);
	        		Delay.msDelay(250);
	        		break;
	        	}
	        	if (backwards) 
			    { 
			    	if (recArr[c] != null) movement(recArr[c], true, recAngle[c]); 
			    	if ("UP".equals(recArr[c]) || "DOWN".equals(recArr[c])) { Delay.msDelay(recTime[c+1]-recTime[c]); }
			    	c--;
			    }
			    else 
			    {
			    	if (recArr[c] != null) movement(recArr[c], false, recAngle[c]);
			    	if ("UP".equals(recArr[c]) || "DOWN".equals(recArr[c])) { Delay.msDelay(recTime[c+1]-recTime[c]); }
			    	c++;
			    }
		    	
		    }
    	} 
    	catch (Exception e) { System.out.println("Error"); }
	    movement("ENTER", false, 0);
		System.out.println("FINISHED");
		recording = false;
    }

    public void run()
    {
        while(true) 
        {
        	// Ist der TouchSensor ausgelöst, kurz zurück fahre, dann anhalten
        	tsProvider.fetchSample(tsSample, 0);
        	if (tsSample[0] == 1) 
        	{
        		movement("DOWN", false, angle);
        		Delay.msDelay(250);
        		movement("ENTER", false, angle);
        	}
    		// Entspricht der Input des Servers dem zuvoriegen, überspirnge
    		if (Integer.parseInt(Server.inputButton.split(";")[1]) <= counter) continue;
        	// Kann aus dem Server des EV3 ein neuer Input erkannt werden, wird der Befehl auf die Variable button übertragen und der Rotationswinkel auf angle
        	else 
        	{
        		angle = Integer.parseInt(Server.inputButton.split(";")[2]);
        		button = Server.inputButton.split(";")[0];
        		counter++;
        	}
        	System.out.println(Server.inputButton);
            
        	// Reaktion auf den übergebenen Button
    		switch(button) 
        	{
    		case "":
        		break;
        	// Aufnahme beenden
        	case "STOPREC":
        		recording = false;
        		break;
        	// Programm beenden
        	case "ESCAPE":
        		System.exit(0);
        		break;
        	// Aufnahme löschen
        	case "DELETE":
                recArr = new String[10];
                recTime = new long[10];
                recAngle = new int[10];
                recVal = 0;
                break;
            // Aufnahme starten
        	case "REC":
        		recording = true;
        		break;
        	// Aufnahme rückwärts abspielen
        	case "PLAY2":
        		play(true);
        		break;
        	// Aufnahme abspielen
        	case "PLAY1":
        		play(false);
        		break;
        	// Ist der Input eine Bewegung
        	default:
        		// Wenn die Bewegung aufgenommen werden soll
    			if (recording)
    			{	
            		if (recVal > recArr.length-1) 
            		{
            			arrUt.addSpaceToArray(recArr, 4);
            			arrUt.addSpaceToArray(recAngle, 4);
            			arrUt.addSpaceToArray(recTime, 4);
            		}
	        		recArr[recVal] = button;
	        		recAngle[recVal] = angle;
	                recTime[recVal] = System.currentTimeMillis();
	        		recVal++;
	                System.out.println(button + " recorded");
    			}
    			// Bewegung ausführen
        		movement(button, false, angle);
        	}
        }
   }
}
