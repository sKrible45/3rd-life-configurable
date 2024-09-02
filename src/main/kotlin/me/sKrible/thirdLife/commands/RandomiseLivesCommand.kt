package me.sKrible.thirdLife.commands

import me.sKrible.thirdLife.Config
import me.sKrible.thirdLife.addPlayerToTeam
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin

class RandomiseLivesCommand(private val plugin: Plugin) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, p2: String, args: Array<out String>?): Boolean {
        if (Config.randomLives) {
            val allPlayers = Bukkit.getOnlinePlayers()

            for (player in allPlayers) {
                player.setMetadata("lives", FixedMetadataValue(plugin, (Config.randomMin..Config.randomMax).random()))
                val playerLives = player.getMetadata("lives")[0].asInt()
                player.showTitle(
                    Title.title(
                        Component.text("You have....").color(NamedTextColor.RED),
                        Component.text("")
                    )
                )
                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                    player.showTitle(
                        Title.title(
                            Component.text("$playerLives lives!").color(Config.lifeColours[Config.lives[playerLives]]),
                            Component.text("")
                        )
                    )
                }, 2 * 20)
                addPlayerToTeam(player, Config.lives[playerLives])
            }

            return true
        }
        else{
            sender.sendMessage(Component.text("This command is not enabled!").color(NamedTextColor.RED))
            return false
        }
    }

}