package junha.board.article.service.request;

import lombok.*;

@Getter
@ToString
@Builder
public class ArticleCreateRequest {
    private String title;
    private String content;
    private Long boardId;
    private Long writerId;
}
