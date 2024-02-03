package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.game.Match;

import java.util.Locale;

public class DroneOperation extends Operation {
    public enum Type {
        Shoot, Travel
    }
    private Type type;

    public DroneOperation(Type type, String title) {
        this.type = type;
        this.title = title;
    }

    public String toString() {
        return String.format(Locale.getDefault(),"Drone: %s --%s",
                this.type, this.title);
    }
    @Override
    public boolean isComplete() {
        if (type == Type.Shoot) {
            //wait half a second for launcher to reach shooting position
            if ((new java.util.Date()).getTime() - this.getStartTime().getTime() > 500) {
                Match.getInstance().getRobot().getDroneLauncher().releaseDrone();
                return true;
            }
            else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void startOperation() {
        if (type == Type.Shoot) {
            Match.getInstance().getRobot().getDroneLauncher().goToShootingPosition();
        }
        else if (type == Type.Travel) {
            Match.getInstance().getRobot().getDroneLauncher().goToTravelPosition();
        }
    }

    @Override
    public void abortOperation() {

    }
}
