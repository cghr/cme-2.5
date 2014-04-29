/*   1:    */ package org.springframework.core.type.filter;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import org.springframework.core.type.ClassMetadata;
/*   5:    */ import org.springframework.core.type.classreading.MetadataReader;
/*   6:    */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*   7:    */ 
/*   8:    */ public abstract class AbstractTypeHierarchyTraversingFilter
/*   9:    */   implements TypeFilter
/*  10:    */ {
/*  11:    */   private final boolean considerInherited;
/*  12:    */   private final boolean considerInterfaces;
/*  13:    */   
/*  14:    */   protected AbstractTypeHierarchyTraversingFilter(boolean considerInherited, boolean considerInterfaces)
/*  15:    */   {
/*  16: 45 */     this.considerInherited = considerInherited;
/*  17: 46 */     this.considerInterfaces = considerInterfaces;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
/*  21:    */     throws IOException
/*  22:    */   {
/*  23: 55 */     if (matchSelf(metadataReader)) {
/*  24: 56 */       return true;
/*  25:    */     }
/*  26: 58 */     ClassMetadata metadata = metadataReader.getClassMetadata();
/*  27: 59 */     if (matchClassName(metadata.getClassName())) {
/*  28: 60 */       return true;
/*  29:    */     }
/*  30: 63 */     if (!this.considerInherited) {
/*  31: 64 */       return false;
/*  32:    */     }
/*  33: 66 */     if (metadata.hasSuperClass())
/*  34:    */     {
/*  35: 68 */       Boolean superClassMatch = matchSuperClass(metadata.getSuperClassName());
/*  36: 69 */       if (superClassMatch != null)
/*  37:    */       {
/*  38: 70 */         if (superClassMatch.booleanValue()) {
/*  39: 71 */           return true;
/*  40:    */         }
/*  41:    */       }
/*  42: 76 */       else if (match(metadata.getSuperClassName(), metadataReaderFactory)) {
/*  43: 77 */         return true;
/*  44:    */       }
/*  45:    */     }
/*  46: 82 */     if (!this.considerInterfaces) {
/*  47: 83 */       return false;
/*  48:    */     }
/*  49: 85 */     for (String ifc : metadata.getInterfaceNames())
/*  50:    */     {
/*  51: 87 */       Boolean interfaceMatch = matchInterface(ifc);
/*  52: 88 */       if (interfaceMatch != null)
/*  53:    */       {
/*  54: 89 */         if (interfaceMatch.booleanValue()) {
/*  55: 90 */           return true;
/*  56:    */         }
/*  57:    */       }
/*  58: 95 */       else if (match(ifc, metadataReaderFactory)) {
/*  59: 96 */         return true;
/*  60:    */       }
/*  61:    */     }
/*  62:101 */     return false;
/*  63:    */   }
/*  64:    */   
/*  65:    */   private boolean match(String className, MetadataReaderFactory metadataReaderFactory)
/*  66:    */     throws IOException
/*  67:    */   {
/*  68:105 */     return match(metadataReaderFactory.getMetadataReader(className), metadataReaderFactory);
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected boolean matchSelf(MetadataReader metadataReader)
/*  72:    */   {
/*  73:114 */     return false;
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected boolean matchClassName(String className)
/*  77:    */   {
/*  78:121 */     return false;
/*  79:    */   }
/*  80:    */   
/*  81:    */   protected Boolean matchSuperClass(String superClassName)
/*  82:    */   {
/*  83:128 */     return null;
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected Boolean matchInterface(String interfaceNames)
/*  87:    */   {
/*  88:135 */     return null;
/*  89:    */   }
/*  90:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter
 * JD-Core Version:    0.7.0.1
 */