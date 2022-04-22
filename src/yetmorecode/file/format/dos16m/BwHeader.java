package yetmorecode.file.format.dos16m;

public class BwHeader {
	public static final short DOS16M_SIGNATURE = 0x5742;
	
	public short signature;          /* BW signature to mark valid file  */
    public short last_page_bytes;    /* length of image mod 512          */
    public short pages_in_file;      /* number of 512 byte pages         */
    public short reserved1;
    public short reserved2;
    public short min_alloc;          /* required memory, in KB           */
    public short max_alloc;          /* max KB (private allocation)      */
    public short stack_seg;          /* segment of stack                 */
    public short stack_ptr;          /* initial SP value                 */
    public short first_reloc_sel;    /* huge reloc list selector         */
    public short init_ip;            /* initial IP value                 */
    public short code_seg;           /* segment of code                  */
    public short runtime_gdt_size;   /* runtime GDT size in bytes        */
    public short MAKEPM_version;     /* ver * 100, GLU = (ver+10)*100    */
    /* end of DOS style EXE header */
    public int next_header_pos;    /* file pos of next spliced .EXP    */
    public int cv_info_offset;     /* offset to start of debug info    */
    public short last_sel_used;      /* last selector value used         */
    public short pmem_alloc;         /* private xm amount KB if nonzero  */
    public short alloc_incr;         /* auto ExtReserve amount, in KB    */
    public byte[] reserved4;
    /* the following used to be referenced as gdtimage[0..1] */
    public short options;            /* runtime options                  */
    public short trans_stack_sel;    /* sel of transparent stack         */
    public short exp_flags;          /* see ef_ constants below          */
    public short program_size;       /* size of program in paras         */
    public short gdtimage_size;      /* size of gdt in file (bytes)      */
    public short first_selector;     /* gdt[first_sel] = gdtimage[0], 0 => 0x80 */
    public byte default_mem_strategy;
    public byte reserved5;
    public short transfer_buffer_size;   /* default in bytes, 0 => 8KB   */
    /* the following used to be referenced as gdtimage[2..15] */
    public byte[] reserved6;
    public byte[] EXP_path;       /* original .EXP file name  */
    /* gdtimage[16..gdtimage_size] follows immediately, then program image follows */
}
