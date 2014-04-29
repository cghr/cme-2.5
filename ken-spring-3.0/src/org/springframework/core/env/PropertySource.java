/*   1:    */ package org.springframework.core.env;
/*   2:    */ 
/*   3:    */ import org.apache.commons.logging.Log;
/*   4:    */ import org.apache.commons.logging.LogFactory;
/*   5:    */ import org.springframework.util.Assert;
/*   6:    */ 
/*   7:    */ public abstract class PropertySource<T>
/*   8:    */ {
/*   9: 58 */   protected final Log logger = LogFactory.getLog(getClass());
/*  10:    */   protected final String name;
/*  11:    */   protected final T source;
/*  12:    */   
/*  13:    */   public PropertySource(String name, T source)
/*  14:    */   {
/*  15: 68 */     Assert.hasText(name, "Property source name must contain at least one character");
/*  16: 69 */     Assert.notNull(source, "Property source must not be null");
/*  17: 70 */     this.name = name;
/*  18: 71 */     this.source = source;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public PropertySource(String name)
/*  22:    */   {
/*  23: 83 */     this(name, new Object());
/*  24:    */   }
/*  25:    */   
/*  26:    */   public String getName()
/*  27:    */   {
/*  28: 90 */     return this.name;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public T getSource()
/*  32:    */   {
/*  33: 97 */     return this.source;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public boolean containsProperty(String key)
/*  37:    */   {
/*  38:108 */     return getProperty(key) != null;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public abstract Object getProperty(String paramString);
/*  42:    */   
/*  43:    */   public int hashCode()
/*  44:    */   {
/*  45:125 */     int result = 1;
/*  46:126 */     result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
/*  47:127 */     return result;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public boolean equals(Object obj)
/*  51:    */   {
/*  52:141 */     if (this == obj) {
/*  53:142 */       return true;
/*  54:    */     }
/*  55:143 */     if (obj == null) {
/*  56:144 */       return false;
/*  57:    */     }
/*  58:145 */     if (!(obj instanceof PropertySource)) {
/*  59:146 */       return false;
/*  60:    */     }
/*  61:147 */     PropertySource<?> other = (PropertySource)obj;
/*  62:148 */     if (this.name == null)
/*  63:    */     {
/*  64:149 */       if (other.name != null) {
/*  65:150 */         return false;
/*  66:    */       }
/*  67:    */     }
/*  68:151 */     else if (!this.name.equals(other.name)) {
/*  69:152 */       return false;
/*  70:    */     }
/*  71:153 */     return true;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public String toString()
/*  75:    */   {
/*  76:169 */     if (this.logger.isDebugEnabled()) {
/*  77:170 */       return String.format("%s@%s [name='%s', properties=%s]", new Object[] {
/*  78:171 */         getClass().getSimpleName(), Integer.valueOf(System.identityHashCode(this)), this.name, this.source });
/*  79:    */     }
/*  80:174 */     return String.format("%s [name='%s']", new Object[] {
/*  81:175 */       getClass().getSimpleName(), this.name });
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static PropertySource<?> named(String name)
/*  85:    */   {
/*  86:202 */     return new ComparisonPropertySource(name);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public static class StubPropertySource
/*  90:    */     extends PropertySource<Object>
/*  91:    */   {
/*  92:    */     public StubPropertySource(String name)
/*  93:    */     {
/*  94:222 */       super(new Object());
/*  95:    */     }
/*  96:    */     
/*  97:    */     public String getProperty(String key)
/*  98:    */     {
/*  99:230 */       return null;
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   static class ComparisonPropertySource
/* 104:    */     extends PropertySource.StubPropertySource
/* 105:    */   {
/* 106:    */     private static final String USAGE_ERROR = "ComparisonPropertySource instances are for collection comparison use only";
/* 107:    */     
/* 108:    */     public ComparisonPropertySource(String name)
/* 109:    */     {
/* 110:245 */       super();
/* 111:    */     }
/* 112:    */     
/* 113:    */     public Object getSource()
/* 114:    */     {
/* 115:250 */       throw new UnsupportedOperationException("ComparisonPropertySource instances are for collection comparison use only");
/* 116:    */     }
/* 117:    */     
/* 118:    */     public boolean containsProperty(String key)
/* 119:    */     {
/* 120:255 */       throw new UnsupportedOperationException("ComparisonPropertySource instances are for collection comparison use only");
/* 121:    */     }
/* 122:    */     
/* 123:    */     public String getProperty(String key)
/* 124:    */     {
/* 125:260 */       throw new UnsupportedOperationException("ComparisonPropertySource instances are for collection comparison use only");
/* 126:    */     }
/* 127:    */     
/* 128:    */     public String toString()
/* 129:    */     {
/* 130:265 */       return String.format("%s [name='%s']", new Object[] { getClass().getSimpleName(), this.name });
/* 131:    */     }
/* 132:    */   }
/* 133:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.PropertySource
 * JD-Core Version:    0.7.0.1
 */