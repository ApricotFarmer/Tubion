package io.github.apricotfarmer.mods.tubion.mixin;

import io.github.apricotfarmer.mods.tubion.event.ScoreboardObjectiveUpdateCallback;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Scoreboard.class)
public class ScoreboardMixin {
    @Inject(at = @At("TAIL"), method = "resetPlayerScore")
    public void resetPlayerScore(String playerName, @Nullable ScoreboardObjective objective, CallbackInfo ci) {
        ScoreboardObjectiveUpdateCallback.EVENT.invoker().interact();
    }
    @Inject(at = @At("TAIL"), method = "<init>")
    public void init(CallbackInfo ci) {
        ScoreboardObjectiveUpdateCallback.EVENT.invoker().interact();
    }
    @Inject(at = @At("TAIL"), method = "addTeam")
    public Team addTeam(String name, CallbackInfoReturnable<Team> ci) {
        ScoreboardObjectiveUpdateCallback.EVENT.invoker().interact();
        return ci.getReturnValue();
    }
    @Inject(at = @At("TAIL"), method = "addPlayerToTeam")
    public boolean addPlayerToTeam(String playerName, Team team, CallbackInfoReturnable<Boolean> ci) {
        ScoreboardObjectiveUpdateCallback.EVENT.invoker().interact();
        return ci.getReturnValue();
    }
}
