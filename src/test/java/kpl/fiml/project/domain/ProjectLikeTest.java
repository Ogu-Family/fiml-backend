package kpl.fiml.project.domain;

import kpl.fiml.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ProjectLikeTest {

    @Test
    @DisplayName("ProjectLike 객체 생성에 성공합니다.")
    void testCreateProjectLike_Success() {
        // Given
        Project project = mock(Project.class);
        User user = mock(User.class);

        // When
        ProjectLike projectLike = ProjectLike.builder()
                .project(project)
                .user(user)
                .build();

        // Then
        assertThat(projectLike).isNotNull();
    }
}
