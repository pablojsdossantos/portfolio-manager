package pd.santos.portfoliomanager.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pd.santos.portfoliomanager.user.model.User;
import pd.santos.portfoliomanager.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User saveNewUser(User user) {
        return userRepository.insert(user.getEmail(), user.getFirstName(), user.getLastName());
    }

    public User updateUser(User user) {
        // Update only firstName and lastName
        // If there is no user with that ID, the update won't change anything
        return userRepository.update(user.getId(), user.getFirstName(), user.getLastName());
    }
}
