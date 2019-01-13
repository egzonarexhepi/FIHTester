package arduinoGUI;

import gnu.io.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.ActionListener;
import java.io.IOException;

public class SerialComm  implements SerialPortEventListener
{
	/*
	 * initialize vars
	 */
	static int numBytes;
	public Executive exec ;
	CommPortIdentifier portIdentifier;
	String selectedComPort;

	/*
	 * reference to Executive class
	 */
	public SerialComm(Executive ec)
	{
		exec = ec;
	}

	/*
	 * connects to the selected port from the executive class
	 */
	public boolean connect()
	{
		boolean result = false;
		int baud = 38400; // baud rate = 38400
		int dbits = 8; // dbits = 8
		int par = 0; // parity = none;
		int sbits = 1; // stopbits = 1

		try{

			selectedComPort = exec.getComm();
			CommPort commPort =  null;
			portIdentifier = CommPortIdentifier.getPortIdentifier(exec.getComm());

			if (portIdentifier.isCurrentlyOwned() ) //cannot open a busy port
			{
				System.out.println("Error: Port is currently in use");
			}
			else
			{

				// connection is opened here
				commPort = portIdentifier.open(this.getClass().getName(),2000);
				if ( commPort instanceof SerialPort )
				{

					exec.spt = (SerialPort) commPort;
					exec.spt.setSerialPortParams(baud, dbits, sbits, par);
					exec.in = exec.spt.getInputStream(); //reading -- this is what you can print (in for loop)
					exec.out = exec.spt.getOutputStream(); //writing
					System.out.println("Out has been initialized");
					(new Thread(new SerialWriter(exec))).start(); // need this
					exec.spt.addEventListener(new SerialReader(exec));
					exec.spt.notifyOnDataAvailable(true);
				}
				System.out.println( selectedComPort + " opened successfully.");
				result = true;
			}
		}
		catch (PortInUseException e)
		{
			System.out.println( selectedComPort + " is busy an cannot be used. (" + e.toString() + ")");
		}
		catch (Exception e)
		{
			System.out.println( "Failed to open " + selectedComPort + "(" + e.toString() + ")");
			e.printStackTrace();
		}
		return result;
	}


	/*
	 * SerialWriter - sends inputs to the board
	 */
	public static class SerialWriter  implements Runnable 
	{
		Executive exec;
		String usertxt="help\r\n";
		byte [] b ;


		/*
		 * reference to Executive Class
		 */
		public SerialWriter (  Executive ser )
		{
			exec = ser;
		}

