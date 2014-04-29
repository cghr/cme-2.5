/*   1:    */ package org.springframework.web.multipart.commons;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.Serializable;
/*   8:    */ import org.apache.commons.fileupload.FileItem;
/*   9:    */ import org.apache.commons.fileupload.FileUploadException;
/*  10:    */ import org.apache.commons.fileupload.disk.DiskFileItem;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.apache.commons.logging.LogFactory;
/*  13:    */ import org.springframework.web.multipart.MultipartFile;
/*  14:    */ 
/*  15:    */ public class CommonsMultipartFile
/*  16:    */   implements MultipartFile, Serializable
/*  17:    */ {
/*  18: 45 */   protected static final Log logger = LogFactory.getLog(CommonsMultipartFile.class);
/*  19:    */   private final FileItem fileItem;
/*  20:    */   private final long size;
/*  21:    */   
/*  22:    */   public CommonsMultipartFile(FileItem fileItem)
/*  23:    */   {
/*  24: 57 */     this.fileItem = fileItem;
/*  25: 58 */     this.size = this.fileItem.getSize();
/*  26:    */   }
/*  27:    */   
/*  28:    */   public final FileItem getFileItem()
/*  29:    */   {
/*  30: 66 */     return this.fileItem;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public String getName()
/*  34:    */   {
/*  35: 71 */     return this.fileItem.getFieldName();
/*  36:    */   }
/*  37:    */   
/*  38:    */   public String getOriginalFilename()
/*  39:    */   {
/*  40: 75 */     String filename = this.fileItem.getName();
/*  41: 76 */     if (filename == null) {
/*  42: 78 */       return "";
/*  43:    */     }
/*  44: 81 */     int pos = filename.lastIndexOf("/");
/*  45: 82 */     if (pos == -1) {
/*  46: 84 */       pos = filename.lastIndexOf("\\");
/*  47:    */     }
/*  48: 86 */     if (pos != -1) {
/*  49: 88 */       return filename.substring(pos + 1);
/*  50:    */     }
/*  51: 92 */     return filename;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public String getContentType()
/*  55:    */   {
/*  56: 97 */     return this.fileItem.getContentType();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public boolean isEmpty()
/*  60:    */   {
/*  61:101 */     return this.size == 0L;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public long getSize()
/*  65:    */   {
/*  66:105 */     return this.size;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public byte[] getBytes()
/*  70:    */   {
/*  71:109 */     if (!isAvailable()) {
/*  72:110 */       throw new IllegalStateException("File has been moved - cannot be read again");
/*  73:    */     }
/*  74:112 */     byte[] bytes = this.fileItem.get();
/*  75:113 */     return bytes != null ? bytes : new byte[0];
/*  76:    */   }
/*  77:    */   
/*  78:    */   public InputStream getInputStream()
/*  79:    */     throws IOException
/*  80:    */   {
/*  81:117 */     if (!isAvailable()) {
/*  82:118 */       throw new IllegalStateException("File has been moved - cannot be read again");
/*  83:    */     }
/*  84:120 */     InputStream inputStream = this.fileItem.getInputStream();
/*  85:121 */     return inputStream != null ? inputStream : new ByteArrayInputStream(new byte[0]);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void transferTo(File dest)
/*  89:    */     throws IOException, IllegalStateException
/*  90:    */   {
/*  91:125 */     if (!isAvailable()) {
/*  92:126 */       throw new IllegalStateException("File has already been moved - cannot be transferred again");
/*  93:    */     }
/*  94:129 */     if ((dest.exists()) && (!dest.delete())) {
/*  95:130 */       throw new IOException(
/*  96:131 */         "Destination file [" + dest.getAbsolutePath() + "] already exists and could not be deleted");
/*  97:    */     }
/*  98:    */     try
/*  99:    */     {
/* 100:135 */       this.fileItem.write(dest);
/* 101:136 */       if (logger.isDebugEnabled())
/* 102:    */       {
/* 103:137 */         String action = "transferred";
/* 104:138 */         if (!this.fileItem.isInMemory()) {
/* 105:139 */           action = isAvailable() ? "copied" : "moved";
/* 106:    */         }
/* 107:141 */         logger.debug("Multipart file '" + getName() + "' with original filename [" + 
/* 108:142 */           getOriginalFilename() + "], stored " + getStorageDescription() + ": " + 
/* 109:143 */           action + " to [" + dest.getAbsolutePath() + "]");
/* 110:    */       }
/* 111:    */     }
/* 112:    */     catch (FileUploadException ex)
/* 113:    */     {
/* 114:147 */       throw new IllegalStateException(ex.getMessage());
/* 115:    */     }
/* 116:    */     catch (IOException ex)
/* 117:    */     {
/* 118:150 */       throw ex;
/* 119:    */     }
/* 120:    */     catch (Exception ex)
/* 121:    */     {
/* 122:153 */       logger.error("Could not transfer to file", ex);
/* 123:154 */       throw new IOException("Could not transfer to file: " + ex.getMessage());
/* 124:    */     }
/* 125:    */   }
/* 126:    */   
/* 127:    */   protected boolean isAvailable()
/* 128:    */   {
/* 129:164 */     if (this.fileItem.isInMemory()) {
/* 130:165 */       return true;
/* 131:    */     }
/* 132:168 */     if ((this.fileItem instanceof DiskFileItem)) {
/* 133:169 */       return ((DiskFileItem)this.fileItem).getStoreLocation().exists();
/* 134:    */     }
/* 135:172 */     return this.fileItem.getSize() == this.size;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public String getStorageDescription()
/* 139:    */   {
/* 140:181 */     if (this.fileItem.isInMemory()) {
/* 141:182 */       return "in memory";
/* 142:    */     }
/* 143:184 */     if ((this.fileItem instanceof DiskFileItem)) {
/* 144:185 */       return "at [" + ((DiskFileItem)this.fileItem).getStoreLocation().getAbsolutePath() + "]";
/* 145:    */     }
/* 146:188 */     return "on disk";
/* 147:    */   }
/* 148:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.commons.CommonsMultipartFile
 * JD-Core Version:    0.7.0.1
 */