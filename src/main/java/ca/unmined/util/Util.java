package ca.unmined.util;


import ca.unmined.Plugin;
import com.google.common.reflect.ClassPath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;

public class Util {
    public static void RegisterAllCommands() throws IOException {
        ClassPath cp = ClassPath.from(Util.class.getClassLoader());
        cp.getTopLevelClassesRecursive("ca.unmined.commands").forEach(info -> {
            try {
                Class<?> c = Class.forName(info.getName());

                Object o = c.newInstance();
                if (o instanceof Command) {
                    Command command = (Command) o;
                    Plugin.COMMANDS.put(command.name.toLowerCase(), command);
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        });
    }

    public static JSONObject getCountryFromJSON(JSONArray countries, String name) {
        for (Object o : countries) {
            JSONObject country = (JSONObject) o;
            if (country.get("location") == name || country.get("country_code") == name) {
                return country;
            }
        }

        return new JSONObject();
    }

    public static JSONArray getTopCasesByCountry(JSONArray countries, int amount) {
        List<JSONObject> jsonValues = new ArrayList<>();
        for (Object country : countries) {
            jsonValues.add((JSONObject) country);
        }

        jsonValues.sort(new Comparator<JSONObject>() {
            private static final String KEY_NAME = "confirmed";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                Long va;
                Long vb;

                va = (Long) a.get(KEY_NAME);
                vb = (Long) b.get(KEY_NAME);

                return -va.compareTo(vb);
            }
        });

        JSONArray out = new JSONArray();
        for (int i = 0; i < countries.size() && i <= amount; i++) {
            out.add(jsonValues.get(i));
        }

        return out;
    }

    public static JSONArray getLowCasesByCountry(JSONArray countries, int amount) {
        List<JSONObject> jsonValues = new ArrayList<>();
        for (Object country : countries) {
            jsonValues.add((JSONObject) country);
        }

        jsonValues.sort(new Comparator<JSONObject>() {
            private static final String KEY_NAME = "confirmed";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                Long va;
                Long vb;

                va = (Long) a.get(KEY_NAME);
                vb = (Long) b.get(KEY_NAME);

                return va.compareTo(vb);
            }
        });

        JSONArray out = new JSONArray();
        for (int i = 0; i < countries.size() && i <= amount; i++) {
            out.add(jsonValues.get(i));
        }

        return out;
    }
}
