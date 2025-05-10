package junha.board.article.service;

import junha.board.article.entity.Article;
import junha.board.article.exception.ArticleNotFoundException;
import junha.board.article.repository.ArticleRepository;
import junha.board.article.service.request.ArticleCreateRequest;
import junha.board.article.service.request.ArticleUpdateRequest;
import junha.board.article.service.response.ArticleResponse;
import junha.board.common.snowflake.Snowflake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    public void create() {
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

    @Test
    @DisplayName("존재하는 아티클 ID로 update 요청 시 update가 성공하고 저장된 결과를 응답한다.")
    public void update_success() {
        //given
        long existingArticleId = 1L;
        long originalBoardId = 2L;
        long originalWriterId = 3L;
        String newTitle = "newTitle";
        String newContent = "newContent";
        Article existing = Article.create(existingArticleId, "oldTitle", "oldContent", originalBoardId, originalWriterId);
        when(articleRepository.findById(existingArticleId)).thenReturn(Optional.of(existing));

        ArticleUpdateRequest updateRequest = ArticleUpdateRequest.builder()
                .title(newTitle)
                .content(newContent)
                .build();

        //when
        ArticleResponse response = articleService.update(existingArticleId, updateRequest);

        //then
        assertNotNull(response);

        // 1. 도메인 객체의 상태가 업데이트 되었는가?
        assertEquals(newTitle, existing.getTitle());
        assertEquals(newContent, existing.getContent());

        // 2. 응답 객체가 업데이트된 값을 담고 있는가?
        assertEquals(newTitle, response.getTitle());
        assertEquals(newContent, response.getContent());

        // 3. 업데이트하지 않는 필드는 유지되는가?
        assertEquals(originalBoardId, existing.getBoardId());
        assertEquals(originalWriterId, existing.getWriterId());
        assertEquals(originalBoardId, response.getBoardId());
        assertEquals(originalWriterId, response.getWriterId());

        //verify
        verify(articleRepository).findById(existingArticleId);
    }

    @Test
    @DisplayName("존재하지 않는 아티클 ID로 update 요청 시 예외가 발생한다.")
    public void update_fail() {
        //given
        long invalidArticleId = 999L;
        ArticleUpdateRequest request = ArticleUpdateRequest.builder()
                .title("newTitle")
                .content("newContent")
                .build();

        when(articleRepository.findById(invalidArticleId)).thenReturn(Optional.empty());

        //when & then
        assertThrows(ArticleNotFoundException.class,
                () -> articleService.update(invalidArticleId, request));

        //verify
        verify(articleRepository).findById(invalidArticleId);
    }

    @Test
    @DisplayName("존재하는 아티클 ID로 read 요청 시 해당 아티클을 응답한다")
    void read_success() {
        // given
        long existingArticleId = 1L;
        Article existing = Article.create(existingArticleId, "title", "content", 1L, 1L);
        when(articleRepository.findById(existingArticleId)).thenReturn(Optional.of(existing));

        // when
        ArticleResponse response = articleService.read(existingArticleId);

        // then
        assertNotNull(response);
        assertEquals("title", response.getTitle());
        assertEquals("content", response.getContent());
        assertEquals(1L, response.getBoardId());
        assertEquals(1L, response.getWriterId());

        verify(articleRepository).findById(existingArticleId);
    }

    @Test
    @DisplayName("존재하지 않는 아티클 ID로 read 요청 시 예외가 발생한다.")
    void read_fail() {
        //given
        long invalidArticleId = 999L;
        when(articleRepository.findById(invalidArticleId)).thenReturn(Optional.empty());

        //when & then
        assertThrows(ArticleNotFoundException.class,
                () -> articleService.read(invalidArticleId));

        //verify
        verify(articleRepository).findById(invalidArticleId);
    }

    @Test
    @DisplayName("delete 요청 시 repository가 해당 ID로 삭제를 수행한다.")
    void delete() {
        //given
        long articleId = 1L;

        //when
        articleService.delete(articleId);

        //then
        verify(articleRepository).deleteById(articleId);
    }

}
