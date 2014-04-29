/*  1:   */ package org.springframework.web.servlet.tags;
/*  2:   */ 
/*  3:   */ public class Param
/*  4:   */ {
/*  5:   */   private String name;
/*  6:   */   private String value;
/*  7:   */   
/*  8:   */   public String getName()
/*  9:   */   {
/* 10:40 */     return this.name;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public void setName(String name)
/* 14:   */   {
/* 15:47 */     this.name = name;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String getValue()
/* 19:   */   {
/* 20:54 */     return this.value;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void setValue(String value)
/* 24:   */   {
/* 25:61 */     this.value = value;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String toString()
/* 29:   */   {
/* 30:66 */     return "JSP Tag Param: name '" + this.name + "', value '" + this.value + "'";
/* 31:   */   }
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.Param
 * JD-Core Version:    0.7.0.1
 */