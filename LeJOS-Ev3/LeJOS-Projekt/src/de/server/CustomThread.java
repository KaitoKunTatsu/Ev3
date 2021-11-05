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
	
	int counter = 1, angel;
	String button;
    EV3LargeRegulatedMotor motorA = new EV3LargeRegulatedMotor(MotorPort.A);
    EV3LargeRegulatedMotor motorB = new EV3LargeRegulatedMotor(MotorPort.B);    
    boolean recording = false;
    int recVal = 0;
    boolean playing = false;
    String[] recArr = new String[20];
    long[] recTime = new long[20];
    
    // Gyro Sensor + Messung initialisieren
    EV3GyroSensor gyro = new EV3GyroSensor(SensorPort.S1);
    SampleProvider gyroProvider = gyro.getAngleAndRateMode();
    float[] gyroSample = new float[gyroProvider.sampleSize()];
    float[] tempSample = new float[gyroProvider.sampleSize()];
    
    // Methode zur Rotation in entgegengesetzte Richtung zum übergebenen Motor
    public void rotate(RegulatedMotor motor, boolean back) 
    {
    	// Start des übergebenen Motors
    	motor.setSpeed(300);
    	if (!back) { motor.forward(); }
    	else { motor.backward(); }
        
        // Gyro Messung vor der Drehung
    	gyroProvider.fetchSample(gyroSample, 0);
    	gyroProvider.fetchSample(tempSample, 0);
    	// Wiederholung bis "angel" 90° von der Messung zuvor entfernt ist
    	while ((tempSample[0] - gyroSample[0])*(tempSample[0] - gyroSample[0]) < (angel)*(angel)) 
    	{
    		gyroProvider.fetchSample(tempSample, 0); 	
    	}
    	// Abbremsen und stoppen 
    	if (!back) {motor.backward();}
    	else {motor.forward();}
    	Delay.msDelay(80);
        motor.stop(false);
    }
    
    
    // Methode zum ausführen von Bewegungen
    // move: String = auszuführende Aktion als Text
    private void movement(String move, boolean back) 
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
            rotate(motorA, back);
            break;
        // Drehung nach rechts
        case "RIGHT":
            motorA.stop(false);
            rotate(motorB, back);
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
    	playing = true;
    	// wenn die Strecke erneut gefahren werden soll
	    if (!backwards) 
	    {
			try 
	    	{
				// Iteration des aufgenommenen Arrays 
	    		for (int i = 0; i < recArr.length; i++) 
		        {
		            if (recArr[i] == "" || recArr[i] == null) break;
		            System.out.println("playin... " + recArr[i]);
		            movement(recArr[i], false);
		            if (recArr[i] == "UP" || recArr[i] == "DOWN") { Delay.msDelay(recTime[i+1]-recTime[i]); }
		        }
		    } catch (Exception e) {System.out.println("Error");}
	    }
    	// wenn die Strecke zurück bis zum Anfangspunkt gefahren werden soll
	    else 
	    {
	    	try 
	    	{
				// Iteration des aufgenommenen Arrays
	    		for (int i = recArr.length-1; i >= 0; i--) 
		        {
		            if (recArr[i] == "" || recArr[i] == null) continue;
		            System.out.println("playin... ");
		            if (recArr[i] == "DOWN" || recArr[i] == "UP") 
		            {
		            	movement(recArr[i], true);
		            	Delay.msDelay(recTime[i+1]-recTime[i]);
		            	movement("ENTER", true);
		            	continue;
		            }
		            movement(recArr[i], true);
		        }
		    } catch (Exception e) {System.out.println("Error");}
	    }
		System.out.println("FINISHED");
        // Variablen auf den Stand vor der Aufnahme zurücksetzen 
		recording = false;
        recVal = 0;
        playing = false;
    }

    // Main des Threads
    public void run()
    {
        while(true) 
        {
    		// Entspricht der Input des Servers dem zuvoriegen, wird var "button" geleert 
    		if (Integer.parseInt(Server.inputButton.split(";")[1]) <= counter) 
        	{
        		button = ""; 
        		continue;
        	}
        	// Kann aus dem Server des EV3 ein neuer Input erkannt werden, wird dieser auf var "button" übertragen
        	else 
        	{
        		angel = Integer.parseInt(Server.inputButton.split(";")[2]);
        		button = Server.inputButton.split(";")[0];
        		counter++;
        	}
        	System.out.println(Server.inputButton);
            
        	// Reaktion auf den übergebenen Button
    		switch(button) 
        	{
    		// Ist button leer, beende switch
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
            			recArr = arrUt.addElementToArray(recArr, button);
            			recTime = arrUt.addElementToArray(recTime, System.currentTimeMillis());
            		}
            		else 
            		{
            			recArr[recVal] = button;
                        recTime[recVal] = System.currentTimeMillis();
            		}
            		recVal++;
                    System.out.println(button + " recorded");
    			}
    			// Bewegung ausführen
        		movement(button, false);    
        	}
        }
   }
}
