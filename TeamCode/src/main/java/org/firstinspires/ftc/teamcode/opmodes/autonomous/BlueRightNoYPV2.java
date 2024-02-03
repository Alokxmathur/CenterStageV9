package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="BlueRightNoYPV2", group="Phoebe", preselectTeleOp="Phoebe: Driver Controlled")
public class BlueRightNoYPV2 extends NearAudienceAutonomous {
    @Override
    public void init() {
        super.init(telemetry, Alliance.Color.BLUE, Field.StartingPosition.Right);
        //super.setTryYellowPixel(false);
    }
}
