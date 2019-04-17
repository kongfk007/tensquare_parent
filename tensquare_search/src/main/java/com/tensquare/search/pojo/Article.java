package com.tensquare.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

/**
 * 文章实体类
 */
@Document(indexName="tensquare",type="article")
public class Article implements Serializable {
    @Id
    private String id;//Id

    //是否索引
    //是否分词
    //是否储存
    @Field(index = true,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
    private String title;//标题
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String content;//文章内容
    private String state;//状态（是否存在，1或0表示）

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
