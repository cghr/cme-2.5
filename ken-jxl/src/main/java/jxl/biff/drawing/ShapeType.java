/*  1:   */ package jxl.biff.drawing;
/*  2:   */ 
/*  3:   */ final class ShapeType
/*  4:   */ {
/*  5:   */   private int value;
/*  6:35 */   private static ShapeType[] types = new ShapeType[0];
/*  7:   */   
/*  8:   */   ShapeType(int v)
/*  9:   */   {
/* 10:44 */     this.value = v;
/* 11:   */     
/* 12:46 */     ShapeType[] old = types;
/* 13:47 */     types = new ShapeType[types.length + 1];
/* 14:48 */     System.arraycopy(old, 0, types, 0, old.length);
/* 15:49 */     types[old.length] = this;
/* 16:   */   }
/* 17:   */   
/* 18:   */   static ShapeType getType(int v)
/* 19:   */   {
/* 20:60 */     ShapeType st = UNKNOWN;
/* 21:61 */     boolean found = false;
/* 22:62 */     for (int i = 0; (i < types.length) && (!found); i++) {
/* 23:64 */       if (types[i].value == v)
/* 24:   */       {
/* 25:66 */         found = true;
/* 26:67 */         st = types[i];
/* 27:   */       }
/* 28:   */     }
/* 29:70 */     return st;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public int getValue()
/* 33:   */   {
/* 34:80 */     return this.value;
/* 35:   */   }
/* 36:   */   
/* 37:83 */   public static final ShapeType MIN = new ShapeType(0);
/* 38:84 */   public static final ShapeType PICTURE_FRAME = new ShapeType(75);
/* 39:85 */   public static final ShapeType HOST_CONTROL = new ShapeType(201);
/* 40:86 */   public static final ShapeType TEXT_BOX = new ShapeType(202);
/* 41:87 */   public static final ShapeType UNKNOWN = new ShapeType(-1);
/* 42:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.ShapeType
 * JD-Core Version:    0.7.0.1
 */