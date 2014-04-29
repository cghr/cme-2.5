/*  1:   */ package com.kentropy.tree;
/*  2:   */ 
/*  3:   */ import java.util.Vector;
/*  4:   */ 
/*  5:   */ public class DTMLTreeVisitor
/*  6:   */   implements TreeVisitor
/*  7:   */ {
/*  8:   */   private String topHeader;
/*  9:   */   
/* 10:   */   public DTMLTreeVisitor(String topHeader)
/* 11:   */   {
/* 12:11 */     this.topHeader = topHeader;
/* 13:   */   }
/* 14:   */   
/* 15:20 */   int idx = 0;
/* 16:21 */   public Vector<StringBuffer> header = new Vector();
/* 17:22 */   String initWidths = "";
/* 18:23 */   String colAlign = "";
/* 19:24 */   String colTypes = "";
/* 20:25 */   String sorting = "";
/* 21:   */   
/* 22:   */   public static void main(String[] args) {}
/* 23:   */   
/* 24:   */   public void getHeader(DBNode dbn) {}
/* 25:   */   
/* 26:   */   public void doTask(DBNode dbn)
/* 27:   */   {
/* 28:34 */     if (this.header.size() <= this.idx) {
/* 29:36 */       this.header.add(new StringBuffer());
/* 30:   */     }
/* 31:38 */     if (dbn.children.size() > 0)
/* 32:   */     {
/* 33:40 */       String h = dbn.key;
/* 34:42 */       if (dbn.key.equals("ALL")) {
/* 35:43 */         h = this.topHeader;
/* 36:   */       }
/* 37:44 */       int count = dbn.getTotalLeaves();
/* 38:45 */       int maxDepth = dbn.getMaxDepth(0);
/* 39:46 */       String cspan = "";
/* 40:47 */       for (int i = 1; i < count + maxDepth - 1; i++) {
/* 41:49 */         cspan = cspan + ",#cspan";
/* 42:   */       }
/* 43:51 */       ((StringBuffer)this.header.get(this.idx)).append((((StringBuffer)this.header.get(this.idx)).length() == 0 ? "" : ",") + h + cspan);
/* 44:   */       int l;
/* 45:53 */       if (this.header.size() <= this.idx + 1)
/* 46:   */       {
/* 47:55 */         this.header.add(new StringBuffer());
/* 48:56 */         String rspan = " ";
/* 49:57 */         for (l = 0; l < this.idx; l++) {
/* 50:59 */           rspan = rspan + (l == 0 ? " ," : " ,");
/* 51:   */         }
/* 52:61 */         ((StringBuffer)this.header.get(this.idx + 1)).append(rspan + "Total");
/* 53:   */       }
/* 54:   */       else
/* 55:   */       {
/* 56:66 */         ((StringBuffer)this.header.get(this.idx + 1)).append(",Total");
/* 57:   */       }
/* 58:70 */       for (DBNode dbn1 : dbn.children)
/* 59:   */       {
/* 60:72 */         this.idx += 1;
/* 61:   */         
/* 62:74 */         doTask(dbn1);
/* 63:75 */         this.idx -= 1;
/* 64:   */       }
/* 65:   */     }
/* 66:   */     else
/* 67:   */     {
/* 68:82 */       ((StringBuffer)this.header.get(this.idx)).append((((StringBuffer)this.header.get(this.idx)).length() == 0 ? "" : ",") + dbn.key);
/* 69:   */     }
/* 70:   */   }
/* 71:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-utilities6\ken-utilities6.jar
 * Qualified Name:     com.kentropy.tree.DTMLTreeVisitor
 * JD-Core Version:    0.7.0.1
 */