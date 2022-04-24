package yetmorecode.file.format.vxd;

public class DescriptionBlock {
	public int DDB_Next;         /* VMM RESERVED FIELD */
    public short DDB_SDK_Version;     /* INIT <DDK_VERSION> RESERVED FIELD */
    public short DDB_Req_Device_Number;   /* INIT <UNDEFINED_DEVICE_ID> */
    public byte DDB_Dev_Major_Version;    /* INIT <0> Major device number */
    public byte DDB_Dev_Minor_Version;    /* INIT <0> Minor device number */
    public short DDB_Flags;           /* INIT <0> for init calls complete */
    public byte[] DDB_Name;          /* 8 bytes AINIT <"        "> Device name */
    public int DDB_Init_Order;       /* INIT <UNDEFINED_INIT_ORDER> */
    public int DDB_Control_Proc;     /* Offset of control procedure */
    public int DDB_V86_API_Proc;     /* INIT <0> Offset of API procedure */
    public int DDB_PM_API_Proc;      /* INIT <0> Offset of API procedure */
    public int DDB_V86_API_CSIP;     /* INIT <0> CS:IP of API entry point */
    public int DDB_PM_API_CSIP;      /* INIT <0> CS:IP of API entry point */
    public int DDB_Reference_Data;       /* Reference data from real mode */
    public int DDB_Service_Table_Ptr;    /* INIT <0> Pointer to service table */
    public int DDB_Service_Table_Size;   /* INIT <0> Number of services */
    public int DDB_Win32_Service_Table;  /* INIT <0> Pointer to Win32 services */
    public int DDB_Prev;         /* INIT <'Prev'> Ptr to prev 4.0 DDB */
    public int DDB_Reserved0;        /* INIT <0> Reserved */
    public int DDB_Reserved1;        /* INIT <'Rsv1'> Reserved */
    public int DDB_Reserved2;        /* INIT <'Rsv2'> Reserved */
    public int DDB_Reserved3;        /* INIT <'Rsv3'> Reserved */
}
