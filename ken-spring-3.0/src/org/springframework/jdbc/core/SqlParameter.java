/*   1:    */ package org.springframework.jdbc.core;
/*   2:    */ 
/*   3:    */ import java.util.LinkedList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.springframework.util.Assert;
/*   6:    */ 
/*   7:    */ public class SqlParameter
/*   8:    */ {
/*   9:    */   private String name;
/*  10:    */   private final int sqlType;
/*  11:    */   private String typeName;
/*  12:    */   private Integer scale;
/*  13:    */   
/*  14:    */   public SqlParameter(int sqlType)
/*  15:    */   {
/*  16: 56 */     this.sqlType = sqlType;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public SqlParameter(int sqlType, String typeName)
/*  20:    */   {
/*  21: 65 */     this.sqlType = sqlType;
/*  22: 66 */     this.typeName = typeName;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public SqlParameter(int sqlType, int scale)
/*  26:    */   {
/*  27: 76 */     this.sqlType = sqlType;
/*  28: 77 */     this.scale = Integer.valueOf(scale);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public SqlParameter(String name, int sqlType)
/*  32:    */   {
/*  33: 86 */     this.name = name;
/*  34: 87 */     this.sqlType = sqlType;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public SqlParameter(String name, int sqlType, String typeName)
/*  38:    */   {
/*  39: 97 */     this.name = name;
/*  40: 98 */     this.sqlType = sqlType;
/*  41: 99 */     this.typeName = typeName;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public SqlParameter(String name, int sqlType, int scale)
/*  45:    */   {
/*  46:110 */     this.name = name;
/*  47:111 */     this.sqlType = sqlType;
/*  48:112 */     this.scale = Integer.valueOf(scale);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public SqlParameter(SqlParameter otherParam)
/*  52:    */   {
/*  53:120 */     Assert.notNull(otherParam, "SqlParameter object must not be null");
/*  54:121 */     this.name = otherParam.name;
/*  55:122 */     this.sqlType = otherParam.sqlType;
/*  56:123 */     this.typeName = otherParam.typeName;
/*  57:124 */     this.scale = otherParam.scale;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public String getName()
/*  61:    */   {
/*  62:132 */     return this.name;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public int getSqlType()
/*  66:    */   {
/*  67:139 */     return this.sqlType;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public String getTypeName()
/*  71:    */   {
/*  72:146 */     return this.typeName;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public Integer getScale()
/*  76:    */   {
/*  77:153 */     return this.scale;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public boolean isInputValueProvided()
/*  81:    */   {
/*  82:163 */     return true;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public boolean isResultsParameter()
/*  86:    */   {
/*  87:172 */     return false;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public static List<SqlParameter> sqlTypesToAnonymousParameterList(int[] types)
/*  91:    */   {
/*  92:181 */     List<SqlParameter> result = new LinkedList();
/*  93:182 */     if (types != null)
/*  94:    */     {
/*  95:183 */       int[] arrayOfInt = types;int j = types.length;
/*  96:183 */       for (int i = 0; i < j; i++)
/*  97:    */       {
/*  98:183 */         int type = arrayOfInt[i];
/*  99:184 */         result.add(new SqlParameter(type));
/* 100:    */       }
/* 101:    */     }
/* 102:187 */     return result;
/* 103:    */   }
/* 104:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.SqlParameter
 * JD-Core Version:    0.7.0.1
 */