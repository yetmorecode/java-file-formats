package yetmorecode.file;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class BinaryFileOutputStream extends FileOutputStream {

	public BinaryFileOutputStream(String name) throws FileNotFoundException {
		super(name);
	}

}
