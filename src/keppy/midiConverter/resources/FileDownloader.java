package keppy.midiConverter.resources;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FileDownloader {
	
	public static void downloadFile(String fileUrl, String outputFilePath) throws Exception {
		FileOutputStream fos;
        URL website = new URL(fileUrl);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        fos = new FileOutputStream(outputFilePath);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
	}
	
}
