package br.pucrs.ages.townsq.model;

public enum ReputationPoints {

    CREATED_QUESTION(10),
    COMPLETED_PROFILE(20),
    DELETED_QUESTION(-10),
    CREATED_ANSWER(10),
    DELETED_ANSWER(-10),
    FAVORED_ANSWER(50),
    UNFAVORED_ANSWER(-50);


    private final int value;

    ReputationPoints(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
