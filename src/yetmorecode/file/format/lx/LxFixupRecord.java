package yetmorecode.file.format.lx;

import java.util.ArrayList;

/**
 * A fixup record in the fixup record table<br>
 * <br>
 * The Fixup Record Table contains entries for all fixups in the linear EXE module. The fixup
 * records for a logical page are grouped together and kept in sorted order by logical page
 * number.<br>
 * The fixups for each page are further sorted such that all external fixups and internal
 * selector/pointer fixups come before internal non-selector/non-pointer fixups. This allows the
 * loader to ignore internal fixups if the loader is able to load all objects at the addresses
 * specified in the object table.<br>
 * Each relocation record has the following format:<br>
 * <br>
 * <pre>
 * +-----+-----+-----+-----+
 * | SRC |FLAGS|SRCOFF/CNT*|
 * |           TARGET DATA *           |
 * | SRCOFF1 @ |   . . .   | SRCOFFn @ |
 * +-----+-----+----   ----+-----+-----+
 * </pre>
 * Target data will differ for different fixup types:<br>
 * <br>
 * Internal Fixup Record:<br>
 * <pre>
 * +-----+-----+-----+-----+
 * | SRC |FLAGS|SRCOFF/CNT*|
 * |  OBJECT * |        TRGOFF * @     |
 * | SRCOFF1 @ |   . . .   | SRCOFFn @ |
 * +-----+-----+----   ----+-----+-----+
 * </pre>
 * <br>
 * Import by Ordinal Fixup Record:
 * <pre>
 * +-----+-----+-----+-----+
 * | SRC |FLAGS|SRCOFF/CNT*|
 * | MOD ORD# *|IMPORT ORD*|     ADDITIVE * @      |
 * | SRCOFF1 @ |   . . .   | SRCOFFn @ |
 * +-----+-----+----   ----+-----+-----+
 * </pre>
 * 
 * 
 * Import by Name Fixup Record:
 * <pre>
 * +-----+-----+-----+-----+
 * | SRC |FLAGS|SRCOFF/CNT*|
 * | MOD ORD# *| PROCEDURE NAME OFFSET*|     ADDITIVE * @      |
 * | SRCOFF1 @ |   . . .   | SRCOFFn @ |
 * +-----+-----+----   ----+-----+-----+
 * </pre>
 * 
 * Internal Entry Table Fixup Record:
 * <pre>
 * +-----+-----+-----+-----+
 * | SRC |FLAGS|SRCOFF/CNT*|
 * |  ORD # *  |     ADDITIVE * @      |
 * | SRCOFF1 @ |   . . .   | SRCOFFn @ |
 * +-----+-----+----   ----+-----+-----+
 * </pre>
 * 
 * 3.13.5 Internal Chaining Fixups<br>
 * <br>
 * Internal chaining fixups are 32-bit offset fixups (source type 07h) to internal references
 * (target types 00h and 03h) where the first fixup in the chain is a record in the Fixup Record
 * Table and the remaining fixups are located in the page referenced by the first fixup rather
 * than as records in the Fixup Record Table. The chain is a linked-list with each fixup pointing
 * to the next fixup in the chain until the end of the chain is reached. All fixups in the chain must
 * fixup source offsets within the same page. All fixups in the chain must reference targets
 * within the same memory object. All target references within a chain are also restricted to be
 * within a 1 MB virtual memory range. Of course multiple chains can be used to meet the
 * restrictions on an individual chain.<br>
 * The first fixup in the chain is contained in a record in the Fixup Record Table with the
 * ‘Internal Chaining Fixup Flag’ bit in the target flags field set. This fixup is otherwise normal
 * except that the 32-bit source location contains information about this fixup and the next fixup
 * in the chain.<br>
 * Before applying an internal chaining fixup, the 32-bit value at the source location should be
 * read. The first 12-bits of the value contain the source offset, within the page, for the next
 * fixup in the chain and the remaining 20-bits contain the target offset of the current fixup.
 * 
 * @author https://github.com/yetmorecode
 *
 */
public class LxFixupRecord {
	/**
	 * 0Fh = Source mask
	 */
	public final static byte SOURCE_MASK = 0xf;
	
	/**
	 * 00h = Byte fixup (8-bits)
	 */
	public final static byte SOURCE_BYTE_FIXUP = 0x0;
	
