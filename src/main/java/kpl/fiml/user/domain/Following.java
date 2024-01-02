package kpl.fiml.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "followings",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"follower_id", "following_id"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Following {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User followerUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private User followingUser;

    @Builder
    public Following(User followerUser, User followingUser) {
        this.followerUser = followerUser;
        this.followingUser = followingUser;
    }

    public boolean isDuplicate(User user, User following) {
        return this.followerUser.isSameUser(user) && this.followingUser.isSameUser(following);
    }
}
