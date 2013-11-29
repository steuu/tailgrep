package steuu.tail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Ref;
import java.util.Scanner;

public class MyTail {
	String filename = null;
	
	public MyTail(String string) {
		filename = string;
	}

	public static void main(String[] args) {
		try {
			String name = null;
			
			if (args.length > 0)  
				name = args[0];
			else 
				name = "log.txt";
			
			new MyTail(name).go();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	byte[] buff = new byte[1024];
	long fileSize = 0;
	long oldSize =  -1;
	
	String regexp = null;
	/*FileChannel fc = null;
	ByteBuffer bb = null;*/

	
			
	private void go() throws IOException {
		
		new Thread() {
			public void run() {
				Scanner scanner = new Scanner(System.in);
				
				while (true) {
					if (scanner.hasNext()) {
						String mom = scanner.nextLine();
						
						System.out.println("\n"+mom+"  "+"w".equals(mom));
						
						if ("q".equals(mom))
							System.exit(0);
						else if ("w".equals(mom))
							regexp = null;
						else 
							regexp = mom; 
					}
				}
			}
		}.start();
		
		
		
		while (true) {
			File f = new File(filename);
			//FileInputStream fi = new FileInputStream(new File(filename));
			//fc =  fi.getChannel();
			long mom = fileSize;
			fileSize = f.length();
			
			if (fileSize < mom)
				mom = 0;
			
			//System.out.println(mom+"  "+fileSize);
			if (fileSize != mom) {
				//bb = fc.map(FileChannel.MapMode.READ_ONLY, mom, fileSize - mom);
				RandomAccessFile raf = new RandomAccessFile(new File(filename), "r" );
				raf.seek(mom);
				
				StringBuilder build = new StringBuilder();
	
				long rem =  fileSize-mom;
				while (rem > 0 ) {
				//while (bb.remaining() > 0) {
					int actual = (int) Math.min(buff.length, rem);
					//bb.get(buff, 0, actual );
					raf.read(buff, 0, actual);
					
					rem -= actual;
					
					String part = new String(buff, 0, actual);
					int i = part.indexOf("\n");
					//System.out.println("index "+i );
					while (i > 0) {
						build.append(part.substring(0, i));
						writeLine(build);
						
						build   = new StringBuilder();
						//build.append(part.substring(i+1));
						
						part = part.substring(i+1);
						i = part.indexOf("\n");
					} 
					
					build.append(part);
					
					
					
				}
				oldSize = fileSize-build.length();
				
				raf.close();		
			} else {
				//System.out.println("-----no difference");
			}
			//fc.close();
			//fi.close();
			
			
			
			synchronized (this) {
				try {
					this.wait(1000l);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	
		}
	}

	private void writeLine(StringBuilder build) {
		if (regexp == null)
			System.out.println(build);
		else if (build.toString().matches(".*"+regexp+".*")) {
			System.out.println(build);
		}
	}

}
