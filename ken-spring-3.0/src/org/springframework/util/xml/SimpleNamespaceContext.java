/*   1:    */ package org.springframework.util.xml;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Map.Entry;
/*  10:    */ import java.util.Set;
/*  11:    */ import javax.xml.namespace.NamespaceContext;
/*  12:    */ import org.springframework.util.Assert;
/*  13:    */ 
/*  14:    */ public class SimpleNamespaceContext
/*  15:    */   implements NamespaceContext
/*  16:    */ {
/*  17: 40 */   private Map<String, String> prefixToNamespaceUri = new HashMap();
/*  18: 42 */   private Map<String, List<String>> namespaceUriToPrefixes = new HashMap();
/*  19: 44 */   private String defaultNamespaceUri = "";
/*  20:    */   
/*  21:    */   public String getNamespaceURI(String prefix)
/*  22:    */   {
/*  23: 47 */     Assert.notNull(prefix, "prefix is null");
/*  24: 48 */     if ("xml".equals(prefix)) {
/*  25: 49 */       return "http://www.w3.org/XML/1998/namespace";
/*  26:    */     }
/*  27: 51 */     if ("xmlns".equals(prefix)) {
/*  28: 52 */       return "http://www.w3.org/2000/xmlns/";
/*  29:    */     }
/*  30: 54 */     if ("".equals(prefix)) {
/*  31: 55 */       return this.defaultNamespaceUri;
/*  32:    */     }
/*  33: 57 */     if (this.prefixToNamespaceUri.containsKey(prefix)) {
/*  34: 58 */       return (String)this.prefixToNamespaceUri.get(prefix);
/*  35:    */     }
/*  36: 60 */     return "";
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getPrefix(String namespaceUri)
/*  40:    */   {
/*  41: 64 */     List prefixes = getPrefixesInternal(namespaceUri);
/*  42: 65 */     return prefixes.isEmpty() ? null : (String)prefixes.get(0);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public Iterator getPrefixes(String namespaceUri)
/*  46:    */   {
/*  47: 69 */     return getPrefixesInternal(namespaceUri).iterator();
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setBindings(Map<String, String> bindings)
/*  51:    */   {
/*  52: 78 */     for (Map.Entry<String, String> entry : bindings.entrySet()) {
/*  53: 79 */       bindNamespaceUri((String)entry.getKey(), (String)entry.getValue());
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void bindDefaultNamespaceUri(String namespaceUri)
/*  58:    */   {
/*  59: 89 */     bindNamespaceUri("", namespaceUri);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void bindNamespaceUri(String prefix, String namespaceUri)
/*  63:    */   {
/*  64: 99 */     Assert.notNull(prefix, "No prefix given");
/*  65:100 */     Assert.notNull(namespaceUri, "No namespaceUri given");
/*  66:101 */     if ("".equals(prefix))
/*  67:    */     {
/*  68:102 */       this.defaultNamespaceUri = namespaceUri;
/*  69:    */     }
/*  70:    */     else
/*  71:    */     {
/*  72:105 */       this.prefixToNamespaceUri.put(prefix, namespaceUri);
/*  73:106 */       getPrefixesInternal(namespaceUri).add(prefix);
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void clear()
/*  78:    */   {
/*  79:112 */     this.prefixToNamespaceUri.clear();
/*  80:    */   }
/*  81:    */   
/*  82:    */   public Iterator<String> getBoundPrefixes()
/*  83:    */   {
/*  84:121 */     return this.prefixToNamespaceUri.keySet().iterator();
/*  85:    */   }
/*  86:    */   
/*  87:    */   private List<String> getPrefixesInternal(String namespaceUri)
/*  88:    */   {
/*  89:125 */     if (this.defaultNamespaceUri.equals(namespaceUri)) {
/*  90:126 */       return Collections.singletonList("");
/*  91:    */     }
/*  92:128 */     if ("http://www.w3.org/XML/1998/namespace".equals(namespaceUri)) {
/*  93:129 */       return Collections.singletonList("xml");
/*  94:    */     }
/*  95:131 */     if ("http://www.w3.org/2000/xmlns/".equals(namespaceUri)) {
/*  96:132 */       return Collections.singletonList("xmlns");
/*  97:    */     }
/*  98:135 */     List<String> list = (List)this.namespaceUriToPrefixes.get(namespaceUri);
/*  99:136 */     if (list == null)
/* 100:    */     {
/* 101:137 */       list = new ArrayList();
/* 102:138 */       this.namespaceUriToPrefixes.put(namespaceUri, list);
/* 103:    */     }
/* 104:140 */     return list;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void removeBinding(String prefix)
/* 108:    */   {
/* 109:150 */     if ("".equals(prefix))
/* 110:    */     {
/* 111:151 */       this.defaultNamespaceUri = "";
/* 112:    */     }
/* 113:    */     else
/* 114:    */     {
/* 115:154 */       String namespaceUri = (String)this.prefixToNamespaceUri.remove(prefix);
/* 116:155 */       List prefixes = getPrefixesInternal(namespaceUri);
/* 117:156 */       prefixes.remove(prefix);
/* 118:    */     }
/* 119:    */   }
/* 120:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.SimpleNamespaceContext
 * JD-Core Version:    0.7.0.1
 */