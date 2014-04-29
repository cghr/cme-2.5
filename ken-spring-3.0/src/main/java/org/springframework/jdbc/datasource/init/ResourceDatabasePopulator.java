/*   1:    */ package org.springframework.jdbc.datasource.init;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.LineNumberReader;
/*   5:    */ import java.sql.Connection;
/*   6:    */ import java.sql.SQLException;
/*   7:    */ import java.sql.Statement;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.Arrays;
/*  10:    */ import java.util.LinkedList;
/*  11:    */ import java.util.List;
/*  12:    */ import org.apache.commons.logging.Log;
/*  13:    */ import org.apache.commons.logging.LogFactory;
/*  14:    */ import org.springframework.core.io.Resource;
/*  15:    */ import org.springframework.core.io.support.EncodedResource;
/*  16:    */ import org.springframework.util.StringUtils;
/*  17:    */ 
/*  18:    */ public class ResourceDatabasePopulator
/*  19:    */   implements DatabasePopulator
/*  20:    */ {
/*  21: 49 */   private static String DEFAULT_COMMENT_PREFIX = "--";
/*  22: 51 */   private static final Log logger = LogFactory.getLog(ResourceDatabasePopulator.class);
/*  23: 54 */   private List<Resource> scripts = new ArrayList();
/*  24:    */   private String sqlScriptEncoding;
/*  25:    */   private String separator;
/*  26: 60 */   private String commentPrefix = DEFAULT_COMMENT_PREFIX;
/*  27: 62 */   private boolean continueOnError = false;
/*  28: 64 */   private boolean ignoreFailedDrops = false;
/*  29:    */   
/*  30:    */   public void addScript(Resource script)
/*  31:    */   {
/*  32: 72 */     this.scripts.add(script);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setScripts(Resource[] scripts)
/*  36:    */   {
/*  37: 80 */     this.scripts = Arrays.asList(scripts);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setSqlScriptEncoding(String sqlScriptEncoding)
/*  41:    */   {
/*  42: 90 */     this.sqlScriptEncoding = sqlScriptEncoding;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setSeparator(String separator)
/*  46:    */   {
/*  47: 97 */     this.separator = separator;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setCommentPrefix(String commentPrefix)
/*  51:    */   {
/*  52:105 */     this.commentPrefix = commentPrefix;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setContinueOnError(boolean continueOnError)
/*  56:    */   {
/*  57:113 */     this.continueOnError = continueOnError;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setIgnoreFailedDrops(boolean ignoreFailedDrops)
/*  61:    */   {
/*  62:123 */     this.ignoreFailedDrops = ignoreFailedDrops;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void populate(Connection connection)
/*  66:    */     throws SQLException
/*  67:    */   {
/*  68:128 */     for (Resource script : this.scripts) {
/*  69:129 */       executeSqlScript(connection, applyEncodingIfNecessary(script), this.continueOnError, this.ignoreFailedDrops);
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   private EncodedResource applyEncodingIfNecessary(Resource script)
/*  74:    */   {
/*  75:134 */     if ((script instanceof EncodedResource)) {
/*  76:135 */       return (EncodedResource)script;
/*  77:    */     }
/*  78:138 */     return new EncodedResource(script, this.sqlScriptEncoding);
/*  79:    */   }
/*  80:    */   
/*  81:    */   private void executeSqlScript(Connection connection, EncodedResource resource, boolean continueOnError, boolean ignoreFailedDrops)
/*  82:    */     throws SQLException
/*  83:    */   {
/*  84:155 */     if (logger.isInfoEnabled()) {
/*  85:156 */       logger.info("Executing SQL script from " + resource);
/*  86:    */     }
/*  87:158 */     long startTime = System.currentTimeMillis();
/*  88:159 */     List<String> statements = new LinkedList();
/*  89:    */     try
/*  90:    */     {
/*  91:162 */       script = readScript(resource);
/*  92:    */     }
/*  93:    */     catch (IOException ex)
/*  94:    */     {
/*  95:    */       String script;
/*  96:165 */       throw new CannotReadScriptException(resource, ex);
/*  97:    */     }
/*  98:    */     String script;
/*  99:167 */     String delimiter = this.separator;
/* 100:168 */     if (delimiter == null)
/* 101:    */     {
/* 102:169 */       delimiter = ";";
/* 103:170 */       if (!containsSqlScriptDelimiters(script, delimiter)) {
/* 104:171 */         delimiter = "\n";
/* 105:    */       }
/* 106:    */     }
/* 107:174 */     splitSqlScript(script, delimiter, statements);
/* 108:175 */     int lineNumber = 0;
/* 109:176 */     Statement stmt = connection.createStatement();
/* 110:    */     try
/* 111:    */     {
/* 112:178 */       for (String statement : statements)
/* 113:    */       {
/* 114:179 */         lineNumber++;
/* 115:    */         try
/* 116:    */         {
/* 117:181 */           int rowsAffected = stmt.executeUpdate(statement);
/* 118:182 */           if (logger.isDebugEnabled()) {
/* 119:183 */             logger.debug(rowsAffected + " rows affected by SQL: " + statement);
/* 120:    */           }
/* 121:    */         }
/* 122:    */         catch (SQLException ex)
/* 123:    */         {
/* 124:187 */           boolean dropStatement = StringUtils.startsWithIgnoreCase(statement.trim(), "drop");
/* 125:188 */           if ((continueOnError) || ((dropStatement) && (ignoreFailedDrops)))
/* 126:    */           {
/* 127:189 */             if (logger.isDebugEnabled()) {
/* 128:190 */               logger.debug("Failed to execute SQL script statement at line " + lineNumber + 
/* 129:191 */                 " of resource " + resource + ": " + statement, ex);
/* 130:    */             }
/* 131:    */           }
/* 132:    */           else {
/* 133:195 */             throw new ScriptStatementFailedException(statement, lineNumber, resource, ex);
/* 134:    */           }
/* 135:    */         }
/* 136:    */       }
/* 137:    */     }
/* 138:    */     finally
/* 139:    */     {
/* 140:    */       try
/* 141:    */       {
/* 142:202 */         stmt.close();
/* 143:    */       }
/* 144:    */       catch (Throwable ex)
/* 145:    */       {
/* 146:205 */         logger.debug("Could not close JDBC Statement", ex);
/* 147:    */       }
/* 148:    */     }
/* 149:208 */     long elapsedTime = System.currentTimeMillis() - startTime;
/* 150:209 */     if (logger.isInfoEnabled()) {
/* 151:210 */       logger.info("Done executing SQL script from " + resource + " in " + elapsedTime + " ms.");
/* 152:    */     }
/* 153:    */   }
/* 154:    */   
/* 155:    */   private String readScript(EncodedResource resource)
/* 156:    */     throws IOException
/* 157:    */   {
/* 158:221 */     LineNumberReader lnr = new LineNumberReader(resource.getReader());
/* 159:222 */     String currentStatement = lnr.readLine();
/* 160:223 */     StringBuilder scriptBuilder = new StringBuilder();
/* 161:224 */     while (currentStatement != null)
/* 162:    */     {
/* 163:225 */       if ((StringUtils.hasText(currentStatement)) && 
/* 164:226 */         (this.commentPrefix != null) && (!currentStatement.startsWith(this.commentPrefix)))
/* 165:    */       {
/* 166:227 */         if (scriptBuilder.length() > 0) {
/* 167:228 */           scriptBuilder.append('\n');
/* 168:    */         }
/* 169:230 */         scriptBuilder.append(currentStatement);
/* 170:    */       }
/* 171:232 */       currentStatement = lnr.readLine();
/* 172:    */     }
/* 173:234 */     maybeAddSeparatorToScript(scriptBuilder);
/* 174:235 */     return scriptBuilder.toString();
/* 175:    */   }
/* 176:    */   
/* 177:    */   private void maybeAddSeparatorToScript(StringBuilder scriptBuilder)
/* 178:    */   {
/* 179:239 */     if (this.separator == null) {
/* 180:240 */       return;
/* 181:    */     }
/* 182:242 */     String trimmed = this.separator.trim();
/* 183:243 */     if (trimmed.length() == this.separator.length()) {
/* 184:244 */       return;
/* 185:    */     }
/* 186:247 */     if (scriptBuilder.lastIndexOf(trimmed) == scriptBuilder.length() - trimmed.length()) {
/* 187:248 */       scriptBuilder.append(this.separator.substring(trimmed.length()));
/* 188:    */     }
/* 189:    */   }
/* 190:    */   
/* 191:    */   private boolean containsSqlScriptDelimiters(String script, String delim)
/* 192:    */   {
/* 193:258 */     boolean inLiteral = false;
/* 194:259 */     char[] content = script.toCharArray();
/* 195:260 */     for (int i = 0; i < script.length(); i++)
/* 196:    */     {
/* 197:261 */       if (content[i] == '\'') {
/* 198:262 */         inLiteral = !inLiteral;
/* 199:    */       }
/* 200:264 */       if ((!inLiteral) && (script.substring(i).startsWith(delim))) {
/* 201:265 */         return true;
/* 202:    */       }
/* 203:    */     }
/* 204:268 */     return false;
/* 205:    */   }
/* 206:    */   
/* 207:    */   private void splitSqlScript(String script, String delim, List<String> statements)
/* 208:    */   {
/* 209:279 */     StringBuilder sb = new StringBuilder();
/* 210:280 */     boolean inLiteral = false;
/* 211:281 */     boolean inEscape = false;
/* 212:282 */     char[] content = script.toCharArray();
/* 213:283 */     for (int i = 0; i < script.length(); i++)
/* 214:    */     {
/* 215:284 */       char c = content[i];
/* 216:285 */       if (inEscape)
/* 217:    */       {
/* 218:286 */         inEscape = false;
/* 219:287 */         sb.append(c);
/* 220:    */       }
/* 221:291 */       else if (c == '\\')
/* 222:    */       {
/* 223:292 */         inEscape = true;
/* 224:293 */         sb.append(c);
/* 225:    */       }
/* 226:    */       else
/* 227:    */       {
/* 228:296 */         if (c == '\'') {
/* 229:297 */           inLiteral = !inLiteral;
/* 230:    */         }
/* 231:299 */         if (!inLiteral)
/* 232:    */         {
/* 233:300 */           if (script.substring(i).startsWith(delim))
/* 234:    */           {
/* 235:301 */             if (sb.length() > 0)
/* 236:    */             {
/* 237:302 */               statements.add(sb.toString());
/* 238:303 */               sb = new StringBuilder();
/* 239:    */             }
/* 240:305 */             i += delim.length() - 1;
/* 241:306 */             continue;
/* 242:    */           }
/* 243:308 */           if ((c == '\n') || (c == '\t')) {
/* 244:309 */             c = ' ';
/* 245:    */           }
/* 246:    */         }
/* 247:312 */         sb.append(c);
/* 248:    */       }
/* 249:    */     }
/* 250:314 */     if (StringUtils.hasText(sb)) {
/* 251:315 */       statements.add(sb.toString());
/* 252:    */     }
/* 253:    */   }
/* 254:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
 * JD-Core Version:    0.7.0.1
 */