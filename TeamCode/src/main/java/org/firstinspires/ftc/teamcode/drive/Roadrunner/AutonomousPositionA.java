package org.firstinspires.ftc.teamcode.drive.Roadrunner;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Autonomous(name = "Auto Foundation Left Roadrunner w/ Coordinate (BLue)")
public class AutonomousPositionA extends LinearOpMode{
    Servo servo;
    Servo servo1;
    Servo hookRight;
    Servo hookLeft;

    @Override
    public void runOpMode(){
        initMotors();

//        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
//        telemetry.addLine("Ready@");
//        telemetry.update();
//        waitForStart();
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
        servo = hardwareMap.servo.get("servo");
        servo1 = hardwareMap.servo.get("servo1");
        hookRight = hardwareMap.servo.get("hookRight");
        hookLeft = hardwareMap.servo.get("hookLeft");

        servo.setPosition(0.9);
        servo1.setPosition(0.5);

    }
}

