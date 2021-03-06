package com.soft1851.article.mapper;

import com.soft1851.my.mapper.MyMapper;
import com.soft1851.pojo.Article;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleMapperCustom extends MyMapper<Article> {
    void updateAppointToPublish();
}
