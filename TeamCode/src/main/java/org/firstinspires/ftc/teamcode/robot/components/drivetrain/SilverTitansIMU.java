package org.firstinspires.ftc.teamcode.robot.components.drivetrain;

import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.game.Match;

public class SilverTitansIMU implements IMU {
    IMU primaryIMU, secondaryIMU;
    boolean primaryFailed = false;
    public SilverTitansIMU(IMU controlHubIMU, IMU expansionHubIMU) {
        this.primaryIMU = controlHubIMU;
        this.secondaryIMU = expansionHubIMU;
    }

    @Override
    /**
     * We presume the primary and secondary IMUs have been initialized elsewhere by the code
     * that created this class instance
     */
    public boolean initialize(Parameters parameters) {
        return true;
    }

    @Override
    public void resetYaw() {
        this.primaryIMU.resetYaw();
        this.secondaryIMU.resetYaw();
        this.primaryFailed = false;
    }

    private boolean usePrimary() {
        if (!primaryFailed) {
            YawPitchRollAngles orientation = primaryIMU.getRobotYawPitchRollAngles();
            long imuTime = orientation.getAcquisitionTime();
            if (imuTime == 0) {
                primaryFailed = true;
                Match.log("Primary IMU failed");
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

    @Override
    public YawPitchRollAngles getRobotYawPitchRollAngles() {
        return usePrimary() ? primaryIMU.getRobotYawPitchRollAngles() : secondaryIMU.getRobotYawPitchRollAngles();
    }

    @Override
    public Orientation getRobotOrientation(AxesReference reference, AxesOrder order, AngleUnit angleUnit) {
        return usePrimary()
                ? primaryIMU.getRobotOrientation(reference, order, angleUnit)
                : secondaryIMU.getRobotOrientation(reference, order, angleUnit);
    }

    public Quaternion getRobotOrientationAsQuaternion() {
        if (usePrimary()) {
            return primaryIMU.getRobotOrientationAsQuaternion();
        }
        else {
            return secondaryIMU.getRobotOrientationAsQuaternion();
        }
    }

    @Override
    public AngularVelocity getRobotAngularVelocity(AngleUnit angleUnit) {
        return usePrimary() ? primaryIMU.getRobotAngularVelocity(angleUnit) : secondaryIMU.getRobotAngularVelocity(angleUnit);
    }

    @Override
    public Manufacturer getManufacturer() {
        return primaryIMU.getManufacturer();
    }

    @Override
    public String getDeviceName() {
        return primaryIMU.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return primaryIMU.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return primaryIMU.getVersion();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        primaryIMU.resetDeviceConfigurationForOpMode();
        secondaryIMU.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        primaryIMU.close();
        secondaryIMU.close();
    }
}
