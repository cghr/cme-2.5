/*   1:    */ package org.springframework.mail.javamail;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import javax.activation.FileTypeMap;
/*   7:    */ import javax.activation.MimetypesFileTypeMap;
/*   8:    */ import org.springframework.beans.factory.InitializingBean;
/*   9:    */ import org.springframework.core.io.ClassPathResource;
/*  10:    */ import org.springframework.core.io.Resource;
/*  11:    */ 
/*  12:    */ public class ConfigurableMimeFileTypeMap
/*  13:    */   extends FileTypeMap
/*  14:    */   implements InitializingBean
/*  15:    */ {
/*  16: 66 */   private Resource mappingLocation = new ClassPathResource("mime.types", getClass());
/*  17:    */   private String[] mappings;
/*  18:    */   private FileTypeMap fileTypeMap;
/*  19:    */   
/*  20:    */   public void setMappingLocation(Resource mappingLocation)
/*  21:    */   {
/*  22: 87 */     this.mappingLocation = mappingLocation;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setMappings(String[] mappings)
/*  26:    */   {
/*  27: 97 */     this.mappings = mappings;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void afterPropertiesSet()
/*  31:    */   {
/*  32:105 */     getFileTypeMap();
/*  33:    */   }
/*  34:    */   
/*  35:    */   protected final FileTypeMap getFileTypeMap()
/*  36:    */   {
/*  37:116 */     if (this.fileTypeMap == null) {
/*  38:    */       try
/*  39:    */       {
/*  40:118 */         this.fileTypeMap = createFileTypeMap(this.mappingLocation, this.mappings);
/*  41:    */       }
/*  42:    */       catch (IOException ex)
/*  43:    */       {
/*  44:121 */         throw new IllegalStateException(
/*  45:122 */           "Could not load specified MIME type mapping file: " + this.mappingLocation, ex);
/*  46:    */       }
/*  47:    */     }
/*  48:125 */     return this.fileTypeMap;
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected FileTypeMap createFileTypeMap(Resource mappingLocation, String[] mappings)
/*  52:    */     throws IOException
/*  53:    */   {
/*  54:142 */     MimetypesFileTypeMap fileTypeMap = null;
/*  55:143 */     if (mappingLocation != null)
/*  56:    */     {
/*  57:144 */       InputStream is = mappingLocation.getInputStream();
/*  58:    */       try
/*  59:    */       {
/*  60:146 */         fileTypeMap = new MimetypesFileTypeMap(is);
/*  61:    */       }
/*  62:    */       finally
/*  63:    */       {
/*  64:149 */         is.close();
/*  65:    */       }
/*  66:    */     }
/*  67:    */     else
/*  68:    */     {
/*  69:153 */       fileTypeMap = new MimetypesFileTypeMap();
/*  70:    */     }
/*  71:155 */     if (mappings != null) {
/*  72:156 */       for (String mapping : mappings) {
/*  73:157 */         fileTypeMap.addMimeTypes(mapping);
/*  74:    */       }
/*  75:    */     }
/*  76:160 */     return fileTypeMap;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public String getContentType(File file)
/*  80:    */   {
/*  81:170 */     return getFileTypeMap().getContentType(file);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public String getContentType(String fileName)
/*  85:    */   {
/*  86:179 */     return getFileTypeMap().getContentType(fileName);
/*  87:    */   }
/*  88:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.mail.javamail.ConfigurableMimeFileTypeMap
 * JD-Core Version:    0.7.0.1
 */