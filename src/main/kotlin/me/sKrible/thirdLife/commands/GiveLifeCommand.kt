package me.sKrible.thirdLife.commands

import me.sKrible.thirdLife.Config
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin
import javax.inject.Named

class GiveLifeCommand(private val plugin : Plugin) : CommandExecutor{
    override fun onCommand(sender: CommandSender, command: Command, p2: String, args: Array<out String>?): Boolean {
        if (Config.giveLifeCommand) {
            if (args?.get(0) != null && sender is Player) {
                val targetPlayer = Bukkit.getPlayer(args[0])

                if (sender.getMetadata("lives")[0].asInt() >= Config.giveLifeMin &&
                    targetPlayer != null &&
                    targetPlayer.getMetadata("lives")[0].asInt() < Config.livesAmount &&
                    targetPlayer.getMetadata("lives")[0].asInt() > 0
                ) {

                    sender.setMetadata("lives", FixedMetadataValue(plugin, sender.getMetadata("lives")[0].asInt() - 1))
                    sender.playSound(sender, Sound.ITEM_TOTEM_USE, 1.0f, 1.0f)
                    sender.spawnParticle(
                        Particle.TOTEM,
                        sender.location,
                        20,
                        1.0, 2.0, 1.0,
                        0.0
                    )
                    sender.showTitle(
                        Title.title(
                            Component.text("you sent a life to ${targetPlayer.name}").color(NamedTextColor.GOLD),
                            Component.text("")
                        )
                    )

                    targetPlayer.setMetadata(
                        "lives",
                        FixedMetadataValue(plugin, targetPlayer.getMetadata("lives")[0].asInt() + 1)
                    )
                    targetPlayer.playSound(targetPlayer, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                    targetPlayer.showTitle(
                        Title.title(
                            Component.text("${sender.name} gave you a life!").color(NamedTextColor.GREEN),
                            Component.text("")
                        )
                    )
                }
            }
            else{
                sender.sendMessage(Component.text("Usage: /givelife [player]").color(NamedTextColor.RED))
                return false
            }


        }
        else{
            sender.sendMessage(Component.text("This command is not enabled!").color(NamedTextColor.RED))
            return false

        }
        return true
    }
}