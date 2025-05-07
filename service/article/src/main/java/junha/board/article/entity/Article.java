package junha.board.article.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "article")
@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 3000, nullable = false)
    private String content;

    @Column(nullable = false)
    private Long boardId; // shard Key

    @Column(nullable = false)
    private Long writerId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public static Article create(Long articleId, String title, String content, Long boardId, Long writerId) {
        LocalDateTime now = LocalDateTime.now();
        return Article.builder()
                .articleId(articleId)
                .title(title)
                .content(content)
                .boardId(boardId)
                .writerId(writerId)
                .createdAt(now)
                .modifiedAt(now)
                .build();
    }

    public static Article create(String title, String content, Long boardId, Long writerId) {
        LocalDateTime now = LocalDateTime.now();
        return Article.builder()
                .title(title)
                .content(content)
                .boardId(boardId)
                .writerId(writerId)
                .createdAt(now)
                .modifiedAt(now)
                .build();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        modifiedAt = LocalDateTime.now();
    }
}
