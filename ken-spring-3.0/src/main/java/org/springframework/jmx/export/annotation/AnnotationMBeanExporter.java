/*  1:   */ package org.springframework.jmx.export.annotation;
/*  2:   */ 
/*  3:   */ import org.springframework.jmx.export.MBeanExporter;
/*  4:   */ import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;
/*  5:   */ import org.springframework.jmx.export.naming.MetadataNamingStrategy;
/*  6:   */ 
/*  7:   */ public class AnnotationMBeanExporter
/*  8:   */   extends MBeanExporter
/*  9:   */ {
/* 10:38 */   private final AnnotationJmxAttributeSource annotationSource = new AnnotationJmxAttributeSource();
/* 11:41 */   private final MetadataNamingStrategy metadataNamingStrategy = new MetadataNamingStrategy(this.annotationSource);
/* 12:44 */   private final MetadataMBeanInfoAssembler metadataAssembler = new MetadataMBeanInfoAssembler(this.annotationSource);
/* 13:   */   
/* 14:   */   public AnnotationMBeanExporter()
/* 15:   */   {
/* 16:48 */     setNamingStrategy(this.metadataNamingStrategy);
/* 17:49 */     setAssembler(this.metadataAssembler);
/* 18:50 */     setAutodetectMode(3);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void setDefaultDomain(String defaultDomain)
/* 22:   */   {
/* 23:63 */     this.metadataNamingStrategy.setDefaultDomain(defaultDomain);
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.annotation.AnnotationMBeanExporter
 * JD-Core Version:    0.7.0.1
 */