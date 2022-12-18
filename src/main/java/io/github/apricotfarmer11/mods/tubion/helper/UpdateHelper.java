package io.github.apricotfarmer11.mods.tubion.helper;

import com.google.gson.JsonArray;
import io.github.apricotfarmer11.mods.tubion.TubionMod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateHelper {
    public static Logger LOGGER = LoggerFactory.getLogger("Tubion/UpdateHelper");
    public boolean isUpdateAvailable() {
        try {
            JsonArray json = RequestHelper.getJSONFromURL("https://api.modrinth.com/v2/project/E6BMMeJm/version").getAsJsonArray();
            return json.get(0).getAsJsonObject().get("version_number").getAsString() != TubionMod.VERSION;
        } catch(Exception ex) {
            LOGGER.error("Error while fetching modrinth manifest: ");
            ex.printStackTrace();
            return false;
        }
    }
}
