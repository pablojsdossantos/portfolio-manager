package pd.santos.portfoliomanager.asset.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing an asset event in the database.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asset_event", schema = "portfolio-manager")
public class AssetEvent {
    @Id
    @Column("event_id")
    private UUID eventId;

    @Column("asset_id")
    private Long assetId;

    @Column("event_type")
    private AssetEventType eventType;

    private BigDecimal amount;

    @Column("event_date")
    private LocalDate eventDate;

    @Column("create_dt")
    private LocalDateTime createDt;

    @Column("update_dt")
    private LocalDateTime updateDt;
}
