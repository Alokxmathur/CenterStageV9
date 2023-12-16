package org.firstinspires.ftc.teamcode.robot.components.vision.detector;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * A class to detect objects on the field
 * <p>
 * For this year the objects we try to detect are red and blue props, yellow, white, green and purple
 * pixels, blue and red tapes
 */
public class ObjectDetector {

    public static final double CAMERA_OFFSET_FRONT = 11.25;
    public static final int FOCAL_LENGTH = 1500;

    public static final int MINIMUM_AREA = 100;

    boolean gamePad1A, gamePad1B, gamePad1Y, gamePad1X, gamePad2A, gamePad2B, gamePad2Y, gamePad2X, gamePad1RightTriggerWasPressed;

    Field.SpikePosition lastSpikePosition = Field.SpikePosition.Left;

    /**
     * Manage Object detection based on game pad buttons
     * <p>
     * GamePad 1 a - toggle Green Pixel Detection
     * GamePad 1 b - toggle Red Prop Detection
     * GamePad 1 y - toggle Yellow Pixel Detection
     * GamePad 1 x - Blue Prop Detection
     * <p>
     * GamePad 2 a - toggle Object Detection at cross hair
     * GamePad 2 b - toggle Red Tape Detection
     * GamePad 2 y - toggle White Pixel Detection
     * GamePad 2 x - toggle Blue Tape Detection
     */
    public void manageObjectDetection(Gamepad gamePad1, Gamepad gamePad2) {
        //enable objects based on the buttons pressed
        if (gamePad1.a && !gamePad1A) {
            Objects.requireNonNull(this.detectableObjects.get(ObjectType.GreenPixel)).toggleDisabled();
            gamePad1A = true;
        } else {
            gamePad1A = false;
        }
        if (gamePad1.b && !gamePad1B) {
            Objects.requireNonNull(this.detectableObjects.get(ObjectType.RedProp)).toggleDisabled();
            gamePad1B = true;
        } else {
            gamePad1B = false;
        }
        if (gamePad1.y && !gamePad1Y) {
            Objects.requireNonNull(this.detectableObjects.get(ObjectType.YellowPixel)).toggleDisabled();
            gamePad1Y = true;
        } else {
            gamePad1Y = false;
        }
        if (gamePad1.x && !gamePad1X) {
            Objects.requireNonNull(this.detectableObjects.get(ObjectType.BlueProp)).toggleDisabled();
            gamePad1X = true;
        } else {
            gamePad1X = false;
        }

        if (gamePad2.b && !gamePad2B) {
            Objects.requireNonNull(this.detectableObjects.get(ObjectType.RedTape)).toggleDisabled();
            gamePad2B = true;
        } else {
            gamePad2B = false;
        }
        if (gamePad2.y && !gamePad2Y) {
            Objects.requireNonNull(this.detectableObjects.get(ObjectType.WhitePixel)).toggleDisabled();
            gamePad2Y = true;
        } else {
            gamePad2Y = false;
        }
        if (gamePad2.x && !gamePad2X) {
            Objects.requireNonNull(this.detectableObjects.get(ObjectType.BlueTape)).toggleDisabled();
            gamePad2X = true;
        } else {
            gamePad2X = false;
        }
        if (gamePad1.right_trigger > .2 && !gamePad1RightTriggerWasPressed) {
            Objects.requireNonNull(this.detectableObjects.get(ObjectType.PurplePixel)).toggleDisabled();
            gamePad1RightTriggerWasPressed = true;
        } else {
            gamePad1RightTriggerWasPressed = false;
        }

    }

    public enum ObjectType {
        RedProp, BlueProp, BlueTape, RedTape, GreenPixel, YellowPixel, PurplePixel, WhitePixel, CrossHair
    }

    // Detectable objects
    private final HashMap<ObjectType, DetectableObject> detectableObjects = new HashMap<>();

    HsvBounds[] redPropBounds = {
            new HsvBounds(new Scalar(170, 200, 80), new Scalar(180, 255, 255)),
            new HsvBounds(new Scalar(0, 200, 80), new Scalar(10, 255, 255))
    };

    HsvBounds[] bluePropBounds = {
            new HsvBounds(new Scalar(105, 50, 80), new Scalar(120, 255, 255))
    };

    HsvBounds[] yellowPixelBounds = {
            new HsvBounds(new Scalar(15, 150, 70), new Scalar(25, 255, 255))
    };

