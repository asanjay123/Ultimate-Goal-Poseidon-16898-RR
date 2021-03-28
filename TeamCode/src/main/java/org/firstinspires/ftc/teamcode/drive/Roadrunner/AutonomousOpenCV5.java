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

package org.firstinspires.ftc.teamcode.drive.Roadrunner;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "Power Shot Auto")
public class AutonomousOpenCV5 extends LinearOpMode
{
    OpenCvInternalCamera phoneCam;
    SkystoneDeterminationPipeline pipeline;

    DcMotor intake;
    DcMotor wobble;
    DcMotor shooterLeft;
    DcMotor shooterRight;

    Servo flicker;
    Servo claw;
    Servo stack;

    int initPos;

    double power = 20;
    double shot = 0.9;

    /*     pi
           |
  3pi/2  ----- pi/2
           |
           0


     */

    @Override
    public void runOpMode()
    {

        //Scan rings
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        pipeline = new SkystoneDeterminationPipeline();
        phoneCam.setPipeline(pipeline);

        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though.
        phoneCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                phoneCam.startStreaming(320,240, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }
        });

        while (opModeIsActive())
        {
            telemetry.addData("Analysis", pipeline.getAnalysis());
            telemetry.addData("Position", pipeline.position);
            telemetry.update();

            // Don't burn CPU cycles busy-looping in this sample
            sleep(50);
        }

        initMotors();
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        claw.setPosition(0.88);
        flicker.setPosition(0.11);

        telemetry.addLine("Ready@");
        telemetry.update();
        waitForStart();


        drive.setPoseEstimate(new Pose2d(-60, -16, 0));

            shooterLeft.setPower(power);
            shooterRight.setPower(-power);



        //Go forward
        drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(-60, 16, 0))
                        .forward(30)
                        .build()
        );

        drive.turn(-Math.toRadians(15));

        flicker.setPosition(0.4);
        sleep(300);
        flicker.setPosition(0.11);
        sleep(300);

        flicker.setPosition(0.4);
        sleep(300);
        flicker.setPosition(0.11);
        sleep(300);

        flicker.setPosition(0.4);
        sleep(300);
        flicker.setPosition(0.11);
        sleep(300);
        drive.turn(Math.toRadians(15));


        shooterLeft.setPower(0.25);
        shooterRight.setPower(0.25);

        //turn
        drive.turn(Math.toRadians(180));

        //Turn on intake
        intake.setPower(-20);
        drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(-45, 16, 3.14))
                        .strafeLeft(7)
                        .build()
        );


        //Move into ring on the way to wobble
        drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(-60, 16, 3.14))
                        .back(80)
                        .build()
        );

        drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(-45, 16, 3.14))
                        .strafeLeft(15)
                        .build()
        );

        //Turn off intake
        intake.setPower(0);


        //Drop wobble
        wobble.setTargetPosition(initPos-670);
        wobble.setPower(-0.45);
        wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        sleep(200);

        while(wobble.isBusy() && opModeIsActive())
        {
            opModeIsActive();
        }

        claw.setPosition(0.5);
        sleep(300);

        //Move to shooting position
        drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(20, 16, 3.14))
                        .forward(45)
                        .build()
        );
        drive.turn(Math.toRadians(167));

        //Shoot ring
        shooterLeft.setPower(shot);
        shooterRight.setPower(-shot);
        sleep(1000);

        sleep(400);
        flicker.setPosition(0.65);
        sleep(300);
        flicker.setPosition(0.11);
        sleep(300);
        shooterLeft.setPower(0);
        shooterRight.setPower(0);

        //Drive to second wobble
//        drive.followTrajectory(
//                drive.trajectoryBuilder(new Pose2d(0, 16, 0))
//                        .strafeRight(20)
//                        .build()
//        );

        drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(0, 36, 0))
                        .back(18)
                        .build()
        );

        //pick up second wobble
        claw.setPosition(0.88);

        //drop off second wobble
        drive.followTrajectory(
                drive.trajectoryBuilder(new Pose2d(-20, -36, 0))
                        .splineToLinearHeading(new Pose2d(40, -20, 0), 0.0)
                        .build()

        );


        drive.turn(-Math.toRadians(140));
        claw.setPosition(0.5);
        sleep(500);

        drive.turn(Math.toRadians(5));

        wobble.setTargetPosition(initPos);
        wobble.setPower(0.45);
        wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while(wobble.isBusy() && opModeIsActive())
        {
            claw.setPosition(0.88);
            opModeIsActive();
        }
        //TODO Position C----------------------------------------------------------------------------------------

