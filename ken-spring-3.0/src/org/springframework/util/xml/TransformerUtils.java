/*  1:   */ package org.springframework.util.xml;
/*  2:   */ 
/*  3:   */ import javax.xml.transform.Transformer;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ 
/*  6:   */ public abstract class TransformerUtils
/*  7:   */ {
/*  8:   */   public static final int DEFAULT_INDENT_AMOUNT = 2;
/*  9:   */   
/* 10:   */   public static void enableIndenting(Transformer transformer)
/* 11:   */   {
/* 12:50 */     enableIndenting(transformer, 2);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public static void enableIndenting(Transformer transformer, int indentAmount)
/* 16:   */   {
/* 17:64 */     Assert.notNull(transformer, "Transformer must not be null");
/* 18:65 */     Assert.isTrue(indentAmount > -1, "The indent amount cannot be less than zero : got " + indentAmount);
/* 19:66 */     transformer.setOutputProperty("indent", "yes");
/* 20:   */     try
/* 21:   */     {
/* 22:69 */       transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indentAmount));
/* 23:   */     }
/* 24:   */     catch (IllegalArgumentException localIllegalArgumentException) {}
/* 25:   */   }
/* 26:   */   
/* 27:   */   public static void disableIndenting(Transformer transformer)
/* 28:   */   {
/* 29:82 */     Assert.notNull(transformer, "Transformer must not be null");
/* 30:83 */     transformer.setOutputProperty("indent", "no");
/* 31:   */   }
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.TransformerUtils
 * JD-Core Version:    0.7.0.1
 */