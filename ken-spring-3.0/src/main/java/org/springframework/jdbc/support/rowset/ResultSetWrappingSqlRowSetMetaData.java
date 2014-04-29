/*   1:    */ package org.springframework.jdbc.support.rowset;
/*   2:    */ 
/*   3:    */ import java.sql.ResultSetMetaData;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import org.springframework.jdbc.InvalidResultSetAccessException;
/*   6:    */ 
/*   7:    */ public class ResultSetWrappingSqlRowSetMetaData
/*   8:    */   implements SqlRowSetMetaData
/*   9:    */ {
/*  10:    */   private final ResultSetMetaData resultSetMetaData;
/*  11:    */   private String[] columnNames;
/*  12:    */   
/*  13:    */   public ResultSetWrappingSqlRowSetMetaData(ResultSetMetaData resultSetMetaData)
/*  14:    */   {
/*  15: 54 */     this.resultSetMetaData = resultSetMetaData;
/*  16:    */   }
/*  17:    */   
/*  18:    */   public String getCatalogName(int column)
/*  19:    */     throws InvalidResultSetAccessException
/*  20:    */   {
/*  21:    */     try
/*  22:    */     {
/*  23: 60 */       return this.resultSetMetaData.getCatalogName(column);
/*  24:    */     }
/*  25:    */     catch (SQLException se)
/*  26:    */     {
/*  27: 63 */       throw new InvalidResultSetAccessException(se);
/*  28:    */     }
/*  29:    */   }
/*  30:    */   
/*  31:    */   public String getColumnClassName(int column)
/*  32:    */     throws InvalidResultSetAccessException
/*  33:    */   {
/*  34:    */     try
/*  35:    */     {
/*  36: 69 */       return this.resultSetMetaData.getColumnClassName(column);
/*  37:    */     }
/*  38:    */     catch (SQLException se)
/*  39:    */     {
/*  40: 72 */       throw new InvalidResultSetAccessException(se);
/*  41:    */     }
/*  42:    */   }
/*  43:    */   
/*  44:    */   public int getColumnCount()
/*  45:    */     throws InvalidResultSetAccessException
/*  46:    */   {
/*  47:    */     try
/*  48:    */     {
/*  49: 78 */       return this.resultSetMetaData.getColumnCount();
/*  50:    */     }
/*  51:    */     catch (SQLException se)
/*  52:    */     {
/*  53: 81 */       throw new InvalidResultSetAccessException(se);
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public String[] getColumnNames()
/*  58:    */     throws InvalidResultSetAccessException
/*  59:    */   {
/*  60: 86 */     if (this.columnNames == null)
/*  61:    */     {
/*  62: 87 */       this.columnNames = new String[getColumnCount()];
/*  63: 88 */       for (int i = 0; i < getColumnCount(); i++) {
/*  64: 89 */         this.columnNames[i] = getColumnName(i + 1);
/*  65:    */       }
/*  66:    */     }
/*  67: 92 */     return this.columnNames;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public int getColumnDisplaySize(int column)
/*  71:    */     throws InvalidResultSetAccessException
/*  72:    */   {
/*  73:    */     try
/*  74:    */     {
/*  75: 97 */       return this.resultSetMetaData.getColumnDisplaySize(column);
/*  76:    */     }
/*  77:    */     catch (SQLException se)
/*  78:    */     {
/*  79:100 */       throw new InvalidResultSetAccessException(se);
/*  80:    */     }
/*  81:    */   }
/*  82:    */   
/*  83:    */   public String getColumnLabel(int column)
/*  84:    */     throws InvalidResultSetAccessException
/*  85:    */   {
/*  86:    */     try
/*  87:    */     {
/*  88:106 */       return this.resultSetMetaData.getColumnLabel(column);
/*  89:    */     }
/*  90:    */     catch (SQLException se)
/*  91:    */     {
/*  92:109 */       throw new InvalidResultSetAccessException(se);
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   public String getColumnName(int column)
/*  97:    */     throws InvalidResultSetAccessException
/*  98:    */   {
/*  99:    */     try
/* 100:    */     {
/* 101:115 */       return this.resultSetMetaData.getColumnName(column);
/* 102:    */     }
/* 103:    */     catch (SQLException se)
/* 104:    */     {
/* 105:118 */       throw new InvalidResultSetAccessException(se);
/* 106:    */     }
/* 107:    */   }
/* 108:    */   
/* 109:    */   public int getColumnType(int column)
/* 110:    */     throws InvalidResultSetAccessException
/* 111:    */   {
/* 112:    */     try
/* 113:    */     {
/* 114:124 */       return this.resultSetMetaData.getColumnType(column);
/* 115:    */     }
/* 116:    */     catch (SQLException se)
/* 117:    */     {
/* 118:127 */       throw new InvalidResultSetAccessException(se);
/* 119:    */     }
/* 120:    */   }
/* 121:    */   
/* 122:    */   public String getColumnTypeName(int column)
/* 123:    */     throws InvalidResultSetAccessException
/* 124:    */   {
/* 125:    */     try
/* 126:    */     {
/* 127:133 */       return this.resultSetMetaData.getColumnTypeName(column);
/* 128:    */     }
/* 129:    */     catch (SQLException se)
/* 130:    */     {
/* 131:136 */       throw new InvalidResultSetAccessException(se);
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   public int getPrecision(int column)
/* 136:    */     throws InvalidResultSetAccessException
/* 137:    */   {
/* 138:    */     try
/* 139:    */     {
/* 140:142 */       return this.resultSetMetaData.getPrecision(column);
/* 141:    */     }
/* 142:    */     catch (SQLException se)
/* 143:    */     {
/* 144:145 */       throw new InvalidResultSetAccessException(se);
/* 145:    */     }
/* 146:    */   }
/* 147:    */   
/* 148:    */   public int getScale(int column)
/* 149:    */     throws InvalidResultSetAccessException
/* 150:    */   {
/* 151:    */     try
/* 152:    */     {
/* 153:151 */       return this.resultSetMetaData.getScale(column);
/* 154:    */     }
/* 155:    */     catch (SQLException se)
/* 156:    */     {
/* 157:154 */       throw new InvalidResultSetAccessException(se);
/* 158:    */     }
/* 159:    */   }
/* 160:    */   
/* 161:    */   public String getSchemaName(int column)
/* 162:    */     throws InvalidResultSetAccessException
/* 163:    */   {
/* 164:    */     try
/* 165:    */     {
/* 166:160 */       return this.resultSetMetaData.getSchemaName(column);
/* 167:    */     }
/* 168:    */     catch (SQLException se)
/* 169:    */     {
/* 170:163 */       throw new InvalidResultSetAccessException(se);
/* 171:    */     }
/* 172:    */   }
/* 173:    */   
/* 174:    */   public String getTableName(int column)
/* 175:    */     throws InvalidResultSetAccessException
/* 176:    */   {
/* 177:    */     try
/* 178:    */     {
/* 179:169 */       return this.resultSetMetaData.getTableName(column);
/* 180:    */     }
/* 181:    */     catch (SQLException se)
/* 182:    */     {
/* 183:172 */       throw new InvalidResultSetAccessException(se);
/* 184:    */     }
/* 185:    */   }
/* 186:    */   
/* 187:    */   public boolean isCaseSensitive(int column)
/* 188:    */     throws InvalidResultSetAccessException
/* 189:    */   {
/* 190:    */     try
/* 191:    */     {
/* 192:178 */       return this.resultSetMetaData.isCaseSensitive(column);
/* 193:    */     }
/* 194:    */     catch (SQLException se)
/* 195:    */     {
/* 196:181 */       throw new InvalidResultSetAccessException(se);
/* 197:    */     }
/* 198:    */   }
/* 199:    */   
/* 200:    */   public boolean isCurrency(int column)
/* 201:    */     throws InvalidResultSetAccessException
/* 202:    */   {
/* 203:    */     try
/* 204:    */     {
/* 205:187 */       return this.resultSetMetaData.isCurrency(column);
/* 206:    */     }
/* 207:    */     catch (SQLException se)
/* 208:    */     {
/* 209:190 */       throw new InvalidResultSetAccessException(se);
/* 210:    */     }
/* 211:    */   }
/* 212:    */   
/* 213:    */   public boolean isSigned(int column)
/* 214:    */     throws InvalidResultSetAccessException
/* 215:    */   {
/* 216:    */     try
/* 217:    */     {
/* 218:196 */       return this.resultSetMetaData.isSigned(column);
/* 219:    */     }
/* 220:    */     catch (SQLException se)
/* 221:    */     {
/* 222:199 */       throw new InvalidResultSetAccessException(se);
/* 223:    */     }
/* 224:    */   }
/* 225:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSetMetaData
 * JD-Core Version:    0.7.0.1
 */