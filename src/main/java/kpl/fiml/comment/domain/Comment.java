package kpl.fiml.comment.domain;

import jakarta.persistence.*;
import kpl.fiml.global.common.BaseEntity;
import kpl.fiml.notice.domain.Notice;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "commenter", nullable = false)
    private String commenter;

    @ManyToOne(targetEntity = Notice.class, fetch = FetchType.LAZY, optional = false)
    private Notice notice;

    @Builder
    public Comment(String content, String commenter, Notice notice) {
        this.content = content;
        this.commenter = commenter;
        this.notice = notice;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
