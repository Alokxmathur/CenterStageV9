package org.firstinspires.ftc.teamcode.opmodes.drivercontrolled;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;

@Config
@TeleOp
@Disabled
public class PIDF_Shoulder extends OpMode {
    private PIDController controller;
    public static double p = 0, i = 0, d = 0;
    public static double f = 0;

    public static double target = 0;

    private final double ticks_in_degree = 28 / 360 * 100 * 3;

    private DcMotorEx armMotor;
    @Override
    public void init() {
        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        armMotor = hardwareMap.get(DcMotorEx.class, RobotConfig.SHOULDER);
    }

    @Override
    public void loop() {
        controller.setPID(p, i, d);
        int armPosition = armMotor.getCurrentPosition();
        double pid = controller.calculate(armPosition, target);
        double ff = Math.cos(Math.toRadians(target/ticks_in_degree)) * f;

        double power = pid + ff;
        armMotor.setPower(power);

        telemetry.addData("pos ", armPosition);
        telemetry.addData("target ", target);
        telemetry.update();
    }
}
