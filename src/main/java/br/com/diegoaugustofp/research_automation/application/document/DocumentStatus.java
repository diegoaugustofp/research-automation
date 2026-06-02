package br.com.diegoaugustofp.research_automation.application.document;

public enum DocumentStatus {
    RECEIVED,
    DOWNLOADED,
    EXTRACTED,
    CLASSIFIED,
    ANALYSIS_IN_PROGRESS,
    ANALYZED,
    FAILED,
    REPROCESSING;

    public boolean canTransitionTo(DocumentStatus next) {
        return switch (this) {
            case RECEIVED -> next == DOWNLOADED;
            case DOWNLOADED -> next == EXTRACTED || next == FAILED;
            case EXTRACTED -> next == CLASSIFIED || next == FAILED;
            case CLASSIFIED -> next == ANALYSIS_IN_PROGRESS;
            case ANALYSIS_IN_PROGRESS -> next == ANALYZED || next == FAILED;
            case FAILED -> next == REPROCESSING;
            case REPROCESSING -> next == ANALYSIS_IN_PROGRESS;
            default -> false;
        };
    }
}
