/*  1:   */ package org.springframework.transaction;
/*  2:   */ 
/*  3:   */ public class HeuristicCompletionException
/*  4:   */   extends TransactionException
/*  5:   */ {
/*  6:   */   public static final int STATE_UNKNOWN = 0;
/*  7:   */   public static final int STATE_COMMITTED = 1;
/*  8:   */   public static final int STATE_ROLLED_BACK = 2;
/*  9:   */   public static final int STATE_MIXED = 3;
/* 10:   */   
/* 11:   */   public static String getStateString(int state)
/* 12:   */   {
/* 13:39 */     switch (state)
/* 14:   */     {
/* 15:   */     case 1: 
/* 16:41 */       return "committed";
/* 17:   */     case 2: 
/* 18:43 */       return "rolled back";
/* 19:   */     case 3: 
/* 20:45 */       return "mixed";
/* 21:   */     }
/* 22:47 */     return "unknown";
/* 23:   */   }
/* 24:   */   
/* 25:55 */   private int outcomeState = 0;
/* 26:   */   
/* 27:   */   public HeuristicCompletionException(int outcomeState, Throwable cause)
/* 28:   */   {
/* 29:64 */     super("Heuristic completion: outcome state is " + getStateString(outcomeState), cause);
/* 30:65 */     this.outcomeState = outcomeState;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public int getOutcomeState()
/* 34:   */   {
/* 35:77 */     return this.outcomeState;
/* 36:   */   }
/* 37:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.HeuristicCompletionException
 * JD-Core Version:    0.7.0.1
 */