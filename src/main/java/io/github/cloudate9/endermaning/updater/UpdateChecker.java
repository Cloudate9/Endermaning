package io.github.cloudate9.endermaning.updater;

public interface UpdateChecker {

    /**
     * Checks for updates. What more can be said?
     */
    void checkForUpdate();

    /**
     * Gets a cached result if an update is available.
     * @return If an update is available.
     */
    boolean isUpdateAvailable();

}
