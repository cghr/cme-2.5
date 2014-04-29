/*  1:   */ package com.kentropy.iterators;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ import net.xoetrope.xui.data.XBaseModel;
/*  5:   */ import net.xoetrope.xui.data.XModel;
/*  6:   */ 
/*  7:   */ public class DummyIterator
/*  8:   */   implements Iterator
/*  9:   */ {
/* 10:10 */   int count = 0;
/* 11:11 */   String currentId = "";
/* 12:   */   
/* 13:   */   public boolean first()
/* 14:   */   {
/* 15:14 */     this.count = 0;
/* 16:15 */     return true;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public XModel getData()
/* 20:   */   {
/* 21:22 */     throw new Error("Unresolved compilation problem: \n\tThe method setId(String) in the type XModel is not applicable for the arguments (int)\n");
/* 22:   */   }
/* 23:   */   
/* 24:   */   public XModel getHeaders()
/* 25:   */   {
/* 26:30 */     XModel xm = new XBaseModel();
/* 27:31 */     xm.setTagName("th");
/* 28:32 */     XModel th = (XModel)xm.get("testHeading");
/* 29:   */     
/* 30:34 */     th.set("testHeading");
/* 31:35 */     th = (XModel)xm.get("testHeading2");
/* 32:   */     
/* 33:37 */     th.set("testHeading2");
/* 34:38 */     return xm;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public boolean last()
/* 38:   */   {
/* 39:43 */     this.count = 100;
/* 40:44 */     return true;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public boolean next()
/* 44:   */   {
/* 45:49 */     this.count += 1;
/* 46:50 */     if (this.count < 100) {
/* 47:51 */       return true;
/* 48:   */     }
/* 49:52 */     return false;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public void setParentContext(XModel parentCtxt) {}
/* 53:   */   
/* 54:   */   public static void main(String[] args)
/* 55:   */   {
/* 56:61 */     DummyIterator dummy = new DummyIterator();
/* 57:62 */     dummy.find("test15");
/* 58:63 */     System.out.println(dummy.getData().get(0).getId());
/* 59:   */   }
/* 60:   */   
/* 61:   */   public boolean find(String key)
/* 62:   */   {
/* 63:68 */     this.count = Integer.parseInt(key.substring("test".length()));
/* 64:   */     
/* 65:70 */     return true;
/* 66:   */   }
/* 67:   */   
/* 68:   */   public void setContext(XModel context) {}
/* 69:   */   
/* 70:   */   public void setSource(XModel source) {}
/* 71:   */   
/* 72:   */   public void init() {}
/* 73:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.iterators.DummyIterator
 * JD-Core Version:    0.7.0.1
 */