package junha.board.article.service;

import junha.board.article.entity.Article;
import junha.board.article.exception.ArticleNotFoundException;
import junha.board.article.repository.ArticleRepository;
import junha.board.article.service.request.ArticleCreateRequest;
import junha.board.article.service.request.ArticleUpdateRequest;
import junha.board.article.service.response.ArticleResponse;
import junha.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final Snowflake snowflake;

    @Transactional
    public ArticleResponse create(ArticleCreateRequest request) {
        Article article = articleRepository.save(
                Article.create(
                        snowflake.nextId(),
                        request.getTitle(),
                        request.getContent(),
                        request.getBoardId(),
                        request.getWriterId()
                )
        );
        return ArticleResponse.from(article);
    }

    @Transactional
    public ArticleResponse update(Long articleId, ArticleUpdateRequest request) {
        Article article = articleRepository.findById(articleId).orElseThrow(() ->
                new ArticleNotFoundException("Article does not exists!")
        );
        article.update(request.getTitle(), request.getContent());
        return ArticleResponse.from(article);
    }

    @Transactional(readOnly = true)
    public ArticleResponse read(Long articleId) {
        return ArticleResponse.from(
                articleRepository.findById(articleId).orElseThrow(() ->
                        new ArticleNotFoundException("Article does not exists!")
                )
        );
    }

    @Transactional
    public void delete(Long articleId) {
        articleRepository.deleteById(articleId);
    }
}
