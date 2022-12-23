package io.github.apricotfarmer11.mods.tubion.core.games;

import io.github.apricotfarmer11.mods.tubion.core.TeamType;
import io.github.apricotfarmer11.mods.tubion.core.TubnetGame;

public class Lobby implements TubnetGame {
    public final TeamType teamType = null;
    public String name = "Lobby";
    @Override
    public boolean isInQueue() {
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public TeamType getTeamType() {
        return null;
    }
}
