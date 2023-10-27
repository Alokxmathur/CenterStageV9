package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.components.drivetrain.DriveTrain;
import org.firstinspires.ftc.teamcode.robot.components.vision.detector.ObjectDetectorWebcam;
import org.firstinspires.ftc.teamcode.robot.components.vision.detector.ObjectDetector;

import java.util.Locale;

import static org.firstinspires.ftc.teamcode.opmodes.drivercontrolled.ObjectAligner.CENTER;
import static org.firstinspires.ftc.teamcode.opmodes.drivercontrolled.ObjectAligner.COEFFECIENT;
import static org.firstinspires.ftc.teamcode.opmodes.drivercontrolled.ObjectAligner.MARGIN;

/**
 * Created by Silver Titans on 10/12/17.
 */

public class AlignToObjectOperation extends BearingOperation {
    public static final double MIN_SPEED = 0.2;

    protected ObjectDetector.ObjectType objectType;

    ObjectDetectorWebcam webcam;
    boolean doNotEvenStart = false;

    public AlignToObjectOperation(ObjectDetector.ObjectType objectType, DriveTrain driveTrain, String title) {
        super(0, driveTrain, title);
        this.title = title;
        this.objectType = objectType;
        this.webcam = Match.getInstance().getRobot().getWebcam();
    }

    public String toString() {
        return String.format(Locale.getDefault(),"AlignToObject: %s --%s",
                this.objectType, this.title);
    }

    @Override
    public void startOperation() {
        if (objectType != null) {
            if (webcam.seeingObject(objectType)) {
                double position = webcam.getYPositionOfLargestObject(objectType);
                if (position >= 0) {
                    double error = (position - CENTER);
                    double correction = error * COEFFECIENT;
                    if (Math.abs(error) > MARGIN) {
                        this.desiredBearing = Math.toDegrees(Match.getInstance().getRobot().getCurrentTheta()) - correction;
                        super.startOperation();
                    } else {
                        Match.log(("Already aligned with " + objectType));
                        doNotEvenStart = true;
                    }
                }
            } else {
                Match.log(("Not aligning as not seeing " + objectType));
                doNotEvenStart = true;
            }
        }
        else {
            Match.log("Not aligning with null object");
            doNotEvenStart = true;
        }

    }
    @Override public boolean isComplete() {
        return doNotEvenStart || super.isComplete();
    }
}
