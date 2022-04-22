package yetmorecode.file.format.lx;

/**
 * LX/LE/LC executable module header
 * 
 * The LX module header may optionally be preceded by a DOS 2 compatible module header.
 * 
 * The DOS 2 compatible module header is identified by the first two bytes of the header
 * containing the signature characters “MZ”. If the DOS 2 compatible module header is absent
 * then the module will start with the LX module header.
 * 
 * If a module begins with a DOS 2 compatible module header, then the following technique
 * should be used to locate the LX module header, if present. The word at offset 18h in the
 * DOS 2 compatible module header is the offset to the DOS relocation table. If this offset is
 * 40h, then the doubleword at offset 3Ch in the DOS 2 compatible module header contains the
 * offset, from the beginning of the file, to the new module header. This may be an LX module
 * header and can be identified by the first two bytes of the header containing the signature
 * characters “LX”. If a valid module header is not found, then the file is a DOS 2 compatible
 * module.
 * 
 * The remainder of the DOS 2 compatible module header will describe a DOS stub program.
 * The stub may be any valid program but will typically be a program which displays an error
 * message. It may also be a DOS version of the LX program.
 * 
 *       00h +------------------+  <--+
 *           | DOS 2 Compatible |     |
 *           |    EXE Header    |     |
 *       1Ch +------------------+     |
 *           |      unused      |     |
 *           +------------------+     |
 *       24h |  OEM Identifier  |     |
 *       26h |  OEM Info        |     |
 *           |                  |     |-- DOS 2.0 Section
 *       3Ch |  Offset to       |     |   (Discarded)
 *           |  Linear EXE      |     |
 *           |  Header          |     |
 *       40h +------------------+     |
 *           |   DOS 2.0 Stub   |     |
 *           |   Program &      |     |
 *           |   Reloc. Table   |     |
 *           +------------------+  <--+
 *           |                  |
 *       xxh +------------------+  <--+
 *           |    Executable    |     |
 *           |       Info       |     |
 *           +------------------+     |
 *           |      Module      |     |
 *           |       Info       |     |
 *           +------------------+     |-- Linear Executable
 *           |  Loader Section  |     |   Module Header
 *           |       Info       |     |   (Resident)
 *           +------------------+     |
 *           |   Table Offset   |     |
 *           |       Info       |     |
 *           +------------------+  <--+
 *           |   Object Table   |     |
 *           +------------------+     |
 *           | Object Page Table|     |
 *           +------------------+     |
 *           |  Resource Table  |     |
 *           +------------------+     |
 *           |  Resident Name   |     |
 *           |      Table       |     |
 *           +------------------+     |-- Loader Section
 *           |   Entry Table    |     |   (Resident)
 *           +------------------+     |
 *           |   Module Format  |     |
 *           | Directives Table |     |
 *           |    (Optional)    |     |
 *           +------------------+     |
 *           |     Resident     |     |
 *           | Directives Data  |     |
 *           |    (Optional)    |     |
 *           |                  |     |
 *           |  (Verify Record) |     |
 *           +------------------+     |
 *           |     Per-Page     |     |
 *           |     Checksum     |     |
 *           +------------------+  <--+
 *           | Fixup Page Table |     |
 *           +------------------+     |
 *           |   Fixup Record   |     |
 *           |       Table      |     |
 *           +------------------+     |-- Fixup Section
 *           |   Import Module  |     |   (Optionally Resident)
 *           |    Name Table    |     |
 *           +------------------+     |
 *           | Import Procedure |     |
 *           |    Name Table    |     |
 *           +------------------+  <--+
 *           |   Preload Pages  |     |
 *           +------------------+     |
 *           |    Demand Load   |     |
 *           |       Pages      |     |
 *           +------------------+     |
 *           |  Iterated Pages  |     |
 *           +------------------+     |
 *           |   Non-Resident   |     |-- (Non-Resident)
 *           |    Name Table    |     |
 *           +------------------+     |
 *           |   Non-Resident   |     |
 *           | Directives Data  |     |
 *           |    (Optional)    |     |
 *           |                  |     |
 *           |  (To be Defined) |     |
 *           +------------------+  <--+
 *           |    Debug Info    |     |-- (Not used by Loader)
 *           +------------------+  <--+
 * 
 * 
 * 
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       00h | "L"   "X" |B-ORD|W-ORD|     FORMAT LEVEL      |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       08h | CPU TYPE  |  OS TYPE  |    MODULE VERSION     |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       10h |     MODULE FLAGS      |   MODULE # OF PAGES   |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       18h |     EIP OBJECT #      |          EIP          |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       20h |     ESP OBJECT #      |          ESP          |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       28h |       PAGE SIZE       |   PAGE OFFSET SHIFT   |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       30h |  FIXUP SECTION SIZE   | FIXUP SECTION CHECKSUM|
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       38h |  LOADER SECTION SIZE  |LOADER SECTION CHECKSUM|
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       40h |    OBJECT TABLE OFF   |  # OBJECTS IN MODULE  |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       48h | OBJECT PAGE TABLE OFF | OBJECT ITER PAGES OFF |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       50h | RESOURCE TABLE OFFSET |#RESOURCE TABLE ENTRIES|
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       58h | RESIDENT NAME TBL OFF |   ENTRY TABLE OFFSET  |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       60h | MODULE DIRECTIVES OFF | # MODULE DIRECTIVES   |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       68h | FIXUP PAGE TABLE OFF  |FIXUP RECORD TABLE OFF |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       70h | IMPORT MODULE TBL OFF | # IMPORT MOD ENTRIES  |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       78h |  IMPORT PROC TBL OFF  | PER-PAGE CHECKSUM OFF |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       80h |   DATA PAGES OFFSET   |    #PRELOAD PAGES     |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       88h | NON-RES NAME TBL OFF  | NON-RES NAME TBL LEN  |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       90h | NON-RES NAME TBL CKSM |   AUTO DS OBJECT #    |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       98h |    DEBUG INFO OFF     |    DEBUG INFO LEN     |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       A0h |   #INSTANCE PRELOAD   |   #INSTANCE DEMAND    |
 *           +-----+-----+-----+-----+-----+-----+-----+-----+
 *       A8h |       HEAPSIZE        |
 *           +-----+-----+-----+-----+
 *           
 * Note: 
 * The OBJECT ITER PAGES OFF must either be 0 or set to the same value as DATA PAGES OFFSET in OS/2 2.0. 
 * Ie., iterated pages are required to be in the same section of the file as regular pages.
 * 
 * Note: 
 * Table offsets in the Linear EXE Header may be set to zero to indicate that the table 
 * does not exist in the EXE file and its size is zero.
 *           
 * @author https://github.com/yetmorecode
 */
