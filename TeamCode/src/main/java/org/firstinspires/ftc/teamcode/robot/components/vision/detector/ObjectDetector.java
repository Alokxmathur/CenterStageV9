package org.firstinspires.ftc.teamcode.robot.components.vision.detector;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A class to detect objects on the field
 *
 * For this year the objects we try to detect are red and blue props, yellow, white, green and purple
 * pixels, blue and red tapes
 */
public class ObjectDetector {

    boolean gamePad1A, gamePad1B, gamePad1Y, gamePad1X, gamePad2A, gamePad2B, gamePad2Y, gamePad2X;

    Field.SpikePosition lastSpikePosition = Field.SpikePosition.NotSeen;

    /**
     * Manage Object detection based on game pad buttons
     *
     * GamePad 1 a - toggle Green Pixel Detection
     * GamePad 1 b - toggle Red Prop Detection
     * GamePad 1 y - toggle Yellow Pixel Detection
     * GamePad 1 x - Blue Prop Detection
     *
     * GamePad 2 a - toggle Object Detection at cross hair
     * GamePad 2 b - toggle Red Tape Detection
     * GamePad 2 y - toggle White Pixel Detection
     * GamePad 2 x - toggle Blue Tape Detection
     * @param
     */
    public void manageObjectDetection(Gamepad gamePad1, Gamepad gamePad2) {
        /*
        //disable all objects to start with
        for (DetectableObject detectableObject: detectableObjects.values()) {
            detectableObject.disable();
        }
         */
        //enable objects based on the buttons pressed
        if (gamePad1.a && !gamePad1A) {
                this.detectableObjects.get(ObjectType.GreenPixel).toggleDisabled();
                gamePad1A = true;
        }
        else {
            gamePad1A = false;
        }
        if (gamePad1.b && !gamePad1B) {
            this.detectableObjects.get(ObjectType.RedProp).toggleDisabled();
            gamePad1B = true;
        }
        else {
            gamePad1B = false;
        }
        if (gamePad1.y && !gamePad1Y) {
            this.detectableObjects.get(ObjectType.YellowPixel).toggleDisabled();
            gamePad1Y = true;
        }
        else {
            gamePad1Y = false;
        }
        if (gamePad1.x && !gamePad1X) {
            this.detectableObjects.get(ObjectType.BlueProp).toggleDisabled();
            gamePad1X = true;
        }
        else {
            gamePad1X = false;
        }

        if (gamePad2.a && !gamePad2A) {
            findObjectAtCrossHair = !findObjectAtCrossHair;
            gamePad2A = true;
        }
        else {
            gamePad2A = false;
        }
        if (gamePad2.b && !gamePad2B) {
            this.detectableObjects.get(ObjectType.RedTape).toggleDisabled();
            gamePad2B = true;
        }
        else {
            gamePad2B = false;
        }
        if (gamePad2.y && !gamePad2Y) {
            this.detectableObjects.get(ObjectType.WhitePixel).toggleDisabled();
            gamePad2Y = true;
        }
        else {
            gamePad2Y = false;
        }
        if (gamePad2.x && !gamePad2X) {
            this.detectableObjects.get(ObjectType.BlueTape).toggleDisabled();
            gamePad2X = true;
        }
        else {
            gamePad2X = false;
        }

    }

    public void setupCrossHair(Mat sizingMat) {
        //System.out.println("setting up cross hair, mat size = " + sizingMat.size());

        crossHairPoint = new Point(sizingMat.cols()/2, sizingMat.rows()/2);
    }

    public Point getCrossHairPoint() {
        return crossHairPoint;
    }

    public enum ObjectType {
        RedProp, BlueProp, BlueTape, RedTape, GreenPixel, YellowPixel, PurplePixel, WhitePixel, CrossHair
    }

    // Detectable objects
    private static final HashMap<ObjectType, DetectableObject> detectableObjects = new HashMap<>();

    HsvBounds[] redPropBounds = {
            new HsvBounds(new Scalar(170, 200, 80), new Scalar(180, 255, 255)),
            new HsvBounds(new Scalar(0, 200, 80), new Scalar(10, 255, 255))
    };

