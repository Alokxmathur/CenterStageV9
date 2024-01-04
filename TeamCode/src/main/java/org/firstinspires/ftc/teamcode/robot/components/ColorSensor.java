package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;

/**
 * Created by Silver Titans on 10/26/17.
 */

public class ColorSensor {
    //our color sensor
    NormalizedColorSensor sensor;

    public ColorSensor(HardwareMap hardwareMap) {
        // Define our sensor
        sensor = hardwareMap.get(NormalizedColorSensor.class, RobotConfig.COLOR_SENSOR);
        sensor.setGain(8f);
        // If possible, turn the light on in the beginning (it might already be on anyway,
        // we just make sure it is if we can).
        if (sensor instanceof SwitchableLight) {
            ((SwitchableLight)sensor).enableLight(true);
        }
    }

    public NormalizedRGBA getColors() {
        return sensor.getNormalizedColors();
    }
    public double getBlue() {
        return getColors().blue;
    }
    public double getRed() {
        return getColors().red;
    }
    public double getGreen() {
        return getColors().green;
    }
}
