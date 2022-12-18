package io.github.apricotfarmer11.mods.tubion.helper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RequestHelper {
    private static Logger LOGGER = LoggerFactory.getLogger("Tubion/RequestHelper");
    public static JsonElement getJSONFromURL(String uri) {
        try {
            try (InputStream is = new URL(uri).openStream()) {
                JsonElement root = JsonParser.parseReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                return root.getAsJsonObject();
            }
        } catch(IOException ex) {
            return new JsonArray();
        }
    }
}
