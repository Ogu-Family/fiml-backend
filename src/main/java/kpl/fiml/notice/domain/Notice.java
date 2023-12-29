package kpl.fiml.notice.domain;

import jakarta.persistence.*;
import kpl.fiml.comment.domain.Comment;
import kpl.fiml.global.common.BaseEntity;
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

    @OneToMany(mappedBy = "notice", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public Notice(String content) {
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