//        //Shoot rings
//        drive.setPoseEstimate(new Pose2d(-60, -16, 0));
//
//        shooterLeft.setPower(power);
//        shooterRight.setPower(-power);
//
//
//
//        //Go forward
//        drive.followTrajectory(
//                drive.trajectoryBuilder(new Pose2d(-60, 16, 0))
//                        .forward(45)
//                        .build()
//        );
//
//        drive.turn(-Math.toRadians(13));
//
//        flicker.setPosition(0.4);
//        sleep(300);
//        flicker.setPosition(0.11);
//        sleep(300);
//
//        flicker.setPosition(0.4);
//        sleep(300);
//        flicker.setPosition(0.11);
//        sleep(300);
//
//        flicker.setPosition(0.4);
//        sleep(300);
//        flicker.setPosition(0.11);
//        sleep(300);
//
//
//        shooterLeft.setPower(0);
//        shooterRight.setPower(0);
//
//
//        drive.followTrajectory(
//                drive.trajectoryBuilder(new Pose2d(-45, 16, 3.14))
//                        .forward(100)
//                        .build()
//        );
//
//        drive.turn(-Math.toRadians(105));
//
//
//        //Move into ring on the way to wobble
//        drive.followTrajectory(
//                drive.trajectoryBuilder(new Pose2d(-60, 16, 3.14))
//                        .back(7)
//                        .build()
//        );
//
//        //Drop wobble
//        wobble.setTargetPosition(initPos-670);
//        wobble.setPower(-0.45);
//        wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        sleep(200);
//
//        while(wobble.isBusy() && opModeIsActive())
//        {
//            opModeIsActive();
//        }
//
//        claw.setPosition(0.5);
//        sleep(300);
//
//        //Move to second wobble
//        drive.followTrajectory(
//                drive.trajectoryBuilder(new Pose2d(20, 16, 3.14))
//                        .forward(7)
//                        .build()
//        );
//        drive.turn(Math.toRadians(95));
//
//        drive.followTrajectory(
//                drive.trajectoryBuilder(new Pose2d(-60, 16, 3.14))
//                        .back(85)
//                        .build()
//        );
//
//        //pick up second wobble
//        claw.setPosition(0.88);
//
//        //drop off second wobble
//        drive.followTrajectory(
//                drive.trajectoryBuilder(new Pose2d(-20, -36, 0))
//                        .splineToLinearHeading(new Pose2d(80, -30, 0), 0.0)
//                        .build()
//
//        );
//
//
//        drive.turn(-Math.toRadians(90));
//        claw.setPosition(0.5);
//        sleep(500);
//
//        drive.turn(Math.toRadians(5));
//
//        wobble.setTargetPosition(initPos);
//        wobble.setPower(0.45);
//        wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        while(wobble.isBusy() && opModeIsActive())
//        {
//            claw.setPosition(0.88);
//            opModeIsActive();
//        }
//
//        drive.followTrajectory(
//                drive.trajectoryBuilder(new Pose2d(-60, 16, 3.14))
//                        .strafeLeft(70)
//                        .build()
//        );

//TODO: POSITION A---------------------------------------------------------------------------------------------------------------------------------------

