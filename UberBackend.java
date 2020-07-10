Uber backend
-------------

Usecases
1) User should able to see nearby drivers
2) User should be able to book trip to a destination
3) Drivers should be able to login to the system and accecpt the trip
4) User pays driver - payment service
5) Calculate trip fare 
6) User should be able to rate a driver

7) User should be able to share with other users.

constraints
------------
1) how many users per day  ?
2) How many drivers per day ?
3) Should high available ?

HLD
----

client -->      Server   --->   database

drivers --> 

LLD
----

@Setters
@Getters
@AllArgsConstructor
public class User{
    private String emailId;
    private String Name;
    private Address address; 
    private List<Trip> trips;
    
}

@Setters
@Getters
@AllArgsConstructor
public class Driver{
    private Vehicle vehicle;
    private String licenseNumber;
    private Long licenseZvalidity;
    private Integer averageRating;
    private List<Rating> ratingsRecieved;
    private PaymentInfo paymentInfos;
    private String currentGeoLocation;

}

@Setters
@Getters
@AllArgsConstructor
public class Rider{
    private List<PaymentInfo> paymentInfos;
    private String currentGeoLocation;
}

@Setters
@Getters
@AllArgsConstructor
public class Address{
    private String streetName;
    private String streetNumber;
    private String city;
    private String Province;
    private String postalCode;
}

public enum TripStatus{

    PROGRESS,REQUESTED,COMPLETED
}

@Getters
@Setters
@AllArgsConstructor
public class Trip{
    private Address sourceLocation;
    private String sourceGeoLocation;
    rivate Address desitnationLocation;
    private String destinationGeoLocation;
    private TripStatus TripStatus;
    private Driver driver;
    private Rider rider;
    private Rating rating;
    private String currentGeoLocation;
}

@Setters
@Getters
@AllArgsConstructor
public class Rating{
    private int ratingNumber;
    private String feedback;
}


public class PaymentInfo{

    private String creditCardNumber;
    private String secureCode;
    private String provider;

    private boolean validateCard(){
        // thirdPartyService to validateCard
        validateCard(creditCardNumber,secureCode,provider);
    }

    public boolean transact(Double cost) throws PaymentException{
        if(validateCard()){
            transact(creditCardNumber,secureCode,provider,cost);
        }else{
            throw new PaymentException("CrediCard validation failed");
        }
    }
}

// Exception to manage payment related errors
public class PaymentException extends exception{
    public PaymentException(String message){
        super(message);
    }
}


// singleton facade for controller
@Singleton
public class ApplicationController{ 

    private DriverService driverService;
    private TripService tripService;
    private FareEngine fareEngine;


    public boolean registerDriver(Driver driver) throws DriverRegistrationException{

        return driverService.registerDriver(driver);
    }

    public Driver requestTrip(Rider rider){
        List<Driver> drivers = driverService.getAllDrivers(rider.getCurrentGeoLocation());
        Trip trip = new Trip.builder(rider)
                            .setStartLocation(rider.getCurrentGeoLocation())
                            .build();
        return tripService.notifyDriver(drivers,trip);
    }

    public List<Driver> showNearbyDrivers(Rider rider){
        return driverService.getAllDrivers(rider.getCurrentGeoLocation());
    }

    public double calculateFare(Trip trip){
        return fareEngine.calculateFare(trip.sourceGeoLocation,trip.desitnationLocation);
    }

    public boolean completeTrip(Trip trip) throws PaymentException{
        Double double = fareEngine.calculateTripFare(trip);
        if(trip.getRider().getPrimaryPayment().authorize(double)){
            return true;
        }else{
            _LOG.error("Payment issu occured "+);
            return false;
        }
    }

    public void acceptTrip(Trip trip){

    }
}

public interface DriverService{

    public List<Driver> getAllDrivers(String geoLocation);
    public boolean registerDriver(Driver driver);
}

public interface TripService{

}

public interface FareEngine{

}







