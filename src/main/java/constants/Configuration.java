package constants;

import java.io.File;

public class Configuration {
	public static String APP_DIRECTORY = System.getProperty("user.home") + File.separator + ".licob";
	public static String CHAIN_SETS_DIRECTORY = APP_DIRECTORY + File.separator + "chainsets";
}
