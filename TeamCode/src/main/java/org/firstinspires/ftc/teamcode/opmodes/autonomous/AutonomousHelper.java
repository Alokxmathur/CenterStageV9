package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.components.vision.detector.ObjectDetector;
import org.firstinspires.ftc.teamcode.robot.operations.State;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class implements the methods to make autonomous happen
 */
public abstract class AutonomousHelper extends OpMode {
    protected Match match;
    protected Robot robot;
    protected Field field;

    ArrayList<State> states = new ArrayList<>();

    Date initStartTime;

    boolean statesAdded = false;
    //start with assuming that there might be an error when initializing the robot
    boolean initErrorHappened = true;
    String initError = "";

    /*
     * Code to run ONCE when the driver hits INIT
     */
    public void init(Telemetry telemetry, Alliance.Color alliance, Field.StartingPosition startingPosition) {
        try {
            initStartTime = new Date();
            statesAdded = false;

            this.match = Match.getNewInstance();
            match.init();
            Match.log("Match initialized, setting alliance to " + alliance
                    + " and starting position to " + startingPosition);
            match.setAlliance(alliance);
            match.setStartingPosition(startingPosition);
            field = match.getField();

            //initialize field for the alliance and starting position
            field.init(alliance, startingPosition);
            //get our robot and initialize it
            this.robot = match.getRobot();
            Match.log("Initializing robot");
            this.robot.init(hardwareMap, telemetry, match);

            telemetry.update();
            initErrorHappened = false;
        }
        catch (Throwable e) {
            RobotLog.logStackTrace(e);
            initError = e.toString();
        }
    }
    /*
     * Code to run repeatedly after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        if (match.getAlliance() != Alliance.Color.NotSelected) {
            if (Field.isNotInitialized()) {
                telemetry.addData("State", "Trajectories initializing, please wait. " +
                        (30 - (int) (new Date().getTime() - initStartTime.getTime()) / 1000));
                telemetry.update();
            } else if (!robot.fullyInitialized()) {
                robot.resetArm();
                robot.getVisionPortal().setExposureAndGain();
                robot.getVisionPortal().enableObjectDetection(
                    match.getAlliance() == Alliance.Color.RED
                            ? ObjectDetector.ObjectType.RedProp
                            : ObjectDetector.ObjectType.BlueProp);
                telemetry.addData("State", "Robot initializing, please wait.");
            }
            else {
                robot.handleGameControllers(gamepad1, gamepad2);
                match.setSpikePosition(robot.getSpikePosition());
                match.updateTelemetry(telemetry, "Ready");
            }
        }
        Thread.yield();
    }

    @Override
    public void start() {
        match.setStart();
        robot.getDriveTrain().resetYaw();
        robot.getVisionPortal().disableObjectDetection();
    }

    /**
     * We go through our specified desired states in this method.
     * Loop through the states, checking if a state is reached, if it is not reached, queue
     *         it if not already queued
     */
    @Override
    public void loop() {
        /*
        Check states sequentially. Skip over reached states and queue those that have not
        been reached and not yet queued
         */
        for (State state : states) {
            if (!state.isReached(robot)) {
                if (state.isQueued()) {
                    match.updateTelemetry(telemetry,"Attempting " + state.getTitle());
                } else {
                    //queue state if it has not been queued
                    match.updateTelemetry(telemetry,"Queueing " + state.getTitle());
                    Match.log("Queueing state: " + state.getTitle());
                    state.queue(robot);
                }
                break;
            }
        }
        //Match.log("Finished reaching final state of states size = "  + states.size());
    }

    @Override
    public void stop() {
        this.robot.stop();
    }
}