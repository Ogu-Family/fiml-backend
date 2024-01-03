package kpl.fiml.notice.domain;

import jakarta.persistence.*;
import kpl.fiml.comment.domain.Comment;
import kpl.fiml.global.common.BaseEntity;
import kpl.fiml.project.domain.Project;
import kpl.fiml.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "notices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public Notice(String content, User user, Project project) {
        this.content = content;
        this.user = user;
        this.project = project;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
