package yetmorecode.file.format.vxd.fileinfo;

import java.util.ArrayList;

public class VS_VERSIONINFO {
	public short wLength;
	public short wType;
	public String szKey;
	public short Padding1;
	public VS_FIXEDFILEINFO value = new VS_FIXEDFILEINFO();
	public short Padding2;
	public ArrayList<VS_VERSIONINFO_CHILD> Children = new ArrayList<>();
}
