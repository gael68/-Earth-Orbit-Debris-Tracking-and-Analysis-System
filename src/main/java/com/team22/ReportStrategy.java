package com.team22;

import java.util.List;

/**
 * Strategy interface for generating reports on space objects.
 * This interface defines a contract for different types of report generation
 * that can be executed using the Strategy Design Pattern.
 */
public interface ReportStrategy {

    /**
     * Executes a report generation algorithm on the provided list of space objects.
     *
     * @param spaceObjects the list of {@link SpaceObject} instances to be analyzed
     */
    void execute(List<SpaceObject> spaceObjects);
}
