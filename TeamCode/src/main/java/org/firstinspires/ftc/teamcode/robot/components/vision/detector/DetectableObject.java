package org.firstinspires.ftc.teamcode.robot.components.vision.detector;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
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
    List<MatOfPoint> foundObjects = new ArrayList<>();

    double largestArea;
    double width;
    double height;
    int largestAreaIndex;
    Scalar largestAreaMean;
    String shortName;

    boolean disabled = true;

    public DetectableObject(ObjectDetector.ObjectType type, ObjectDetector.HsvBounds[] hsvBounds, double width, double height) {
        this.type = type;
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

    public List<MatOfPoint> getFoundObjects() {
        return this.foundObjects;
    }

    public void clearFoundObjects() {
        this.foundObjects = new ArrayList<>();
        largestArea = 0;
        largestAreaMean = new Scalar(0, 0, 0);
    }

    /**
     * Add an object to the list of the objects of this type
     * If the area passed is greater than the largest area already found, the new object passed is
     * considered the largest object and the mean of it considered the mean of the largest object
     *
     * @param objectFound
     * @param area        - are of the object found
     */
    public void addFoundObject(MatOfPoint objectFound, double area, Scalar mean) {
        this.foundObjects.add(objectFound);
        if (area > largestArea) {
            largestAreaIndex = this.foundObjects.size() - 1;
            largestArea = area;
            largestAreaMean = mean;
        }
    }

    /**
     * Returns the largest object seen of this detectable object type
     *
     * @return
     */
    public MatOfPoint getLargestObject() {
        if (largestAreaIndex < this.foundObjects.size()) {
            return this.foundObjects.get(largestAreaIndex);
        } else {
            return null;
        }
    }

    /**
     * Returns the y position of the middle of the largest object
     *
     * @return
     */
    public int getYPositionOfLargestObject() {
        if (getLargestObject() != null) {
            Rect boundingRectangle = Imgproc.boundingRect(getLargestObject());
            return boundingRectangle.y + boundingRectangle.height / 2;
        } else {
            return -1;
        }
    }

    /**
     * Returns the x position of the center of the largest object
     *
     * @return
     */
    public int getXPositionOfLargestObject() {
        if (getLargestObject() != null) {
            Rect boundingRectangle = Imgproc.boundingRect(getLargestObject());
            return boundingRectangle.x + boundingRectangle.width / 2;
        } else {
            return -1;
        }
    }

    /**
     * Returns the width of the largest object
     *
     * @return
     */
    public double getWidthOfLargestObject() {
        RotatedRect rotatedRectangle = getRotatedRectangleOfLargestObject();
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
    public double getHeightOfLargestObject() {
        RotatedRect rotatedRectangle = getRotatedRectangleOfLargestObject();
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
    public Rect getBoundingRectangleOfLargestObject() {
        if (getLargestObject() != null) {
            return Imgproc.boundingRect(getLargestObject());
        } else {
            return null;
        }
    }

    /**
     * Return the rectangle that fits the largest object
     * The rectangle could be at an angle to the x and y axes
     *
     * @return
     */
    public RotatedRect getRotatedRectangleOfLargestObject() {
        if (getLargestObject() != null) {
            return Imgproc.minAreaRect(new MatOfPoint2f(getLargestObject().toArray()));
        } else {
            return null;
        }
    }

    /**
     * Return the mean hsv values of the largest object
     *
     * @return
     */
    public Scalar getMeanOfLargestObject() {
        if (getLargestObject() != null) {
            return largestAreaMean;
        } else {
            return new Scalar(0, 0, 0);
        }
    }


    public double getLargestArea() {
        return largestArea;
    }

    public Scalar getLargestAreaMean() {
        return largestAreaMean;
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
        return String.format(Locale.getDefault(), "Count:%d,Largest@%d,%d,area:%.0f, placement: %s",
                getFoundObjects().size(),
                getXPositionOfLargestObject(),
                getYPositionOfLargestObject(),
                largestArea,
                getXPositionOfLargestObject() > 1200 ? "Right" : (getXPositionOfLargestObject() > 600 ? "Middle" : "Left"));
    }
}
