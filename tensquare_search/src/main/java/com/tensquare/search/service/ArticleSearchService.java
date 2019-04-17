package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleSearchDao;
import com.tensquare.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

@Service
@Transactional
public class ArticleSearchService {
    @Autowired
    private ArticleSearchDao articleSearchDao;
    @Autowired
    private IdWorker idWorker;

    /**
     * 增加文章
     */
    public void saveArticle(Article article){
        article.setId(idWorker.nextId()+"");
        articleSearchDao.save(article);
    }

    /**
     * 增加文章
     */
    public Page<Article> findByTitleOrContentLike(String keywords,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return articleSearchDao.findByTitleOrContentLike(keywords,keywords,pageable);
    }

}
