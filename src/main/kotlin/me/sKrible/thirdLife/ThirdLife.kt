package me.sKrible.thirdLife

import me.sKrible.thirdLife.commands.GiveLifeCommand
import me.sKrible.thirdLife.commands.RandomiseLivesCommand
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

object Config {

    var giveLifeCommand = false
    var livesAmount = 3
    var lives = listOf(
        "gray",
        "red",
        "yellow",
        "green"
    )
    var giveLifeMin = 2

    var randomLives = false
    var randomMin = 2
    var randomMax = 3

    var lifeColours = mapOf(
        "red" to NamedTextColor.RED,
        "dark_red" to NamedTextColor.DARK_RED,
        "yellow" to NamedTextColor.YELLOW,
        "gold" to NamedTextColor.GOLD,
        "green" to NamedTextColor.GREEN,
        "dark_green" to NamedTextColor.DARK_GREEN,
        "aqua" to NamedTextColor.AQUA,
        "dark_blue" to NamedTextColor.DARK_BLUE,
        "blue" to NamedTextColor.BLUE,
        "gray" to NamedTextColor.GRAY
    )
}

private fun registerColourTeams(){
    val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
    for ((team, colour) in Config.lifeColours) {
        if (scoreboard.getTeam(team) == null){
            scoreboard.registerNewTeam(team).color(colour)
            scoreboard.getTeam(team)?.setCanSeeFriendlyInvisibles(false)
        }
    }
}

class ThirdLife : JavaPlugin(), Listener {

    private lateinit var playerDataManager: PlayerDataManager
    private lateinit var serverDataManager: ServerDataManager

    override fun onEnable() {
        logger.info("3rd life plugin enabled! for configuration go to plugins/ThirdLife/config.yml")
        playerDataManager = PlayerDataManager(this)
        serverDataManager = ServerDataManager(this)
        if (!File(dataFolder, "config.yml").exists()) {
            serverDataManager.saveConfig()
        }
        else{
            serverDataManager.loadConfig()
        }

        registerColourTeams()
        registerEvents()
        registerCommands()
    }

    override fun onDisable() {
        // Plugin shutdown logic

    }

    private fun registerEvents(){
        server.pluginManager.registerEvents(this, this)
        server.pluginManager.registerEvents(DeathListener(this), this)
    }

    private fun registerCommands(){

        this.getCommand("givelife")?.setExecutor(GiveLifeCommand(this))
        this.getCommand("randomiselives")?.setExecutor(RandomiseLivesCommand(this))
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (!playerDataManager.loadPlayerData(player)){
            player.setMetadata("lives", FixedMetadataValue(this, Config.livesAmount))
        }
        addPlayerToTeam(player, Config.lives[player.getMetadata("lives")[0].asInt()])
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        playerDataManager.savePlayerData(player)
    }
}
