package org.usfirst.frc.team4151.robot;		//This should automatically be here, if not, you're not using the FIRST template for robot code

/**
 * Author = Brian Warner - WHS Programming Captain
 * Authorized Editor = Donny Stevenson - WHS Programming Captain (please notify me of any changes)
 * Editors Needing Supervision = Matthew McKenna and Victoria Nash - Programming Trainees
 * 
 * This is the 2015 FRC Season Robot Code for Team SCRAP 4151
 * If your name is not listed above, or you are one of the "Editors Needing Supervision",
 * please contact Brian Warner or Donny Stevenson with any requests and do NOT edit the code without both of their permissions.
 * 
 * Last edited by: Brian Warner
 * Date and time finished editing: March 27, 2015 at 5:52 PM
 * Changes made: made button-instantiated code to both lower the lift incrementally and hold the lift in place
*/

import edu.wpi.first.wpilibj.IterativeRobot;	//At the top, before the actual robot code, create all the imports you're going to use
import edu.wpi.first.wpilibj.Joystick;			//If you're unsure about what you are going to use, don't worry
import edu.wpi.first.wpilibj.RobotDrive;		//If you are missing an import, Eclipse will tell you,
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.Timer;				//And you can also just put these in at the top as you go
import edu.wpi.first.wpilibj.livewindow.LiveWindow;		//This is just for the test mode
import edu.wpi.first.wpilibj.Compressor;		//This is the air compressor for pneumatics
import edu.wpi.first.wpilibj.DoubleSolenoid;	//This is the Double Solenoid for pneumatics
import edu.wpi.first.wpilibj.Talon;			//This is the motor controller for the lift mechanism, a Talon SR
import edu.wpi.first.wpilibj.Victor;		//This is another motor controller for the lift mechanism, a Victor 888
import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {		//First, sort of "half-declare" your objects' names at the top of the Iterative Class
	RobotDrive myRobot;		//This is the drive object, it will handle standard driving in auto and teleop
	Joystick driveStick;			//This is the drive joystick, has 11 buttons for other functions
	Talon arm;
	Joystick armStick;
	//Joystick liftStick;				//This is the joystick for the lift mechanism, same type of Joystick as driveStick
	//Compressor compressor;	//This is for pneumatics, and only needs to be declared so that it activates when the robot is enabled
	//DoubleSolenoid piston;	//This is our double solenoid, it will control the pneumatics functions just like the motor controllers do with the drive
	//Talon lift3;				//This is the motor controller specifically used for the lift mechanism, NOT driving. A Talon SR
	//Victor lift2;				//Yet another motor Controller for the lift, this time a Victor 888
	PowerDistributionPanel power;
	Victor armWheels;
	int autoLoopCounter;
	Timer time;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {		//fully declare all objects here (Timer is a method, not an object)
    	myRobot = new RobotDrive(0, 1);		//The values in RobotDrive() are the roboRIO ports where the PWM's for the drive are plugged in, ports #0 and #1
    	driveStick = new Joystick(0);			//The value in Joystick() is the order in which the Joysticks are plugged in. It can be checked on the USB section of the Drive Station
    	arm = new Talon(2);
    	armStick = new Joystick(1);
    	//liftStick = new Joystick(1);			//First in = 0, second in = 1
    	//compressor = new Compressor(1);		//Again, only declare this and nothing more. The value '1' should work fine.
    	//piston = new DoubleSolenoid(0, 1);	//The values here are the ports on the PCM where the Solenoid is plugged in, ports #0 and #1
    	//lift2 = new Victor(2);				//Port where the PWM is on the roboRIO, port #2
    	//lift3 = new Talon(3);				//This value is, again, where the PWM is plugged in on the roboRIO, port #3
    	myRobot.setInvertedMotor(MotorType.kRearLeft, true);	//This code inverts the motors so that forward/backward, lef/right are consistent with the joystick input. 
    	myRobot.setInvertedMotor(MotorType.kRearRight, true);	//You only need this if you find that your robot is 'in reverse'.
    	myRobot.setMaxOutput(1.0);
    	power = new PowerDistributionPanel();
    	power.clearStickyFaults();
    	//compressor.clearAllPCMStickyFaults();
    	armWheels = new Victor(3);
    	time = new Timer();
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {		//This is the base code for autonomous, and so far should only establish autoLoopCounter for the timing
    	autoLoopCounter = 0;			//this will ensure that this value always start at 0, it will increase once with each reading of the loop,
    									//and can be used like Timer.delay() to make the duration of a command
    	time.reset();
    	time.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {		// full speed is about 12'1" feet per second; 50 Loops = one second; Autonomous lasts 15 seconds
    					// half speed is about 6'5" per second
//*** Risky Auto ***
    /**	
    	if(autoLoopCounter < 150){
    		myRobot.drive(-0.50, 0.0);
    		autoLoopCounter ++;
    	}
    	else if(autoLoopCounter < 200){
    		myRobot.drive(0.15, -1.0);
    		autoLoopCounter ++;
    	}
    	else if(autoLoopCounter < 350){
    		myRobot.drive(-0.40, 0.0);
    		autoLoopCounter ++;
    	}
    	else if(autoLoopCounter < 425){
    		myRobot.drive(0.0, 0.0);
    		arm.set(0.50);
    		autoLoopCounter ++;
    	}
    	else if(autoLoopCounter < 475){
    		myRobot.drive(-0.25, 0.0);
    		arm.set(0.0);
    		armWheels.set(-1.0);
    		autoLoopCounter ++;
    	}
    	else {
    		myRobot.drive(0.0, 0.0);
    		arm.set(0.0);
    		armWheels.set(0.0);
    	}
**/
    	
    	//*** Right Angle Approach***
    	/*
    	if(autoLoopCounter < 210){
    		myRobot.drive(-0.5, 0.0);
    		autoLoopCounter ++;
    	}
    	else if(autoLoopCounter < 235){
    		myRobot.drive(-0.5, -	1.0);
    		autoLoopCounter ++;
    	}
    	else if(autoLoopCounter < 300){
    		myRobot.drive(-0.5, 0.0);
    		autoLoopCounter ++;
        } 
    	else if(autoLoopCounter < 350){
    		myRobot.drive(0.0, 0.0);
    		arm.set(0.50);
    		autoLoopCounter ++;
    	}
    	else if(autoLoopCounter < 400){
    		myRobot.drive(-0.25, 0.0);
    		arm.set(0.0);
    		armWheels.set(-1.0);
    		autoLoopCounter ++;
    	}
    	
    	else { 	
    		myRobot.drive(0.0, 0.0);
    		arm.set(0.0);
    		armWheels.set(0.0);
    	}
    	*/
    	
    	/** Driveway Test*/
   
    if(time.get()<2.0)
    {
    	myRobot.drive(-0.5, 0.0);
    }
    else
    	if(time.get()<3.13)
    	{
    		myRobot.arcadeDrive(-0.0, -0.5);
    	}
    	else
    		if(time.get()<5.13)
    	    {
    	    	myRobot.drive(-0.5, 0.0);
    	    }
    	    else
    	    	if(time.get()<6.26)
    	    	{
    	    		myRobot.arcadeDrive(-0.0, -0.5);
    	    	}
    
    
    	/**
    	//****Approach test ***
    	if(autoLoopCounter < 25){
    		myRobot.drive(-0.5, 1.0);
    		autoLoopCounter ++;
    	}
    	else if(autoLoopCounter < 90){
    		myRobot.drive(-0.5, 0.0);
    		autoLoopCounter ++;
        } 
    	else if(autoLoopCounter < 140){
    		myRobot.drive(0.0, 0.0);
    		arm.set(0.50);
    		autoLoopCounter ++;
    	}
    	else if(autoLoopCounter < 190){
    		myRobot.drive(-0.25, 0.0);
    		arm.set(0.0);
    		armWheels.set(-1.0);
    		autoLoopCounter ++;
    	}
    	
    	else { 	
    		myRobot.drive(0.0, 0.0);
    		arm.set(0.0);
    		armWheels.set(0.0);
    	}
    	**/
    	//** Shoot Test ***
    	/**
    	if(autoLoopCounter < 50){
    		myRobot.drive(-0.25, 0.0);
    		arm.set(0.0);
    		armWheels.set(-1.0);
    		autoLoopCounter ++;
    	}
    	else {
    		armWheels.set(0.0);
    		arm.set(0.0);
    		myRobot.drive(0.0, 0.0);
    	}
    	**/
    }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    
    double lastInput;				//Just a variable for use with the lift
    
    public void teleopInit(){
    	lastInput = 0.0;			//Just starting that variable off at 0.0 every time teleop is started
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {	//This will control Operator period, with code for the standard drive, the lift, and pneumatics 
    	myRobot.setSafetyEnabled(true);
    	while(true && isOperatorControl() && isEnabled()){		// need 3 values: true && isOperatorControl() && isEnabled()
        
    	//***DRIVE TELEOP***
    	//myRobot.setLeftRightMotorOutputs( ((driveStick.getY() + driveStick.getX()) * 0.7), ((driveStick.getY() - driveStick.getX()) * 0.7) );				// Driving with one joystick's input, handles everything about standard drive in teleop
        //Timer.delay(0.01);		// obligatory break in reading input, should be small like 0.01 s
        myRobot.arcadeDrive(driveStick);
        arm.set(armStick.getY());
        if(armStick.getRawButton(1)){
        	armWheels.set(-1.0);
        }
        else if(armStick.getRawButton(2)){
        	armWheels.set(1.0);
        }
        else if(armStick.getRawButton(3)){
        	armWheels.set(-0.3);
        }
        else if(armStick.getRawButton(4)){
        	armWheels.set(0.3);
        }
        else {
        	armWheels.set(0.0);
        }
    		
        //***LIFT TELEOP***
        //lift2.set( Math.abs(liftStick.getY()) * 0.5);		//This sets one-half the Joystick's Y-value as the speed for the lift motor (may need to scale values if it goes to fast)
        //lift3.set( Math.abs(liftStick.getY()) * 0.5);		//Same as ^
        //Timer.delay(0.01);		//More 'just in case' timer delays
        
       /** if(Math.abs(liftStick.getY()) >= lastInput){		//"if the absolute value of the joystick input is greater than or equal to the variable -
        	lastInput = Math.abs( liftStick.getY() );		// - then set the variable to the absolute value of the joystick input"
        }		//This will make sure the 'lastInput' variable is the highest recent value of the Joystick, so that if you let go of the joystick
        		//it won't lower the value of 'lastInput', but if you push it farther it will raise the value.
        
        if(liftStick.getRawButton(3)){						//Button #3 on the liftStick
        	lift2.set(lastInput);							//Set the lift motors to the 'lastInput' value
        	lift3.set(lastInput);							// ^
        } else if(liftStick.getRawButton(4) && lastInput > 0.0) {		//Button #4 on the liftStick, and only if lastInput isn't zero
        	lastInput = lastInput - 0.01;								//Take away 1% speed every 0.02 seconds until 'lastInput' is zero (takes 2 seconds)
        	lift2.set(lastInput);										//Set the lift motors to the new 'lastInput' value each time
        	lift3.set(lastInput);										// ^
        } else if(liftStick.getRawButton(4)){				//Button #4 on the liftStick
        	lastInput = 0.0;											// If button #4 is pressed and "lastInput" is zero, it stays zero
        }
        if(liftStick.getRawButton(5)){						//Button #5
        	lastInput = 0.0;											//If button #5 is pressed at any time, 'lastInput' will be set to zero
        }
        **/
        //***PNEUMATICS TELEOP***
        
       /* if(driveStick.getRawButton(1)){	//This will control the opening and closing of the fork for the lift
        	piston.set(DoubleSolenoid.Value.kForward);	// closes fork
        } else if(driveStick.getRawButton(2)){
        	piston.set(DoubleSolenoid.Value.kReverse);	// opens fork
        } else{
        	piston.set(DoubleSolenoid.Value.kOff);	// keeps fork in current position
        }
        */
        
        
    }
    	
}
    
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() { //Just don't worry about this, he'll do his own thing, don't change him
    	LiveWindow.run();
    }
    
}

/**	NOTES **
 * 
 * AUTONOMOUS:
 * The two values in the drive() function are as follows,
 *  -the first value is forward or backward speed (right now, negative is forward and positive is backward)
 *  -the second value is how 'perfect' your turn will be, the "curve value". Put simply, a +/- 1.0 will give you as close
 *  to a perfect turn as you'll get, around the center of the robot. Any less will increase the radius of the turn
 *  and make it a 'wider' turn. For most of the autonomous plans we come up with, we will only use perfect turns at multiples of 90 degrees.
 *  If you, for some reason, need a wider turn, you will have to test out the different values as they are pretty contingent upon the size of
 *  the robot, among other factors.
 * 
 * Any other functions need to be called upon directly, rather than by their buttons as in teleoperated mode.
 *  Consider this like talking directly with the robot: rather than controlling it with puppet strings like in teleop,
 *  you have to actually tell it what you want it to do, step by step.
 *  
 * The timing in Iterative works by repeatedly reading code:
 * 	First: Set a variable autoLoopCounter = 0 in autonomousInit
 * 	Second: Every time you make a command, put it in an if-else statement.
 * 			Make the 'if' dependent upon autoLoopCounter being below some number.
 * 			It runs about 50 loops every second, so if you want a command to be for one second,
 * 			you would make it:
 * 						if(autoLoopCounter < 50)
 * 			For subsequent commands, retain the previous number of loops like so:
 * 						if(autoLoopCounter < 50)
 * 						if(autoLoopCounter < 100)
 * 			: this would run one command for one second, and a subsequent command for another second.
 * Third: At the end of every command, make autoLoopCounter increase by 1 like so:
 * 						if(autoLoopCounter < 50){
 * 							robot.drive(-1.0, 0.0);
 * 							autoLoopCounter ++;
 * }
 * 			: this will ensure that the loop progresses.
 * 
 * Extra: If you want to run two different commands at the same time, like drive and lift, use the same loop count:
 * 						if(autoLoopCounter < 50){
 * 							robot.drive(-1.0, 0.0);
 * 							talon.set(-1.0);
 * 							autoLoopCounter ++;
 * }
 * 			: This will both move the robot forward and activate the lift motor at full speed for one second
 * 
 * More Extra: If you want to run two commands simultaneously, but one to continue afterward, just write a new command for the remaining time:
 * 						if(autoLoopCounter < 50){
 * 							robot.drive(-1.0, 0.0);
 * 							talon.set(-1.0);
 * 							autoLoopCounter ++;
 * 						} else if(autoLoopCounter < 100){
 * 							robot.drive(-1.0, 0.0);
 * 							talon.set(0.0);
 * 							autoLoopCounter ++;
 * }
 * 			: This will make the robot continue to drive for one second after the first command
 * 
 * And Finally: When You are done with all your commands, make your final 'else' statement like so:
 * 						if(autoLoopCounter < 50){
 * 							robot.drive(-1.0, 0.0);
 * 							talon.set(-1.0);
 * 							autoLoopCounter ++;
 * 						} else if(autoLoopCounter < 100){
 * 							robot.drive(-1.0, 0.0);
 * 							talon.set(0.0);
 * 							autoLoopCounter ++;
 * 						} else {
 * 							robot.drive(0.0, 0.0);
 * 							talon.set(0.0);
 * 						}
 * 			: Be sure to stop all motors, pneumatics, etc. that you started during autonomous. If you leave anything un-stopped, it will continue
 * 			  to run indefinitely and I will be sad.
 *  
 *TELEOP/OPERATOR CONTROL:
 * The gist of this is to create a translation between your physical input and the commands the robot receives.
 * Use joystick input as well as buttons and things like that in if-else statements and link those to commands.
 * 
 * Everything with the joystick will be a method attached to that object, or that object as the input for a method:
 * 	"stick.getRawButton()" will take an input of the button number, and will tie that button to the command in the if-else statement
 * 	"myRobot.arcadeDrive(stick)" will take the joystick's input for arcade drive with your RobotDrive object
 * 
 * For a motor controller like a Talon, just set speed values with this command:
 * 	"nameoftalon.set(speed);"
 * and you can use Timer.delay or a loop like in autonomous to create the duration, or write the code so that if the button
 * isn't being pressed the motor won't turn, so you manually control the time.
 * 
 * If you want to use the input of an axis from a joystick, the idea is the same,
 * the joysticks return values between -1 and 1, and the motor controllers take input from -1 to 1,
 * so all you have to do is set the motor controller's input as the joystick axis's output like so:
 * 		nameofmotorcontroller.set(nameofjoystick.getY());
 * 		or
 * 		nameofmotorcontroller.set(nameofjoystick.getX());
 * 
 * If you need to dampen the motion of your drive - that is, scale down the overall speeds so that it isn't so sensitive and crazy - the use the following code:
 * 
 * 		 myRobot.setLeftRightMotorOutputs( ((driveStick.getY() + driveStick.getX()) * 0.7), ((driveStick.getY() - driveStick.getX()) * 0.7) );				// Driving with one joystick's input, handles everything about standard drive in teleop
 *       Timer.delay(0.01);
 *       
 *       This will set the left/right motors on your drive train to the Y-value +/- the X-value of your joystick.
 *       Y should always be positive, and the X should be + or - depending of the side of the robot, in order to turn correctly (always test this!).
 *       To see how this works, just look at what some values from a joystick would do. If I were to push the joystick forwards or backwards,
 *       both motors would move at the same speed in the same direction. If I move the joystick to the left or right, each motor would move at the 
 *       same speed, but in the opposite direction. If I were to put the joystick in some position in between those two, the speeds and directions
 *       would result in an appropriate turn. This is how "arcadeDrive" works, but this way has the advantage of allowing you to directly
 *       manipulate the numerical speed inputs from the joystick. Simply multiply the sum of the axes by your desired percentage (70% above, be sure
 *       to use the same multiplier for each side) and the robot will move at that percentage of the speed input from the joystick. For instance,
 *       were I to push the joystick all the way forward, the robot would only move at 70% speed, were I to push is 10% forward, it would only move
 *       at 7% speed. This method of teleop does not change the speed percentages for autonomous. Putting in a speed value of 1.0 in auto will still
 *       return 100% speed; this only affects your teleop input system.
 * 
 * For a DoubleSolenoid, attach it to a button and use the following commands:
 * 	- to extend - "nameofsolenoid.set(DoubleSolenoid.Value.kForward);"
 * 	- to retract - "nameofsolenoid.set(DoubleSolenoid.Value.kReverse);"
 *	- to keep its current state - "nameofsolenoid.set(DoubleSolenoid.Value.kOff);"
 *(note: everything in the parentheses in the solenoid code should appear as it does here, things like "Value" should actually be "Value")
 *
 *To hold the speed on a motor, like for holding a lift up on our 2015 robot, first create a double variable and start it at zero in teleopInit:
 *				double lastInput;
 *				lastInput = 0.0;
 *Next, create a loop to set 'lastInput' to the value of a joystick, as long as that value isn't less than 'lastInput' (this will make it so the speed
 * doesn't lower if you let go of the stick):
 * 				if( Math.abs( liftStick.getY() ) >= lastInput){
 * 					lastInput = Math.abs( liftStick.getY() );
 * 				}
 *This will make it so that the absolute value ('Math.abs()') of the joystick value will be the 'lastInput' value only if it is greater than the current
 *'lastInput value'. We are using an absolute value so we can subtract speed effectively.
 *Set the speed to 'lastInput' only if a certain button is pressed, so you can still lower the lift manually if you want to:
 *				if( liftStick.getRawButton(3) ){
 *					lift1.set(lastInput);
 *					lift2.set(lastInput);
 *				}
 *This just sets the lift motors to 'lastInput' if button #3 is pressed.
 *After that, attach an 'else if' statement to do the lowering:
 *				else if( liftStick.getRawButton(4) && lastInput >= 0.0 ){
 *					lastInput = lastInput - 0.01;
 *					lift1.set(lastInput);
 *					lift2.set(lastInput);
 *				} else if( liftStick.getRawButton(4) ){
 *					lastInput = 0.0;
 *				}
 *				if( liftStick.getRawButton(5) ){
 *					lastInput = 0.0;
 *				}
 *The first part, in the 'if' parentheses, sets the condition that button #4 is pressed AND 'lastInput' is not zero. Then, it will subtract 0.01
 *from the value of 'lastInput' (0.01 is 1% of full speed). Then, it sets each lift motor to that new value of 'lastInput'. Since the Iterative Template
 *will cycle through a loop 50 times per second, and each time it subtracts 0.01 from 'lastInput', the longest it could take to reach zero (starting at
 *1.0) would be 2 seconds. The last 'else if' condition is when button #4 is pressed and 'lastInput' is zero (or is negative for some reason); it will
 *set 'lastInput' to 0.0, which will reset the 'lastInput' value so that it can climb to a value lower than the first one it is set to. So, if you are
 *done holding the lift, and want to do it again at a lower height, you need only to let go of the joystick, press button #4, and the value will be set
 *to 0.0 again so you can start over. If that doesn't work, just press button #5 (as per the last 'if' statement) at any time and 'lastInput' will be
 *set to 0.0.
 *
 * TROUBLESHOOTING:
 * 	Be careful with forums and the like, because different teams use different setups.
 * 	Your best sources for internet research are:
 * 		The FRC website
 * 		The FRC subreddit (either looking for your problem or making a new post)
 * 		Any pdf's of FRC specific manuals and the like
 * 		Me! Contact me whenever through a Facebook chat or my phone/email
 *			(I might not be able to answer immediately, but I will always help eventually)
 *			My phone number is 973-216-5081 and you're better off with a text at first
 *			I'll stay in the FB programming group for as long as I'm needed, I don't mind being there when I'm in college
 *			My email is brian.warner67@yahoo.com, put in the subject line " WHS ROBOTICS" or something like that and I'll definitely read it
 *			If I can't help you, I'll find someone or something that can. Good luck!
 * 
 */

                                  
                                                                       
                               