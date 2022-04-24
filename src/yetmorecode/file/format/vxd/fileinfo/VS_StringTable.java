package yetmorecode.file.format.vxd.fileinfo;

import java.util.ArrayList;

public class VS_StringTable {
	public short wLength;
	public short wValueLength;
	public short wType;
	public String szKey;
	public short Padding;
	public ArrayList<VS_String> Children = new ArrayList<>();
}
