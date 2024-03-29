package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="BlueRight", group="Phoebe", preselectTeleOp="Phoebe: Driver Controlled")
public class BlueRight extends NearAudienceAutonomous {
    @Override
    public void init() {
        super.tryYellowPixel = false;
        super.init(telemetry, Alliance.Color.BLUE, Field.StartingPosition.Right);
    }
}
