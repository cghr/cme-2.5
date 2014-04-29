/*   1:    */ package org.springframework.web.method.annotation;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.HashSet;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Set;
/*  10:    */ import org.springframework.core.annotation.AnnotationUtils;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.web.bind.annotation.SessionAttributes;
/*  13:    */ import org.springframework.web.bind.support.SessionAttributeStore;
/*  14:    */ import org.springframework.web.context.request.WebRequest;
/*  15:    */ 
/*  16:    */ public class SessionAttributesHandler
/*  17:    */ {
/*  18: 50 */   private final Set<String> attributeNames = new HashSet();
/*  19: 52 */   private final Set<Class<?>> attributeTypes = new HashSet();
/*  20: 54 */   private final Set<String> resolvedAttributeNames = Collections.synchronizedSet(new HashSet(4));
/*  21:    */   private final SessionAttributeStore sessionAttributeStore;
/*  22:    */   
/*  23:    */   public SessionAttributesHandler(Class<?> handlerType, SessionAttributeStore sessionAttributeStore)
/*  24:    */   {
/*  25: 67 */     Assert.notNull(sessionAttributeStore, "SessionAttributeStore may not be null.");
/*  26: 68 */     this.sessionAttributeStore = sessionAttributeStore;
/*  27:    */     
/*  28: 70 */     SessionAttributes annotation = (SessionAttributes)AnnotationUtils.findAnnotation(handlerType, SessionAttributes.class);
/*  29: 71 */     if (annotation != null)
/*  30:    */     {
/*  31: 72 */       this.attributeNames.addAll((Collection)Arrays.asList(annotation.value()));
/*  32: 73 */       this.attributeTypes.addAll((Collection)Arrays.asList(annotation.types()));
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   public boolean hasSessionAttributes()
/*  37:    */   {
/*  38: 82 */     return (this.attributeNames.size() > 0) || (this.attributeTypes.size() > 0);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public boolean isHandlerSessionAttribute(String attributeName, Class<?> attributeType)
/*  42:    */   {
/*  43: 99 */     Assert.notNull(attributeName, "Attribute name must not be null");
/*  44:100 */     if ((this.attributeNames.contains(attributeName)) || (this.attributeTypes.contains(attributeType)))
/*  45:    */     {
/*  46:101 */       this.resolvedAttributeNames.add(attributeName);
/*  47:102 */       return true;
/*  48:    */     }
/*  49:105 */     return false;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void storeAttributes(WebRequest request, Map<String, ?> attributes)
/*  53:    */   {
/*  54:116 */     for (String name : attributes.keySet())
/*  55:    */     {
/*  56:117 */       Object value = attributes.get(name);
/*  57:118 */       Class<?> attrType = value != null ? value.getClass() : null;
/*  58:120 */       if (isHandlerSessionAttribute(name, attrType)) {
/*  59:121 */         this.sessionAttributeStore.storeAttribute(request, name, value);
/*  60:    */       }
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public Map<String, Object> retrieveAttributes(WebRequest request)
/*  65:    */   {
/*  66:133 */     Map<String, Object> attributes = new HashMap();
/*  67:134 */     for (String name : this.resolvedAttributeNames)
/*  68:    */     {
/*  69:135 */       Object value = this.sessionAttributeStore.retrieveAttribute(request, name);
/*  70:136 */       if (value != null) {
/*  71:137 */         attributes.put(name, value);
/*  72:    */       }
/*  73:    */     }
/*  74:140 */     return attributes;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void cleanupAttributes(WebRequest request)
/*  78:    */   {
/*  79:149 */     for (String attributeName : this.resolvedAttributeNames) {
/*  80:150 */       this.sessionAttributeStore.cleanupAttribute(request, attributeName);
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   Object retrieveAttribute(WebRequest request, String attributeName)
/*  85:    */   {
/*  86:161 */     return this.sessionAttributeStore.retrieveAttribute(request, attributeName);
/*  87:    */   }
/*  88:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.annotation.SessionAttributesHandler
 * JD-Core Version:    0.7.0.1
 */