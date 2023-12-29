package kpl.fiml.user.domain;

import jakarta.persistence.*;
import kpl.fiml.global.common.BaseEntity;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.user.vo.ContactVo;
import kpl.fiml.user.vo.EmailVo;
import kpl.fiml.user.vo.PasswordVo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "bio")
    private String bio;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "contact", nullable = false)
    private String contact;

    @Column(name = "cash", nullable = false)
    private Long cash;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Notice> noticeList = new ArrayList<>();

    @Builder
    public User(String name, String bio, String profileImage, String email, String password, String contact) {
        this.name = name;
        this.bio = bio;
        this.profileImage = profileImage;
        this.email = new EmailVo(email).getEmail();
        this.password = new PasswordVo(password).getPassword();
        this.contact = new ContactVo(contact).getContact();
        this.cash = 0L;
    }

    public void increaseCash(Long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("금액은 음수가 될 수 없습니다.");
        }
        this.cash += amount;
    }

    public void decreaseCash(Long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("금액은 음수가 될 수 없습니다.");
        }
        if (this.cash < amount) {
            throw new IllegalArgumentException("보유 현금이 부족합니다.");
        }
        this.cash -= amount;
    }
}