    HsvBounds[] redTapeBounds = {
            new HsvBounds(new Scalar(0, 211, 70), new Scalar(10, 255, 255)),
            new HsvBounds(new Scalar(170, 211, 70), new Scalar(180, 255, 255))
    };

    HsvBounds[] blueTapeBounds = {
            new HsvBounds(new Scalar(105, 50, 191), new Scalar(115, 255, 255))
    };

    HsvBounds[] bluePropBounds = {
            new HsvBounds(new Scalar(105, 50, 100), new Scalar(120, 255, 190))
    };

    HsvBounds[] yellowPixelBounds = {
            new HsvBounds(new Scalar(15, 150, 70), new Scalar(25, 255, 255))
    };

    HsvBounds[] greenPixelBounds = {
            new HsvBounds(new Scalar(50, 50, 70), new Scalar(65, 255, 255))
    };

    HsvBounds[] purplePixelBounds = {
            new HsvBounds(new Scalar(127, 60, 120), new Scalar(150, 180, 255))
    };

    HsvBounds[] whitePixelBounds = {
            new HsvBounds(new Scalar(0, 0, 150), new Scalar(180, 35, 255))
    };

    {
        DetectableObject redPropObject = new DetectableObject(ObjectType.RedProp, redPropBounds, 4, 4);
        redPropObject.setShortName("RP");
        redPropObject.enable();
        this.addObject(redPropObject);

        DetectableObject bluePropObject = new DetectableObject(ObjectType.BlueProp, bluePropBounds, 4, 4);
        bluePropObject.setShortName("BP");
        bluePropObject.enable();
        this.addObject(bluePropObject);

        this.addObject(new DetectableObject(ObjectType.YellowPixel, yellowPixelBounds, 1, 10));
        this.addObject(new DetectableObject(ObjectType.GreenPixel, greenPixelBounds, 1, 10));
        this.addObject(new DetectableObject(ObjectType.PurplePixel, purplePixelBounds, 1, 10));
        this.addObject(new DetectableObject(ObjectType.WhitePixel, whitePixelBounds, 1, 10));

        this.addObject(new DetectableObject(ObjectType.RedTape, redTapeBounds, 4, 5));
        this.addObject(new DetectableObject(ObjectType.BlueTape, blueTapeBounds, 4, 5));
    }

    DetectableObject objectAtCrossHair;
    public static HashMap<ObjectType, DetectableObject> getDetectableObjects() {
        return detectableObjects;
    }
    Rect areaOfInterest;
    Point crossHairPoint;

    boolean findObjectAtCrossHair = true;

    int minAllowedX;
    int maxAllowedX;
    int minAllowedY;
    int maxAllowedY;
    double minArea;

    Mat mHsvMat = new Mat();
    Mat pyrDownHsvMat = new Mat();
    Mat mMask = new Mat();
    Mat mSingularMask = new Mat();
    Mat mDilatedMask = new Mat();
    Mat mHierarchy = new Mat();
    Mat nothingMat = new Mat();
    List<MatOfPoint> objectsFound = new ArrayList<>();

    public double[] getCrossHairHSV() {
        return crossHairHSV;
    }

    private double[] crossHairHSV = new double[3];
    private Scalar crossHairColor = new Scalar(0, 0, 0);

    public Scalar getCrossHairColor() {
        return crossHairColor;
    }

    public ObjectDetector(int minAllowedX, int maxAllowedX, int minAllowedY, int maxAllowedY,
                          double minArea) {
        this.minAllowedX = minAllowedY;
        this.maxAllowedX = maxAllowedY;
        this.minAllowedY = minAllowedX;
        this.maxAllowedY = maxAllowedX;
        this.minArea = minArea;
        setupAreaOfInterest();
    }

    public void addObject(DetectableObject object) {
        this.detectableObjects.put(object.getType(), object);
    }

    private void setupAreaOfInterest() {
        this.areaOfInterest = new Rect(minAllowedY, minAllowedX, maxAllowedY-minAllowedY, maxAllowedX-minAllowedX);
    }

