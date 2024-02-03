package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;

import java.util.Date;
import java.util.Locale;

/**
 * Created by Silver Titans on 10/12/17.
 */

public class DriveToColorOperation extends DriveTrainOperation {

    public enum Type {
        RED, BLUE, GREEN
    };

    protected Type type;
    protected double robotRelativeHeading;
    protected double heading, speed;
    String title;

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Type getType() {
        return type;
    }

    /**
     * Drive for the specified time
     * @param type
     * @param heading - the heading relative to the robot in radians
     *                This is not the field heading
     * @param speed
     * @param title
     */
    public DriveToColorOperation(Type type, double heading, double speed, String title) {
        super();
        this.type = type;
        this.heading = heading;
        this.speed = speed;
        this.title = title;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "DriveToColor: %s@%.2f, speed:%.2f --%s",
                this.type, Math.toDegrees(this.heading), this.speed,
                this.title);
    }

    public boolean isComplete() {
        boolean seeingColor = false;
        switch (type) {
            case RED: {
                seeingColor = Match.getInstance().getRobot().getColors().red >= .47;
                break;
            }
            case BLUE: {
                seeingColor = Match.getInstance().getRobot().getColors().blue >= .47;
                break;
            }
            case GREEN: {
                seeingColor = Match.getInstance().getRobot().getColors().green >= .47;
                break;
            }
        }
        if (seeingColor) {
            driveTrain.stop();
            return true;
        }
        else {
            DriveInDirectionOperation.driveInDirection(Math.signum(speed), heading, speed, driveTrain);
            return false;
        }
    }

    public double getSpeed() {
        return this.speed;
    }

    @Override
    public void startOperation() {
    }

}

