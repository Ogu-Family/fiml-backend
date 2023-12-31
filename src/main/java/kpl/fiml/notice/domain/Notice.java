package kpl.fiml.notice.domain;

import jakarta.persistence.*;
import kpl.fiml.comment.domain.Comment;
import kpl.fiml.global.common.BaseEntity;
import kpl.fiml.notice.exception.NoticeErrorCode;
import kpl.fiml.notice.exception.NoticePermissionException;
import kpl.fiml.project.domain.Project;
import kpl.fiml.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Table(name = "notices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", columnDefinition = "text", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public Notice(String content, User user, Project project) {
        this.content = content;
        this.user = user;
        this.project = project;
    }

    public void updateContent(String content, User loginUser) {
        validateLoginUser(loginUser);

        this.content = Objects.requireNonNull(content, "content 가 null 입니다");
    }

    public void deleteNotice(User loginUser) {
        validateLoginUser(loginUser);

        delete();
    }

    private void validateLoginUser(User loginUser) {
        if(!loginUser.isSameUser(this.user)) {
            throw new NoticePermissionException(NoticeErrorCode.ACCESS_DENIED);
        }
    }
}
