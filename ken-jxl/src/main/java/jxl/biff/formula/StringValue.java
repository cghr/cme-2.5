/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import jxl.WorkbookSettings;
/*   4:    */ import jxl.biff.StringHelper;
/*   5:    */ import jxl.common.Logger;
/*   6:    */ 
/*   7:    */ class StringValue
/*   8:    */   extends Operand
/*   9:    */   implements ParsedThing
/*  10:    */ {
/*  11: 37 */   private static final Logger logger = Logger.getLogger(StringValue.class);
/*  12:    */   private String value;
/*  13:    */   private WorkbookSettings settings;
/*  14:    */   
/*  15:    */   public StringValue(WorkbookSettings ws)
/*  16:    */   {
/*  17: 54 */     this.settings = ws;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public StringValue(String s)
/*  21:    */   {
/*  22: 65 */     this.value = s;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public int read(byte[] data, int pos)
/*  26:    */   {
/*  27: 77 */     int length = data[pos] & 0xFF;
/*  28: 78 */     int consumed = 2;
/*  29: 80 */     if ((data[(pos + 1)] & 0x1) == 0)
/*  30:    */     {
/*  31: 82 */       this.value = StringHelper.getString(data, length, pos + 2, this.settings);
/*  32: 83 */       consumed += length;
/*  33:    */     }
/*  34:    */     else
/*  35:    */     {
/*  36: 87 */       this.value = StringHelper.getUnicodeString(data, length, pos + 2);
/*  37: 88 */       consumed += length * 2;
/*  38:    */     }
/*  39: 91 */     return consumed;
/*  40:    */   }
/*  41:    */   
/*  42:    */   byte[] getBytes()
/*  43:    */   {
/*  44:101 */     byte[] data = new byte[this.value.length() * 2 + 3];
/*  45:102 */     data[0] = Token.STRING.getCode();
/*  46:103 */     data[1] = ((byte)this.value.length());
/*  47:104 */     data[2] = 1;
/*  48:105 */     StringHelper.getUnicodeBytes(this.value, data, 3);
/*  49:    */     
/*  50:107 */     return data;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void getString(StringBuffer buf)
/*  54:    */   {
/*  55:118 */     buf.append("\"");
/*  56:119 */     buf.append(this.value);
/*  57:120 */     buf.append("\"");
/*  58:    */   }
/*  59:    */   
/*  60:    */   void handleImportedCellReferences() {}
/*  61:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.StringValue
 * JD-Core Version:    0.7.0.1
 */