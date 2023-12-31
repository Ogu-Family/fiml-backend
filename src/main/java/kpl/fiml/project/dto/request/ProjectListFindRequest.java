package kpl.fiml.project.dto.request;

import kpl.fiml.project.domain.enums.ProjectCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Sort.Direction;

@Getter
@AllArgsConstructor
public class ProjectListFindRequest {

    private Integer page;
    private Integer size;
    private String searchKeyword;
    private Status status;
    private Integer minAchieveRate;
    private Integer maxAchieveRate;
    private SortField sortField;
    private Direction sortDirection;
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
