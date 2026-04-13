package org.example.db;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class LocalizationServiceTest {

    private LocalizationService localizationService;

    @Before
    public void setUp() {
        localizationService = new LocalizationService();
    }

    @Test
    public void testLoadStringsWithEnglish() {
        // Act
        localizationService.loadStrings("EN");

        // Assert - should use fallback strings
        assertNotNull(localizationService.getString("title"));
        assertEquals("Fuel Consumption Calculator", localizationService.getString("title"));
    }

    @Test
    public void testLoadStringsWithFrench() {
        // Act
        localizationService.loadStrings("FR");

        // Assert - should load localized values when available
        assertEquals("Calculateur de consommation", localizationService.getString("title"));
        assertEquals("Distance (km)", localizationService.getString("lbl_distance"));
    }

    @Test
    public void testGetStringWithValidKey() {
        // Arrange
        localizationService.loadStrings("EN");

        // Act
        String result = localizationService.getString("msg_error_input");

        // Assert
        assertEquals("Please enter valid positive numbers.", result);
    }

    @Test
    public void testGetStringWithInvalidKey() {
        // Arrange
        localizationService.loadStrings("EN");

        // Act
        String result = localizationService.getString("invalid_key");

        // Assert
        assertTrue(result.contains("MISSING_KEY"));
    }

    @Test
    public void testGetAllKeys() {
        // Arrange
        localizationService.loadStrings("EN");

        // Act
        Set<String> keys = localizationService.getAllKeys();

        // Assert
        assertNotNull(keys);
        assertTrue(keys.size() > 0);
    }

    @Test
    public void testFallbackEnglishStrings() {
        // Arrange & Act
        localizationService.loadStrings("EN");

        // Assert
        assertEquals("Fuel Consumption Calculator", localizationService.getString("title"));
        assertEquals("Distance (km)", localizationService.getString("lbl_distance"));
        assertEquals("Fuel Consumption (L/100km)", localizationService.getString("lbl_consumption"));
        assertEquals("Fuel Price (per Liter)", localizationService.getString("lbl_price"));
        assertEquals("Calculate", localizationService.getString("btn_calculate"));
        assertEquals("Total Fuel Needed:", localizationService.getString("res_total_fuel"));
        assertEquals("Total Trip Cost:", localizationService.getString("res_total_cost"));
        assertEquals("Please enter valid positive numbers.", localizationService.getString("msg_error_input"));
    }

    @Test
    public void testLanguageSwitch() {
        // Arrange
        localizationService.loadStrings("EN");
        String enTitle = localizationService.getString("title");

        // Act
        localizationService.loadStrings("JP");
        String jpTitle = localizationService.getString("title");

        // Assert - language switch should actually change localized title
        assertEquals("Fuel Consumption Calculator", enTitle);
        assertEquals("燃料消費量計算機", jpTitle);
        assertNotEquals(enTitle, jpTitle);
    }

    @Test
    public void testMultipleLanguageLoads() {
        // Act & Assert
        localizationService.loadStrings("EN");
        assertEquals("Fuel Consumption Calculator", localizationService.getString("title"));

        localizationService.loadStrings("FR");
        assertEquals("Calculateur de consommation", localizationService.getString("title"));

        localizationService.loadStrings("JP");
        assertEquals("燃料消費量計算機", localizationService.getString("title"));

        localizationService.loadStrings("IR");
        assertEquals("ماشین حساب مصرف سوخت", localizationService.getString("title"));
    }

    @Test
    public void testAllFallbackStringsAvailable() {
        // Arrange
        localizationService.loadStrings("ZZ");

        // Act & Assert - verify all fallback strings are available
        String[] expectedKeys = {
            "title", "lbl_distance", "lbl_consumption", "lbl_price",
            "btn_calculate", "res_total_fuel", "res_total_cost", "msg_error_input", "msg_success"
        };

        for (String key : expectedKeys) {
            String value = localizationService.getString(key);
            assertNotNull("Key '" + key + "' should have a value", value);
            assertFalse("Key '" + key + "' should not return MISSING_KEY", value.contains("MISSING_KEY"));
        }
    }
}

