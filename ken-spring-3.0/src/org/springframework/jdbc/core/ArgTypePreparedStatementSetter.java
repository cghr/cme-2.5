/*   1:    */ package org.springframework.jdbc.core;
/*   2:    */ 
/*   3:    */ import java.sql.PreparedStatement;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*   8:    */ 
/*   9:    */ class ArgTypePreparedStatementSetter
/*  10:    */   implements PreparedStatementSetter, ParameterDisposer
/*  11:    */ {
/*  12:    */   private final Object[] args;
/*  13:    */   private final int[] argTypes;
/*  14:    */   
/*  15:    */   public ArgTypePreparedStatementSetter(Object[] args, int[] argTypes)
/*  16:    */   {
/*  17: 46 */     if (((args != null) && (argTypes == null)) || ((args == null) && (argTypes != null)) || (
/*  18: 47 */       (args != null) && (args.length != argTypes.length))) {
/*  19: 48 */       throw new InvalidDataAccessApiUsageException("args and argTypes parameters must match");
/*  20:    */     }
/*  21: 50 */     this.args = args;
/*  22: 51 */     this.argTypes = argTypes;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setValues(PreparedStatement ps)
/*  26:    */     throws SQLException
/*  27:    */   {
/*  28: 56 */     int parameterPosition = 1;
/*  29: 57 */     if (this.args != null) {
/*  30: 58 */       for (int i = 0; i < this.args.length; i++)
/*  31:    */       {
/*  32: 59 */         Object arg = this.args[i];
/*  33: 60 */         if (((arg instanceof Collection)) && (this.argTypes[i] != 2003))
/*  34:    */         {
/*  35: 61 */           Collection entries = (Collection)arg;
/*  36: 62 */           for (Iterator it = entries.iterator(); it.hasNext();)
/*  37:    */           {
/*  38: 63 */             Object entry = it.next();
/*  39: 64 */             if ((entry instanceof Object[]))
/*  40:    */             {
/*  41: 65 */               Object[] valueArray = (Object[])entry;
/*  42: 66 */               for (int k = 0; k < valueArray.length; k++)
/*  43:    */               {
/*  44: 67 */                 Object argValue = valueArray[k];
/*  45: 68 */                 doSetValue(ps, parameterPosition, this.argTypes[i], argValue);
/*  46: 69 */                 parameterPosition++;
/*  47:    */               }
/*  48:    */             }
/*  49:    */             else
/*  50:    */             {
/*  51: 73 */               doSetValue(ps, parameterPosition, this.argTypes[i], entry);
/*  52: 74 */               parameterPosition++;
/*  53:    */             }
/*  54:    */           }
/*  55:    */         }
/*  56:    */         else
/*  57:    */         {
/*  58: 79 */           doSetValue(ps, parameterPosition, this.argTypes[i], arg);
/*  59: 80 */           parameterPosition++;
/*  60:    */         }
/*  61:    */       }
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected void doSetValue(PreparedStatement ps, int parameterPosition, int argType, Object argValue)
/*  66:    */     throws SQLException
/*  67:    */   {
/*  68: 97 */     StatementCreatorUtils.setParameterValue(ps, parameterPosition, argType, argValue);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void cleanupParameters()
/*  72:    */   {
/*  73:101 */     StatementCreatorUtils.cleanupParameters(this.args);
/*  74:    */   }
/*  75:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.ArgTypePreparedStatementSetter
 * JD-Core Version:    0.7.0.1
 */