package yetmorecode.file.format.lx;

import java.util.ArrayList;
import java.util.HashMap;

import yetmorecode.file.format.mz.MzHeader;

public class LinearExecutable {
	/**
	 * Old-style MS-DOS header (optional)
	 */
	public MzHeader dosHeader = new MzHeader();
	
	/**
	 * LX/LE/LC header
	 */
	public LinearHeader header = new LinearHeader();
	
	/**
	 * Object table
	 */
	public ArrayList<LinearObjectTableEntry> objects = new ArrayList<>();
	
	/**
	 * The object page table (mapping page numbers to file offsets)
	 */
	public ArrayList<LinearPageTableEntry> pageRecords = new ArrayList<>();
	
	/**
	 * The fixup page table
	 */
	public long[] fixupTable;
	
	/**
	 * The fixup record table
	 */
    public HashMap<Integer, ArrayList<LinearFixupRecord>> fixups = new HashMap<>();
    
    public int fixupCount;
	
	/**
	 * The actual data pages
	 */
	public ArrayList<byte[]> pages = new ArrayList<>();
}
