package kpl.fiml.project.domain.enums;

import lombok.Getter;

@Getter
public enum ProjectCategory {
    BOARD_GAME("보드게임/TRPG"),
    DIGITAL_GAME("디지털 게임"),
    WEBTOON("웹툰/만화"),
    WEBTOON_RESOURCES("웹툰 리소스"),
    DESIGN_STATIONERY("디자인 문구"),
    CHARACTER_GOODS("캐릭터/굿즈"),
    HOME_LIVING("홈/리빙"),
    TECH_APPLIANCES("테크/가전"),
    PET("반려동물"),
    FOOD("푸드"),
    PERFUME_BEAUTY("향수/뷰티"),
    CLOTHING("의류"),
    ACCESSORIES("잡화"),
    JEWELRY("주얼리"),
    PUBLISHING("출판"),
    DESIGN("디자인"),
    ART("예술"),
    PHOTOGRAPHY("사진"),
    MUSIC("음악"),
    MOVIE_VIDEO("영화/비디오"),
    PERFORMANCE("공연");

    private final String displayName;

    ProjectCategory(String displayName) {
        this.displayName = displayName;
    }
}
