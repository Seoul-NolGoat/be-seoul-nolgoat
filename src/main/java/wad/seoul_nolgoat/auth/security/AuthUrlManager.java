package wad.seoul_nolgoat.auth.security;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class AuthUrlManager {

    public RequestMatcher[] getUserRequestMatchers() {
        return new RequestMatcher[]{
                new AntPathRequestMatcher("/api/auths/token/reissue", "POST"),
                new AntPathRequestMatcher("/api/auths/logout", "POST"),
                new AntPathRequestMatcher("/api/auths/withdrawal/verification", "POST"),
                new AntPathRequestMatcher("/api/auths/withdrawal", "DELETE"),

                new AntPathRequestMatcher("/api/users/me", "GET"),
                new AntPathRequestMatcher("/api/users/me/bookmarks", "GET"),
                new AntPathRequestMatcher("/api/users/me/reviews", "GET"),
                new AntPathRequestMatcher("/api/users/me/comments", "GET"),

                new AntPathRequestMatcher("/api/stores/{storeId}", "GET"),
                new AntPathRequestMatcher("/api/stores/search", "GET"),

                new AntPathRequestMatcher("/api/reviews/{storeId}", "POST"),
                new AntPathRequestMatcher("/api/reviews/{reviewId}", "PUT"),
                new AntPathRequestMatcher("/api/reviews/{reviewId}", "DELETE"),

                new AntPathRequestMatcher("/api/bookmarks/{storeId}", "POST"),
                new AntPathRequestMatcher("/api/bookmarks/{userId}/{storeId}", "DELETE"),
                new AntPathRequestMatcher("/api/bookmarks/{userId}/{storeId}", "GET"),

                new AntPathRequestMatcher("/api/comments/parties/{partyId}", "POST"),
                new AntPathRequestMatcher("/api/comments/{commentId}", "PUT"),
                new AntPathRequestMatcher("/api/comments/{commentId}", "DELETE"),

                new AntPathRequestMatcher("/api/search/all", "GET"),
                new AntPathRequestMatcher("/api/search/possible/categories", "GET"),

                new AntPathRequestMatcher("/api/parties", "POST"),
                new AntPathRequestMatcher("/api/parties/{partyId}/join", "POST"),
                new AntPathRequestMatcher("/api/parties/{partyId}/participants/me", "DELETE"),
                new AntPathRequestMatcher("/api/parties/{partyId}", "POST"),
                new AntPathRequestMatcher("/api/parties/{partyId}", "PUT"),
                new AntPathRequestMatcher("/api/parties/{partyId}", "DELETE"),
                new AntPathRequestMatcher("/api/parties/{partyId}/participants/{userId}", "DELETE"),
                new AntPathRequestMatcher("/api/parties/{partyId}", "GET"),
                new AntPathRequestMatcher("/api/parties", "GET"),
                new AntPathRequestMatcher("/api/parties//me/created", "GET"),
                new AntPathRequestMatcher("/api/parties/me/joined", "GET"),

                new AntPathRequestMatcher("/api/inquiries", "POST"),
                new AntPathRequestMatcher("/api/inquiries/{inquiryId}", "GET"),
                new AntPathRequestMatcher("/api/inquiries", "GET"),
                new AntPathRequestMatcher("/api/inquiries/{inquiryId}", "PUT"),
                new AntPathRequestMatcher("/api/inquiries/{inquiryId}", "DELETE"),

                new AntPathRequestMatcher("/api/notices", "POST"),
                new AntPathRequestMatcher("/api/notices/{noticeId}", "GET"),
                new AntPathRequestMatcher("/api/notices", "GET"),
                new AntPathRequestMatcher("/api/notices/{noticeId}", "PUT"),
                new AntPathRequestMatcher("/api/notices/{noticeId}", "DELETE"),
                new AntPathRequestMatcher("/api/notices/{noticeId}/views", "PUT"),

                new AntPathRequestMatcher("/api/mail/withdrawal/verification", "POST")
        };
    }
}