public class LinearHeader {
	public final static int SIZE = 0xb0;
	
	/**
	 * Signature for LX executables
	 */
	public final static short SIGNATURE_LX = 0x584c;
	
	/**
	 * Signature for LE (linear extended? / 32bit) executables
	 */
	public final static short SIGNATURE_LE = 0x454c;
	
	/**
	 * Signature for LC (linear compressed) executables
	 */
	public final static short SIGNATURE_LC = 0x434c;
	
	/**
	 * The magic number for LX/LE/LC executables
	 * 
	 * The signature word is used by the loader to identify the EXE file as a valid 32-bit
	 * Linear Executable Module Format. "L" is low order byte. "E", "C" or "X" is high order byte.
	 */
	public short signature;
	
	/**
	 * 00H - Little Endian Byte Ordering.
	 */
	public final static byte BYTEORDER_LE = 0;
	
	/**
	 * 01H - Big Endian Byte Ordering.
	 */
	public final static byte BYTEORDER_BE = 1;
	
	/**
	 * B-ORD = DB Byte Ordering.
	 * 
	 * This byte specifies the byte ordering for the linear EXE format. The values are:
	 * 00H - Little Endian Byte Ordering.
	 * 01H - Big Endian Byte Ordering.
	 */
	public byte byteOrdering;
	
	/**
	 * 00H - Little Endian Word Ordering.
	 */
	public final static byte WORDORDER_LE = 0;
	
	/**
	 * 01H - Big Endian Word Ordering.
	 */
	public final static byte WORDORDER_BE = 1;
	
	/**
	 * W-ORD = DB Word Ordering.
	 * 
	 * This byte specifies the Word ordering for the linear EXE format. The values are:
	 * 00H - Little Endian Word Ordering.
	 * 01H - Big Endian Word Ordering.
	 */
	public byte wordOrdering;
	
