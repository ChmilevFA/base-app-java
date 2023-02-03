package net.chmilevfa.templates.base.action;

import net.chmilevfa.templates.action.Action;
import net.chmilevfa.templates.base.model.User;
import net.chmilevfa.templates.base.repository.UserRepository;

import static net.chmilevfa.templates.base.model.User.Builder.user;
import static net.chmilevfa.templates.base.model.User.State.ACTIVE;

public class UserCreateAction extends Action<UserCreateAction.Params, User> {

    public record Params(String username,
                         String password,
                         String email) {
    }
    private final UserRepository userRepository;

    public UserCreateAction(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User run(Params params) {
        if (userRepository.findByUsername(params.username).isPresent()) {
            throw new IllegalArgumentException("Username " + params.username + " already taken");
        }
        if (userRepository.findByEmail(params.email).isPresent()) {
            throw new IllegalArgumentException("Email " + params.email + " already taken");
        }
        return batch.add(user()
            .state(ACTIVE)
            .username(params.username)
            .password(params.password)
            .email(params.email)
            .build());
    }
}
