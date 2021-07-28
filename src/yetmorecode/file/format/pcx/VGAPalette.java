package yetmorecode.file.format.pcx;

import java.io.IOException;
import java.util.ArrayList;

import yetmorecode.file.BinaryFileInputStream;

public class VGAPalette {
	public static final int COLORS = 256;
	public static final int SIZE = 3*COLORS;
	
	public ArrayList<RGBColor> colors = new ArrayList<>();
	
	public static VGAPalette createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var p = new VGAPalette();
		return createFrom(input, offset, p);
	}
	
	public static VGAPalette createFrom(BinaryFileInputStream input, long offset, VGAPalette p) throws IOException {
		var old = input.position(offset);
		for (int i = 0; i < COLORS; i++) {
    		var r =  input.read();
    		var g = input.read();
    		var b = input.read();
    		p.colors.add(new RGBColor(r, g, b));
		}
		input.position(old);
		return p;
	}
}
