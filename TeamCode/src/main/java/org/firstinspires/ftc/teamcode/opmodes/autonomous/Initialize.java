package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Initialization", group="Phoebe", preselectTeleOp="Phoebe: Driver Controlled")
public class Initialize extends Autonomous {
    @Override
    public void init() {
        super.init(telemetry, Alliance.Color.NotSelected, Field.StartingPosition.NotSelected);
    }
}