    HsvBounds[] greenPixelBounds = {
            new HsvBounds(new Scalar(35, 0, 0), new Scalar(65, 255, 255))
    };

    HsvBounds[] purplePixelBounds = {
            new HsvBounds(new Scalar(120, 50, 120), new Scalar(150, 255, 255))
    };

    HsvBounds[] whitePixelBounds = {
            new HsvBounds(new Scalar(0, 0, 150), new Scalar(180, 35, 255))
    };

    {
        DetectableObject redPropObject = new DetectableObject(ObjectType.RedProp, "RP", redPropBounds, 4, 4);
        this.addObject(redPropObject);

        DetectableObject bluePropObject = new DetectableObject(ObjectType.BlueProp, "BP", bluePropBounds, 4, 4);
        this.addObject(bluePropObject);

        DetectableObject yellowPixel = new DetectableObject(ObjectType.YellowPixel, "YP", yellowPixelBounds, 1, 10);
        this.addObject(yellowPixel);

        DetectableObject greenPixel = new DetectableObject(ObjectType.GreenPixel, "GP", greenPixelBounds, 1, 10);
        this.addObject(greenPixel);

        DetectableObject purplePixel = new DetectableObject(ObjectType.PurplePixel, "PP", purplePixelBounds, 1, 10);
        this.addObject(purplePixel);

        DetectableObject whitePixel = new DetectableObject(ObjectType.WhitePixel, "WP", whitePixelBounds, 1, 10);
        this.addObject(whitePixel);
    }

    public HashMap<ObjectType, DetectableObject> getDetectableObjects() {
        return detectableObjects;
    }

    Rect areaOfInterest;

    int minAllowedX;
    int maxAllowedX;
    int minAllowedY;
    int maxAllowedY;

    Mat mHsvMat = new Mat();
    Mat pyrDownHsvMat = new Mat();
    Mat overallMaskOfObject = new Mat();
    Mat mSingularMask = new Mat();
    Mat mDilatedMask = new Mat();
    Mat mHierarchy = new Mat();
    Mat nothingMat = new Mat();
    List<MatOfPoint> objectsFound = new ArrayList<>();

    public ObjectDetector(int minAllowedX, int maxAllowedX, int minAllowedY, int maxAllowedY) {
        this.minAllowedX = minAllowedX;
        this.maxAllowedX = maxAllowedX;
        this.minAllowedY = minAllowedY;
        this.maxAllowedY = maxAllowedY;
        setupAreaOfInterest();
    }

    public void addObject(DetectableObject object) {
        this.detectableObjects.put(object.getType(), object);
    }

    private void setupAreaOfInterest() {
        this.areaOfInterest = new Rect(minAllowedX, minAllowedY, maxAllowedX - minAllowedX, maxAllowedY - minAllowedY);
    }

    public String getStatus() {
        return String.format(Locale.getDefault(),
                "%s x:%d-%d, y:%d-%d",
                getDetectionStatus(), minAllowedX, maxAllowedX, minAllowedY, maxAllowedY
        );
    }

    public String getDetectionStatus() {
        StringBuilder status = new StringBuilder();
        for (DetectableObject detectableObject : detectableObjects.values()) {
            synchronized (detectableObject) {
                if (!detectableObject.isDisabled() && detectableObject.getFoundObjects().size() > 0) {
                    status
                            .append(detectableObject.getShortName())
                            .append(": ")
                            .append(detectableObject.getFoundObjects().size())
                            .append("@")
                            .append(detectableObject.getXPositionOfLargestObject())
                            .append(",")
                            .append(detectableObject.getYPositionOfLargestObject())
                            .append(", ");
                }
            }
        }
        return status.toString();
    }

    public Rect getAreaOfInterest() {
        return areaOfInterest;
    }

    public void enableObject(ObjectType type) {
        this.enableObject(type, true);
    }

    public void disableObject(ObjectType type) {
        this.enableObject(type, false);
    }

    public void enableObject(ObjectType type, boolean enable) {
        DetectableObject detectableObject = detectableObjects.get(type);
        if (detectableObject != null) {
            if (enable) {
                detectableObject.enable();
            } else {
                detectableObject.disable();
            }
        }
    }

