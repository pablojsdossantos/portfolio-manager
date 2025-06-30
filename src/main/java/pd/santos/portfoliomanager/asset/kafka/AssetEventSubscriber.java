package pd.santos.portfoliomanager.asset.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pd.santos.portfoliomanager.asset.dto.AssetEventDto;
import pd.santos.portfoliomanager.asset.service.AssetService;

@Slf4j
@Component
@RequiredArgsConstructor
public class AssetEventSubscriber {

    private final AssetService assetService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${kafka.topic.asset-events}",
            groupId = "${kafka.group-id}",
            autoStartup = "${kafka.auto-start:true}"
    )
    public void consumeAssetEvent(String payload) {
        try {
            log.info("Received asset event: {}", payload);
            AssetEventDto eventDto = objectMapper.readValue(payload, AssetEventDto.class);
            assetService.processAssetEvent(eventDto);
        } catch (Exception e) {
            log.error("Error processing asset event: {}", payload, e);
        }
    }
}