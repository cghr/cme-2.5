/*   1:    */ package org.springframework.jca.endpoint;
/*   2:    */ 
/*   3:    */ import javax.resource.ResourceException;
/*   4:    */ import javax.resource.spi.ActivationSpec;
/*   5:    */ import javax.resource.spi.ResourceAdapter;
/*   6:    */ import javax.resource.spi.endpoint.MessageEndpointFactory;
/*   7:    */ import org.springframework.beans.factory.DisposableBean;
/*   8:    */ import org.springframework.beans.factory.InitializingBean;
/*   9:    */ import org.springframework.context.SmartLifecycle;
/*  10:    */ 
/*  11:    */ public class GenericMessageEndpointManager
/*  12:    */   implements SmartLifecycle, InitializingBean, DisposableBean
/*  13:    */ {
/*  14:    */   private ResourceAdapter resourceAdapter;
/*  15:    */   private MessageEndpointFactory messageEndpointFactory;
/*  16:    */   private ActivationSpec activationSpec;
/*  17:155 */   private boolean autoStartup = true;
/*  18:157 */   private int phase = 2147483647;
/*  19:159 */   private boolean running = false;
/*  20:161 */   private final Object lifecycleMonitor = new Object();
/*  21:    */   
/*  22:    */   public void setResourceAdapter(ResourceAdapter resourceAdapter)
/*  23:    */   {
/*  24:168 */     this.resourceAdapter = resourceAdapter;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public ResourceAdapter getResourceAdapter()
/*  28:    */   {
/*  29:175 */     return this.resourceAdapter;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setMessageEndpointFactory(MessageEndpointFactory messageEndpointFactory)
/*  33:    */   {
/*  34:187 */     this.messageEndpointFactory = messageEndpointFactory;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public MessageEndpointFactory getMessageEndpointFactory()
/*  38:    */   {
/*  39:194 */     return this.messageEndpointFactory;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setActivationSpec(ActivationSpec activationSpec)
/*  43:    */   {
/*  44:203 */     this.activationSpec = activationSpec;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public ActivationSpec getActivationSpec()
/*  48:    */   {
/*  49:210 */     return this.activationSpec;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setAutoStartup(boolean autoStartup)
/*  53:    */   {
/*  54:220 */     this.autoStartup = autoStartup;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public boolean isAutoStartup()
/*  58:    */   {
/*  59:228 */     return this.autoStartup;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setPhase(int phase)
/*  63:    */   {
/*  64:239 */     this.phase = phase;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public int getPhase()
/*  68:    */   {
/*  69:246 */     return this.phase;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void afterPropertiesSet()
/*  73:    */     throws ResourceException
/*  74:    */   {
/*  75:254 */     if (getResourceAdapter() == null) {
/*  76:255 */       throw new IllegalArgumentException("Property 'resourceAdapter' is required");
/*  77:    */     }
/*  78:257 */     if (getMessageEndpointFactory() == null) {
/*  79:258 */       throw new IllegalArgumentException("Property 'messageEndpointFactory' is required");
/*  80:    */     }
/*  81:260 */     ActivationSpec activationSpec = getActivationSpec();
/*  82:261 */     if (activationSpec == null) {
/*  83:262 */       throw new IllegalArgumentException("Property 'activationSpec' is required");
/*  84:    */     }
/*  85:265 */     if (activationSpec.getResourceAdapter() == null) {
/*  86:266 */       activationSpec.setResourceAdapter(getResourceAdapter());
/*  87:268 */     } else if (activationSpec.getResourceAdapter() != getResourceAdapter()) {
/*  88:269 */       throw new IllegalArgumentException("ActivationSpec [" + activationSpec + 
/*  89:270 */         "] is associated with a different ResourceAdapter: " + activationSpec.getResourceAdapter());
/*  90:    */     }
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void start()
/*  94:    */   {
/*  95:278 */     synchronized (this.lifecycleMonitor)
/*  96:    */     {
/*  97:279 */       if (!this.running)
/*  98:    */       {
/*  99:    */         try
/* 100:    */         {
/* 101:281 */           getResourceAdapter().endpointActivation(getMessageEndpointFactory(), getActivationSpec());
/* 102:    */         }
/* 103:    */         catch (ResourceException ex)
/* 104:    */         {
/* 105:284 */           throw new IllegalStateException("Could not activate message endpoint", ex);
/* 106:    */         }
/* 107:286 */         this.running = true;
/* 108:    */       }
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void stop()
/* 113:    */   {
/* 114:295 */     synchronized (this.lifecycleMonitor)
/* 115:    */     {
/* 116:296 */       if (this.running)
/* 117:    */       {
/* 118:297 */         getResourceAdapter().endpointDeactivation(getMessageEndpointFactory(), getActivationSpec());
/* 119:298 */         this.running = false;
/* 120:    */       }
/* 121:    */     }
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void stop(Runnable callback)
/* 125:    */   {
/* 126:304 */     synchronized (this.lifecycleMonitor)
/* 127:    */     {
/* 128:305 */       stop();
/* 129:306 */       callback.run();
/* 130:    */     }
/* 131:    */   }
/* 132:    */   
/* 133:    */   public boolean isRunning()
/* 134:    */   {
/* 135:314 */     synchronized (this.lifecycleMonitor)
/* 136:    */     {
/* 137:315 */       return this.running;
/* 138:    */     }
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void destroy()
/* 142:    */   {
/* 143:323 */     stop();
/* 144:    */   }
/* 145:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.endpoint.GenericMessageEndpointManager
 * JD-Core Version:    0.7.0.1
 */