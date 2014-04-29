/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import org.springframework.core.convert.ConversionService;
/*  5:   */ import org.springframework.core.convert.TypeDescriptor;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ 
/*  8:   */ public class ConvertingPropertyEditorAdapter
/*  9:   */   extends PropertyEditorSupport
/* 10:   */ {
/* 11:   */   private final ConversionService conversionService;
/* 12:   */   private final TypeDescriptor targetDescriptor;
/* 13:   */   private final boolean canConvertToString;
/* 14:   */   
/* 15:   */   public ConvertingPropertyEditorAdapter(ConversionService conversionService, TypeDescriptor targetDescriptor)
/* 16:   */   {
/* 17:49 */     Assert.notNull(conversionService, "ConversionService must not be null");
/* 18:50 */     Assert.notNull(targetDescriptor, "TypeDescriptor must not be null");
/* 19:51 */     this.conversionService = conversionService;
/* 20:52 */     this.targetDescriptor = targetDescriptor;
/* 21:53 */     this.canConvertToString = conversionService.canConvert(this.targetDescriptor, TypeDescriptor.valueOf(String.class));
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void setAsText(String text)
/* 25:   */     throws IllegalArgumentException
/* 26:   */   {
/* 27:59 */     setValue(this.conversionService.convert(text, TypeDescriptor.valueOf(String.class), this.targetDescriptor));
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String getAsText()
/* 31:   */   {
/* 32:64 */     if (this.canConvertToString) {
/* 33:65 */       return (String)this.conversionService.convert(getValue(), this.targetDescriptor, TypeDescriptor.valueOf(String.class));
/* 34:   */     }
/* 35:68 */     return null;
/* 36:   */   }
/* 37:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.ConvertingPropertyEditorAdapter
 * JD-Core Version:    0.7.0.1
 */