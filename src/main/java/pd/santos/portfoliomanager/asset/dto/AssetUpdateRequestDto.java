package pd.santos.portfoliomanager.asset.dto;

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
public class AssetUpdateRequestDto {

    @NotBlank(message = "Ticker is required")
    @Size(max = 20, message = "Ticker must be less than 20 characters")
    private String ticker;

    @NotBlank(message = "Name is required")
    @Size(max = 256, message = "Name must be less than 256 characters")
    private String name;

    @NotBlank(message = "Country is required")
    @Size(max = 10, message = "Country must be less than 10 characters")
    private String country;

    @NotBlank(message = "Category is required")
    @Size(max = 64, message = "Category must be less than 64 characters")
    private String category;
}