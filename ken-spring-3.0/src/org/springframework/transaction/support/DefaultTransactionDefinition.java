/*   1:    */ package org.springframework.transaction.support;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Set;
/*   5:    */ import org.springframework.core.Constants;
/*   6:    */ import org.springframework.transaction.TransactionDefinition;
/*   7:    */ 
/*   8:    */ public class DefaultTransactionDefinition
/*   9:    */   implements TransactionDefinition, Serializable
/*  10:    */ {
/*  11:    */   public static final String PREFIX_PROPAGATION = "PROPAGATION_";
/*  12:    */   public static final String PREFIX_ISOLATION = "ISOLATION_";
/*  13:    */   public static final String PREFIX_TIMEOUT = "timeout_";
/*  14:    */   public static final String READ_ONLY_MARKER = "readOnly";
/*  15: 51 */   static final Constants constants = new Constants(TransactionDefinition.class);
/*  16: 53 */   private int propagationBehavior = 0;
/*  17: 55 */   private int isolationLevel = -1;
/*  18: 57 */   private int timeout = -1;
/*  19: 59 */   private boolean readOnly = false;
/*  20:    */   private String name;
/*  21:    */   
/*  22:    */   public DefaultTransactionDefinition() {}
/*  23:    */   
/*  24:    */   public DefaultTransactionDefinition(TransactionDefinition other)
/*  25:    */   {
/*  26: 85 */     this.propagationBehavior = other.getPropagationBehavior();
/*  27: 86 */     this.isolationLevel = other.getIsolationLevel();
/*  28: 87 */     this.timeout = other.getTimeout();
/*  29: 88 */     this.readOnly = other.isReadOnly();
/*  30: 89 */     this.name = other.getName();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public DefaultTransactionDefinition(int propagationBehavior)
/*  34:    */   {
/*  35:102 */     this.propagationBehavior = propagationBehavior;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public final void setPropagationBehaviorName(String constantName)
/*  39:    */     throws IllegalArgumentException
/*  40:    */   {
/*  41:116 */     if ((constantName == null) || (!constantName.startsWith("PROPAGATION_"))) {
/*  42:117 */       throw new IllegalArgumentException("Only propagation constants allowed");
/*  43:    */     }
/*  44:119 */     setPropagationBehavior(constants.asNumber(constantName).intValue());
/*  45:    */   }
/*  46:    */   
/*  47:    */   public final void setPropagationBehavior(int propagationBehavior)
/*  48:    */   {
/*  49:130 */     if (!constants.getValues("PROPAGATION_").contains(Integer.valueOf(propagationBehavior))) {
/*  50:131 */       throw new IllegalArgumentException("Only values of propagation constants allowed");
/*  51:    */     }
/*  52:133 */     this.propagationBehavior = propagationBehavior;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public final int getPropagationBehavior()
/*  56:    */   {
/*  57:137 */     return this.propagationBehavior;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public final void setIsolationLevelName(String constantName)
/*  61:    */     throws IllegalArgumentException
/*  62:    */   {
/*  63:150 */     if ((constantName == null) || (!constantName.startsWith("ISOLATION_"))) {
/*  64:151 */       throw new IllegalArgumentException("Only isolation constants allowed");
/*  65:    */     }
/*  66:153 */     setIsolationLevel(constants.asNumber(constantName).intValue());
/*  67:    */   }
/*  68:    */   
/*  69:    */   public final void setIsolationLevel(int isolationLevel)
/*  70:    */   {
/*  71:164 */     if (!constants.getValues("ISOLATION_").contains(Integer.valueOf(isolationLevel))) {
/*  72:165 */       throw new IllegalArgumentException("Only values of isolation constants allowed");
/*  73:    */     }
/*  74:167 */     this.isolationLevel = isolationLevel;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public final int getIsolationLevel()
/*  78:    */   {
/*  79:171 */     return this.isolationLevel;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public final void setTimeout(int timeout)
/*  83:    */   {
/*  84:180 */     if (timeout < -1) {
/*  85:181 */       throw new IllegalArgumentException("Timeout must be a positive integer or TIMEOUT_DEFAULT");
/*  86:    */     }
/*  87:183 */     this.timeout = timeout;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public final int getTimeout()
/*  91:    */   {
/*  92:187 */     return this.timeout;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public final void setReadOnly(boolean readOnly)
/*  96:    */   {
/*  97:195 */     this.readOnly = readOnly;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public final boolean isReadOnly()
/* 101:    */   {
/* 102:199 */     return this.readOnly;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public final void setName(String name)
/* 106:    */   {
/* 107:208 */     this.name = name;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public final String getName()
/* 111:    */   {
/* 112:212 */     return this.name;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public boolean equals(Object other)
/* 116:    */   {
/* 117:222 */     return ((other instanceof TransactionDefinition)) && (toString().equals(other.toString()));
/* 118:    */   }
/* 119:    */   
/* 120:    */   public int hashCode()
/* 121:    */   {
/* 122:231 */     return toString().hashCode();
/* 123:    */   }
/* 124:    */   
/* 125:    */   public String toString()
/* 126:    */   {
/* 127:248 */     return getDefinitionDescription().toString();
/* 128:    */   }
/* 129:    */   
/* 130:    */   protected final StringBuilder getDefinitionDescription()
/* 131:    */   {
/* 132:256 */     StringBuilder result = new StringBuilder();
/* 133:257 */     result.append(constants.toCode(Integer.valueOf(this.propagationBehavior), "PROPAGATION_"));
/* 134:258 */     result.append(',');
/* 135:259 */     result.append(constants.toCode(Integer.valueOf(this.isolationLevel), "ISOLATION_"));
/* 136:260 */     if (this.timeout != -1)
/* 137:    */     {
/* 138:261 */       result.append(',');
/* 139:262 */       result.append("timeout_").append(this.timeout);
/* 140:    */     }
/* 141:264 */     if (this.readOnly)
/* 142:    */     {
/* 143:265 */       result.append(',');
/* 144:266 */       result.append("readOnly");
/* 145:    */     }
/* 146:268 */     return result;
/* 147:    */   }
/* 148:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.DefaultTransactionDefinition
 * JD-Core Version:    0.7.0.1
 */