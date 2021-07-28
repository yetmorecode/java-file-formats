package yetmorecode.file.format.pcx;

import java.io.IOException;
import java.util.ArrayList;

import yetmorecode.file.BinaryFileInputStream;

public class PCX {

	/**
	 * Always 0x0A
	 */
	public byte manufacturer = 0xa;
	
	/**
	 * PC Paintbrush version. Acts as file format version.
	 * 0 = v2.5
	 * 2 = v2.8 with palette
	 * 3 = v2.8 without palette
	 * 4 = Paintbrush for Windows
	 * 5 = v3.0 or higher
	 */
	public byte version = 5;
	
	/**
	 * Should be 0x01
	 * 0 = uncompressed image (not officially allowed, but some software supports it)
	 * 1 = PCX run length encoding
	 */
	public byte encoding = 1;
	
	/**
	 * 
	 */
	public byte bitsPerPlane;
	
	public short xMin = 0;
	public short xMax = 0;
	
	public short yMin = 0;
	public short yMax = 0;
	
	public short verticalDPI;
	public short horizontalDPI;
	
	public byte[] palette = new byte[48];
	public byte reserved1;
	public byte colorPlanes;
	public short bytesPerScanline;
	public short paletteInfo;
	public short horizontalScreenSize;
	public short verticalScreenSize;
	public byte[] padding = new byte[58];
	
	public ArrayList<RGBColor> pixels = new ArrayList<>();
	
	public boolean hasExtendedPalette = false;
	public byte[] extendedPalette = new byte[3*256];
	
	public short getHeight() {
		return (short) (yMax - yMin + 1);
	}
	
	public short getWidth() {
		return (short) (xMax - xMin + 1);
	}
	
	public PCX() {
		pixels = new ArrayList<>();
	}
	
	public static PCX createFrom(BinaryFileInputStream input, long offset, ArrayList<RGBColor> palette) throws IOException {
		var pcx = new PCX();
		return createFrom(input, offset, palette, pcx);
	}
	
	public static PCX createFrom(BinaryFileInputStream input, long offset, ArrayList<RGBColor> palette, PCX pcx) throws IOException {
		var old = input.position(offset);
		pcx.manufacturer = (byte) input.read();
		pcx.version = (byte) input.read();
		pcx.encoding = (byte) input.read();
		pcx.bitsPerPlane = (byte) input.read();
		pcx.xMin = input.readShort();
		pcx.yMin = input.readShort();
		pcx.xMax = input.readShort();
		pcx.yMax = input.readShort();
		pcx.horizontalDPI = input.readShort();
		pcx.verticalDPI = input.readShort();
		pcx.palette = input.readNBytes(48);
		pcx.reserved1 = (byte) input.read();
		pcx.colorPlanes = (byte) input.read();
		pcx.bytesPerScanline = input.readShort();
		input.readShort();
		pcx.horizontalScreenSize = input.readShort();
		pcx.verticalScreenSize = input.readShort();
		input.readNBytes(54);
		
		var width = pcx.getWidth();
		var height = pcx.getHeight();
		int unpackedSize;
		for (int i = 0; i < height; i++) {		
			for (int j = 0; j < width; j += unpackedSize) {
				int color = input.read();
				unpackedSize = 1;
    			if ((color & 0xc0) == 0xc0) {
    				int c = input.read();
        			unpackedSize = color & 0x3f;
        			for (int k = 0; k < unpackedSize; k++) {
        				pcx.pixels.add(palette.get(c));
        			}
    			} else {
    				pcx.pixels.add(palette.get(color));	
    			}
    		}
    	}
		input.position(old);
		return pcx;
	}
}
