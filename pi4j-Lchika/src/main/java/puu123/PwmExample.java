package puu123;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;

public class PwmExample {

    /**
     * [ARGUMENT/OPTION "--pin (#)" | "-p (#)" ]
     * This example program accepts an optional argument for specifying the GPIO pin (by number)
     * to use with this GPIO listener example. If no argument is provided, then GPIO #1 will be used.
     * -- EXAMPLE: "--pin 4" or "-p 0".
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws Exception {

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate code)
        final Console console = new Console();

        // print program title/header
        console.title("<-- The Pi4J Project -->", "PWM Example");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // create GPIO controller instance
        GpioController gpio = GpioFactory.getInstance();

        // All Raspberry Pi models support a hardware PWM pin on GPIO_01.
        // Raspberry Pi models A+, B+, 2B, 3B also support hardware PWM pins: GPIO_23, GPIO_24, GPIO_26
        //
        // by default we will use gpio pin #01; however, if an argument
        // has been provided, then lookup the pin by address
        Pin pin = CommandArgumentParser.getPin(
                RaspiPin.class, // pin provider class to obtain pin instance from
                RaspiPin.GPIO_01, // default pin if no pin argument found
                args); // argument array to search in

        GpioPinPwmOutput pwm = gpio.provisionPwmOutputPin(pin);



        // you can optionally use these wiringPi methods to further customize the PWM generator
        // see: http://wiringpi.com/reference/raspberry-pi-specifics/
        com.pi4j.wiringpi.Gpio.pwmSetMode(com.pi4j.wiringpi.Gpio.PWM_MODE_MS);
        com.pi4j.wiringpi.Gpio.pwmSetRange(1024);
        com.pi4j.wiringpi.Gpio.pwmSetClock(400);

        // set the PWM rate to 500
        pwm.setPwm(24);
        console.println("PWM rate is: " + pwm.getPwm());

        //console.println("Press ENTER to set the PWM to a rate of 250");
        //System.console().readLine();
        readValue("Press ENTER to set the PWM to a rate of 115");

        // set the PWM rate to 250
        pwm.setPwm(115);
        console.println("PWM rate is: " + pwm.getPwm());
        Thread.sleep(500);

        //console.println("Press ENTER to set the PWM to a rate to 0 (stop PWM)");
        //System.console().readLine();
        readValue("Press ENTER to set the PWM to a rate to 0 (stop PWM)");

        // set the PWM rate to 0
        pwm.setPwm(0);
        console.println("PWM rate is: " + pwm.getPwm());
        Thread.sleep(500);

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
    }

    private static String readLine(String format, Object... args) throws IOException {
        if (System.console() != null) {
            return System.console().readLine(format, args);
        }
        System.out.print(String.format(format, args));
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));
        return reader.readLine();
    }

    private static char[] readValue(String format, Object... args)
            throws IOException {
        if (System.console() != null)
            return System.console().readPassword(format, args);
        return readLine(format, args).toCharArray();
    }

}
