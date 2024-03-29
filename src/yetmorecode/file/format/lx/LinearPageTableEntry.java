package yetmorecode.file.format.lx;

public class LinearPageTableEntry {	
	public int dataOffset;
	public short dataSize;
	public short flags;	
	public int index;

	public int getIndex() {
		return index;
	}
		
	/**
	 * PAGE DATA OFFSET = DD Offset to the page data in the EXE file.
	 * 
	 * This field, when bit shifted left by the PAGE OFFSET SHIFT from the module header,
	 * specifies the offset from the beginning of the Preload Page section of the physical
	 * page data in the EXE file that corresponds to this logical page entry. The page data
	 * may reside in the Preload Pages, Demand Load Pages or the Iterated Data Pages
	 * sections.
	 * 
	 * A page might not start at the next available alignment boundary. Extra padding is
	 * acceptable between pages as long as each page starts on an alignment boundary.
	 * 
	 * For example, several alignment boundaries may be skipped in order to start a
	 * frequently accessed page on a sector boundary.
	 * If the FLAGS field specifies that this is a Zero-Filled page then the PAGE DATA
	 * OFFSET field will contain a 0.
	 * 
	 * If the logical page is specified as an iterated data page, as indicated by the FLAGS
	 * field, then this field specifies the offset into the Iterated Data Pages section.
	 * The logical page number (Object Page Table index), is used to index the Fixup Page
	 * Table to find any fixups associated with the logical page.
	 * 
	 */
	public int getOffset() {
		return dataOffset;
	}
	
	/**
	 * DATA SIZE = DW Number of bytes of data for this page.
	 * 
	 * This field specifies the actual number of bytes that represent the page in the file. If
	 * the PAGE SIZE field from the module header is greater than the value of this field
	 * and the FLAGS field indicates a Legal Physical Page, the remaining bytes are to be
	 * filled with zeros. If the FLAGS field indicates an Iterated Data Page, the iterated data
	 * records will completely fill out the remainder.
	 * 
	 */
	public short getSize() {
		return dataSize;
	}
	
	/**
	 * 00h = Legal Physical Page in the module (Offset from Preload Page Section).
	 */
	public final static short FLAG_LEGAL = 0x0;
	
	/**
	 * 01h = Iterated Data Page (Offset from Iterated Data Pages Section).
	 */
	public final static short FLAG_ITERATED = 0x1;
	
	/**
	 * 02h = Invalid Page (zero).
	 */
	public final static short FLAG_INVALID = 0x2;
	
	/**
	 * 03h = Zero Filled Page (zero).
	 */
	public final static short FLAG_ZERO = 0x3;
	
	/**
	 * 04h = Range of Pages.
	 */
	public final static short FLAG_RANGE = 0x4;
	
	/**
	 * 05h = Compressed Page (Offset from Preload Pages Section).
	 */
	public final static short FLAG_COMPRESSED = 0x5;
	
	/**
	 * FLAGS = DW Attributes specifying characteristics of this logical page.
	 * 
	 * The bit definitions for this word field follow,
	 * 00h = Legal Physical Page in the module (Offset from Preload Page Section).
	 * 01h = Iterated Data Page (Offset from Iterated Data Pages Section).
	 * 02h = Invalid Page (zero).
	 * 03h = Zero Filled Page (zero).
	 * 04h = Range of Pages.
	 * 05h = Compressed Page (Offset from Preload Pages Section).
	 */
	
	public short getFlags() {
		return flags;
	}
}
