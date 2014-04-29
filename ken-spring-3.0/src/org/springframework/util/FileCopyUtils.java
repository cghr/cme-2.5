/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.io.BufferedInputStream;
/*   4:    */ import java.io.BufferedOutputStream;
/*   5:    */ import java.io.ByteArrayInputStream;
/*   6:    */ import java.io.ByteArrayOutputStream;
/*   7:    */ import java.io.File;
/*   8:    */ import java.io.FileInputStream;
/*   9:    */ import java.io.FileOutputStream;
/*  10:    */ import java.io.IOException;
/*  11:    */ import java.io.InputStream;
/*  12:    */ import java.io.OutputStream;
/*  13:    */ import java.io.Reader;
/*  14:    */ import java.io.StringWriter;
/*  15:    */ import java.io.Writer;
/*  16:    */ 
/*  17:    */ public abstract class FileCopyUtils
/*  18:    */ {
/*  19:    */   public static final int BUFFER_SIZE = 4096;
/*  20:    */   
/*  21:    */   public static int copy(File in, File out)
/*  22:    */     throws IOException
/*  23:    */   {
/*  24: 61 */     Assert.notNull(in, "No input File specified");
/*  25: 62 */     Assert.notNull(out, "No output File specified");
/*  26: 63 */     return copy(new BufferedInputStream(new FileInputStream(in)), 
/*  27: 64 */       new BufferedOutputStream(new FileOutputStream(out)));
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static void copy(byte[] in, File out)
/*  31:    */     throws IOException
/*  32:    */   {
/*  33: 74 */     Assert.notNull(in, "No input byte array specified");
/*  34: 75 */     Assert.notNull(out, "No output File specified");
/*  35: 76 */     ByteArrayInputStream inStream = new ByteArrayInputStream(in);
/*  36: 77 */     OutputStream outStream = new BufferedOutputStream(new FileOutputStream(out));
/*  37: 78 */     copy(inStream, outStream);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static byte[] copyToByteArray(File in)
/*  41:    */     throws IOException
/*  42:    */   {
/*  43: 88 */     Assert.notNull(in, "No input File specified");
/*  44: 89 */     return copyToByteArray(new BufferedInputStream(new FileInputStream(in)));
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static int copy(InputStream in, OutputStream out)
/*  48:    */     throws IOException
/*  49:    */   {
/*  50:106 */     Assert.notNull(in, "No InputStream specified");
/*  51:107 */     Assert.notNull(out, "No OutputStream specified");
/*  52:    */     try
/*  53:    */     {
/*  54:109 */       int byteCount = 0;
/*  55:110 */       byte[] buffer = new byte[4096];
/*  56:111 */       int bytesRead = -1;
/*  57:112 */       while ((bytesRead = in.read(buffer)) != -1)
/*  58:    */       {
/*  59:113 */         out.write(buffer, 0, bytesRead);
/*  60:114 */         byteCount += bytesRead;
/*  61:    */       }
/*  62:116 */       out.flush();
/*  63:117 */       return byteCount;
/*  64:    */     }
/*  65:    */     finally
/*  66:    */     {
/*  67:    */       try
/*  68:    */       {
/*  69:121 */         in.close();
/*  70:    */       }
/*  71:    */       catch (IOException localIOException3) {}
/*  72:    */       try
/*  73:    */       {
/*  74:126 */         out.close();
/*  75:    */       }
/*  76:    */       catch (IOException localIOException4) {}
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static void copy(byte[] in, OutputStream out)
/*  81:    */     throws IOException
/*  82:    */   {
/*  83:141 */     Assert.notNull(in, "No input byte array specified");
/*  84:142 */     Assert.notNull(out, "No OutputStream specified");
/*  85:    */     try
/*  86:    */     {
/*  87:144 */       out.write(in);
/*  88:    */     }
/*  89:    */     finally
/*  90:    */     {
/*  91:    */       try
/*  92:    */       {
/*  93:148 */         out.close();
/*  94:    */       }
/*  95:    */       catch (IOException localIOException1) {}
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   public static byte[] copyToByteArray(InputStream in)
/* 100:    */     throws IOException
/* 101:    */   {
/* 102:163 */     ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
/* 103:164 */     copy(in, out);
/* 104:165 */     return out.toByteArray();
/* 105:    */   }
/* 106:    */   
/* 107:    */   public static int copy(Reader in, Writer out)
/* 108:    */     throws IOException
/* 109:    */   {
/* 110:182 */     Assert.notNull(in, "No Reader specified");
/* 111:183 */     Assert.notNull(out, "No Writer specified");
/* 112:    */     try
/* 113:    */     {
/* 114:185 */       int byteCount = 0;
/* 115:186 */       char[] buffer = new char[4096];
/* 116:187 */       int bytesRead = -1;
/* 117:188 */       while ((bytesRead = in.read(buffer)) != -1)
/* 118:    */       {
/* 119:189 */         out.write(buffer, 0, bytesRead);
/* 120:190 */         byteCount += bytesRead;
/* 121:    */       }
/* 122:192 */       out.flush();
/* 123:193 */       return byteCount;
/* 124:    */     }
/* 125:    */     finally
/* 126:    */     {
/* 127:    */       try
/* 128:    */       {
/* 129:197 */         in.close();
/* 130:    */       }
/* 131:    */       catch (IOException localIOException3) {}
/* 132:    */       try
/* 133:    */       {
/* 134:202 */         out.close();
/* 135:    */       }
/* 136:    */       catch (IOException localIOException4) {}
/* 137:    */     }
/* 138:    */   }
/* 139:    */   
/* 140:    */   public static void copy(String in, Writer out)
/* 141:    */     throws IOException
/* 142:    */   {
/* 143:217 */     Assert.notNull(in, "No input String specified");
/* 144:218 */     Assert.notNull(out, "No Writer specified");
/* 145:    */     try
/* 146:    */     {
/* 147:220 */       out.write(in);
/* 148:    */     }
/* 149:    */     finally
/* 150:    */     {
/* 151:    */       try
/* 152:    */       {
/* 153:224 */         out.close();
/* 154:    */       }
/* 155:    */       catch (IOException localIOException1) {}
/* 156:    */     }
/* 157:    */   }
/* 158:    */   
/* 159:    */   public static String copyToString(Reader in)
/* 160:    */     throws IOException
/* 161:    */   {
/* 162:239 */     StringWriter out = new StringWriter();
/* 163:240 */     copy(in, out);
/* 164:241 */     return out.toString();
/* 165:    */   }
/* 166:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.FileCopyUtils
 * JD-Core Version:    0.7.0.1
 */