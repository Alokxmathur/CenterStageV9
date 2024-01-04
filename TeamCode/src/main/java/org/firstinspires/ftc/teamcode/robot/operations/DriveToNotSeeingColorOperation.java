package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.game.Match;

import java.util.Locale;

/**
 * Created by Silver Titans on 10/12/17.
 */

public class DriveToNotSeeingColorOperation extends DriveToColorOperation {

    /**
     * Drive for the specified time
     * @param type
     * @param heading - the heading relative to the robot in radians
     *                This is not the field heading
     * @param speed
     * @param title
     */
    public DriveToNotSeeingColorOperation(Type type, double heading, double speed, String title) {
        super(type, heading, speed, title);
    }

    public String toString() {
        return String.format(Locale.getDefault(), "DriveToNotColor: %s@%.2f --%s",
                this.type, this.robotRelativeHeading,
                this.title);
    }

    public boolean isComplete() {
        boolean seeingColor = false;
        switch (type) {
            case RED: {
                seeingColor = Match.getInstance().getRobot().getColors().red >= .5;
                break;
            }
            case BLUE: {
                seeingColor = Match.getInstance().getRobot().getColors().blue >= .5;
                break;
            }
            case GREEN: {
                seeingColor = Match.getInstance().getRobot().getColors().green >= .5;
                break;
            }
        }
        if (!seeingColor) {
            driveTrain.stop();
            return true;
        } else {
            driveTrain.drive(this.robotRelativeHeading, this.getSpeed(), 0);
            return false;
        }
    }
}

