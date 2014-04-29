/*   1:    */ package org.springframework.jdbc.support;
/*   2:    */ 
/*   3:    */ import org.springframework.dao.InvalidDataAccessResourceUsageException;
/*   4:    */ import org.springframework.util.StringUtils;
/*   5:    */ 
/*   6:    */ public class SQLErrorCodes
/*   7:    */ {
/*   8:    */   private String[] databaseProductNames;
/*   9: 39 */   private boolean useSqlStateForTranslation = false;
/*  10: 41 */   private SQLExceptionTranslator customSqlExceptionTranslator = null;
/*  11: 43 */   private String[] badSqlGrammarCodes = new String[0];
/*  12: 45 */   private String[] invalidResultSetAccessCodes = new String[0];
/*  13: 47 */   private String[] duplicateKeyCodes = new String[0];
/*  14: 49 */   private String[] dataIntegrityViolationCodes = new String[0];
/*  15: 51 */   private String[] permissionDeniedCodes = new String[0];
/*  16: 53 */   private String[] dataAccessResourceFailureCodes = new String[0];
/*  17: 55 */   private String[] transientDataAccessResourceCodes = new String[0];
/*  18: 57 */   private String[] cannotAcquireLockCodes = new String[0];
/*  19: 59 */   private String[] deadlockLoserCodes = new String[0];
/*  20: 61 */   private String[] cannotSerializeTransactionCodes = new String[0];
/*  21:    */   private CustomSQLErrorCodesTranslation[] customTranslations;
/*  22:    */   
/*  23:    */   public void setDatabaseProductName(String databaseProductName)
/*  24:    */   {
/*  25: 71 */     this.databaseProductNames = new String[] { databaseProductName };
/*  26:    */   }
/*  27:    */   
/*  28:    */   public String getDatabaseProductName()
/*  29:    */   {
/*  30: 75 */     return (this.databaseProductNames != null) && (this.databaseProductNames.length > 0) ? 
/*  31: 76 */       this.databaseProductNames[0] : null;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setDatabaseProductNames(String[] databaseProductNames)
/*  35:    */   {
/*  36: 84 */     this.databaseProductNames = databaseProductNames;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String[] getDatabaseProductNames()
/*  40:    */   {
/*  41: 88 */     return this.databaseProductNames;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setUseSqlStateForTranslation(boolean useStateCodeForTranslation)
/*  45:    */   {
/*  46: 96 */     this.useSqlStateForTranslation = useStateCodeForTranslation;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean isUseSqlStateForTranslation()
/*  50:    */   {
/*  51:100 */     return this.useSqlStateForTranslation;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public SQLExceptionTranslator getCustomSqlExceptionTranslator()
/*  55:    */   {
/*  56:104 */     return this.customSqlExceptionTranslator;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void setCustomSqlExceptionTranslatorClass(Class customSqlExceptionTranslatorClass)
/*  60:    */   {
/*  61:108 */     if (customSqlExceptionTranslatorClass != null) {
/*  62:    */       try
/*  63:    */       {
/*  64:110 */         this.customSqlExceptionTranslator = 
/*  65:111 */           ((SQLExceptionTranslator)customSqlExceptionTranslatorClass.newInstance());
/*  66:    */       }
/*  67:    */       catch (InstantiationException e)
/*  68:    */       {
/*  69:114 */         throw new InvalidDataAccessResourceUsageException(
/*  70:115 */           "Unable to instantiate " + customSqlExceptionTranslatorClass.getName(), e);
/*  71:    */       }
/*  72:    */       catch (IllegalAccessException e)
/*  73:    */       {
/*  74:118 */         throw new InvalidDataAccessResourceUsageException(
/*  75:119 */           "Unable to instantiate " + customSqlExceptionTranslatorClass.getName(), e);
/*  76:    */       }
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setBadSqlGrammarCodes(String[] badSqlGrammarCodes)
/*  81:    */   {
/*  82:125 */     this.badSqlGrammarCodes = StringUtils.sortStringArray(badSqlGrammarCodes);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public String[] getBadSqlGrammarCodes()
/*  86:    */   {
/*  87:129 */     return this.badSqlGrammarCodes;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setInvalidResultSetAccessCodes(String[] invalidResultSetAccessCodes)
/*  91:    */   {
/*  92:133 */     this.invalidResultSetAccessCodes = StringUtils.sortStringArray(invalidResultSetAccessCodes);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public String[] getInvalidResultSetAccessCodes()
/*  96:    */   {
/*  97:137 */     return this.invalidResultSetAccessCodes;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public String[] getDuplicateKeyCodes()
/* 101:    */   {
/* 102:141 */     return this.duplicateKeyCodes;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setDuplicateKeyCodes(String[] duplicateKeyCodes)
/* 106:    */   {
/* 107:145 */     this.duplicateKeyCodes = duplicateKeyCodes;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setDataIntegrityViolationCodes(String[] dataIntegrityViolationCodes)
/* 111:    */   {
/* 112:149 */     this.dataIntegrityViolationCodes = StringUtils.sortStringArray(dataIntegrityViolationCodes);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public String[] getDataIntegrityViolationCodes()
/* 116:    */   {
/* 117:153 */     return this.dataIntegrityViolationCodes;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void setPermissionDeniedCodes(String[] permissionDeniedCodes)
/* 121:    */   {
/* 122:157 */     this.permissionDeniedCodes = StringUtils.sortStringArray(permissionDeniedCodes);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public String[] getPermissionDeniedCodes()
/* 126:    */   {
/* 127:161 */     return this.permissionDeniedCodes;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void setDataAccessResourceFailureCodes(String[] dataAccessResourceFailureCodes)
/* 131:    */   {
/* 132:165 */     this.dataAccessResourceFailureCodes = StringUtils.sortStringArray(dataAccessResourceFailureCodes);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public String[] getDataAccessResourceFailureCodes()
/* 136:    */   {
/* 137:169 */     return this.dataAccessResourceFailureCodes;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public void setTransientDataAccessResourceCodes(String[] transientDataAccessResourceCodes)
/* 141:    */   {
/* 142:173 */     this.transientDataAccessResourceCodes = StringUtils.sortStringArray(transientDataAccessResourceCodes);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public String[] getTransientDataAccessResourceCodes()
/* 146:    */   {
/* 147:177 */     return this.transientDataAccessResourceCodes;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public void setCannotAcquireLockCodes(String[] cannotAcquireLockCodes)
/* 151:    */   {
/* 152:181 */     this.cannotAcquireLockCodes = StringUtils.sortStringArray(cannotAcquireLockCodes);
/* 153:    */   }
/* 154:    */   
/* 155:    */   public String[] getCannotAcquireLockCodes()
/* 156:    */   {
/* 157:185 */     return this.cannotAcquireLockCodes;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public void setDeadlockLoserCodes(String[] deadlockLoserCodes)
/* 161:    */   {
/* 162:189 */     this.deadlockLoserCodes = StringUtils.sortStringArray(deadlockLoserCodes);
/* 163:    */   }
/* 164:    */   
/* 165:    */   public String[] getDeadlockLoserCodes()
/* 166:    */   {
/* 167:193 */     return this.deadlockLoserCodes;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public void setCannotSerializeTransactionCodes(String[] cannotSerializeTransactionCodes)
/* 171:    */   {
/* 172:197 */     this.cannotSerializeTransactionCodes = StringUtils.sortStringArray(cannotSerializeTransactionCodes);
/* 173:    */   }
/* 174:    */   
/* 175:    */   public String[] getCannotSerializeTransactionCodes()
/* 176:    */   {
/* 177:201 */     return this.cannotSerializeTransactionCodes;
/* 178:    */   }
/* 179:    */   
/* 180:    */   public void setCustomTranslations(CustomSQLErrorCodesTranslation[] customTranslations)
/* 181:    */   {
/* 182:205 */     this.customTranslations = customTranslations;
/* 183:    */   }
/* 184:    */   
/* 185:    */   public CustomSQLErrorCodesTranslation[] getCustomTranslations()
/* 186:    */   {
/* 187:209 */     return this.customTranslations;
/* 188:    */   }
/* 189:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.SQLErrorCodes
 * JD-Core Version:    0.7.0.1
 */