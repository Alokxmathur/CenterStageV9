package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.game.Match;

import java.util.Locale;

/**
 * Created by Silver Titans on 10/12/17.
 */

public class StrafeRightToNotColorOperation extends StrafeRightToColorOperation {

    /**
     * Strafe to the specified type of color
     * @param type: Red, Blue or Green
     * @param speed
     * @param title
     */
    public StrafeRightToNotColorOperation(Type type, double speed, String title) {
        super(type, speed, title);
    }

    public String toString() {
        return String.format(Locale.getDefault(), "StrafeToNotColor: %s@%.2f --%s",
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
            return false;
        }
    }

}

