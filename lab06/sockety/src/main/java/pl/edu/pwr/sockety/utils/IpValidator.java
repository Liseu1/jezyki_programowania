package pl.edu.pwr.sockety.utils;

public class IpValidator {
    public static boolean validate(String ip) {
        try {
            String[] splitIp = ip.split("\\.");

            if(ip.equals("localhost")) {
                return true;
            }
            if(splitIp.length != 4) {
                return false;
            }

            for (String s : splitIp) {
                if (Integer.parseInt(s) < 0 || Integer.parseInt(s) > 255) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
