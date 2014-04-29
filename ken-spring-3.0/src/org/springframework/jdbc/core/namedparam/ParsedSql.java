/*   1:    */ package org.springframework.jdbc.core.namedparam;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ 
/*   6:    */ public class ParsedSql
/*   7:    */ {
/*   8:    */   private String originalSql;
/*   9: 33 */   private List<String> parameterNames = new ArrayList();
/*  10: 35 */   private List<int[]> parameterIndexes = new ArrayList();
/*  11:    */   private int namedParameterCount;
/*  12:    */   private int unnamedParameterCount;
/*  13:    */   private int totalParameterCount;
/*  14:    */   
/*  15:    */   ParsedSql(String originalSql)
/*  16:    */   {
/*  17: 49 */     this.originalSql = originalSql;
/*  18:    */   }
/*  19:    */   
/*  20:    */   String getOriginalSql()
/*  21:    */   {
/*  22: 56 */     return this.originalSql;
/*  23:    */   }
/*  24:    */   
/*  25:    */   void addNamedParameter(String parameterName, int startIndex, int endIndex)
/*  26:    */   {
/*  27: 67 */     this.parameterNames.add(parameterName);
/*  28: 68 */     this.parameterIndexes.add(new int[] { startIndex, endIndex });
/*  29:    */   }
/*  30:    */   
/*  31:    */   List<String> getParameterNames()
/*  32:    */   {
/*  33: 76 */     return this.parameterNames;
/*  34:    */   }
/*  35:    */   
/*  36:    */   int[] getParameterIndexes(int parameterPosition)
/*  37:    */   {
/*  38: 87 */     return (int[])this.parameterIndexes.get(parameterPosition);
/*  39:    */   }
/*  40:    */   
/*  41:    */   void setNamedParameterCount(int namedParameterCount)
/*  42:    */   {
/*  43: 95 */     this.namedParameterCount = namedParameterCount;
/*  44:    */   }
/*  45:    */   
/*  46:    */   int getNamedParameterCount()
/*  47:    */   {
/*  48:103 */     return this.namedParameterCount;
/*  49:    */   }
/*  50:    */   
/*  51:    */   void setUnnamedParameterCount(int unnamedParameterCount)
/*  52:    */   {
/*  53:110 */     this.unnamedParameterCount = unnamedParameterCount;
/*  54:    */   }
/*  55:    */   
/*  56:    */   int getUnnamedParameterCount()
/*  57:    */   {
/*  58:117 */     return this.unnamedParameterCount;
/*  59:    */   }
/*  60:    */   
/*  61:    */   void setTotalParameterCount(int totalParameterCount)
/*  62:    */   {
/*  63:125 */     this.totalParameterCount = totalParameterCount;
/*  64:    */   }
/*  65:    */   
/*  66:    */   int getTotalParameterCount()
/*  67:    */   {
/*  68:133 */     return this.totalParameterCount;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public String toString()
/*  72:    */   {
/*  73:142 */     return this.originalSql;
/*  74:    */   }
/*  75:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.namedparam.ParsedSql
 * JD-Core Version:    0.7.0.1
 */