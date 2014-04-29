/*   1:    */ package org.springframework.jdbc.core.support;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import javax.sql.DataSource;
/*   5:    */ import org.springframework.dao.support.DaoSupport;
/*   6:    */ import org.springframework.jdbc.CannotGetJdbcConnectionException;
/*   7:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*   8:    */ import org.springframework.jdbc.datasource.DataSourceUtils;
/*   9:    */ import org.springframework.jdbc.support.SQLExceptionTranslator;
/*  10:    */ 
/*  11:    */ public abstract class JdbcDaoSupport
/*  12:    */   extends DaoSupport
/*  13:    */ {
/*  14:    */   private JdbcTemplate jdbcTemplate;
/*  15:    */   
/*  16:    */   public final void setDataSource(DataSource dataSource)
/*  17:    */   {
/*  18: 55 */     if ((this.jdbcTemplate == null) || (dataSource != this.jdbcTemplate.getDataSource()))
/*  19:    */     {
/*  20: 56 */       this.jdbcTemplate = createJdbcTemplate(dataSource);
/*  21: 57 */       initTemplateConfig();
/*  22:    */     }
/*  23:    */   }
/*  24:    */   
/*  25:    */   protected JdbcTemplate createJdbcTemplate(DataSource dataSource)
/*  26:    */   {
/*  27: 71 */     return new JdbcTemplate(dataSource);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public final DataSource getDataSource()
/*  31:    */   {
/*  32: 78 */     return this.jdbcTemplate != null ? this.jdbcTemplate.getDataSource() : null;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public final void setJdbcTemplate(JdbcTemplate jdbcTemplate)
/*  36:    */   {
/*  37: 86 */     this.jdbcTemplate = jdbcTemplate;
/*  38: 87 */     initTemplateConfig();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public final JdbcTemplate getJdbcTemplate()
/*  42:    */   {
/*  43: 95 */     return this.jdbcTemplate;
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected void initTemplateConfig() {}
/*  47:    */   
/*  48:    */   protected void checkDaoConfig()
/*  49:    */   {
/*  50:111 */     if (this.jdbcTemplate == null) {
/*  51:112 */       throw new IllegalArgumentException("'dataSource' or 'jdbcTemplate' is required");
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected final SQLExceptionTranslator getExceptionTranslator()
/*  56:    */   {
/*  57:123 */     return getJdbcTemplate().getExceptionTranslator();
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected final Connection getConnection()
/*  61:    */     throws CannotGetJdbcConnectionException
/*  62:    */   {
/*  63:133 */     return DataSourceUtils.getConnection(getDataSource());
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected final void releaseConnection(Connection con)
/*  67:    */   {
/*  68:143 */     DataSourceUtils.releaseConnection(con, getDataSource());
/*  69:    */   }
/*  70:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.support.JdbcDaoSupport
 * JD-Core Version:    0.7.0.1
 */