/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Constructor;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import net.sf.cglib.proxy.Callback;
/*   6:    */ import net.sf.cglib.proxy.CallbackFilter;
/*   7:    */ import net.sf.cglib.proxy.Enhancer;
/*   8:    */ import net.sf.cglib.proxy.MethodInterceptor;
/*   9:    */ import net.sf.cglib.proxy.MethodProxy;
/*  10:    */ import net.sf.cglib.proxy.NoOp;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.apache.commons.logging.LogFactory;
/*  13:    */ import org.springframework.beans.factory.BeanFactory;
/*  14:    */ 
/*  15:    */ public class CglibSubclassingInstantiationStrategy
/*  16:    */   extends SimpleInstantiationStrategy
/*  17:    */ {
/*  18:    */   private static final int PASSTHROUGH = 0;
/*  19:    */   private static final int LOOKUP_OVERRIDE = 1;
/*  20:    */   private static final int METHOD_REPLACER = 2;
/*  21:    */   
/*  22:    */   protected Object instantiateWithMethodInjection(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner)
/*  23:    */   {
/*  24: 71 */     return new CglibSubclassCreator(beanDefinition, owner).instantiate(null, null);
/*  25:    */   }
/*  26:    */   
/*  27:    */   protected Object instantiateWithMethodInjection(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner, Constructor ctor, Object[] args)
/*  28:    */   {
/*  29: 79 */     return new CglibSubclassCreator(beanDefinition, owner).instantiate(ctor, args);
/*  30:    */   }
/*  31:    */   
/*  32:    */   private static class CglibSubclassCreator
/*  33:    */   {
/*  34: 88 */     private static final Log logger = LogFactory.getLog(CglibSubclassCreator.class);
/*  35:    */     private final RootBeanDefinition beanDefinition;
/*  36:    */     private final BeanFactory owner;
/*  37:    */     
/*  38:    */     public CglibSubclassCreator(RootBeanDefinition beanDefinition, BeanFactory owner)
/*  39:    */     {
/*  40: 95 */       this.beanDefinition = beanDefinition;
/*  41: 96 */       this.owner = owner;
/*  42:    */     }
/*  43:    */     
/*  44:    */     public Object instantiate(Constructor ctor, Object[] args)
/*  45:    */     {
/*  46:109 */       Enhancer enhancer = new Enhancer();
/*  47:110 */       enhancer.setSuperclass(this.beanDefinition.getBeanClass());
/*  48:111 */       enhancer.setCallbackFilter(new CallbackFilterImpl(null));
/*  49:112 */       enhancer.setCallbacks(new Callback[] {
/*  50:113 */         NoOp.INSTANCE, 
/*  51:114 */         new LookupOverrideMethodInterceptor(null), 
/*  52:115 */         new ReplaceOverrideMethodInterceptor(null) });
/*  53:    */       
/*  54:    */ 
/*  55:118 */       return ctor == null ? 
/*  56:119 */         enhancer.create() : 
/*  57:120 */         enhancer.create(ctor.getParameterTypes(), args);
/*  58:    */     }
/*  59:    */     
/*  60:    */     private class CglibIdentitySupport
/*  61:    */     {
/*  62:    */       private CglibIdentitySupport() {}
/*  63:    */       
/*  64:    */       protected RootBeanDefinition getBeanDefinition()
/*  65:    */       {
/*  66:135 */         return CglibSubclassingInstantiationStrategy.CglibSubclassCreator.this.beanDefinition;
/*  67:    */       }
/*  68:    */       
/*  69:    */       public boolean equals(Object other)
/*  70:    */       {
/*  71:141 */         return (other.getClass().equals(getClass())) && (((CglibIdentitySupport)other).getBeanDefinition().equals(CglibSubclassingInstantiationStrategy.CglibSubclassCreator.this.beanDefinition));
/*  72:    */       }
/*  73:    */       
/*  74:    */       public int hashCode()
/*  75:    */       {
/*  76:146 */         return CglibSubclassingInstantiationStrategy.CglibSubclassCreator.this.beanDefinition.hashCode();
/*  77:    */       }
/*  78:    */     }
/*  79:    */     
/*  80:    */     private class LookupOverrideMethodInterceptor
/*  81:    */       extends CglibSubclassingInstantiationStrategy.CglibSubclassCreator.CglibIdentitySupport
/*  82:    */       implements MethodInterceptor
/*  83:    */     {
/*  84:    */       private LookupOverrideMethodInterceptor()
/*  85:    */       {
/*  86:155 */         super(null);
/*  87:    */       }
/*  88:    */       
/*  89:    */       public Object intercept(Object obj, Method method, Object[] args, MethodProxy mp)
/*  90:    */         throws Throwable
/*  91:    */       {
/*  92:159 */         LookupOverride lo = (LookupOverride)CglibSubclassingInstantiationStrategy.CglibSubclassCreator.this.beanDefinition.getMethodOverrides().getOverride(method);
/*  93:160 */         return CglibSubclassingInstantiationStrategy.CglibSubclassCreator.this.owner.getBean(lo.getBeanName());
/*  94:    */       }
/*  95:    */     }
/*  96:    */     
/*  97:    */     private class ReplaceOverrideMethodInterceptor
/*  98:    */       extends CglibSubclassingInstantiationStrategy.CglibSubclassCreator.CglibIdentitySupport
/*  99:    */       implements MethodInterceptor
/* 100:    */     {
/* 101:    */       private ReplaceOverrideMethodInterceptor()
/* 102:    */       {
/* 103:169 */         super(null);
/* 104:    */       }
/* 105:    */       
/* 106:    */       public Object intercept(Object obj, Method method, Object[] args, MethodProxy mp)
/* 107:    */         throws Throwable
/* 108:    */       {
/* 109:172 */         ReplaceOverride ro = (ReplaceOverride)CglibSubclassingInstantiationStrategy.CglibSubclassCreator.this.beanDefinition.getMethodOverrides().getOverride(method);
/* 110:    */         
/* 111:174 */         MethodReplacer mr = (MethodReplacer)CglibSubclassingInstantiationStrategy.CglibSubclassCreator.this.owner.getBean(ro.getMethodReplacerBeanName());
/* 112:175 */         return mr.reimplement(obj, method, args);
/* 113:    */       }
/* 114:    */     }
/* 115:    */     
/* 116:    */     private class CallbackFilterImpl
/* 117:    */       extends CglibSubclassingInstantiationStrategy.CglibSubclassCreator.CglibIdentitySupport
/* 118:    */       implements CallbackFilter
/* 119:    */     {
/* 120:    */       private CallbackFilterImpl()
/* 121:    */       {
/* 122:183 */         super(null);
/* 123:    */       }
/* 124:    */       
/* 125:    */       public int accept(Method method)
/* 126:    */       {
/* 127:186 */         MethodOverride methodOverride = CglibSubclassingInstantiationStrategy.CglibSubclassCreator.this.beanDefinition.getMethodOverrides().getOverride(method);
/* 128:187 */         if (CglibSubclassingInstantiationStrategy.CglibSubclassCreator.logger.isTraceEnabled()) {
/* 129:188 */           CglibSubclassingInstantiationStrategy.CglibSubclassCreator.logger.trace("Override for '" + method.getName() + "' is [" + methodOverride + "]");
/* 130:    */         }
/* 131:190 */         if (methodOverride == null) {
/* 132:191 */           return 0;
/* 133:    */         }
/* 134:193 */         if ((methodOverride instanceof LookupOverride)) {
/* 135:194 */           return 1;
/* 136:    */         }
/* 137:196 */         if ((methodOverride instanceof ReplaceOverride)) {
/* 138:197 */           return 2;
/* 139:    */         }
/* 140:199 */         throw new UnsupportedOperationException(
/* 141:200 */           "Unexpected MethodOverride subclass: " + methodOverride.getClass().getName());
/* 142:    */       }
/* 143:    */     }
/* 144:    */   }
/* 145:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.CglibSubclassingInstantiationStrategy
 * JD-Core Version:    0.7.0.1
 */