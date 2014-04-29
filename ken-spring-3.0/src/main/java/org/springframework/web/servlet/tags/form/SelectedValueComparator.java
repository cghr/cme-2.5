/*   1:    */ package org.springframework.web.servlet.tags.form;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditor;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Map;
/*   7:    */ import org.springframework.core.enums.LabeledEnum;
/*   8:    */ import org.springframework.util.CollectionUtils;
/*   9:    */ import org.springframework.util.ObjectUtils;
/*  10:    */ import org.springframework.web.servlet.support.BindStatus;
/*  11:    */ 
/*  12:    */ abstract class SelectedValueComparator
/*  13:    */ {
/*  14:    */   public static boolean isSelected(BindStatus bindStatus, Object candidateValue)
/*  15:    */   {
/*  16: 68 */     if (bindStatus == null) {
/*  17: 69 */       return candidateValue == null;
/*  18:    */     }
/*  19: 74 */     Object boundValue = bindStatus.getValue();
/*  20: 75 */     if (ObjectUtils.nullSafeEquals(boundValue, candidateValue)) {
/*  21: 76 */       return true;
/*  22:    */     }
/*  23: 78 */     Object actualValue = bindStatus.getActualValue();
/*  24: 79 */     if ((actualValue != null) && (actualValue != boundValue) && 
/*  25: 80 */       (ObjectUtils.nullSafeEquals(actualValue, candidateValue))) {
/*  26: 81 */       return true;
/*  27:    */     }
/*  28: 83 */     if (actualValue != null) {
/*  29: 84 */       boundValue = actualValue;
/*  30: 86 */     } else if (boundValue == null) {
/*  31: 87 */       return false;
/*  32:    */     }
/*  33: 92 */     boolean selected = false;
/*  34: 93 */     if (boundValue.getClass().isArray()) {
/*  35: 94 */       selected = collectionCompare(CollectionUtils.arrayToList(boundValue), candidateValue, bindStatus);
/*  36: 96 */     } else if ((boundValue instanceof Collection)) {
/*  37: 97 */       selected = collectionCompare((Collection)boundValue, candidateValue, bindStatus);
/*  38: 99 */     } else if ((boundValue instanceof Map)) {
/*  39:100 */       selected = mapCompare((Map)boundValue, candidateValue, bindStatus);
/*  40:    */     }
/*  41:102 */     if (!selected) {
/*  42:103 */       selected = exhaustiveCompare(boundValue, candidateValue, bindStatus.getEditor(), null);
/*  43:    */     }
/*  44:105 */     return selected;
/*  45:    */   }
/*  46:    */   
/*  47:    */   private static boolean collectionCompare(Collection boundCollection, Object candidateValue, BindStatus bindStatus)
/*  48:    */   {
/*  49:    */     try
/*  50:    */     {
/*  51:110 */       if (boundCollection.contains(candidateValue)) {
/*  52:111 */         return true;
/*  53:    */       }
/*  54:    */     }
/*  55:    */     catch (ClassCastException localClassCastException) {}
/*  56:117 */     return exhaustiveCollectionCompare(boundCollection, candidateValue, bindStatus);
/*  57:    */   }
/*  58:    */   
/*  59:    */   private static boolean mapCompare(Map boundMap, Object candidateValue, BindStatus bindStatus)
/*  60:    */   {
/*  61:    */     try
/*  62:    */     {
/*  63:122 */       if (boundMap.containsKey(candidateValue)) {
/*  64:123 */         return true;
/*  65:    */       }
/*  66:    */     }
/*  67:    */     catch (ClassCastException localClassCastException) {}
/*  68:129 */     return exhaustiveCollectionCompare((Collection)boundMap.keySet(), candidateValue, bindStatus);
/*  69:    */   }
/*  70:    */   
/*  71:    */   private static boolean exhaustiveCollectionCompare(Collection collection, Object candidateValue, BindStatus bindStatus)
/*  72:    */   {
/*  73:135 */     Map<PropertyEditor, Object> convertedValueCache = new HashMap(1);
/*  74:136 */     PropertyEditor editor = null;
/*  75:137 */     boolean candidateIsString = candidateValue instanceof String;
/*  76:138 */     if (!candidateIsString) {
/*  77:139 */       editor = bindStatus.findEditor(candidateValue.getClass());
/*  78:    */     }
/*  79:141 */     for (Object element : collection)
/*  80:    */     {
/*  81:142 */       if ((editor == null) && (element != null) && (candidateIsString)) {
/*  82:143 */         editor = bindStatus.findEditor(element.getClass());
/*  83:    */       }
/*  84:145 */       if (exhaustiveCompare(element, candidateValue, editor, convertedValueCache)) {
/*  85:146 */         return true;
/*  86:    */       }
/*  87:    */     }
/*  88:149 */     return false;
/*  89:    */   }
/*  90:    */   
/*  91:    */   private static boolean exhaustiveCompare(Object boundValue, Object candidate, PropertyEditor editor, Map<PropertyEditor, Object> convertedValueCache)
/*  92:    */   {
/*  93:155 */     String candidateDisplayString = ValueFormatter.getDisplayString(candidate, editor, false);
/*  94:156 */     if ((boundValue instanceof LabeledEnum))
/*  95:    */     {
/*  96:157 */       LabeledEnum labeledEnum = (LabeledEnum)boundValue;
/*  97:158 */       String enumCodeAsString = ObjectUtils.getDisplayString(labeledEnum.getCode());
/*  98:159 */       if (enumCodeAsString.equals(candidateDisplayString)) {
/*  99:160 */         return true;
/* 100:    */       }
/* 101:162 */       String enumLabelAsString = ObjectUtils.getDisplayString(labeledEnum.getLabel());
/* 102:163 */       if (enumLabelAsString.equals(candidateDisplayString)) {
/* 103:164 */         return true;
/* 104:    */       }
/* 105:    */     }
/* 106:167 */     else if (boundValue.getClass().isEnum())
/* 107:    */     {
/* 108:168 */       Enum boundEnum = (Enum)boundValue;
/* 109:169 */       String enumCodeAsString = ObjectUtils.getDisplayString(boundEnum.name());
/* 110:170 */       if (enumCodeAsString.equals(candidateDisplayString)) {
/* 111:171 */         return true;
/* 112:    */       }
/* 113:173 */       String enumLabelAsString = ObjectUtils.getDisplayString(boundEnum.toString());
/* 114:174 */       if (enumLabelAsString.equals(candidateDisplayString)) {
/* 115:175 */         return true;
/* 116:    */       }
/* 117:    */     }
/* 118:    */     else
/* 119:    */     {
/* 120:178 */       if (ObjectUtils.getDisplayString(boundValue).equals(candidateDisplayString)) {
/* 121:179 */         return true;
/* 122:    */       }
/* 123:181 */       if ((editor != null) && ((candidate instanceof String)))
/* 124:    */       {
/* 125:183 */         String candidateAsString = (String)candidate;
/* 126:    */         Object candidateAsValue;
/* 127:    */         Object candidateAsValue;
/* 128:185 */         if ((convertedValueCache != null) && (convertedValueCache.containsKey(editor)))
/* 129:    */         {
/* 130:186 */           candidateAsValue = convertedValueCache.get(editor);
/* 131:    */         }
/* 132:    */         else
/* 133:    */         {
/* 134:189 */           editor.setAsText(candidateAsString);
/* 135:190 */           candidateAsValue = editor.getValue();
/* 136:191 */           if (convertedValueCache != null) {
/* 137:192 */             convertedValueCache.put(editor, candidateAsValue);
/* 138:    */           }
/* 139:    */         }
/* 140:195 */         if (ObjectUtils.nullSafeEquals(boundValue, candidateAsValue)) {
/* 141:196 */           return true;
/* 142:    */         }
/* 143:    */       }
/* 144:    */     }
/* 145:199 */     return false;
/* 146:    */   }
/* 147:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.SelectedValueComparator
 * JD-Core Version:    0.7.0.1
 */