/*
 * Copyright (c) 2020 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.opmodes.drivercontrolled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.robot.components.vision.detector.ObjectDetectorWebcam;

import java.io.PrintWriter;
import java.io.StringWriter;

@TeleOp(name = "Phoebe: Object Finder", group = "Swanky")
@Disabled
public class ObjectFinderTeleOp extends DriverControlledOperation {
    ObjectDetectorWebcam webcam;

    @Override
    public void init() {
        super.init();
        webcam = robot.getWebcam();
    }
    public void startStreaming() {
        webcam.init(hardwareMap, telemetry);
    }
    @Override
    public void start() {
        startStreaming();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        if (robot.fullyInitialized()) {
            try {
                //robot.handleDriveTrain(gamepad1);

                telemetry.update();
                if (gamepad2.left_stick_x < -0.2) {
                    webcam.decrementMinX();
                }
                if (gamepad2.left_stick_x > 0.2) {
                    webcam.incrementMinX();
                }
                if (gamepad2.right_stick_x < -0.2) {
                    webcam.decrementMaxX();
                }
                if (gamepad2.right_stick_x > 0.2) {
                    webcam.incrementMaxX();
                }

                if (gamepad2.left_stick_y < -0.2) {
                    webcam.decrementMinY();
                }
                if (gamepad2.left_stick_y > 0.2) {
                    webcam.incrementMinY();
                }
                if (gamepad2.right_stick_y < -0.2) {
                    webcam.decrementMaxY();
                }
                if (gamepad2.right_stick_y > 0.2) {
                    webcam.incrementMaxY();
                }


                webcam.manageObjectVisibility(gamepad1, gamepad2);

                match.updateTelemetry(telemetry, robot.getState());
            } catch (Throwable e) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                e.printStackTrace(printWriter);
                telemetry.addLine(stringWriter.toString());
                telemetry.update();
                RobotLog.e("TeleOp run", e, "Error");
            }
        }
    }


}