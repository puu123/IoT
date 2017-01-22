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
				(byte) 0b1101_0000: (byte) 0b1111_0000;
		command[1] = 0b00;

		Spi.wiringPiSPIDataRW(cs, command, 2);
		int intMSB = (command[0] << 7) & 0x380;
		int intLSB = (command[0] << 1) & 0x7f;
		int value = (intMSB + intLSB) & 0x3ff;
		return value;
	}

	public double readVolt(int ch) {
		double u = readChannel(ch);
		return (3.3d*u)/1023.0d;

	}

	public static void main(String[] args) {
		MCP3002 mcp3002 = new MCP3002(0, CLOCK1M);
		System.out.println(mcp3002.readVolt(CS0));
		System.out.println(mcp3002.readVolt(CS0));
		System.out.println(mcp3002.readVolt(CS0));

	}

}
