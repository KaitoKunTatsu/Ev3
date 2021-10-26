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
	int counter = 1, angel;
	String button;
    EV3LargeRegulatedMotor motorA = new EV3LargeRegulatedMotor(MotorPort.A);
    EV3LargeRegulatedMotor motorB = new EV3LargeRegulatedMotor(MotorPort.B);    
    EV3GyroSensor gyro = new EV3GyroSensor(SensorPort.S1);
    boolean recording = false;
    int recVal = 0;
    boolean playing = false;
    String[] recArr = new String[20];
    Long[] recTime = new Long[20];
    
    SampleProvider gyroProvider = gyro.getAngleAndRateMode();
    float[] gyroSample = new float[gyroProvider.sampleSize()];
    float[] tempSample = new float[gyroProvider.sampleSize()];
    
    public void rotate(RegulatedMotor motor) 
    {
        motor.setSpeed(300);
        motor.forward();
    	gyroProvider.fetchSample(gyroSample, 0);
    	gyroProvider.fetchSample(tempSample, 0);
    	while ((tempSample[0] - gyroSample[0])*(tempSample[0] - gyroSample[0]) < angel-2*angel-2) 
    	{
    		gyroProvider.fetchSample(tempSample, 0); 	
    	}
        motor.stop(false);
    }
    
    private void movement(String move, boolean backwards) 
    {
        switch(move) 
        {
        case "ENTER":
        	motorA.stop(true);
        	motorB.stop(false);
        	break;
        case "LEFT":
            motorB.stop(false);
            rotate(motorA);
            break;
        case "RIGHT":
            motorA.stop(false);
            rotate(motorB);
            break;
        case "UP":
        	if (backwards) 
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
        case "DOWN":
        	if (backwards) 
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
    
    private void play(boolean backwards) 
    {
    	playing = true;
	    if (!backwards) 
	    {
			try 
	    	{
	    		for (int i = 0; i < recArr.length-1; i++) 
		        {
		            if (recArr[i] == "" || recArr[i] == null) break;
		            System.out.println("playin... " + recArr[i]);
		            movement(recArr[i], false);
		            Delay.msDelay(recTime[i+1]-recTime[i]);
		        }
		    } catch (Exception e) {}
	    }
	    else 
	    {
	    	try 
	    	{
	    		for (int i = recArr.length-1; i > 0; i--) 
		        {
		            if (recArr[i] == "" || recArr[i] == null) continue;
		            System.out.println("playin... ");
		            if (recArr[i] == "ENTER") 
		            {
		            	movement(recArr[i-1], true);
		            	Delay.msDelay(recTime[i]-recTime[i-1]);
		            	movement("ENTER", true);
		            	i--;
		            	continue;
		            }
		            movement(recArr[i], true);
		            Delay.msDelay(recTime[i]-recTime[i-1]);
		        }
		    } catch (Exception e) {}
	    }

		System.out.println("FINISHED");
        recording = false;
        recVal = 0;
        playing = false;
    }

    
    public void run()
    {
        while(true) 
        {
        	if (!playing) {
        		if (Integer.parseInt(Server.inputButton.split(";")[1]) <= counter) 
            	{
            		button = ""; 
            		continue;
            	}
            	else 
            	{
            		angel = Integer.parseInt(Server.inputButton.split(";")[2]);
            		button = Server.inputButton.split(";")[0];
            		counter++;
            	}
            	System.out.println(Server.inputButton);
                
        		switch(button) 
            	{
            	case "":
            		break;
            	case "STOPREC":
            		recording = false;
            		break;
            	case "ESCAPE":
            		System.exit(0);
            		break;
            	case "DELETE":
                    recArr = new String[20];
                    recTime = new Long[20];
                    break;
            	case "REC":
            		recording = true;
            		break;
            	case "PLAY2":
            		play(true);
            		break;
            	case "PLAY1":
            		play(false);
            		break;
            	default:
	    			if (recording)
	    			{	
	            		if (recVal > recArr.length-1) 
	            		{
	            			recArr = ArrayUtils.addSpaceToArray(recArr, 2);
	            			recTime = ArrayUtils.addSpaceToArray(recTime, 2);
	            		}
	                    recArr[recVal] = button;
	                    recTime[recVal] = System.currentTimeMillis();
	                    recVal++;
	                    System.out.println(button + " recorded");
            		}                
            		movement(button, false);    
            }
            
        }
    }
   }
}
