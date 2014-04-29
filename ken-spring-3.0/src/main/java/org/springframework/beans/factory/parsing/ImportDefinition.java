/*  1:   */ package org.springframework.beans.factory.parsing;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.BeanMetadataElement;
/*  4:   */ import org.springframework.core.io.Resource;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ 
/*  7:   */ public class ImportDefinition
/*  8:   */   implements BeanMetadataElement
/*  9:   */ {
/* 10:   */   private final String importedResource;
/* 11:   */   private final Resource[] actualResources;
/* 12:   */   private final Object source;
/* 13:   */   
/* 14:   */   public ImportDefinition(String importedResource)
/* 15:   */   {
/* 16:44 */     this(importedResource, null, null);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public ImportDefinition(String importedResource, Object source)
/* 20:   */   {
/* 21:53 */     this(importedResource, null, source);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public ImportDefinition(String importedResource, Resource[] actualResources, Object source)
/* 25:   */   {
/* 26:62 */     Assert.notNull(importedResource, "Imported resource must not be null");
/* 27:63 */     this.importedResource = importedResource;
/* 28:64 */     this.actualResources = actualResources;
/* 29:65 */     this.source = source;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public final String getImportedResource()
/* 33:   */   {
/* 34:73 */     return this.importedResource;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public final Resource[] getActualResources()
/* 38:   */   {
/* 39:77 */     return this.actualResources;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public final Object getSource()
/* 43:   */   {
/* 44:81 */     return this.source;
/* 45:   */   }
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.ImportDefinition
 * JD-Core Version:    0.7.0.1
 */