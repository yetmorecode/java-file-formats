package yetmorecode.file.format.mz;

import java.util.ArrayList;

public class MzExecutable {
	public MzHeader header = new MzHeader();
	public ArrayList<MzRelocationEntry> relocations = new ArrayList<MzRelocationEntry>();
	public byte[] programData = new byte[0];
}
