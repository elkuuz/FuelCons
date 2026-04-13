package org.example;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.db.CalculationService;
import org.example.db.LocalizationService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class ControllerTest {

    private Controller controller;
    private MockLocalizationService mockLocService;
    private MockCalculationService mockCalcService;

    @BeforeClass
    public static void initJavaFxToolkit() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException ignored) {
            latch.countDown();
        }
        assertTrue("JavaFX toolkit failed to initialize", latch.await(5, TimeUnit.SECONDS));
    }

    private static void runOnFxThread(Runnable action) {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> errorRef = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                action.run();
            } catch (Throwable t) {
                errorRef.set(t);
            } finally {
                latch.countDown();
            }
        });

        try {
            assertTrue("Timed out waiting for FX thread", latch.await(5, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Interrupted while waiting for FX thread");
        }

        if (errorRef.get() != null) {
            throw new RuntimeException(errorRef.get());
        }
    }

    private static class MockLocalizationService extends LocalizationService {
        int loadStringsCallCount = 0;
        String lastLanguage;
        private final Map<String, String> values = new HashMap<>();

        @Override
        public void loadStrings(String language) {
            loadStringsCallCount++;
            lastLanguage = language;
            values.clear();

            values.put("title", "EN".equals(language) ? "Fuel Consumption Calculator"
                    : "FR".equals(language) ? "Calculateur de consommation"
                    : "JP".equals(language) ? "燃料消費量計算機"
                    : "IR".equals(language) ? "ماشین حساب مصرف سوخت"
                    : "Fuel Consumption Calculator");
            values.put("lbl_distance", "Distance (km)");
            values.put("lbl_consumption", "Fuel Consumption (L/100km)");
            values.put("lbl_price", "Fuel Price (per Liter)");
            values.put("btn_calculate", "Calculate");
            values.put("res_total_fuel", "Total Fuel Needed:");
            values.put("res_total_cost", "Total Trip Cost:");
            values.put("msg_error_input", "Please enter valid positive numbers.");
        }

        @Override
        public String getString(String key) {
            return values.getOrDefault(key, "MISSING_KEY: " + key);
        }
    }

    private static class MockCalculationService extends CalculationService {
        int saveCalculationCallCount = 0;
        double lastDistance;
        double lastConsumption;
        double lastPrice;
        double lastTotalFuel;
        double lastTotalCost;
        String lastLanguage;

        @Override
        public void saveCalculation(double distance, double consumption, double price,
                                    double totalFuel, double totalCost, String language) {
            saveCalculationCallCount++;
            lastDistance = distance;
            lastConsumption = consumption;
            lastPrice = price;
            lastTotalFuel = totalFuel;
            lastTotalCost = totalCost;
            lastLanguage = language;
        }
    }

    private void setField(String fieldName, Object value) {
        try {
            Field field = Controller.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(controller, value);
        } catch (Exception e) {
            fail("Failed to set field '" + fieldName + "': " + e.getMessage());
        }
    }

    private Object invokeMethod(String methodName, Class<?>[] paramTypes, Object[] args) {
        try {
            Method method = Controller.class.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            return method.invoke(controller, args);
        } catch (Exception e) {
            fail("Failed to invoke method '" + methodName + "': " + e.getMessage());
            return null;
        }
    }

    private void injectUiControls() {
        setField("lblDistance", new Label());
        setField("lblConsumption", new Label());
        setField("lblPrice", new Label());
        setField("lblResult", new Label());
        setField("lblTitle", new Label());
        setField("txtDistance", new TextField());
        setField("txtConsumption", new TextField());
        setField("txtPrice", new TextField());
        setField("btnCalculate", new Button());
        setField("mbLanguage", new MenuButton());
        setField("menuEN", new MenuItem("EN"));
        setField("menuFR", new MenuItem("FR"));
        setField("menuJP", new MenuItem("JP"));
        setField("menuIR", new MenuItem("IR"));
        setField("rootContainer", new VBox());
    }

    @Before
    public void setUp() {
        controller = new Controller();
        mockLocService = new MockLocalizationService();
        mockCalcService = new MockCalculationService();
        setField("locService", mockLocService);
        setField("calcService", mockCalcService);
    }

    @Test
    public void testControllerInitialization() {
        runOnFxThread(() -> {
            injectUiControls();
            invokeMethod("initialize", new Class<?>[]{}, new Object[]{});

            assertEquals(1, mockLocService.loadStringsCallCount);
            assertEquals("EN", mockLocService.lastLanguage);
            Label lblTitle = (Label) getField("lblTitle");
            assertEquals("Fuel Consumption Calculator", lblTitle.getText());
        });
    }

    @Test
    public void testHandleCalculateActionWithValidInput() {
        runOnFxThread(() -> {
            injectUiControls();
            invokeMethod("setLanguage", new Class<?>[]{String.class}, new Object[]{"EN"});
            ((TextField) getField("txtDistance")).setText("100");
            ((TextField) getField("txtConsumption")).setText("8");
            ((TextField) getField("txtPrice")).setText("1.5");

            invokeMethod("handleCalculateAction", new Class<?>[]{ActionEvent.class}, new Object[]{null});

            assertEquals(1, mockCalcService.saveCalculationCallCount);
            assertEquals(8.0, mockCalcService.lastTotalFuel, 0.0001);
            assertEquals(12.0, mockCalcService.lastTotalCost, 0.0001);
            Label lblResult = (Label) getField("lblResult");
            assertEquals("Total Fuel Needed: 8.00 | Total Trip Cost: 12.00", lblResult.getText());
        });
    }

    @Test
    public void testHandleCalculateActionWithInvalidNumber() {
        runOnFxThread(() -> {
            injectUiControls();
            invokeMethod("setLanguage", new Class<?>[]{String.class}, new Object[]{"EN"});
            ((TextField) getField("txtDistance")).setText("abc");
            ((TextField) getField("txtConsumption")).setText("8");
            ((TextField) getField("txtPrice")).setText("1.5");

            invokeMethod("handleCalculateAction", new Class<?>[]{ActionEvent.class}, new Object[]{null});

            assertEquals(0, mockCalcService.saveCalculationCallCount);
            Label lblResult = (Label) getField("lblResult");
            assertEquals("Please enter valid positive numbers.", lblResult.getText());
        });
    }

    @Test
    public void testHandleCalculateActionWithNegativeInput() {
        runOnFxThread(() -> {
            injectUiControls();
            invokeMethod("setLanguage", new Class<?>[]{String.class}, new Object[]{"EN"});
            ((TextField) getField("txtDistance")).setText("-1");
            ((TextField) getField("txtConsumption")).setText("8");
            ((TextField) getField("txtPrice")).setText("1.5");

            invokeMethod("handleCalculateAction", new Class<?>[]{ActionEvent.class}, new Object[]{null});

            assertEquals(0, mockCalcService.saveCalculationCallCount);
            Label lblResult = (Label) getField("lblResult");
            assertEquals("Please enter valid positive numbers.", lblResult.getText());
        });
    }

    @Test
    public void testHandleLanguageChangeUsesSelectedMenuItem() {
        runOnFxThread(() -> {
            injectUiControls();
            MenuItem menuFR = (MenuItem) getField("menuFR");
            MenuItem menuJP = (MenuItem) getField("menuJP");

            invokeMethod("handleLanguageChange", new Class<?>[]{ActionEvent.class}, new Object[]{new ActionEvent(menuFR, null)});
            assertEquals("FR", mockLocService.lastLanguage);

            invokeMethod("handleLanguageChange", new Class<?>[]{ActionEvent.class}, new Object[]{new ActionEvent(menuJP, null)});
            assertEquals("JP", mockLocService.lastLanguage);
        });
    }

    @Test
    public void testHandleLanguageChangeCoversRemainingBranches() {
        runOnFxThread(() -> {
            injectUiControls();
            MenuItem menuEN = (MenuItem) getField("menuEN");
            MenuItem menuIR = (MenuItem) getField("menuIR");
            MenuItem unmatched = new MenuItem("XX");

            invokeMethod("handleLanguageChange", new Class<?>[]{ActionEvent.class}, new Object[]{new ActionEvent(menuEN, null)});
            assertEquals("EN", mockLocService.lastLanguage);

            invokeMethod("handleLanguageChange", new Class<?>[]{ActionEvent.class}, new Object[]{new ActionEvent(menuIR, null)});
            assertEquals("IR", mockLocService.lastLanguage);

            int callsBefore = mockLocService.loadStringsCallCount;
            invokeMethod("handleLanguageChange", new Class<?>[]{ActionEvent.class}, new Object[]{new ActionEvent(unmatched, null)});
            assertEquals(callsBefore, mockLocService.loadStringsCallCount);
        });
    }

    @Test
    public void testSetLanguageWithNullUiControlsDoesNotCrash() {
        // Intentionally do not inject FXML controls to execute all null-guard false branches.
        invokeMethod("setLanguage", new Class<?>[]{String.class}, new Object[]{"EN"});
        assertEquals("EN", mockLocService.lastLanguage);
        assertEquals(1, mockLocService.loadStringsCallCount);
    }

    @Test
    public void testApplyLayoutDirectionAndIsRtl() {
        runOnFxThread(() -> {
            injectUiControls();
            VBox root = (VBox) getField("rootContainer");

            invokeMethod("applyLayoutDirection", new Class<?>[]{String.class}, new Object[]{"IR"});
            assertEquals(NodeOrientation.RIGHT_TO_LEFT, root.getNodeOrientation());

            invokeMethod("applyLayoutDirection", new Class<?>[]{String.class}, new Object[]{"EN"});
            assertEquals(NodeOrientation.LEFT_TO_RIGHT, root.getNodeOrientation());

            assertTrue((boolean) invokeMethod("isRtlLanguage", new Class<?>[]{String.class}, new Object[]{"IR"}));
            assertFalse((boolean) invokeMethod("isRtlLanguage", new Class<?>[]{String.class}, new Object[]{"EN"}));
        });
    }

    private Object getField(String fieldName) {
        try {
            Field field = Controller.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(controller);
        } catch (Exception e) {
            fail("Failed to get field '" + fieldName + "': " + e.getMessage());
            return null;
        }
    }
}

