package yetmorecode.file.format.mz;

import java.io.IOException;

import yetmorecode.file.BinaryFileInputStream;

/**
 * MS-DOS MZ header<br>
 * <br>
 * This class represents the IMAGE_DOS_HEADER struct as defined in winnt.h<br>
 * The program's data begins just after the header.<br>
 * The header includes the relocation entries. <br>
 * Note that some OSs and/or programs may fail if the header is not a multiple of 512 bytes<br>
 * <pre>
 *     +-----+-----+-----+-----+-----+-----+-----+-----+
 * 00h |   MAGIC   |BYTE LAST P|   BLOCKS  |   RELOCS  |
 * 08h |   SIZE    | MIN XTRA  |  MAX XTRA |    SS     |
 * 10h |     SP    |  CHKSUM   |     IP    |    CS     |
 * 18h |  RLC ADDR |    OVL#   |       RESERVED        |
 * 20h |       RESERVED        |   OEMID   |  OEMINFO  |
 * 28h |                    RESERVED                   |
 * 30h |                    RESERVED                   |
 * 38h |       RESERVED        |        LFA EXE        |
 *     +-----+-----+-----+-----+-----+-----+-----+-----+
 * </pre>
 * 
 * @author https://github.com/yetmorecode
 */
public class MzHeader {
	public final static int BLOCK_SIZE = 0x200;
	public final static int PARAGRAPH_SIZE = 0x10;
	
	/**
	 * MZ header signature
	 */
	public final static int SIGNATURE_DOS = 0x5A4D;
	
	/**
	 * Size of a MZ header
	 */
    public final static int SIZE = 0x40;

    /**
     * MZ signature.
     */
    public short signature; 

    /**
     * The number of bytes in the last block of the program that are actually used.<br> 
     * If this value is zero, that means the entire last block is used (i.e. the effective value is 512).
     */
    public short bytesOnLastBlock; 

    /**
     * Number of block in the file that are part of the EXE file.<br>
     * If bytesOnLastBlock is non-zero, only that much of the last block is used.
     */
    public short blockCount; 

    /**
     * Number of relocation entries stored after the header. May be zero.
     */
    public short relocations; 

    /**
     * Number of paragraphs in the header.<br>
     * <br>
     * The program's data begins just after the header, and this field can be used to<br>
     * calculate the appropriate file offset.<br>
     * The header includes the relocation entries.<br>
     * Note that some OSs and/or programs may fail if the header is not a multiple of 512 bytes.
     */
    public short headerSize; 

    /**
     * Number of paragraphs of additional memory that the program will need.<br>
     * <br>
     * This is the equivalent of the BSS size in a Unix program.<br>
     * The program can't be loaded if there isn't at least this much memory available to it.
     */
    public short minExtraParagraphs; 

    /**
     * Maximum number of paragraphs of additional memory.<br>
     * <br>
     * Normally, the OS reserves all the remaining conventional memory for your program,<br>
     * but you can limit it with this field.
     */
    public short maxExtraParagraphs;          

    /**
     * Relative value of the stack segment.<br>This value is added to the segment the program was loaded at, and the result is used to initialize the SS register.
     */
    public short ss;            

    /**
     * Initial value of the SP register.
     */
    public short sp; 

    /**
     * Word checksum. If set properly, the 16-bit sum of all words in the file should be zero. Usually, this isn't filled in.
     */
    public short checksum; 

    /**
     * Initial value of the IP register.
     */
    public short ip; 

    /**
     * Initial value of the CS register, relative to the segment the program was loaded at.
     */
    public short cs; 

    /**
     * Offset of the first relocation item in the file (relocation table).
     */
    public short relocationTableOffset; 

    /**
     * Overlay number. Normally zero, meaning that it's the main program.
     */
    public short overlayNumber; 

    /**
     * Reserved words
     */
    public short [] reserved = new short[4]; 

    /**
     * OEM identifier (for e_oeminfo)
     */
    public short oemId;            

    /**
     * OEM information; e_oemid specific
     */
    public short oemInfo;             

    /**
     * Reserved words
     */
    public short [] resreved2 = new short[10]; 

    /**
     * File address of new exe header
     */
    public int   fileAddressNewExe; 		

    /**
     * DOS-stub bytes
     */
	public byte [] stubBytes;
	
	public static MzHeader fromStream(BinaryFileInputStream input, long offset) throws IOException {
		var header = new MzHeader();
		var old = input.position(offset);
		header.signature = input.readShort();
		header.bytesOnLastBlock = input.readShort();
		header.blockCount = input.readShort();
		header.relocations = input.readShort();
		header.headerSize = input.readShort();
		header.minExtraParagraphs = input.readShort();
		header.maxExtraParagraphs = input.readShort();
		header.ss = input.readShort();
		header.sp = input.readShort();
		header.checksum = input.readShort();
		header.ip = input.readShort();
		header.cs = input.readShort();
		header.relocationTableOffset = input.readShort();
		header.overlayNumber = input.readShort();
		input.skip(8);
		header.oemId = input.readShort();
		header.oemInfo = input.readShort();
		input.skip(20);
		header.fileAddressNewExe = input.readInt();
		input.position(old);
		return header;
	}
}
