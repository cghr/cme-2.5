/*   1:    */ package org.springframework.beans.support;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Date;
/*   6:    */ import java.util.List;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ 
/*   9:    */ public class PagedListHolder<E>
/*  10:    */   implements Serializable
/*  11:    */ {
/*  12:    */   public static final int DEFAULT_PAGE_SIZE = 10;
/*  13:    */   public static final int DEFAULT_MAX_LINKED_PAGES = 10;
/*  14:    */   private List<E> source;
/*  15:    */   private Date refreshDate;
/*  16:    */   private SortDefinition sort;
/*  17:    */   private SortDefinition sortUsed;
/*  18: 65 */   private int pageSize = 10;
/*  19: 67 */   private int page = 0;
/*  20:    */   private boolean newPageSet;
/*  21: 71 */   private int maxLinkedPages = 10;
/*  22:    */   
/*  23:    */   public PagedListHolder()
/*  24:    */   {
/*  25: 80 */     this(new ArrayList(0));
/*  26:    */   }
/*  27:    */   
/*  28:    */   public PagedListHolder(List<E> source)
/*  29:    */   {
/*  30: 90 */     this(source, new MutableSortDefinition(true));
/*  31:    */   }
/*  32:    */   
/*  33:    */   public PagedListHolder(List<E> source, SortDefinition sort)
/*  34:    */   {
/*  35: 99 */     setSource(source);
/*  36:100 */     setSort(sort);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setSource(List<E> source)
/*  40:    */   {
/*  41:108 */     Assert.notNull(source, "Source List must not be null");
/*  42:109 */     this.source = source;
/*  43:110 */     this.refreshDate = new Date();
/*  44:111 */     this.sortUsed = null;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public List<E> getSource()
/*  48:    */   {
/*  49:118 */     return this.source;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public Date getRefreshDate()
/*  53:    */   {
/*  54:125 */     return this.refreshDate;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setSort(SortDefinition sort)
/*  58:    */   {
/*  59:134 */     this.sort = sort;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public SortDefinition getSort()
/*  63:    */   {
/*  64:141 */     return this.sort;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setPageSize(int pageSize)
/*  68:    */   {
/*  69:150 */     if (pageSize != this.pageSize)
/*  70:    */     {
/*  71:151 */       this.pageSize = pageSize;
/*  72:152 */       if (!this.newPageSet) {
/*  73:153 */         this.page = 0;
/*  74:    */       }
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   public int getPageSize()
/*  79:    */   {
/*  80:162 */     return this.pageSize;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setPage(int page)
/*  84:    */   {
/*  85:170 */     this.page = page;
/*  86:171 */     this.newPageSet = true;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public int getPage()
/*  90:    */   {
/*  91:179 */     this.newPageSet = false;
/*  92:180 */     if (this.page >= getPageCount()) {
/*  93:181 */       this.page = (getPageCount() - 1);
/*  94:    */     }
/*  95:183 */     return this.page;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setMaxLinkedPages(int maxLinkedPages)
/*  99:    */   {
/* 100:190 */     this.maxLinkedPages = maxLinkedPages;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public int getMaxLinkedPages()
/* 104:    */   {
/* 105:197 */     return this.maxLinkedPages;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public int getPageCount()
/* 109:    */   {
/* 110:205 */     float nrOfPages = getNrOfElements() / getPageSize();
/* 111:206 */     return (int)((nrOfPages > (int)nrOfPages) || (nrOfPages == 0.0D) ? nrOfPages + 1.0F : nrOfPages);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public boolean isFirstPage()
/* 115:    */   {
/* 116:213 */     return getPage() == 0;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public boolean isLastPage()
/* 120:    */   {
/* 121:220 */     return getPage() == getPageCount() - 1;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void previousPage()
/* 125:    */   {
/* 126:228 */     if (!isFirstPage()) {
/* 127:229 */       this.page -= 1;
/* 128:    */     }
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void nextPage()
/* 132:    */   {
/* 133:238 */     if (!isLastPage()) {
/* 134:239 */       this.page += 1;
/* 135:    */     }
/* 136:    */   }
/* 137:    */   
/* 138:    */   public int getNrOfElements()
/* 139:    */   {
/* 140:247 */     return getSource().size();
/* 141:    */   }
/* 142:    */   
/* 143:    */   public int getFirstElementOnPage()
/* 144:    */   {
/* 145:255 */     return getPageSize() * getPage();
/* 146:    */   }
/* 147:    */   
/* 148:    */   public int getLastElementOnPage()
/* 149:    */   {
/* 150:263 */     int endIndex = getPageSize() * (getPage() + 1);
/* 151:264 */     int size = getNrOfElements();
/* 152:265 */     return (endIndex > size ? size : endIndex) - 1;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public List<E> getPageList()
/* 156:    */   {
/* 157:272 */     return getSource().subList(getFirstElementOnPage(), getLastElementOnPage() + 1);
/* 158:    */   }
/* 159:    */   
/* 160:    */   public int getFirstLinkedPage()
/* 161:    */   {
/* 162:279 */     return Math.max(0, getPage() - getMaxLinkedPages() / 2);
/* 163:    */   }
/* 164:    */   
/* 165:    */   public int getLastLinkedPage()
/* 166:    */   {
/* 167:286 */     return Math.min(getFirstLinkedPage() + getMaxLinkedPages() - 1, getPageCount() - 1);
/* 168:    */   }
/* 169:    */   
/* 170:    */   public void resort()
/* 171:    */   {
/* 172:297 */     SortDefinition sort = getSort();
/* 173:298 */     if ((sort != null) && (!sort.equals(this.sortUsed)))
/* 174:    */     {
/* 175:299 */       this.sortUsed = copySortDefinition(sort);
/* 176:300 */       doSort(getSource(), sort);
/* 177:301 */       setPage(0);
/* 178:    */     }
/* 179:    */   }
/* 180:    */   
/* 181:    */   protected SortDefinition copySortDefinition(SortDefinition sort)
/* 182:    */   {
/* 183:318 */     return new MutableSortDefinition(sort);
/* 184:    */   }
/* 185:    */   
/* 186:    */   protected void doSort(List<E> source, SortDefinition sort)
/* 187:    */   {
/* 188:329 */     PropertyComparator.sort(source, sort);
/* 189:    */   }
/* 190:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.support.PagedListHolder
 * JD-Core Version:    0.7.0.1
 */