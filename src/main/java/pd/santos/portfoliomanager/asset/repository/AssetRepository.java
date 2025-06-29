package pd.santos.portfoliomanager.asset.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pd.santos.portfoliomanager.asset.model.Asset;

import java.util.List;

@Repository
public interface AssetRepository extends CrudRepository<Asset, Long> {

    @Query("INSERT INTO \"portfolio-manager\".asset (ticker, name, country, category) " +
           "VALUES (:ticker, :name, :country, :category) " +
           "RETURNING id, ticker, name, country, category, active")
    Asset insert(@Param("ticker") String ticker, @Param("name") String name, 
                 @Param("country") String country, @Param("category") String category);

    @Query("UPDATE \"portfolio-manager\".asset " +
           "SET ticker = :ticker, name = :name, country = :country, category = :category " +
           "WHERE id = :id " +
           "RETURNING id, ticker, name, country, category, active")
    Asset update(@Param("id") Long id, @Param("ticker") String ticker, 
                 @Param("name") String name, @Param("country") String country, 
                 @Param("category") String category);

    @Query("UPDATE \"portfolio-manager\".asset SET active = false WHERE id = :id " +
           "RETURNING id, ticker, name, country, category, active")
    Asset softDelete(@Param("id") Long id);

    @Query("SELECT id, ticker, name, country, category, active FROM \"portfolio-manager\".asset " +
           "WHERE active = true")
    List<Asset> findAllActive();

    @Query("SELECT id, ticker, name, country, category, active FROM \"portfolio-manager\".asset " +
           "WHERE (:ticker IS NULL OR ticker = :ticker) " +
           "AND (:name IS NULL OR name LIKE CONCAT('%', :name, '%')) " +
           "AND (:country IS NULL OR country = :country) " +
           "AND (:category IS NULL OR category = :category) " +
           "AND (:active IS NULL OR active = :active)")
    List<Asset> findByFilters(@Param("ticker") String ticker, @Param("name") String name,
                              @Param("country") String country, @Param("category") String category,
                              @Param("active") Boolean active);

    @Query("SELECT id, ticker, name, country, category, active FROM \"portfolio-manager\".asset " +
           "WHERE id = :id")
    Asset findAssetById(@Param("id") Long id);
}
