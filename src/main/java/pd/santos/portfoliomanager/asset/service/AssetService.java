package pd.santos.portfoliomanager.asset.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pd.santos.portfoliomanager.asset.model.Asset;
import pd.santos.portfoliomanager.asset.repository.AssetRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;

    public Asset saveNewAsset(Asset asset) {
        return assetRepository.insert(
                asset.getTicker(),
                asset.getName(),
                asset.getCountry(),
                asset.getCategory()
        );
    }

    public Asset updateAsset(Asset asset) {
        return assetRepository.update(
                asset.getId(),
                asset.getTicker(),
                asset.getName(),
                asset.getCountry(),
                asset.getCategory()
        );
    }

    public Asset softDeleteAsset(Long id) {
        return assetRepository.softDelete(id);
    }

    public List<Asset> findAssets(String ticker, String name, String country, String category, Boolean active) {
        // Default to active assets if active parameter is not provided
        Boolean activeFilter = active != null ? active : true;
        return assetRepository.findByFilters(ticker, name, country, category, activeFilter);
    }

    public Asset findAssetById(Long id) {
        return assetRepository.findAssetById(id);
    }
}
