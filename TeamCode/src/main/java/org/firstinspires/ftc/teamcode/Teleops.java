package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Teleops", group = "")
public class Teleops extends OpMode {

    double drive;   // Power for forward and back motion
    double strafe;  // Power for left and right motion
    double rotate;  // Power for rotating the robot
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor intake;
    DcMotor wobble;
    DcMotor shooterLeft;
    DcMotor shooterRight;

    Servo flicker;
    Servo claw;
    Servo stack;
    Servo wall1;
    Servo wall2;

    double high = 1;

    ElapsedTime timer = new ElapsedTime();

    boolean moving = false;
    boolean powerShotTurn = false;

    double nitro;

    int initPos;

    @Override
    public void init(){
        frontLeft = hardwareMap.dcMotor.get("frontleft");
        backLeft = hardwareMap.dcMotor.get("backleft");
        frontRight = hardwareMap.dcMotor.get("frontright");
        backRight = hardwareMap.dcMotor.get("backright");

        intake = hardwareMap.dcMotor.get("ring");
        wobble = hardwareMap.dcMotor.get("wobble");
        initPos = wobble.getCurrentPosition();
        wobble.getCurrentPosition();
        telemetry.addData("Position: ", wobble.getCurrentPosition());
        telemetry.update();

        flicker = hardwareMap.servo.get("pusher");
        claw = hardwareMap.servo.get("clamp");
        stack = hardwareMap.servo.get("stack");
        wall1 = hardwareMap.servo.get("wall1");
        wall2 = hardwareMap.servo.get("wall2");

        shooterLeft = hardwareMap.dcMotor.get("flywheelleft");
        shooterRight = hardwareMap.dcMotor.get("flywheelright");
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);


        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        nitro = 2.0;
        flicker.setPosition(0.11);
        wall1.setPosition(0.61);
        wall2.setPosition(0.15);



    }

    public void sleep(double waitTime){
        timer.reset();
        while ((timer.milliseconds() < waitTime)) {

        }
    }

    public void motorPower (){
        double frontLeftPower = drive + strafe - rotate;
        double backLeftPower = drive - strafe - rotate;
        double frontRightPower = drive - strafe + rotate;
        double backRightPower = drive + strafe + rotate;

        frontLeft.setPower(frontLeftPower/nitro);
        backLeft.setPower(backLeftPower/nitro);
        frontRight.setPower(frontRightPower/nitro);
        backRight.setPower(backRightPower/nitro);
    }

    @Override
    public void loop() {

//        telemetry.addData("Nitro: ", (nitro == 1) ? "High":( (nitro==2) ? "Medium":"Low"));
//        telemetry.update();

        telemetry.addData("Position: ", wobble.getCurrentPosition());
        telemetry.update();

        //Intake controls
        if (gamepad1.dpad_up) {
            intake.setPower(-1);
        }
        if (gamepad1.dpad_down) {
            intake.setPower(0);
        }
        if (gamepad1.dpad_right) {
            intake.setPower(1);
        }

        //Drive controls
        if (!moving && !powerShotTurn){
            drive = gamepad1.left_stick_y;
            strafe = -gamepad1.left_stick_x;
            rotate = gamepad1.right_stick_x;
        }

        motorPower();

        //Nitro controls
        if (gamepad1.right_trigger > 0) {
            nitro = 1;
        }
        else {
            if (gamepad1.left_trigger > 0) {
                nitro = 4;
            }
            else {
                nitro = 2;
            }
        }

        //Wobble Arm controls
        if (gamepad1.right_bumper)
        {
            wobble.setTargetPosition(initPos-200);
            wobble.setPower(0.2);
            wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        if (gamepad1.left_bumper){
            wobble.setTargetPosition(initPos-15);
            wobble.setPower(-0.1);
            wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        }


        //Claw controls
        if (gamepad2.x)
            //close
            claw.setPosition(0.88);
        if (gamepad2.y)
            //open
            claw.setPosition(0.5);

        //fly wheel power controls
        if (gamepad2.left_bumper)
        {
            shooterLeft.setPower(0);
            shooterRight.setPower(0);
        }
        if (gamepad2.right_bumper)
        {
            shooterLeft.setPower(high);
            shooterRight.setPower(-high);
        }

        //flicker servo controls
        if (gamepad2.right_trigger > 0) {
            flicker.setPosition(0.35);
        }
        else {
            flicker.setPosition(0.11);
        }

//        if (gamepad2.a && !moving) {
//            moving = true;
//            for (int i = 0; i < 3; i++)
//            {
//                flicker.setPosition(0.35);
//                sleep(0.6);
//                flicker.setPosition(0.11);
//                sleep(0.6);
//
//            }
//            moving = false;
//        }

        //powershot turns
        if (gamepad2.b && !powerShotTurn)
        {
            powerShotTurn = true;
            double power = 0.8;
            double time = 180;

            //shoot middle
            flicker.setPosition(0.33);
            sleep(600);
            flicker.setPosition(0.11);
            sleep(600);

            //turn right
            rotate = power;
            motorPower();
            sleep(time/2);
            rotate = 0;
            motorPower();

            //shoot right
            flicker.setPosition(0.33);
            sleep(600);
            flicker.setPosition(0.11);
            sleep(600);

            //turn left
            rotate = -power;
            motorPower();
            sleep(time-30);
            rotate = 0;
            motorPower();

            //shoot left
            flicker.setPosition(0.33);
            sleep(600);
            flicker.setPosition(0.11);
            sleep(600);

            rotate = power;
            motorPower();
            sleep(time/2);
            rotate = 0;
            motorPower();

            powerShotTurn = false;
        }

        if (gamepad2.a && !moving)
        {
            moving = true;
            Thread flickMove = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 3; i++)
                    {
                        flicker.setPosition(0.33);
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        flicker.setPosition(0.11);
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    moving = false;
                }
            }
            );

            flickMove.start();
            flickMove.run();

        }

        //ring collection walls down
        if (gamepad2.dpad_right)
        {
            wall1.setPosition(0.15);
            wall2.setPosition(0.59);
        }

        //ring collection walls up
        if (gamepad2.dpad_left)
        {
            wall1.setPosition(0.61);
            wall2.setPosition(0.15);
        }

        //stack servo controls
        if (gamepad2.left_trigger > 0) {
            stack.setPosition(0.70);
        }
        else {
            stack.setPosition(0.97);
        }

    }
}