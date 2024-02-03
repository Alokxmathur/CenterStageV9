package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.game.Field;

import java.util.Locale;

/**
 * Created by Silver Titans on 10/12/17.
 */

public class DriveForDistanceOperation extends DriveTrainOperation {
    protected double distance;
    protected double speed;

    /**
     * Drive forward for the distance specified at the speed specfied
     * @param distance - distance to travel in mms
     * @param speed -1 to 1
     * @param title
     * Robot will move backwards if either speed or distance is negative, but not both
     */
    public DriveForDistanceOperation(double distance, double speed, String title) {
        super();
        this.distance = distance;
        this.speed = speed;
        this.title = title;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "Distance: %.2f(%.2f\")@%.2f --%s",
                this.distance, this.distance/ Field.MM_PER_INCH, this.speed,
                this.title);
    }

    public boolean isComplete() {
        if (driveTrain.driveTrainWithinRange()) {
            return true;
        }
        return false;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    public double getDistanceToTravel() {
        return this.distance;
    }

    @Override
    public void startOperation() {
        driveTrain.handleOperation(this);
    }
}

