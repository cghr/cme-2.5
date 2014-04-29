/*   1:    */ package org.springframework.web.bind.support;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.PropertyEditorRegistrar;
/*   4:    */ import org.springframework.core.convert.ConversionService;
/*   5:    */ import org.springframework.validation.BindingErrorProcessor;
/*   6:    */ import org.springframework.validation.MessageCodesResolver;
/*   7:    */ import org.springframework.validation.Validator;
/*   8:    */ import org.springframework.web.bind.WebDataBinder;
/*   9:    */ import org.springframework.web.context.request.WebRequest;
/*  10:    */ 
/*  11:    */ public class ConfigurableWebBindingInitializer
/*  12:    */   implements WebBindingInitializer
/*  13:    */ {
/*  14: 43 */   private boolean autoGrowNestedPaths = true;
/*  15: 45 */   private boolean directFieldAccess = false;
/*  16:    */   private MessageCodesResolver messageCodesResolver;
/*  17:    */   private BindingErrorProcessor bindingErrorProcessor;
/*  18:    */   private Validator validator;
/*  19:    */   private ConversionService conversionService;
/*  20:    */   private PropertyEditorRegistrar[] propertyEditorRegistrars;
/*  21:    */   
/*  22:    */   public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths)
/*  23:    */   {
/*  24: 69 */     this.autoGrowNestedPaths = autoGrowNestedPaths;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public boolean isAutoGrowNestedPaths()
/*  28:    */   {
/*  29: 76 */     return this.autoGrowNestedPaths;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public final void setDirectFieldAccess(boolean directFieldAccess)
/*  33:    */   {
/*  34: 87 */     this.directFieldAccess = directFieldAccess;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public boolean isDirectFieldAccess()
/*  38:    */   {
/*  39: 94 */     return this.directFieldAccess;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public final void setMessageCodesResolver(MessageCodesResolver messageCodesResolver)
/*  43:    */   {
/*  44:105 */     this.messageCodesResolver = messageCodesResolver;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public final MessageCodesResolver getMessageCodesResolver()
/*  48:    */   {
/*  49:112 */     return this.messageCodesResolver;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public final void setBindingErrorProcessor(BindingErrorProcessor bindingErrorProcessor)
/*  53:    */   {
/*  54:123 */     this.bindingErrorProcessor = bindingErrorProcessor;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public final BindingErrorProcessor getBindingErrorProcessor()
/*  58:    */   {
/*  59:130 */     return this.bindingErrorProcessor;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public final void setValidator(Validator validator)
/*  63:    */   {
/*  64:137 */     this.validator = validator;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public final Validator getValidator()
/*  68:    */   {
/*  69:144 */     return this.validator;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public final void setConversionService(ConversionService conversionService)
/*  73:    */   {
/*  74:152 */     this.conversionService = conversionService;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public final ConversionService getConversionService()
/*  78:    */   {
/*  79:159 */     return this.conversionService;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public final void setPropertyEditorRegistrar(PropertyEditorRegistrar propertyEditorRegistrar)
/*  83:    */   {
/*  84:166 */     this.propertyEditorRegistrars = new PropertyEditorRegistrar[] { propertyEditorRegistrar };
/*  85:    */   }
/*  86:    */   
/*  87:    */   public final void setPropertyEditorRegistrars(PropertyEditorRegistrar[] propertyEditorRegistrars)
/*  88:    */   {
/*  89:173 */     this.propertyEditorRegistrars = propertyEditorRegistrars;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public final PropertyEditorRegistrar[] getPropertyEditorRegistrars()
/*  93:    */   {
/*  94:180 */     return this.propertyEditorRegistrars;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void initBinder(WebDataBinder binder, WebRequest request)
/*  98:    */   {
/*  99:185 */     binder.setAutoGrowNestedPaths(this.autoGrowNestedPaths);
/* 100:186 */     if (this.directFieldAccess) {
/* 101:187 */       binder.initDirectFieldAccess();
/* 102:    */     }
/* 103:189 */     if (this.messageCodesResolver != null) {
/* 104:190 */       binder.setMessageCodesResolver(this.messageCodesResolver);
/* 105:    */     }
/* 106:192 */     if (this.bindingErrorProcessor != null) {
/* 107:193 */       binder.setBindingErrorProcessor(this.bindingErrorProcessor);
/* 108:    */     }
/* 109:195 */     if ((this.validator != null) && (binder.getTarget() != null) && 
/* 110:196 */       (this.validator.supports(binder.getTarget().getClass()))) {
/* 111:197 */       binder.setValidator(this.validator);
/* 112:    */     }
/* 113:199 */     if (this.conversionService != null) {
/* 114:200 */       binder.setConversionService(this.conversionService);
/* 115:    */     }
/* 116:202 */     if (this.propertyEditorRegistrars != null) {
/* 117:203 */       for (PropertyEditorRegistrar propertyEditorRegistrar : this.propertyEditorRegistrars) {
/* 118:204 */         propertyEditorRegistrar.registerCustomEditors(binder);
/* 119:    */       }
/* 120:    */     }
/* 121:    */   }
/* 122:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.support.ConfigurableWebBindingInitializer
 * JD-Core Version:    0.7.0.1
 */