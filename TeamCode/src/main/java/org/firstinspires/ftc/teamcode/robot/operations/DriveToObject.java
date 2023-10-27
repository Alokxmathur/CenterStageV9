package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.components.vision.detector.ObjectDetector;

import java.util.Locale;

/**
 * Created by Silver Titans on 10/12/17.
 */

public class DriveToObject extends DriveInDirectionOperation {
    Robot robot;
    ObjectDetector.ObjectType objectType;
    boolean doNotEvenStart = false;

    /**
     * Drive to the specified object type
     * @param objectType
     * @param speed
     * @param title
     */
    public DriveToObject(ObjectDetector.ObjectType objectType, double speed, String title) {
        super(0, 0, speed, title);
        this.objectType = objectType;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "MoveToObject: %s@%.2f speed --%s",
                this.objectType, this.speed,
                this.title);
    }

    @Override
    public void startOperation() {
        if (objectType != null) {
            if (robot.getWebcam().seeingObject(objectType)) {
                double distance = robot.getWebcam().getDistanceToLargestObject(objectType);
                robot.setState("Distance to " + objectType + " " + distance);
                if (distance >= 10) {
                    this.distance = -(distance-10)* Field.MM_PER_INCH;
                    this.direction = Math.toDegrees(robot.getCurrentTheta());
                    super.startOperation();
                    Match.log("Approaching " + objectType + ", it is " + distance + " inches away");
                }
            }
            else {
                Match.log("Can't approach " + objectType + " as it is not in view");
                doNotEvenStart = true;
            }

        }
        else {
            Match.log("Can't approach object of type null");
            doNotEvenStart = true;
        }
    }

    @Override
    public boolean isComplete() {
        return doNotEvenStart || super.isComplete();
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    public double getDistance() {
        return this.distance;
    }
}

