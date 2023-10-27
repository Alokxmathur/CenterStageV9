package org.firstinspires.ftc.teamcode.opmodes.drivercontrolled;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.Robot;

@TeleOp(name="Test T265", group="Phoebe")
//@Disabled
public class TestT265Camera extends OpMode
{
    private final FtcDashboard dashboard = FtcDashboard.getInstance();
    private Match match = Match.getNewInstance();

    private Robot robot = match.getRobot();

    @Override
    public void init() {
        robot.init(hardwareMap, telemetry, match);
        match.getField().init(Alliance.Color.RED, Field.StartingPosition.Right);
    }

    @Override
    public void init_loop() {
        if (Field.isNotInitialized()) {
            telemetry.addData("Status", "Trajectories initializing, please wait");
        }
        else if (robot.fullyInitialized()) {
            telemetry.addData("Status", "Let's go");
        }
        else {
            telemetry.addData("Status", "Waiting for cameras to initialize");
        }
        telemetry.update();
    }

    @Override
    public void loop() {
        robot.handleGameControllers(gamepad1, gamepad2);
        match.updateTelemetry(telemetry,"Running");
    }
}