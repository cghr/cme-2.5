/*  1:   */ package org.springframework.jmx.export.metadata;
/*  2:   */ 
/*  3:   */ public class ManagedOperationParameter
/*  4:   */ {
/*  5:28 */   private int index = 0;
/*  6:30 */   private String name = "";
/*  7:32 */   private String description = "";
/*  8:   */   
/*  9:   */   public void setIndex(int index)
/* 10:   */   {
/* 11:39 */     this.index = index;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public int getIndex()
/* 15:   */   {
/* 16:46 */     return this.index;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void setName(String name)
/* 20:   */   {
/* 21:53 */     this.name = name;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public String getName()
/* 25:   */   {
/* 26:60 */     return this.name;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void setDescription(String description)
/* 30:   */   {
/* 31:67 */     this.description = description;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public String getDescription()
/* 35:   */   {
/* 36:74 */     return this.description;
/* 37:   */   }
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.metadata.ManagedOperationParameter
 * JD-Core Version:    0.7.0.1
 */