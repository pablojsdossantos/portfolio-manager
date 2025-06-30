package pd.santos.portfoliomanager.asset.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pd.santos.portfoliomanager.asset.model.AssetEvent;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetEventRepository extends CrudRepository<AssetEvent, UUID> {

    @Query("SELECT * FROM \"portfolio-manager\".asset_event WHERE event_id = :eventId")
    Optional<AssetEvent> findByEventId(@Param("eventId") UUID eventId);
}