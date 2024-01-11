package kpl.fiml.project.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kpl.fiml.project.domain.enums.ProjectCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Sort.Direction;

@Schema(description = "프로젝트 리스트 요청 정보")
@Getter
@AllArgsConstructor
public class ProjectListFindRequest {

    @Schema(description = "페이지 번호", example = "0")
    private Integer page;

    @Schema(description = "페이지 사이즈", example = "10")
    private Integer size;

    @Schema(description = "검색 키워드", example = "프로젝트")
    private String searchKeyword;

    @Schema(description = "프로젝트 상태", example = "PROCEEDING")
    private Status status;

    @Schema(description = "최소 달성률 0 - 100", example = "0")
    private Integer minAchieveRate;

    @Schema(description = "최대 달성률 0 - 100", example = "100")
    private Integer maxAchieveRate;

    @Schema(description = "정렬 필드", example = "START_DATE")
    private SortField sortField;

    @Schema(description = "정렬 방향", example = "DESC")
    private Direction sortDirection;

    @Schema(description = "프로젝트 카테고리", example = "DIGITAL_GAME")
    private ProjectCategory category;

    @Getter
    public enum SortField {
        START_DATE("startDate"),
        END_DATE("endDate"),
        POPULAR("popular");

        private final String field;

        SortField(String field) {
            this.field = field;
        }
    }

    @Getter
    public enum Status {
        PREPARING("준비 중"),
        PROCEEDING("진행 중"),
        COMPLETE("완료"),
        CANCEL("취소");

        private final String displayName;

        Status(String displayName) {
            this.displayName = displayName;
        }
    }
}
