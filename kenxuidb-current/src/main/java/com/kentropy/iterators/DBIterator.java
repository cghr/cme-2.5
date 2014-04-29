/*   1:    */ package com.kentropy.iterators;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import net.xoetrope.xui.data.XBaseModel;
/*   6:    */ import net.xoetrope.xui.data.XModel;
/*   7:    */ 
/*   8:    */ public class DBIterator
/*   9:    */   implements Iterator
/*  10:    */ {
/*  11: 11 */   XModel table = null;
/*  12: 12 */   XModel context = null;
/*  13: 13 */   String fields = "*";
/*  14: 14 */   String constraints = "";
/*  15: 15 */   String contextType = "";
/*  16: 16 */   boolean autoUpdate = false;
/*  17: 18 */   int count = 0;
/*  18: 19 */   String currentId = "";
/*  19: 21 */   XModel dataM = null;
/*  20:    */   
/*  21:    */   public void setSource(XModel source)
/*  22:    */   {
/*  23: 25 */     this.table = source;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setContext(XModel context)
/*  27:    */   {
/*  28: 30 */     this.context = context;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void init()
/*  32:    */   {
/*  33: 35 */     String where = "";
/*  34:    */     try
/*  35:    */     {
/*  36: 38 */       if (this.autoUpdate) {
/*  37: 40 */         TestXUIDB.getInstance().applyAutoUpdate(this.context, this.contextType);
/*  38:    */       }
/*  39: 42 */       this.dataM = TestXUIDB.getInstance().getEnumDataChildren(this.context, this.fields, this.constraints, this.contextType);
/*  40:    */     }
/*  41:    */     catch (Exception e)
/*  42:    */     {
/*  43: 45 */       e.printStackTrace();
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   public boolean first()
/*  48:    */   {
/*  49: 51 */     this.count = 0;
/*  50: 52 */     return true;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public XModel getData()
/*  54:    */   {
/*  55: 57 */     XModel xm = new XBaseModel();
/*  56: 58 */     if (this.dataM.getNumChildren() == 0) {
/*  57: 59 */       return null;
/*  58:    */     }
/*  59: 60 */     xm.setTagName("tr");
/*  60: 61 */     xm.setId(this.dataM.get(this.count).getId());
/*  61: 62 */     for (int i = 0; i < this.dataM.get(this.count).getNumChildren(); i++) {
/*  62: 64 */       xm.append(this.dataM.get(this.count).get(i));
/*  63:    */     }
/*  64: 67 */     return xm;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public XModel getHeaders()
/*  68:    */   {
/*  69: 72 */     XModel xm = new XBaseModel();
/*  70: 73 */     if (this.dataM.getNumChildren() == 0) {
/*  71: 74 */       return null;
/*  72:    */     }
/*  73: 75 */     xm.setTagName("th");
/*  74: 76 */     for (int i = 0; i < this.dataM.get(0).getNumChildren(); i++)
/*  75:    */     {
/*  76: 78 */       String id = this.dataM.get(0).get(i).getId();
/*  77: 79 */       XModel th = (XModel)xm.get(id);
/*  78:    */       
/*  79: 81 */       th.set(id);
/*  80:    */     }
/*  81: 84 */     return xm;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public boolean last()
/*  85:    */   {
/*  86: 89 */     this.count = (this.dataM.getNumChildren() - 1);
/*  87: 90 */     return true;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public boolean next()
/*  91:    */   {
/*  92: 95 */     this.count += 1;
/*  93: 96 */     if (this.count < this.dataM.getNumChildren()) {
/*  94: 97 */       return true;
/*  95:    */     }
/*  96: 98 */     return false;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void setParentContext(XModel parentCtxt) {}
/* 100:    */   
/* 101:    */   public static void main(String[] args)
/* 102:    */   {
/* 103:107 */     DBIterator dummy = new DBIterator();
/* 104:108 */     XModel table = new XBaseModel();
/* 105:109 */     table.setId("hc_area");
/* 106:110 */     XModel context = new XBaseModel();
/* 107:111 */     dummy.setSource(table);
/* 108:112 */     dummy.setContext(context);
/* 109:113 */     dummy.init();
/* 110:    */     
/* 111:115 */     System.out.println(dummy.getData().getNumChildren());
/* 112:116 */     System.out.println(dummy.getData().get(2).get());
/* 113:    */   }
/* 114:    */   
/* 115:    */   public boolean find(String key)
/* 116:    */   {
/* 117:121 */     this.count = Integer.parseInt(key.substring("test".length()));
/* 118:    */     
/* 119:123 */     return true;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void setSource() {}
/* 123:    */   
/* 124:    */   public void setConstraints(XModel constraints) {}
/* 125:    */   
/* 126:    */   public void setConstraints(String constraints)
/* 127:    */   {
/* 128:136 */     this.constraints = constraints;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void setNextContextType(String contextType)
/* 132:    */   {
/* 133:141 */     this.contextType = contextType;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void setAutoUpdate(boolean flag)
/* 137:    */   {
/* 138:146 */     this.autoUpdate = flag;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void setFields(String fields)
/* 142:    */   {
/* 143:151 */     this.fields = fields;
/* 144:    */   }
/* 145:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.iterators.DBIterator
 * JD-Core Version:    0.7.0.1
 */