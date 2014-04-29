/*   1:    */ package org.springframework.jca.cci.core.support;
/*   2:    */ 
/*   3:    */ import javax.resource.cci.Connection;
/*   4:    */ import javax.resource.cci.ConnectionFactory;
/*   5:    */ import javax.resource.cci.ConnectionSpec;
/*   6:    */ import org.springframework.dao.support.DaoSupport;
/*   7:    */ import org.springframework.jca.cci.CannotGetCciConnectionException;
/*   8:    */ import org.springframework.jca.cci.connection.ConnectionFactoryUtils;
/*   9:    */ import org.springframework.jca.cci.core.CciTemplate;
/*  10:    */ 
/*  11:    */ public abstract class CciDaoSupport
/*  12:    */   extends DaoSupport
/*  13:    */ {
/*  14:    */   private CciTemplate cciTemplate;
/*  15:    */   
/*  16:    */   public final void setConnectionFactory(ConnectionFactory connectionFactory)
/*  17:    */   {
/*  18: 55 */     if ((this.cciTemplate == null) || (connectionFactory != this.cciTemplate.getConnectionFactory())) {
/*  19: 56 */       this.cciTemplate = createCciTemplate(connectionFactory);
/*  20:    */     }
/*  21:    */   }
/*  22:    */   
/*  23:    */   protected CciTemplate createCciTemplate(ConnectionFactory connectionFactory)
/*  24:    */   {
/*  25: 70 */     return new CciTemplate(connectionFactory);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public final ConnectionFactory getConnectionFactory()
/*  29:    */   {
/*  30: 77 */     return this.cciTemplate.getConnectionFactory();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public final void setCciTemplate(CciTemplate cciTemplate)
/*  34:    */   {
/*  35: 85 */     this.cciTemplate = cciTemplate;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public final CciTemplate getCciTemplate()
/*  39:    */   {
/*  40: 93 */     return this.cciTemplate;
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected final void checkDaoConfig()
/*  44:    */   {
/*  45: 98 */     if (this.cciTemplate == null) {
/*  46: 99 */       throw new IllegalArgumentException("'connectionFactory' or 'cciTemplate' is required");
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   protected final CciTemplate getCciTemplate(ConnectionSpec connectionSpec)
/*  51:    */   {
/*  52:114 */     return getCciTemplate().getDerivedTemplate(connectionSpec);
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected final Connection getConnection()
/*  56:    */     throws CannotGetCciConnectionException
/*  57:    */   {
/*  58:125 */     return ConnectionFactoryUtils.getConnection(getConnectionFactory());
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected final void releaseConnection(Connection con)
/*  62:    */   {
/*  63:135 */     ConnectionFactoryUtils.releaseConnection(con, getConnectionFactory());
/*  64:    */   }
/*  65:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.core.support.CciDaoSupport
 * JD-Core Version:    0.7.0.1
 */