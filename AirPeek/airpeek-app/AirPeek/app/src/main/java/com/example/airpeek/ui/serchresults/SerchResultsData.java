package com.example.airpeek.ui.serchresults;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SerchResultsData {

    private String id;
    private String origin;
    private String destination;
    private String departure_hour;
    private String arrival_hour;
    private String departure_date;
    private String arrival_date;
    private String price;
    private String buy_button;
    private String id2;
    private String origin2;
    private String destination2;
    private String departure_hour2;
    private String arrival_hour2;
    private String departure_date2;
    private String arrival_date2;
    private String price2;
    private String buy_button2;
    private String id3;
    private String origin3;
    private String destination3;
    private String departure_hour3;
    private String arrival_hour3;
    private String departure_date3;
    private String arrival_date3;
    private String price3;
    private String buy_button3;
    private String top3;

    public SerchResultsData(JSONObject jsonObject1, JSONObject jsonObject2, JSONObject jsonObject3, double weight) throws JSONException{
        try{
            this.id = jsonObject1.getString("id");
            this.origin = jsonObject1.getString("departure_location");
            this.destination = jsonObject1.getString("arrival_location");
            this.price = String.format("%.2f", Double.parseDouble(jsonObject1.getString("price")) * weight);
            this.buy_button = jsonObject1.getString("buyUrl");

            this.id2 = jsonObject1.getString("id");
            this.origin2 = jsonObject2.getString("departure_location");
            this.destination2 = jsonObject2.getString("arrival_location");
            this.price2 = String.format("%.2f", Double.parseDouble(jsonObject2.getString("price")) * weight);
            this.buy_button2 = jsonObject2.getString("buyUrl");

            this.id3 = jsonObject1.getString("id");
            this.origin3 = jsonObject3.getString("departure_location");
            this.destination3 = jsonObject3.getString("arrival_location");
            this.price3 = String.format("%.2f", Double.parseDouble(jsonObject3.getString("price")) * weight);
            this.buy_button3 = jsonObject3.getString("buyUrl");

            try {
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

                Date departureDatetime1 = parser.parse(jsonObject1.getString("departure_datetime"));
                this.departure_date = new SimpleDateFormat("dd/MM/yyyy").format(departureDatetime1);
                this.departure_hour = new SimpleDateFormat("HH:mm").format(departureDatetime1);

                Date departureDatetime2 = parser.parse(jsonObject2.getString("departure_datetime"));
                this.departure_date2 = new SimpleDateFormat("dd/MM/yyyy").format(departureDatetime2);
                this.departure_hour2 = new SimpleDateFormat("HH:mm").format(departureDatetime2);

                Date departureDatetime3 = parser.parse(jsonObject3.getString("departure_datetime"));
                this.departure_date3 = new SimpleDateFormat("dd/MM/yyyy").format(departureDatetime3);
                this.departure_hour3 = new SimpleDateFormat("HH:mm").format(departureDatetime3);

                Date arrivalDatetime1 = parser.parse(jsonObject1.getString("arrival_datetime"));
                this.arrival_date = new SimpleDateFormat("dd/MM/yyyy").format(arrivalDatetime1);
                this.arrival_hour = new SimpleDateFormat("HH:mm").format(arrivalDatetime1);

                Date arrivalDatetime2 = parser.parse(jsonObject2.getString("arrival_datetime"));
                this.arrival_date2 = new SimpleDateFormat("dd/MM/yyyy").format(arrivalDatetime2);
                this.arrival_hour2 = new SimpleDateFormat("HH:mm").format(arrivalDatetime2);

                Date arrivalDatetime3 = parser.parse(jsonObject3.getString("arrival_datetime"));
                this.arrival_date3 = new SimpleDateFormat("dd/MM/yyyy").format(arrivalDatetime3);
                this.arrival_hour3 = new SimpleDateFormat("HH:mm").format(arrivalDatetime3);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }
    public String getOrigin() {
        return origin;
    }
    public String getDestination() { return destination; }
    public String getDepartureHour(){
        return departure_hour;
    }
    public String getArrivalHour() {
        return arrival_hour;
    }
    public String getDepartureDate(){
        return departure_date;
    }
    public String getArrivalDate() {
        return arrival_date;
    }
    public String getPrice() {
        return price + "€";
    }
    public String getBuyUrl() {
        return buy_button;
    }
    public String getId2() {
        return id2;
    }
    public String getOrigin2() {
        return origin2;
    }
    public String getDestination2() { return destination2; }
    public String getDepartureHour2(){
        return departure_hour2;
    }
    public String getArrivalHour2() {
        return arrival_hour2;
    }
    public String getDepartureDate2(){
        return departure_date2;
    }
    public String getArrivalDate2() {
        return arrival_date2;
    }
    public String getPrice2() {
        return price2 + "€";
    }
    public String getBuyUrl2() {
        return buy_button2;
    }
    public String getId3() {
        return id3;
    }
    public String getOrigin3() {
        return origin3;
    }
    public String getDestination3() { return destination3; }
    public String getDepartureHour3(){
        return departure_hour3;
    }
    public String getArrivalHour3() {
        return arrival_hour3;
    }
    public String getDepartureDate3(){
        return departure_date3;
    }
    public String getArrivalDate3() {
        return arrival_date3;
    }
    public String getPrice3() {
        return price3 + "€";
    }
    public String getBuyUrl3() {
        return buy_button3;
    }
    public String getTop3() {
        return top3;
    }
}
