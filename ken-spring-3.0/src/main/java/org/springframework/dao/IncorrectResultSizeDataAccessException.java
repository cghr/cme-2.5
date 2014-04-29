/*   1:    */ package org.springframework.dao;
/*   2:    */ 
/*   3:    */ public class IncorrectResultSizeDataAccessException
/*   4:    */   extends DataRetrievalFailureException
/*   5:    */ {
/*   6:    */   private int expectedSize;
/*   7:    */   private int actualSize;
/*   8:    */   
/*   9:    */   public IncorrectResultSizeDataAccessException(int expectedSize)
/*  10:    */   {
/*  11: 41 */     super("Incorrect result size: expected " + expectedSize);
/*  12: 42 */     this.expectedSize = expectedSize;
/*  13: 43 */     this.actualSize = -1;
/*  14:    */   }
/*  15:    */   
/*  16:    */   public IncorrectResultSizeDataAccessException(int expectedSize, int actualSize)
/*  17:    */   {
/*  18: 52 */     super("Incorrect result size: expected " + expectedSize + ", actual " + actualSize);
/*  19: 53 */     this.expectedSize = expectedSize;
/*  20: 54 */     this.actualSize = actualSize;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public IncorrectResultSizeDataAccessException(String msg, int expectedSize)
/*  24:    */   {
/*  25: 63 */     super(msg);
/*  26: 64 */     this.expectedSize = expectedSize;
/*  27: 65 */     this.actualSize = -1;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public IncorrectResultSizeDataAccessException(String msg, int expectedSize, Throwable ex)
/*  31:    */   {
/*  32: 75 */     super(msg, ex);
/*  33: 76 */     this.expectedSize = expectedSize;
/*  34: 77 */     this.actualSize = -1;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public IncorrectResultSizeDataAccessException(String msg, int expectedSize, int actualSize)
/*  38:    */   {
/*  39: 87 */     super(msg);
/*  40: 88 */     this.expectedSize = expectedSize;
/*  41: 89 */     this.actualSize = actualSize;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public int getExpectedSize()
/*  45:    */   {
/*  46: 97 */     return this.expectedSize;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public int getActualSize()
/*  50:    */   {
/*  51:104 */     return this.actualSize;
/*  52:    */   }
/*  53:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.IncorrectResultSizeDataAccessException
 * JD-Core Version:    0.7.0.1
 */