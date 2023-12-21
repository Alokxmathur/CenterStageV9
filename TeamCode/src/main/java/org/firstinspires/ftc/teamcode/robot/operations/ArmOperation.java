package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.components.Arm;

import java.util.Locale;

/**
 * This class implements oll of the operations related to managing the arm.
 * The types of operations permitted are:
 *  Open - opens the claw to release cone
 *  Close - closes the claw to grip cone
 *  Ground - hold cone at ground junction level
 *  Low - hold cone at low junction level
 *  Mid - hold cone at mid junction level
 *  High - hold cone at high junction level
 *  Pickup - get claw to level to pickup upright cone on the ground
 *  Stack5 - get claw to level to pickup the top (5'th) cone in the stack
 *  Stack4 - get claw to level to pickup the 4'th cone in the stack
 *  Stack3 - get claw to level to pickup the 3'rd cone in the stack
 *  Stack2 - get claw to level to pickup the 2'nd cone in the stack
 *  Stack1 - get claw to level to pickup the bottom cone in the stack - same as ground
 */
public class ArmOperation extends Operation {

    public enum Type {
        Intake, Raised, Travel, InterimTravel, Travel_From_Deposit, Deposit1, Deposit2, Deposit3, AutoDeposit,
        PreHang, Hang1, Hang2,
        Eat, ThrowUp, Expel, Abstain
    }
    Arm arm;
    Type type;

    public ArmOperation(Type type, String title) {
        this.arm = Match.getInstance().getRobot().getArm();
        this.type = type;
        this.title = title;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "Arm: --%s",
                this.title);
    }

    public boolean isComplete() {

        switch (this.type) {
            case Intake:
            case Travel:
            case InterimTravel:
            case Deposit1:
            case Deposit2:
            case Deposit3:
            case AutoDeposit:
            case Hang1:
            case Hang2:
            case PreHang:
            case Raised:
            {
                return arm.isWithinRange();
            }
            case Expel: {
                return arm.intakeWithinRange();
            }
            default: return true;
        }
    }

    @Override
    public void startOperation() {
        switch (this.type) {
            case Intake:
            case Travel:
            case InterimTravel:
            case Deposit1:
            case Deposit2:
            case Deposit3:
            case AutoDeposit:
            case Hang1:
            case Hang2:
            case PreHang:
            case Raised:
            {
                arm.setPositions(type);
                break;
            }
            case Eat: {
                arm.eat();
                break;
            }
            case Abstain: {
                arm.abstain();
                break;
            }
            case ThrowUp: {
                arm.throwUp();
                break;
            }
            case Expel: {
                arm.expel();
                break;
            }
        }
    }

    @Override
    public void abortOperation() {
        arm.stop();
    }
}
