package pd.santos.portfoliomanager.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO representing an asset event message from Kafka.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetEventDto {
    private UUID eventId;
    private String ticker;
    private String eventType;
    private BigDecimal amount;
    private LocalDate date;
}