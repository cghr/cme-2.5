/*   1:    */ package org.springframework.http.converter;
/*   2:    */ 
/*   3:    */ import java.awt.image.BufferedImage;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.OutputStream;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.Collections;
/*  10:    */ import java.util.Iterator;
/*  11:    */ import java.util.List;
/*  12:    */ import javax.imageio.IIOImage;
/*  13:    */ import javax.imageio.ImageIO;
/*  14:    */ import javax.imageio.ImageReadParam;
/*  15:    */ import javax.imageio.ImageReader;
/*  16:    */ import javax.imageio.ImageWriteParam;
/*  17:    */ import javax.imageio.ImageWriter;
/*  18:    */ import javax.imageio.stream.FileCacheImageInputStream;
/*  19:    */ import javax.imageio.stream.FileCacheImageOutputStream;
/*  20:    */ import javax.imageio.stream.ImageInputStream;
/*  21:    */ import javax.imageio.stream.ImageOutputStream;
/*  22:    */ import javax.imageio.stream.MemoryCacheImageInputStream;
/*  23:    */ import javax.imageio.stream.MemoryCacheImageOutputStream;
/*  24:    */ import org.springframework.http.HttpHeaders;
/*  25:    */ import org.springframework.http.HttpInputMessage;
/*  26:    */ import org.springframework.http.HttpOutputMessage;
/*  27:    */ import org.springframework.http.MediaType;
/*  28:    */ import org.springframework.util.Assert;
/*  29:    */ 
/*  30:    */ public class BufferedImageHttpMessageConverter
/*  31:    */   implements HttpMessageConverter<BufferedImage>
/*  32:    */ {
/*  33: 65 */   private final List<MediaType> readableMediaTypes = new ArrayList();
/*  34:    */   private MediaType defaultContentType;
/*  35:    */   private File cacheDir;
/*  36:    */   
/*  37:    */   public BufferedImageHttpMessageConverter()
/*  38:    */   {
/*  39: 73 */     String[] readerMediaTypes = ImageIO.getReaderMIMETypes();
/*  40: 74 */     for (String mediaType : readerMediaTypes) {
/*  41: 75 */       this.readableMediaTypes.add(MediaType.parseMediaType(mediaType));
/*  42:    */     }
/*  43: 78 */     String[] writerMediaTypes = ImageIO.getWriterMIMETypes();
/*  44: 79 */     if (writerMediaTypes.length > 0) {
/*  45: 80 */       this.defaultContentType = MediaType.parseMediaType(writerMediaTypes[0]);
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setDefaultContentType(MediaType defaultContentType)
/*  50:    */   {
/*  51: 89 */     Assert.notNull(defaultContentType, "'contentType' must not be null");
/*  52: 90 */     Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByMIMEType(defaultContentType.toString());
/*  53: 91 */     if (!imageWriters.hasNext()) {
/*  54: 92 */       throw new IllegalArgumentException(
/*  55: 93 */         "ContentType [" + defaultContentType + "] is not supported by the Java Image I/O API");
/*  56:    */     }
/*  57: 96 */     this.defaultContentType = defaultContentType;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public MediaType getDefaultContentType()
/*  61:    */   {
/*  62:104 */     return this.defaultContentType;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setCacheDir(File cacheDir)
/*  66:    */   {
/*  67:112 */     Assert.notNull(cacheDir, "'cacheDir' must not be null");
/*  68:113 */     Assert.isTrue(cacheDir.isDirectory(), "'cacheDir' is not a directory");
/*  69:114 */     this.cacheDir = cacheDir;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public boolean canRead(Class<?> clazz, MediaType mediaType)
/*  73:    */   {
/*  74:119 */     return (BufferedImage.class.equals(clazz)) && (isReadable(mediaType));
/*  75:    */   }
/*  76:    */   
/*  77:    */   private boolean isReadable(MediaType mediaType)
/*  78:    */   {
/*  79:123 */     if (mediaType == null) {
/*  80:124 */       return true;
/*  81:    */     }
/*  82:126 */     Iterator<ImageReader> imageReaders = ImageIO.getImageReadersByMIMEType(mediaType.toString());
/*  83:127 */     return imageReaders.hasNext();
/*  84:    */   }
/*  85:    */   
/*  86:    */   public boolean canWrite(Class<?> clazz, MediaType mediaType)
/*  87:    */   {
/*  88:131 */     return (BufferedImage.class.equals(clazz)) && (isWritable(mediaType));
/*  89:    */   }
/*  90:    */   
/*  91:    */   private boolean isWritable(MediaType mediaType)
/*  92:    */   {
/*  93:135 */     if (mediaType == null) {
/*  94:136 */       return true;
/*  95:    */     }
/*  96:138 */     Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByMIMEType(mediaType.toString());
/*  97:139 */     return imageWriters.hasNext();
/*  98:    */   }
/*  99:    */   
/* 100:    */   public List<MediaType> getSupportedMediaTypes()
/* 101:    */   {
/* 102:143 */     return Collections.unmodifiableList(this.readableMediaTypes);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public BufferedImage read(Class<? extends BufferedImage> clazz, HttpInputMessage inputMessage)
/* 106:    */     throws IOException, HttpMessageNotReadableException
/* 107:    */   {
/* 108:149 */     ImageInputStream imageInputStream = null;
/* 109:150 */     ImageReader imageReader = null;
/* 110:    */     try
/* 111:    */     {
/* 112:152 */       imageInputStream = createImageInputStream(inputMessage.getBody());
/* 113:153 */       MediaType contentType = inputMessage.getHeaders().getContentType();
/* 114:154 */       Iterator<ImageReader> imageReaders = ImageIO.getImageReadersByMIMEType(contentType.toString());
/* 115:155 */       if (imageReaders.hasNext())
/* 116:    */       {
/* 117:156 */         imageReader = (ImageReader)imageReaders.next();
/* 118:157 */         ImageReadParam irp = imageReader.getDefaultReadParam();
/* 119:158 */         process(irp);
/* 120:159 */         imageReader.setInput(imageInputStream, true);
/* 121:160 */         return imageReader.read(0, irp);
/* 122:    */       }
/* 123:    */       Iterator<ImageReader> imageReaders;
/* 124:    */       MediaType contentType;
/* 125:163 */       throw new HttpMessageNotReadableException(
/* 126:164 */         "Could not find javax.imageio.ImageReader for Content-Type [" + contentType + "]");
/* 127:    */     }
/* 128:    */     finally
/* 129:    */     {
/* 130:168 */       if (imageReader != null) {
/* 131:169 */         imageReader.dispose();
/* 132:    */       }
/* 133:171 */       if (imageInputStream != null) {
/* 134:    */         try
/* 135:    */         {
/* 136:173 */           imageInputStream.close();
/* 137:    */         }
/* 138:    */         catch (IOException localIOException2) {}
/* 139:    */       }
/* 140:    */     }
/* 141:    */   }
/* 142:    */   
/* 143:    */   private ImageInputStream createImageInputStream(InputStream is)
/* 144:    */     throws IOException
/* 145:    */   {
/* 146:183 */     if (this.cacheDir != null) {
/* 147:184 */       return new FileCacheImageInputStream(is, this.cacheDir);
/* 148:    */     }
/* 149:187 */     return new MemoryCacheImageInputStream(is);
/* 150:    */   }
/* 151:    */   
/* 152:    */   public void write(BufferedImage image, MediaType contentType, HttpOutputMessage outputMessage)
/* 153:    */     throws IOException, HttpMessageNotWritableException
/* 154:    */   {
/* 155:194 */     if (contentType == null) {
/* 156:195 */       contentType = getDefaultContentType();
/* 157:    */     }
/* 158:197 */     Assert.notNull(contentType, 
/* 159:198 */       "Count not determine Content-Type, set one using the 'defaultContentType' property");
/* 160:199 */     outputMessage.getHeaders().setContentType(contentType);
/* 161:200 */     ImageOutputStream imageOutputStream = null;
/* 162:201 */     ImageWriter imageWriter = null;
/* 163:    */     try
/* 164:    */     {
/* 165:203 */       imageOutputStream = createImageOutputStream(outputMessage.getBody());
/* 166:204 */       Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByMIMEType(contentType.toString());
/* 167:205 */       if (imageWriters.hasNext())
/* 168:    */       {
/* 169:206 */         imageWriter = (ImageWriter)imageWriters.next();
/* 170:207 */         ImageWriteParam iwp = imageWriter.getDefaultWriteParam();
/* 171:208 */         process(iwp);
/* 172:209 */         imageWriter.setOutput(imageOutputStream);
/* 173:210 */         imageWriter.write(null, new IIOImage(image, null, null), iwp);
/* 174:    */       }
/* 175:    */       else
/* 176:    */       {
/* 177:213 */         throw new HttpMessageNotWritableException(
/* 178:214 */           "Could not find javax.imageio.ImageWriter for Content-Type [" + contentType + "]");
/* 179:    */       }
/* 180:    */     }
/* 181:    */     finally
/* 182:    */     {
/* 183:218 */       if (imageWriter != null) {
/* 184:219 */         imageWriter.dispose();
/* 185:    */       }
/* 186:221 */       if (imageOutputStream != null) {
/* 187:    */         try
/* 188:    */         {
/* 189:223 */           imageOutputStream.close();
/* 190:    */         }
/* 191:    */         catch (IOException localIOException1) {}
/* 192:    */       }
/* 193:    */     }
/* 194:218 */     if (imageWriter != null) {
/* 195:219 */       imageWriter.dispose();
/* 196:    */     }
/* 197:221 */     if (imageOutputStream != null) {
/* 198:    */       try
/* 199:    */       {
/* 200:223 */         imageOutputStream.close();
/* 201:    */       }
/* 202:    */       catch (IOException localIOException2) {}
/* 203:    */     }
/* 204:    */   }
/* 205:    */   
/* 206:    */   private ImageOutputStream createImageOutputStream(OutputStream os)
/* 207:    */     throws IOException
/* 208:    */   {
/* 209:233 */     if (this.cacheDir != null) {
/* 210:234 */       return new FileCacheImageOutputStream(os, this.cacheDir);
/* 211:    */     }
/* 212:237 */     return new MemoryCacheImageOutputStream(os);
/* 213:    */   }
/* 214:    */   
/* 215:    */   protected void process(ImageReadParam irp) {}
/* 216:    */   
/* 217:    */   protected void process(ImageWriteParam iwp) {}
/* 218:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.BufferedImageHttpMessageConverter
 * JD-Core Version:    0.7.0.1
 */