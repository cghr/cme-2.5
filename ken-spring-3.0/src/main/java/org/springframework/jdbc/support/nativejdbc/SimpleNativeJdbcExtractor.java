/*   1:    */ package org.springframework.jdbc.support.nativejdbc;
/*   2:    */ 
/*   3:    */ public class SimpleNativeJdbcExtractor
/*   4:    */   extends NativeJdbcExtractorAdapter
/*   5:    */ {
/*   6: 74 */   private boolean nativeConnectionNecessaryForNativeStatements = false;
/*   7: 76 */   private boolean nativeConnectionNecessaryForNativePreparedStatements = false;
/*   8: 78 */   private boolean nativeConnectionNecessaryForNativeCallableStatements = false;
/*   9:    */   
/*  10:    */   public void setNativeConnectionNecessaryForNativeStatements(boolean nativeConnectionNecessaryForNativeStatements)
/*  11:    */   {
/*  12: 94 */     this.nativeConnectionNecessaryForNativeStatements = nativeConnectionNecessaryForNativeStatements;
/*  13:    */   }
/*  14:    */   
/*  15:    */   public boolean isNativeConnectionNecessaryForNativeStatements()
/*  16:    */   {
/*  17: 99 */     return this.nativeConnectionNecessaryForNativeStatements;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public void setNativeConnectionNecessaryForNativePreparedStatements(boolean nativeConnectionNecessary)
/*  21:    */   {
/*  22:115 */     this.nativeConnectionNecessaryForNativePreparedStatements = nativeConnectionNecessary;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public boolean isNativeConnectionNecessaryForNativePreparedStatements()
/*  26:    */   {
/*  27:120 */     return this.nativeConnectionNecessaryForNativePreparedStatements;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setNativeConnectionNecessaryForNativeCallableStatements(boolean nativeConnectionNecessary)
/*  31:    */   {
/*  32:136 */     this.nativeConnectionNecessaryForNativeCallableStatements = nativeConnectionNecessary;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public boolean isNativeConnectionNecessaryForNativeCallableStatements()
/*  36:    */   {
/*  37:141 */     return this.nativeConnectionNecessaryForNativeCallableStatements;
/*  38:    */   }
/*  39:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.nativejdbc.SimpleNativeJdbcExtractor
 * JD-Core Version:    0.7.0.1
 */