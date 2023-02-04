package net.chmilevfa.templates.base.action;

import net.chmilevfa.templates.base.action.UserCreateAction.Params;
import net.chmilevfa.templates.base.model.UserTestData;
import net.chmilevfa.templates.base.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Optional.empty;
import static net.chmilevfa.templates.base.model.User.Builder.user;
import static net.chmilevfa.templates.base.model.User.State.ACTIVE;
import static net.chmilevfa.templates.repository.Batch.ChangeType.ADD;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class UserCreateActionTest implements UserTestData {

    private final UserRepository userRepository = mock(UserRepository.class);

    private final UserCreateAction action = new UserCreateAction(userRepository);

    @Test
    void should_create_a_user() {
        // given
        final var username = randomAlphanumeric(5);
        final var password = randomAlphanumeric(10);
        final var email = randomAlphanumeric(15);

        // when
        final var user = action.run(new Params(username, password, email));

        // then
        assertThat(action.changes())
            .singleElement()
            .satisfies(change -> {
                assertThat(change.type).isEqualTo(ADD);
                assertThat(change.entity).isEqualTo(user);
            });

        assertThat(user.username).isEqualTo(username);
        assertThat(user.email).isEqualTo(email);
        assertThat(user.password).isEqualTo(password);
    }

    @Test
    void should_throw_an_exception_if_username_is_taken() {
        // given
        final var username = randomAlphanumeric(10);
        final var user = aUser()
            .username(username)
            .build();
        given(userRepository.findByUsername(username))
            .willReturn(Optional.of(user));

        // then
        assertThatThrownBy(() -> action.run(new Params(user.username, user.password, user.email)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Username %s already taken", username);

        then(userRepository).should().findByUsername(username);
        then(userRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void should_throw_an_exception_if_email_is_taken() {
        // given
        final var email = randomAlphanumeric(10);
        final var user = aUser()
            .email(email)
            .build();
        given(userRepository.findByUsername(email))
            .willReturn(empty());
        given(userRepository.findByEmail(email))
            .willReturn(Optional.of(user));

        // then
        assertThatThrownBy(() -> action.run(new Params(user.username, user.password, user.email)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Email %s already taken", email);

        then(userRepository).should().findByUsername(user.username);
        then(userRepository).should().findByEmail(email);
        then(userRepository).shouldHaveNoMoreInteractions();
    }

}