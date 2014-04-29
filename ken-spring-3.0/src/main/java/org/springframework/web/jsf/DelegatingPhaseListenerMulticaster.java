/*   1:    */ package org.springframework.web.jsf;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.faces.context.FacesContext;
/*   6:    */ import javax.faces.event.PhaseEvent;
/*   7:    */ import javax.faces.event.PhaseId;
/*   8:    */ import javax.faces.event.PhaseListener;
/*   9:    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*  10:    */ import org.springframework.beans.factory.ListableBeanFactory;
/*  11:    */ import org.springframework.web.context.WebApplicationContext;
/*  12:    */ 
/*  13:    */ public class DelegatingPhaseListenerMulticaster
/*  14:    */   implements PhaseListener
/*  15:    */ {
/*  16:    */   public PhaseId getPhaseId()
/*  17:    */   {
/*  18: 65 */     return PhaseId.ANY_PHASE;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public void beforePhase(PhaseEvent event)
/*  22:    */   {
/*  23: 69 */     for (PhaseListener listener : getDelegates(event.getFacesContext())) {
/*  24: 70 */       listener.beforePhase(event);
/*  25:    */     }
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void afterPhase(PhaseEvent event)
/*  29:    */   {
/*  30: 75 */     for (PhaseListener listener : getDelegates(event.getFacesContext())) {
/*  31: 76 */       listener.afterPhase(event);
/*  32:    */     }
/*  33:    */   }
/*  34:    */   
/*  35:    */   protected Collection<PhaseListener> getDelegates(FacesContext facesContext)
/*  36:    */   {
/*  37: 89 */     ListableBeanFactory bf = getBeanFactory(facesContext);
/*  38: 90 */     return BeanFactoryUtils.beansOfTypeIncludingAncestors(bf, PhaseListener.class, true, false).values();
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected ListableBeanFactory getBeanFactory(FacesContext facesContext)
/*  42:    */   {
/*  43:103 */     return getWebApplicationContext(facesContext);
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected WebApplicationContext getWebApplicationContext(FacesContext facesContext)
/*  47:    */   {
/*  48:114 */     return FacesContextUtils.getRequiredWebApplicationContext(facesContext);
/*  49:    */   }
/*  50:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.jsf.DelegatingPhaseListenerMulticaster
 * JD-Core Version:    0.7.0.1
 */