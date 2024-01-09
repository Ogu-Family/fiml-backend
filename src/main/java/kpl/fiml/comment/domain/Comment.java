package kpl.fiml.comment.domain;

import jakarta.persistence.*;
import kpl.fiml.global.common.BaseEntity;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @Builder
    public Comment(String content, User user, Notice notice) {
        this.content = content;
        this.user = user;
        this.notice = notice;
    }

    public void updateContent(String content, User loginUser) {
        validateLoginUser(loginUser);

        this.content = Objects.requireNonNull(content, "content 가 null 입니다.");
    }

    public void deleteComment(User loginUser) {
        validateLoginUser(loginUser);

        delete();
    }

    private void validateLoginUser(User loginUser) {
        if(!loginUser.isSameUser(this.user)) {
            throw new IllegalArgumentException("댓글 접근 권한이 없습니다.");
        }
    }
}
