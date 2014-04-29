/*  1:   */ package com.kentropy.services;
/*  2:   */ 
/*  3:   */ public class CMEService
/*  4:   */ {
/*  5:   */   public String getTasks(String phy, String status)
/*  6:   */   {
/*  7:12 */     return 
/*  8:13 */       "task=1,stage=Coding, age=10Y ,unino=123456,datessigned=,duedate=,status=" + status + "\r\ntask=2,stage=Coding, age=90Y ,unino=78823456,datessigned=,duedate=,status=" + status;
/*  9:   */   }
/* 10:   */   
/* 11:   */   public String getTaskData(String phy, String taskType, String status)
/* 12:   */   {
/* 13:19 */     return 
/* 14:   */     
/* 15:   */ 
/* 16:   */ 
/* 17:   */ 
/* 18:   */ 
/* 19:25 */       "<data id='va'><data id='gi<data id='info1' value='test'>=test,stage=Coding, age=10Y ,unino=123456,datessigned=,duedate=,status=" + status + "\r\ntask=2,stage=Coding, age=90Y ,uniqno=78823456,datessigned=,duedate=,status=" + status;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public static void main(String[] args) {}
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.CMEService
 * JD-Core Version:    0.7.0.1
 */