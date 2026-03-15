// 8. Custom unchecked exception
class RoomAlreadyBookedException extends RuntimeException {
    public RoomAlreadyBookedException(String message) {
        super(message);
    }
}

// Additional: Prices enum
enum Prices {
    ECONOMY(100),
    STANDARD(200),
    PRO(350),
    LUX(500),
    ULTRA_LUX(1000);

    private final int value;

    Prices(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

// 1, 4. Abstract Room class (cannot create objects)
abstract class Room {
    private int roomNumber;
    private int maxCapacity;
    private int pricePerNight;
    private boolean isBooked;

    // 2. Constructors
    public Room(int roomNumber, int maxCapacity, int pricePerNight) {
        this.roomNumber = roomNumber;
        this.maxCapacity = maxCapacity;
        this.pricePerNight = pricePerNight;
        this.isBooked = false;
    }

    // Constructor with random capacity
    public Room(int roomNumber, int pricePerNight) {
        this.roomNumber = roomNumber;
        this.maxCapacity = (int) (Math.random() * 4) + 1; // 1 to 4 people
        this.pricePerNight = pricePerNight;
        this.isBooked = false;
    }

    // Getters and setters
    public int getRoomNumber() {
        return roomNumber;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getPricePerNight() {
        return pricePerNight;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    @Override
    public String toString() {
        return "Room{" +
                "number=" + roomNumber +
                ", capacity=" + maxCapacity +
                ", price=" + pricePerNight +
                ", booked=" + isBooked +
                '}';
    }
}

// EconomyRoom - concrete class
class EconomyRoom extends Room {
    public EconomyRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }

    public EconomyRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
}

// 3, 4. Abstract ProRoom (cannot create objects)
abstract class ProRoom extends Room {
    public ProRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }

    public ProRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
}

// StandardRoom - concrete class
class StandardRoom extends ProRoom {
    public StandardRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }

    public StandardRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
}

// LuxRoom - concrete class
class LuxRoom extends ProRoom {
    public LuxRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }

    public LuxRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
}

// UltraLuxRoom - concrete class
class UltraLuxRoom extends LuxRoom {
    public UltraLuxRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }

    public UltraLuxRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
}

// 5. RoomService interface with generics
interface RoomService<T extends Room> {
    void clean(T room);
    void reserve(T room);
    void free(T room);
}

// Additional: LuxRoomService interface
interface LuxRoomService<T extends LuxRoom> {
    void foodDelivery(T room);
}

// 6, 7. RoomServiceImpl class
class RoomServiceImpl implements RoomService<Room> {
    @Override
    public void clean(Room room) {
        System.out.println("Cleaning room " + room.getRoomNumber());
    }

    @Override
    public void reserve(Room room) {
        // 8. Check if room is already booked
        if (room.isBooked()) {
            throw new RoomAlreadyBookedException("Room " + room.getRoomNumber() + " is already booked!");
        }
        room.setBooked(true);
        System.out.println("Room " + room.getRoomNumber() + " has been reserved");
    }

    @Override
    public void free(Room room) {
        room.setBooked(false);
        System.out.println("Room " + room.getRoomNumber() + " has been freed");
    }
}

// Additional: LuxRoomServiceImpl
class LuxRoomServiceImpl implements RoomService<LuxRoom>, LuxRoomService<LuxRoom> {
    @Override
    public void clean(LuxRoom room) {
        System.out.println("[LUX] Premium cleaning for room " + room.getRoomNumber());
    }

    @Override
    public void reserve(LuxRoom room) {
        if (room.isBooked()) {
            throw new RoomAlreadyBookedException("Lux room " + room.getRoomNumber() + " is already booked!");
        }
        room.setBooked(true);
        System.out.println("[LUX] Room " + room.getRoomNumber() + " has been reserved");
    }

    @Override
    public void free(LuxRoom room) {
        room.setBooked(false);
        System.out.println("[LUX] Room " + room.getRoomNumber() + " has been freed");
    }

    @Override
    public void foodDelivery(LuxRoom room) {
        System.out.println("[LUX] Food delivery to room " + room.getRoomNumber() + " - Champagne and strawberries!");
    }
}

// 9. Test class
public class java {
    public static void main(String[] args) {
        RoomService<Room> roomService = new RoomServiceImpl();
        LuxRoomServiceImpl luxService = new LuxRoomServiceImpl();

        System.out.println("=== Testing RoomService with different room types ===\n");

        // Create rooms using Prices enum
        Room economyRoom = new EconomyRoom(101, Prices.ECONOMY.getValue());
        Room standardRoom = new StandardRoom(201, Prices.STANDARD.getValue());
        Room luxRoom = new LuxRoom(301, Prices.LUX.getValue());
        Room ultraLuxRoom = new UltraLuxRoom(401, Prices.ULTRA_LUX.getValue());

        // Test with EconomyRoom
        System.out.println("Testing EconomyRoom:");
        roomService.clean(economyRoom);
        roomService.reserve(economyRoom);
        System.out.println(economyRoom);

        // Try to book already booked room (should throw exception)
        try {
            roomService.reserve(economyRoom);
        } catch (RoomAlreadyBookedException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }

        roomService.free(economyRoom);
        System.out.println();

        // Test with StandardRoom
        System.out.println("Testing StandardRoom:");
        roomService.clean(standardRoom);
        roomService.reserve(standardRoom);
        roomService.free(standardRoom);
        System.out.println();

        // Test with LuxRoom using regular service
        System.out.println("Testing LuxRoom with RoomService:");
        roomService.clean(luxRoom);
        roomService.reserve(luxRoom);
        roomService.free(luxRoom);
        System.out.println();

        // Test with UltraLuxRoom
        System.out.println("Testing UltraLuxRoom:");
        roomService.clean(ultraLuxRoom);
        roomService.reserve(ultraLuxRoom);
        roomService.free(ultraLuxRoom);
        System.out.println();

        System.out.println("=== Testing LuxRoomService ===\n");

        // Test LuxRoomService with LuxRoom
        System.out.println("Testing LuxRoom with LuxRoomService:");
        luxService.clean((LuxRoom) luxRoom);
        luxService.reserve((LuxRoom) luxRoom);
        luxService.foodDelivery((LuxRoom) luxRoom);
        luxService.free((LuxRoom) luxRoom);
        System.out.println();

        // Test LuxRoomService with UltraLuxRoom
        System.out.println("Testing UltraLuxRoom with LuxRoomService:");
        luxService.clean((UltraLuxRoom) ultraLuxRoom);
        luxService.reserve((UltraLuxRoom) ultraLuxRoom);
        luxService.foodDelivery((UltraLuxRoom) ultraLuxRoom);
        luxService.free((UltraLuxRoom) ultraLuxRoom);
        System.out.println();

        // The following would cause compilation error (as expected):
        // luxService.foodDelivery(economyRoom); // Cannot order food from non-luxury room
        // luxService.foodDelivery(standardRoom); // Cannot order food from non-luxury room

        System.out.println("Note: Cannot call foodDelivery() on EconomyRoom or StandardRoom");
        System.out.println("(This is enforced by compile-time type checking)");
    }
}