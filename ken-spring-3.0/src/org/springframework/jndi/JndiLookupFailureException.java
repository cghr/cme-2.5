/*  1:   */ package org.springframework.jndi;
/*  2:   */ 
/*  3:   */ import javax.naming.NamingException;
/*  4:   */ import org.springframework.core.NestedRuntimeException;
/*  5:   */ 
/*  6:   */ public class JndiLookupFailureException
/*  7:   */   extends NestedRuntimeException
/*  8:   */ {
/*  9:   */   public JndiLookupFailureException(String msg, NamingException cause)
/* 10:   */   {
/* 11:41 */     super(msg, cause);
/* 12:   */   }
/* 13:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jndi.JndiLookupFailureException
 * JD-Core Version:    0.7.0.1
 */