//        if (pipeline.position == SkystoneDeterminationPipeline.RingPosition.NONE) {
//
//            drive.setPoseEstimate(new Pose2d(-60, -15, 0));
//
//            shooterLeft.setPower(power);
//            shooterRight.setPower(-power);
//
//            //Drive towards power shot
//            drive.followTrajectory(
//                    drive.trajectoryBuilder(new Pose2d(-60, -15, 0))
//                            .strafeLeft(48)
//                            .build()
//            );
//
//            //correct shift from the strafe
//            drive.turn(-Math.toRadians(21));
//
//            //drive forward
//            drive.followTrajectory(
//                    drive.trajectoryBuilder(new Pose2d(-60, 38, 0))
//                            .forward(65)
//                            .build()
//            );
//
//            //shoot middle
//            flicker.setPosition(0.4);
//            sleep(350);
//            flicker.setPosition(0.11);
//            sleep(300);
//            stack.setPosition(0.7);
//            sleep(300);
//            stack.setPosition(0.97);
//            sleep(300);
//
//            //shoot left
//            drive.turn(-Math.toRadians(20));
//            flicker.setPosition(0.4);
//            sleep(350);
//            flicker.setPosition(0.11);
//            sleep(300);
//            stack.setPosition(0.7);
//            sleep(300);
//            stack.setPosition(0.97);
//            sleep(300);
//
//            //shoot right
//            drive.turn(Math.toRadians(25));
//            flicker.setPosition(0.4);
//            sleep(350);
//            flicker.setPosition(0.11);
//            sleep(300);
//
//            drive.followTrajectory(
//                    drive.trajectoryBuilder(new Pose2d(31, 38, 0))
//                            .forward(20)
//                            .build()
//            );
//
//            //go to target zone A
//            drive.turn(-Math.toRadians(105));
//
//            shooterLeft.setPower(0);
//            shooterRight.setPower(0);
//
//            drive.followTrajectory(
//                    drive.trajectoryBuilder(new Pose2d(10, 38, 0))
//                            .back(36)
//                            .build()
//            );
//
//            //drop wobble
//            wobble.setTargetPosition(initPos-650);
//            wobble.setPower(-0.45);
//            wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            sleep(200);
//
//            while(wobble.isBusy() && opModeIsActive())
//            {
//                opModeIsActive();
//            }
//
//            claw.setPosition(0.5);
//
//            //pick up second wobble
//
//            drive.turn(Math.toRadians(100));
//
//            drive.followTrajectory(
//                    drive.trajectoryBuilder(new Pose2d(10, 38, 0))
//                            .back(40)
//                            .build()
//            );
//
//            claw.setPosition(0.88);
//
//            drive.followTrajectory(
//                    drive.trajectoryBuilder(new Pose2d(-40, 16, 0))
//                            .forward(52)
//                            .build()
//            );
//
//            drive.turn(-Math.toRadians(80));
//            claw.setPosition(0.5);
//            sleep(500);
//
//            drive.turn(Math.toRadians(5));
//
//            wobble.setTargetPosition(initPos);
//            wobble.setPower(0.45);
//            wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            while(wobble.isBusy() && opModeIsActive())
//            {
//                claw.setPosition(0.88);
//                opModeIsActive();
//            }

//
//        }

//        //TODO: POSITION B---------------------------------------------------------------------------------------------------------
//
//        else if (pipeline.position == SkystoneDeterminationPipeline.RingPosition.ONE) {
//
//Shoot rings
//

