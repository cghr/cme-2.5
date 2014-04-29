/*  1:   */ package org.springframework.jdbc.object;
/*  2:   */ 
/*  3:   */ import java.sql.ResultSet;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import java.util.Map;
/*  6:   */ import javax.sql.DataSource;
/*  7:   */ 
/*  8:   */ public abstract class MappingSqlQuery<T>
/*  9:   */   extends MappingSqlQueryWithParameters<T>
/* 10:   */ {
/* 11:   */   public MappingSqlQuery() {}
/* 12:   */   
/* 13:   */   public MappingSqlQuery(DataSource ds, String sql)
/* 14:   */   {
/* 15:52 */     super(ds, sql);
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected final T mapRow(ResultSet rs, int rowNum, Object[] parameters, Map context)
/* 19:   */     throws SQLException
/* 20:   */   {
/* 21:65 */     return mapRow(rs, rowNum);
/* 22:   */   }
/* 23:   */   
/* 24:   */   protected abstract T mapRow(ResultSet paramResultSet, int paramInt)
/* 25:   */     throws SQLException;
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.object.MappingSqlQuery
 * JD-Core Version:    0.7.0.1
 */