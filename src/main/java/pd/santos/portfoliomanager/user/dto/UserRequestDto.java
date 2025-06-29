package pd.santos.portfoliomanager.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 126, message = "Email must be less than 126 characters")
    private String email;

    @NotBlank(message = "First name is required")
    @Size(max = 126, message = "First name must be less than 126 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 126, message = "Last name must be less than 126 characters")
    private String lastName;
}