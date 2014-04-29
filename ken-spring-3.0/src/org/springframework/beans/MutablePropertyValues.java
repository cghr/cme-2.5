/*   1:    */ package org.springframework.beans;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.HashSet;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Map.Entry;
/*   9:    */ import java.util.Set;
/*  10:    */ import org.springframework.util.StringUtils;
/*  11:    */ 
/*  12:    */ public class MutablePropertyValues
/*  13:    */   implements PropertyValues, Serializable
/*  14:    */ {
/*  15:    */   private final List<PropertyValue> propertyValueList;
/*  16:    */   private Set<String> processedProperties;
/*  17: 44 */   private volatile boolean converted = false;
/*  18:    */   
/*  19:    */   public MutablePropertyValues()
/*  20:    */   {
/*  21: 53 */     this.propertyValueList = new ArrayList(0);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public MutablePropertyValues(PropertyValues original)
/*  25:    */   {
/*  26: 66 */     if (original != null)
/*  27:    */     {
/*  28: 67 */       PropertyValue[] pvs = original.getPropertyValues();
/*  29: 68 */       this.propertyValueList = new ArrayList(pvs.length);
/*  30: 69 */       for (PropertyValue pv : pvs) {
/*  31: 70 */         this.propertyValueList.add(new PropertyValue(pv));
/*  32:    */       }
/*  33:    */     }
/*  34:    */     else
/*  35:    */     {
/*  36: 74 */       this.propertyValueList = new ArrayList(0);
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   public MutablePropertyValues(Map<?, ?> original)
/*  41:    */   {
/*  42: 86 */     if (original != null)
/*  43:    */     {
/*  44: 87 */       this.propertyValueList = new ArrayList(original.size());
/*  45: 88 */       for (Map.Entry entry : original.entrySet()) {
/*  46: 89 */         this.propertyValueList.add(new PropertyValue(entry.getKey().toString(), entry.getValue()));
/*  47:    */       }
/*  48:    */     }
/*  49:    */     else
/*  50:    */     {
/*  51: 93 */       this.propertyValueList = new ArrayList(0);
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   public MutablePropertyValues(List<PropertyValue> propertyValueList)
/*  56:    */   {
/*  57:105 */     this.propertyValueList = 
/*  58:106 */       (propertyValueList != null ? propertyValueList : new ArrayList());
/*  59:    */   }
/*  60:    */   
/*  61:    */   public List<PropertyValue> getPropertyValueList()
/*  62:    */   {
/*  63:117 */     return this.propertyValueList;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public int size()
/*  67:    */   {
/*  68:124 */     return this.propertyValueList.size();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public MutablePropertyValues addPropertyValues(PropertyValues other)
/*  72:    */   {
/*  73:135 */     if (other != null)
/*  74:    */     {
/*  75:136 */       PropertyValue[] pvs = other.getPropertyValues();
/*  76:137 */       for (PropertyValue pv : pvs) {
/*  77:138 */         addPropertyValue(new PropertyValue(pv));
/*  78:    */       }
/*  79:    */     }
/*  80:141 */     return this;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public MutablePropertyValues addPropertyValues(Map<?, ?> other)
/*  84:    */   {
/*  85:151 */     if (other != null) {
/*  86:152 */       for (Map.Entry<?, ?> entry : other.entrySet()) {
/*  87:153 */         addPropertyValue(new PropertyValue(entry.getKey().toString(), entry.getValue()));
/*  88:    */       }
/*  89:    */     }
/*  90:156 */     return this;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public MutablePropertyValues addPropertyValue(PropertyValue pv)
/*  94:    */   {
/*  95:166 */     for (int i = 0; i < this.propertyValueList.size(); i++)
/*  96:    */     {
/*  97:167 */       PropertyValue currentPv = (PropertyValue)this.propertyValueList.get(i);
/*  98:168 */       if (currentPv.getName().equals(pv.getName()))
/*  99:    */       {
/* 100:169 */         pv = mergeIfRequired(pv, currentPv);
/* 101:170 */         setPropertyValueAt(pv, i);
/* 102:171 */         return this;
/* 103:    */       }
/* 104:    */     }
/* 105:174 */     this.propertyValueList.add(pv);
/* 106:175 */     return this;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void addPropertyValue(String propertyName, Object propertyValue)
/* 110:    */   {
/* 111:188 */     addPropertyValue(new PropertyValue(propertyName, propertyValue));
/* 112:    */   }
/* 113:    */   
/* 114:    */   public MutablePropertyValues add(String propertyName, Object propertyValue)
/* 115:    */   {
/* 116:199 */     addPropertyValue(new PropertyValue(propertyName, propertyValue));
/* 117:200 */     return this;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void setPropertyValueAt(PropertyValue pv, int i)
/* 121:    */   {
/* 122:208 */     this.propertyValueList.set(i, pv);
/* 123:    */   }
/* 124:    */   
/* 125:    */   private PropertyValue mergeIfRequired(PropertyValue newPv, PropertyValue currentPv)
/* 126:    */   {
/* 127:217 */     Object value = newPv.getValue();
/* 128:218 */     if ((value instanceof Mergeable))
/* 129:    */     {
/* 130:219 */       Mergeable mergeable = (Mergeable)value;
/* 131:220 */       if (mergeable.isMergeEnabled())
/* 132:    */       {
/* 133:221 */         Object merged = mergeable.merge(currentPv.getValue());
/* 134:222 */         return new PropertyValue(newPv.getName(), merged);
/* 135:    */       }
/* 136:    */     }
/* 137:225 */     return newPv;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public void removePropertyValue(PropertyValue pv)
/* 141:    */   {
/* 142:233 */     this.propertyValueList.remove(pv);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void removePropertyValue(String propertyName)
/* 146:    */   {
/* 147:242 */     this.propertyValueList.remove(getPropertyValue(propertyName));
/* 148:    */   }
/* 149:    */   
/* 150:    */   public PropertyValue[] getPropertyValues()
/* 151:    */   {
/* 152:247 */     return (PropertyValue[])this.propertyValueList.toArray(new PropertyValue[this.propertyValueList.size()]);
/* 153:    */   }
/* 154:    */   
/* 155:    */   public PropertyValue getPropertyValue(String propertyName)
/* 156:    */   {
/* 157:251 */     for (PropertyValue pv : this.propertyValueList) {
/* 158:252 */       if (pv.getName().equals(propertyName)) {
/* 159:253 */         return pv;
/* 160:    */       }
/* 161:    */     }
/* 162:256 */     return null;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public PropertyValues changesSince(PropertyValues old)
/* 166:    */   {
/* 167:260 */     MutablePropertyValues changes = new MutablePropertyValues();
/* 168:261 */     if (old == this) {
/* 169:262 */       return changes;
/* 170:    */     }
/* 171:266 */     for (PropertyValue newPv : this.propertyValueList)
/* 172:    */     {
/* 173:268 */       PropertyValue pvOld = old.getPropertyValue(newPv.getName());
/* 174:269 */       if (pvOld == null) {
/* 175:270 */         changes.addPropertyValue(newPv);
/* 176:272 */       } else if (!pvOld.equals(newPv)) {
/* 177:274 */         changes.addPropertyValue(newPv);
/* 178:    */       }
/* 179:    */     }
/* 180:277 */     return changes;
/* 181:    */   }
/* 182:    */   
/* 183:    */   public boolean contains(String propertyName)
/* 184:    */   {
/* 185:282 */     return (getPropertyValue(propertyName) != null) || ((this.processedProperties != null) && (this.processedProperties.contains(propertyName)));
/* 186:    */   }
/* 187:    */   
/* 188:    */   public boolean isEmpty()
/* 189:    */   {
/* 190:286 */     return this.propertyValueList.isEmpty();
/* 191:    */   }
/* 192:    */   
/* 193:    */   public void registerProcessedProperty(String propertyName)
/* 194:    */   {
/* 195:299 */     if (this.processedProperties == null) {
/* 196:300 */       this.processedProperties = new HashSet();
/* 197:    */     }
/* 198:302 */     this.processedProperties.add(propertyName);
/* 199:    */   }
/* 200:    */   
/* 201:    */   public void setConverted()
/* 202:    */   {
/* 203:310 */     this.converted = true;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public boolean isConverted()
/* 207:    */   {
/* 208:318 */     return this.converted;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public boolean equals(Object other)
/* 212:    */   {
/* 213:324 */     if (this == other) {
/* 214:325 */       return true;
/* 215:    */     }
/* 216:327 */     if (!(other instanceof MutablePropertyValues)) {
/* 217:328 */       return false;
/* 218:    */     }
/* 219:330 */     MutablePropertyValues that = (MutablePropertyValues)other;
/* 220:331 */     return this.propertyValueList.equals(that.propertyValueList);
/* 221:    */   }
/* 222:    */   
/* 223:    */   public int hashCode()
/* 224:    */   {
/* 225:336 */     return this.propertyValueList.hashCode();
/* 226:    */   }
/* 227:    */   
/* 228:    */   public String toString()
/* 229:    */   {
/* 230:341 */     PropertyValue[] pvs = getPropertyValues();
/* 231:342 */     StringBuilder sb = new StringBuilder("PropertyValues: length=").append(pvs.length);
/* 232:343 */     if (pvs.length > 0) {
/* 233:344 */       sb.append("; ").append(StringUtils.arrayToDelimitedString(pvs, "; "));
/* 234:    */     }
/* 235:346 */     return sb.toString();
/* 236:    */   }
/* 237:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.MutablePropertyValues
 * JD-Core Version:    0.7.0.1
 */