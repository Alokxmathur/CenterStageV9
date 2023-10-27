package org.firstinspires.ftc.teamcode.robot.components.vision.detector;



import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.*;

public class DetectorPipeline extends OpenCvPipeline {

    public static final double CAMERA_OFFSET_FRONT = 11.25;
    public static final int FOCAL_LENGTH = 1500;

    public static final int MINIMUM_AREA = 100;

    boolean showMean;


    private final Object synchronizer = new Object();

    Field.SpikePosition lastSpikePosition = Field.SpikePosition.NotSeen;

    /*
     * Red, Blue and Green color constants
     */
    final Scalar RED = new Scalar(0, 0, 255);
    final Scalar GREEN = new Scalar(0, 255, 0);
    final Scalar SILVER = new Scalar(192, 192, 192);
    final Scalar BLACK = new Scalar(0, 0, 0);
    boolean setupCrossHair;

    ObjectDetector objectDetector = new ObjectDetector(
            0, RobotConfig.X_PIXEL_COUNT, 0, RobotConfig.Y_PIXEL_COUNT, MINIMUM_AREA);
    public Mat processFrame(Mat inputImageRGB) {
        synchronized (synchronizer) {
            if (!setupCrossHair) {
                objectDetector.setupCrossHair(inputImageRGB);
                setupCrossHair = true;
            }
            //let our object detector detect objects
            Map<ObjectDetector.ObjectType, DetectableObject> detectedObjects = objectDetector.process(inputImageRGB);

            //Draw a marker at the cross hair point
            Imgproc.drawMarker(inputImageRGB, objectDetector.getCrossHairPoint(), RED, Imgproc.MARKER_CROSS, 100);

            //now go through each object to paint contours etc.
            for (ObjectDetector.ObjectType objectType: detectedObjects.keySet()) {
                DetectableObject detectableObject = detectedObjects.get(objectType);
                if (!detectableObject.isDisabled()) {
                    if (detectableObject.getType() == ObjectDetector.ObjectType.CrossHair) {
                        Imgproc.drawContours(inputImageRGB, detectableObject.getFoundObjects(), -1, GREEN, 5);
                    }
                    else {
                        Imgproc.drawContours(inputImageRGB, detectableObject.getFoundObjects(), -1, SILVER, 5);
                    }
                    MatOfPoint largestContour = detectableObject.getLargestObject();
                    if (largestContour != null) {
                        List<MatOfPoint> largestContours = new ArrayList<>();
                        largestContours.add(detectableObject.getLargestObject());
                        if (detectableObject.getType() == ObjectDetector.ObjectType.CrossHair) {
                            Imgproc.drawContours(inputImageRGB, largestContours, -1, GREEN, 2);
                        } else {
                            Imgproc.drawContours(inputImageRGB, largestContours, -1, GREEN, 2);
                        }
                        RotatedRect rotatedRectangle = detectableObject.getRotatedRectangleOfLargestObject();
                        if (rotatedRectangle != null) {
                            Point[] vertices = new Point[4];
                            rotatedRectangle.points(vertices);
                            MatOfPoint points = new MatOfPoint(vertices);
                            Imgproc.drawContours(inputImageRGB, Collections.singletonList(points), -1, RED, 2);
                        }


                        Point point = new Point(detectableObject.getXPositionOfLargestObject() / 50 * 50, detectableObject.getYPositionOfLargestObject() / 50 * 50);
                        /*double distance = objectDetector.getDistanceFromCameraOfLargestObject(objectType);
                        Imgproc.putText(inputImageRGB,
                                String.format(Locale.getDefault(), "%s[%.0f]",
                                        objectType,
                                        detectableObject.getMeanOfLargestObject()[0]),
                                point,
                                Imgproc.FONT_HERSHEY_SIMPLEX,
                                1,
                                BLACK,
                                3
                        );

                         */

                        Scalar mean = detectableObject.getMeanOfLargestObject();
                        Imgproc.putText(inputImageRGB,
                                String.format(Locale.getDefault(), "%s %.0f (%.0f,%.0f,%.0f)",
                                        detectableObject.getShortName(), objectDetector.getLargestArea(objectType),
                                        objectDetector.getLargestAreaMean(objectType).val[0],
                                        objectDetector.getLargestAreaMean(objectType).val[1],
                                        objectDetector.getLargestAreaMean(objectType).val[2]),
                                point,
                                Imgproc.FONT_HERSHEY_SIMPLEX,
                                2,
                                BLACK,
                                2
                        );
                    }
                }
            }
            //Draw a rectangle depicting our area of interest
            Rect areaOfInterest = objectDetector.getRectangleOfInterest();
            if (areaOfInterest != null) {
                Imgproc.rectangle(inputImageRGB, objectDetector.getRectangleOfInterest(), GREEN, 5);
                Imgproc.putText(inputImageRGB,
                        String.format(Locale.getDefault(), "Max X) {%d, Max y) {%d",
                                objectDetector.getRectangleOfInterest().width,
                                objectDetector.getRectangleOfInterest().height),
                        new Point(20, objectDetector.getRectangleOfInterest().height),
                        Imgproc.FONT_HERSHEY_SIMPLEX,
                        2,
                        BLACK,
                        2
                );
            }


            Thread.yield();

            return inputImageRGB;
        }
    }

    public void manageVisibility(Gamepad gamepad1, Gamepad gamepad2) {
        objectDetector.manageObjectDetection(gamepad1, gamepad2);
    }

    public String getStatus() {
        double[] crossHairHSV = objectDetector.getCrossHairHSV();
        Scalar crossHairColor = objectDetector.getCrossHairColor();
        return objectDetector.getStatus() + String.format(Locale.getDefault(), ", Cross hair HSV: %.0f,%.0f,%.0f, RGB: %.0f, %.0f, %.0f",
                crossHairHSV[0], crossHairHSV[1], crossHairHSV[2],
                crossHairColor.val[0], crossHairColor.val[1], crossHairColor.val[2]);
        //return objectDetector.getStatus();
    }

    public Field.SpikePosition getSpikePosition () {
        synchronized (synchronizer) {
                DetectableObject detectableObject = null;
                if (Match.getInstance().getAlliance() == Alliance.Color.RED) {
                    detectableObject = objectDetector.getDetectableObjects().get(ObjectDetector.ObjectType.RedProp);
                } else {
                    detectableObject = objectDetector.getDetectableObjects().get(ObjectDetector.ObjectType.BlueProp);
                }
                if (detectableObject == null) {
                    return Field.SpikePosition.NotSeen;
                } else {
                    if (detectableObject.getYPositionOfLargestObject() > 1600) {
                        lastSpikePosition = Field.SpikePosition.Left;
                        return Field.SpikePosition.Left;
                    } else if (detectableObject.getYPositionOfLargestObject() > 700) {
                        lastSpikePosition = Field.SpikePosition.Middle;
                        return Field.SpikePosition.Middle;
                    } else {
                        lastSpikePosition = Field.SpikePosition.Right;
                        return Field.SpikePosition.Right;
                    }
                }
            }
        }
}
