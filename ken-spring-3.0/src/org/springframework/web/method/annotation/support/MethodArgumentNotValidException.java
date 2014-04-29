/*  1:   */ package org.springframework.web.method.annotation.support;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Method;
/*  4:   */ import org.springframework.core.MethodParameter;
/*  5:   */ import org.springframework.validation.BindingResult;
/*  6:   */ import org.springframework.validation.ObjectError;
/*  7:   */ 
/*  8:   */ public class MethodArgumentNotValidException
/*  9:   */   extends Exception
/* 10:   */ {
/* 11:   */   private final MethodParameter parameter;
/* 12:   */   private final BindingResult bindingResult;
/* 13:   */   
/* 14:   */   public MethodArgumentNotValidException(MethodParameter parameter, BindingResult bindingResult)
/* 15:   */   {
/* 16:42 */     this.parameter = parameter;
/* 17:43 */     this.bindingResult = bindingResult;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public MethodParameter getParameter()
/* 21:   */   {
/* 22:50 */     return this.parameter;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public BindingResult getBindingResult()
/* 26:   */   {
/* 27:57 */     return this.bindingResult;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String getMessage()
/* 31:   */   {
/* 32:62 */     StringBuilder sb = new StringBuilder("Validation failed for argument at index [")
/* 33:63 */       .append(this.parameter.getParameterIndex()).append("] in method: ")
/* 34:64 */       .append(this.parameter.getMethod().toGenericString())
/* 35:65 */       .append(", with ").append(this.bindingResult.getErrorCount()).append(" error(s): ");
/* 36:66 */     for (ObjectError error : this.bindingResult.getAllErrors()) {
/* 37:67 */       sb.append("[").append(error).append("] ");
/* 38:   */     }
/* 39:69 */     return sb.toString();
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.annotation.support.MethodArgumentNotValidException
 * JD-Core Version:    0.7.0.1
 */