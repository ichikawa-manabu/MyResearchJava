import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;

import java.util.List;

/**
 * Created by manabu on 2017/01/13.
 */
public class GooglePlaceApiTest {
    public static void main(String args[]) {
        GooglePlaces client = new GooglePlaces("AIzaSyAu7lFsJdjfpYD6XpG53E-trZ6VNo5q4wI");
        List<Place> places = client.getPlacesByQuery("Empire State Building", GooglePlaces.MAXIMUM_RESULTS);
        Place empireStateBuilding = null;
        for (Place place : places) {
            if (place.getName().equals("Empire State Building")) {
                empireStateBuilding = place;
                break;
            }
        }
        if (empireStateBuilding != null) {
            Place detailedEmpireStateBuilding = empireStateBuilding.getDetails(); // sends a GET request for more details
            // Just an example of the amount of information at your disposal:
            System.out.println("ID: " + detailedEmpireStateBuilding.getPlaceId());
            System.out.println("Name: " + detailedEmpireStateBuilding.getName());
            System.out.println("Phone: " + detailedEmpireStateBuilding.getPhoneNumber());
            System.out.println("International Phone: " + empireStateBuilding.getInternationalPhoneNumber());
            System.out.println("Website: " + detailedEmpireStateBuilding.getWebsite());
            System.out.println("Always Opened: " + detailedEmpireStateBuilding.isAlwaysOpened());
            System.out.println("Status: " + detailedEmpireStateBuilding.getStatus());
            System.out.println("Google Place URL: " + detailedEmpireStateBuilding.getGoogleUrl());
            System.out.println("Price: " + detailedEmpireStateBuilding.getPrice());
            System.out.println("Address: " + detailedEmpireStateBuilding.getAddress());
            System.out.println("Vicinity: " + detailedEmpireStateBuilding.getVicinity());
            System.out.println("Reviews: " + detailedEmpireStateBuilding.getReviews().size());
            System.out.println("Hours:\n " + detailedEmpireStateBuilding.getHours());
            System.out.println(detailedEmpireStateBuilding.getLatitude());
        }
    }
}
