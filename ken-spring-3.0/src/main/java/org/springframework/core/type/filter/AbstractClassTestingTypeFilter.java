/*  1:   */ package org.springframework.core.type.filter;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import org.springframework.core.type.ClassMetadata;
/*  5:   */ import org.springframework.core.type.classreading.MetadataReader;
/*  6:   */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*  7:   */ 
/*  8:   */ public abstract class AbstractClassTestingTypeFilter
/*  9:   */   implements TypeFilter
/* 10:   */ {
/* 11:   */   public final boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
/* 12:   */     throws IOException
/* 13:   */   {
/* 14:41 */     return match(metadataReader.getClassMetadata());
/* 15:   */   }
/* 16:   */   
/* 17:   */   protected abstract boolean match(ClassMetadata paramClassMetadata);
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.filter.AbstractClassTestingTypeFilter
 * JD-Core Version:    0.7.0.1
 */