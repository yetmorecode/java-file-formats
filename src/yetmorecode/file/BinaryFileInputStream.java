package yetmorecode.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BinaryFileInputStream extends FileInputStream {

	private ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
	
	public BinaryFileInputStream(String name) throws FileNotFoundException {
		super(name);
	}
	
	public BinaryFileInputStream(String name, ByteOrder order) throws FileNotFoundException {
		super(name);
		byteOrder = order;
	}
	
	public BinaryFileInputStream(File file) throws FileNotFoundException {
		super(file);
	}
	
	public BinaryFileInputStream(File file, ByteOrder order) throws FileNotFoundException {
		this(file);
		byteOrder = order;
	}
	
	public int readByte(long offset) throws IOException {
		var old = position(offset);
		int ret = readByte();
		position(old);
		return ret;
	}
	
	public int readByte() throws IOException {
		return read();
	}
	
	public short readShort(long offset) throws IOException {
		var old = position(offset);
		var i = readShort();
		position(old);
		return i;
	}
	
	public short readShort() throws IOException  {
		byte[] bytes = readNBytes(2);
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		bb.order(byteOrder);
		return bb.getShort();
	}
	
	public int readInt(long offset) throws IOException {
		var old = position(offset);
		var i = readInt();
		position(old);
		return i;
	}
	
	public int readInt() throws IOException  {
		byte[] bytes = readNBytes(4);
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		bb.order(byteOrder);
		return bb.getInt();
	}
	
	public float readFloat(long offset) throws IOException {
		var old = position(offset);
		var i = readFloat();
		position(old);
		return i;
	}
	
	public float readFloat() throws IOException  {
		byte[] bytes = readNBytes(4);
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		bb.order(byteOrder);
		return bb.getFloat();
	}
	
	public String readString(long offset, int size) throws IOException {
		var old = position(offset);
		var i = readString(size);
		position(old);
		return i;
	}
	
	public String readString(int size) throws IOException {
		return new String(readNBytes(size));
	}
	
	public long position(long offset) throws IOException {
		var old = getChannel().position();
		getChannel().position(offset);
		return old;
	}
}