	/**
	 * Format Level = DD Linear EXE Format Level.
	 * 
	 * The Linear EXE Format Level is set to 0 for the initial version of the 32-bit linear EXE
	 * format. Each incompatible change to the linear EXE format must increment this
	 * value. This allows the system to recognized future EXE file versions so that an
	 * appropriate error message may be displayed if an attempt is made to load them.
	 */
	public int formatLevel;

	/**
	 * 01H - 80286 or upwardly compatible CPU is required to execute this module.
	 */
	public final static short CPU_80286 = 0x1;
	
	/**
	 * 02H - 80386 or upwardly compatible CPU is required to execute this module.
	 */
	public final static short CPU_80386 = 0x2;
	
	/**
	 * 03H - 80486 or upwardly compatible CPU is required to execute this module. 
	 */
	public final static short CPU_80486 = 0x3;
	
	/**
	 * CPU Type = DW Module CPU Type.
	 * 
	 * This field specifies the type of CPU required by this module to run. The values are:
	 * 01H - 80286 or upwardly compatible CPU is required to execute this module.
	 * 02H - 80386 or upwardly compatible CPU is required to execute this module.
	 * 03H - 80486 or upwardly compatible CPU is required to execute this module. 
	 */
	public short cpuType;
	
	/**
	 * 00H - Unknown (any “new-format” OS)
	 */
	public final static byte OSTYPE_UNKNOWN = 0x00;
	
	/**
	 * 01H - OS/2 (default)
	 */
	public final static byte OSTYPE_OS2 = 0x01;
	
	/**
	 * 02H - Windows1
	 */
	public final static byte OSTYPE_WINDOWS1 = 0x02;
	
	/**
	 * 03H - DOS 4.x
	 */
	public final static byte OSTYPE_DOS4 = 0x03;
	
	/**
	 * 04H - Windows 386
	 */
	public final static byte OSTYPE_WINDOWS386 = 0x04;
	
	/**
	 * 05H - IBM Microkernel Personality Neutral
	 */
	public final static byte OSTYPE_IBM = 0x05;
	
	/**
	 * OS Type = DW Module OS Type.
	 * 
	 * This field specifies the type of Operating system required to run this module. The
	 * currently defined values are:
	 * 00H - Unknown (any “new-format” OS)
	 * 01H - OS/2 (default)
	 * 02H - Windows1
	 * 03H - DOS 4.x
	 * 04H - Windows 386
	 * 05H - IBM Microkernel Personality Neutral
	 */
	public short osType;
	
	/**
	 * MODULE VERSION = DD Version of the linear EXE module.
	 * 
	 * This is useful for differentiating between revisions of dynamic linked modules. This
	 * value is specified at link time by the user.
	 */
	public int moduleVersion;
	
	/**
	 * MODULE FLAGS = DD Flag bits for the module.
	 * 
	 * The module flag bits have the following definitions.
	 * 00000001h = Reserved for system use.
	 * 00000002h = Reserved for system use.
	 * 00000004h = Per-Process Library Initialization.
	 *   The setting of this bit requires the EIP Object # and EIP fields to have valid
	 *   values. If the EIP Object # and EIP fields are valid and this bit is NOT set,
	 *   then Global Library Initialization is assumed. Setting this bit for an EXE file is
	 *   invalid.
	 * 00000008h = Reserved for system use.
	 * 00000010h = Internal fixups for the module have been applied.
	 *   The setting of this bit in a Linear Executable Module indicates that each
	 *   object of the module has a preferred load address specified in the Object
	 *   Table Reloc Base Addr. If the module’s objects can not be loaded at these
	 *   preferred addresses, then the relocation records that have been retained in
	 *   the file data will be applied.
	 * 00000020h = External fixups for the module have been applied.
	 * 00000040h = Reserved for system use.
	 * 00000080h = Reserved for system use.
	 * 00000100h = Incompatible with PM windowing.
	 * 00000200h = Compatible with PM windowing.
	 * 00000300h = Uses PM windowing API.
	 * 00000400h = Reserved for system use.
	 * 00000800h = Reserved for system use.
	 * 00001000h = Reserved for system use.
	 * 00002000h = Module is not loadable.
	 *   When the ‘Module is not loadable’ flag is set, it indicates that either errors
	 *   were detected at link time or that the module is being incrementally linked
	 *   and therefore can’t be loaded.
	 * 00004000h = Reserved for system use.
	 * 00038000h = Module type mask.
	 * 00000000h = Program module (EXE).
	 *   A module can not contain dynamic links to other modules that have the
	 *   ‘program module’ type.
	 * 00008000h = Library module (DLL).
	 * 00010000h = Reserved for system use.
	 * 00020000h = Physical Device Driver module.
	 * 00028000h = Virtual Device Driver module.
	 * 00080000h = MP-unsafe.
	 *   The program module is multiple-processor unsafe. It does not provide the
	 *    necessary serialization to run on more than one CPU at a time.
	 * 40000000h = Per-process Library Termination.
	 *   The setting of this bit requires the EIP Object # and EIP fields to have valid
	 *   values. If the EIP Object # and EIP fields are valid and this bit is NOT set,
	 *   then Global Library Termination is assumed. Setting this bit for an EXE file is
	 *   invalid.
	 */
	public int moduleFlags;
	public final static int MODULE_PHY_DEVICE_DRIVER = 0x20000;
	public final static int MODULE_VIR_DEVICE_DRIVER = 0x28000;
	public final static int MODULE_VXD = 0x38000;
	public final static int MODULE_LIBRARY = 0x8000;
	public final static int MODULE_TYPE_MASK = 0x38000;
	
