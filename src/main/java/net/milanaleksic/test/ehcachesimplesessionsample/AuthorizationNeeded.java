package net.milanaleksic.test.ehcachesimplesessionsample;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthorizationNeeded {
}
