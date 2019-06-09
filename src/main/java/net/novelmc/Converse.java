package net.novelmc;

import java.io.InputStream;
import java.util.Properties;
import me.lucko.luckperms.api.LuckPermsApi;
import net.novelmc.commands.ConverseCommand;
import net.novelmc.commands.StaffCommand;
import net.novelmc.util.Config;
import net.novelmc.util.Updater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Converse extends JavaPlugin
{
    public static Converse plugin;
    public static final BuildProperties build = new BuildProperties();
    public static Server server;
    public static YamlConfiguration config;
    public static YamlConfiguration bans = Config.getConfig(Config.ConfigFile.BANS);

    public void onLoad()
    {
        plugin = this;
        server = plugin.getServer();
    }

    public void onEnable()
    {
        build.load(this);
        new Metrics(this);
        Config.loadConfigs();
        getLuckPermsAPI();
        registerCommands();
    }

    public static LuckPermsApi getLuckPermsAPI()
    {
        RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
        if (provider != null)
        {
            return provider.getProvider();
        }
        return null;
    }


    public void onDisable()
    {
        try
        {
            Updater updater = new Updater(this);
            updater.update();
        }
        catch (NoClassDefFoundError ex)
        {
            plugin.getLogger().info("There was an error checking for an update");
        }
    }

    private void registerCommands()
    {
        getCommand("converse").setExecutor(new ConverseCommand());
        getCommand("converse").setTabCompleter(new ConverseCommand());
        getCommand("staff").setExecutor(new StaffCommand());
    }

    public static class BuildProperties
    {
        public String author;
        public String codename;
        public String version;
        public String number;
        public String date;
        public String head;

        void load(Converse plugin)
        {
            try
            {
                final Properties props;

                try (InputStream in = plugin.getResource("build.properties"))
                {
                    props = new Properties();
                    props.load(in);
                }

                author = props.getProperty("buildAuthor", "unknown");
                codename = props.getProperty("buildCodename", "unknown");
                version = props.getProperty("buildVersion", plugin.getDescription().getVersion());
                number = props.getProperty("buildNumber", "1");
                date = props.getProperty("buildDate", "unknown");
                head = props.getProperty("buildHead", "unknown").replace("${git.commit.id.abbrev}", "unknown");
            }
            catch (Exception ex)
            {
                server.getLogger().severe("Could not load build properties! Did you compile with NetBeans/Maven?");
                server.getLogger().severe(ex.toString());
            }
        }
    }
}