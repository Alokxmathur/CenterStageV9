package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.components.drivetrain.DriveTrain;

import java.util.Locale;

/**
 * Created by Silver Titans on 10/12/17.
 */

public class BearingOperation extends DriveToPositionOperation {
    public static final double MIN_SPEED = 0.2;

    protected double desiredBearing;
    private DriveTrain driveTrain;

    /**
     * Create a bearing operation
     * @param desiredBearing - desired bearing (radians)
     * @param title
     */
    public BearingOperation(double desiredBearing, String title) {
        super(null, title);
        this.title = title;
        this.desiredBearing = desiredBearing;
        this.driveTrain = Match.getInstance().getRobot().getDriveTrain();
    }

    public String toString() {
        return String.format(Locale.getDefault(),"Bearing: %.2f --%s",
                Math.toDegrees(this.desiredBearing), this.title);
    }

    public double getDesiredBearing() {
        return desiredBearing;
    }

    @Override
    public void startOperation() {
        this.driveTrain.handleOperation(this);
    }

}
