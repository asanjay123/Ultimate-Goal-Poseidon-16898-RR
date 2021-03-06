package org.firstinspires.ftc.teamcode.drive.Roadrunner;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
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

    int initPos;

    @Override
    public void runOpMode(){
        initMotors();

        waitForStart();

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        telemetry.addLine("Ready@");
        telemetry.update();

        //Scan rings

        //Move to box

        //Wobble extension
        wobble.setTargetPosition(initPos-700);
        claw.setPosition(0.5);

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