	/**
	 * 02h = 16-bit Selector fixup (16-bits)
	 */
	public final static byte SOURCE_16BIT_SELECTOR_FIXUP = 0x2;
	
	/**
	 * 03h = 16:16 Pointer fixup (32-bits)
	 */
	public final static byte SOURCE_1616PTR_FIXUP = 0x3;
	
	/**
	 * 05h = 16-bit Offset fixup (16-bits)
	 */
	public final static byte SOURCE_16BIT_OFFSET_FIXUP = 0x5;
	
	/**
	 * 06h = 16:32 Pointer fixup (48-bits)
	 */
	public final static byte SOURCE_1632PTR_FIXUP = 0x6;
	
	/**
	 * 07h = 32-bit Offset fixup (32-bits)
	 */
	public final static byte SOURCE_32BIT_OFFSET_FIXUP = 0x7;
	
	/**
	 * 08h = 32-bit Self-relative offset fixup (32-bits)
	 */
	public final static byte SOURCE_32BIT__SELF_REF_OFFSET_FIXUP = 0x8;
	
	/**
	 * 10h = Fixup to Alias Flag.<br>
	 * When the ‘Fixup to Alias’ Flag is set, the source fixup refers to the 16:16
	 * alias for the object. This is only valid for source types of 2, 3, and 6. For
	 * fixups such as this, the linker and loader will be required to perform
	 * additional checks such as ensuring that the target offset for this fixup is less
	 * than 64K
	 */
	public final static byte SOURCE_ALIAS_FLAG = 0x10;
	
	/**
	 * 20h = Source List Flag.<br>
	 * When the ‘Source List’ Flag is set, the SRCOFF field is compressed to a
	 * byte and contains the number of source offsets, and a list of source offsets
	 * follows the end of fixup record (after the optional additive value).
	 */
	public final static byte SOURCE_SOURCE_LIST = 0x20;
	
	/**
	 * SRC = DB Source type.<br>
	 * <br>
	 * The source type specifies the size and type of the fixup to be performed on the fixup
	 * source. The source type is defined as follows:<br>
	 * 0Fh = Source mask.<br>
	 * 00h = Byte fixup (8-bits).<br>
	 * 01h = (undefined).<br>
	 * 02h = 16-bit Selector fixup (16-bits).<br>
	 * 03h = 16:16 Pointer fixup (32-bits).<br>
	 * 04h = (undefined).<br>
	 * 05h = 16-bit Offset fixup (16-bits).<br>
	 * 06h = 16:32 Pointer fixup (48-bits).<br>
	 * 07h = 32-bit Offset fixup (32-bits).<br>
	 * 08h = 32-bit Self-relative offset fixup (32-bits).<br>
	 * 10h = Fixup to Alias Flag.<br>
	 * When the ‘Fixup to Alias’ Flag is set, the source fixup refers to the 16:16
	 * alias for the object. This is only valid for source types of 2, 3, and 6. For
	 * fixups such as this, the linker and loader will be required to perform
	 * additional checks such as ensuring that the target offset for this fixup is less
	 * than 64K.<br>
	 * 20h = Source List Flag.<br>
	 * When the ‘Source List’ Flag is set, the SRCOFF field is compressed to a
	 * byte and contains the number of source offsets, and a list of source offsets
	 * follows the end of fixup record (after the optional additive value).
	 */
	public byte sourceType;
	
	/**
	 * 03h = Fixup target type mask
	 */
	public final static byte TARGET_TYPE_MASK = 0x3;

	/**
	 * 00h = Internal reference
	 */
	public final static byte TARGET_INTERNAL_REF = 0x0;

	/**
	 * 01h = Imported reference by ordinal
	 */
	public final static byte TARGET_IMPORT_ORDINAL = 0x1;

	/**
	 * 02h = Imported reference by name
	 */
	public final static byte TARGET_IMPORT_NAME = 0x2;

	/**
	 * 03h = Internal reference via entry table
	 */
	public final static byte TARGET_IMPORT_ENTRY = 0x3;

	/**
	 * 04h = Additive Fixup Flag.<br>
	 * When set, an additive value trails the fixup record (before the optional
	 * source offset list).
	 */
	public final static byte TARGET_ADDITIVE_FIXUP = 0x4;

