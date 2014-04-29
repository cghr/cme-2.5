/*   1:    */ package org.springframework.scripting.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.InputStreamReader;
/*   6:    */ import java.io.Reader;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.core.io.Resource;
/*  10:    */ import org.springframework.scripting.ScriptSource;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.util.FileCopyUtils;
/*  13:    */ import org.springframework.util.StringUtils;
/*  14:    */ 
/*  15:    */ public class ResourceScriptSource
/*  16:    */   implements ScriptSource
/*  17:    */ {
/*  18: 50 */   protected final Log logger = LogFactory.getLog(getClass());
/*  19:    */   private final Resource resource;
/*  20: 54 */   private long lastModified = -1L;
/*  21: 56 */   private final Object lastModifiedMonitor = new Object();
/*  22: 58 */   private String encoding = "UTF-8";
/*  23:    */   
/*  24:    */   public ResourceScriptSource(Resource resource)
/*  25:    */   {
/*  26: 65 */     Assert.notNull(resource, "Resource must not be null");
/*  27: 66 */     this.resource = resource;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public final Resource getResource()
/*  31:    */   {
/*  32: 74 */     return this.resource;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public String getScriptAsString()
/*  36:    */     throws IOException
/*  37:    */   {
/*  38: 78 */     synchronized (this.lastModifiedMonitor)
/*  39:    */     {
/*  40: 79 */       this.lastModified = retrieveLastModifiedTime();
/*  41:    */     }
/*  42: 82 */     InputStream stream = this.resource.getInputStream();
/*  43: 83 */     Reader reader = StringUtils.hasText(this.encoding) ? new InputStreamReader(stream, this.encoding) : 
/*  44: 84 */       new InputStreamReader(stream);
/*  45:    */     
/*  46: 86 */     return FileCopyUtils.copyToString(reader);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean isModified()
/*  50:    */   {
/*  51: 90 */     synchronized (this.lastModifiedMonitor)
/*  52:    */     {
/*  53: 91 */       return (this.lastModified < 0L) || (retrieveLastModifiedTime() > this.lastModified);
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected long retrieveLastModifiedTime()
/*  58:    */   {
/*  59:    */     try
/*  60:    */     {
/*  61:101 */       return getResource().lastModified();
/*  62:    */     }
/*  63:    */     catch (IOException ex)
/*  64:    */     {
/*  65:103 */       if (this.logger.isDebugEnabled()) {
/*  66:104 */         this.logger.debug(getResource() + " could not be resolved in the file system - " + 
/*  67:105 */           "current timestamp not available for script modification check", ex);
/*  68:    */       }
/*  69:    */     }
/*  70:107 */     return 0L;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public String suggestedClassName()
/*  74:    */   {
/*  75:112 */     return StringUtils.stripFilenameExtension(getResource().getFilename());
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setEncoding(String encoding)
/*  79:    */   {
/*  80:122 */     this.encoding = encoding;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public String toString()
/*  84:    */   {
/*  85:127 */     return this.resource.toString();
/*  86:    */   }
/*  87:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scripting.support.ResourceScriptSource
 * JD-Core Version:    0.7.0.1
 */