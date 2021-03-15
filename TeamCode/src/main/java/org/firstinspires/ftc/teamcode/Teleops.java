package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

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

    double high = 20;
    double shot = 0.96;



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
        flicker = hardwareMap.servo.get("pusher");
        claw = hardwareMap.servo.get("clamp");
        stack = hardwareMap.servo.get("stack");
        shooterLeft = hardwareMap.dcMotor.get("flywheelleft");
        shooterRight = hardwareMap.dcMotor.get("flywheelright");
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);


        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        nitro = 2.0;
        flicker.setPosition(0.14);
//

    }

    @Override
    public void loop() {

        telemetry.addData("Stack pos: ", stack.getPosition());
        telemetry.update();

        telemetry.addData("Nitro: ", (nitro == 1) ? "High":( (nitro==2) ? "Medium":"Low"));
        telemetry.update();

        //Intake controls
        if (gamepad1.x) {
            intake.setPower(-1);
        }
        if (gamepad1.b) {
            intake.setPower(-0.75);
        }
        if (gamepad1.y) {
            intake.setPower(0);
        }
        if (gamepad1.a) {
            intake.setPower(1);
        }

        //Drive controls
        drive = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        double frontLeftPower = drive + strafe - rotate;
        double backLeftPower = drive - strafe - rotate;
        double frontRightPower = drive - strafe + rotate;
        double backRightPower = drive + strafe + rotate;

        frontLeft.setPower(frontLeftPower/nitro);
        backLeft.setPower(backLeftPower/nitro);
        frontRight.setPower(frontRightPower/nitro);
        backRight.setPower(backRightPower/nitro);

        //Nitro controls
        if (gamepad1.a){
            nitro = 1;
        }
        else if (gamepad1.b){
            nitro = 2;
        }

        if (gamepad1.left_trigger > 0){
            nitro = 4;
        }else if (gamepad1.right_trigger > 0) {
            nitro = 1;
        }
        else{
            nitro = 2;
        }

        //Wobble Arm controls
        if (gamepad2.right_bumper){
            wobble.setTargetPosition(initPos-700);
            wobble.setPower(-0.3);
            wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        if (gamepad2.left_bumper){
            wobble.setTargetPosition(initPos);
            wobble.setPower(0.3);
            wobble.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        }

        wobble.getCurrentPosition();
        telemetry.addData("Position: ", wobble.getCurrentPosition());

        //Claw controls
        if (gamepad2.x)
            //close
            claw.setPosition(0.88);
        if (gamepad2.y)
            //open
            claw.setPosition(0.5);

        //fly wheel power controls
        if (gamepad1.left_bumper)
        {
            shooterLeft.setPower(0);
            shooterRight.setPower(0);
        }
        if (gamepad1.right_bumper)
        {
            shooterLeft.setPower(high);
            shooterRight.setPower(-high);
        }
        if (gamepad1.dpad_right)
        {
            shooterLeft.setPower(0.93);
            shooterRight.setPower(-0.93);
        }
        if (gamepad1.dpad_left)
        {
            shooterLeft.setPower(shot);
            shooterRight.setPower(-shot);
        }
        if (gamepad1.dpad_up)
        {
            shooterLeft.setPower(0.88);
            shooterRight.setPower(-0.88);
        }if (gamepad1.dpad_down)
        {
            shooterLeft.setPower(0.97);
            shooterRight.setPower(-0.97);
        }

        //flicker servo controls
        if (gamepad2.right_trigger > 0) {
            flicker.setPosition(0.65);
        }
        else {
            flicker.setPosition(0.11);
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