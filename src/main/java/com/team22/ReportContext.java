package com.team22;

import java.util.List;

/**
 * Context class for executing reporting strategies using the Strategy Design Pattern.
 * This class allows dynamic switching between different {@link ReportStrategy} implementations
 * at runtime to generate various types of reports based on space object data.
 */
public class ReportContext {

    private ReportStrategy strategy;

    /**
     * Constructs a report context with a given strategy.
     *
     * @param strategy the initial reporting strategy to use
     */
    public ReportContext(ReportStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Sets or updates the current reporting strategy.
     *
     * @param strategy the new {@link ReportStrategy} to apply
     */
    public void setStrategy(ReportStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Executes the current report strategy on the provided list of space objects.
     *
     * @param spaceObjects list of {@link SpaceObject} instances to process in the report
     */
    public void runReport(List<SpaceObject> spaceObjects) {
        strategy.execute(spaceObjects);
    }
}
