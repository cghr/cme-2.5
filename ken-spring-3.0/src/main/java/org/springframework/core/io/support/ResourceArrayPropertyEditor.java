/*   1:    */ package org.springframework.core.io.support;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditorSupport;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Arrays;
/*   7:    */ import java.util.Collection;
/*   8:    */ import java.util.List;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.apache.commons.logging.LogFactory;
/*  11:    */ import org.springframework.core.env.PropertyResolver;
/*  12:    */ import org.springframework.core.env.StandardEnvironment;
/*  13:    */ import org.springframework.core.io.Resource;
/*  14:    */ 
/*  15:    */ public class ResourceArrayPropertyEditor
/*  16:    */   extends PropertyEditorSupport
/*  17:    */ {
/*  18: 56 */   private static final Log logger = LogFactory.getLog(ResourceArrayPropertyEditor.class);
/*  19:    */   private final PropertyResolver propertyResolver;
/*  20:    */   private final ResourcePatternResolver resourcePatternResolver;
/*  21:    */   private final boolean ignoreUnresolvablePlaceholders;
/*  22:    */   
/*  23:    */   public ResourceArrayPropertyEditor()
/*  24:    */   {
/*  25: 72 */     this(new PathMatchingResourcePatternResolver(), new StandardEnvironment(), true);
/*  26:    */   }
/*  27:    */   
/*  28:    */   @Deprecated
/*  29:    */   public ResourceArrayPropertyEditor(ResourcePatternResolver resourcePatternResolver)
/*  30:    */   {
/*  31: 83 */     this(resourcePatternResolver, new StandardEnvironment(), true);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public ResourceArrayPropertyEditor(ResourcePatternResolver resourcePatternResolver, PropertyResolver propertyResolver)
/*  35:    */   {
/*  36: 93 */     this(resourcePatternResolver, propertyResolver, true);
/*  37:    */   }
/*  38:    */   
/*  39:    */   @Deprecated
/*  40:    */   public ResourceArrayPropertyEditor(ResourcePatternResolver resourcePatternResolver, boolean ignoreUnresolvablePlaceholders)
/*  41:    */   {
/*  42:106 */     this(resourcePatternResolver, new StandardEnvironment(), ignoreUnresolvablePlaceholders);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public ResourceArrayPropertyEditor(ResourcePatternResolver resourcePatternResolver, PropertyResolver propertyResolver, boolean ignoreUnresolvablePlaceholders)
/*  46:    */   {
/*  47:119 */     this.resourcePatternResolver = resourcePatternResolver;
/*  48:120 */     this.propertyResolver = propertyResolver;
/*  49:121 */     this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setAsText(String text)
/*  53:    */   {
/*  54:130 */     String pattern = resolvePath(text).trim();
/*  55:    */     try
/*  56:    */     {
/*  57:132 */       setValue(this.resourcePatternResolver.getResources(pattern));
/*  58:    */     }
/*  59:    */     catch (IOException ex)
/*  60:    */     {
/*  61:135 */       throw new IllegalArgumentException(
/*  62:136 */         "Could not resolve resource location pattern [" + pattern + "]: " + ex.getMessage());
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void setValue(Object value)
/*  67:    */     throws IllegalArgumentException
/*  68:    */   {
/*  69:146 */     if (((value instanceof Collection)) || (((value instanceof Object[])) && (!(value instanceof Resource[]))))
/*  70:    */     {
/*  71:147 */       Collection<?> input = (value instanceof Collection) ? (Collection)value : (Collection)Arrays.asList((Object[])value);
/*  72:148 */       List<Resource> merged = new ArrayList();
/*  73:149 */       for (Object element : input) {
/*  74:150 */         if ((element instanceof String))
/*  75:    */         {
/*  76:153 */           String pattern = resolvePath((String)element).trim();
/*  77:    */           try
/*  78:    */           {
/*  79:155 */             Resource[] resources = this.resourcePatternResolver.getResources(pattern);
/*  80:156 */             for (Resource resource : resources) {
/*  81:157 */               if (!merged.contains(resource)) {
/*  82:158 */                 merged.add(resource);
/*  83:    */               }
/*  84:    */             }
/*  85:    */           }
/*  86:    */           catch (IOException ex)
/*  87:    */           {
/*  88:164 */             if (!logger.isDebugEnabled()) {
/*  89:    */               continue;
/*  90:    */             }
/*  91:    */           }
/*  92:165 */           logger.debug("Could not retrieve resources for pattern '" + pattern + "'", ex);
/*  93:    */         }
/*  94:169 */         else if ((element instanceof Resource))
/*  95:    */         {
/*  96:171 */           Resource resource = (Resource)element;
/*  97:172 */           if (!merged.contains(resource)) {
/*  98:173 */             merged.add(resource);
/*  99:    */           }
/* 100:    */         }
/* 101:    */         else
/* 102:    */         {
/* 103:177 */           throw new IllegalArgumentException("Cannot convert element [" + element + "] to [" + 
/* 104:178 */             Resource.class.getName() + "]: only location String and Resource object supported");
/* 105:    */         }
/* 106:    */       }
/* 107:181 */       super.setValue(merged.toArray(new Resource[merged.size()]));
/* 108:    */     }
/* 109:    */     else
/* 110:    */     {
/* 111:187 */       super.setValue(value);
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   protected String resolvePath(String path)
/* 116:    */   {
/* 117:200 */     return this.ignoreUnresolvablePlaceholders ? 
/* 118:201 */       this.propertyResolver.resolvePlaceholders(path) : 
/* 119:202 */       this.propertyResolver.resolveRequiredPlaceholders(path);
/* 120:    */   }
/* 121:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.support.ResourceArrayPropertyEditor
 * JD-Core Version:    0.7.0.1
 */