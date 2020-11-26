package com.soft1851.article.service;

import com.soft1851.pojo.Category;
import com.soft1851.pojo.bo.NewArticleBO;
import com.soft1851.pojo.vo.ArticleDetailVO;

public interface ArticleService {
    void createArticle(NewArticleBO newArticleBO, Category category);

    void updateArticleStatus(String articleId,Integer pendingStatus);

    void updateAppointToPublish();

    void deleteArticle(String userId,String articleId);

    void withdrawArticle(String userId,String articleId);

    ArticleDetailVO queryDetail(String articleId);
}
