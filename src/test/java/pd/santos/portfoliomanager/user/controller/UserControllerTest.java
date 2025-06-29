package pd.santos.portfoliomanager.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import pd.santos.portfoliomanager.user.dto.UserRequestDto;
import pd.santos.portfoliomanager.user.dto.UserUpdateRequestDto;
import pd.santos.portfoliomanager.user.model.User;
import pd.santos.portfoliomanager.user.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void testCreateUser_Success() throws Exception {
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .build();

        User savedUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .build();

        Mockito.when(userService.saveNewUser(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)))
                .andDo(print())  // Print request and response for debugging
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void testCreateUser_ValidationFailure() throws Exception {
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email("invalid-email")
                .firstName("")
                .lastName("Doe")
                .build();

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)))
                .andDo(print())  // Print request and response for debugging
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUser_TooLongFields() throws Exception {
        String tooLongString = "a".repeat(127);

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email(tooLongString + "@example.com")
                .firstName("John")
                .lastName("Doe")
                .build();

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)))
                .andDo(print())  // Print request and response for debugging
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        Long userId = 1L;
        UserUpdateRequestDto userUpdateRequestDto = UserUpdateRequestDto.builder()
                .id(userId)
                .firstName("Updated John")
                .lastName("Updated Doe")
                .build();

        User existingUser = User.builder()
                .id(userId)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .build();

        User updatedUser = User.builder()
                .id(userId)
                .email("test@example.com")
                .firstName("Updated John")
                .lastName("Updated Doe")
                .build();

        Mockito.when(userService.updateUser(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateRequestDto)))
                .andDo(print())  // Print request and response for debugging
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Updated John"))
                .andExpect(jsonPath("$.lastName").value("Updated Doe"));
    }

    @Test
    public void testUpdateUser_ValidationFailure() throws Exception {
        Long userId = 1L;
        UserUpdateRequestDto userUpdateRequestDto = UserUpdateRequestDto.builder()
                .id(userId)
                .firstName("")  // Empty first name should fail validation
                .lastName("Updated Doe")
                .build();

        mockMvc.perform(put("/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateRequestDto)))
                .andDo(print())  // Print request and response for debugging
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUser_IdMismatch() throws Exception {
        Long pathUserId = 1L;
        Long bodyUserId = 2L;  // Different from pathUserId

        UserUpdateRequestDto userUpdateRequestDto = UserUpdateRequestDto.builder()
                .id(bodyUserId)
                .firstName("Updated John")
                .lastName("Updated Doe")
                .build();

        mockMvc.perform(put("/users/" + pathUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateRequestDto)))
                .andDo(print())  // Print request and response for debugging
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUser_UserNotFound() throws Exception {
        Long userId = 999L;  // Non-existent user ID
        UserUpdateRequestDto userUpdateRequestDto = UserUpdateRequestDto.builder()
                .id(userId)
                .firstName("Updated John")
                .lastName("Updated Doe")
                .build();

        Mockito.when(userService.updateUser(any(User.class))).thenThrow(new RuntimeException("User not found with id: " + userId));

        mockMvc.perform(put("/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateRequestDto)))
                .andDo(print())  // Print request and response for debugging
                .andExpect(status().isNotFound());
    }
}
