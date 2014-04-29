/*  1:   */ package org.springframework.web.client;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ 
/*  5:   */ public class ResourceAccessException
/*  6:   */   extends RestClientException
/*  7:   */ {
/*  8:   */   public ResourceAccessException(String msg)
/*  9:   */   {
/* 10:34 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public ResourceAccessException(String msg, IOException ex)
/* 14:   */   {
/* 15:43 */     super(msg, ex);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.client.ResourceAccessException
 * JD-Core Version:    0.7.0.1
 */