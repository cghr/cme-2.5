/*   1:    */ package org.springframework.scripting.jruby;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Array;
/*   4:    */ import java.lang.reflect.InvocationHandler;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.lang.reflect.Proxy;
/*   7:    */ import java.util.Collections;
/*   8:    */ import java.util.List;
/*   9:    */ import org.jruby.Ruby;
/*  10:    */ import org.jruby.RubyArray;
/*  11:    */ import org.jruby.RubyException;
/*  12:    */ import org.jruby.RubyNil;
/*  13:    */ import org.jruby.ast.ClassNode;
/*  14:    */ import org.jruby.ast.Colon2Node;
/*  15:    */ import org.jruby.ast.NewlineNode;
/*  16:    */ import org.jruby.ast.Node;
/*  17:    */ import org.jruby.exceptions.JumpException;
/*  18:    */ import org.jruby.exceptions.RaiseException;
/*  19:    */ import org.jruby.javasupport.JavaEmbedUtils;
/*  20:    */ import org.jruby.runtime.builtin.IRubyObject;
/*  21:    */ import org.springframework.core.NestedRuntimeException;
/*  22:    */ import org.springframework.util.ClassUtils;
/*  23:    */ import org.springframework.util.ObjectUtils;
/*  24:    */ import org.springframework.util.ReflectionUtils;
/*  25:    */ import org.springframework.util.StringUtils;
/*  26:    */ 
/*  27:    */ public abstract class JRubyScriptUtils
/*  28:    */ {
/*  29:    */   public static Object createJRubyObject(String scriptSource, Class[] interfaces)
/*  30:    */     throws JumpException
/*  31:    */   {
/*  32: 67 */     return createJRubyObject(scriptSource, interfaces, ClassUtils.getDefaultClassLoader());
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static Object createJRubyObject(String scriptSource, Class[] interfaces, ClassLoader classLoader)
/*  36:    */   {
/*  37: 79 */     Ruby ruby = initializeRuntime();
/*  38:    */     
/*  39: 81 */     Node scriptRootNode = ruby.parseEval(scriptSource, "", null, 0);
/*  40:    */     
/*  41: 83 */     IRubyObject rubyObject = ruby.runNormally(scriptRootNode, false);
/*  42: 85 */     if ((rubyObject instanceof RubyNil))
/*  43:    */     {
/*  44: 86 */       String className = findClassName(scriptRootNode);
/*  45: 87 */       rubyObject = ruby.evalScriptlet("\n" + className + ".new");
/*  46:    */     }
/*  47: 90 */     if ((rubyObject instanceof RubyNil)) {
/*  48: 91 */       throw new IllegalStateException("Compilation of JRuby script returned RubyNil: " + rubyObject);
/*  49:    */     }
/*  50: 94 */     return Proxy.newProxyInstance(classLoader, interfaces, new RubyObjectInvocationHandler(rubyObject, ruby));
/*  51:    */   }
/*  52:    */   
/*  53:    */   private static Ruby initializeRuntime()
/*  54:    */   {
/*  55:101 */     return JavaEmbedUtils.initialize(Collections.EMPTY_LIST);
/*  56:    */   }
/*  57:    */   
/*  58:    */   private static String findClassName(Node rootNode)
/*  59:    */   {
/*  60:110 */     ClassNode classNode = findClassNode(rootNode);
/*  61:111 */     if (classNode == null) {
/*  62:112 */       throw new IllegalArgumentException("Unable to determine class name for root node '" + rootNode + "'");
/*  63:    */     }
/*  64:114 */     Colon2Node node = (Colon2Node)classNode.getCPath();
/*  65:115 */     return node.getName();
/*  66:    */   }
/*  67:    */   
/*  68:    */   private static ClassNode findClassNode(Node node)
/*  69:    */   {
/*  70:124 */     if ((node instanceof ClassNode)) {
/*  71:125 */       return (ClassNode)node;
/*  72:    */     }
/*  73:127 */     List<Node> children = node.childNodes();
/*  74:128 */     for (Node child : children)
/*  75:    */     {
/*  76:129 */       if ((child instanceof ClassNode)) {
/*  77:130 */         return (ClassNode)child;
/*  78:    */       }
/*  79:132 */       if ((child instanceof NewlineNode))
/*  80:    */       {
/*  81:133 */         NewlineNode nn = (NewlineNode)child;
/*  82:134 */         Node found = findClassNode(nn.getNextNode());
/*  83:135 */         if ((found instanceof ClassNode)) {
/*  84:136 */           return (ClassNode)found;
/*  85:    */         }
/*  86:    */       }
/*  87:    */     }
/*  88:140 */     for (Node child : children)
/*  89:    */     {
/*  90:141 */       Node found = findClassNode(child);
/*  91:142 */       if ((found instanceof ClassNode)) {
/*  92:143 */         return (ClassNode)found;
/*  93:    */       }
/*  94:    */     }
/*  95:146 */     return null;
/*  96:    */   }
/*  97:    */   
/*  98:    */   private static class RubyObjectInvocationHandler
/*  99:    */     implements InvocationHandler
/* 100:    */   {
/* 101:    */     private final IRubyObject rubyObject;
/* 102:    */     private final Ruby ruby;
/* 103:    */     
/* 104:    */     public RubyObjectInvocationHandler(IRubyObject rubyObject, Ruby ruby)
/* 105:    */     {
/* 106:160 */       this.rubyObject = rubyObject;
/* 107:161 */       this.ruby = ruby;
/* 108:    */     }
/* 109:    */     
/* 110:    */     public Object invoke(Object proxy, Method method, Object[] args)
/* 111:    */       throws Throwable
/* 112:    */     {
/* 113:165 */       if (ReflectionUtils.isEqualsMethod(method)) {
/* 114:166 */         return Boolean.valueOf(isProxyForSameRubyObject(args[0]));
/* 115:    */       }
/* 116:168 */       if (ReflectionUtils.isHashCodeMethod(method)) {
/* 117:169 */         return Integer.valueOf(this.rubyObject.hashCode());
/* 118:    */       }
/* 119:171 */       if (ReflectionUtils.isToStringMethod(method))
/* 120:    */       {
/* 121:172 */         String toStringResult = this.rubyObject.toString();
/* 122:173 */         if (!StringUtils.hasText(toStringResult)) {
/* 123:174 */           toStringResult = ObjectUtils.identityToString(this.rubyObject);
/* 124:    */         }
/* 125:176 */         return "JRuby object [" + toStringResult + "]";
/* 126:    */       }
/* 127:    */       try
/* 128:    */       {
/* 129:179 */         IRubyObject[] rubyArgs = convertToRuby(args);
/* 130:180 */         IRubyObject rubyResult = 
/* 131:181 */           this.rubyObject.callMethod(this.ruby.getCurrentContext(), method.getName(), rubyArgs);
/* 132:182 */         return convertFromRuby(rubyResult, method.getReturnType());
/* 133:    */       }
/* 134:    */       catch (RaiseException ex)
/* 135:    */       {
/* 136:185 */         throw new JRubyScriptUtils.JRubyExecutionException(ex);
/* 137:    */       }
/* 138:    */     }
/* 139:    */     
/* 140:    */     private boolean isProxyForSameRubyObject(Object other)
/* 141:    */     {
/* 142:190 */       if (!Proxy.isProxyClass(other.getClass())) {
/* 143:191 */         return false;
/* 144:    */       }
/* 145:193 */       InvocationHandler ih = Proxy.getInvocationHandler(other);
/* 146:    */       
/* 147:195 */       return ((ih instanceof RubyObjectInvocationHandler)) && (this.rubyObject.equals(((RubyObjectInvocationHandler)ih).rubyObject));
/* 148:    */     }
/* 149:    */     
/* 150:    */     private IRubyObject[] convertToRuby(Object[] javaArgs)
/* 151:    */     {
/* 152:199 */       if ((javaArgs == null) || (javaArgs.length == 0)) {
/* 153:200 */         return new IRubyObject[0];
/* 154:    */       }
/* 155:202 */       IRubyObject[] rubyArgs = new IRubyObject[javaArgs.length];
/* 156:203 */       for (int i = 0; i < javaArgs.length; i++) {
/* 157:204 */         rubyArgs[i] = JavaEmbedUtils.javaToRuby(this.ruby, javaArgs[i]);
/* 158:    */       }
/* 159:206 */       return rubyArgs;
/* 160:    */     }
/* 161:    */     
/* 162:    */     private Object convertFromRuby(IRubyObject rubyResult, Class returnType)
/* 163:    */     {
/* 164:210 */       Object result = JavaEmbedUtils.rubyToJava(this.ruby, rubyResult, returnType);
/* 165:211 */       if (((result instanceof RubyArray)) && (returnType.isArray())) {
/* 166:212 */         result = convertFromRubyArray(((RubyArray)result).toJavaArray(), returnType);
/* 167:    */       }
/* 168:214 */       return result;
/* 169:    */     }
/* 170:    */     
/* 171:    */     private Object convertFromRubyArray(IRubyObject[] rubyArray, Class returnType)
/* 172:    */     {
/* 173:218 */       Class targetType = returnType.getComponentType();
/* 174:219 */       Object javaArray = Array.newInstance(targetType, rubyArray.length);
/* 175:220 */       for (int i = 0; i < rubyArray.length; i++)
/* 176:    */       {
/* 177:221 */         IRubyObject rubyObject = rubyArray[i];
/* 178:222 */         Array.set(javaArray, i, convertFromRuby(rubyObject, targetType));
/* 179:    */       }
/* 180:224 */       return javaArray;
/* 181:    */     }
/* 182:    */   }
/* 183:    */   
/* 184:    */   public static class JRubyExecutionException
/* 185:    */     extends NestedRuntimeException
/* 186:    */   {
/* 187:    */     public JRubyExecutionException(RaiseException ex)
/* 188:    */     {
/* 189:244 */       super(ex);
/* 190:    */     }
/* 191:    */     
/* 192:    */     private static String buildMessage(RaiseException ex)
/* 193:    */     {
/* 194:248 */       RubyException rubyEx = ex.getException();
/* 195:249 */       return (rubyEx != null) && (rubyEx.message != null) ? rubyEx.message.toString() : "Unexpected JRuby error";
/* 196:    */     }
/* 197:    */   }
/* 198:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scripting.jruby.JRubyScriptUtils
 * JD-Core Version:    0.7.0.1
 */