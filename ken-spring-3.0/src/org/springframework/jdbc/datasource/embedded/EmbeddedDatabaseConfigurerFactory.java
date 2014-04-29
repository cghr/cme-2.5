/*  1:   */ package org.springframework.jdbc.datasource.embedded;
/*  2:   */ 
/*  3:   */ import org.springframework.util.Assert;
/*  4:   */ 
/*  5:   */ final class EmbeddedDatabaseConfigurerFactory
/*  6:   */ {
/*  7:   */   public static EmbeddedDatabaseConfigurer getConfigurer(EmbeddedDatabaseType type)
/*  8:   */     throws IllegalStateException
/*  9:   */   {
/* 10:32 */     Assert.notNull(type, "EmbeddedDatabaseType is required");
/* 11:   */     try
/* 12:   */     {
/* 13:34 */       switch (type)
/* 14:   */       {
/* 15:   */       case DERBY: 
/* 16:36 */         return HsqlEmbeddedDatabaseConfigurer.getInstance();
/* 17:   */       case H2: 
/* 18:38 */         return H2EmbeddedDatabaseConfigurer.getInstance();
/* 19:   */       case HSQL: 
/* 20:40 */         return DerbyEmbeddedDatabaseConfigurer.getInstance();
/* 21:   */       }
/* 22:42 */       throw new UnsupportedOperationException("Other embedded database types not yet supported");
/* 23:   */     }
/* 24:   */     catch (ClassNotFoundException ex)
/* 25:   */     {
/* 26:46 */       throw new IllegalStateException("Driver for test database type [" + type + 
/* 27:47 */         "] is not available in the classpath", ex);
/* 28:   */     }
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurerFactory
 * JD-Core Version:    0.7.0.1
 */