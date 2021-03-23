package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Flicker Test", group = "")
public class FlickerTest extends OpMode {


    Servo flicker;

    @Override
    public void init(){

        flicker = hardwareMap.servo.get("pusher");
        flicker.setPosition(0.11);
//

    }

    @Override
    public void loop() {


        //flicker servo controls
        if (gamepad2.right_trigger > 0) {
            flicker.setPosition(0.5);
        }
        else {
            flicker.setPosition(0.11);
        }



    }
}