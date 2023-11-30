package org.firstinspires.ftc.teamcode.robot.components.vision;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.components.vision.detector.ObjectDetectionVisionProcessor;
import org.firstinspires.ftc.teamcode.robot.components.vision.detector.ObjectDetector;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class SilverTitansVisionPortal {
    org.firstinspires.ftc.vision.VisionPortal visionPortal;
    AprilTagProcessor aprilTagProcessor;
    ObjectDetectionVisionProcessor objectDetectionVisionProcessor;
    public void init(HardwareMap hardwareMap) {
        this.aprilTagProcessor = new AprilTagProcessor.Builder().setDrawCubeProjection(true).build();
        this.objectDetectionVisionProcessor = new ObjectDetectionVisionProcessor();
        visionPortal = new org.firstinspires.ftc.vision.VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, RobotConfig.WEBCAM_ID))
                .addProcessors(objectDetectionVisionProcessor, aprilTagProcessor)
                .setCameraResolution(new android.util.Size(RobotConfig.X_PIXEL_COUNT, RobotConfig.Y_PIXEL_COUNT))
                .build();
    }
    public Field.SpikePosition getSpikePosition() {
        return objectDetectionVisionProcessor.getSpikePosition();
    }
    public String getStatus() {
        return objectDetectionVisionProcessor.getStatus();
    }
    /**
     * Add telemetry about AprilTag detections.
     */
    private void telemetryAprilTag(Telemetry telemetry) {
        List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }   // end for() loop

    }   // end method telemetryAprilTag()
    public void decrementMinX() {
        objectDetectionVisionProcessor.decrementMinX();
    }
    public void incrementMinX() {
        objectDetectionVisionProcessor.incrementMinX();
    }
    public void decrementMaxX() {
        objectDetectionVisionProcessor.decrementMaxX();
    }
    public void incrementMaxX() {
        objectDetectionVisionProcessor.incrementMaxX();
    }
    public void decrementMinY() {
        objectDetectionVisionProcessor.decrementMinY();
    }
    public void incrementMinY() {
        objectDetectionVisionProcessor.incrementMinY();
    }
    public void decrementMaxY() {
        objectDetectionVisionProcessor.decrementMaxY();
    }
    public void incrementMaxY() {
        objectDetectionVisionProcessor.incrementMaxY();
    }


    public void manageObjectVisibility(Gamepad gamepad1, Gamepad gamepad2) {
        this.objectDetectionVisionProcessor.manageVisibility(gamepad1, gamepad2);
    }

    public double getXPositionOfLargestObject(ObjectDetector.ObjectType objectType) {
        return objectDetectionVisionProcessor.getXPositionOfLargestObject(objectType);
    }
    public double getYPositionOfLargestObject(ObjectDetector.ObjectType objectType) {
        return objectDetectionVisionProcessor.getYPositionOfLargestObject(objectType);
    }

    public double getWidthOfLargestObject(ObjectDetector.ObjectType objectType) {
        return objectDetectionVisionProcessor.getWidthOfLargestObject(objectType);
    }

    public double getHeightOfLargestObject(ObjectDetector.ObjectType objectType) {
        return objectDetectionVisionProcessor.getHeightOfLargestObject(objectType);
    }

    public boolean seeingObject(ObjectDetector.ObjectType objectName) {
        return getLargestArea(objectName) > 0;
    }

    public double getLargestArea(ObjectDetector.ObjectType objectName) {
        return objectDetectionVisionProcessor.getLargestArea(objectName);
    }

    public double getDistanceToLargestObject(ObjectDetector.ObjectType objectType) {
        return objectDetectionVisionProcessor.getDistanceToLargestObject(objectType);
    }

    public List<AprilTagDetection>  getAprilTags() {
        return this.aprilTagProcessor.getDetections();
    }
}
