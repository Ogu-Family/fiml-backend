package kpl.fiml.customMockUser;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMockUserSecurityContextFactory.class)
public @interface WithCustomMockUser {
    String email() default "aaa@gmail.com";
    String contact() default "01012345678";
    String name() default "name";
    String password() default "password123!";
}
