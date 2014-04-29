/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.io.BufferedReader;
/*   4:    */ import java.io.BufferedWriter;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.OutputStream;
/*   8:    */ import java.io.Reader;
/*   9:    */ import java.io.Writer;
/*  10:    */ import java.util.Date;
/*  11:    */ import java.util.Enumeration;
/*  12:    */ import java.util.Properties;
/*  13:    */ 
/*  14:    */ public class DefaultPropertiesPersister
/*  15:    */   implements PropertiesPersister
/*  16:    */ {
/*  17: 71 */   private static final boolean loadFromReaderAvailable = ClassUtils.hasMethod(Properties.class, "load", new Class[] { Reader.class });
/*  18: 75 */   private static final boolean storeToWriterAvailable = ClassUtils.hasMethod(Properties.class, "store", new Class[] { Writer.class, String.class });
/*  19:    */   
/*  20:    */   public void load(Properties props, InputStream is)
/*  21:    */     throws IOException
/*  22:    */   {
/*  23: 79 */     props.load(is);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void load(Properties props, Reader reader)
/*  27:    */     throws IOException
/*  28:    */   {
/*  29: 83 */     if (loadFromReaderAvailable) {
/*  30: 85 */       props.load(reader);
/*  31:    */     } else {
/*  32: 89 */       doLoad(props, reader);
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected void doLoad(Properties props, Reader reader)
/*  37:    */     throws IOException
/*  38:    */   {
/*  39: 94 */     BufferedReader in = new BufferedReader(reader);
/*  40:    */     for (;;)
/*  41:    */     {
/*  42: 96 */       String line = in.readLine();
/*  43: 97 */       if (line == null) {
/*  44: 98 */         return;
/*  45:    */       }
/*  46:100 */       line = StringUtils.trimLeadingWhitespace(line);
/*  47:101 */       if (line.length() > 0)
/*  48:    */       {
/*  49:102 */         char firstChar = line.charAt(0);
/*  50:103 */         if ((firstChar != '#') && (firstChar != '!'))
/*  51:    */         {
/*  52:104 */           while (endsWithContinuationMarker(line))
/*  53:    */           {
/*  54:105 */             String nextLine = in.readLine();
/*  55:106 */             line = line.substring(0, line.length() - 1);
/*  56:107 */             if (nextLine != null) {
/*  57:108 */               line = line + StringUtils.trimLeadingWhitespace(nextLine);
/*  58:    */             }
/*  59:    */           }
/*  60:111 */           int separatorIndex = line.indexOf("=");
/*  61:112 */           if (separatorIndex == -1) {
/*  62:113 */             separatorIndex = line.indexOf(":");
/*  63:    */           }
/*  64:115 */           String key = separatorIndex != -1 ? line.substring(0, separatorIndex) : line;
/*  65:116 */           String value = separatorIndex != -1 ? line.substring(separatorIndex + 1) : "";
/*  66:117 */           key = StringUtils.trimTrailingWhitespace(key);
/*  67:118 */           value = StringUtils.trimLeadingWhitespace(value);
/*  68:119 */           props.put(unescape(key), unescape(value));
/*  69:    */         }
/*  70:    */       }
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected boolean endsWithContinuationMarker(String line)
/*  75:    */   {
/*  76:126 */     boolean evenSlashCount = true;
/*  77:127 */     int index = line.length() - 1;
/*  78:128 */     while ((index >= 0) && (line.charAt(index) == '\\'))
/*  79:    */     {
/*  80:129 */       evenSlashCount = !evenSlashCount;
/*  81:130 */       index--;
/*  82:    */     }
/*  83:132 */     return !evenSlashCount;
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected String unescape(String str)
/*  87:    */   {
/*  88:136 */     StringBuilder result = new StringBuilder(str.length());
/*  89:137 */     for (int index = 0; index < str.length();)
/*  90:    */     {
/*  91:138 */       char c = str.charAt(index++);
/*  92:139 */       if (c == '\\')
/*  93:    */       {
/*  94:140 */         c = str.charAt(index++);
/*  95:141 */         if (c == 't') {
/*  96:142 */           c = '\t';
/*  97:144 */         } else if (c == 'r') {
/*  98:145 */           c = '\r';
/*  99:147 */         } else if (c == 'n') {
/* 100:148 */           c = '\n';
/* 101:150 */         } else if (c == 'f') {
/* 102:151 */           c = '\f';
/* 103:    */         }
/* 104:    */       }
/* 105:154 */       result.append(c);
/* 106:    */     }
/* 107:156 */     return result.toString();
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void store(Properties props, OutputStream os, String header)
/* 111:    */     throws IOException
/* 112:    */   {
/* 113:161 */     props.store(os, header);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public void store(Properties props, Writer writer, String header)
/* 117:    */     throws IOException
/* 118:    */   {
/* 119:165 */     if (storeToWriterAvailable) {
/* 120:167 */       props.store(writer, header);
/* 121:    */     } else {
/* 122:171 */       doStore(props, writer, header);
/* 123:    */     }
/* 124:    */   }
/* 125:    */   
/* 126:    */   protected void doStore(Properties props, Writer writer, String header)
/* 127:    */     throws IOException
/* 128:    */   {
/* 129:176 */     BufferedWriter out = new BufferedWriter(writer);
/* 130:177 */     if (header != null)
/* 131:    */     {
/* 132:178 */       out.write("#" + header);
/* 133:179 */       out.newLine();
/* 134:    */     }
/* 135:181 */     out.write("#" + new Date());
/* 136:182 */     out.newLine();
/* 137:183 */     for (Enumeration keys = props.keys(); keys.hasMoreElements();)
/* 138:    */     {
/* 139:184 */       String key = (String)keys.nextElement();
/* 140:185 */       String val = props.getProperty(key);
/* 141:186 */       out.write(escape(key, true) + "=" + escape(val, false));
/* 142:187 */       out.newLine();
/* 143:    */     }
/* 144:189 */     out.flush();
/* 145:    */   }
/* 146:    */   
/* 147:    */   protected String escape(String str, boolean isKey)
/* 148:    */   {
/* 149:193 */     int len = str.length();
/* 150:194 */     StringBuilder result = new StringBuilder(len * 2);
/* 151:195 */     for (int index = 0; index < len; index++)
/* 152:    */     {
/* 153:196 */       char c = str.charAt(index);
/* 154:197 */       switch (c)
/* 155:    */       {
/* 156:    */       case ' ': 
/* 157:199 */         if ((index == 0) || (isKey)) {
/* 158:200 */           result.append('\\');
/* 159:    */         }
/* 160:202 */         result.append(' ');
/* 161:203 */         break;
/* 162:    */       case '\\': 
/* 163:205 */         result.append("\\\\");
/* 164:206 */         break;
/* 165:    */       case '\t': 
/* 166:208 */         result.append("\\t");
/* 167:209 */         break;
/* 168:    */       case '\n': 
/* 169:211 */         result.append("\\n");
/* 170:212 */         break;
/* 171:    */       case '\r': 
/* 172:214 */         result.append("\\r");
/* 173:215 */         break;
/* 174:    */       case '\f': 
/* 175:217 */         result.append("\\f");
/* 176:218 */         break;
/* 177:    */       default: 
/* 178:220 */         if ("=: \t\r\n\f#!".indexOf(c) != -1) {
/* 179:221 */           result.append('\\');
/* 180:    */         }
/* 181:223 */         result.append(c);
/* 182:    */       }
/* 183:    */     }
/* 184:226 */     return result.toString();
/* 185:    */   }
/* 186:    */   
/* 187:    */   public void loadFromXml(Properties props, InputStream is)
/* 188:    */     throws IOException
/* 189:    */   {
/* 190:    */     try
/* 191:    */     {
/* 192:232 */       props.loadFromXML(is);
/* 193:    */     }
/* 194:    */     catch (NoSuchMethodError err)
/* 195:    */     {
/* 196:235 */       throw new IOException("Cannot load properties XML file - not running on JDK 1.5+: " + err.getMessage());
/* 197:    */     }
/* 198:    */   }
/* 199:    */   
/* 200:    */   public void storeToXml(Properties props, OutputStream os, String header)
/* 201:    */     throws IOException
/* 202:    */   {
/* 203:    */     try
/* 204:    */     {
/* 205:241 */       props.storeToXML(os, header);
/* 206:    */     }
/* 207:    */     catch (NoSuchMethodError err)
/* 208:    */     {
/* 209:244 */       throw new IOException("Cannot store properties XML file - not running on JDK 1.5+: " + err.getMessage());
/* 210:    */     }
/* 211:    */   }
/* 212:    */   
/* 213:    */   public void storeToXml(Properties props, OutputStream os, String header, String encoding)
/* 214:    */     throws IOException
/* 215:    */   {
/* 216:    */     try
/* 217:    */     {
/* 218:250 */       props.storeToXML(os, header, encoding);
/* 219:    */     }
/* 220:    */     catch (NoSuchMethodError err)
/* 221:    */     {
/* 222:253 */       throw new IOException("Cannot store properties XML file - not running on JDK 1.5+: " + err.getMessage());
/* 223:    */     }
/* 224:    */   }
/* 225:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.DefaultPropertiesPersister
 * JD-Core Version:    0.7.0.1
 */