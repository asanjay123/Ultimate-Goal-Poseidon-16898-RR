package org.firstinspires.ftc.teamcode.drive.Roadrunner;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Autonomous(name = "Autonomous Position A")
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

        waitForStart();

        telemetry.addLine("Ready@");
        telemetry.update();

        //Scan rings

        //Move to box


        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(60.5, 27.75, 0))
                        .splineToLinearHeading(new Pose2d(2,60,0), 0.0)
                        .build()
        );


        wobble.setTargetPosition(initPos-700);
        wobble.setPower(0.3);
        wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while(wobble.isBusy())
        {

        }

        claw.setPosition(0.5);

        drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(2, 60, 0))
                        .forward(10)
                        .build()
        );

        claw.setPosition(0.88);

        sleep(500);

        wobble.setTargetPosition(initPos);
        wobble.setPower(-0.3);
        wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while(wobble.isBusy())
        {

        }

        drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(12, 60, 0))
                        .splineToLinearHeading(new Pose2d(0,8,0), 0.0)
                        .build()

        );
        drive.turn(Math.toRadians(185));

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

