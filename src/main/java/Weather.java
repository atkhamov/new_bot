import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Weather {
	//67fe202f7cbb874d1e21df3db14aea20
    public static String getWeather(String message, Model model) throws IOException {
        /**For simple extraction of info we use the URL*/
        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=67fe202f7cbb874d1e21df3db14aea20");

        /**Now we need to read the content of the data we received from URL*/
        Scanner in = new Scanner((InputStream)url.getContent());

        String result = "";
        while(in.hasNext()){
            result += in.nextLine();
        }

        /**To solve the problem with the JSON output, we need to cast the JSONObject
         * to the 'result'*/
        JSONObject jsonObject = new JSONObject(result);
        model.setName(jsonObject.getString("name"));

        /**To get the sub object from the whole JSON object, we need to cast another JSONObject*/
        JSONObject main = jsonObject.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        JSONArray getArray = jsonObject.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject jsonObject1 = getArray.getJSONObject(i);
            model.setIcon((String) jsonObject1.get("icon"));
            model.setMain((String) jsonObject1.get("main"));
        }

        return "City: " + model.getName() + "\n" +
                "Temperature: " + model.getTemp() + "C"+ "\n" +
                "Humidity: " + model.getHumidity() + "%"+ "\n" +
                "Description of the weather: " + model.getMain() + "\n" +
                "https://openweathermap.org/img/w/" + model.getIcon() + ".png";
    }
}
