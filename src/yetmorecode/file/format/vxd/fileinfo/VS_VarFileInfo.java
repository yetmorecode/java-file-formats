package yetmorecode.file.format.vxd.fileinfo;

import java.util.ArrayList;

public class VS_VarFileInfo implements VS_VERSIONINFO_CHILD {
	public short wLength;
	public short wValueLength;
	public short wType;
	public String szKey;
	public short Padding;
	
	public short varLength;
	public short varValueLength;
	public short varType;
	public String varszKey;
	public short varPadding;
	
	// list of 32bit language IDs
	public ArrayList<Integer> Children = new ArrayList<>();
}
