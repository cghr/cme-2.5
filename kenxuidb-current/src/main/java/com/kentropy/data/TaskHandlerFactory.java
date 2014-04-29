/* 1:  */ package com.kentropy.data;
/* 2:  */ 
/* 3:  */ import com.kentropy.db.TestXUIDB;
/* 4:  */ 
/* 5:  */ public class TaskHandlerFactory
/* 6:  */ {
/* 7:  */   public static TaskHandler getDefaultTaskHandler()
/* 8:  */   {
/* 9:9 */     return TestXUIDB.getInstance();
/* ::  */   }
/* ;:  */   
/* <:  */   public static void main(String[] args) {}
/* =:  */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.data.TaskHandlerFactory
 * JD-Core Version:    0.7.0.1
 */