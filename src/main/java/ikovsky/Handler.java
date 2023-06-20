package ikovsky;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;

import java.util.HashMap;
import java.util.Map;

public class Handler implements RequestHandler<Object, APIGatewayV2HTTPResponse>{
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Override
    public APIGatewayV2HTTPResponse handleRequest(Object event, Context context)
    {
        if(event instanceof Map)
        {
            JsonElement lambdaInputJsonStr = gson.toJsonTree(event);
            JsonPrimitive bodyPrim = lambdaInputJsonStr.getAsJsonObject().getAsJsonPrimitive("body");

            final Gson prettyGson = new GsonBuilder()
                    .setPrettyPrinting()
                    .serializeNulls()
                    .disableHtmlEscaping()
                    .create();

            //parse the garbled mess of a JSON string we receive from AWS API Gateway
            String prettyGsonString = prettyGson.toJson(bodyPrim).replaceAll("\\\\n", "")
                    .replaceAll("\\\\t", "")
                    .replaceAll("\\\\b", "")
                    .replaceAll("\\\\r", "")
                    .replaceAll("\\\\f", "")
                    .replaceAll("\\\\'", "\'")
                    .replaceAll("\\\\\"", "\"");

            JsonObject body = JsonParser.parseString(prettyGsonString.substring(1, prettyGsonString.length() - 1)).getAsJsonObject();

            String key = body.get("key").getAsString();
            String tempo = body.get("tempo").getAsString();
            String timeSig = body.get("timeSig").getAsString();
            String octaveLow = body.get("octaveLow").getAsString();
            String octaveHigh = body.get("octaveHigh").getAsString();
            String dynamicsLow = body.get("dynamicsLow").getAsString();
            String dynamicsHigh = body.get("dynamicsHigh").getAsString();
            String noteDensity = body.get("noteDensity").getAsString();
            String instrument = body.get("instrument").getAsString();
            String weightA = body.get("weightA").getAsString();
            String weightB = body.get("weightB").getAsString();
            String weightC = body.get("weightC").getAsString();
            String weightD = body.get("weightD").getAsString();
            String weightE = body.get("weightE").getAsString();
            String weightF = body.get("weightF").getAsString();
            String weightG = body.get("weightG").getAsString();

            SongGenerator songGenerator = new SongGenerator(false, null, key, tempo, timeSig, octaveLow, octaveHigh,
                    dynamicsLow, dynamicsHigh, noteDensity, instrument, weightA,weightB, weightC, weightD, weightE, weightF,
                    weightG);

            SongResponse songResponse = songGenerator.generateSongResponse();

            HashMap<String, String> headers = new HashMap<>();
            headers.put("Access-Control-Allow-Headers", "Content-Type");
            headers.put("Access-Control-Allow-Origin", "*");
            headers.put("Access-Control-Allow-Methods", "OPTIONS,POST,GET");

            ObjectMapper mapper = new ObjectMapper();
            try {
                String json = mapper.writeValueAsString(songResponse);
                return APIGatewayV2HTTPResponse.builder()
                        .withStatusCode(200)
                        .withHeaders(headers)
                        .withBody(json)
                        .build();
            } catch (JsonProcessingException e) {
                return APIGatewayV2HTTPResponse.builder()
                        .withStatusCode(500)
                        .build();
            }


        }
        return null;
    }
}