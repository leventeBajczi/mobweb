package data;

public class DataManager {

    private static final int HOLIDAY_MAX_VALUE =20;
    private static final int HOLIDAY_DEFAULT_VALUE =15;
    private int holidays=HOLIDAY_DEFAULT_VALUE;
    private static DataManager instance;

    private Person person;

    private DataManager() {
        person = new Person(
                "Test User", "testuser@domain.com",
                "1234 Test, Random Street 1.",
                "123456AB",
                "123456789",
                "1234567890",
                "0123456789");
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }

        return instance;
    }

    public Person getPerson() {
        return person;
    }

    public int getHolidays() {
        return holidays;
    }

    public void setHolidays(int holidays) {
        this.holidays = holidays;
    }
    public int getRemainingHolidays(){
        return HOLIDAY_MAX_VALUE-getHolidays();
    }
}
