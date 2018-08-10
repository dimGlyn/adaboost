/**
 * Created by dimitris on 27-Jan-17.
 */
public class Car {
    private String buying,maint,doors,persons,lug_boot,safety,acceptabillity;

    public Car(String buying, String maint, String doors, String persons, String lug_boot, String safety, String acceptabillity) {
        setBuying(buying);
        setMaint(maint);
        setDoors(doors);
        setPersons(persons);
        setLug_boot(lug_boot);
        setSafety(safety);
        setAcceptabillity(acceptabillity);
    }

    public Car(String buying, String maint, String doors, String persons, String lug_boot, String safety) {
        setBuying(buying);
        setMaint(maint);
        setDoors(doors);
        setPersons(persons);
        setLug_boot(lug_boot);
        setSafety(safety);
    }

    /**
     * Returns array with the values of each attribute
     *
     * @return
     */
    public String [] getAttributes(){
        String [] attr = {
                this.getBuying(),
                this.getMaint(),
                this.getDoors(),
                this.getPersons(),
                this.getLug_boot(),
                this.getSafety(),
                this.getAcceptabillity()
        };
        return attr;

    }

    public String getBuying() {
        return buying;
    }

    public void setBuying(String buying) {
        this.buying = buying;
    }

    public String getMaint() {
        return maint;
    }

    public void setMaint(String maint) {
        this.maint = maint;
    }

    public String getDoors() {
        return doors;
    }

    public void setDoors(String doors) {
        this.doors = doors;
    }

    public String getPersons() {
        return persons;
    }

    public void setPersons(String persons) {
        this.persons = persons;
    }

    public String getLug_boot() {
        return lug_boot;
    }

    @Override
    public String toString() {
        return "Car{" +
                "buying='" + buying + '\'' +
                ", maint='" + maint + '\'' +
                ", doors='" + doors + '\'' +
                ", persons='" + persons + '\'' +
                ", lug_boot='" + lug_boot + '\'' +
                ", safety='" + safety + '\'' +
                '}';
    }

    public void setLug_boot(String lug_boot) {
        this.lug_boot = lug_boot;
    }

    public String getSafety() {
        return safety;
    }

    public void setSafety(String safety) {
        this.safety = safety;
    }

    public String getAcceptabillity() {
        return acceptabillity;
    }

    public void setAcceptabillity(String acceptabillity) {
        this.acceptabillity = acceptabillity;
    }
}
