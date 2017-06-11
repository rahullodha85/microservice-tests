package hooks;

/**
 * Created by 461967 on 12/15/2015.
 */

import java.util.Properties;

public class CucumberHooks {
    private static String workingDir;
    private static String serviceUrl = "";
    private static String uiUrl = "";
    private static Properties configProp = new Properties();

    public CucumberHooks() {
        LoadConfigProperties();
    }

    public static void Initialize() {
        LoadConfigProperties();
    }

    private static void LoadConfigProperties() {
        workingDir = System.getProperty("user.dir");
        try {
            configProp.load(CucumberHooks.class.getClassLoader().getResourceAsStream("Configuration.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String JsonReqDirPath() {
        String path = workingDir + "\\src\\main\\DataContracts\\Requests\\";
        path = path.replace("\\",System.getProperty("file.separator"));
        path = path.replace("/",System.getProperty("file.separator"));
        return path;
    }

    public static String JsonRespDirPath() {
        String path = workingDir + "\\src\\main\\DataContracts\\Responses\\";
        path = path.replace("\\",System.getProperty("file.separator"));
        path = path.replace("/",System.getProperty("file.separator"));
        return path;
    }

    public static String getServiceUrl(String serviceName) {
        serviceUrl = configProp.getProperty(getEnvironment() + ".ServiceUrl")
                + configProp.getProperty(serviceName + ".EnvironmentURL");
        return serviceUrl;
    }

    public static String getUIUrl(String serviceName) {
        uiUrl = configProp.getProperty(getEnvironment() + ".UIUrl");
        return uiUrl;
    }

    private static String getEnvironment() {
        String env = configProp.getProperty("Environment");
        return env;
    }
}
