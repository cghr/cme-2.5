/*   1:    */ package org.springframework.jdbc.core.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.sql.ResultSet;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import org.springframework.dao.DataAccessException;
/*   7:    */ import org.springframework.dao.EmptyResultDataAccessException;
/*   8:    */ import org.springframework.dao.IncorrectResultSizeDataAccessException;
/*   9:    */ import org.springframework.jdbc.LobRetrievalFailureException;
/*  10:    */ import org.springframework.jdbc.core.ResultSetExtractor;
/*  11:    */ 
/*  12:    */ public abstract class AbstractLobStreamingResultSetExtractor
/*  13:    */   implements ResultSetExtractor
/*  14:    */ {
/*  15:    */   public final Object extractData(ResultSet rs)
/*  16:    */     throws SQLException, DataAccessException
/*  17:    */   {
/*  18: 68 */     if (!rs.next()) {
/*  19: 69 */       handleNoRowFound();
/*  20:    */     } else {
/*  21:    */       try
/*  22:    */       {
/*  23: 73 */         streamData(rs);
/*  24: 74 */         if (rs.next()) {
/*  25: 75 */           handleMultipleRowsFound();
/*  26:    */         }
/*  27:    */       }
/*  28:    */       catch (IOException ex)
/*  29:    */       {
/*  30: 79 */         throw new LobRetrievalFailureException("Couldn't stream LOB content", ex);
/*  31:    */       }
/*  32:    */     }
/*  33: 82 */     return null;
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected void handleNoRowFound()
/*  37:    */     throws DataAccessException
/*  38:    */   {
/*  39: 92 */     throw new EmptyResultDataAccessException(
/*  40: 93 */       "LobStreamingResultSetExtractor did not find row in database", 1);
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected void handleMultipleRowsFound()
/*  44:    */     throws DataAccessException
/*  45:    */   {
/*  46:103 */     throw new IncorrectResultSizeDataAccessException(
/*  47:104 */       "LobStreamingResultSetExtractor found multiple rows in database", 1);
/*  48:    */   }
/*  49:    */   
/*  50:    */   protected abstract void streamData(ResultSet paramResultSet)
/*  51:    */     throws SQLException, IOException, DataAccessException;
/*  52:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor
 * JD-Core Version:    0.7.0.1
 */