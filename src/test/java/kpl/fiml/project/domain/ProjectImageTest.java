package kpl.fiml.project.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectImageTest {

    @Test
    @DisplayName("ProjectImage 객체 생성에 성공합니다.")
    void testCreateProjectImage_Success() {
        // Given
        Integer sequence = 1;
        String path = "/images/example.jpg";

        // When
        ProjectImage projectImage = ProjectImage.builder()
                .sequence(sequence)
                .path(path)
                .build();

        // Then
        assertThat(projectImage).isNotNull();
        assertThat(projectImage.getSequence()).isEqualTo(sequence);
        assertThat(projectImage.getPath()).isEqualTo(path);
    }
}