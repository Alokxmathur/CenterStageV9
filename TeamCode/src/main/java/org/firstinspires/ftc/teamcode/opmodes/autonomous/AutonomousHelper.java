package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.Robot;
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

    boolean processedAllState = false;

    ArrayList<State> states = new ArrayList<>();

    Date initStartTime;

    boolean cameraPoseSet = false;
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
            Match.log("Robot initialized");
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
        //robot.handleGameControllers(gamepad1, gamepad2);
        if (initErrorHappened) {
            telemetry.addData("State", "Error: " + initError);
        }
        else if (Field.isNotInitialized()) {
            telemetry.addData("State", "Trajectories initializing, please wait. " +
                    (30 - (int)(new Date().getTime() - initStartTime.getTime())/1000));
            telemetry.addData("Position", robot.getPose());
        }
        else if (robot.fullyInitialized()) {
            Field.SpikePosition spikePosition = robot.getSpikePosition();
            match.setSpikePosition(spikePosition);
            match.updateTelemetry(telemetry, "Ready");
        }
        else {
            telemetry.addData("Status", "Cameras initializing, please wait");
        }
        telemetry.update();
        Thread.yield();
    }

    @Override
    public void start() {
        match.setStart();
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