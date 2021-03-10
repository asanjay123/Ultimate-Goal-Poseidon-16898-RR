package org.firstinspires.ftc.teamcode.drive.Roadrunner;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Autonomous(name = "Autonomous Position A")
@Disabled
public class AutonomousPositionA extends LinearOpMode{


    DcMotor intake;
    DcMotor wobble;
    DcMotor shooterLeft;
    DcMotor shooterRight;

    Servo flicker;
    Servo claw;
    Servo stack;

    int ringCount = 0; // 0, 1, 3

    int initPos;

    /*     pi
           |
  3pi/2  ----- pi/2
           |
           0


     */

    @Override
    public void runOpMode(){
        initMotors();
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        claw.setPosition(0.88);
        flicker.setPosition(0.11);

        telemetry.addLine("Ready@");
        telemetry.update();

        waitForStart();

        //Scan rings

        //Move to box


        drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(62, 22, 0))
                        .splineToLinearHeading(new Pose2d(35,40,0), 0.0)
                        .splineToLinearHeading(new Pose2d(-10,52,0), 0.0)
                        .build()
        );

        /*drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(60.5, 68, 0))
                        .splineToLinearHeading(new Pose2d(-10,68,0), 0.0)
                        .build()
        );*/

        drive.turn(Math.toRadians(60));

        sleep(200);
        wobble.setTargetPosition(initPos-650);
        wobble.setPower(0.3);
        wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while(wobble.isBusy())
        {
            opModeIsActive();
        }

        claw.setPosition(0.5);

        drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(-10, 62, 0))
                        .forward(10)
                        .build()
        );

        sleep(300);
        claw.setPosition(0.88);

        drive.turn(-Math.toRadians(48));

        sleep(200);

        wobble.setTargetPosition(initPos);
        wobble.setPower(-0.3);
        wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while(wobble.isBusy())
        {
            opModeIsActive();

        }

        sleep(200);

        drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(0, 62, 0))
                        .splineToLinearHeading(new Pose2d(-2,-6,0), 0.0)
                        .build()

        );

        drive.turn(Math.toRadians(180));

        /*shooterLeft.setPower(0.87);
        shooterRight.setPower(-0.87);

        flicker.setPosition(0.65);
        sleep(1500);
        flicker.setPosition(0.11);
        sleep(1000);

        flicker.setPosition(0.65);
        sleep(1000);
        flicker.setPosition(0.11);
        sleep(1000);

        flicker.setPosition(0.65);
        sleep(1000);
        flicker.setPosition(0.11); */

        sleep(200);

        drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(-2, -6, Math.PI))
                        .splineToLinearHeading(new Pose2d(20,-6, Math.PI), 0.0)
                        .build()

        );

        wobble.setTargetPosition(initPos-650);
        wobble.setPower(0.3);
        wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while(wobble.isBusy())
        {
            opModeIsActive();
        }

        claw.setPosition(0.5);

        sleep(200);

        drive.turn(Math.toRadians(20));

        drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(-2, -6, Math.PI))
                        .splineToLinearHeading(new Pose2d(22,-6, Math.PI), 0.0)
                        .build()

        );

        claw.setPosition(0.88);

        sleep(200);

        drive.turn(-Math.toRadians(17));

        sleep(200);

        drive.turn(Math.toRadians(180));

        drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(22, -6, Math.PI))
                        .splineToLinearHeading(new Pose2d(10,20, Math.PI), 0.0)
                        .build()

        );

        drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(10,20, Math.PI))
                        .splineToLinearHeading(new Pose2d(0,40, 0), 0.0)
                        .build()

        );

        /*drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(0,40, 0))
                        .splineToLinearHeading(new Pose2d(-10,68, 0), 0.0)
                        .build()

        ); */









        if (ringCount == 0)
        {


        }

        /*else if (ringCount == 1)
        {

        }
        else
        {

        } */

        //Wobble extension
        /*wobble.setTargetPosition(initPos-700);
        wobble.setPower(0.1);
        wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        claw.setPosition(0.88);
        wobble.setTargetPosition(initPos);
        wobble.setPower(-0.1);
        wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION); */

        //Move out of the box

        //Close claw and move arm back up
        claw.setPosition(0.88);
        wobble.setTargetPosition(initPos);

        //Move to shooting position

        //Shoot the three rings

        //Move to stack of rings

        //Collect stack of rings

        //Move to shooting position

        //Shoot the rings

        //Park at the middle line



//
//
//
//        drive.setPoseEstimate(new Pose2d(36, 63, Math.PI/2));
//
//
//        drive.followTrajectory(
//                drive.trajectoryBuilder()
//                        .forward(10)
//                        .strafeLeft(25)
//                        .forward(15)
//                        .build()
//        );
//        hookRight.setPosition(0.26);
//        hookLeft.setPosition(0.56);
//
//        drive.followTrajectory(
//                drive.trajectoryBuilder()
//                        .forward(4)
//                        .build()
//        );
//
//
//        drive.followTrajectory(
//                drive.trajectoryBuilder()
////                        .reverse()
//                        .back(40)
//                        .build()
//        );
//
//        drive.turn(Math.toRadians(80));
//
//        drive.setMotorPowers(1, 1, 1, 1);
//        sleep(1000);
//        drive.setMotorPowers(0,0,0,0);
//
//        sleep(200);
//        hookRight.setPosition(0.6);
//        hookLeft.setPosition(0.22);
//        sleep(200);
//
//        servo.setPosition(.17);
//
//        drive.followTrajectory(
//                drive.trajectoryBuilder()
//                        .back(1)
//                        .build()
//        );
//
//        sleep(100);
//        drive.followTrajectory(
//                drive.trajectoryBuilder()
//                        .strafeLeft(30)
//                        .build()
//        );
//
//        sleep(100);
//        drive.followTrajectory(
//                drive.trajectoryBuilder()
//                        .back(20)
//                        .build()
//        );
//


    }



    public void initMotors(){
        intake = hardwareMap.dcMotor.get("ring");
        wobble = hardwareMap.dcMotor.get("wobble");
        initPos = wobble.getCurrentPosition();
        wobble.getCurrentPosition();
        telemetry.addData("Position: ", wobble.getCurrentPosition());
        flicker = hardwareMap.servo.get("pusher");
        claw = hardwareMap.servo.get("clamp");
        stack = hardwareMap.servo.get("stack");
        shooterLeft = hardwareMap.dcMotor.get("flywheelleft");
        shooterRight = hardwareMap.dcMotor.get("flywheelright");

    }
}

