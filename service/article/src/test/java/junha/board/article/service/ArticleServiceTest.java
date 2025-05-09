package junha.board.article.service;

import junha.board.article.entity.Article;
import junha.board.article.repository.ArticleRepository;
import junha.board.article.service.request.ArticleCreateRequest;
import junha.board.article.service.response.ArticleResponse;
import junha.board.common.snowflake.Snowflake;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private Snowflake snowflake;

    @InjectMocks
    private ArticleService articleService;

    @Test
    @DisplayName("올바른 요청 정보를 기반으로 아티클을 생성하고 저장된 결과를 응답한다.")
    void create() {
        //given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("title")
                .content("content")
                .boardId(1L)
                .writerId(1L)
                .build();
        long generatedId = 1234L;

        when(snowflake.nextId()).thenReturn(generatedId);

        when(articleRepository.save(any(Article.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //when
        ArticleResponse response = articleService.create(request);

        //then
        assertNotNull(response);
        assertEquals("title", response.getTitle());
        assertEquals("content", response.getContent());
        assertEquals(1L, response.getBoardId());
        assertEquals(1L, response.getWriterId());
        assertEquals(generatedId, response.getArticleId());

        //verify
        verify(snowflake).nextId();
        verify(articleRepository).save(any(Article.class));
    }


}
