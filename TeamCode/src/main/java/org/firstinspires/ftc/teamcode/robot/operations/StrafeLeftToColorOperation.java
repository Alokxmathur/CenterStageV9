package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;

import java.util.Locale;

/**
 * Created by Silver Titans on 10/12/17.
 */

public class StrafeLeftToColorOperation extends StrafeRightToColorOperation {

    /**
     * Strafe to the specified type of color
     * @param type: Red, Blue or Green
     * @param speed
     * @param title
     */
    public StrafeLeftToColorOperation(Type type, double speed, String title) {
        super(type, speed, title);
    }

    public String toString() {
        return String.format(Locale.getDefault(), "StrafeLeftToColor: %s@%.2f --%s",
                this.type, this.robotRelativeHeading,
                this.title);
    }

    @Override
    public void startOperation() {
        this.driveTrain.drive(Math.atan2(-speed, 0), Math.hypot(speed, 0), 0);
    }
}

