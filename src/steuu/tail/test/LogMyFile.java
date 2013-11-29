package steuu.tail.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;


public class LogMyFile {

	static String[] lines = {"1asdfsdfsd1","2asaaaaaaaaaaa2", "3ityiuy iuyi uyio3"};
	public static void main(String[] args) {
		new LogMyFile().go();
	}
	
	private void go() {
		
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(new File("log.txt")));
			double i = Math.random()*10000;
			
			i = 50;
			
			while (true) {
				out.write(getLine());
				out.flush();
				synchronized (this) {
					this.wait((long)(Math.random()*1000l));
				}
				System.out.print(".");
				if (i-- < 0 ) {
					out.close();
					out = new OutputStreamWriter(new FileOutputStream(new File("log.txt")));
					i = 50 ; //Math.random()*1000;
					System.out.println();
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	static int count = 0;
	private static String getLine() {
		String ret = "";
		count++;
		//count %= 3;
		return count +" " + lines[count%3]+"\n";
		/*for (int i = 0; i < Math.random()*10; i++) {
			ret +=  lines[(int)(Math.random()*lines.length) ];
		}*/
		//return ret+"\n";
	}

}
