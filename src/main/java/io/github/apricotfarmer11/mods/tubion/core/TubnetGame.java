package io.github.apricotfarmer11.mods.tubion.core;

public interface TubnetGame {
    TeamType teamType = TeamType.SOLOS;
    String name = "Game";
    boolean isInQueue();
    String getName();
    TeamType getTeamType();
    default void recomputeTeamType() {

    }
}
