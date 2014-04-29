/*  1:   */ package org.springframework.http.converter.feed;
/*  2:   */ 
/*  3:   */ import com.sun.syndication.feed.rss.Channel;
/*  4:   */ import org.springframework.http.MediaType;
/*  5:   */ 
/*  6:   */ public class RssChannelHttpMessageConverter
/*  7:   */   extends AbstractWireFeedHttpMessageConverter<Channel>
/*  8:   */ {
/*  9:   */   public RssChannelHttpMessageConverter()
/* 10:   */   {
/* 11:38 */     super(new MediaType("application", "rss+xml"));
/* 12:   */   }
/* 13:   */   
/* 14:   */   protected boolean supports(Class<?> clazz)
/* 15:   */   {
/* 16:43 */     return Channel.class.isAssignableFrom(clazz);
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.feed.RssChannelHttpMessageConverter
 * JD-Core Version:    0.7.0.1
 */