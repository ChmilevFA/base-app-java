package net.chmilevfa.templates.base.model;

import static net.chmilevfa.templates.base.model.User.Builder.user;
import static net.chmilevfa.templates.base.model.User.State.ACTIVE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public interface UserTestData {

    default User.Builder aUser() {
        return user()
            .state(ACTIVE)
            .username(randomAlphanumeric(5))
            .password(randomAlphanumeric(10))
            .email(randomAlphanumeric(15));
    }

}
