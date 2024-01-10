package kpl.fiml.user.domain;

import jakarta.persistence.*;
import kpl.fiml.global.common.BaseEntity;
import kpl.fiml.project.domain.Project;
import kpl.fiml.user.exception.UserErrorCode;
import kpl.fiml.user.exception.UserPermissionException;
import kpl.fiml.user.exception.CashNotEnoughException;
import kpl.fiml.user.exception.InvalidAmountException;
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

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "contact", nullable = false)
    private String contact;

    @Column(name = "cash", nullable = false)
    private Long cash;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Project> projectList = new ArrayList<>();

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
            throw new InvalidAmountException(UserErrorCode.INVALID_AMOUNT);
        }
        this.cash += amount;
    }

    public void decreaseCash(Long amount) {
        if (amount < 0) {
            throw new InvalidAmountException(UserErrorCode.INVALID_AMOUNT);
        }
        if (this.cash < amount) {
            throw new CashNotEnoughException(UserErrorCode.CASH_NOT_ENOUGH);
        }
        this.cash -= amount;
    }

    public void updateUser(User loginUser, String name, String bio, String profileImage, String email, String encryptPassword, String contact) {
        validateLoginUser(loginUser);

        if (!name.isBlank()) {
            this.name = name;
        }
        this.bio = bio;
        this.profileImage = profileImage;
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

    public void deleteUser(User loginUser) {
        validateLoginUser(loginUser);

        delete();
    }

    public boolean isSameUser(User user) {
        return this.id.equals(user.getId());
    }

    private void validateLoginUser(User loginUser) {
        if(!loginUser.isSameUser(this)) {
            throw new UserPermissionException(UserErrorCode.ACCESS_DENIED);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
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
