package pd.santos.portfoliomanager.asset.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Asset {

    private Long id;
    private String ticker;
    private String name;
    private String country;
    private String category;
    private boolean active;
}