package kpl.fiml;

import kpl.fiml.comment.domain.Comment;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.ProjectImage;
import kpl.fiml.project.domain.Reward;
import kpl.fiml.project.domain.enums.ProjectCategory;
import kpl.fiml.project.domain.enums.ProjectStatus;
import kpl.fiml.user.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TestDataFactory {

    public static User generateUserWithId(Long id) {
        User user = User.builder()
                .email("email@email.com")
                .encryptPassword("encryptPassword!")
                .name("name")
                .bio("bio")
                .contact("01012345678")
                .profileImage("profileImage")
                .build();

        ReflectionTestUtils.setField(user, "id", id);

        return user;
    }

    public static Reward generateReward(Integer sequence, Long price, Boolean quantityLimited, Integer totalQuantity, LocalDate deliveryDate, Integer maxPurchaseQuantity) {
        return Reward.builder()
                .title("Sample Reward")
                .content("Sample reward content")
                .sequence(sequence)
                .price(price)
                .quantityLimited(quantityLimited)
                .totalQuantity(totalQuantity)
                .deliveryDate(deliveryDate)
                .maxPurchaseQuantity(maxPurchaseQuantity)
                .build();
    }

    public static Project generateDefaultProject(Long userId) {
        String summary = "Sample summary";
        ProjectCategory category = ProjectCategory.BOARD_GAME;
        User user = generateUserWithId(userId);

        return Project.builder()
                .summary(summary)
                .category(category)
                .user(user)
                .build();
    }

    public static Project generateFullParamsProject(Long userId) {
        Project project = generateDefaultProject(userId);
        ReflectionTestUtils.setField(project, "title", "Sample Title");
        ReflectionTestUtils.setField(project, "introduction", "Sample Introduction");
        ReflectionTestUtils.setField(project, "goalAmount", 100000L);
        ReflectionTestUtils.setField(project, "startAt", LocalDateTime.now().plusDays(7));
        ReflectionTestUtils.setField(project, "endAt", LocalDateTime.now().plusDays(14));
        ReflectionTestUtils.setField(project, "commissionRate", 5.0);
        ReflectionTestUtils.setField(project, "status", ProjectStatus.WRITING);
        ReflectionTestUtils.setField(project, "rewards", generateSampleRewards());
        ReflectionTestUtils.setField(project, "projectImages", generateSampleProjectImages());

        return project;
    }

    public static List<ProjectImage> generateSampleProjectImages() {
        return List.of(
                ProjectImage.builder()
                        .sequence(0)
                        .path("https://sample-image1.com")
                        .build(),
                ProjectImage.builder()
                        .sequence(1)
                        .path("https://sample-image2.com")
                        .build()
        );
    }

    public static List<Reward> generateSampleRewards() {
        return List.of(
                Reward.builder()
                        .title("Sample Reward 1")
                        .content("Sample reward content 1")
                        .sequence(0)
                        .price(10000L)
                        .quantityLimited(true)
                        .totalQuantity(10)
                        .deliveryDate(LocalDate.of(2022, 12, 31))
                        .maxPurchaseQuantity(2)
                        .build(),
                Reward.builder()
                        .title("Sample Reward 2")
                        .content("Sample reward content 2")
                        .sequence(1)
                        .price(20000L)
                        .quantityLimited(true)
                        .totalQuantity(20)
                        .deliveryDate(LocalDate.of(2022, 12, 31))
                        .maxPurchaseQuantity(2)
                        .build(),
                Reward.builder()
                        .title("Sample Reward 3")
                        .content("Sample reward content 3")
                        .sequence(3)
                        .price(30000L)
                        .quantityLimited(true)
                        .totalQuantity(30)
                        .deliveryDate(LocalDate.of(2022, 12, 31))
                        .maxPurchaseQuantity(2)
                        .build()
        );
    }

    public static Notice generateNotice(User user, Project project) {
        return Notice.builder()
                .content("sample notice content")
                .user(user)
                .project(project)
                .build();
    }

    public static Comment generateComment(User user, Notice notice) {
        return Comment.builder()
                .user(user)
                .notice(notice)
                .content("Sample Comment Content").build();
    }
}