		/*
		 * where the inputs are sent to the board
		 */
		public void creditRun() {
			exec.loadButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					try{
						usertxt=exec.commandInput()+exec.inputField.getText();

						b=usertxt.getBytes();

						for(int i=0;i<usertxt.length();i++)
						{
							exec.out.write(b[i]);
							System.out.print((char)(b[i]));
						}

						System.out.println("");
						exec.out.write(13);
						exec.out.write(10);
						exec.out.flush();

					}catch(NullPointerException np){
						System.out.println("Command cannot be sent.");

					} catch (IOException f) {
						f.printStackTrace();
					}
				}
			});

		}

		/*
		 * sends operation mode input to the board
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run ()
		{
			exec.operationModeSelect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					try{
						usertxt=exec.commandInput();
						if (usertxt == "cred ") {
							creditRun();
						}
						else {
							b=usertxt.getBytes();

							for(int i=0;i<usertxt.length();i++)
							{
								exec.out.write(b[i]);
								System.out.print((char)(b[i]));
							}

							System.out.println("");
							exec.out.write(13);
							exec.out.write(10);
							exec.out.flush();
						}

					}catch(NullPointerException np){
						System.out.println("Command cannot be sent.");

					} catch (IOException f) {
						f.printStackTrace();
					}
				}
			});
		}
	}

	/*
	 * Serial reader - reads the output of the board and makes calls to Executive Class to update the GUI
	 */
	public static class SerialReader implements SerialPortEventListener 
	{
		private String line="";
		private int ascval = 0;
		private Executive exe;
		boolean creditTest = false;
		//	private DataLog data;

		public SerialReader ( Executive exec)
		{ 
			exe=exec;
		}

		public void serialEvent(SerialPortEvent event) {


			switch(event.getEventType()) {
			case SerialPortEvent.BI:
			case SerialPortEvent.OE:
			case SerialPortEvent.FE:
			case SerialPortEvent.PE:
			case SerialPortEvent.CD:
			case SerialPortEvent.CTS:
			case SerialPortEvent.DSR:
			case SerialPortEvent.RI:
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
				//break;
			case SerialPortEvent.DATA_AVAILABLE:

				byte[] readSBuffer = new byte[4096]; //increase this buffer if data seems truncated
				//changed this
				try {
					while(exe.in.available()>0)
						numBytes = exe.in.read(readSBuffer);

					//print the num
					//line = "";
					for(int s=0;s<numBytes;s++)
					{
						ascval= readSBuffer[s];

						if (ascval > 0 && ascval != 46) {

							if(ascval==10 || ascval == 13) 
							{
								String oneLine = line;
								String newLine = oneLine.replaceAll("\r", "").replaceAll("\n", "");

								if (newLine.equals("CON")) {

									exe.updateLights(exe.colorButton, exe.electricBlue);
									exe.updateLights(exe.monoButton, Color.white);
								} else if (newLine.equals("MON")) {

									exe.updateLights(exe.colorButton, Color.white);
									exe.updateLights(exe.monoButton, exe.electricBlue);
								} else if (newLine.equals("EN")) {
									creditTest = false;

									exe.updateLights(exe.enableButton, exe.electricBlue);
									exe.updateLights(exe.copyPulseButton, Color.white);
									exe.updateLights(exe.monoButton, Color.white);
									exe.updateLights(exe.colorButton, Color.white);

									exe.creditCountLabel.setText("N/A");
								} else if (newLine.equals("DIS") || newLine.equals("RCC: 0") || newLine.equals("CR: 0")){
									creditTest = false;

									exe.updateLights(exe.enableButton, Color.white);
									exe.updateLights(exe.copyPulseButton, Color.white);
									exe.updateLights(exe.monoButton, Color.white);
									exe.updateLights(exe.copyPulseButton, Color.white);

									exe.creditCountLabel.setText("N/A");
								} else if (newLine.equals("CC")) {
									exe.typeUpdate.add("C");

									exe.colorCount++;
									exe.totalCount++;

									exe.updateDisplay();

									exe.updateLights(exe.colorButton, exe.electricBlue);
									exe.updateLights(exe.copyPulseButton, exe.electricBlue);
									exe.updateLights(exe.monoButton, Color.white);

									exe.updateCount("color");
									exe.updateCount("total");

									if (creditTest == true) {
										exe.creditCount--;
										exe.updateCount("credit");
									}

								} else if (newLine.equals("MC")) {
									exe.typeUpdate.add("M");

									exe.monoCount++;
									exe.totalCount++;

									exe.updateDisplay();

									exe.updateLights(exe.monoButton, exe.electricBlue);
									exe.updateLights(exe.copyPulseButton, exe.electricBlue);
									exe.updateLights(exe.colorButton, Color.white);

									exe.updateCount("mono");
									exe.updateCount("total");

									if (creditTest == true) {
										exe.creditCount--;
										exe.updateCount("credit");
									}

								} else if (newLine.substring(0, 4).equals("CR: ")) {
									creditTest = true;
									exe.creditCount = Integer.parseInt(newLine.substring(4));
									exe.updateCount("credit");

									exe.updateLights(exe.enableButton, Color.white);
									exe.updateLights(exe.copyPulseButton, Color.white);
									exe.updateLights(exe.colorButton, Color.white);
									exe.updateLights(exe.monoButton, Color.white);
								}
								line="";

							} else {

								line+=""+(char) readSBuffer[s];
							}  
						} 
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		// TODO Auto-generated method stub

	}
}

