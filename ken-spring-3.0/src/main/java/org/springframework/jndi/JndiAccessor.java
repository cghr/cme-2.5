/*  1:   */ package org.springframework.jndi;
/*  2:   */ 
/*  3:   */ import java.util.Properties;
/*  4:   */ import org.apache.commons.logging.Log;
/*  5:   */ import org.apache.commons.logging.LogFactory;
/*  6:   */ 
/*  7:   */ public class JndiAccessor
/*  8:   */ {
/*  9:38 */   protected final Log logger = LogFactory.getLog(getClass());
/* 10:40 */   private JndiTemplate jndiTemplate = new JndiTemplate();
/* 11:   */   
/* 12:   */   public void setJndiTemplate(JndiTemplate jndiTemplate)
/* 13:   */   {
/* 14:49 */     this.jndiTemplate = (jndiTemplate != null ? jndiTemplate : new JndiTemplate());
/* 15:   */   }
/* 16:   */   
/* 17:   */   public JndiTemplate getJndiTemplate()
/* 18:   */   {
/* 19:56 */     return this.jndiTemplate;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setJndiEnvironment(Properties jndiEnvironment)
/* 23:   */   {
/* 24:65 */     this.jndiTemplate = new JndiTemplate(jndiEnvironment);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public Properties getJndiEnvironment()
/* 28:   */   {
/* 29:72 */     return this.jndiTemplate.getEnvironment();
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jndi.JndiAccessor
 * JD-Core Version:    0.7.0.1
 */