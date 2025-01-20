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
import wad.seoul_nolgoat.web.comment.dto.CommentSaveDto;

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
}
