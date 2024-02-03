package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;

import java.util.Locale;

public class LED {
    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;

    DcMotor whiteLED;

    public LED(HardwareMap hardwareMap) {
        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, RobotConfig.BLINKIN);
        whiteLED = hardwareMap.get(DcMotor.class, RobotConfig.WHITE_LED);
        whiteLED.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        turnOnWhiteLED(true);
        stop();
    }

    public void setPattern(RevBlinkinLedDriver.BlinkinPattern pattern) {
        this.pattern = pattern;
        this.blinkinLedDriver.setPattern(pattern);
        turnOnWhiteLED(true);
    }

    public RevBlinkinLedDriver.BlinkinPattern getPattern() {
        return pattern;
    }

    public void stop() {
        this.blinkinLedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
        turnOnWhiteLED(false);
    }
    public void turnOnWhiteLED(boolean turnOn) {

        if (turnOn) {
            whiteLED.setPower(-1);
        }
        else {
            whiteLED.setPower(0);
        }
    }

    public String getStatus() {
        return String.format(Locale.getDefault(), "Pattern: %s, white LED: %.2f", getPattern().toString(),
                whiteLED.getPower());
    }
}
