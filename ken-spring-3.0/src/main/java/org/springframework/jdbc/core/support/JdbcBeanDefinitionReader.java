/*   1:    */ package org.springframework.jdbc.core.support;
/*   2:    */ 
/*   3:    */ import java.sql.ResultSet;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import java.util.Properties;
/*   6:    */ import javax.sql.DataSource;
/*   7:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*   8:    */ import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
/*   9:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  10:    */ import org.springframework.jdbc.core.RowCallbackHandler;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ 
/*  13:    */ public class JdbcBeanDefinitionReader
/*  14:    */ {
/*  15:    */   private final PropertiesBeanDefinitionReader propReader;
/*  16:    */   private JdbcTemplate jdbcTemplate;
/*  17:    */   
/*  18:    */   public JdbcBeanDefinitionReader(BeanDefinitionRegistry beanFactory)
/*  19:    */   {
/*  20: 63 */     this.propReader = new PropertiesBeanDefinitionReader(beanFactory);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public JdbcBeanDefinitionReader(PropertiesBeanDefinitionReader beanDefinitionReader)
/*  24:    */   {
/*  25: 74 */     Assert.notNull(beanDefinitionReader, "Bean definition reader must not be null");
/*  26: 75 */     this.propReader = beanDefinitionReader;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setDataSource(DataSource dataSource)
/*  30:    */   {
/*  31: 84 */     this.jdbcTemplate = new JdbcTemplate(dataSource);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
/*  35:    */   {
/*  36: 92 */     Assert.notNull(jdbcTemplate, "JdbcTemplate must not be null");
/*  37: 93 */     this.jdbcTemplate = jdbcTemplate;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void loadBeanDefinitions(String sql)
/*  41:    */   {
/*  42:107 */     Assert.notNull(this.jdbcTemplate, "Not fully configured - specify DataSource or JdbcTemplate");
/*  43:108 */     final Properties props = new Properties();
/*  44:109 */     this.jdbcTemplate.query(sql, new RowCallbackHandler()
/*  45:    */     {
/*  46:    */       public void processRow(ResultSet rs)
/*  47:    */         throws SQLException
/*  48:    */       {
/*  49:111 */         String beanName = rs.getString(1);
/*  50:112 */         String property = rs.getString(2);
/*  51:113 */         String value = rs.getString(3);
/*  52:    */         
/*  53:115 */         props.setProperty(beanName + "." + property, value);
/*  54:    */       }
/*  55:117 */     });
/*  56:118 */     this.propReader.registerBeanDefinitions(props);
/*  57:    */   }
/*  58:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.support.JdbcBeanDefinitionReader
 * JD-Core Version:    0.7.0.1
 */