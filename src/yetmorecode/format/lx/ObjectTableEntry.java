package yetmorecode.format.lx;

/**
 * LX Executable Object Table Entry<br>
 * <br>
 * The number of entries in the Object Table is given by the # Objects in Module field in
 * the linear EXE header. Entries in the Object Table are numbered starting from one.
 * <br>
 * Each Object Table entry has the following format:
 * <pre>
 *     +-----+-----+-----+-----+-----+-----+-----+-----+
 * 00h |     VIRTUAL SIZE      |    RELOC BASE ADDR    |
 * 08h |     OBJECT FLAGS      |    PAGE TABLE INDEX   |
 * 10h |  # PAGE TABLE ENTRIES |       RESERVED        |
 *     +-----+-----+-----+-----+-----+-----+-----+-----+
 * </pre
 *   
 * @author https://github.com/yetmorecode
 */
public class ObjectTableEntry {
	/**
	 * Size of an object table entry
	 */
	public final static int SIZE = 0x18;
	
	/**
	 * Object number (1-based index as used in other LX header fields)
	 */
	public int number;
	
	/**
	 * Virtual size of object
	 * 
	 * This is the size of the object that will be allocated when the object is loaded. The
	 * object data length must be less than or equal to the total size of the pages in the
	 * EXE file for the object. This memory size must also be large enough to contain all of
	 * the iterated data and uninitialized data in the EXE file.
	 */
	public int size;
	
	/**
	 * Relocation base address
	 * 
	 * The relocation base address the object is currently relocated to. If the internal
     * relocation fixups for the module have been removed, this is the address the object
 	 * will be allocated at by the loader.
	 */
	public int base;
	
	/**
	 * 0001h = Readable Object.
	 */
	public final static int FLAG_READABLE = 0x1;
	
	/**
	 * 0002h = Writable Object.
	 */
	public final static int FLAG_WRITEABLE = 0x2;
	
	/**
	 * 0004h = Executable Object.
	 */
	public final static int FLAG_EXECUTABLE = 0x4;
	
	/**
	 * 0008h = Resource Object.
	 */
	public final static int FLAG_RESOURCE = 0x8;
	
	/**
	 * 0010h = Discardable Object.
	 */
	public final static int FLAG_DISCARDABLE = 0x10;
	
	/**
	 * 0020h = Object is Shared.
	 */
	public final static int FLAG_SHARED = 0x20;
	
	/**
	 * 0040h = Object has Preload Pages.
	 */
	public final static int FLAG_PRELOAD_PAGES = 0x40;
	
	/**
	 * 0080h = Object has Invalid Pages.
	 */
	public final static int FLAG_INVALID_PAGES = 0x80;
	
	/**
	 * 0100h = Object has Zero Filled Pages.
	 */
	public final static int FLAG_ZERO_PAGES = 0x100;
	
	/**
	 * 0200h = Object is Resident (valid for VDDs, PDDs only).
	 */
	public final static int FLAG_RESIDENT = 0x200;
	
	/**
	 * 0300h = Object is Resident & Contiguous (VDDs, PDDs only).
	 */
	public final static int FLAG_RESIDENT_CONTINOUS = 0x300;
	
	/**
	 * 0400h = Object is Resident & 'long-lockable' (VDDs, PDDs only).
	 */
	public final static int FLAG_RESIDENT_LONGLOCKABLE = 0x400;
	
	/**
	 * 0800h = Reserved for system use.
	 */
	public final static int FLAG_RESERVED = 0x800;
	
	/**
	 * 1000h = 16:16 Alias Required (80x86 Specific).
	 */
	public final static int FLAG_1616_ALIAS = 0x1000;
	
	/**
	 * 2000h = Big/Default Bit Setting (80x86 Specific).
	 * 
	 * The 'big/default' bit, for data segments, controls the setting of the Big bit in the segment descriptor. 
	 * (The Big bit, or B-bit, determines whether ESP or SP is used as the stack pointer.) 
	 * 
	 * For code segments, this bit controls the setting of the Default bit in the segment descriptor. 
	 * (The Default bit, or D-bit, determines whether the default word size is 32-bits or 16-bits.
	 * It also affects the interpretation of the instruction stream.)
	 */
	public final static int FLAG_BIG_DEFAULT_BIT = 0x2000;
	
	/**
	 * 4000h = Object is conforming for code (80x86 Specific).
	 */
	public final static int FLAG_CODE_CONFORMING = 0x4000;
	
	/**
	 * 8000h = Object I/O privilege level (80x86 Specific). Only used for 16:16 Alias Objects.
	 */
	public final static int FLAG_IO_PRIVILEGE_LEVEL = 0x8000;
	
	/**
	 * Object flags
	 * 
	 * The object flag bits have the following definitions.
	 * 
	 * 0001h = Readable Object.
	 * 0002h = Writable Object.
	 * 0004h = Executable Object.
	 * The readable, writable and executable flags provide support for all possible protections. 
	 * In systems where all of these protections are not supported, the loader will be responsible 
	 * for making the appropriate protection match for the system.
	 * 
	 * 0008h = Resource Object.
	 * 0010h = Discardable Object.
	 * 0020h = Object is Shared.
	 * 0040h = Object has Preload Pages.
	 * 0080h = Object has Invalid Pages.
	 * 0100h = Object has Zero Filled Pages.
	 * 0200h = Object is Resident (valid for VDDs, PDDs only).
	 * 0300h = Object is Resident & Contiguous (VDDs, PDDs only).
	 * 0400h = Object is Resident & 'long-lockable' (VDDs, PDDs only).
	 * 0800h = Reserved for system use.
	 * 1000h = 16:16 Alias Required (80x86 Specific).
	 * 2000h = Big/Default Bit Setting (80x86 Specific).
	 * The 'big/default' bit, for data segments, controls the setting of the Big bit in the segment descriptor. 
	 * (The Big bit, or B-bit, determines whether ESP or SP is used as the stack pointer.) 
	 * For code segments, this bit controls the setting of the Default bit in the segment descriptor. 
	 * (The Default bit, or D-bit, determines whether the default word size is 32-bits or 16-bits. 
	 * It also affects the interpretation of the instruction stream.)
	 * 
	 * 4000h = Object is conforming for code (80x86 Specific).
	 * 8000h = Object I/O privilege level (80x86 Specific). Only used for 16:16 Alias Objects.
	 */
	public int flags;
	
	/**
	 * Page table index
	 * 
	 * PAGE TABLE INDEX = DD Object Page Table Index.
	 * This specifies the number of the first object page table entry for this object. 
	 * The object page table specifies where in the EXE file a page can be found for a 
	 * given object and specifies per-page attributes.
	 * 
	 * The object table entries are ordered by logical page in the object table. 
	 * In other words the object table entries are sorted based on the object page table index value.
	 */
	public int pageTableIndex;
	
	/**
	 * Number of pages used for this object
	 * 
	 * # PAGE TABLE ENTRIES = DD # of object page table entries for this object.
	 * 
	 * Any logical pages at the end of an object that do not have an entry in the object page
	 * table associated with them are handled as zero filled or invalid pages by the loader.
	 * 
	 * When the last logical pages of an object are not specified with an object page table
	 * entry, they are treated as either zero filled pages or invalid pages based on the last
	 * entry in the object page table for that object. 
	 * 
	 * If the last entry was neither a zero filled or invalid page, then the 
	 * additional pages are treated as zero filled pages.
	 */
	public int pageCount;
	
}
