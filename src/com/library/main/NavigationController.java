package com.library.main;

public interface NavigationController {
    void navigateTo(String viewName);
    void navigateTo(String viewName, Object... args);
    void goBack();
}
