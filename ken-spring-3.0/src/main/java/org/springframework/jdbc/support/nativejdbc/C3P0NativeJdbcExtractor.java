/*   1:    */ package org.springframework.jdbc.support.nativejdbc;
/*   2:    */ 
/*   3:    */ import com.mchange.v2.c3p0.C3P0ProxyConnection;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.sql.SQLException;
/*   7:    */ import org.springframework.util.ReflectionUtils;
/*   8:    */ 
/*   9:    */ public class C3P0NativeJdbcExtractor
/*  10:    */   extends NativeJdbcExtractorAdapter
/*  11:    */ {
/*  12:    */   private final Method getRawConnectionMethod;
/*  13:    */   
/*  14:    */   public static Connection getRawConnection(Connection con)
/*  15:    */   {
/*  16: 60 */     return con;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public C3P0NativeJdbcExtractor()
/*  20:    */   {
/*  21:    */     try
/*  22:    */     {
/*  23: 66 */       this.getRawConnectionMethod = getClass().getMethod("getRawConnection", new Class[] { Connection.class });
/*  24:    */     }
/*  25:    */     catch (NoSuchMethodException ex)
/*  26:    */     {
/*  27: 69 */       throw new IllegalStateException("Internal error in C3P0NativeJdbcExtractor: " + ex.getMessage());
/*  28:    */     }
/*  29:    */   }
/*  30:    */   
/*  31:    */   public boolean isNativeConnectionNecessaryForNativeStatements()
/*  32:    */   {
/*  33: 76 */     return true;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public boolean isNativeConnectionNecessaryForNativePreparedStatements()
/*  37:    */   {
/*  38: 81 */     return true;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public boolean isNativeConnectionNecessaryForNativeCallableStatements()
/*  42:    */   {
/*  43: 86 */     return true;
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected Connection doGetNativeConnection(Connection con)
/*  47:    */     throws SQLException
/*  48:    */   {
/*  49: 97 */     if ((con instanceof C3P0ProxyConnection))
/*  50:    */     {
/*  51: 98 */       C3P0ProxyConnection cpCon = (C3P0ProxyConnection)con;
/*  52:    */       try
/*  53:    */       {
/*  54:100 */         return (Connection)cpCon.rawConnectionOperation(
/*  55:101 */           this.getRawConnectionMethod, null, new Object[] { C3P0ProxyConnection.RAW_CONNECTION });
/*  56:    */       }
/*  57:    */       catch (SQLException ex)
/*  58:    */       {
/*  59:104 */         throw ex;
/*  60:    */       }
/*  61:    */       catch (Exception ex)
/*  62:    */       {
/*  63:107 */         ReflectionUtils.handleReflectionException(ex);
/*  64:    */       }
/*  65:    */     }
/*  66:110 */     return con;
/*  67:    */   }
/*  68:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.nativejdbc.C3P0NativeJdbcExtractor
 * JD-Core Version:    0.7.0.1
 */