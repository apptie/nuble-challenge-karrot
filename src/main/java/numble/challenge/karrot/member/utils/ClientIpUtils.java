package numble.challenge.karrot.member.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import numble.challenge.karrot.common.exception.IpIOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class ClientIpUtils {

    @Value("${open.api.key}")
    private String openApiKey;

    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public boolean checkPlace(String clientIp, String place) throws IpIOException {
        Map<String, Double> ipMap;
        Map<String, String> placeMap;

        try {
            ipMap = getLocationByIp(clientIp);
            placeMap = getLocationByPlace(place);
        } catch (Exception e) {
            throw new IpIOException();
        }

        double ipLat = ipMap.get("lat");
        double ipLon = ipMap.get("lon");
        double placeLat = Double.parseDouble(placeMap.get("y"));
        double placeLon = Double.parseDouble(placeMap.get("x"));

        double distance = getDistance(ipLat, ipLon, placeLat, placeLon);

        return !(distance > 6.0D);
    }

    private Map<String, Double> getLocationByIp(String ip) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://ip-api.com/json/");
        urlBuilder.append(ip);

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");

        BufferedReader br;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        conn.disconnect();

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(sb.toString(), HashMap.class);
    }

    private Map<String, String> getLocationByPlace(String place) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://api.vworld.kr/req/address?");

        urlBuilder.append("service=address&")
                .append("request=getcoord&")
                .append("version=2.0&")
                .append("crs=epsg:4326&")
                .append("address=")
                .append(URLEncoder.encode(place, StandardCharsets.UTF_8))
                .append("&")
                .append("refine=true&")
                .append("simple=true&")
                .append("type=road&")
                .append("key=")
                .append(openApiKey);

        URL url = new URL(urlBuilder.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");

        BufferedReader br;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();
        conn.disconnect();

        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> jsonToHashMap = mapper.readValue(sb.toString(), HashMap.class);
        return jsonToHashMap.get("response").get("result").get("point");
    }

    private double getDistance(double ipLat, double ipLon, double placeLat, double placeLon) {
        double theta = ipLon - placeLon;
        double dist = Math.sin(deg2rad(ipLat)) * Math.sin(deg2rad(placeLat)) + Math.cos(deg2rad(ipLat))
                * Math.cos(deg2rad(placeLat)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344;

        return dist;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
