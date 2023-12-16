package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.operations.MiniArmOperation;

import java.util.Locale;

public class MiniArm {
    Servo miniArm;

    public MiniArm (HardwareMap hardwareMap) {
        this.miniArm = hardwareMap.get(Servo.class, RobotConfig.MINIARM);

        assumeInitialPosition();
    }

    private void assumeInitialPosition() {
        this.miniArm.setPosition(RobotConfig.MINIARM_INITIAL_POSITION);
    }

    public void goUp() {
        this.miniArm.setPosition(RobotConfig.MINIARM_INITIAL_POSITION);
    }
    public void goDrop() {
        this.miniArm.setPosition(RobotConfig.MINIARM_DROP_POSITION);
    }

    public void incrementalUp() {
        this.miniArm.setPosition(miniArm.getPosition() + RobotConfig.SERVO_INCREMENT);
    }

    public void decrementalDrop() {
        this.miniArm.setPosition(miniArm.getPosition() - RobotConfig.SERVO_INCREMENT);
    }

    public void setPositions(MiniArmOperation.Type type) {
        switch (type) {
            case Up: {
                miniArm.setPosition(RobotConfig.MINIARM_INITIAL_POSITION);
                break;
            }
            case Drop: {
                miniArm.setPosition(RobotConfig.MINIARM_DROP_POSITION);
                break;
            }
        }
    }

    public void stop(){

    }

    public String getStatus() {
        return String.format(Locale.getDefault(), "M:%.3f",
                miniArm.getPosition());
    }
}
