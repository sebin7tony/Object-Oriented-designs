
Parking lot design

Usecases
--------

1) single location vs multiple locations
2) multiple levels vs single levels
3) Do we have multiple types of parking spots ? eg large, medium, small
4) when enters the parking lot -> give a parking spot number (closest location)
5) different types of cars, medium cars, large and small
6) do we have different rates for different spots
7) if we dont have small sports available then should we assign a larger spot ?

constraints
----------
number of users in this parking lot ?

HLD
---
public class Spot{

    private Long spotNumber;
    private Long levelNumber;
    private SpotType spotType;

    public Spot(Long spotNumber,Long levelNumber,SpotType spotType){
        this.spotNumber = spotNumber;
        this.levelNumber = levelNumber;
        this.SpotType = spotType;
    }

    public Long getSpotNumber(){
        return spotNumber;
    }

    public SpotType getSpotType(){
        return spotType;
    }
}

public enum SpotType {
    LARGE,MEDIUM,SMALL
}

public enum VehicleType {
    LARGE,MEDIUM,SMALL
}

@Getters
public class Vehicle{
    private String licenseNumber;
    private String make;
    private String model;
    private String color;
    private VehicleType vehicleType;

    public vehicle(String licenseNumber,String make,String model,String color,VehicleType vehicleType){
        this.licenseNumber = licenseNumber;
        this.make = make;
        this.model = model;
        this.color = color;
        this.vehicleType = vehicleType;
    }
}


@Getters
@AllArgsConstructor
public class Transaction {
    private Spot spot;
    private Vehicle vehicle;
    private Double cost; 
    private Date enterTime;
    private Date exitTime;
    private TransactionStatus status;

    public Transaction(Spot spot,Vehicle vehicle,Double cost,Date enterTime){
        this.spot = spot;
        this.vehicle = vehicle;
    }

    public void setExitTime(Date exitTime){
        this.exitTime = exitTime;
    }
}

public enum TransactionStatus{
    RESERVED,COMPLETED,PROGRESS
}

public class ParkingLot{

    private String address;
    private AvailableSpotFinderService spotFinderService;
    private Map<String,Transactions> currentTransactions;

    public ParkingLot(FinderType type){
        spotFinderService = SpotFinderFactory.getSpotFinder(type);
    }

    public void enterTransaction(String license,String make,String model,String color,VehicleType vehicleType){
        Transaction newTransaction;
        Vehicle vehicle = new Vehicle(license,make,model,color,vehicleType);
        Spot newSpot = spotFinderService.findNextSpot(vehicle);
        newTransaction = new Transaction(spot,vehicle);

        currentTransactions.put(license,newTransaction);
    }

    public void exitTransaction(String license){
        if(!currentTransactions.containsKey(license)){
            throw new ApplicationException("Invalid license plate")
        }
        Transactions transaction = currentTransactions.get(license);
        currentTransactions.remove(license);
        Double cost = calculateFare(transaction);
        transaction.setCost(cost);
        archiveTransaction(transaction);
        AvailableSpotFinderService.addSpot(transaction.getSpot());
        transaction = null;
    }

 }

 SpotFinderFactory factory{
     Map<FinderType,AvailableSpotFinderService > spotFinderFactory = new HashMap();

     SpotFinderFactory(){
            spotFinderFactory.add(FinderType.CLOSEST , new SpotFinderClosestImpl());
     }

     getSpotFinder(){

     }
 }

// service for finding the next available spot and
// managing available spots in the system
 public class AvailableSpotFinderServiceImpl implements AvailableSpotFinderService {

    private Queue<Spot> smallAvailableSpots; //check for priority queue
    private Queue<Spot> mediumAvailableSpots;
    private Queue<Spot> largeAvailableSpots;
    private Long availableNoOfSpots; 

    public Spot findNextSpot(Vehicle vehicle){

    }

    public Spot addSpot(Spot spot){

    }

 }



