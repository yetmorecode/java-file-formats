package yetmorecode.file.format.vxd.fileinfo;

import java.util.ArrayList;

public class VS_StringFileInfo implements VS_VERSIONINFO_CHILD {
	public short wLength;
	public short wValueLength;
	public short wType;
	public String szKey;
	public short Padding;
	public ArrayList<VS_StringTable> Children = new ArrayList<>();
}
