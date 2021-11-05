package de.server;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import de.KaitoLib.Utils.*;

public class CustomThread extends Thread
{
	// Klasse einer eigenen Bibliothek zum Umgang mit Arrays
	ArrayUtils arrUt = new ArrayUtils();
	
	int counter = 1, angle;
	String button;
    EV3LargeRegulatedMotor motorA = new EV3LargeRegulatedMotor(MotorPort.A);
    EV3LargeRegulatedMotor motorB = new EV3LargeRegulatedMotor(MotorPort.B);    
    boolean recording = false;
    int recVal = 0;
    String[] recArr = new String[10];
    long[] recTime = new long[10];
    int[] recAngle = new int[10];
    // Gyro Sensor + Messung initialisieren
    EV3GyroSensor gyro = new EV3GyroSensor(SensorPort.S1);
    SampleProvider gyroProvider = gyro.getAngleAndRateMode();
    float[] gyroSample = new float[gyroProvider.sampleSize()];
    float[] tempSample = new float[gyroProvider.sampleSize()];
    
    // Methode zur Rotation in entgegengesetzte Richtung zum übergebenen Motor
    public void rotate(RegulatedMotor motor, boolean back, int angle) 
    {
    	// Start des übergebenen Motors
    	motor.setSpeed(300);
    	if (!back) { motor.forward(); }
    	else { motor.backward(); }
        
        // Gyro Messung vor der Drehung
    	gyroProvider.fetchSample(gyroSample, 0);
    	gyroProvider.fetchSample(tempSample, 0);
    	// Wiederholung bis der Winkel des EV3 den Wert von "angle" von der Messung zuvor entfernt ist
    	while ((tempSample[0] - gyroSample[0])*(tempSample[0] - gyroSample[0]) < (angle-3)*(angle-3)) 
    	{
    		gyroProvider.fetchSample(tempSample, 0); 	
    	}
        motor.stop(false);
    }
    
    
    // Methode zum ausführen von Bewegungen
    // move: String = auszuführende Aktion als Text
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
        		motorA.setSpeed(300);
                motorB.setSpeed(300);
                motorA.backward();
                motorB.backward();
        		break;
        	}
            motorA.setSpeed(300);
            motorB.setSpeed(300);
            motorA.forward();
            motorB.forward();
            break;
        // rückwärts fahren
        case "DOWN":
        	if (back) 
        	{
                motorA.setSpeed(300);
                motorB.setSpeed(300);
                motorA.forward();
                motorB.forward();
        		break;
        	}
            motorA.setSpeed(300);
            motorB.setSpeed(300);
            motorA.backward();
            motorB.backward();
            break;
        default:
            break;
        }
    }
    
    // Methode zum abspielen des aufgenommenen Arrays (movement pattern)
    // backwards: boolean = Soll das pattern erneut abgespielt oder zurück gefahren werden?
    private void play(boolean backwards) 
    {
		int c;
		if (backwards) { c = recArr.length-1; }
		else { c = 0; }
		    
		try {
		    while(c >= 0 && c < recArr.length) 
		    {
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

    // Main des Threads
    public void run()
    {
        while(true) 
        {
    		// Entspricht der Input des Servers dem zuvoriegen, überspirnge
    		if (Integer.parseInt(Server.inputButton.split(";")[1]) <= counter) continue;
        	// Kann aus dem Server des EV3 ein neuer Input erkannt werden, wird dieser auf die Variable "button" übertragen
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
                recArr = new String[20];
                recTime = new long[20];
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
            			arrUt.addElementToArray(recArr, button);
            			arrUt.addElementToArray(recAngle, angle);
            			arrUt.addElementToArray(recTime, System.currentTimeMillis());
            		}
            		else 
            		{
            			recArr[recVal] = button;
            			recAngle[recVal] = angle;
                        recTime[recVal] = System.currentTimeMillis();
            		}
            		recVal++;
                    System.out.println(button + " recorded");
    			}
    			// Bewegung ausführen
        		movement(button, false, angle);
        	}
        }
   }
}
