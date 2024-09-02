package me.sKrible.thirdLife

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin
import javax.inject.Named

fun addPlayerToTeam(player: Player, teamName: String) {
    val scoreboard = Bukkit.getScoreboardManager().mainScoreboard

    var team = scoreboard.getTeam(teamName)
    if (team == null) {
        team = scoreboard.registerNewTeam(teamName)
    }

    team.addPlayer(player)
}


class DeathListener(private val plugin: Plugin) : Listener{
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent){
        val player = event.player

        val currentPlayerLives = player.getMetadata("lives")[0].asInt()
        val newPlayerLives = currentPlayerLives - 1

        player.setMetadata("lives", FixedMetadataValue(plugin, newPlayerLives))
        if (newPlayerLives > 0){
            addPlayerToTeam(player, Config.lives[newPlayerLives])
        }
        else{
            player.showTitle(Title.title(Component.text("You died!").color(NamedTextColor.RED), Component.text("And run out of lives!").color(NamedTextColor.GOLD)))
            player.gameMode = GameMode.SPECTATOR
            addPlayerToTeam(player, Config.lives[0])
        }
    }
}