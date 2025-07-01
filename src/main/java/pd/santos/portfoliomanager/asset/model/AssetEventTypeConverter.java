package pd.santos.portfoliomanager.asset.model;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

/**
 * Converters for AssetEventType enum to ensure it's properly serialized/deserialized when saving to the database.
 */
public class AssetEventTypeConverter {

    /**
     * Converter to convert from AssetEventType to String when writing to the database.
     */
    @WritingConverter
    public static class AssetEventTypeToStringConverter implements Converter<AssetEventType, String> {
        @Override
        public String convert(AssetEventType source) {
            return source.name();
        }
    }

    /**
     * Converter to convert from String to AssetEventType when reading from the database.
     */
    @ReadingConverter
    public static class StringToAssetEventTypeConverter implements Converter<String, AssetEventType> {
        @Override
        public AssetEventType convert(String source) {
            return AssetEventType.valueOf(source);
        }
    }
}