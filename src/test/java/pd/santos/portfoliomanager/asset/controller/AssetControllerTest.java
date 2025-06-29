package pd.santos.portfoliomanager.asset.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import pd.santos.portfoliomanager.asset.dto.AssetRequestDto;
import pd.santos.portfoliomanager.asset.dto.AssetUpdateRequestDto;
import pd.santos.portfoliomanager.asset.model.Asset;
import pd.santos.portfoliomanager.asset.service.AssetService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AssetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AssetService assetService;

    @InjectMocks
    private AssetController assetController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(assetController)
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void testCreateAsset_Success() throws Exception {
        AssetRequestDto assetRequestDto = AssetRequestDto.builder()
                .ticker("AAPL")
                .name("Apple Inc.")
                .country("USA")
                .category("Technology")
                .build();

        Asset savedAsset = Asset.builder()
                .id(1L)
                .ticker("AAPL")
                .name("Apple Inc.")
                .country("USA")
                .category("Technology")
                .active(true)
                .build();

        Mockito.when(assetService.saveNewAsset(any(Asset.class))).thenReturn(savedAsset);

        mockMvc.perform(post("/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assetRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ticker").value("AAPL"))
                .andExpect(jsonPath("$.name").value("Apple Inc."))
                .andExpect(jsonPath("$.country").value("USA"))
                .andExpect(jsonPath("$.category").value("Technology"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    public void testUpdateAsset_Success() throws Exception {
        Long assetId = 1L;
        AssetUpdateRequestDto assetUpdateRequestDto = AssetUpdateRequestDto.builder()
                .id(assetId)
                .ticker("AAPL")
                .name("Apple Inc. Updated")
                .country("USA")
                .category("Technology")
                .build();

        Asset updatedAsset = Asset.builder()
                .id(assetId)
                .ticker("AAPL")
                .name("Apple Inc. Updated")
                .country("USA")
                .category("Technology")
                .active(true)
                .build();

        Mockito.when(assetService.updateAsset(any(Asset.class))).thenReturn(updatedAsset);

        mockMvc.perform(put("/assets/" + assetId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assetUpdateRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assetId))
                .andExpect(jsonPath("$.ticker").value("AAPL"))
                .andExpect(jsonPath("$.name").value("Apple Inc. Updated"))
                .andExpect(jsonPath("$.country").value("USA"))
                .andExpect(jsonPath("$.category").value("Technology"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    public void testUpdateAsset_IdMismatch() throws Exception {
        Long pathAssetId = 1L;
        Long bodyAssetId = 2L;  // Different from pathAssetId

        AssetUpdateRequestDto assetUpdateRequestDto = AssetUpdateRequestDto.builder()
                .id(bodyAssetId)
                .ticker("AAPL")
                .name("Apple Inc.")
                .country("USA")
                .category("Technology")
                .build();

        mockMvc.perform(put("/assets/" + pathAssetId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assetUpdateRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAssets_Success() throws Exception {
        List<Asset> assets = Arrays.asList(
                Asset.builder()
                        .id(1L)
                        .ticker("AAPL")
                        .name("Apple Inc.")
                        .country("USA")
                        .category("Technology")
                        .active(true)
                        .build(),
                Asset.builder()
                        .id(2L)
                        .ticker("MSFT")
                        .name("Microsoft Corporation")
                        .country("USA")
                        .category("Technology")
                        .active(true)
                        .build()
        );

        Mockito.when(assetService.findAssets(any(), any(), any(), any(), any())).thenReturn(assets);

        mockMvc.perform(get("/assets"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].ticker").value("AAPL"))
                .andExpect(jsonPath("$[0].name").value("Apple Inc."))
                .andExpect(jsonPath("$[0].country").value("USA"))
                .andExpect(jsonPath("$[0].category").value("Technology"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].ticker").value("MSFT"))
                .andExpect(jsonPath("$[1].name").value("Microsoft Corporation"))
                .andExpect(jsonPath("$[1].country").value("USA"))
                .andExpect(jsonPath("$[1].category").value("Technology"))
                .andExpect(jsonPath("$[1].active").value(true));
    }

    @Test
    public void testGetAssets_WithFilters() throws Exception {
        List<Asset> filteredAssets = Arrays.asList(
                Asset.builder()
                        .id(1L)
                        .ticker("AAPL")
                        .name("Apple Inc.")
                        .country("USA")
                        .category("Technology")
                        .active(true)
                        .build()
        );

        Mockito.when(assetService.findAssets(eq("AAPL"), any(), any(), any(), any())).thenReturn(filteredAssets);

        mockMvc.perform(get("/assets")
                .param("ticker", "AAPL"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].ticker").value("AAPL"))
                .andExpect(jsonPath("$[0].name").value("Apple Inc."))
                .andExpect(jsonPath("$[0].country").value("USA"))
                .andExpect(jsonPath("$[0].category").value("Technology"))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    public void testDeleteAsset_Success() throws Exception {
        Long assetId = 1L;
        Asset deletedAsset = Asset.builder()
                .id(assetId)
                .ticker("AAPL")
                .name("Apple Inc.")
                .country("USA")
                .category("Technology")
                .active(false)  // Set to false after soft delete
                .build();

        Mockito.when(assetService.softDeleteAsset(assetId)).thenReturn(deletedAsset);

        mockMvc.perform(delete("/assets/" + assetId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assetId))
                .andExpect(jsonPath("$.ticker").value("AAPL"))
                .andExpect(jsonPath("$.name").value("Apple Inc."))
                .andExpect(jsonPath("$.country").value("USA"))
                .andExpect(jsonPath("$.category").value("Technology"))
                .andExpect(jsonPath("$.active").value(false));  // Verify active is false
    }
}
