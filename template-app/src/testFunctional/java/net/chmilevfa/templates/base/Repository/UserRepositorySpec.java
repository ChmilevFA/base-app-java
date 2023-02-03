package net.chmilevfa.templates.base.Repository;

import net.chmilevfa.templates.base.FunctionalSpec;
import net.chmilevfa.templates.base.model.User;
import org.jooq.exception.DataAccessException;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static net.chmilevfa.templates.base.model.User.Builder.user;
import static net.chmilevfa.templates.base.model.User.State.ACTIVE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserRepositorySpec extends FunctionalSpec {

    @Test
    void should_save_user() {
        // given
        final var user = user()
            .state(ACTIVE)
            .username(randomAlphanumeric(5))
            .password(randomAlphanumeric(10))
            .email(randomAlphanumeric(15))
            .build();

        // when
        userRepository.add(user);

        //then
        final var savedUser = userRepository.get(user.id);
        assertThat(savedUser.state).isEqualTo(user.state);
        assertThat(savedUser.username).isEqualTo(user.username);
        assertThat(savedUser.password).isEqualTo(user.password);
        assertThat(savedUser.email).isEqualTo(user.email);
    }

    @Test
    void should_find_by_username() {
        // given
        final var user = userRepository.add(user() //TODO use testFixtures
            .state(ACTIVE)
            .username(randomAlphanumeric(5))
            .password(randomAlphanumeric(10))
            .email(randomAlphanumeric(15))
            .build());

        // when
        final var userByUsername = userRepository.findByUsername(user.username);

        // then
        assertThat(userByUsername).hasValue(user);
    }

    @Test
    void should_throw_exception_if_username_already_exists_case_independent() {
        // given
        final var lowerCaseUsername = randomAlphanumeric(10).toLowerCase();
        userRepository.add(user() //TODO use testFixtures
            .state(ACTIVE)
            .username(lowerCaseUsername)
            .password(randomAlphanumeric(10))
            .email(randomAlphanumeric(15))
            .build());

        final var upperCaseUsername = lowerCaseUsername.toUpperCase();

        // then
        assertThatThrownBy(() -> userRepository.add(user() //TODO use testFixtures
            .state(ACTIVE)
                .username(upperCaseUsername)
            .password(randomAlphanumeric(10))
            .email(randomAlphanumeric(15))
            .build()))
            .isInstanceOf(DataAccessException.class)
            .hasMessageContaining("already exists");
    }

    @Test
    void should_find_by_email() {
        // given
        final var user = userRepository.add(user() //TODO use testFixtures
            .state(ACTIVE)
            .username(randomAlphanumeric(5))
            .password(randomAlphanumeric(10))
            .email(randomAlphanumeric(15))
            .build());

        // when
        final var userByEmail = userRepository.findByEmail(user.email);

        // then
        assertThat(userByEmail).hasValue(user);
    }

    @Test
    void should_throw_exception_if_email_already_exists_case_independent() {
        // given
        final var lowerCaseEmail = randomAlphanumeric(10).toLowerCase();
        userRepository.add(user() //TODO use testFixtures
            .state(ACTIVE)
            .username(randomAlphanumeric(10))
            .password(randomAlphanumeric(15))
            .email(lowerCaseEmail)
            .build());

        final var upperCaseEmail = lowerCaseEmail.toUpperCase();

        // then
        assertThatThrownBy(() -> userRepository.add(user() //TODO use testFixtures
            .state(ACTIVE)
            .username(randomAlphanumeric(10))
            .password(randomAlphanumeric(10))
            .email(upperCaseEmail)
            .build()))
            .isInstanceOf(DataAccessException.class)
            .hasMessageContaining("already exists");
    }
}
