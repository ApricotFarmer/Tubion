package io.github.apricotfarmer11.mods.tubion.core.games;

import io.github.apricotfarmer11.mods.tubion.TubionMod;
import io.github.apricotfarmer11.mods.tubion.core.TeamType;
import io.github.apricotfarmer11.mods.tubion.core.TubnetGame;
import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.ScoreboardObjective;

import java.time.Instant;

public class CrystalRush implements TubnetGame {
    public TeamType teamType;
    public String name = "Crystal Rush";
    private static MinecraftClient CLIENT = MinecraftClient.getInstance();
    public CrystalRush() {
        this.recomputeTeamType();
    }
    @Override
    public boolean isInQueue() {
        if (TubionMod.scoreboard.length >= 3) {
            String row = TubionMod.scoreboard[2].toString();
            if (row.toLowerCase().contains("in-queue")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public TeamType getTeamType() {
        return teamType;
    }

    @Override
    public void recomputeTeamType() {
        ScoreboardObjective objective = CLIENT.world.getScoreboard().getObjectiveForSlot(1);
        String teamIdentifier = TubionMod.scoreboard[TubionMod.scoreboard.length - 1].toString();
        if (teamIdentifier.contains("solos")) {
            this.teamType = TeamType.SOLOS;
        } else {
            this.teamType = TeamType.DUOS;
        }
    }
}
