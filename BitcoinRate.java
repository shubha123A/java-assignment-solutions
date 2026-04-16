import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitcoinRate {

    // Helper arrays for word conversion
    private static final String[] ones = {
        "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
        "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
    };

    private static final String[] tens = {
        "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    };

    // basic method to convert numbers < 1000 into words
    private static String getWordsLessThanThousand(int num) {
        String wordStr = "";

        if (num % 100 < 20) {
            wordStr = ones[num % 100];
            num /= 100;
        } else {
            wordStr = ones[num % 10];
            num /= 10;
            wordStr = tens[num % 10] + (wordStr.isEmpty() ? "" : " " + wordStr);
            num /= 10;
        }

        if (num == 0) return wordStr;

        return ones[num] + " Hundred" + (wordStr.isEmpty() ? "" : " and " + wordStr);
    }

    // converts a large number to Indian format words
    public static String numberToIndianWords(long num) {
        if (num == 0) {
            return "Zero";
        }

        String result = "";

        // check crores
        if (num / 10000000 > 0) {
            result += numberToIndianWords(num / 10000000) + " Crore ";
            num %= 10000000;
        }

        // check lakhs
        if (num / 100000 > 0) {
            result += getWordsLessThanThousand((int)(num / 100000)) + " Lakh ";
            num %= 100000;
        }

        // check thousands
        if (num / 1000 > 0) {
            result += getWordsLessThanThousand((int)(num / 1000)) + " Thousand ";
            num %= 1000;
        }

        // rest of the number
        if (num > 0) {
            result += getWordsLessThanThousand((int)num);
        }

        return result.trim();
    }

    public static void main(String[] args) {
        try {
            // we'll use URL and HttpURLConnection to do the java fetch
            URL coingeckoUrl = new URL("https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd,inr,eur");
            HttpURLConnection conn = (HttpURLConnection) coingeckoUrl.openConnection();
            
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                // start reading the input stream
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // doing manual extraction so we don't have to pull in jackson/gson dependencies for a small task
                String json = response.toString();
                String targetKey = "\"inr\":";
                int start = json.indexOf(targetKey) + targetKey.length();
                int end = json.indexOf(",", start);
                if (end == -1) end = json.indexOf("}", start);

                if (start != -1 + targetKey.length() && end != -1) {
                    String rateStr = json.substring(start, end).trim();
                    long inrRate = (long) Double.parseDouble(rateStr);
                    
                    System.out.println("Bitcoin Rate (INR string from API): " + rateStr);
                    System.out.println("In words: " + numberToIndianWords(inrRate));
                } else {
                    System.out.println("Couldn't parse INR rate out of the response.");
                }

            } else {
                System.out.println("API request failed. Http code: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            System.out.println("Error while fetching bitcoin price:");
            e.printStackTrace();
        }
    }
}
