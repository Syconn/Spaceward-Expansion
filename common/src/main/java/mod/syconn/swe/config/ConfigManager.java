package mod.syconn.swe.config;

import com.electronwill.nightconfig.core.file.FileConfig;

import java.io.File;

public class ConfigManager {

    private void setupConfig() {
        FileConfig config = FileConfig.of(new File("dummy.toml"));
        config.load();



        config.save();
        config.close();
    }

    /**
     * private static boolean getConfig() throws IOException
     *     {
     *         Files.createDirectories(file.getParent());
     *         Path defaultConfigPath = Services.CONFIG.getGamePath().resolve(Services.CONFIG.getDefaultConfigPath());
     *         Path defaultConfigFile = defaultConfigPath.resolve(fileName);
     *         if(Files.exists(defaultConfigFile))
     *         {
     *             Files.copy(defaultConfigFile, file);
     *             return true;
     *         }
     *         Files.createFile(file);
     *         format.initEmptyFile(file);
     *         return false;
     *     }
     */
}
