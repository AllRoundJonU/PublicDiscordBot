package de.allroundjonu.DiscordBot;

import de.allroundjonu.Main;
import de.allroundjonu.Utils.ConfigFile;
import de.allroundjonu.Utils.CustomLogger;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class LoadEmbeds {

    public static ConfigFile embedsConfig = Main.embeds;
    public static HashMap<String, EmbedBuilder> embedMessages = Main.embedMessages;

    public static void loadEmbeds(){

        JSONArray embeds = (JSONArray) embedsConfig.get("embeds");
        for(Object o: embeds){
            try {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                JSONObject embedInfos = (JSONObject) o;
                JSONObject embed = (JSONObject) embedInfos.get("embed");
                if (embed == null){
                    CustomLogger.error("Error while loading embeds: Embed is null");
                    continue;
                }
                if (embedInfos.get("name") == null){
                    CustomLogger.error("Error while loading embeds: Embed name is null");
                    continue;
                }
                String name = (String) embedInfos.get("name");
                if (embed.get("title") != null && embed.get("url") != null){
                    embedBuilder.setTitle((String) embed.get("title"), (String) embed.get("url"));
                    //CustomLogger.message("Loaded Title and URL for embed: " + name);
                }else if(embed.get("title") != null && embed.get("url") == null){
                    embedBuilder.setTitle((String) embed.get("title"));
                    //CustomLogger.message("Loaded Title for embed: " + name);
                }
                if(embed.get("description") != null){
                    embedBuilder.setDescription((String) embed.get("description"));
                    //CustomLogger.message("Loaded Description for embed: " + name);
                }
              /*  if(embed.get("color") != null) {
                    embedBuilder.setColor(new Color(Integer.parseInt((String) embed.get("color"))));
                    CustomLogger.message("Loaded Color for embed: " + name);
                }*/

                JSONObject footer = (JSONObject) embed.get("footer");
                if (footer != null){
                    if(footer.get("icon_url") != null && footer.get("text") != null) {
                        embedBuilder.setFooter((String) footer.get("text"), (String) footer.get("icon_url"));
                        //CustomLogger.message("Loaded Footer and ICON for embed: " + name);
                    }else if(footer.get("icon_url") == null && footer.get("text") != null) {
                        embedBuilder.setFooter((String) footer.get("text"));
                        //CustomLogger.message("Loaded Footer for embed: " + name);
                    }
                }
                JSONObject thumbnail = (JSONObject) embed.get("thumbnail");
                if (thumbnail != null) {
                    if (thumbnail.get("url") != null) {
                        embedBuilder.setThumbnail((String) thumbnail.get("url"));
                        //CustomLogger.message("Loaded Thumbnail for embed: " + name);
                    }
                }
                JSONObject image = (JSONObject) embed.get("image");
                if (image != null) {
                    if (image.get("url") != null) {
                        embedBuilder.setImage((String) image.get("url"));
                        //CustomLogger.message("Loaded Image for embed: " + name);
                    }
                }
                JSONObject author = (JSONObject) embed.get("author");
                if (author != null) {
                    if (author.get("name") != null && author.get("url") != null && author.get("icon_url") != null) {
                        embedBuilder.setAuthor((String) author.get("name"), (String) author.get("url"), (String) author.get("icon_url"));
                        //CustomLogger.message("Loaded Author, URL and ICON for embed: " + name);
                    } else if (author.get("name") != null && author.get("url") != null && author.get("icon_url") == null) {
                        embedBuilder.setAuthor((String) author.get("name"), (String) author.get("url"));
                        //CustomLogger.message("Loaded Author and URL for embed: " + name);
                    } else if (author.get("name") != null && author.get("url") == null && author.get("icon_url") != null) {
                        embedBuilder.setAuthor((String) author.get("name"), null, (String) author.get("icon_url"));
                        //CustomLogger.message("Loaded Author and ICON for embed: " + name);
                    } else if (author.get("name") != null && author.get("url") == null && author.get("icon_url") == null) {
                        embedBuilder.setAuthor((String) author.get("name"));
                        //CustomLogger.message("Loaded Author for embed: " + name);
                    }
                }
                JSONArray fields = (JSONArray) embed.get("fields");
                if (fields != null) {
                    for (Object field : fields) {
                        JSONObject fieldObject = (JSONObject) field;
                        if (fieldObject.get("name") != null && fieldObject.get("value") != null && fieldObject.get("inline") != null) {
                            embedBuilder.addField((String) fieldObject.get("name"), (String) fieldObject.get("value"), (boolean) fieldObject.get("inline"));
                            //CustomLogger.message("Loaded Field for embed: " + name);
                        } else {
                            CustomLogger.error("Error while loading embeds: Field name, value or inline is null");
                        }
                    }
                }
                embedMessages.put(name, embedBuilder);
            }catch (Exception e){
                CustomLogger.error("Error while loading embeds: " + e);
                DiscordBot.stopDiscordBot();
                return;
            }
        }
    }
}

