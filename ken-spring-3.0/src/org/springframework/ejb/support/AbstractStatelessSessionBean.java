/*  1:   */ package org.springframework.ejb.support;
/*  2:   */ 
/*  3:   */ import javax.ejb.CreateException;
/*  4:   */ import javax.ejb.EJBException;
/*  5:   */ import org.apache.commons.logging.Log;
/*  6:   */ import org.apache.commons.logging.LogFactory;
/*  7:   */ 
/*  8:   */ public abstract class AbstractStatelessSessionBean
/*  9:   */   extends AbstractSessionBean
/* 10:   */ {
/* 11:52 */   protected final Log logger = LogFactory.getLog(getClass());
/* 12:   */   
/* 13:   */   public void ejbCreate()
/* 14:   */     throws CreateException
/* 15:   */   {
/* 16:66 */     loadBeanFactory();
/* 17:67 */     onEjbCreate();
/* 18:   */   }
/* 19:   */   
/* 20:   */   protected abstract void onEjbCreate()
/* 21:   */     throws CreateException;
/* 22:   */   
/* 23:   */   public void ejbActivate()
/* 24:   */     throws EJBException
/* 25:   */   {
/* 26:86 */     throw new IllegalStateException("ejbActivate must not be invoked on a stateless session bean");
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void ejbPassivate()
/* 30:   */     throws EJBException
/* 31:   */   {
/* 32:94 */     throw new IllegalStateException("ejbPassivate must not be invoked on a stateless session bean");
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ejb.support.AbstractStatelessSessionBean
 * JD-Core Version:    0.7.0.1
 */