	/**
	 * MODULE # PAGES = DD Physical number of pages in module.
	 * 
	 * This field specifies the number of pages physically contained in this module. In other
	 * words, pages containing either enumerated or iterated data, not invalid or zero-fill
	 * pages. These pages are contained in the ‘preload pages’, ‘demand load pages’ and
	 * ‘iterated data pages’ sections of the linear EXE module. This is used to determine
	 * the size of the other physical page based tables in the linear EXE module.
	 */
	public int pageCount;
	
	/**
	 * EIP OBJECT # = DD The Object number to which the Entry Address is relative.
	 * 
	 * This specifies the object to which the Entry Address is relative. This must be a
	 * nonzero value for a program module to be correctly loaded. A zero value for a library
	 * module indicates that no library entry routine exists. If this value is zero, then both
	 * the Per-process Library Initialization bit and the Per-process Library Termination bit
	 * must be clear in the module flags, or else the loader will fail to load the module.
	 * Further, if the Per-process Library Termination bit is set, then the object to which this
	 * field refers must be a 32-bit object (i.e., the Big/Default bit must be set in the object
	 * flags; see below).
	 */
	public int eipObject;
	
	/**
	 * EIP = DD Entry Address of module.
	 * 
	 * The Entry Address is the starting address for program modules and the library
	 * initialization and Library termination address for library modules.
	 */
	public int eip;
	
	/**
	 * ESP OBJECT # = DD The Object number to which the ESP is relative.
	 * 
	 * This specifies the object to which the starting ESP is relative. This must be a
	 * nonzero value for a program module to be correctly loaded. This field is ignored for a
	 * library module.
	 */
	public int espObject;
	
	/**
	 * ESP = DD Starting stack address of module.
	 * 
	 * The ESP defines the starting stack pointer address for program modules. A zero
	 * value in this field indicates that the stack pointer is to be initialized to the highest
	 * address/offset in the object. This field is ignored for a library module.
	 */
	public int esp;
	
	/**
	 * PAGE SIZE = DD The size of one page for this system.
	 * 
	 * This field specifies the page size used by the linear EXE format and the system. For
	 * the initial version of this linear EXE format the page size is 4Kbytes. (The 4K page
	 * size is specified by a value of 4096 in this field.)
	 */
	public int pageSize;
	
	/**
	 * PAGE OFFSET SHIFT = DD The shift left bits for page offsets. (only LX)
	 * 
	 * This field gives the number of bit positions to shift left when interpreting the Object
	 * Page Table entries’ page offset field. This determines the alignment of the page
	 * information in the file. For example, a value of 4 in this field would align all pages in
	 * the Data Pages and Iterated Pages sections on 16 byte (paragraph) boundaries. A
	 * Page Offset Shift of 9 would align all pages on a 512 byte (disk sector) basis. All
	 * other offsets are byte aligned.
	 * A page might not start at the next available alignment boundary. Extra padding is
	 * acceptable between pages as long as each page starts on an alignment boundary.
	 * For example, several alignment boundaries may be skipped in order to start a
	 * frequently accessed page on a sector boundary.
	 */
	public int pageOffsetShift;
	
