package pd.santos.portfoliomanager.asset.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing an asset history in the database.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asset_history", schema = "portfolio-manager")
public class AssetHistory {
    @Column("asset_id")
    private Long assetId;
    
    private LocalDate date;
    
    private BigDecimal price;
    
    @Column("variation_1d")
    private BigDecimal variation1d;
    
    @Column("variation_30d")
    private BigDecimal variation30d;
    
    @Column("variation_60d")
    private BigDecimal variation60d;
    
    @Column("variation_180d")
    private BigDecimal variation180d;
    
    @Column("variation_360d")
    private BigDecimal variation360d;
}