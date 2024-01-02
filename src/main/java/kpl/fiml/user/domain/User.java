package kpl.fiml.user.domain;

import jakarta.persistence.*;
import kpl.fiml.global.common.BaseEntity;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.user.vo.ContactVo;
import kpl.fiml.user.vo.EmailVo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Notice> noticeList = new ArrayList<>();

    @OneToMany(mappedBy = "followerUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Following> followingList = new ArrayList<>();

    @OneToMany(mappedBy = "followingUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Following> followerList = new ArrayList<>();

    @Builder
    public User(String name, String bio, String profileImage, String email, String encryptPassword, String contact) {
        this.name = name;
        this.bio = bio;
        this.profileImage = profileImage;
        this.email = new EmailVo(email).getEmail();
        this.password = encryptPassword;
        this.contact = new ContactVo(contact).getContact();
        this.cash = 0L;
        this.roles.add("ROLE_USER"); // 권한 처리 필요로 하지 않아서 생성 시 기본 ROLE_USER
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

    public void updateUser(String name, String bio, String profileImage, String email, String encryptPassword, String contact) {
        if (!name.isBlank()) {
            this.name = name;
        }
        if (!bio.isBlank()) {
            this.bio = bio;
        }
        if (!profileImage.isBlank()) {
            this.profileImage = profileImage;
        }
        if (!email.isBlank()) {
            this.email = new EmailVo(email).getEmail();
        }
        if (!password.isBlank()) {
            this.password = encryptPassword;
        }
        if (!contact.isBlank()) {
            this.contact = new ContactVo(contact).getContact();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public boolean isSameUser(User user) {
        return this.id.equals(user.getId());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
