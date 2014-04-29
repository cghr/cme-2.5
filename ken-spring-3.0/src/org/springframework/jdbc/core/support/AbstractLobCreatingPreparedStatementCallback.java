/*  1:   */ package org.springframework.jdbc.core.support;
/*  2:   */ 
/*  3:   */ import java.sql.PreparedStatement;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import org.springframework.dao.DataAccessException;
/*  6:   */ import org.springframework.jdbc.core.PreparedStatementCallback;
/*  7:   */ import org.springframework.jdbc.support.lob.LobCreator;
/*  8:   */ import org.springframework.jdbc.support.lob.LobHandler;
/*  9:   */ 
/* 10:   */ public abstract class AbstractLobCreatingPreparedStatementCallback
/* 11:   */   implements PreparedStatementCallback<Integer>
/* 12:   */ {
/* 13:   */   private final LobHandler lobHandler;
/* 14:   */   
/* 15:   */   public AbstractLobCreatingPreparedStatementCallback(LobHandler lobHandler)
/* 16:   */   {
/* 17:65 */     this.lobHandler = lobHandler;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public final Integer doInPreparedStatement(PreparedStatement ps)
/* 21:   */     throws SQLException, DataAccessException
/* 22:   */   {
/* 23:70 */     LobCreator lobCreator = this.lobHandler.getLobCreator();
/* 24:   */     try
/* 25:   */     {
/* 26:72 */       setValues(ps, lobCreator);
/* 27:73 */       return Integer.valueOf(ps.executeUpdate());
/* 28:   */     }
/* 29:   */     finally
/* 30:   */     {
/* 31:76 */       lobCreator.close();
/* 32:   */     }
/* 33:   */   }
/* 34:   */   
/* 35:   */   protected abstract void setValues(PreparedStatement paramPreparedStatement, LobCreator paramLobCreator)
/* 36:   */     throws SQLException, DataAccessException;
/* 37:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback
 * JD-Core Version:    0.7.0.1
 */