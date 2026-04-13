package org.example;

import org.junit.Test;

import static org.junit.Assert.*;

public class AppTest {

    @Test
    public void testAppClassExists() {
        // Assert that App class extends Application
        assertNotNull(App.class);
        assertTrue("App should extend Application",
            javafx.application.Application.class.isAssignableFrom(App.class));
    }

    @Test
    public void testAppHasMainMethod() {
        // Test that App has a main method
        try {
            java.lang.reflect.Method mainMethod = App.class.getDeclaredMethod("main", String[].class);
            assertTrue("main method should be static",
                java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("App should have a main method: " + e.getMessage());
        }
    }

    @Test
    public void testAppHasStartMethod() {
        // Test that App has a start method
        try {
            java.lang.reflect.Method startMethod = App.class.getDeclaredMethod("start", javafx.stage.Stage.class);
            assertNotNull(startMethod);
        } catch (NoSuchMethodException e) {
            fail("App should have a start method: " + e.getMessage());
        }
    }

    @Test
    public void testAppIsApplicationSubclass() {
        // Test that App is a subclass of Application
        assertTrue("App should be a subclass of Application",
            javafx.application.Application.class.isAssignableFrom(App.class));
    }

    @Test
    public void testMainMethodIsPublic() {
        // Test that main method is public
        try {
            java.lang.reflect.Method mainMethod = App.class.getDeclaredMethod("main", String[].class);
            int modifiers = mainMethod.getModifiers();
            assertTrue("main method should be public", java.lang.reflect.Modifier.isPublic(modifiers));
        } catch (NoSuchMethodException e) {
            fail("App should have a main method");
        }
    }
}

