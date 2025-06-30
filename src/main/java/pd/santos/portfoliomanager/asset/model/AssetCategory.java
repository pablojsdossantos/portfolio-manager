package pd.santos.portfoliomanager.asset.model;

/**
 * Enum representing the different categories of assets.
 */
public enum AssetCategory {
    STOCK,
    REIT,
    FII,
    FI_INFRA,
    FI_AGRO;

    /**
     * Converts a string to the corresponding AssetCategory enum value.
     * 
     * @param category the string representation of the category
     * @return the corresponding AssetCategory enum value, or null if not found
     */
    public static AssetCategory fromString(String category) {
        if (category == null) {
            return null;
        }
        
        try {
            return AssetCategory.valueOf(category);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}