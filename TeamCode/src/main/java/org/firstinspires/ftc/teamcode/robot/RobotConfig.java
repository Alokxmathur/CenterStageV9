 package org.firstinspires.ftc.teamcode.robot;

 import org.firstinspires.ftc.teamcode.game.Field;
 import org.firstinspires.ftc.teamcode.robot.components.ArmPosition;

 public class RobotConfig {
    public static final double SERVO_INCREMENT = 0.005;
    //drive train motors
    public static final String LEFT_FRONT_DRIVE = "leftFrontDrive";
    public static final String LEFT_REAR_DRIVE = "leftRearDrive";
    public static final String RIGHT_REAR_DRIVE = "rightRearDrive";
    public static final String RIGHT_FRONT_DRIVE = "rightFrontDrive";
    public static final String WEBCAM_ID = "Webcam 1";
    public static final String BLINKIN = "blinkin";

    public static final String SHOULDER = "shoulder";
    public static final String ELBOW = "elbow";
    public static final String ROTATOR = "rotator";
    public static final String BUCKET = "bucket";
    public static final String SORTER = "sorter";

    public static final String MINIARM = "miniArm";

    public static final String DRONE_LAUNCHER = "droneLauncher";

    public static final double WRIST_STARTING_POSITION = .9;
    public static final double WRIST_INTAKE_POSITION = .795;
    public static final double WRIST_DUMP_POSITION = 0.5;

    public static final double ROTATOR_INITIAL_POSITION = 0.24;
    public static final double ROTATOR_TURNED_OVER_POSITION = 1.0;

    public static final double SORTER_LEFT_POSITION = 0.66;
    public static final double SORTER_RIGHT_POSITION = .24;

    public static final double TRIGGER_INITIAL_POSITION = 1;
    public static final double TRIGGER_RELEASE_POSITION = 0.3;
    public static final double TRIGGER_INCREMENT = 0.01;

    public static final double MINIARM_INITIAL_POSITION = 0.5;

    public static final double MINIARM_DROP_POSITION = 0.9;

    public static final double CAUTIOUS_SPEED = 0.25;

    //Robot center from back is five and half inches away
    public static double ROBOT_CENTER_FROM_BACK = 5.5 * Field.MM_PER_INCH;

    //Robot center from held cone is three inches farther than the rear of the robot
    public static double ROBOT_CENTER_FROM_HELD_CONE = ROBOT_CENTER_FROM_BACK + 3*Field.MM_PER_INCH;
    //Robot center from front is four and a half inches
    public static double ROBOT_CENTER_FROM_FRONT = 4.5 * Field.MM_PER_INCH;
    public static final double ROBOT_WIDTH = 14.5 * Field.MM_PER_INCH;

    public static final double ROBOT_LENGTH = ROBOT_CENTER_FROM_BACK + ROBOT_CENTER_FROM_FRONT;
    public static final double ALLOWED_BEARING_ERROR = 0.75;
    public static final double ALLOWED_POSITIONAL_ERROR = .25;

    public static final long SERVO_REQUIRED_TIME = 500; //500 milli-seconds for servo to function

    public static final int ACCEPTABLE_ELBOW_ERROR = 10;
    public static final double MAX_ELBOW_POWER = 1;
    public static final int ELBOW_INCREMENT = 2;

    public static final int ACCEPTABLE_SHOULDER_ERROR = 10;
    public static final double MAX_SHOULDER_POWER = 1.0;
    public static final int SHOULDER_INCREMENT = 10;

    public static final ArmPosition ARM_STARTING_POSITION = new ArmPosition(0, 0, ROTATOR_INITIAL_POSITION, WRIST_STARTING_POSITION, SORTER_LEFT_POSITION);
    public static final ArmPosition ARM_PICKUP_POSITION = new ArmPosition(0, 0, ROTATOR_INITIAL_POSITION, WRIST_INTAKE_POSITION, SORTER_LEFT_POSITION);
    public static final ArmPosition ARM_RELEASE_POSITION_1 = new ArmPosition(400, 0, ROTATOR_INITIAL_POSITION, .24, SORTER_LEFT_POSITION);
    public static final ArmPosition ARM_RELEASE_POSITION_2 = new ArmPosition(1500, -200, ROTATOR_INITIAL_POSITION, .24, SORTER_LEFT_POSITION);
    public static final ArmPosition ARM_RELEASE_POSITION_3 = new ArmPosition(1500, -200, ROTATOR_INITIAL_POSITION, .24, SORTER_LEFT_POSITION);
    public static final ArmPosition ARM_TRAVEL_POSITION = new ArmPosition(1500, 1200, ROTATOR_INITIAL_POSITION, .24, SORTER_LEFT_POSITION);
    public static final ArmPosition ARM_DEPOSIT_POSITION_1 = new ArmPosition(1500, 1000, ROTATOR_TURNED_OVER_POSITION, WRIST_DUMP_POSITION, SORTER_LEFT_POSITION);
    public static final ArmPosition ARM_DEPOSIT_POSITION_2 = new ArmPosition(1000, 1000, ROTATOR_TURNED_OVER_POSITION, WRIST_DUMP_POSITION, SORTER_LEFT_POSITION);

    public static final String INOUT_TAKE_MOTOR = "inoutMotor";
    public static final double MAX_INOUT_SPEED = 1.0;

    public static final int X_PIXEL_COUNT = 2304;
    public static final int Y_PIXEL_COUNT = 1536;

    public static final double IN_OUT_TAKE_IN_POWER = -1;
    public static final double IN_OUT_TAKE_OUT_POWER = 1;
    public static final double IN_OUT_TAKE_STOP_POWER = 0;
 }
