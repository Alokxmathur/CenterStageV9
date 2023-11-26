package org.firstinspires.ftc.teamcode.robot.operations;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.components.IntakeBucket;

import java.util.Date;
import java.util.Locale;

public class IntakeBucketOperation extends Operation {
    public enum Type {
        Up, Down, Clamp, Unclamp
    }

    IntakeBucket intakeBucket;
    Type type;

    public IntakeBucketOperation (IntakeBucket intakeBucket, IntakeBucketOperation.Type type, String title){
        this.intakeBucket = intakeBucket;
        this.type = type;
        this.title = title;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "IntakeBucket: --%s",
                this.title);
    }


    @Override
    public boolean isComplete() {
        if (type == IntakeBucketOperation.Type.Down || type == IntakeBucketOperation.Type.Up || type == IntakeBucketOperation.Type.Clamp || type == IntakeBucketOperation.Type.Unclamp) {
            return (new Date().getTime() - this.getStartTime().getTime() > RobotConfig.SERVO_REQUIRED_TIME);
        }
        else {
            return false;
        }
    }

    @Override
    public void startOperation() {
        switch (this.type) {
            case Up:
                intakeBucket.raise();
                break;
            case Down: {
                intakeBucket.lower();
                break;
            }
            case Clamp:
            {
                intakeBucket.clamp();
                break;
            }
            case Unclamp:{
                intakeBucket.unclamp();
                break;
            }
        }
    }

    @Override
    public void abortOperation() {
        intakeBucket.stop();
    }
}
