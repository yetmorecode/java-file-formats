package yetmorecode.file.format.vxd;

import yetmorecode.file.format.vxd.fileinfo.VS_VERSIONINFO;

/**
 * In VxD LE headers the e32_winresoff points to this structure
 */
public class VersionResource {
	public byte cType;
	public short wID;
	public byte cName;
	public short wOrdinal;
	public short wFlags;
	public int dwResSize;
	public VS_VERSIONINFO info = new VS_VERSIONINFO();
}
