package wad.seoul_nolgoat.web.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.service.comment.CommentService;
import wad.seoul_nolgoat.web.comment.dto.request.CommentSaveDto;
import wad.seoul_nolgoat.web.comment.dto.request.CommentUpdateDto;

import java.net.URI;

@Tag(name = "댓글")
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성")
    @PostMapping("/parties/{partyId}")
    public ResponseEntity<Void> createComment(
            @AuthenticationPrincipal OAuth2User loginUser,
            @Valid @RequestBody CommentSaveDto commentSaveDto,
            @PathVariable Long partyId,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long commentId = commentService.createComment(
                loginUser.getName(),
                commentSaveDto,
                partyId
        );

        URI location = uriComponentsBuilder.path("/api/comments/{commentId}")
                .buildAndExpand(commentId)
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    @Operation(summary = "댓글 수정")
    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> update(
            @AuthenticationPrincipal OAuth2User loginUser,
            @Valid @RequestBody CommentUpdateDto commentUpdateDto,
            @PathVariable Long commentId
    ) {
        commentService.update(
                loginUser.getName(),
                commentUpdateDto,
                commentId
        );

        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal OAuth2User loginUser, @PathVariable Long commentId) {
        commentService.delete(loginUser.getName(), commentId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
