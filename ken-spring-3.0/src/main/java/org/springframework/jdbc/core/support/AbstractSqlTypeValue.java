/*  1:   */ package org.springframework.jdbc.core.support;
/*  2:   */ 
/*  3:   */ import java.sql.Connection;
/*  4:   */ import java.sql.PreparedStatement;
/*  5:   */ import java.sql.SQLException;
/*  6:   */ import org.springframework.jdbc.core.SqlTypeValue;
/*  7:   */ 
/*  8:   */ public abstract class AbstractSqlTypeValue
/*  9:   */   implements SqlTypeValue
/* 10:   */ {
/* 11:   */   public final void setTypeValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName)
/* 12:   */     throws SQLException
/* 13:   */   {
/* 14:58 */     Object value = createTypeValue(ps.getConnection(), sqlType, typeName);
/* 15:59 */     if (sqlType == -2147483648) {
/* 16:60 */       ps.setObject(paramIndex, value);
/* 17:   */     } else {
/* 18:63 */       ps.setObject(paramIndex, value, sqlType);
/* 19:   */     }
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected abstract Object createTypeValue(Connection paramConnection, int paramInt, String paramString)
/* 23:   */     throws SQLException;
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.support.AbstractSqlTypeValue
 * JD-Core Version:    0.7.0.1
 */