    public String getStatus() {
        return String.format(Locale.getDefault(),
                "%s x:%d-%d, y:%d-%d",
                getDetectionStatus(), minAllowedX, maxAllowedX, minAllowedY, maxAllowedY
        );
    }

    public String getDetectionStatus() {
        StringBuilder status = new StringBuilder();
        for (DetectableObject detectableObject: detectableObjects.values()) {
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
        return status.toString();
    }

    public Rect getRectangleOfInterest() {
        return areaOfInterest;
    }

    /**
     * Take an rgb image and return a map of objects detected
     * @param rgbImage
     * @return
     */
    public Map<ObjectType, DetectableObject> process(Mat rgbImage) {
        //save image sent in HSV format
        Imgproc.cvtColor(rgbImage, mHsvMat, Imgproc.COLOR_RGB2HSV);
        //pyramid down twice
        Imgproc.pyrDown(rgbImage, pyrDownHsvMat);
        Imgproc.pyrDown(pyrDownHsvMat, pyrDownHsvMat);
        //convert to HSV so we can use hsv range of objects to filter
        Imgproc.cvtColor(pyrDownHsvMat, pyrDownHsvMat, Imgproc.COLOR_RGB2HSV);

        crossHairHSV = mHsvMat.get((int)crossHairPoint.x, (int)crossHairPoint.y);
        if (findObjectAtCrossHair) {
            double minHue = Math.max(crossHairHSV[0] - 5, 0);
            double maxHue = Math.min(crossHairHSV[0] + 5, 180);
            double minSaturation = Math.max(crossHairHSV[1] - 50, 0);
            double maxSaturation = Math.min(crossHairHSV[1] + 50, 255);
            double minValue = Math.max(crossHairHSV[2] - 50, 0);
            double maxValue = Math.min(crossHairHSV[2] + 50, 255);
            HsvBounds[] hsvBoundsAtCrossHair = new HsvBounds[1];
            hsvBoundsAtCrossHair[0] = new HsvBounds(
                    new Scalar(minHue, minSaturation, minValue),
                    new Scalar(maxHue, maxSaturation, maxValue));
            objectAtCrossHair = new DetectableObject(ObjectType.CrossHair, hsvBoundsAtCrossHair, 0, 0);
            this.addObject(objectAtCrossHair);
        }
        else {
            this.detectableObjects.remove(ObjectType.CrossHair);
        }
        crossHairColor = new Scalar(rgbImage.get((int)crossHairPoint.y, (int)crossHairPoint.x));

        //go through each of our detectable objects
        for (DetectableObject detectableObject : detectableObjects.values()) {
            detectableObject.clearFoundObjects();
            //only look for object if it is not disabled
            if (!detectableObject.isDisabled()) {
                findObject(detectableObject);
            }
        }
        return detectableObjects;
    }

    private void findObject(DetectableObject detectableObject) {
        mMask = Mat.zeros(pyrDownHsvMat.size(), CvType.CV_8UC1);
        //go through each specified hsv bounds of the detectable object
        for (HsvBounds bounds : detectableObject.getHsvBounds()) {
            //remove all aspects of the image except those within the hsv bounds
            Core.inRange(pyrDownHsvMat, bounds.getLowerBound(), bounds.getUpperBound(), mSingularMask);
            Core.bitwise_or(mMask, mSingularMask, mMask);
        }

        //dilate image to get less sharp images
        Imgproc.dilate(mMask, mDilatedMask, nothingMat);
        objectsFound.clear();
        //find the contours in the dilated image
        Imgproc.findContours(mDilatedMask, objectsFound, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        //System.out.println("Found " + objectsFound.size() + " objects of type: " + detectableObject.getType());
        //check each contour found
        for (MatOfPoint contour : objectsFound) {
            Rect boundingRectangle = Imgproc.boundingRect(contour);
            //check to see if the contour is within our specified x and y limits
            if (boundingRectangle.x * 4 <= maxAllowedX && boundingRectangle.x * 4 >= minAllowedX
                    && boundingRectangle.y * 4 <= maxAllowedY && boundingRectangle.y * 4 >= minAllowedY) {
                double area = Imgproc.contourArea(contour);
                //check to see if contour area is at least our minimum area
                if (area >= minArea || detectableObject.getType() == ObjectType.CrossHair) {
                    //zoom into contour because we had pyrDown twice earlier
                    Core.multiply(contour, new Scalar(4, 4), contour);
                    mMask = Mat.zeros(mHsvMat.size(), CvType.CV_8UC1);
                    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
                    contours.add(contour);
                    Imgproc.drawContours(mMask, contours, -1, new Scalar(255), -1);
                    Scalar mean = Core.mean(mHsvMat, mMask);
                    detectableObject.addFoundObject(contour, area, mean);

                    //Match.log("Found " + objectType + " of area: " + area);
                } else {
                    //System.out.println("Area " + area + " too small for " + detectableObject.getType());
                }
            }
            else {
                //System.out.println("Skipping " + detectableObject.getType() + " contour at " + boundingRectangle.x*4 + "," + boundingRectangle.y*4);
            }
        }
    }

    /**
     * Returns the area of the largest object (in area) seen of the provided objectName
     * @param objectType
     * @return
     */
    public double getLargestArea(ObjectType objectType) {
        DetectableObject detectableObject = detectableObjects.get(objectType);
        if (detectableObject != null) {
            return detectableObject.largestArea;
        }
        return 0;
    }

    /**
     * Returns the mean value of the largest object seen of the provided objectName
     * @param objectType
     * @return
     */
    public Scalar getLargestAreaMean(ObjectType objectType) {
        DetectableObject detectableObject = detectableObjects.get(objectType);
        if (detectableObject != null) {
            return detectableObject.getLargestAreaMean();
        }
        return new Scalar(0);
    }

    public void decrementMinAllowedX() {
        this.minAllowedX = Math.max(minAllowedX - 1, 0);
        setupAreaOfInterest();
    }
    public void incrementMinAllowedX() {
        this.minAllowedX = Math.min(minAllowedX + 1, RobotConfig.Y_PIXEL_COUNT -1);
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
        this.minAllowedY = Math.min(minAllowedY + 1, RobotConfig.X_PIXEL_COUNT-1);
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

    /** Return the distance to the object from the camera
     *
     * @return
     */
    public double getDistanceFromCameraOfLargestObject(ObjectType objectType) {
        DetectableObject detectableObject = detectableObjects.get(objectType);
        if (detectableObject != null) {
            Rect boundingRectangle = Imgproc.boundingRect(detectableObject.getLargestObject());
            return detectableObject.getWidth() * DetectorPipeline.FOCAL_LENGTH / boundingRectangle.height;
        }
        else {
            return -1;
        }
    }
    /** Returns the x position of the largest object of the type seen
     * X and y coordinates are reversed from the point of view of the robot from the camera image
     *
     * Also camera's 0 is really at 1920/2
     *
     * @return
     */
    public double getXPositionOfLargestObject(ObjectType objectType) {
        DetectableObject detectableObject = detectableObjects.get(objectType);
        if (detectableObject != null) {
            return detectableObject.getXPositionOfLargestObject();
        }
        return -1;
    }
    /** Returns the y position of the largest object of the type seen (in inches)
     * X and y coordinates are reversed from the point of view of the robot from the camera image
     *
     * Also camera's 0 is really at 1920/2
     *
     * @return
     */
    public double getYPositionOfLargestObject(ObjectType objectType) {
        DetectableObject detectableObject = detectableObjects.get(objectType);
        if (detectableObject != null) {
            return detectableObject.getYPositionOfLargestObject();
        }
        return -1;
    }

    public double getWidthOfLargestObject(ObjectType objectType) {
        DetectableObject detectableObject = detectableObjects.get(objectType);
        if (detectableObject != null) {
            return detectableObject.getWidthOfLargestObject();
        }
        return -1;
    }

    public double getHeightOfLargestObject(ObjectType objectType) {
        DetectableObject detectableObject = detectableObjects.get(objectType);
        if (detectableObject != null) {
            return detectableObject.getHeightOfLargestObject();
        }
        return -1;
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

}
