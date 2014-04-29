/*   1:    */ package org.springframework.web.servlet;
/*   2:    */ 
/*   3:    */ import javax.servlet.http.HttpServletRequest;
/*   4:    */ 
/*   5:    */ public abstract interface HandlerMapping
/*   6:    */ {
/*   7: 65 */   public static final String PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE = HandlerMapping.class.getName() + ".pathWithinHandlerMapping";
/*   8: 75 */   public static final String BEST_MATCHING_PATTERN_ATTRIBUTE = HandlerMapping.class.getName() + ".bestMatchingPattern";
/*   9: 83 */   public static final String INTROSPECT_TYPE_LEVEL_MAPPING = HandlerMapping.class.getName() + ".introspectTypeLevelMapping";
/*  10: 93 */   public static final String URI_TEMPLATE_VARIABLES_ATTRIBUTE = HandlerMapping.class.getName() + ".uriTemplateVariables";
/*  11:101 */   public static final String PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE = HandlerMapping.class.getName() + ".producibleMediaTypes";
/*  12:    */   
/*  13:    */   public abstract HandlerExecutionChain getHandler(HttpServletRequest paramHttpServletRequest)
/*  14:    */     throws Exception;
/*  15:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.HandlerMapping
 * JD-Core Version:    0.7.0.1
 */