//        }
//
//
//        else if (pipeline.position == SkystoneDeterminationPipeline.RingPosition.FOUR) {
//
//            //Drive to Position C
//            drive.followTrajectory(
//                    drive.trajectoryBuilder(new Pose2d(62, 22, 0))
//                            .splineToLinearHeading(new Pose2d(-45,80,0), 0.0)
//                            .build()
//            );
//
//            drive.followTrajectory(
//                    drive.trajectoryBuilder(new Pose2d(62, 22, 0))
//                            .strafeLeft(60)
//                            .build()
//            );
//            drive.followTrajectory(
//                    drive.trajectoryBuilder(new Pose2d(62, 22, 0))
//                            .back(90)
//                            .build()
//            );
//            //Extend wobble
//            wobble.setTargetPosition(initPos-650);
//            wobble.setPower(0.35);
//            wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            while(wobble.isBusy() && opModeIsActive())
//            {
//
//                opModeIsActive();
//            }
//
//
//            //Release wobble
//            sleep(100);
//            claw.setPosition(0.5);
//            sleep(200);
//
//            //Retract arm
//            wobble.setTargetPosition(initPos);
//            wobble.setPower(0.45);
//            wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//            while(wobble.isBusy() && opModeIsActive())
//            {
//                claw.setPosition(0.88);
//                opModeIsActive();
//            }
//
//
//            //Drive to shooting position
//            drive.followTrajectory(
//                    drive.trajectoryBuilder(new Pose2d(-45, 75, 0))
//                            .splineToLinearHeading(new Pose2d(0, 45, 0), 0.0)
//                            .build()
//            );
//
//
//            drive.turn(Math.toRadians(184));
//
//            //Shooting rings
//            shooterLeft.setPower(power);
//            shooterRight.setPower(power);
//
//            flicker.setPosition(0.65);
//            sleep(900);
//            flicker.setPosition(0.11);
//            sleep(800);
//
//            flicker.setPosition(0.65);
//            sleep(900);
//            flicker.setPosition(0.11);
//            sleep(900);
//
//            flicker.setPosition(0.65);
//            sleep(900);
//            flicker.setPosition(0.11);
//
//            shooterLeft.setPower(0);
//            shooterRight.setPower(0);
//
//
//            //Extending arm
//            wobble.setTargetPosition(initPos-650);
//            wobble.setPower(0.3);
//            wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//            //Driving to second wobble
//            drive.followTrajectory(
//                    drive.trajectoryBuilder(new Pose2d(0, 40, Math.PI))
//                            .splineToLinearHeading(new Pose2d(25,-2, Math.PI), 0.0)
//                            .build()
//
//            );
//
//            drive.setPoseEstimate(new Pose2d(25, -2, Math.PI));
//
//            //Open claw
//            claw.setPosition(0.5);
//            while(wobble.isBusy() && opModeIsActive())
//            {
//                opModeIsActive();
//            }
//
//            sleep(50);
//
//            drive.followTrajectory(
//                    drive.trajectoryBuilder(new Pose2d(25, 8, Math.PI))
//                            .back(15)
//                            .build()
//
//            );
//
//            //Grab wobble
//            claw.setPosition(0.88);
//
//            drive.followTrajectory(
//                    drive.trajectoryBuilder(new Pose2d(15, -6, Math.PI))
//                            .splineToLinearHeading(new Pose2d(-75,40, Math.PI), 0.0)
//                            .build()
//
//            );
//
//            drive.turn(-Math.toRadians(110));
//            claw.setPosition(0.5);
//
//
//        }





    }

    //TODO: Open CV Object Detection Code
    //TODO: DO NOT TOUCH THIS CODE
    public static class SkystoneDeterminationPipeline extends OpenCvPipeline
    {
        /*
         * An enum to define the skystone position
         */
        public enum RingPosition
        {
            FOUR,
            ONE,
            NONE
        }

        /*
         * Some color constants
         */
        static final Scalar BLUE = new Scalar(0, 0, 255);
        static final Scalar GREEN = new Scalar(0, 255, 0);

        /*
         * The core values which define the location and size of the sample regions
         */
        static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(130,70);

        static final int REGION_WIDTH = 30;
        static final int REGION_HEIGHT = 40;

        final int FOUR_RING_THRESHOLD = 150;
        final int ONE_RING_THRESHOLD = 135;

        Point region1_pointA = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x,
                REGION1_TOPLEFT_ANCHOR_POINT.y);
        Point region1_pointB = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

        /*
         * Working variables
         */
        Mat region1_Cb;
        Mat YCrCb = new Mat();
        Mat Cb = new Mat();
        int avg1;

        // Volatile since accessed by OpMode thread w/o synchronization
        private volatile RingPosition position = RingPosition.FOUR;

        /*
         * This function takes the RGB frame, converts to YCrCb,
         * and extracts the Cb channel to the 'Cb' variable
         */
        void inputToCb(Mat input)
        {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, 1);
        }

        @Override
        public void init(Mat firstFrame)
        {
            inputToCb(firstFrame);

            region1_Cb = Cb.submat(new Rect(region1_pointA, region1_pointB));
        }

        @Override
        public Mat processFrame(Mat input)
        {
            inputToCb(input);

            avg1 = (int) Core.mean(region1_Cb).val[0];

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    2); // Thickness of the rectangle lines

            position = RingPosition.FOUR; // Record our analysis
            if(avg1 > FOUR_RING_THRESHOLD){
                position = RingPosition.FOUR;
            }else if (avg1 > ONE_RING_THRESHOLD){
                position = RingPosition.ONE;
            }else{
                position = RingPosition.NONE;
            }

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill

            return input;
        }

        public int getAnalysis()
        {
            return avg1;
        }
    }

    //TODO: INITIALIZATION OF MOTORS AND SERVOS
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