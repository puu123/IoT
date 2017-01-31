package puu123;

import java.io.IOException;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.util.Console;

public class MCP3002 {

    // SPI device
    public static SpiDevice spi = null;

    // create Pi4J console wrapper/helper
    // (This is a utility class to abstract some of the boilerplate code)
    protected static final Console console = new Console();

	public static void main(String[] args) throws InterruptedException, IOException {

        console.title("<-- The Pi4J Project -->", "SPI test program using MCP3002 AtoD Chip");
        console.promptForExit();

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
