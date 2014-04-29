/*   1:    */ package org.springframework.web.servlet.tags;
/*   2:    */ 
/*   3:    */ import javax.servlet.jsp.JspException;
/*   4:    */ import javax.servlet.jsp.JspTagException;
/*   5:    */ import javax.servlet.jsp.PageContext;
/*   6:    */ import javax.servlet.jsp.tagext.TagSupport;
/*   7:    */ import javax.servlet.jsp.tagext.TryCatchFinally;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.commons.logging.LogFactory;
/*  10:    */ import org.springframework.web.servlet.support.JspAwareRequestContext;
/*  11:    */ import org.springframework.web.servlet.support.RequestContext;
/*  12:    */ 
/*  13:    */ public abstract class RequestContextAwareTag
/*  14:    */   extends TagSupport
/*  15:    */   implements TryCatchFinally
/*  16:    */ {
/*  17:    */   public static final String REQUEST_CONTEXT_PAGE_ATTRIBUTE = "org.springframework.web.servlet.tags.REQUEST_CONTEXT";
/*  18: 59 */   protected final Log logger = LogFactory.getLog(getClass());
/*  19:    */   private RequestContext requestContext;
/*  20:    */   
/*  21:    */   public final int doStartTag()
/*  22:    */     throws JspException
/*  23:    */   {
/*  24:    */     try
/*  25:    */     {
/*  26: 74 */       this.requestContext = ((RequestContext)this.pageContext.getAttribute("org.springframework.web.servlet.tags.REQUEST_CONTEXT"));
/*  27: 75 */       if (this.requestContext == null)
/*  28:    */       {
/*  29: 76 */         this.requestContext = new JspAwareRequestContext(this.pageContext);
/*  30: 77 */         this.pageContext.setAttribute("org.springframework.web.servlet.tags.REQUEST_CONTEXT", this.requestContext);
/*  31:    */       }
/*  32: 79 */       return doStartTagInternal();
/*  33:    */     }
/*  34:    */     catch (JspException ex)
/*  35:    */     {
/*  36: 82 */       this.logger.error(ex.getMessage(), ex);
/*  37: 83 */       throw ex;
/*  38:    */     }
/*  39:    */     catch (RuntimeException ex)
/*  40:    */     {
/*  41: 86 */       this.logger.error(ex.getMessage(), ex);
/*  42: 87 */       throw ex;
/*  43:    */     }
/*  44:    */     catch (Exception ex)
/*  45:    */     {
/*  46: 90 */       this.logger.error(ex.getMessage(), ex);
/*  47: 91 */       throw new JspTagException(ex.getMessage());
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected final RequestContext getRequestContext()
/*  52:    */   {
/*  53: 99 */     return this.requestContext;
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected abstract int doStartTagInternal()
/*  57:    */     throws Exception;
/*  58:    */   
/*  59:    */   public void doCatch(Throwable throwable)
/*  60:    */     throws Throwable
/*  61:    */   {
/*  62:113 */     throw throwable;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void doFinally()
/*  66:    */   {
/*  67:117 */     this.requestContext = null;
/*  68:    */   }
/*  69:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.RequestContextAwareTag
 * JD-Core Version:    0.7.0.1
 */