package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="BlueRightNoYP", group="Phoebe", preselectTeleOp="Phoebe: Driver Controlled")
public class BlueRightNoYP extends NearAudienceAutonomous {
    @Override
    public void init() {
        super.init(telemetry, Alliance.Color.BLUE, Field.StartingPosition.Right);
        super.setTryYellowPixel(false);
    }
}
