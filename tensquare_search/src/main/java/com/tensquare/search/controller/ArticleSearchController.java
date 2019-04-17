package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleSearchService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/article")
public class ArticleSearchController {
    @Autowired
    private ArticleSearchService service;

    @RequestMapping(method = RequestMethod.POST)
    public Result saveArticle(@RequestBody Article article){
        service.saveArticle(article);
        return new Result(true, StatusCode.OK,"操作成功");
    }

    /**
     * 文章搜索
     */
    @RequestMapping(value="/search/{keywords}/{page}/{size}",method=RequestMethod.GET)
    public Result findByTitleLike(@PathVariable String keywords,
                                  @PathVariable int page,@PathVariable int size){
        Page<Article> pageData = service.findByTitleOrContentLike(keywords, page, size);
        return new Result(true, StatusCode.OK,"查询成功",new PageResult<>(pageData.getTotalElements(),pageData.getContent()));
    }
}
