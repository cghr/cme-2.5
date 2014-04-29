/*  1:   */ package org.springframework.scripting.support;
/*  2:   */ 
/*  3:   */ import org.springframework.aop.target.dynamic.BeanFactoryRefreshableTargetSource;
/*  4:   */ import org.springframework.beans.factory.BeanFactory;
/*  5:   */ import org.springframework.scripting.ScriptFactory;
/*  6:   */ import org.springframework.scripting.ScriptSource;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ 
/*  9:   */ public class RefreshableScriptTargetSource
/* 10:   */   extends BeanFactoryRefreshableTargetSource
/* 11:   */ {
/* 12:   */   private final ScriptFactory scriptFactory;
/* 13:   */   private final ScriptSource scriptSource;
/* 14:   */   private final boolean isFactoryBean;
/* 15:   */   
/* 16:   */   public RefreshableScriptTargetSource(BeanFactory beanFactory, String beanName, ScriptFactory scriptFactory, ScriptSource scriptSource, boolean isFactoryBean)
/* 17:   */   {
/* 18:55 */     super(beanFactory, beanName);
/* 19:56 */     Assert.notNull(scriptFactory, "ScriptFactory must not be null");
/* 20:57 */     Assert.notNull(scriptSource, "ScriptSource must not be null");
/* 21:58 */     this.scriptFactory = scriptFactory;
/* 22:59 */     this.scriptSource = scriptSource;
/* 23:60 */     this.isFactoryBean = isFactoryBean;
/* 24:   */   }
/* 25:   */   
/* 26:   */   protected boolean requiresRefresh()
/* 27:   */   {
/* 28:71 */     return this.scriptFactory.requiresScriptedObjectRefresh(this.scriptSource);
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected Object obtainFreshBean(BeanFactory beanFactory, String beanName)
/* 32:   */   {
/* 33:79 */     return super.obtainFreshBean(beanFactory, 
/* 34:80 */       this.isFactoryBean ? "&" + beanName : beanName);
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scripting.support.RefreshableScriptTargetSource
 * JD-Core Version:    0.7.0.1
 */