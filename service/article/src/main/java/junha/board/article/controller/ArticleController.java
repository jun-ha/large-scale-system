package junha.board.article.controller;

import junha.board.article.service.ArticleService;
import junha.board.article.service.request.ArticleCreateRequest;
import junha.board.article.service.request.ArticleUpdateRequest;
import junha.board.article.service.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleResponse> read(@PathVariable Long articleId) {
        return ResponseEntity.ok(articleService.read(articleId));
    }

    @PostMapping
    public ResponseEntity<ArticleResponse> create(@RequestBody ArticleCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(articleService.create(request));
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<ArticleResponse> update(@PathVariable Long articleId, @RequestBody ArticleUpdateRequest request) {
        return ResponseEntity.ok(articleService.update(articleId, request));
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<ArticleResponse> delete(@PathVariable Long articleId) {
        articleService.delete(articleId);
        return ResponseEntity.noContent().build();
    }
}
