package com.team22;

import java.util.List;

/**
 * Concrete implementation of the {@link ReportStrategy} interface.
 * This strategy generates a full impact report for a list of space objects,
 * including long-term impact analysis and density reporting.
 * 
 * It leverages the {@link ImpactAnalysis} class to perform the following:
 * <ul>
 *     <li>Analyze long-term impact for LEO objects older than 200 days with at least one conjunction.</li>
 *     <li>Prompt the user to enter a longitude range and display a density report of objects in that range.</li>
 * </ul>
 */
public class ImpactReportStrategy implements ReportStrategy {

    /**
     * Executes the full impact report strategy by performing both long-term impact
     * and density analysis using the {@link ImpactAnalysis} module.
     *
     * @param spaceObjects list of {@link SpaceObject} instances to analyze
     */
    @Override
    public void execute(List<SpaceObject> spaceObjects) {
        ImpactAnalysis analysis = new ImpactAnalysis(spaceObjects);
        analysis.analyzeLongTermImpact();
        analysis.generateDensityReport();
    }
}
