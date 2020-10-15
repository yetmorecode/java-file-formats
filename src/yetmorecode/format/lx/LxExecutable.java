package yetmorecode.format.lx;

import java.util.ArrayList;

import yetmorecode.format.mz.MzHeader;

public class LxExecutable {
	/**
	 * Old-style MS-DOS header (optional)
	 */
	public MzHeader dosHeader = new MzHeader();
	
	/**
	 * LX/LE/LC header
	 */
	public LxHeader header = new LxHeader();
	
	/**
	 * Object table
	 */
	public ArrayList<ObjectTableEntry> objectTable = new ArrayList<>();
	
	/**
	 * The object page table (mapping page numbers to file offsets)
	 */
	public ArrayList<ObjectPageTableEntry> objectPageTable = new ArrayList<>();
	
	/**
	 * The fixup record table
	 */
	public ArrayList<ArrayList<LxFixupRecord>> fixupRecordTable = new ArrayList<>();
	
	/**
	 * The actual data pages
	 */
	public ArrayList<byte[]> pages = new ArrayList<>();
}
