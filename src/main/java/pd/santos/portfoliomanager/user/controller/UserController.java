package pd.santos.portfoliomanager.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pd.santos.portfoliomanager.user.dto.UserRequestDto;
import pd.santos.portfoliomanager.user.dto.UserUpdateRequestDto;
import pd.santos.portfoliomanager.user.model.User;
import pd.santos.portfoliomanager.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleUserNotFoundException(RuntimeException ex) {
        if (ex.getMessage().contains("User not found")) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        throw ex;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        User user = User.builder()
                .email(userRequestDto.getEmail())
                .firstName(userRequestDto.getFirstName())
                .lastName(userRequestDto.getLastName())
                .build();

        User savedUser = userService.saveNewUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        // Validate that the userId in the path matches the id in the request body
        if (!userId.equals(userUpdateRequestDto.getId())) {
            return new ResponseEntity<>("Path variable userId does not match the id in the request body", HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .id(userUpdateRequestDto.getId())
                .firstName(userUpdateRequestDto.getFirstName())
                .lastName(userUpdateRequestDto.getLastName())
                .build();

        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
