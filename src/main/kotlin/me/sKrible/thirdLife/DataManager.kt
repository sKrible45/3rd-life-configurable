package me.sKrible.thirdLife

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin
import java.io.File

class PlayerDataManager(private val plugin: Plugin) {

    private val playerDataFolder = File(plugin.dataFolder, "playerdata")

    init {
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs()
        }
    }

    fun savePlayerData(player: Player) {
        val playerFile = File(playerDataFolder, "${player.name}.yml")
        val config: FileConfiguration = YamlConfiguration.loadConfiguration(playerFile)

        config.set("lives", player.getMetadata("lives")[0].asInt())

        config.save(playerFile)
    }

    fun loadPlayerData(player: Player): Boolean {
        val playerFile = File(playerDataFolder, "${player.name}.yml")
        if (playerFile.exists()) {
            val config: FileConfiguration = YamlConfiguration.loadConfiguration(playerFile)

            player.setMetadata("lives", FixedMetadataValue(plugin, config.getInt("lives")))
            return true
        }
        else{
            return false
        }
    }
}

class ServerDataManager(private val plugin : Plugin){
    private val configFile = File(plugin.dataFolder, "config.yml")

    fun saveConfig(){
        val config = YamlConfiguration.loadConfiguration(configFile)

        config.set("amount_of_lives", Config.livesAmount)

//        config.setComments("life_colours", mutableListOf(
//            "The first colour is the username colour for spectating."
//        ))
        config.set("life_colours", Config.lives)


//        config.setComments("give_life_command_enabled", mutableListOf(
//            "This enables the /givelife command seen in grian's last-life series.",
//            "It gives one of your lives to the player specified."
//        ))
        config.set("give_life_command_enabled", Config.giveLifeCommand)


//        config.setComments("give_life_command_enabled", mutableListOf(
//            "The minimum lives you have to have to use /givelife"
//        ))
        config.set("givelife_min", Config.giveLifeMin)


//        config.setComments("random_lives_command_enabled", mutableListOf(
//            "toggles the /randomiselives command which randomises all players lives in the server just like last-life"
//        ))
        config.set("random_lives_command_enabled", Config.randomLives)

//        config.setComments("random_lives_min", mutableListOf(
//            "The minimum and maximum amounts of lives when using the /randomiselives command"
//        ))
        config.set("random_lives_min", Config.randomMin)
        config.set("random_lives_max", Config.randomMax)

        config.save(configFile)
    }

    fun loadConfig(){
        val config = YamlConfiguration.loadConfiguration(configFile)

        Config.livesAmount = config.getInt("amount_of_lives")

        Config.lives = config.getStringList("life_colours")

        Config.giveLifeCommand = config.getBoolean("give_life_command_enabled")

        Config.giveLifeMin = config.getInt("minimum_lives_to_use_givelife")


        Config.randomLives = config.getBoolean("random_lives_command_enabled")

        Config.randomMin = config.getInt("random_lives_min")
        Config.randomMax = config.getInt("random_lives_max")



        config.save(configFile)
    }
}