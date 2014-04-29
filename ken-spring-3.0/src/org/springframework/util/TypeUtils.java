/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.GenericArrayType;
/*   4:    */ import java.lang.reflect.ParameterizedType;
/*   5:    */ import java.lang.reflect.Type;
/*   6:    */ import java.lang.reflect.WildcardType;
/*   7:    */ 
/*   8:    */ public abstract class TypeUtils
/*   9:    */ {
/*  10:    */   public static boolean isAssignable(Type lhsType, Type rhsType)
/*  11:    */   {
/*  12: 45 */     Assert.notNull(lhsType, "Left-hand side type must not be null");
/*  13: 46 */     Assert.notNull(rhsType, "Right-hand side type must not be null");
/*  14: 49 */     if ((lhsType.equals(rhsType)) || (lhsType.equals(Object.class))) {
/*  15: 50 */       return true;
/*  16:    */     }
/*  17: 53 */     if ((lhsType instanceof Class))
/*  18:    */     {
/*  19: 54 */       Class<?> lhsClass = (Class)lhsType;
/*  20: 57 */       if ((rhsType instanceof Class)) {
/*  21: 58 */         return ClassUtils.isAssignable(lhsClass, (Class)rhsType);
/*  22:    */       }
/*  23: 61 */       if ((rhsType instanceof ParameterizedType))
/*  24:    */       {
/*  25: 62 */         Type rhsRaw = ((ParameterizedType)rhsType).getRawType();
/*  26: 65 */         if ((rhsRaw instanceof Class)) {
/*  27: 66 */           return ClassUtils.isAssignable(lhsClass, (Class)rhsRaw);
/*  28:    */         }
/*  29:    */       }
/*  30: 69 */       else if ((lhsClass.isArray()) && ((rhsType instanceof GenericArrayType)))
/*  31:    */       {
/*  32: 70 */         Type rhsComponent = ((GenericArrayType)rhsType).getGenericComponentType();
/*  33:    */         
/*  34: 72 */         return isAssignable(lhsClass.getComponentType(), rhsComponent);
/*  35:    */       }
/*  36:    */     }
/*  37: 77 */     if ((lhsType instanceof ParameterizedType)) {
/*  38: 78 */       if ((rhsType instanceof Class))
/*  39:    */       {
/*  40: 79 */         Type lhsRaw = ((ParameterizedType)lhsType).getRawType();
/*  41: 81 */         if ((lhsRaw instanceof Class)) {
/*  42: 82 */           return ClassUtils.isAssignable((Class)lhsRaw, (Class)rhsType);
/*  43:    */         }
/*  44:    */       }
/*  45: 85 */       else if ((rhsType instanceof ParameterizedType))
/*  46:    */       {
/*  47: 86 */         return isAssignable((ParameterizedType)lhsType, (ParameterizedType)rhsType);
/*  48:    */       }
/*  49:    */     }
/*  50: 90 */     if ((lhsType instanceof GenericArrayType))
/*  51:    */     {
/*  52: 91 */       Type lhsComponent = ((GenericArrayType)lhsType).getGenericComponentType();
/*  53: 93 */       if ((rhsType instanceof Class))
/*  54:    */       {
/*  55: 94 */         Class<?> rhsClass = (Class)rhsType;
/*  56: 96 */         if (rhsClass.isArray()) {
/*  57: 97 */           return isAssignable(lhsComponent, rhsClass.getComponentType());
/*  58:    */         }
/*  59:    */       }
/*  60:100 */       else if ((rhsType instanceof GenericArrayType))
/*  61:    */       {
/*  62:101 */         Type rhsComponent = ((GenericArrayType)rhsType).getGenericComponentType();
/*  63:    */         
/*  64:103 */         return isAssignable(lhsComponent, rhsComponent);
/*  65:    */       }
/*  66:    */     }
/*  67:107 */     if ((lhsType instanceof WildcardType)) {
/*  68:108 */       return isAssignable((WildcardType)lhsType, rhsType);
/*  69:    */     }
/*  70:111 */     return false;
/*  71:    */   }
/*  72:    */   
/*  73:    */   private static boolean isAssignable(ParameterizedType lhsType, ParameterizedType rhsType)
/*  74:    */   {
/*  75:115 */     if (lhsType.equals(rhsType)) {
/*  76:116 */       return true;
/*  77:    */     }
/*  78:119 */     Type[] lhsTypeArguments = lhsType.getActualTypeArguments();
/*  79:120 */     Type[] rhsTypeArguments = rhsType.getActualTypeArguments();
/*  80:122 */     if (lhsTypeArguments.length != rhsTypeArguments.length) {
/*  81:123 */       return false;
/*  82:    */     }
/*  83:126 */     int size = lhsTypeArguments.length;
/*  84:126 */     for (int i = 0; i < size; i++)
/*  85:    */     {
/*  86:127 */       Type lhsArg = lhsTypeArguments[i];
/*  87:128 */       Type rhsArg = rhsTypeArguments[i];
/*  88:130 */       if ((!lhsArg.equals(rhsArg)) && (
/*  89:131 */         (!(lhsArg instanceof WildcardType)) || (!isAssignable((WildcardType)lhsArg, rhsArg)))) {
/*  90:132 */         return false;
/*  91:    */       }
/*  92:    */     }
/*  93:136 */     return true;
/*  94:    */   }
/*  95:    */   
/*  96:    */   private static boolean isAssignable(WildcardType lhsType, Type rhsType)
/*  97:    */   {
/*  98:140 */     Type[] lUpperBounds = lhsType.getUpperBounds();
/*  99:143 */     if (lUpperBounds.length == 0) {
/* 100:144 */       lUpperBounds = new Type[] { Object.class };
/* 101:    */     }
/* 102:147 */     Type[] lLowerBounds = lhsType.getLowerBounds();
/* 103:150 */     if (lLowerBounds.length == 0) {
/* 104:151 */       lLowerBounds = new Type[1];
/* 105:    */     }
/* 106:    */     Type[] rUpperBounds;
/* 107:    */     Type[] rLowerBounds;
/* 108:    */     Type lBound;
/* 109:154 */     if ((rhsType instanceof WildcardType))
/* 110:    */     {
/* 111:158 */       WildcardType rhsWcType = (WildcardType)rhsType;
/* 112:159 */       rUpperBounds = rhsWcType.getUpperBounds();
/* 113:161 */       if (rUpperBounds.length == 0) {
/* 114:162 */         rUpperBounds = new Type[] { Object.class };
/* 115:    */       }
/* 116:165 */       rLowerBounds = rhsWcType.getLowerBounds();
/* 117:167 */       if (rLowerBounds.length == 0) {
/* 118:168 */         rLowerBounds = new Type[1];
/* 119:    */       }
/* 120:171 */       for (Type lBound : lUpperBounds)
/* 121:    */       {
/* 122:172 */         for (Type rBound : rUpperBounds) {
/* 123:173 */           if (!isAssignableBound(lBound, rBound)) {
/* 124:174 */             return false;
/* 125:    */           }
/* 126:    */         }
/* 127:178 */         for (Type rBound : rLowerBounds) {
/* 128:179 */           if (!isAssignableBound(lBound, rBound)) {
/* 129:180 */             return false;
/* 130:    */           }
/* 131:    */         }
/* 132:    */       }
/* 133:185 */       for (lBound : lLowerBounds)
/* 134:    */       {
/* 135:186 */         for (Type rBound : rUpperBounds) {
/* 136:187 */           if (!isAssignableBound(rBound, lBound)) {
/* 137:188 */             return false;
/* 138:    */           }
/* 139:    */         }
/* 140:192 */         for (Type rBound : rLowerBounds) {
/* 141:193 */           if (!isAssignableBound(rBound, lBound)) {
/* 142:194 */             return false;
/* 143:    */           }
/* 144:    */         }
/* 145:    */       }
/* 146:    */     }
/* 147:    */     else
/* 148:    */     {
/* 149:200 */       rLowerBounds = (lBound = lUpperBounds).length;
/* 150:200 */       for (rUpperBounds = 0; rUpperBounds < rLowerBounds; rUpperBounds++)
/* 151:    */       {
/* 152:200 */         Type lBound = lBound[rUpperBounds];
/* 153:201 */         if (!isAssignableBound(lBound, rhsType)) {
/* 154:202 */           return false;
/* 155:    */         }
/* 156:    */       }
/* 157:206 */       rLowerBounds = (lBound = lLowerBounds).length;
/* 158:206 */       for (rUpperBounds = 0; rUpperBounds < rLowerBounds; rUpperBounds++)
/* 159:    */       {
/* 160:206 */         Type lBound = lBound[rUpperBounds];
/* 161:207 */         if (!isAssignableBound(rhsType, lBound)) {
/* 162:208 */           return false;
/* 163:    */         }
/* 164:    */       }
/* 165:    */     }
/* 166:213 */     return true;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public static boolean isAssignableBound(Type lhsType, Type rhsType)
/* 170:    */   {
/* 171:217 */     if (rhsType == null) {
/* 172:218 */       return true;
/* 173:    */     }
/* 174:221 */     if (lhsType == null) {
/* 175:222 */       return false;
/* 176:    */     }
/* 177:224 */     return isAssignable(lhsType, rhsType);
/* 178:    */   }
/* 179:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.TypeUtils
 * JD-Core Version:    0.7.0.1
 */