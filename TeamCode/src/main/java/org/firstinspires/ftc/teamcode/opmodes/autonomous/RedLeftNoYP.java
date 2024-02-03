package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="RedLeftNoYP", group="Phoebe", preselectTeleOp="Phoebe: Driver Controlled")
public class RedLeftNoYP extends NearAudienceAutonomous {
    @Override
    public void init() {
        super.init(telemetry, Alliance.Color.RED, Field.StartingPosition.Left);
        super.setTryYellowPixel(false);
    }
}
