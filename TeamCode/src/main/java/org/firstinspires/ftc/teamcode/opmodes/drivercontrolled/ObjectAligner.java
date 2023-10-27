package org.firstinspires.ftc.teamcode.opmodes.drivercontrolled;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.components.vision.detector.ObjectDetectorWebcam;
import org.firstinspires.ftc.teamcode.robot.components.vision.detector.ObjectDetector;
import org.firstinspires.ftc.teamcode.robot.operations.AlignToObjectOperation;
import org.firstinspires.ftc.teamcode.robot.operations.DriveToObject;

@TeleOp(name = "Baby: Object Aligner", group = "Baby")

public class ObjectAligner extends ObjectFinderTeleOp {
    public static final int CENTER = RobotConfig.Y_PIXEL_COUNT / 2;
    public static final int MARGIN = 60;
    public static double COEFFECIENT = .02;
    ObjectDetectorWebcam webcam;
    @Override
    public void start() {
        super.startStreaming();
        webcam = robot.getWebcam();
    }
    @Override
    public void loop() {
        super.loop();
        if (gamepad1.left_trigger > 0.2 && robot.allOperationsCompleted()) {
            ObjectDetector.ObjectType objectType = null;
            if (gamepad1.b) {
                if (match.getAlliance() == Alliance.Color.RED) {
                    objectType = ObjectDetector.ObjectType.RedProp;
                }
                else {
                    objectType = ObjectDetector.ObjectType.BlueProp;
                }
            }
            if (objectType != null) {
                Match.log("Aligning with " + objectType);
                robot.queueSecondaryOperation(new AlignToObjectOperation(objectType, robot.getDriveTrain(),"Align with " + objectType));
            }
        }
        if (gamepad1.right_trigger > 0.2 && robot.allOperationsCompleted()) {
            ObjectDetector.ObjectType objectType = null;
            if (gamepad1.b) {
                if (match.getAlliance() == Alliance.Color.RED) {
                    objectType = ObjectDetector.ObjectType.RedProp;
                }
            }
            if (objectType != null) {
                robot.queueSecondaryOperation(new DriveToObject(objectType, 0.4, "Drive to " + objectType));
            }
        }
    }
}
