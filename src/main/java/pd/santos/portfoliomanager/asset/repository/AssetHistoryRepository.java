package pd.santos.portfoliomanager.asset.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pd.santos.portfoliomanager.asset.model.AssetHistory;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AssetHistoryRepository extends CrudRepository<AssetHistory, Long> {

    @Query("SELECT * FROM \"portfolio-manager\".asset_history WHERE asset_id = :assetId AND date = :date")
    Optional<AssetHistory> findByAssetIdAndDate(@Param("assetId") Long assetId, @Param("date") LocalDate date);

    @Query("SELECT * FROM \"portfolio-manager\".asset_history WHERE asset_id = :assetId AND date = :date")
    AssetHistory findHistoryByAssetIdAndDate(@Param("assetId") Long assetId, @Param("date") LocalDate date);

    @Query("SELECT * FROM \"portfolio-manager\".asset_history WHERE asset_id = :assetId AND date = :date - :days * INTERVAL '1 day'")
    Optional<AssetHistory> findHistoryByAssetIdAndDateMinus(@Param("assetId") Long assetId, @Param("date") LocalDate date, @Param("days") int days);
}
