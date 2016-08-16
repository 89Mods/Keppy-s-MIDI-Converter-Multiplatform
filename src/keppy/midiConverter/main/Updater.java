package keppy.midiConverter.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import keppy.midiConverter.GUI.ErrorMessage;
import keppy.midiConverter.resources.FileDownloader;

public class Updater {
	
	public static int checkForUpdates(){
		try {
			FileDownloader.downloadFile("https://www.dropbox.com/s/61v9ndom0iyeaqq/converter_version.dat?dl=1", "convversion.dat");
		}catch(Exception e){
			ErrorMessage.showErrorMessage(KeppysMidiConverter.frame, "Error downloading convversion.dat for update check", e, false);
			e.printStackTrace();
			return 0;
		}
		int major = 0;
		int minor = 0;
		int patch = 0;
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(new File("convversion.dat")));
			major = dis.readInt();
			minor = dis.readInt();
			patch = dis.readInt();
			dis.close();
		}catch(Exception e){
			ErrorMessage.showErrorMessage(KeppysMidiConverter.frame, "Error reading convversion.dat", e, false);
			e.printStackTrace();
			return 0;
		}
		new File("convversion.dat").delete();
		if(major == KeppysMidiConverter.VERSION_MAJOR && minor == KeppysMidiConverter.VERSION_MINOR && patch == KeppysMidiConverter.VERSION_PATCH){
			return 0;
		}
		if(minor <= KeppysMidiConverter.VERSION_MINOR && patch <= KeppysMidiConverter.VERSION_PATCH && major <= KeppysMidiConverter.VERSION_MAJOR){
			return 2;
		}
		return 1;
	}
	
	public static void writeVersion(){
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File("converter_version.dat")));
			dos.writeInt(KeppysMidiConverter.VERSION_MAJOR);
			dos.writeInt(KeppysMidiConverter.VERSION_MINOR);
			dos.writeInt(KeppysMidiConverter.VERSION_PATCH);
			dos.close();
		}catch(Exception e){
			System.err.println("Error writing current version to file");
			e.printStackTrace();
			return;
		}
	}
	
}
