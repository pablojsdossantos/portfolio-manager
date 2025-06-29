package pd.santos.portfoliomanager.asset.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pd.santos.portfoliomanager.asset.dto.AssetRequestDto;
import pd.santos.portfoliomanager.asset.dto.AssetUpdateRequestDto;
import pd.santos.portfoliomanager.asset.model.Asset;
import pd.santos.portfoliomanager.asset.service.AssetService;

import java.util.List;

@RestController
@RequestMapping("/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @PostMapping
    public ResponseEntity<Asset> createAsset(@Valid @RequestBody AssetRequestDto assetRequestDto) {
        Asset asset = Asset.builder()
                .ticker(assetRequestDto.getTicker())
                .name(assetRequestDto.getName())
                .country(assetRequestDto.getCountry())
                .category(assetRequestDto.getCategory())
                .build();

        Asset savedAsset = assetService.saveNewAsset(asset);
        return new ResponseEntity<>(savedAsset, HttpStatus.CREATED);
    }

    @PutMapping("/{assetId}")
    public ResponseEntity<?> updateAsset(@PathVariable Long assetId, @Valid @RequestBody AssetUpdateRequestDto assetUpdateRequestDto) {
        Asset asset = Asset.builder()
                .id(assetId)
                .ticker(assetUpdateRequestDto.getTicker())
                .name(assetUpdateRequestDto.getName())
                .country(assetUpdateRequestDto.getCountry())
                .category(assetUpdateRequestDto.getCategory())
                .build();

        Asset updatedAsset = assetService.updateAsset(asset);
        return new ResponseEntity<>(updatedAsset, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Asset>> getAssets(
            @RequestParam(required = false) String ticker,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean active) {

        List<Asset> assets = assetService.findAssets(ticker, name, country, category, active);
        return new ResponseEntity<>(assets, HttpStatus.OK);
    }

    @DeleteMapping("/{assetId}")
    public ResponseEntity<Asset> deleteAsset(@PathVariable Long assetId) {
        Asset deletedAsset = assetService.softDeleteAsset(assetId);
        return new ResponseEntity<>(deletedAsset, HttpStatus.OK);
    }
}
