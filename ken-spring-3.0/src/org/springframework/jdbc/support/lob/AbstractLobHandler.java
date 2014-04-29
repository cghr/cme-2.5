/*  1:   */ package org.springframework.jdbc.support.lob;
/*  2:   */ 
/*  3:   */ import java.io.InputStream;
/*  4:   */ import java.io.Reader;
/*  5:   */ import java.sql.ResultSet;
/*  6:   */ import java.sql.SQLException;
/*  7:   */ 
/*  8:   */ public abstract class AbstractLobHandler
/*  9:   */   implements LobHandler
/* 10:   */ {
/* 11:   */   public byte[] getBlobAsBytes(ResultSet rs, String columnName)
/* 12:   */     throws SQLException
/* 13:   */   {
/* 14:37 */     return getBlobAsBytes(rs, rs.findColumn(columnName));
/* 15:   */   }
/* 16:   */   
/* 17:   */   public InputStream getBlobAsBinaryStream(ResultSet rs, String columnName)
/* 18:   */     throws SQLException
/* 19:   */   {
/* 20:41 */     return getBlobAsBinaryStream(rs, rs.findColumn(columnName));
/* 21:   */   }
/* 22:   */   
/* 23:   */   public String getClobAsString(ResultSet rs, String columnName)
/* 24:   */     throws SQLException
/* 25:   */   {
/* 26:45 */     return getClobAsString(rs, rs.findColumn(columnName));
/* 27:   */   }
/* 28:   */   
/* 29:   */   public InputStream getClobAsAsciiStream(ResultSet rs, String columnName)
/* 30:   */     throws SQLException
/* 31:   */   {
/* 32:49 */     return getClobAsAsciiStream(rs, rs.findColumn(columnName));
/* 33:   */   }
/* 34:   */   
/* 35:   */   public Reader getClobAsCharacterStream(ResultSet rs, String columnName)
/* 36:   */     throws SQLException
/* 37:   */   {
/* 38:53 */     return getClobAsCharacterStream(rs, rs.findColumn(columnName));
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.lob.AbstractLobHandler
 * JD-Core Version:    0.7.0.1
 */