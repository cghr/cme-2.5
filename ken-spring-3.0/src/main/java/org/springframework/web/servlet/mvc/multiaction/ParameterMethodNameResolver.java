/*   1:    */ package org.springframework.web.servlet.mvc.multiaction;
/*   2:    */ 
/*   3:    */ import java.util.Properties;
/*   4:    */ import javax.servlet.http.HttpServletRequest;
/*   5:    */ import org.apache.commons.logging.Log;
/*   6:    */ import org.apache.commons.logging.LogFactory;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ import org.springframework.util.StringUtils;
/*   9:    */ import org.springframework.web.util.WebUtils;
/*  10:    */ 
/*  11:    */ public class ParameterMethodNameResolver
/*  12:    */   implements MethodNameResolver
/*  13:    */ {
/*  14:    */   public static final String DEFAULT_PARAM_NAME = "action";
/*  15: 93 */   protected final Log logger = LogFactory.getLog(getClass());
/*  16: 95 */   private String paramName = "action";
/*  17:    */   private String[] methodParamNames;
/*  18:    */   private Properties logicalMappings;
/*  19:    */   private String defaultMethodName;
/*  20:    */   
/*  21:    */   public void setParamName(String paramName)
/*  22:    */   {
/*  23:113 */     if (paramName != null) {
/*  24:114 */       Assert.hasText(paramName, "'paramName' must not be empty");
/*  25:    */     }
/*  26:116 */     this.paramName = paramName;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setMethodParamNames(String[] methodParamNames)
/*  30:    */   {
/*  31:128 */     this.methodParamNames = methodParamNames;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setLogicalMappings(Properties logicalMappings)
/*  35:    */   {
/*  36:145 */     this.logicalMappings = logicalMappings;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setDefaultMethodName(String defaultMethodName)
/*  40:    */   {
/*  41:153 */     if (defaultMethodName != null) {
/*  42:154 */       Assert.hasText(defaultMethodName, "'defaultMethodName' must not be empty");
/*  43:    */     }
/*  44:156 */     this.defaultMethodName = defaultMethodName;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public String getHandlerMethodName(HttpServletRequest request)
/*  48:    */     throws NoSuchRequestHandlingMethodException
/*  49:    */   {
/*  50:161 */     String methodName = null;
/*  51:165 */     if (this.methodParamNames != null) {
/*  52:166 */       for (String candidate : this.methodParamNames) {
/*  53:167 */         if (WebUtils.hasSubmitParameter(request, candidate))
/*  54:    */         {
/*  55:168 */           methodName = candidate;
/*  56:169 */           if (!this.logger.isDebugEnabled()) {
/*  57:    */             break;
/*  58:    */           }
/*  59:170 */           this.logger.debug("Determined handler method '" + methodName + 
/*  60:171 */             "' based on existence of explicit request parameter of same name");
/*  61:    */           
/*  62:173 */           break;
/*  63:    */         }
/*  64:    */       }
/*  65:    */     }
/*  66:179 */     if ((methodName == null) && (this.paramName != null))
/*  67:    */     {
/*  68:180 */       methodName = request.getParameter(this.paramName);
/*  69:181 */       if ((methodName != null) && 
/*  70:182 */         (this.logger.isDebugEnabled())) {
/*  71:183 */         this.logger.debug("Determined handler method '" + methodName + 
/*  72:184 */           "' based on value of request parameter '" + this.paramName + "'");
/*  73:    */       }
/*  74:    */     }
/*  75:189 */     if ((methodName != null) && (this.logicalMappings != null))
/*  76:    */     {
/*  77:191 */       String originalName = methodName;
/*  78:192 */       methodName = this.logicalMappings.getProperty(methodName, methodName);
/*  79:193 */       if (this.logger.isDebugEnabled()) {
/*  80:194 */         this.logger.debug("Resolved method name '" + originalName + "' to handler method '" + methodName + "'");
/*  81:    */       }
/*  82:    */     }
/*  83:198 */     if ((methodName != null) && (!StringUtils.hasText(methodName)))
/*  84:    */     {
/*  85:199 */       if (this.logger.isDebugEnabled()) {
/*  86:200 */         this.logger.debug("Method name '" + methodName + "' is empty: treating it as no method name found");
/*  87:    */       }
/*  88:202 */       methodName = null;
/*  89:    */     }
/*  90:205 */     if (methodName == null) {
/*  91:206 */       if (this.defaultMethodName != null)
/*  92:    */       {
/*  93:208 */         methodName = this.defaultMethodName;
/*  94:209 */         if (this.logger.isDebugEnabled()) {
/*  95:210 */           this.logger.debug("Falling back to default handler method '" + this.defaultMethodName + "'");
/*  96:    */         }
/*  97:    */       }
/*  98:    */       else
/*  99:    */       {
/* 100:215 */         throw new NoSuchRequestHandlingMethodException(request);
/* 101:    */       }
/* 102:    */     }
/* 103:219 */     return methodName;
/* 104:    */   }
/* 105:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver
 * JD-Core Version:    0.7.0.1
 */