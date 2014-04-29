/*  1:   */ package org.springframework.web.servlet.mvc;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ import javax.servlet.http.HttpServletResponse;
/*  5:   */ import org.springframework.validation.BindException;
/*  6:   */ import org.springframework.web.bind.ServletRequestDataBinder;
/*  7:   */ import org.springframework.web.servlet.ModelAndView;
/*  8:   */ 
/*  9:   */ @Deprecated
/* 10:   */ public abstract class AbstractCommandController
/* 11:   */   extends BaseCommandController
/* 12:   */ {
/* 13:   */   public AbstractCommandController() {}
/* 14:   */   
/* 15:   */   public AbstractCommandController(Class commandClass)
/* 16:   */   {
/* 17:66 */     setCommandClass(commandClass);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public AbstractCommandController(Class commandClass, String commandName)
/* 21:   */   {
/* 22:75 */     setCommandClass(commandClass);
/* 23:76 */     setCommandName(commandName);
/* 24:   */   }
/* 25:   */   
/* 26:   */   protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
/* 27:   */     throws Exception
/* 28:   */   {
/* 29:84 */     Object command = getCommand(request);
/* 30:85 */     ServletRequestDataBinder binder = bindAndValidate(request, command);
/* 31:86 */     BindException errors = new BindException(binder.getBindingResult());
/* 32:87 */     return handle(request, response, command, errors);
/* 33:   */   }
/* 34:   */   
/* 35:   */   protected abstract ModelAndView handle(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Object paramObject, BindException paramBindException)
/* 36:   */     throws Exception;
/* 37:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.AbstractCommandController
 * JD-Core Version:    0.7.0.1
 */