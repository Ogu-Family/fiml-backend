package kpl.fiml.project.domain;

import jakarta.persistence.*;
import kpl.fiml.global.common.BaseEntity;
import lombok.*;

@Entity
@Getter
@Table(name = "project_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @Column(name = "path", length = 100, nullable = false)
    private String path;

    @Builder
    public ProjectImage(Integer sequence, String path) {
        this.sequence = sequence;
        this.path = path;
    }
}
