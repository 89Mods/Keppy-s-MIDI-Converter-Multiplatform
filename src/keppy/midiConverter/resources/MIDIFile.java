package keppy.midiConverter.resources;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import jouvieje.bass.Bass;
import jouvieje.bass.defines.BASS_DEVICE;
import jouvieje.bass.defines.BASS_MIDI;
import jouvieje.bass.defines.BASS_SAMPLE;
import jouvieje.bass.defines.BASS_STREAM;
import jouvieje.bass.structures.HSTREAM;
import jouvieje.bass.utils.BufferUtils;
import jouvieje.bass.utils.Pointer;
import keppy.midiConverter.GUI.ErrorMessage;
import keppy.midiConverter.main.KeppysMidiConverter;

public class MIDIFile {
	
	private File file;
	private String length;
	private String size;
	private long noteCount = 0;
	
	public MIDIFile(File file) {
		super();
		this.file = file;
		long filesize = file.length();
		try {
	        if (filesize / 1024f >= 9860)
	        {
	        	length =  "-:--";
	        }else{
	        	Bass.BASS_Init(-1, 22050, BASS_DEVICE.BASS_DEVICE_NOSPEAKER, null, null);
	  	        HSTREAM time = Bass.BASS_MIDI_StreamCreateFile(false, pointerFromString(file.getPath()), 0L, 0L, BASS_STREAM.BASS_STREAM_DECODE | BASS_SAMPLE.BASS_SAMPLE_FLOAT | BASS_MIDI.BASS_MIDI_DECAYEND, 0);
		        long pos = Bass.BASS_ChannelGetLength(time.asInt(), 0);
		        double num9 = Bass.BASS_ChannelBytes2Seconds(time.asInt(), pos);
		        String str4 = TimeUnit.SECONDS.toMinutes((long)num9) + ":" + (TimeUnit.SECONDS.toSeconds((long)num9) % 60);
		        if (str4 == "0:-1"){
		        	length =  "-:--";
		        }
		        length = str4;
		        Bass.BASS_Free();
		        countNotes(file);
	        }
	        //int count = Bass.BASS_MIDI_StreamGetEvents(time, -1, BASSMIDIEvent.MIDI_EVENT_NOTE, null);
		} catch(Exception e){
			ErrorMessage.showErrorMessage(KeppysMidiConverter.frame, "Error getting MIDI length", e, false);
			e.printStackTrace();
			length =  "-:--";
		}
		int i = 0;
		if(filesize >= 1024){
			i = 1;
			filesize = filesize / 1024;
		}
		/*
		 *                 
                BASS_MIDI_EVENT[] events = new BASS_MIDI_EVENT[count];
                BassMidi.BASS_MIDI_StreamGetEvents(time, -1, BASSMIDIEvent.MIDI_EVENT_NOTE, events);

                int notes = 0;
for (int a = 0; a < count; a++) { if ((events[a].param & 0xff00) != 0) { notes++; } }
		 */
		if(filesize >= 1024){
			i = 2;
			filesize = filesize / 1024;
		}
		if(filesize >= 1024){
			i = 3;
			filesize = filesize / 1024;
		}
		if(i == 0){
			size = Long.toString(filesize);
		}else if(i == 1){
			size = Long.toString(filesize) + "KB";
		}else if(i == 2){
			size = Long.toString(filesize) + "MB";
		}else if(i == 3){
			size = Long.toString(filesize) + "GB";
		}
	}
	
