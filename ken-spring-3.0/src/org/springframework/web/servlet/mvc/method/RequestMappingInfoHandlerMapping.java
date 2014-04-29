/*   1:    */ package org.springframework.web.servlet.mvc.method;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Comparator;
/*   5:    */ import java.util.HashSet;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Set;
/*   9:    */ import javax.servlet.ServletException;
/*  10:    */ import javax.servlet.http.HttpServletRequest;
/*  11:    */ import org.springframework.http.MediaType;
/*  12:    */ import org.springframework.util.PathMatcher;
/*  13:    */ import org.springframework.util.StringUtils;
/*  14:    */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*  15:    */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*  16:    */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*  17:    */ import org.springframework.web.bind.annotation.RequestMethod;
/*  18:    */ import org.springframework.web.method.HandlerMethod;
/*  19:    */ import org.springframework.web.servlet.HandlerMapping;
/*  20:    */ import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
/*  21:    */ import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
/*  22:    */ import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
/*  23:    */ import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
/*  24:    */ import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
/*  25:    */ 
/*  26:    */ public abstract class RequestMappingInfoHandlerMapping
/*  27:    */   extends AbstractHandlerMethodMapping<RequestMappingInfo>
/*  28:    */ {
/*  29:    */   protected Set<String> getMappingPathPatterns(RequestMappingInfo info)
/*  30:    */   {
/*  31: 53 */     return info.getPatternsCondition().getPatterns();
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected RequestMappingInfo getMatchingMapping(RequestMappingInfo info, HttpServletRequest request)
/*  35:    */   {
/*  36: 64 */     return info.getMatchingCondition(request);
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected Comparator<RequestMappingInfo> getMappingComparator(final HttpServletRequest request)
/*  40:    */   {
/*  41: 72 */     new Comparator()
/*  42:    */     {
/*  43:    */       public int compare(RequestMappingInfo info1, RequestMappingInfo info2)
/*  44:    */       {
/*  45: 74 */         return info1.compareTo(info2, request);
/*  46:    */       }
/*  47:    */     };
/*  48:    */   }
/*  49:    */   
/*  50:    */   protected void handleMatch(RequestMappingInfo info, String lookupPath, HttpServletRequest request)
/*  51:    */   {
/*  52: 86 */     super.handleMatch(info, lookupPath, request);
/*  53:    */     
/*  54: 88 */     String pattern = (String)info.getPatternsCondition().getPatterns().iterator().next();
/*  55: 89 */     request.setAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE, pattern);
/*  56:    */     
/*  57: 91 */     Map<String, String> uriTemplateVariables = getPathMatcher().extractUriTemplateVariables(pattern, lookupPath);
/*  58: 92 */     request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, uriTemplateVariables);
/*  59: 94 */     if (!info.getProducesCondition().getProducibleMediaTypes().isEmpty())
/*  60:    */     {
/*  61: 95 */       Set<MediaType> mediaTypes = info.getProducesCondition().getProducibleMediaTypes();
/*  62: 96 */       request.setAttribute(PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypes);
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected HandlerMethod handleNoMatch(Set<RequestMappingInfo> requestMappingInfos, String lookupPath, HttpServletRequest request)
/*  67:    */     throws ServletException
/*  68:    */   {
/*  69:115 */     Set<String> allowedMethods = new HashSet(6);
/*  70:116 */     Set<MediaType> consumableMediaTypes = new HashSet();
/*  71:117 */     Set<MediaType> producibleMediaTypes = new HashSet();
/*  72:118 */     for (RequestMappingInfo info : requestMappingInfos) {
/*  73:119 */       if (info.getPatternsCondition().getMatchingCondition(request) != null)
/*  74:    */       {
/*  75:120 */         if (info.getMethodsCondition().getMatchingCondition(request) == null) {
/*  76:121 */           for (RequestMethod method : info.getMethodsCondition().getMethods()) {
/*  77:122 */             allowedMethods.add(method.name());
/*  78:    */           }
/*  79:    */         }
/*  80:125 */         if (info.getConsumesCondition().getMatchingCondition(request) == null) {
/*  81:126 */           consumableMediaTypes.addAll(info.getConsumesCondition().getConsumableMediaTypes());
/*  82:    */         }
/*  83:128 */         if (info.getProducesCondition().getMatchingCondition(request) == null) {
/*  84:129 */           producibleMediaTypes.addAll(info.getProducesCondition().getProducibleMediaTypes());
/*  85:    */         }
/*  86:    */       }
/*  87:    */     }
/*  88:133 */     if (!allowedMethods.isEmpty()) {
/*  89:134 */       throw new HttpRequestMethodNotSupportedException(request.getMethod(), allowedMethods);
/*  90:    */     }
/*  91:136 */     if (!consumableMediaTypes.isEmpty())
/*  92:    */     {
/*  93:137 */       MediaType contentType = null;
/*  94:138 */       if (StringUtils.hasLength(request.getContentType())) {
/*  95:139 */         contentType = MediaType.parseMediaType(request.getContentType());
/*  96:    */       }
/*  97:141 */       throw new HttpMediaTypeNotSupportedException(contentType, new ArrayList(consumableMediaTypes));
/*  98:    */     }
/*  99:143 */     if (!producibleMediaTypes.isEmpty()) {
/* 100:144 */       throw new HttpMediaTypeNotAcceptableException(new ArrayList(producibleMediaTypes));
/* 101:    */     }
/* 102:147 */     return null;
/* 103:    */   }
/* 104:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping
 * JD-Core Version:    0.7.0.1
 */