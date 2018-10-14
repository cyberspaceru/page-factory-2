package ru.sbtqa.tag.pagefactory.environment;

import ru.sbtqa.tag.pagefactory.drivers.DriverService;

/**
 * Static storage for infrastructure
 */
public class Environment {
    private static DriverService driverService;

    public static void setDriverService(DriverService driverService) {
        Environment.driverService = driverService;
    }

    public static DriverService getDriverService() {
        return driverService;
    }

    public static boolean isDriverEmpty() {
        return driverService == null || driverService.isDriverEmpty();
    }
}
