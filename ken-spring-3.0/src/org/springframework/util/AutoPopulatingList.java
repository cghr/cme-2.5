/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.lang.reflect.Modifier;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Collection;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.ListIterator;
/*  10:    */ 
/*  11:    */ public class AutoPopulatingList<E>
/*  12:    */   implements List<E>, Serializable
/*  13:    */ {
/*  14:    */   private final List<E> backingList;
/*  15:    */   private final ElementFactory<E> elementFactory;
/*  16:    */   
/*  17:    */   public AutoPopulatingList(Class<? extends E> elementClass)
/*  18:    */   {
/*  19: 62 */     this(new ArrayList(), elementClass);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public AutoPopulatingList(List<E> backingList, Class<? extends E> elementClass)
/*  23:    */   {
/*  24: 71 */     this(backingList, new ReflectiveElementFactory(elementClass));
/*  25:    */   }
/*  26:    */   
/*  27:    */   public AutoPopulatingList(ElementFactory<E> elementFactory)
/*  28:    */   {
/*  29: 79 */     this(new ArrayList(), elementFactory);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public AutoPopulatingList(List<E> backingList, ElementFactory<E> elementFactory)
/*  33:    */   {
/*  34: 87 */     Assert.notNull(backingList, "Backing List must not be null");
/*  35: 88 */     Assert.notNull(elementFactory, "Element factory must not be null");
/*  36: 89 */     this.backingList = backingList;
/*  37: 90 */     this.elementFactory = elementFactory;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void add(int index, E element)
/*  41:    */   {
/*  42: 95 */     this.backingList.add(index, element);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public boolean add(E o)
/*  46:    */   {
/*  47: 99 */     return this.backingList.add(o);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public boolean addAll(Collection<? extends E> c)
/*  51:    */   {
/*  52:103 */     return this.backingList.addAll(c);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public boolean addAll(int index, Collection<? extends E> c)
/*  56:    */   {
/*  57:107 */     return this.backingList.addAll(index, c);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void clear()
/*  61:    */   {
/*  62:111 */     this.backingList.clear();
/*  63:    */   }
/*  64:    */   
/*  65:    */   public boolean contains(Object o)
/*  66:    */   {
/*  67:115 */     return this.backingList.contains(o);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public boolean containsAll(Collection c)
/*  71:    */   {
/*  72:119 */     return this.backingList.containsAll(c);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public E get(int index)
/*  76:    */   {
/*  77:127 */     int backingListSize = this.backingList.size();
/*  78:128 */     E element = null;
/*  79:129 */     if (index < backingListSize)
/*  80:    */     {
/*  81:130 */       element = this.backingList.get(index);
/*  82:131 */       if (element == null)
/*  83:    */       {
/*  84:132 */         element = this.elementFactory.createElement(index);
/*  85:133 */         this.backingList.set(index, element);
/*  86:    */       }
/*  87:    */     }
/*  88:    */     else
/*  89:    */     {
/*  90:137 */       for (int x = backingListSize; x < index; x++) {
/*  91:138 */         this.backingList.add(null);
/*  92:    */       }
/*  93:140 */       element = this.elementFactory.createElement(index);
/*  94:141 */       this.backingList.add(element);
/*  95:    */     }
/*  96:143 */     return element;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public int indexOf(Object o)
/* 100:    */   {
/* 101:147 */     return this.backingList.indexOf(o);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public boolean isEmpty()
/* 105:    */   {
/* 106:151 */     return this.backingList.isEmpty();
/* 107:    */   }
/* 108:    */   
/* 109:    */   public Iterator<E> iterator()
/* 110:    */   {
/* 111:155 */     return this.backingList.iterator();
/* 112:    */   }
/* 113:    */   
/* 114:    */   public int lastIndexOf(Object o)
/* 115:    */   {
/* 116:159 */     return this.backingList.lastIndexOf(o);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public ListIterator<E> listIterator()
/* 120:    */   {
/* 121:163 */     return this.backingList.listIterator();
/* 122:    */   }
/* 123:    */   
/* 124:    */   public ListIterator<E> listIterator(int index)
/* 125:    */   {
/* 126:167 */     return this.backingList.listIterator(index);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public E remove(int index)
/* 130:    */   {
/* 131:171 */     return this.backingList.remove(index);
/* 132:    */   }
/* 133:    */   
/* 134:    */   public boolean remove(Object o)
/* 135:    */   {
/* 136:175 */     return this.backingList.remove(o);
/* 137:    */   }
/* 138:    */   
/* 139:    */   public boolean removeAll(Collection<?> c)
/* 140:    */   {
/* 141:179 */     return this.backingList.removeAll(c);
/* 142:    */   }
/* 143:    */   
/* 144:    */   public boolean retainAll(Collection<?> c)
/* 145:    */   {
/* 146:183 */     return this.backingList.retainAll(c);
/* 147:    */   }
/* 148:    */   
/* 149:    */   public E set(int index, E element)
/* 150:    */   {
/* 151:187 */     return this.backingList.set(index, element);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public int size()
/* 155:    */   {
/* 156:191 */     return this.backingList.size();
/* 157:    */   }
/* 158:    */   
/* 159:    */   public List<E> subList(int fromIndex, int toIndex)
/* 160:    */   {
/* 161:195 */     return this.backingList.subList(fromIndex, toIndex);
/* 162:    */   }
/* 163:    */   
/* 164:    */   public Object[] toArray()
/* 165:    */   {
/* 166:199 */     return this.backingList.toArray();
/* 167:    */   }
/* 168:    */   
/* 169:    */   public <T> T[] toArray(T[] a)
/* 170:    */   {
/* 171:203 */     return this.backingList.toArray(a);
/* 172:    */   }
/* 173:    */   
/* 174:    */   public boolean equals(Object other)
/* 175:    */   {
/* 176:209 */     return this.backingList.equals(other);
/* 177:    */   }
/* 178:    */   
/* 179:    */   public int hashCode()
/* 180:    */   {
/* 181:214 */     return this.backingList.hashCode();
/* 182:    */   }
/* 183:    */   
/* 184:    */   public static abstract interface ElementFactory<E>
/* 185:    */   {
/* 186:    */     public abstract E createElement(int paramInt)
/* 187:    */       throws AutoPopulatingList.ElementInstantiationException;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public static class ElementInstantiationException
/* 191:    */     extends RuntimeException
/* 192:    */   {
/* 193:    */     public ElementInstantiationException(String msg)
/* 194:    */     {
/* 195:240 */       super();
/* 196:    */     }
/* 197:    */   }
/* 198:    */   
/* 199:    */   private static class ReflectiveElementFactory<E>
/* 200:    */     implements AutoPopulatingList.ElementFactory<E>, Serializable
/* 201:    */   {
/* 202:    */     private final Class<? extends E> elementClass;
/* 203:    */     
/* 204:    */     public ReflectiveElementFactory(Class<? extends E> elementClass)
/* 205:    */     {
/* 206:255 */       Assert.notNull(elementClass, "Element clas must not be null");
/* 207:256 */       Assert.isTrue(!elementClass.isInterface(), "Element class must not be an interface type");
/* 208:257 */       Assert.isTrue(!Modifier.isAbstract(elementClass.getModifiers()), "Element class cannot be an abstract class");
/* 209:258 */       this.elementClass = elementClass;
/* 210:    */     }
/* 211:    */     
/* 212:    */     public E createElement(int index)
/* 213:    */     {
/* 214:    */       try
/* 215:    */       {
/* 216:263 */         return this.elementClass.newInstance();
/* 217:    */       }
/* 218:    */       catch (InstantiationException ex)
/* 219:    */       {
/* 220:266 */         throw new AutoPopulatingList.ElementInstantiationException("Unable to instantiate element class [" + 
/* 221:267 */           this.elementClass.getName() + "]. Root cause is " + ex);
/* 222:    */       }
/* 223:    */       catch (IllegalAccessException ex)
/* 224:    */       {
/* 225:270 */         throw new AutoPopulatingList.ElementInstantiationException("Cannot access element class [" + 
/* 226:271 */           this.elementClass.getName() + "]. Root cause is " + ex);
/* 227:    */       }
/* 228:    */     }
/* 229:    */   }
/* 230:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.AutoPopulatingList
 * JD-Core Version:    0.7.0.1
 */