package org.example.db;

import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LocalizationService {
    private static final Map<String, String> FALLBACK_EN = createFallbackEnglish();

    private Map<String, String> languageCache = new HashMap<>();
    private String currentLanguage = "EN";

    private static Map<String, String> createFallbackEnglish() {
        Map<String, String> fallback = new HashMap<>();
        fallback.put("title", "Fuel Consumption Calculator");
        fallback.put("lbl_distance", "Distance (km)");
        fallback.put("lbl_consumption", "Fuel Consumption (L/100km)");
        fallback.put("lbl_price", "Fuel Price (per Liter)");
        fallback.put("btn_calculate", "Calculate");
        fallback.put("res_total_fuel", "Total Fuel Needed:");
        fallback.put("res_total_cost", "Total Trip Cost:");
        fallback.put("msg_error_input", "Please enter valid positive numbers.");
        fallback.put("msg_success", "Record saved to database!");
        return Collections.unmodifiableMap(fallback);
    }

    /**
     * Loads all strings for the specified language from the DB
     */
    public void loadStrings(String language) {
        this.currentLanguage = language;
        languageCache.clear();

        String query = "SELECT translation_key, `value` FROM localization_strings WHERE language = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, language);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                languageCache.put(rs.getString("translation_key"), rs.getString("value"));
            }

            if (languageCache.isEmpty()) {
                applyFallbackStrings();
                System.err.println("No localization rows found for language '" + language + "'. Using fallback strings.");
            }

        } catch (SQLException e) {
            System.err.println("Error loading localization strings: " + e.getMessage());
            applyFallbackStrings();
        }
    }

    private void applyFallbackStrings() {
        languageCache.putAll(FALLBACK_EN);
    }

    /**
     * Returns a specific localized string from the cache
     */
    public String getString(String key) {
        return languageCache.getOrDefault(key, "MISSING_KEY: " + key);
    }

    /**
     * Returns all available keys for the current language
     */
    public Set<String> getAllKeys() {
        return languageCache.keySet();
    }
}