/*  1:   */ package jxl.biff.formula;
/*  2:   */ 
/*  3:   */ class BooleanValue
/*  4:   */   extends Operand
/*  5:   */   implements ParsedThing
/*  6:   */ {
/*  7:   */   private boolean value;
/*  8:   */   
/*  9:   */   public BooleanValue() {}
/* 10:   */   
/* 11:   */   public BooleanValue(String s)
/* 12:   */   {
/* 13:47 */     this.value = Boolean.valueOf(s).booleanValue();
/* 14:   */   }
/* 15:   */   
/* 16:   */   public int read(byte[] data, int pos)
/* 17:   */   {
/* 18:60 */     this.value = (data[pos] == 1);
/* 19:61 */     return 1;
/* 20:   */   }
/* 21:   */   
/* 22:   */   byte[] getBytes()
/* 23:   */   {
/* 24:71 */     byte[] data = new byte[2];
/* 25:72 */     data[0] = Token.BOOL.getCode();
/* 26:73 */     data[1] = ((byte)(this.value ? 1 : 0));
/* 27:   */     
/* 28:75 */     return data;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void getString(StringBuffer buf)
/* 32:   */   {
/* 33:86 */     buf.append(new Boolean(this.value).toString());
/* 34:   */   }
/* 35:   */   
/* 36:   */   void handleImportedCellReferences() {}
/* 37:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.BooleanValue
 * JD-Core Version:    0.7.0.1
 */