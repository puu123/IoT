package puu123;

import com.pi4j.wiringpi.Spi;

public class MCP3002 {

	public static final int CS0 = 0;
	public static final int CS1 = 1;
	public static final int CLOCK1M = 1000000;
	private int cs = 0;

	public MCP3002(int cs, int clock) {
		this.cs = cs;
		int ret = Spi.wiringPiSPISetup(cs, clock);
		if (ret < 0) {
			System.err.println("fail..."+ret);
		}
	}

	public int readChannel(int channel) {

		byte[] command = new byte[2];
		command[0] = channel == 0 ?
				(byte) 0b0110_1000:
				(byte) 0b0111_1000;
		command[1] = 0b0000_0000;

		Spi.wiringPiSPIDataRW(cs, command);
		//int intMSB = (command[0] << 7) & 0x380;
		//int intLSB = (command[1] << 1) & 0x7f;
		//int value = (intMSB + intLSB) & 0x3ff;
		int value = ((command[0]<<8) + command[1]) & 0x03FF;
		return value;
	}

	public double readVolt(int ch) {
		double u = readChannel(ch);
		return (3.3d*u)/1023.0d;

	}

	public static void main(String[] args) {

		MCP3002 mcp3002 = new MCP3002(CS0, CLOCK1M);
		while(true) {
			System.out.println(mcp3002.readVolt(0));
			System.out.println(mcp3002.readVolt(1));
		}
	}

}
