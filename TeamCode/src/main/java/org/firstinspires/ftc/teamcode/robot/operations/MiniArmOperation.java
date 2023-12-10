package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.components.MiniArm;

import java.util.Date;
import java.util.Locale;

public class MiniArmOperation extends Operation {

    public enum Type {
        Drop, Up, Middle
    }
    MiniArm miniArm;
    Type type;

    public MiniArmOperation(MiniArm miniArm, MiniArmOperation.Type type, String title) {
        this.miniArm = miniArm;
        this.type = type;
        this.title = title;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "MiniArm: --%s",
                this.title);
    }
    @Override
    public boolean isComplete() {
        if (type == MiniArmOperation.Type.Drop || type == MiniArmOperation.Type.Up) {
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
                    miniArm.goUp();
                    break;
                case Drop:
                {
                    miniArm.goDrop();
                    break;
                }
                case Middle:
                {
                    miniArm.goMiddle();
                    break;
                }
            }
        }


    @Override
    public void abortOperation() {
        miniArm.stop();
    }
}
