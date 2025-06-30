package pd.santos.portfoliomanager.asset.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Asset {

    private Long id;
    private String ticker;
    private String name;
    private String country;

    @Column("category")
    private AssetCategory category;

    private boolean active;

    @PersistenceCreator
    public Asset(Long id, String ticker, String name, String country, String category, boolean active) {
        this.id = id;
        this.ticker = ticker;
        this.name = name;
        this.country = country;
        this.category = AssetCategory.fromString(category);
        this.active = active;
    }
}
