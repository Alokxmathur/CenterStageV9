package org.firstinspires.ftc.teamcode.robot.components.vision.detector;

import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Imgproc;

import java.util.Locale;

/**
 * A class representing types of objects on the competition field
 * Currently the object differentiation is done based on HSV ranges
 * <p>
 * The class contains a list of object of the type found
 */
public class DetectableObject {
    ObjectDetector.ObjectType type;
    ObjectDetector.HsvBounds[] hsvBounds;
    MatOfPoint foundObject = null;
    Rect boundingRectangle;
    RotatedRect rotatedRectangle;

    double largestArea;
    double width;
    double height;
    int largestAreaIndex;
    String shortName;

    boolean disabled = true;

    public DetectableObject(ObjectDetector.ObjectType type, String shortName, ObjectDetector.HsvBounds[] hsvBounds, double width, double height) {
        this.type = type;
        this.setShortName(shortName);
        this.hsvBounds = hsvBounds;
        this.width = width;
        this.height = height;
    }

    public String getShortName() {
        if (shortName == null) {
            return this.type.toString();
        }
        return shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public ObjectDetector.ObjectType getType() {
        return this.type;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public ObjectDetector.HsvBounds[] getHsvBounds() {
        return hsvBounds;
    }

    public void setHsvBounds(ObjectDetector.HsvBounds hsvBounds[]) {
        this.hsvBounds = hsvBounds;
    }

    public MatOfPoint getFoundObject() {
        return this.foundObject;
    }

    public void clearFoundObjects() {
        if (this.foundObject != null) {
            this.foundObject.release();
        }
        this.foundObject = null;
        this.boundingRectangle = null;
        this.rotatedRectangle = null;
        largestArea = 0;
    }

    /**
     * Add an object
     * If the area passed is greater than the largest area already found, the new object passed is
     * considered the largest object and the mean of it considered the mean of the largest object
     *
     * @param objectFound
     * @param area        - area of the object found
     */
    public void addFoundObject(MatOfPoint objectFound, double area) {
        if (area > largestArea) {
            this.foundObject = objectFound;
            boundingRectangle = Imgproc.boundingRect(foundObject);
            rotatedRectangle = Imgproc.minAreaRect(new MatOfPoint2f(foundObject.toArray()));
            largestArea = area;
        }
    }

    /**
     * Returns the object seen of this detectable object type
     *
     * @return
     */
    public MatOfPoint getObjectSeen() {
        return this.foundObject;
    }

    /**
     * Returns the y position of the middle of the largest object
     *
     * @return
     */
    public int getYPositionOfObject() {
        if (boundingRectangle != null) {
            try {
                return boundingRectangle.y + boundingRectangle.height / 2;
            }
            catch (Throwable e){}
        }
        return -1;
    }

    /**
     * Returns the x position of the center of the object
     *
     * @return
     */
    public int getXPositionOfObject() {
        if (foundObject != null) {
            try {
                return boundingRectangle.x + boundingRectangle.width / 2;
            }
            catch (Throwable e) {
            }
        }
        return -1;
    }

    /**
     * Returns the width of the largest object
     *
     * @return
     */
    public double getWidthOfObject() {
        if (rotatedRectangle != null) {
            return rotatedRectangle.size.height;
        } else {
            return -1;
        }
    }

    /**
     * Returns the height of the largest object
     *
     * @return
     */
    public double getHeightOfObject() {
        if (rotatedRectangle != null) {
            return rotatedRectangle.size.width;
        } else {
            return -1;
        }
    }

    /**
     * Return the bounding rectangle that fits the largest object
     * The rectangle is always aligned with the x and y axes
     *
     * @return
     */
    public Rect getBoundingRectangleOfObject() {
        return boundingRectangle;
    }

    /**
     * Return the rectangle that fits the largest object
     * The rectangle could be at an angle to the x and y axes
     *
     * @return
     */
    public RotatedRect getRotatedRectangleOfObject() {
        return rotatedRectangle;
    }

    public double getLargestArea() {
        return largestArea;
    }

    /**
     * Returns if the object detection is disabled for this object type
     */
    public boolean isDisabled() {
        return disabled;
    }

    public void disable() {
        this.disabled = true;
    }
    public void enable() {
        this.disabled = false;
    }
    public void toggleDisabled() {
        this.disabled = !this.disabled;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "@%d,%d,area:%.0f, placement: %s",
                getXPositionOfObject(),
                getYPositionOfObject(),
                largestArea,
                getXPositionOfObject() > 1200 ? "Right" : (getXPositionOfObject() > 600 ? "Middle" : "Left"));
    }
}