	/**
	 * 08h = Internal Chaining Fixup Flag.<br>
	 * When set, this bit indicates that this fixup record is the beginning of a chain
	 * of fixups. The remaining fixups in the chain are contained within the page
	 * rather than as additional entries in the fixup record table. See § 3.13.5,
	 * “Internal Chaining Fixups”, on page 86 for additional details on this fixup
	 * type.<br>
	 * Setting this bit is only valid for fixups of source type 07h (32-bit Offset) and
	 * target types 00h and 03h (Internal references). This bit is only valid if the
	 * Source List Flag is not set
	 */
	public final static byte TARGET_CHAINING = 0x8;

	/**
	 * 10h = 32-bit Target Offset Flag.<br>
	 * When set, the target offset is 32-bits, otherwise it is 16-bits
	 */
	public final static byte TARGET_32BIT_OFFSET = 0x10;

	/**
	 * 20h = 32-bit Additive Fixup Flag.<br>
	 * When set, the additive value is 32-bits, otherwise it is 16-bits
	 */
	public final static byte TARGET_32BIT_ADDITIVE = 0x20;

	/**
	 * 40h = 16-bit Object Number/Module Ordinal Flag.<br>
	 * When set, the object number or module ordinal number is 16-bits, otherwise
	 * it is 8-bits
	 */
	public final static byte TARGET_16BIT_OBJECT = 0x40;

	/**
	 * 80h = 8-bit Ordinal Flag.<br>
	 * When set, the ordinal number is 8-bits, otherwise it is 16-bits
	 */
	public final static byte TARGET_8BIT_ORDINAL = (byte) 0x80;
	
	/**
	 * FLAGS = DB Target Flags.<br>
	 * <br>
	 * The target flags specify how the target information is interpreted. The target flags are
	 * defined as follows:<br>
	 * 03h = Fixup target type mask.<br>
	 * 00h = Internal reference.<br>
	 * 01h = Imported reference by ordinal.<br>
	 * 02h = Imported reference by name.<br>
	 * 03h = Internal reference via entry table.<br>
	 * 04h = Additive Fixup Flag.<br>
	 * When set, an additive value trails the fixup record (before the optional
	 * source offset list).<br>
	 * 08h = Internal Chaining Fixup Flag.<br>
	 * When set, this bit indicates that this fixup record is the beginning of a chain
	 * of fixups. The remaining fixups in the chain are contained within the page
	 * rather than as additional entries in the fixup record table. See § 3.13.5,
	 * “Internal Chaining Fixups”, on page 86 for additional details on this fixup
	 * type.<br>
	 * Setting this bit is only valid for fixups of source type 07h (32-bit Offset) and
	 * target types 00h and 03h (Internal references). This bit is only valid if the
	 * Source List Flag is not set.<br>
	 * 10h = 32-bit Target Offset Flag.<br>
	 * When set, the target offset is 32-bits, otherwise it is 16-bits.<br>
	 * 20h = 32-bit Additive Fixup Flag.<br>
	 * When set, the additive value is 32-bits, otherwise it is 16-bits.<br>
	 * 40h = 16-bit Object Number/Module Ordinal Flag.<br>
	 * When set, the object number or module ordinal number is 16-bits, otherwise
	 * it is 8-bits.<br>
	 * 80h = 8-bit Ordinal Flag.<br>
	 * When set, the ordinal number is 8-bits, otherwise it is 16-bits.
	 */
	public byte targetFlags;
	
	/**
	 * SRCOFF = DW/CNT = DB Source offset or source offset list count.<br>
	 * <br>
	 * This field contains either an offset or a count depending on the Source List Flag. If
	 * the Source List Flag is set, a list of source offsets follows the additive field and this
	 * field contains the count of the entries in the source offset list. Otherwise, this is the
	 * single source offset for the fixup. Source offsets are relative to the beginning of the
	 * page where the fixup is to be made.<br>
	 * Note: For fixups that cross page boundaries, a separate fixup record is specified
	 * for each page. An offset is still used for the 2nd page but it now becomes a
	 * negative offset since the fixup originated on the preceding page. (For
	 * example, if only the last one byte of a 32-bit address is on the page to be
	 * fixed up, then the offset would have a value of -3.)
	 */
	public short sourceOffset;
	
