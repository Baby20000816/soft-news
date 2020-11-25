package com.soft1851.article.service;

import com.soft1851.pojo.Category;
import com.soft1851.pojo.bo.NewArticleBO;

public interface ArticleService {
    void createArticle(NewArticleBO newArticleBO, Category category);

    void updateArticleStatus(String articleId,Integer pendingStatus);

    void updateAppointToPublish();
}
