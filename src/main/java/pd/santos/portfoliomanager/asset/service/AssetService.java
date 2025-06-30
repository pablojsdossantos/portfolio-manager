package pd.santos.portfoliomanager.asset.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pd.santos.portfoliomanager.asset.dto.AssetEventDto;
import pd.santos.portfoliomanager.asset.model.Asset;
import pd.santos.portfoliomanager.asset.model.AssetCategory;
import pd.santos.portfoliomanager.asset.model.AssetEvent;
import pd.santos.portfoliomanager.asset.model.AssetHistory;
import pd.santos.portfoliomanager.asset.repository.AssetEventRepository;
import pd.santos.portfoliomanager.asset.repository.AssetHistoryRepository;
import pd.santos.portfoliomanager.asset.repository.AssetRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final AssetEventRepository assetEventRepository;
    private final AssetHistoryRepository assetHistoryRepository;

    public Asset saveNewAsset(Asset asset) {
        return assetRepository.insert(
                asset.getTicker(),
                asset.getName(),
                asset.getCountry(),
                asset.getCategory() != null ? asset.getCategory().name() : null
        );
    }

    public Asset updateAsset(Asset asset) {
        return assetRepository.update(
                asset.getId(),
                asset.getTicker(),
                asset.getName(),
                asset.getCountry(),
                asset.getCategory() != null ? asset.getCategory().name() : null
        );
    }

    public Asset softDeleteAsset(Long id) {
        return assetRepository.softDelete(id);
    }

    public List<Asset> findAssets(String ticker, String name, String country, AssetCategory category, Boolean active) {
        // Default to active assets if active parameter is not provided
        Boolean activeFilter = active != null ? active : true;
        String categoryStr = category != null ? category.name() : null;
        return assetRepository.findByFilters(ticker, name, country, categoryStr, activeFilter);
    }


    /**
     * Process an asset event received from Kafka.
     * 
     * @param eventDto the event DTO
     * @return true if the event was processed successfully, false otherwise
     */
    @Transactional
    public void processAssetEvent(AssetEventDto eventDto) {
        log.info("Processing asset event: {}", eventDto);

        // 1. Check if the event has already been processed
        if (assetEventRepository.findByEventId(eventDto.getEventId()).isPresent()) {
            log.info("Event {} has already been processed, skipping", eventDto.getEventId());
            return;
        }

        // 2. Find the asset by ticker
        List<Asset> assets = assetRepository.findByFilters(eventDto.getTicker(), null, null, null, true);

        if (assets.isEmpty()) {
            log.warn("No active asset found with ticker {}, skipping event", eventDto.getTicker());
            return;
        }

        Asset asset = assets.getFirst();

        // 3. Build the AssetEvent
        AssetEvent assetEvent = AssetEvent.builder()
                .eventId(eventDto.getEventId())
                .assetId(asset.getId())
                .eventType(eventDto.getEventType())
                .amount(eventDto.getAmount())
                .eventDate(eventDto.getDate())
                .build();

        // 4. Build and process the AssetHistory based on event type
        AssetHistory assetHistory = buildAssetHistory(asset.getId(), eventDto);

        // 5. Save both entities in a transaction
        assetEventRepository.save(assetEvent);
        assetHistoryRepository.save(assetHistory);

        log.info("Asset event processed successfully: {}", eventDto.getEventId());
    }

    /**
     * Build an AssetHistory object based on the event type.
     * 
     * @param assetId the asset ID
     * @param eventDto the event DTO
     * @return the AssetHistory object
     */
    private AssetHistory buildAssetHistory(Long assetId, AssetEventDto eventDto) {
        AssetHistory.AssetHistoryBuilder builder = AssetHistory.builder()
                .assetId(assetId)
                .date(eventDto.getDate());

        switch (eventDto.getEventType()) {
            case "PRICE_UPDATE":
                return handlePriceUpdate(builder, assetId, eventDto);
            case "SPLIT":
                return handleSplit(builder, assetId, eventDto);
            case "AGGREGATE":
                return handleAggregate(builder, assetId, eventDto);
            default:
                log.warn("Unknown event type: {}", eventDto.getEventType());
                return builder.price(eventDto.getAmount()).build();
        }
    }

    /**
     * Handle a PRICE_UPDATE event.
     * 
     * @param builder the AssetHistory builder
     * @param assetId the asset ID
     * @param eventDto the event DTO
     * @return the AssetHistory object
     */
    private AssetHistory handlePriceUpdate(AssetHistory.AssetHistoryBuilder builder, Long assetId, AssetEventDto eventDto) {
        builder.price(eventDto.getAmount());

        // Calculate variations
        calculateVariation(builder, assetId, eventDto.getDate(), 1);
        calculateVariation(builder, assetId, eventDto.getDate(), 30);
        calculateVariation(builder, assetId, eventDto.getDate(), 60);
        calculateVariation(builder, assetId, eventDto.getDate(), 180);
        calculateVariation(builder, assetId, eventDto.getDate(), 360);

        return builder.build();
    }

    /**
     * Handle a SPLIT event.
     * 
     * @param builder the AssetHistory builder
     * @param assetId the asset ID
     * @param eventDto the event DTO
     * @return the AssetHistory object
     */
    private AssetHistory handleSplit(AssetHistory.AssetHistoryBuilder builder, Long assetId, AssetEventDto eventDto) {
        Optional<AssetHistory> existingHistory = assetHistoryRepository.findByAssetIdAndDate(assetId, eventDto.getDate());

        if (existingHistory.isPresent()) {
            BigDecimal newPrice = existingHistory.get().getPrice().divide(eventDto.getAmount(), 4, RoundingMode.HALF_UP);
            builder.price(newPrice)
                   .variation1d(existingHistory.get().getVariation1d())
                   .variation30d(existingHistory.get().getVariation30d())
                   .variation60d(existingHistory.get().getVariation60d())
                   .variation180d(existingHistory.get().getVariation180d())
                   .variation360d(existingHistory.get().getVariation360d());
        } else {
            builder.price(BigDecimal.ZERO);
        }

        return builder.build();
    }

    /**
     * Handle an AGGREGATE event.
     * 
     * @param builder the AssetHistory builder
     * @param assetId the asset ID
     * @param eventDto the event DTO
     * @return the AssetHistory object
     */
    private AssetHistory handleAggregate(AssetHistory.AssetHistoryBuilder builder, Long assetId, AssetEventDto eventDto) {
        Optional<AssetHistory> existingHistory = assetHistoryRepository.findByAssetIdAndDate(assetId, eventDto.getDate());

        if (existingHistory.isPresent()) {
            BigDecimal newPrice = existingHistory.get().getPrice().multiply(eventDto.getAmount());
            builder.price(newPrice)
                   .variation1d(existingHistory.get().getVariation1d())
                   .variation30d(existingHistory.get().getVariation30d())
                   .variation60d(existingHistory.get().getVariation60d())
                   .variation180d(existingHistory.get().getVariation180d())
                   .variation360d(existingHistory.get().getVariation360d());
        } else {
            builder.price(BigDecimal.ZERO);
        }

        return builder.build();
    }

    /**
     * Calculate the variation between the current price and the price from a specified number of days ago.
     * 
     * @param builder the AssetHistory builder
     * @param assetId the asset ID
     * @param currentDate the current date
     * @param days the number of days to look back
     */
    private void calculateVariation(AssetHistory.AssetHistoryBuilder builder, Long assetId, LocalDate currentDate, int days) {
        Optional<AssetHistory> previousHistory = assetHistoryRepository.findHistoryByAssetIdAndDateMinus(assetId, currentDate, days);

        if (previousHistory.isPresent() && previousHistory.get().getPrice() != null && !previousHistory.get().getPrice().equals(BigDecimal.ZERO)) {
            BigDecimal currentPrice = builder.build().getPrice();
            BigDecimal previousPrice = previousHistory.get().getPrice();
            BigDecimal variation = currentPrice.subtract(previousPrice)
                    .divide(previousPrice, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

            switch (days) {
                case 1:
                    builder.variation1d(variation);
                    break;
                case 30:
                    builder.variation30d(variation);
                    break;
                case 60:
                    builder.variation60d(variation);
                    break;
                case 180:
                    builder.variation180d(variation);
                    break;
                case 360:
                    builder.variation360d(variation);
                    break;
            }
        }
    }
}