	/**
	 * Bytes on last page (only LE) = DD
	 */
	public int lastPageSize;
	
	/**
	 * FIXUP SECTION SIZE = DD Total size of the fixup information in bytes.
	 * 
	 * This includes the following 4 tables:
	 * Fixup Page Table
	 * Fixup Record Table
	 * Import Module name Table
	 * Import Procedure Name Table
	 */
	public int fixupSectionSize;
	
	/**
	 * FIXUP SECTION CHECKSUM = DD Checksum for fixup information.
	 * 
	 * This is a cryptographic checksum covering all of the fixup information. The
	 * checksum for the fixup information is kept separate because the fixup data is not
	 * always loaded into main memory with the ‘loader section’. If the checksum feature is
	 * not implemented, then the linker will set these fields to zero.
	 */
	public int fixupSectionChecksum;
	
	/**
	 * LOADER SECTION SIZE = DD Size of memory resident tables.
	 * 
	 * This is the total size in bytes of the tables required to be memory resident for the
	 * module, while the module is in use. This total size includes all tables from the Object
	 * Table down to and including the Per-Page Checksum Table.
	 */
	public int loaderSectionSize;
	
	/**
	 * LOADER SECTION CHECKSUM = DD Checksum for loader section.
	 * 
	 * This is a cryptographic checksum covering all of the loader section information. If the
	 * checksum feature is not implemented, then the linker will set these fields to zero.
	 */
	public int loaderSectionChecksum;
	
	/**
	 * OBJECT TABLE OFF = DD Object Table offset.
	 * 
	 * This offset is relative to the beginning of the linear EXE header. This offset also
	 * points to the start of the Loader Section.
	 */
	public int objectTableOffset;
	
	/**
	 * # OBJECTS IN MODULE = DD Object Table Count.
	 * 
	 * This defines the number of entries in Object Table.
	 */
	public int objectCount;
	
	/**
	 * OBJECT PAGE TABLE OFFSET = DD Object Page Table offset
	 * 
	 * This offset is relative to the beginning of the linear EXE header.
	 */
	public int pageTableOffset;
	
	/**
	 * OBJECT ITER PAGES OFF = DD Object Iterated Pages offset.
	 * 
	 * This offset is relative to the beginning of the EXE file.
	 */
	public int iterPagesOffset;
	
	/**
	 * RESOURCE TABLE OFF = DD Resource Table offset.
	 * 
	 * This offset is relative to the beginning of the linear EXE header.
	 */
	public int resourceTableOffset;
	
	/**
	 * # RESOURCE TABLE ENTRIES = DD Number of entries in Resource Table.
	 */
	public int resourceCount;
	
	/**
	 * RESIDENT NAME TBL OFF = DD Resident Name Table offset.
	 * 
	 * This offset is relative to the beginning of the linear EXE header.
	 */
	public int residentNameTableOffset;
	
	/**
	 * ENTRY TBL OFF = DD Entry Table offset.
	 * 
	 * This offset is relative to the beginning of the linear EXE header.
	 */
	public int entryTableOffset;
	
	/**
	 * MODULE DIRECTIVES OFF = DD Module Format Directives Table offset.
	 * 
	 * This offset is relative to the beginning of the linear EXE header.
	 */
	public int directivesTableOffset;
	
	/**
	 * #MODULE DIRECTIVES = DD Number of Module Format Directives in the Table.
	 * 
	 * This field specifies the number of entries in the Module Format Directives Table. 
	 */
	public int directivesCount;
	
	/**
	 * FIXUP PAGE TABLE OFF = DD Fixup Page Table offset.
	 * 
	 * This offset is relative to the beginning of the linear EXE header. This offset also
	 * points to the start of the Fixup Section.
	 */
	public int fixupPageTableOffset;
	
	/**
	 * FIXUP RECORD TABLE OFF = DD Fixup Record Table Offset
	 * 
	 * This offset is relative to the beginning of the linear EXE header.
	 */
	public int fixupRecordTableOffset;
	
	/**
	 * IMPORT MODULE TBL OFF = DD Import Module Name Table offset.
	 * 
	 * This offset is relative to the beginning of the linear EXE header.
	 */
	public int importModuleNameTableOffset;
	
