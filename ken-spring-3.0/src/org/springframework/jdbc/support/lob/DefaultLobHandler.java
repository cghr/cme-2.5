/*   1:    */ package org.springframework.jdbc.support.lob;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.InputStreamReader;
/*   6:    */ import java.io.Reader;
/*   7:    */ import java.io.StringReader;
/*   8:    */ import java.io.UnsupportedEncodingException;
/*   9:    */ import java.sql.Blob;
/*  10:    */ import java.sql.Clob;
/*  11:    */ import java.sql.PreparedStatement;
/*  12:    */ import java.sql.ResultSet;
/*  13:    */ import java.sql.SQLException;
/*  14:    */ import org.apache.commons.logging.Log;
/*  15:    */ import org.apache.commons.logging.LogFactory;
/*  16:    */ 
/*  17:    */ public class DefaultLobHandler
/*  18:    */   extends AbstractLobHandler
/*  19:    */ {
/*  20: 72 */   protected final Log logger = LogFactory.getLog(getClass());
/*  21: 74 */   private boolean wrapAsLob = false;
/*  22: 76 */   private boolean streamAsLob = false;
/*  23:    */   
/*  24:    */   public void setWrapAsLob(boolean wrapAsLob)
/*  25:    */   {
/*  26: 94 */     this.wrapAsLob = wrapAsLob;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setStreamAsLob(boolean streamAsLob)
/*  30:    */   {
/*  31:112 */     this.streamAsLob = streamAsLob;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public byte[] getBlobAsBytes(ResultSet rs, int columnIndex)
/*  35:    */     throws SQLException
/*  36:    */   {
/*  37:117 */     this.logger.debug("Returning BLOB as bytes");
/*  38:118 */     if (this.wrapAsLob)
/*  39:    */     {
/*  40:119 */       Blob blob = rs.getBlob(columnIndex);
/*  41:120 */       return blob.getBytes(1L, (int)blob.length());
/*  42:    */     }
/*  43:123 */     return rs.getBytes(columnIndex);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public InputStream getBlobAsBinaryStream(ResultSet rs, int columnIndex)
/*  47:    */     throws SQLException
/*  48:    */   {
/*  49:128 */     this.logger.debug("Returning BLOB as binary stream");
/*  50:129 */     if (this.wrapAsLob)
/*  51:    */     {
/*  52:130 */       Blob blob = rs.getBlob(columnIndex);
/*  53:131 */       return blob.getBinaryStream();
/*  54:    */     }
/*  55:134 */     return rs.getBinaryStream(columnIndex);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public String getClobAsString(ResultSet rs, int columnIndex)
/*  59:    */     throws SQLException
/*  60:    */   {
/*  61:139 */     this.logger.debug("Returning CLOB as string");
/*  62:140 */     if (this.wrapAsLob)
/*  63:    */     {
/*  64:141 */       Clob clob = rs.getClob(columnIndex);
/*  65:142 */       return clob.getSubString(1L, (int)clob.length());
/*  66:    */     }
/*  67:145 */     return rs.getString(columnIndex);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public InputStream getClobAsAsciiStream(ResultSet rs, int columnIndex)
/*  71:    */     throws SQLException
/*  72:    */   {
/*  73:150 */     this.logger.debug("Returning CLOB as ASCII stream");
/*  74:151 */     if (this.wrapAsLob)
/*  75:    */     {
/*  76:152 */       Clob clob = rs.getClob(columnIndex);
/*  77:153 */       return clob.getAsciiStream();
/*  78:    */     }
/*  79:156 */     return rs.getAsciiStream(columnIndex);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public Reader getClobAsCharacterStream(ResultSet rs, int columnIndex)
/*  83:    */     throws SQLException
/*  84:    */   {
/*  85:161 */     this.logger.debug("Returning CLOB as character stream");
/*  86:162 */     if (this.wrapAsLob)
/*  87:    */     {
/*  88:163 */       Clob clob = rs.getClob(columnIndex);
/*  89:164 */       return clob.getCharacterStream();
/*  90:    */     }
/*  91:167 */     return rs.getCharacterStream(columnIndex);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public LobCreator getLobCreator()
/*  95:    */   {
/*  96:172 */     return new DefaultLobCreator();
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected class DefaultLobCreator
/* 100:    */     implements LobCreator
/* 101:    */   {
/* 102:    */     protected DefaultLobCreator() {}
/* 103:    */     
/* 104:    */     public void setBlobAsBytes(PreparedStatement ps, int paramIndex, byte[] content)
/* 105:    */       throws SQLException
/* 106:    */     {
/* 107:185 */       if (DefaultLobHandler.this.streamAsLob)
/* 108:    */       {
/* 109:186 */         if (content != null) {
/* 110:187 */           ps.setBlob(paramIndex, new ByteArrayInputStream(content), content.length);
/* 111:    */         } else {
/* 112:190 */           ps.setBlob(paramIndex, null);
/* 113:    */         }
/* 114:    */       }
/* 115:193 */       else if (DefaultLobHandler.this.wrapAsLob)
/* 116:    */       {
/* 117:194 */         if (content != null) {
/* 118:195 */           ps.setBlob(paramIndex, new PassThroughBlob(content));
/* 119:    */         } else {
/* 120:198 */           ps.setBlob(paramIndex, null);
/* 121:    */         }
/* 122:    */       }
/* 123:    */       else {
/* 124:202 */         ps.setBytes(paramIndex, content);
/* 125:    */       }
/* 126:204 */       if (DefaultLobHandler.this.logger.isDebugEnabled()) {
/* 127:205 */         DefaultLobHandler.this.logger.debug(content != null ? "Set bytes for BLOB with length " + content.length : 
/* 128:206 */           "Set BLOB to null");
/* 129:    */       }
/* 130:    */     }
/* 131:    */     
/* 132:    */     public void setBlobAsBinaryStream(PreparedStatement ps, int paramIndex, InputStream binaryStream, int contentLength)
/* 133:    */       throws SQLException
/* 134:    */     {
/* 135:214 */       if (DefaultLobHandler.this.streamAsLob)
/* 136:    */       {
/* 137:215 */         if (binaryStream != null) {
/* 138:216 */           ps.setBlob(paramIndex, binaryStream, contentLength);
/* 139:    */         } else {
/* 140:219 */           ps.setBlob(paramIndex, null);
/* 141:    */         }
/* 142:    */       }
/* 143:222 */       else if (DefaultLobHandler.this.wrapAsLob)
/* 144:    */       {
/* 145:223 */         if (binaryStream != null) {
/* 146:224 */           ps.setBlob(paramIndex, new PassThroughBlob(binaryStream, contentLength));
/* 147:    */         } else {
/* 148:227 */           ps.setBlob(paramIndex, null);
/* 149:    */         }
/* 150:    */       }
/* 151:    */       else {
/* 152:231 */         ps.setBinaryStream(paramIndex, binaryStream, contentLength);
/* 153:    */       }
/* 154:233 */       if (DefaultLobHandler.this.logger.isDebugEnabled()) {
/* 155:234 */         DefaultLobHandler.this.logger.debug(binaryStream != null ? "Set binary stream for BLOB with length " + contentLength : 
/* 156:235 */           "Set BLOB to null");
/* 157:    */       }
/* 158:    */     }
/* 159:    */     
/* 160:    */     public void setClobAsString(PreparedStatement ps, int paramIndex, String content)
/* 161:    */       throws SQLException
/* 162:    */     {
/* 163:242 */       if (DefaultLobHandler.this.streamAsLob)
/* 164:    */       {
/* 165:243 */         if (content != null) {
/* 166:244 */           ps.setClob(paramIndex, new StringReader(content), content.length());
/* 167:    */         } else {
/* 168:247 */           ps.setClob(paramIndex, null);
/* 169:    */         }
/* 170:    */       }
/* 171:250 */       else if (DefaultLobHandler.this.wrapAsLob)
/* 172:    */       {
/* 173:251 */         if (content != null) {
/* 174:252 */           ps.setClob(paramIndex, new PassThroughClob(content));
/* 175:    */         } else {
/* 176:255 */           ps.setClob(paramIndex, null);
/* 177:    */         }
/* 178:    */       }
/* 179:    */       else {
/* 180:259 */         ps.setString(paramIndex, content);
/* 181:    */       }
/* 182:261 */       if (DefaultLobHandler.this.logger.isDebugEnabled()) {
/* 183:262 */         DefaultLobHandler.this.logger.debug(content != null ? "Set string for CLOB with length " + content.length() : 
/* 184:263 */           "Set CLOB to null");
/* 185:    */       }
/* 186:    */     }
/* 187:    */     
/* 188:    */     public void setClobAsAsciiStream(PreparedStatement ps, int paramIndex, InputStream asciiStream, int contentLength)
/* 189:    */       throws SQLException
/* 190:    */     {
/* 191:271 */       if ((DefaultLobHandler.this.streamAsLob) || (DefaultLobHandler.this.wrapAsLob))
/* 192:    */       {
/* 193:272 */         if (asciiStream != null) {
/* 194:    */           try
/* 195:    */           {
/* 196:274 */             if (DefaultLobHandler.this.streamAsLob) {
/* 197:275 */               ps.setClob(paramIndex, new InputStreamReader(asciiStream, "US-ASCII"), contentLength);
/* 198:    */             } else {
/* 199:278 */               ps.setClob(paramIndex, new PassThroughClob(asciiStream, contentLength));
/* 200:    */             }
/* 201:    */           }
/* 202:    */           catch (UnsupportedEncodingException ex)
/* 203:    */           {
/* 204:282 */             throw new SQLException("US-ASCII encoding not supported: " + ex);
/* 205:    */           }
/* 206:    */         } else {
/* 207:286 */           ps.setClob(paramIndex, null);
/* 208:    */         }
/* 209:    */       }
/* 210:    */       else {
/* 211:290 */         ps.setAsciiStream(paramIndex, asciiStream, contentLength);
/* 212:    */       }
/* 213:292 */       if (DefaultLobHandler.this.logger.isDebugEnabled()) {
/* 214:293 */         DefaultLobHandler.this.logger.debug(asciiStream != null ? "Set ASCII stream for CLOB with length " + contentLength : 
/* 215:294 */           "Set CLOB to null");
/* 216:    */       }
/* 217:    */     }
/* 218:    */     
/* 219:    */     public void setClobAsCharacterStream(PreparedStatement ps, int paramIndex, Reader characterStream, int contentLength)
/* 220:    */       throws SQLException
/* 221:    */     {
/* 222:302 */       if (DefaultLobHandler.this.streamAsLob)
/* 223:    */       {
/* 224:303 */         if (characterStream != null) {
/* 225:304 */           ps.setClob(paramIndex, characterStream, contentLength);
/* 226:    */         } else {
/* 227:307 */           ps.setClob(paramIndex, null);
/* 228:    */         }
/* 229:    */       }
/* 230:310 */       else if (DefaultLobHandler.this.wrapAsLob)
/* 231:    */       {
/* 232:311 */         if (characterStream != null) {
/* 233:312 */           ps.setClob(paramIndex, new PassThroughClob(characterStream, contentLength));
/* 234:    */         } else {
/* 235:315 */           ps.setClob(paramIndex, null);
/* 236:    */         }
/* 237:    */       }
/* 238:    */       else {
/* 239:319 */         ps.setCharacterStream(paramIndex, characterStream, contentLength);
/* 240:    */       }
/* 241:321 */       if (DefaultLobHandler.this.logger.isDebugEnabled()) {
/* 242:322 */         DefaultLobHandler.this.logger.debug(characterStream != null ? "Set character stream for CLOB with length " + contentLength : 
/* 243:323 */           "Set CLOB to null");
/* 244:    */       }
/* 245:    */     }
/* 246:    */     
/* 247:    */     public void close() {}
/* 248:    */   }
/* 249:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.lob.DefaultLobHandler
 * JD-Core Version:    0.7.0.1
 */