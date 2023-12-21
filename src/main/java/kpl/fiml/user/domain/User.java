package kpl.fiml.user.domain;

import jakarta.persistence.*;
import kpl.fiml.global.common.BaseEntity;
import kpl.fiml.user.vo.ContactVo;
import kpl.fiml.user.vo.EmailVo;
import kpl.fiml.user.vo.PasswordVo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    public User(String name, String bio, String profileImage, String email, String password, String contact) {
        this.name = name;
        this.bio = bio;
        this.profileImage = profileImage;
        this.email = new EmailVo(email).email();
        this.password = new PasswordVo(password).password();
        this.contact = new ContactVo(contact).contact();
    }
}
