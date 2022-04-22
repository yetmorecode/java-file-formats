package yetmorecode.file.format.lx;

/**
 * The Object page table provides information about a logical page in an object. 
 * 
 * A logical page may be an enumerated page, a pseudo page or an iterated page. 
 * The structure of the object page table in conjunction with the structure of the object table 
 * allows for efficient access of a page when a page fault occurs, while still allowing the 
 * physical page data to be located in the preload page, demand load page or 
 * iterated data page sections in the linear EXE module. 
 * 
 * The logical page entries in the Object Page Table are numbered starting from one. 
 * 
 * The Object Page Table is parallel to the Fixup Page Table as they are both indexed by the logical page number.
 * 
 * Each Object Page Table entry has the following format:
 * 
 *    32                                  8 7          0
 *     +-----+-----+-----+-----+-----+-----+-----+-----+
 * 00h |             PAGE NUMBER           |   FLAGS   |
 *     +-----+-----+-----+-----+-----+-----+-----+-----+
 *     
 * @author https://github.com/yetmorecode
 *
 */
public class LePageTableEntry extends LinearPageTableEntry {
	/**
	 * Size of an LE object page table entry
	 */
	public final static int SIZE = 0x4;
}