	public void forceInformation(){
		try {
	        Bass.BASS_Init(-1, 22050, BASS_DEVICE.BASS_DEVICE_NOSPEAKER, null, null);
	        HSTREAM time = Bass.BASS_MIDI_StreamCreateFile(false, pointerFromString(file.getPath()), 0L, 0L, BASS_STREAM.BASS_STREAM_DECODE | BASS_SAMPLE.BASS_SAMPLE_FLOAT | BASS_MIDI.BASS_MIDI_DECAYEND, 0);
		    long pos = Bass.BASS_ChannelGetLength(time.asInt(), 0);
		    double num9 = Bass.BASS_ChannelBytes2Seconds(time.asInt(), pos);
		    String str4 = TimeUnit.SECONDS.toMinutes((long)num9) + ":" + (TimeUnit.SECONDS.toSeconds((long)num9) % 60);
		    if (str4 == "0:-1"){
		        length =  "-:--";
		    }
		    length = str4;
	        countNotes(file);
	        Bass.BASS_Free();
		} catch(Exception e){
			ErrorMessage.showErrorMessage(KeppysMidiConverter.frame, "Error getting MIDI length", e, false);
			e.printStackTrace();
			length =  "-:--";
		}
	}
	
	public File getFilepath() {
		return file;
	}
	
	public String getLength() {
		return length;
	}
	
	public String getSize() {
		return size;
	}
	
	private Pointer pointerFromString(String s){
		return BufferUtils.asPointer(BufferUtils.fromString(s));
	}
	
	public long getNoteCount(){
		return noteCount;
	}
	
	private int countNotes(File midiFile){
		try {
		FileInputStream stream = new FileInputStream(midiFile);
		byte[] indentifier = new byte[4];
		stream.read(indentifier);
		String s = new String(indentifier);
		if(!s.equalsIgnoreCase("MThd")){
			stream.close();
			return 0;
		}
		stream.skip(4);
		byte[] header = new byte[0x06];
		stream.read(header);
		int trackCount = bytesToInt(Arrays.copyOfRange(header, 2, 4));
		for(int i = 0; i < trackCount; i++){
			indentifier = new byte[4];
			stream.read(indentifier);
			s = new String(indentifier);
			if(!s.equalsIgnoreCase("MTrk")){
				stream.close();
				return 0;
			}
			byte[] headerSize = new byte[4];
			stream.read(headerSize);
			int size = bytesToInt(headerSize);
			byte[] data = new byte[size];
			stream.read(data);
			ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
			while(byteStream.available() > 0){
				long time = getVaribaleLengthValue(byteStream);
				byteStream.mark(1);
				if(time < 0){
					break;
				}
				int meta = byteStream.read() & 0xFF;
				if(meta >= 0x80 && meta <= 0xFE){
					if(meta >= 0xF4 && meta <= 0xFF){
						continue;
					}
					byteStream.read();
					if((!(meta >= 0xC0 && meta <= 0xDF)) && meta != 0xF3 && meta != 0xF1){
						byteStream.read();
					}
					if(meta == 0x90 || meta == 0x91 || meta == 0x92 || meta == 0x93 || meta == 0x94 || meta == 0x95 || meta == 0x96 || meta == 0x97 || meta == 0x98 || meta == 0x99 || meta == 0x9A || meta == 0x9B || meta == 0x9C || meta == 0x9D || meta == 0x9E || meta == 0x9F){
						noteCount++;
					}
				}else
				if(meta == 0xFF){
					byteStream.read();
					int length = (int)getVaribaleLengthValue(byteStream);
					byte[] data2 = new byte[length];
					if(length > 0){
						byteStream.read(data2);
					}
				}else
				if(meta == 0xF0 || meta == 0xF7){
					int lol = 0;
					byteStream.read();
					while(lol != 0xF7){
						lol = byteStream.read();
					}
				}else{
					byteStream.reset();
					byteStream.read();
				}
			}
		}
		stream.close();
		} catch(Exception e){
			System.err.println("Error counting them notes");
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	
	private long getVaribaleLengthValue(ByteArrayInputStream byteStream){
		long n = 0;
		boolean loop = true;
		while(loop){
			int curByte = byteStream.read() & 0xFF;
			n = (n << 7) | (curByte & 0x7F);
			if((curByte & 0x80) == 0){
				loop = false;
			}
		}
		return n;
	}
	
	private int bytesToInt(byte[] lol){
	    int value = 0;
	    for (int i = 0; i < lol.length; i++) {
	        int shift = (lol.length - 1 - i) * 8;
	        value += (lol[i] & 0x000000FF) << shift;
	    }
	    return value;
	}
	
}
