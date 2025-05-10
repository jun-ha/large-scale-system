package junha.board.article.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class ArticleUpdateRequest {
    private String title;
    private String content;
}
