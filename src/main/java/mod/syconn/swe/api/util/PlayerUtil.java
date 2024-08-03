package mod.syconn.swe.api.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class PlayerUtil {

    public static String convertUsernameToUUID(String name){
        try {
            HttpGet request = new HttpGet("https://api.mojang.com/users/profiles/minecraft/" + name);
            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            JsonObject jsonObject = (JsonObject) JsonParser.parseString(EntityUtils.toString(entity));

            if (jsonObject.has("id"))return jsonObject.get("id").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getTextureURL(String name){
        String id = convertUsernameToUUID(name);
        if (!id.isEmpty()) {
            try {
                HttpGet request = new HttpGet("https://sessionserver.mojang.com/session/minecraft/profile/" + id);
                CloseableHttpClient client = HttpClients.createDefault();
                CloseableHttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                JsonObject jsonObject = (JsonObject) JsonParser.parseString(EntityUtils.toString(entity));
                if (jsonObject != null){
                    String bitcode = jsonObject.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
                    byte[] decodedBytes = Base64.decodeBase64(bitcode.getBytes());
                    JsonObject SkinData = (JsonObject) JsonParser.parseString(new String(decodedBytes));
                    return SkinData.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static NativeImage getSkinTexture(String url){
        try {
            HttpGet request = new HttpGet(url);
            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            return NativeImage.read(entity.getContent());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean getModelType(String name){
        String id = convertUsernameToUUID(name);
        if (!id.isEmpty()) {
            try {
                HttpGet request = new HttpGet("https://sessionserver.mojang.com/session/minecraft/profile/" + id);
                CloseableHttpClient client = HttpClients.createDefault();
                CloseableHttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                JsonObject jsonObject = (JsonObject) JsonParser.parseString(EntityUtils.toString(entity));

                if (jsonObject != null){
                    String bitcode = jsonObject.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
                    byte[] decodedBytes = Base64.decodeBase64(bitcode.getBytes());
                    JsonObject SkinData = (JsonObject) JsonParser.parseString(new String(decodedBytes));
                    return SkinData.getAsJsonObject("textures").getAsJsonObject("SKIN").has("metadata");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
