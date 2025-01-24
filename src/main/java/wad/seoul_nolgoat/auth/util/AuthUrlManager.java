package wad.seoul_nolgoat.auth.util;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class AuthUrlManager {

    public static RequestMatcher[] getUserRequestMatchers() {
        return new RequestMatcher[]{
                new AntPathRequestMatcher("/api/auths/logout"),
                new AntPathRequestMatcher("/api/auths/withdrawal/verification"),
                new AntPathRequestMatcher("/api/auths/withdrawal"),

                new AntPathRequestMatcher("/api/users/me"),
                new AntPathRequestMatcher("/api/users/me/bookmarks"),
                new AntPathRequestMatcher("/api/users/me/reviews"),

                new AntPathRequestMatcher("/api/stores/**"),

                new AntPathRequestMatcher("/api/reviews/**"),

                new AntPathRequestMatcher("/api/bookmarks/**"),

                new AntPathRequestMatcher("/api/search/**"),

                new AntPathRequestMatcher("/api/parties/**"),

                new AntPathRequestMatcher("/api/inquiries", "POST"),
                new AntPathRequestMatcher("/api/inquiries/{inquiryId}", "PUT"),
                new AntPathRequestMatcher("/api/inquiries/{inquiryId}", "DELETE"),

                new AntPathRequestMatcher("/api/notices", "POST"),
                new AntPathRequestMatcher("/api/notices/{noticeId}", "PUT"),
                new AntPathRequestMatcher("/api/notices/{noticeId}", "DELETE"),

                new AntPathRequestMatcher("/api/mail/withdrawal/verification")
        };
    }
}
