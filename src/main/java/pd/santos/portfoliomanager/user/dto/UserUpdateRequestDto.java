package pd.santos.portfoliomanager.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {

    @NotNull(message = "ID is required")
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(max = 126, message = "First name must be less than 126 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 126, message = "Last name must be less than 126 characters")
    private String lastName;
}