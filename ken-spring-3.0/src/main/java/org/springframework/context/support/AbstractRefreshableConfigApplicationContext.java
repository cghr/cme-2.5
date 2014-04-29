/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.factory.BeanNameAware;
/*   4:    */ import org.springframework.beans.factory.InitializingBean;
/*   5:    */ import org.springframework.context.ApplicationContext;
/*   6:    */ import org.springframework.core.env.ConfigurableEnvironment;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ import org.springframework.util.StringUtils;
/*   9:    */ 
/*  10:    */ public abstract class AbstractRefreshableConfigApplicationContext
/*  11:    */   extends AbstractRefreshableApplicationContext
/*  12:    */   implements BeanNameAware, InitializingBean
/*  13:    */ {
/*  14:    */   private String[] configLocations;
/*  15: 45 */   private boolean setIdCalled = false;
/*  16:    */   
/*  17:    */   public AbstractRefreshableConfigApplicationContext() {}
/*  18:    */   
/*  19:    */   public AbstractRefreshableConfigApplicationContext(ApplicationContext parent)
/*  20:    */   {
/*  21: 59 */     super(parent);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setConfigLocation(String location)
/*  25:    */   {
/*  26: 69 */     setConfigLocations(StringUtils.tokenizeToStringArray(location, ",; \t\n"));
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setConfigLocations(String[] locations)
/*  30:    */   {
/*  31: 77 */     if (locations != null)
/*  32:    */     {
/*  33: 78 */       Assert.noNullElements(locations, "Config locations must not be null");
/*  34: 79 */       this.configLocations = new String[locations.length];
/*  35: 80 */       for (int i = 0; i < locations.length; i++) {
/*  36: 81 */         this.configLocations[i] = resolvePath(locations[i]).trim();
/*  37:    */       }
/*  38:    */     }
/*  39:    */     else
/*  40:    */     {
/*  41: 85 */       this.configLocations = null;
/*  42:    */     }
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected String[] getConfigLocations()
/*  46:    */   {
/*  47:100 */     return this.configLocations != null ? this.configLocations : getDefaultConfigLocations();
/*  48:    */   }
/*  49:    */   
/*  50:    */   protected String[] getDefaultConfigLocations()
/*  51:    */   {
/*  52:112 */     return null;
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected String resolvePath(String path)
/*  56:    */   {
/*  57:122 */     return getEnvironment().resolveRequiredPlaceholders(path);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setId(String id)
/*  61:    */   {
/*  62:128 */     super.setId(id);
/*  63:129 */     this.setIdCalled = true;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void setBeanName(String name)
/*  67:    */   {
/*  68:137 */     if (!this.setIdCalled)
/*  69:    */     {
/*  70:138 */       super.setId(name);
/*  71:139 */       setDisplayName("ApplicationContext '" + name + "'");
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void afterPropertiesSet()
/*  76:    */   {
/*  77:148 */     if (!isActive()) {
/*  78:149 */       refresh();
/*  79:    */     }
/*  80:    */   }
/*  81:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.AbstractRefreshableConfigApplicationContext
 * JD-Core Version:    0.7.0.1
 */