	/**
	 * SRCOFF1 - SRCOFFn = DW[] Source offset list.<br>
	 * <br>
	 * This list is present if the Source List Flag is set in the Target Flags field. The number
	 * of entries in the source offset list is defined in the SRCOFF/CNT field. The source
	 * offsets are relative to the beginning of the page where the fixups are to be made.
	 */
	public ArrayList<Short> sourceList = new ArrayList<Short>();
	
	// internal fixup target data (offset & selector fixups)
	/**
	 * OBJECT = D[B|W] Target object number.<br>
	 * <br>
	 * This field is an index into the current module’s Object Table to specify the target
	 * Object. It is a Byte value when the ‘16-bit Object Number/Module Ordinal Flag’ bit in
	 * the target flags field is clear and a Word value when the bit is set.
	 */
	public short objectNumber;
	
	/**
	 * TRGOFF = D[W|D] Target offset.<br>
	 * <br>
	 * This field is an offset into the specified target Object. It is not present when the
	 * Source Type specifies a 16-bit Selector fixup. It is a Word value when the ‘32-bit
	 * Target Offset Flag’ bit in the target flags field is clear and a Dword value when the bit
	 * is set.
	 */
	public int targetOffset;
	
	// import by ordinal or name, internal entry fixup
	/**
	 * MOD ORD # = D[B|W] Ordinal index into the Import Module Name Table.<br>
	 * <br>
	 * This value is an ordered index in to the Import Module Name Table for the module
	 * containing the procedure entry point. It is a Byte value when the ‘16-bit Object
	 * Number/Module Ordinal’ Flag bit in the target flags field is clear and a Word value
	 * when the bit is set. The loader creates a table of pointers with each pointer in the
	 * table corresponds to the modules named in the Import Module Name Table. This
	 * value is used by the loader to index into this table created by the loader to locate the
	 * referenced module.
	 */
	public short ordinalIndex;
	
	/**
	 * IMPORT ORD = D[B|W|D] Imported ordinal number.<br>
	 * <br>
	 * This is the imported procedure’s ordinal number. It is a Byte value when the ‘8-bit
	 * Ordinal’ bit in the target flags field is set. Otherwise it is a Word value when the ‘32-
	 * bit Target Offset Flag’ bit in the target flags field is clear and a Dword value when the
	 * bit is set.
	 */
	public int ordinalNumber;
	
	/**
	 * PROCEDURE NAME OFFSET = D[W|D] Offset into the Import Procedure Name Table.<br>
	 * <br>
	 * This field is an offset into the Import Procedure Name Table. It is a Word value when
	 * the ‘32-bit Target Offset Flag’ bit in the target flags field is clear and a Dword value
	 * when the bit is set.
	 */
	public int procedureNameTableOffset;
	
	/**
	 * ADDITIVE = D[W|D] Additive fixup value.<br>
	 * <br>
	 * This field exists in the fixup record only when the ‘Additive Fixup Flag’ bit in the
	 * target flags field is set. When the ‘Additive Fixup Flag’ is clear the fixup record does
	 * not contain this field and is immediately followed by the next fixup record (or by the
	 * source offset list for this fixup record).<br>
	 * This value is added to the address derived from the target entry point. This field is a
	 * Word value when the ‘32-bit Additive Flag’ bit in the target flags field is clear and a
	 * Dword value when the bit is set.
	 */
	public int additive;
	
	public int getSize() {
		int size = 2;
		if ((sourceType & SOURCE_SOURCE_LIST) > 0) {
			size++;
		} else {
			size += 2;
		}
				
		// target data
		switch (sourceType & SOURCE_MASK) {
		case SOURCE_32BIT_OFFSET_FIXUP:
			if ((targetFlags & TARGET_16BIT_OBJECT) > 0) {
				size += 2;
			} else {
				size++;
			}
			
			if ((targetFlags & TARGET_32BIT_OFFSET) > 0) {
				size += 4;
			} else {
				size += 2;
			}
			break;
		case LxFixupRecord.SOURCE_16BIT_SELECTOR_FIXUP:
			if ((targetFlags & TARGET_16BIT_OBJECT) > 0) {
				size += 2;
			} else {
				size++;
			}
			break;
		default:
		}
		
		// source list
		if ((sourceType & SOURCE_SOURCE_LIST) > 0) {
			size += 2 * sourceOffset;
		}
		return size;
	}
}