	/**
	 * # IMPORT MOD ENTRIES = DD The number of entries in the Import Module Name Table.
	 */
	public int importModuleNameCount;
	
	/**
	 * IMPORT PROC TBL OFF = DD Import Procedure Name Table offset.
	 * 
	 * This offset is relative to the beginning of the linear EXE header.
	 */
	public int importProcedureNameTableOffset;
	
	/**
	 * PER-PAGE CHECKSUM OFF = DD Per-Page Checksum Table offset.
	 * 
	 * This offset is relative to the beginning of the linear EXE header.
	 */
	public int checksumTableOffset;
	
	/**
	 * DATA PAGES OFFSET = DD Data Pages Offset.
	 * 
	 * This offset is relative to the beginning of the EXE file. This offset also points to the
	 * start of the Data Section.
	 */
	public int dataPagesOffset;
	
	/**
	 * # PRELOAD PAGES = DD Number of Preload pages for this module.
	 * 
	 * Note: OS/2 2.0 does not respect the preload of pages as specified in the
	 * executable file for performance reasons.
	 */
	public int preloadPagesCount;
	
	/**
	 * NON-RES NAME TBL OFF = DD Non-Resident Name Table offset.
	 * 
	 * This offset is relative to the beginning of the EXE file.
	 */
	public int nameTableOffset;
	
	/**
	 * NON-RES NAME TBL LEN = DD Number of bytes in the Non-resident name table.
	 */
	public int nameTableLength;
	
	/**
	 * NON-RES NAME TBL CKSM = DD Non-Resident Name Table Checksum.
	 * 
	 * This is a cryptographic checksum of the Non-Resident Name Table.
	 */
	public int nameTableChecksum;
	
	/**
	 * AUTO DS OBJECT # = DD The Auto Data Segment Object number.
	 * 
	 * This is the object number for the Auto Data Segment used by 16-bit modules. This
	 * field is supported for 16-bit compatibility only and is not used by 32-bit modules.
	 */
	public int autoDataSegmentObjectNumber;
	
	/**
	 * DEBUG INFO OFF = DD Debug Information offset.
	 * 
	 * This offset is relative to the beginning of the file. This offset also points to the start of the
	 * Debug Section.
	 * 
	 * Note: Earlier versions of this document stated that this offset was from the linear
	 * EXE header - this is incorrect.
	 */
	public int debugOffset;
	
	/**
	 * DEBUG INFO LEN = DD Debug Information length.<br>
	 * <br>
	 * The length of the debug information in bytes.
	 */
	public int debugLength;
	
	/**
	 * # INSTANCE PRELOAD = DD Instance pages in preload section.
	 * 
	 * The number of instance data pages found in the preload section.
	 */
	public int pagesInPreloadSectionCount;
	
	/**
	 * # INSTANCE DEMAND = DD Instance pages in demand section.
	 * 
	 * The number of instance data pages found in the demand section.
	 */
	public int pagesInDemandSectionCount;
	
	/**
	 * HEAPSIZE = DD Heap size added to the Auto DS Object.
	 * 
	 * The heap size is the number of bytes added to the Auto Data Segment by the loader.
	 * This field is supported for 16-bit compatibility only and is not used by 32-bit modules.
	 */
	public int heapSize;
	
	/**
	 * STACKSIZE = DD Stack size.
	 * 
	 * The stack size is the number of bytes specified by:
	 * 1. size of a segment with combine type stack
	 * 2. STACKSIZE in the .DEF file
	 * 3. /STACK link option
	 * The stacksize may be zero.
	 * 
	 * Note: 
	 * Stack sizes with byte 2 equal to 02 or 04 (e.g. 00020000h, 11041111h,
	 * 0f02ffffh) should be avoided for programs that will run on OS/2 2.0.
	 */
	public int stackSize;
	
	public byte[] res3;
    /* Pad structure to 192 bytes */
	public long winresoff ;
	public long winreslen ;
	public short Dev386_Device_ID;
    /* Device ID for VxD */
	public short Dev386_DDK_Version;
    /* DDK version for VxD */
	

	public LinearHeader() {}
	
	public boolean isLe() {
		return signature == SIGNATURE_LE;
	}
	
	public boolean isLx() {
		return signature == SIGNATURE_LX;
	}
	
	public boolean isLc() {
		return signature == SIGNATURE_LC;
	}
}
