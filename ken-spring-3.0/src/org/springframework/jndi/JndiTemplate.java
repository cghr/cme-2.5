/*   1:    */ package org.springframework.jndi;
/*   2:    */ 
/*   3:    */ import java.util.Hashtable;
/*   4:    */ import java.util.Properties;
/*   5:    */ import javax.naming.Context;
/*   6:    */ import javax.naming.InitialContext;
/*   7:    */ import javax.naming.NameNotFoundException;
/*   8:    */ import javax.naming.NamingException;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.apache.commons.logging.LogFactory;
/*  11:    */ import org.springframework.util.CollectionUtils;
/*  12:    */ 
/*  13:    */ public class JndiTemplate
/*  14:    */ {
/*  15: 43 */   protected final Log logger = LogFactory.getLog(getClass());
/*  16:    */   private Properties environment;
/*  17:    */   
/*  18:    */   public JndiTemplate() {}
/*  19:    */   
/*  20:    */   public JndiTemplate(Properties environment)
/*  21:    */   {
/*  22: 58 */     this.environment = environment;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setEnvironment(Properties environment)
/*  26:    */   {
/*  27: 66 */     this.environment = environment;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Properties getEnvironment()
/*  31:    */   {
/*  32: 73 */     return this.environment;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public <T> T execute(JndiCallback<T> contextCallback)
/*  36:    */     throws NamingException
/*  37:    */   {
/*  38: 85 */     Context ctx = getContext();
/*  39:    */     try
/*  40:    */     {
/*  41: 87 */       return contextCallback.doInContext(ctx);
/*  42:    */     }
/*  43:    */     finally
/*  44:    */     {
/*  45: 90 */       releaseContext(ctx);
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public Context getContext()
/*  50:    */     throws NamingException
/*  51:    */   {
/*  52:103 */     return createInitialContext();
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void releaseContext(Context ctx)
/*  56:    */   {
/*  57:112 */     if (ctx != null) {
/*  58:    */       try
/*  59:    */       {
/*  60:114 */         ctx.close();
/*  61:    */       }
/*  62:    */       catch (NamingException ex)
/*  63:    */       {
/*  64:117 */         this.logger.debug("Could not close JNDI InitialContext", ex);
/*  65:    */       }
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected Context createInitialContext()
/*  70:    */     throws NamingException
/*  71:    */   {
/*  72:130 */     Hashtable icEnv = null;
/*  73:131 */     Properties env = getEnvironment();
/*  74:132 */     if (env != null)
/*  75:    */     {
/*  76:133 */       icEnv = new Hashtable(env.size());
/*  77:134 */       CollectionUtils.mergePropertiesIntoMap(env, icEnv);
/*  78:    */     }
/*  79:136 */     return new InitialContext(icEnv);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public Object lookup(final String name)
/*  83:    */     throws NamingException
/*  84:    */   {
/*  85:149 */     if (this.logger.isDebugEnabled()) {
/*  86:150 */       this.logger.debug("Looking up JNDI object with name [" + name + "]");
/*  87:    */     }
/*  88:152 */     execute(new JndiCallback()
/*  89:    */     {
/*  90:    */       public Object doInContext(Context ctx)
/*  91:    */         throws NamingException
/*  92:    */       {
/*  93:154 */         Object located = ctx.lookup(name);
/*  94:155 */         if (located == null) {
/*  95:156 */           throw new NameNotFoundException(
/*  96:157 */             "JNDI object with [" + name + "] not found: JNDI implementation returned null");
/*  97:    */         }
/*  98:159 */         return located;
/*  99:    */       }
/* 100:    */     });
/* 101:    */   }
/* 102:    */   
/* 103:    */   public <T> T lookup(String name, Class<T> requiredType)
/* 104:    */     throws NamingException
/* 105:    */   {
/* 106:178 */     Object jndiObject = lookup(name);
/* 107:179 */     if ((requiredType != null) && (!requiredType.isInstance(jndiObject))) {
/* 108:180 */       throw new TypeMismatchNamingException(
/* 109:181 */         name, requiredType, jndiObject != null ? jndiObject.getClass() : null);
/* 110:    */     }
/* 111:183 */     return jndiObject;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void bind(final String name, final Object object)
/* 115:    */     throws NamingException
/* 116:    */   {
/* 117:193 */     if (this.logger.isDebugEnabled()) {
/* 118:194 */       this.logger.debug("Binding JNDI object with name [" + name + "]");
/* 119:    */     }
/* 120:196 */     execute(new JndiCallback()
/* 121:    */     {
/* 122:    */       public Object doInContext(Context ctx)
/* 123:    */         throws NamingException
/* 124:    */       {
/* 125:198 */         ctx.bind(name, object);
/* 126:199 */         return null;
/* 127:    */       }
/* 128:    */     });
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void rebind(final String name, final Object object)
/* 132:    */     throws NamingException
/* 133:    */   {
/* 134:212 */     if (this.logger.isDebugEnabled()) {
/* 135:213 */       this.logger.debug("Rebinding JNDI object with name [" + name + "]");
/* 136:    */     }
/* 137:215 */     execute(new JndiCallback()
/* 138:    */     {
/* 139:    */       public Object doInContext(Context ctx)
/* 140:    */         throws NamingException
/* 141:    */       {
/* 142:217 */         ctx.rebind(name, object);
/* 143:218 */         return null;
/* 144:    */       }
/* 145:    */     });
/* 146:    */   }
/* 147:    */   
/* 148:    */   public void unbind(final String name)
/* 149:    */     throws NamingException
/* 150:    */   {
/* 151:229 */     if (this.logger.isDebugEnabled()) {
/* 152:230 */       this.logger.debug("Unbinding JNDI object with name [" + name + "]");
/* 153:    */     }
/* 154:232 */     execute(new JndiCallback()
/* 155:    */     {
/* 156:    */       public Object doInContext(Context ctx)
/* 157:    */         throws NamingException
/* 158:    */       {
/* 159:234 */         ctx.unbind(name);
/* 160:235 */         return null;
/* 161:    */       }
/* 162:    */     });
/* 163:    */   }
/* 164:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jndi.JndiTemplate
 * JD-Core Version:    0.7.0.1
 */