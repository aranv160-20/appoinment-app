package Package_BC3;

import java.sql.Time;
import java.util.Date;

public class Appointment {
    int id;
    String description;
    Date thisDate;
    Time starttime;
    Time endtime;
    boolean state;
    String createdby;
    String realID;
    Appointment(int id, String description, Date thisDate, Time starttime, Time endtime, boolean state, String createdby,String realID){
        this.id = id;
        this.description = description;
        this.thisDate = thisDate;
        this.starttime = starttime;
        this.endtime = endtime;
        this.state = state;
        this.createdby = createdby;
        this.realID = realID;
    }
}