    /**
     * Take an rgb image and return a map of objects detected
     *
     * @param rgbImage a matrix of rgb pixels
     * @return a map of detected objects
     */
    public Map<ObjectType, DetectableObject> process(Mat rgbImage) {
        //save image sent in HSV format
        Imgproc.cvtColor(rgbImage, mHsvMat, Imgproc.COLOR_RGB2HSV);
        //pyramid down twice
        Imgproc.pyrDown(rgbImage, pyrDownHsvMat);
        Imgproc.pyrDown(pyrDownHsvMat, pyrDownHsvMat);
        //convert to HSV so we can use hsv range of objects to filter
        Imgproc.cvtColor(pyrDownHsvMat, pyrDownHsvMat, Imgproc.COLOR_RGB2HSV);

        //go through each of our detectable objects to see if we are seeing any of them
        synchronized (detectableObjects) {
            for (DetectableObject detectableObject : detectableObjects.values()) {
                detectableObject.clearFoundObjects();
                //only look for object if it is not disabled
                if (!detectableObject.isDisabled()) {
                    findObject(detectableObject);
                }
            }
        }
        return detectableObjects;
    }

    private void findObject(DetectableObject detectableObject) {
        overallMaskOfObject = Mat.zeros(pyrDownHsvMat.size(), CvType.CV_8UC1);
        //go through each specified hsv bounds of the detectable object
        for (HsvBounds bounds : detectableObject.getHsvBounds()) {
            //remove all aspects of the image except those within the hsv bounds being considered
            Core.inRange(pyrDownHsvMat, bounds.getLowerBound(), bounds.getUpperBound(), mSingularMask);
            //do a bitwise or with the overall mask (mMask) with this range specific mask to create
            //an overall mask considering all ranges for this object
            Core.bitwise_or(overallMaskOfObject, mSingularMask, overallMaskOfObject);
        }

        //dilate image to get less sharp images
        Imgproc.dilate(overallMaskOfObject, mDilatedMask, nothingMat);
        //release previously found objects
        for (MatOfPoint foundContour : objectsFound) {
            foundContour.release();
        }
        objectsFound.clear();
        //find the contours in the dilated image
        Imgproc.findContours(mDilatedMask, objectsFound, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        //System.out.println("Found " + objectsFound.size() + " objects of type: " + detectableObject.getType());
        //check each contour found to see if it is the largest
        for (MatOfPoint contour : objectsFound) {
            Rect boundingRectangle = Imgproc.boundingRect(contour);
            //check to see if the contour is within our specified x and y limits
            //we multiply by 4 because we had pyramid down twice
            if (boundingRectangle.x * 4 <= maxAllowedY && boundingRectangle.x * 4 >= minAllowedY
                    && boundingRectangle.y * 4 <= maxAllowedX && boundingRectangle.y * 4 >= minAllowedX) {
                double area = Imgproc.contourArea(contour);
                //check to see if contour area is at least our minimum area
                if (area >= MINIMUM_AREA || detectableObject.getType() == ObjectType.CrossHair) {
                    //zoom into contour because we had pyrDown twice earlier
                    Core.multiply(contour, new Scalar(4, 4), contour);
                    detectableObject.addFoundObject(contour, area);
                    //Match.log("Found " + objectType + " of area: " + area);
                }
            }
        }
    }

    /**
     * Returns the area of the largest object (in area) seen of the provided objectName
     *
     * @param objectType
     * @return
     */
    public double getLargestArea(ObjectType objectType) {
        synchronized (detectableObjects) {
            DetectableObject detectableObject = detectableObjects.get(objectType);
            if (detectableObject != null) {
                return detectableObject.largestArea;
            }
        }
        return 0;
    }

    public void decrementMinAllowedX() {
        this.minAllowedX = Math.max(minAllowedX - 1, 0);
        setupAreaOfInterest();
    }

    public void incrementMinAllowedX() {
        this.minAllowedX = Math.min(minAllowedX + 1, RobotConfig.Y_PIXEL_COUNT - 1);
        setupAreaOfInterest();
    }

    public void decrementMaxAllowedX() {
        this.maxAllowedX = Math.max(maxAllowedX - 1, 0);
        setupAreaOfInterest();
    }

    public void incrementMaxAllowedX() {
        this.maxAllowedX = Math.min(maxAllowedX + 1, RobotConfig.Y_PIXEL_COUNT);
        setupAreaOfInterest();
    }

    public void decrementMinAllowedY() {
        this.minAllowedY = Math.max(minAllowedY - 1, 0);
        setupAreaOfInterest();
    }

    public void incrementMinAllowedY() {
        this.minAllowedY = Math.min(minAllowedY + 1, RobotConfig.X_PIXEL_COUNT - 1);
        setupAreaOfInterest();
    }

    public void decrementMaxAllowedY() {
        this.maxAllowedY = Math.max(maxAllowedY - 1, 0);
        setupAreaOfInterest();
    }

    public void incrementMaxAllowedY() {
        this.maxAllowedY = Math.min(maxAllowedY + 1, RobotConfig.X_PIXEL_COUNT);
        setupAreaOfInterest();
    }

    public void setMaxAllowedY(int maxAllowedY) {
        this.maxAllowedY = maxAllowedY;
    }

    public void setMaxAllowedX(int maxAllowedX) {
        this.maxAllowedX = maxAllowedX;
    }

    /**
     * Return the distance to the object from the camera
     *
     * @return
     */
    public double getDistanceFromCameraOfLargestObject(ObjectType objectType) {
        synchronized (detectableObjects) {
            DetectableObject detectableObject = detectableObjects.get(objectType);
            Rect boundingRectangle = Imgproc.boundingRect(detectableObject.getLargestObject());
            return detectableObject.getWidth() * FOCAL_LENGTH / boundingRectangle.height;
        }
    }

    /**
     * Returns the x position of the largest object of the type seen
     * X and y coordinates are reversed from the point of view of the robot from the camera image
     * <p>
     * Also camera's 0 is really at 1920/2
     *
     * @return
     */
    public double getXPositionOfLargestObject(ObjectType objectType) {
        synchronized (detectableObjects) {
            DetectableObject detectableObject = detectableObjects.get(objectType);
            return detectableObject.getXPositionOfLargestObject();
        }
    }

    /**
     * Returns the y position of the largest object of the type seen (in inches)
     * X and y coordinates are reversed from the point of view of the robot from the camera image
     * <p>
     * Also camera's 0 is really at 1920/2
     *
     * @return
     */
    public double getYPositionOfLargestObject(ObjectType objectType) {
        synchronized (detectableObjects) {
            DetectableObject detectableObject = detectableObjects.get(objectType);
            return detectableObject.getYPositionOfLargestObject();
        }

    }

    public double getWidthOfLargestObject(ObjectType objectType) {
        synchronized (detectableObjects) {
            DetectableObject detectableObject = detectableObjects.get(objectType);
            return detectableObject.getWidthOfLargestObject();
        }

    }

    public double getHeightOfLargestObject(ObjectType objectType) {
        synchronized (detectableObjects) {
            DetectableObject detectableObject = detectableObjects.get(objectType);
            return detectableObject.getHeightOfLargestObject();
        }

    }

    public static class HsvBounds {
        Scalar lowerBound, upperBound;

        public HsvBounds(Scalar lowerBound, Scalar upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        public Scalar getLowerBound() {
            return lowerBound;
        }

        public void setLowerBound(Scalar lowerBound) {
            this.lowerBound = lowerBound;
        }

        public Scalar getUpperBound() {
            return upperBound;
        }

        public void setUpperBound(Scalar upperBound) {
            this.upperBound = upperBound;
        }

        public String toString() {
            return String.format(Locale.getDefault(), "%s-%s",
                    lowerBound.toString(), upperBound.toString());
        }
    }

    public Field.SpikePosition getSpikePosition() {
        DetectableObject detectableObject;
        synchronized (detectableObjects) {
            if (Match.getInstance().getAlliance() == Alliance.Color.RED) {
                detectableObject = getDetectableObjects().get(ObjectDetector.ObjectType.RedProp);
            } else {
                detectableObject = getDetectableObjects().get(ObjectDetector.ObjectType.BlueProp);
            }
            if (detectableObject != null) {
                int xPositionOfProp = detectableObject.getXPositionOfLargestObject();
                if (xPositionOfProp > 450) {
                    lastSpikePosition = Field.SpikePosition.Right;
                } else if (xPositionOfProp > 150) {
                    lastSpikePosition = Field.SpikePosition.Middle;
                } else {
                    lastSpikePosition = Field.SpikePosition.Left;
                }
            }
        }
        return lastSpikePosition;
    }
}
