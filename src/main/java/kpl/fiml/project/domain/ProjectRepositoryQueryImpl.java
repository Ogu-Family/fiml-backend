package kpl.fiml.project.domain;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kpl.fiml.project.domain.enums.ProjectCategory;
import kpl.fiml.project.domain.enums.ProjectStatus;
import kpl.fiml.project.dto.request.ProjectListFindRequest;
import kpl.fiml.project.dto.request.ProjectListFindRequest.SortField;
import kpl.fiml.project.exception.project.ProjectErrorCode;
import kpl.fiml.project.exception.project.ProjectFindConditionException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Objects;

import static kpl.fiml.project.domain.QProject.project;

@RequiredArgsConstructor
public class ProjectRepositoryQueryImpl implements ProjectRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Project> findWithSearchKeyword(ProjectListFindRequest searchRequest, Pageable pageable) {
        BooleanExpression expression = titleContainsIgnoreCase(searchRequest.getSearchKeyword())
                .and(projectStatusEq(searchRequest.getStatus()))
                .and(project.status.ne(ProjectStatus.WRITING))
                .and(project.deletedAt.isNull())
                .and(projectCategoryEq(searchRequest.getCategory()))
                .and(achieveRateBetween(searchRequest.getMinAchieveRate(), searchRequest.getMaxAchieveRate()));

        List<Project> projects = jpaQueryFactory
                .selectFrom(project)
                .where(expression)
                .orderBy(getOrderSpecifier(searchRequest.getSortField(), searchRequest.getSortDirection()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Project> countQuery = jpaQueryFactory
                .selectFrom(project)
                .where(expression);

        return PageableExecutionUtils.getPage(projects, pageable, countQuery::fetchCount);
    }

    private BooleanExpression titleContainsIgnoreCase(String keyword) {
        return project.title.containsIgnoreCase(Objects.requireNonNullElse(keyword, ""));
    }

    private OrderSpecifier<?> getOrderSpecifier(SortField sortField, Direction sortDirection) {
        if (sortField == SortField.POPULAR) {
            return sortDirection == Direction.ASC
                    ? project.likedCount.asc()
                    : project.likedCount.desc();
        } else if (sortField == SortField.START_DATE) {
            return sortDirection == Direction.ASC
                    ? project.startAt.asc()
                    : project.startAt.desc();
        } else if (sortField == SortField.END_DATE) {
            return sortDirection == Direction.ASC
                    ? project.endAt.asc()
                    : project.endAt.desc();
        } else {
            return project.id.desc();
        }
    }

    private BooleanExpression projectStatusEq(ProjectListFindRequest.Status status) {
        if (status == ProjectListFindRequest.Status.PREPARING) {
            return project.status.eq(ProjectStatus.PREPARING);
        } else if (status == ProjectListFindRequest.Status.PROCEEDING) {
            return project.status.eq(ProjectStatus.PROCEEDING);
        } else if (status == ProjectListFindRequest.Status.COMPLETE) {
            return project.status.eq(ProjectStatus.FUNDING_COMPLETE)
                    .or(project.status.eq(ProjectStatus.SETTLEMENT_COMPLETE));
        } else if (status == ProjectListFindRequest.Status.CANCEL) {
            return project.status.eq(ProjectStatus.CANCEL);
        } else {
            return project.status.isNotNull();
        }
    }

    private BooleanExpression projectCategoryEq(ProjectCategory category) {
        if (category != null) {
            return project.category.eq(category);
        }
        return project.category.isNotNull();
    }

    private BooleanExpression achieveRateBetween(Integer minAchieveRate, Integer maxAchieveRate) {
        validateAchieveRateRange(minAchieveRate, maxAchieveRate);

        if (minAchieveRate != null && maxAchieveRate != null) {
            return project.currentAmount.divide(project.goalAmount).between(minAchieveRate, maxAchieveRate);
        } else if (minAchieveRate != null) {
            return project.currentAmount.divide(project.goalAmount).goe(minAchieveRate);
        } else if (maxAchieveRate != null) {
            return project.currentAmount.divide(project.goalAmount).loe(maxAchieveRate);
        } else {
            return project.currentAmount.divide(project.goalAmount).goe(0);
        }
    }

    private void validateAchieveRateRange(Integer minAchieveRate, Integer maxAchieveRate) {
        if (minAchieveRate != null && (minAchieveRate < 0 || minAchieveRate > 100)) {
            throw new ProjectFindConditionException(ProjectErrorCode.INVALID_PROJECT_ACHIEVE_RATE);
        }
        if (maxAchieveRate != null && (maxAchieveRate < 0 || maxAchieveRate > 100)) {
            throw new ProjectFindConditionException(ProjectErrorCode.INVALID_PROJECT_ACHIEVE_RATE);
        }
        if (minAchieveRate != null && maxAchieveRate != null && (minAchieveRate > maxAchieveRate)) {
            throw new ProjectFindConditionException(ProjectErrorCode.INVALID_PROJECT_ACHIEVE_RATE_RANGE);
        }
    }
}
