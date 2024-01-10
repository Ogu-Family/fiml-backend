package kpl.fiml.customMockUser;

import kpl.fiml.user.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.reflect.Field;
import java.util.List;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {
    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
        Long id = 1L;
        String email = annotation.email();
        String contact = annotation.contact();
        String name = annotation.name();
        String password = annotation.password();

        User user = User.builder()
                .name(name)
                .email(email)
                .contact(contact)
                .encryptPassword(password)
                .build();
        try {
            Field userId = user.getClass().getDeclaredField("id");
            userId.setAccessible(true);
            userId.set(user, id);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {

        }

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user, "password", List.of(new SimpleGrantedAuthority("USER")));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);

        return context;
    }
}
