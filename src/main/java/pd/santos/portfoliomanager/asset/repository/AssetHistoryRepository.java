package pd.santos.portfoliomanager.asset.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pd.santos.portfoliomanager.asset.model.AssetHistory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Repository
public interface AssetHistoryRepository extends CrudRepository<AssetHistory, Long> {

    @Query("SELECT * FROM \"portfolio-manager\".asset_history WHERE asset_id = :assetId AND date = :date")
    Optional<AssetHistory> findByAssetIdAndDate(@Param("assetId") Long assetId, @Param("date") LocalDate date);

    @Query("SELECT * FROM \"portfolio-manager\".asset_history WHERE asset_id = :assetId AND date = :date")
    AssetHistory findHistoryByAssetIdAndDate(@Param("assetId") Long assetId, @Param("date") LocalDate date);

    @Query("SELECT * FROM \"portfolio-manager\".asset_history WHERE asset_id = :assetId AND date = :date - :days * INTERVAL '1 day'")
    Optional<AssetHistory> findHistoryByAssetIdAndDateMinus(@Param("assetId") Long assetId, @Param("date") LocalDate date, @Param("days") int days);

    /**
     * Finds historical prices for an asset on multiple dates in a single query.
     * Returns a map where the key is the date and the value is the price.
     * 
     * @param assetId the asset ID
     * @param currentDate the current date
     * @param days array of days to look back (e.g., [1, 30, 60, 180, 360])
     * @return a map of dates to prices
     */
    @Query("WITH date_list AS (" +
           "  SELECT :currentDate - d * INTERVAL '1 day' AS date " +
           "  FROM unnest(:days) AS d " +
           ") " +
           "SELECT ah.date, ah.price " +
           "FROM date_list dl " +
           "LEFT JOIN \"portfolio-manager\".asset_history ah ON ah.asset_id = :assetId AND ah.date = dl.date " +
           "ORDER BY ah.date DESC")
    Map<LocalDate, BigDecimal> findHistoricalPricesForDates(@Param("assetId") Long assetId, 
                                                           @Param("currentDate") LocalDate currentDate, 
                                                           @Param("days") Integer[] days);
}
