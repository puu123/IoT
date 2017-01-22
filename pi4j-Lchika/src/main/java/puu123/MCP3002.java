package puu123;

import java.io.IOException;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.util.Console;

public class MCP3002 {

//	public static final int CS0 = 0;
//	public static final int CS1 = 1;
//	public static final int CLOCK1M = 1000000;
//	private int cs = 0;
//
//	public MCP3002(int cs, int clock) {
//		this.cs = cs;
//		int ret = Spi.wiringPiSPISetup(cs, clock);
//		if (ret < 0) {
//			System.err.println("fail..."+ret);
//		}
//	}
//
//	public int readChannel(int channel) {
//
//		byte[] command = new byte[2];
//		command[0] = channel == 0 ?
//				(byte) 0b0110_1000:
//				(byte) 0b0111_1000;
//		command[1] = 0b0000_0000;
//
//		Spi.wiringPiSPIDataRW(cs, command);
//		//int intMSB = (command[0] << 7) & 0x380;
//		//int intLSB = (command[1] << 1) & 0x7f;
//		//int value = (intMSB + intLSB) & 0x3ff;
//		int value = ((command[0]<<8) + command[1]) & 0x03FF;
//		return value;
//	}
//
//	public double readVolt(int ch) {
//		double u = readChannel(ch);
//		return (3.3d*u)/1023.0d;
//
//	}
//
//	public static void main(String[] args) {
//
//		MCP3002 mcp3002 = new MCP3002(CS0, CLOCK1M);
//		while(true) {
//			System.out.println(mcp3002.readVolt(0));
//			System.out.println(mcp3002.readVolt(1));
//		}
//	}


    // SPI device
    public static SpiDevice spi = null;

    // create Pi4J console wrapper/helper
    // (This is a utility class to abstract some of the boilerplate code)
    protected static final Console console = new Console();

	public static void main(String[] args) throws InterruptedException, IOException {

        // print program title/header
        console.title("<-- The Pi4J Project -->", "SPI test program using MCP3004/MCP3008 AtoD Chip");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // This SPI example is using the Pi4J SPI interface to communicate with
        // the SPI hardware interface connected to a MCP3004/MCP3008 AtoD Chip.
        //
        // Please make sure the SPI is enabled on your Raspberry Pi via the
        // raspi-config utility under the advanced menu option.
        //
        // see this blog post for additional details on SPI and WiringPi
        // http://wiringpi.com/reference/spi-library/
        //
        // see the link below for the data sheet on the MCP3004/MCP3008 chip:
        // http://ww1.microchip.com/downloads/en/DeviceDoc/21294E.pdf

        // create SPI object instance for SPI for communication
        spi = SpiFactory.getInstance(SpiChannel.CS0,
                SpiDevice.DEFAULT_SPI_SPEED, // default spi speed 1 MHz
                SpiDevice.DEFAULT_SPI_MODE); // default spi mode 0

        // continue running program until user exits using CTRL-C
        while(console.isRunning()) {
            read();
            Thread.sleep(1000);
        }
        console.emptyLine();


	}

    /**
     * Read data via SPI bus from MCP3002 chip.
     * @throws IOException
     */
    public static void read() throws IOException, InterruptedException {
        for(short channel = 0; channel < 2; channel++){
            int conversion_value = getConversionValue(channel);
            console.print(String.format(" | %04d", conversion_value)); // print 4 digits with leading zeros
        }
        console.print(" |\r");
        Thread.sleep(250);
    }


    /**
     * Communicate to the ADC chip via SPI to get single-ended conversion value for a specified channel.
     * @param channel analog input channel on ADC chip
     * @return conversion value for specified analog input channel
     * @throws IOException
     */
    public static int getConversionValue(short channel) throws IOException {

        // create a data buffer and initialize a conversion request payload
        byte data[] = new byte[2];

        data[0] = channel == 0  ?
				(byte) 0b0110_1000:
				(byte) 0b0111_1000;
		data[1] = 0b0000_0000;

        // send conversion request to ADC chip via SPI channel
        byte[] result = spi.write(data);

        // calculate and return conversion value from result bytes
        int value = ((result[0]<<8) + result[1]) & 0x03FF;
        return value;
    }

}
