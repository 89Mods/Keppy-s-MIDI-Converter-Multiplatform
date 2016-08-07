package keppy.midiConverter.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ResourceExtractor {
	
	public static void extractRecourceFromJar(String resource, String outputFilePath) throws Exception {
		File outputFile = new File(outputFilePath);
		FileOutputStream str = new FileOutputStream(outputFile);
		InputStream strIn = ResourceExtractor.class.getResourceAsStream(resource);
		int counter = 0;
		while(strIn.available() > 0){
			byte b = (byte)strIn.read();
			str.write(b);
			if(counter >= 10){
				str.flush();
				counter = 0;
			}
			counter++;
		}
		str.flush();
		strIn.close();
		str.close();
	}
	
}
