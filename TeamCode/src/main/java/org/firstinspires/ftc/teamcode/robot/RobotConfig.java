 package org.firstinspires.ftc.teamcode.robot;

 import org.firstinspires.ftc.teamcode.game.Field;
 import org.firstinspires.ftc.teamcode.robot.components.ArmState;

 public class RobotConfig {
    public static final double SERVO_INCREMENT = 0.005;
    //drive train motors
    public static final String LEFT_FRONT_DRIVE = "leftFrontDrive";
    public static final String LEFT_REAR_DRIVE = "leftRearDrive";
    public static final String RIGHT_REAR_DRIVE = "rightRearDrive";
    public static final String RIGHT_FRONT_DRIVE = "rightFrontDrive";
    public static final String WEBCAM_ID = "Webcam 1";
    public static final String BLINKIN = "blinkin";
    public static final String WHITE_LED = "whiteLED";

    public static final String SHOULDER = "shoulder";
    public static final String ELBOW = "elbow";
    public static final String INOUT_MOTOR = "inOutMotor";
    public static final String ROTATOR = "rotator";
    public static final String BUCKET = "wrist";
    public static final String SORTER = "sorter";
    public static final String SHOULDER_LIMIT_SWITCH = "shoulderLimitSwitch";
    public static final String ELBOW_LIMIT_SWITCH = "elbowLimitSwitch";


    public static final String MINIARM = "miniArm";

    public static final String DRONE_HOLDER = "droneHolder";
    public static final String DRONE_LOADER = "droneLoader";

    public static final String COLOR_SENSOR = "colorSensor";
    public static final double WRIST_STARTING_POSITION = .9;
    public static final double WRIST_DEPOSIT_POSITION_1 = .929;
    public static final double WRIST_DEPOSIT_POSITION_3 = .45;
    public static final double WRIST_INTAKE_POSITION = .72;
    public static final double WRIST_INTERIM_TRAVEL_POSITION = .699;
    public static final double WRIST_TRAVEL_POSITION = .644;
    public static final double WRIST_DUMP_POSITION = 0.435;
    public static final double WRIST_AUTO_DUMP_POSITION = .318;
    public static final double ROTATOR_STARTING_POSITION = .80;
    public static final double ROTATOR_INTAKE_POSITION = .925;

    public static final double ROTATOR_TURNED_OVER_POSITION = .269;

    public static final double DRONE_HOLDER_HOLD_POSITION = .57;
    public static final double DRONE_TRIGGER_RELEASE_POSITION = 0.7;
    public static final double DRONE_LOADER_INITIAL_POSITION = .638;
    public static final double DRONE_LOADER_TRAVEL_POSITION = 0.69;
    public static final double DRONE_LOADER_SHOOTING_POSITION = 0.608;
    public static final double TRIGGER_INCREMENT = 0.01;

    public static final double MINIARM_INITIAL_POSITION = 0.6;
    public static final double MINIARM_DROP_POSITION = .2;

    public static final double CAUTIOUS_SPEED = 0.8;
    public static final double APRIL_TAG_SPEED = 0.4;

    public static final double FIND_COLOR_SPEED = 0.35;

    //Robot center from back is five and half inches away
    public static double ROBOT_CENTER_FROM_BACK = 8 * Field.MM_PER_INCH;

    //Robot center from front is four and a half inches
    public static double ROBOT_CENTER_FROM_FRONT = 8 * Field.MM_PER_INCH;
    public static final double ROBOT_WIDTH = 14.5 * Field.MM_PER_INCH;

    public static final double ROBOT_LENGTH = ROBOT_CENTER_FROM_BACK + ROBOT_CENTER_FROM_FRONT;

    public static final long SERVO_REQUIRED_TIME = 500; //500 milli-seconds for servo to function

    public static final int ACCEPTABLE_ELBOW_ERROR = 10;
    public static final double MAX_ELBOW_POWER = 1;

    public static final int ACCEPTABLE_SHOULDER_ERROR = 10;
    public static final double MAX_SHOULDER_POWER = 0.5;

    public static final int SHOULDER_STARTING_POSITION = 0;
    public static final int SHOULDER_INTAKE_POSITION = 180;
    public static final int SHOULDER_INTERIM_TRAVEL_POSITION = 0;
    public static final int SHOULDER_TRAVEL_POSITION = 140;
    public static final int SHOULDER_DEPOSIT_POSITION = 1630;
    public static final int SHOULDER_AUTO_DEPOSIT_POSITION = 1200;

    public static final int ELBOW_STARTING_POSITION = 0;
    public static final int ELBOW_INTAKE_POSITION = -1920;
    public static final int ELBOW_INTERIM_TRAVEL_POSITION = -1646;
    public static final int ELBOW_DEPOSIT_POSITION = -2365;



    public static final ArmState ARM_STARTING_POSITION = new ArmState(
            SHOULDER_STARTING_POSITION,
            ELBOW_STARTING_POSITION,
            ROTATOR_STARTING_POSITION,
            WRIST_STARTING_POSITION,
            false);
    public static final ArmState ARM_INTAKE_POSITION = new ArmState(
            643,
            -2840,
            .930,
            .598,
            true);
    public static final ArmState ARM_DEPOSIT_POSITION_1 = new ArmState(
            630,
            -1372,
            0,
            .304,
            true);
    public static final ArmState ARM_DEPOSIT_POSITION_1_BACKWARDS = new ArmState(
            1940,
            -1475,
            0.74,
            .349,
            true);
    public static final ArmState ARM_DEPOSIT_POSITION_2_BACKWARDS = new ArmState(
            2090,
            -1899,
            0.74,
            .243,
            true);
    public static final ArmState ARM_DEPOSIT_POSITION_2 = new ArmState(
            1230,
            -1677,
            0,
            .269,
            true);
    public static final ArmState ARM_DEPOSIT_POSITION_3 = new ArmState(
            SHOULDER_DEPOSIT_POSITION,
            ELBOW_DEPOSIT_POSITION,
            ROTATOR_TURNED_OVER_POSITION,
            WRIST_DEPOSIT_POSITION_3,
            true);
    public static final ArmState ARM_EXTENDED_TRAVEL_POSITION = new ArmState(
            320,
            -1900,
            .93,
            .598,
            true);
    public static final ArmState ARM_UNDER_RIGGING_TRAVEL_POSITION = new ArmState(
            330,
            0,
            0.765,
            .459,
            true);
    public static final ArmState ARM_RAISED_POSITION = new ArmState(
            140,
            -620,
            ROTATOR_STARTING_POSITION,
            WRIST_TRAVEL_POSITION,
            false);
    public static final ArmState ARM_STAGE_DOOR_TRAVEL_POSITION = new ArmState(
            770,
            0,
            0.765,
            .459,
            true);

    public static final ArmState ARM_AUTO_DEPOSIT_POSITION = new ArmState(
            360,
            -1526,
            0,
            .329,
            true);

    public static final ArmState ARM_PRE_HANG_POSITION = new ArmState(
            1880,
            -2000,
            0,
            .324,
            false);


    public static final ArmState ARM_HANG_POSITION_1 = new ArmState(
            2300,
            -50,
            0,
            .324,
            false);
    public static final ArmState ARM_HANG_POSITION_2 = new ArmState(
            0,
            -50,
            0,
            .324,
            false);

    public static final int X_PIXEL_COUNT = 1920;
    public static final int Y_PIXEL_COUNT = 1080;
 }
