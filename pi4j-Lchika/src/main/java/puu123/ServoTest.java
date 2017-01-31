package puu123;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

public class ServoTest {

    private static final int PIN_NUMBER = 1;

    public static void main(String[] args) throws Exception {

        Gpio.wiringPiSetup();
        SoftPwm.softPwmCreate(PIN_NUMBER, 0, 100);

        for (int i = 0; i <= 100; i++) {
            SoftPwm.softPwmWrite(PIN_NUMBER, i);
            Thread.sleep(500);
        }
        for (int i = 100; i >= 0; i--) {
            SoftPwm.softPwmWrite(PIN_NUMBER, i);
            Thread.sleep(500);
        }
    }

}
