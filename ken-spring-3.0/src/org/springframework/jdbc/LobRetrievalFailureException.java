/*  1:   */ package org.springframework.jdbc;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import org.springframework.dao.DataRetrievalFailureException;
/*  5:   */ 
/*  6:   */ public class LobRetrievalFailureException
/*  7:   */   extends DataRetrievalFailureException
/*  8:   */ {
/*  9:   */   public LobRetrievalFailureException(String msg)
/* 10:   */   {
/* 11:36 */     super(msg);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public LobRetrievalFailureException(String msg, IOException ex)
/* 15:   */   {
/* 16:45 */     super(msg, ex);
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.LobRetrievalFailureException
 * JD-Core Version:    0.7.0.1
 */