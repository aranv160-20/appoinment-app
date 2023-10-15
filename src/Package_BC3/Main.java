package Package_BC3;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static ArrayList<User> updateUserArray(){
        Connection conn = null;
        // Initialize Variables
        ArrayList<User> userArr = new ArrayList<>();
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost/grouppbo2?" + "user=root");

            Statement statement1 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet result = statement1.executeQuery("SELECT * FROM user");


            int countedID = 0;
            while(result.next()){
                userArr.add(new User(countedID, result.getString(2), result.getString(3),result.getString(4),result.getString(1)));
                countedID++;
            }

        }catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return userArr;
    }
    public static ArrayList<Appointment> updateAppointmentArray(){
        Connection conn = null;
        // Initialize Variables
        ArrayList<Appointment> appointmentArr = new ArrayList<>();


        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost/grouppbo2?" + "user=root");

            Statement statement1 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet result = statement1.executeQuery("SELECT * FROM appointments");


            int countedID = 0;
            while(result.next()){
                String description = result.getString(2);
                String thisDateStr = result.getString(3);
                String starttimeStr = result.getString(4);
                String endtimeStr = result.getString(5);
                String state = result.getString(6);
                String username = result.getString(7);
                boolean stateBool = state.equals("1");
                try{
                    // Convert Data
                    java.util.Date thisDate = new SimpleDateFormat("yyyy-MM-dd").parse(thisDateStr);
                    Time starttime = new java.sql.Time(new SimpleDateFormat("HH:mm").parse(starttimeStr).getTime());
                    Time endtime = new java.sql.Time(new SimpleDateFormat("HH:mm").parse(endtimeStr).getTime());
                    appointmentArr.add(new Appointment(countedID,description,thisDate, starttime, endtime, stateBool, username, UUID.randomUUID().toString()));
                    countedID++;
                }
                catch(ParseException e){
                    System.out.println("Exception: "+e);
                }

            }
        }catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return appointmentArr;
    }
    public static void updateUserDatabase(ArrayList<User> userList){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost/grouppbo2?" + "user=root");
            PreparedStatement clearTable = conn.prepareStatement("DELETE FROM user");
            clearTable.execute();

            for(User x : userList){
                String query = "insert into user (username, password, role)" + " values (?, ?, ?)";

                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setString (1, x.username);
                preparedStmt.setString (2, x.password);
                preparedStmt.setString (3, x.role);
                preparedStmt.execute();
            }
        }catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
    public static void updateAppointmentDatabase(ArrayList<Appointment> appointmentList){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost/grouppbo2?" + "user=root");
            PreparedStatement clearTable = conn.prepareStatement("DELETE FROM appointments");
            clearTable.execute();
            for(Appointment x : appointmentList){
                String query = " insert into appointments (id, description, date, starttime, endtime, state, createdby)" + " values (?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setString (1, x.realID);
                preparedStmt.setString (2, x.description);
                preparedStmt.setObject (3, x.thisDate);
                preparedStmt.setTime (4, x.starttime);
                preparedStmt.setTime (5, x.endtime);
                preparedStmt.setBoolean (6, x.state);
                preparedStmt.setString (7, x.createdby);

                preparedStmt.execute();
            }
        }catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public static void main(String[] args) {
        ArrayList<User> userList = new ArrayList<>();
        ArrayList<Appointment> appointmentList = new ArrayList<>();

        Scanner scInt = new Scanner(System.in);
        Scanner sc = new Scanner(System.in);

        int currentIDCount = 1;
        int currentAIDCount = 0;
        int loggedID = -1;


        // Initialize Admin
        userList.add(new User(0,"admin","admin","admin",UUID.randomUUID().toString()));

        userList = updateUserArray();
        appointmentList = updateAppointmentArray();

        // Functions


        // Main Program
        while(true){
            // Input Login
            String inputUser, inputPass;
            System.out.print("Enter username: ");
            inputUser = sc.nextLine();
            System.out.print("Enter password: ");
            inputPass = sc.nextLine();

            // Check Login
            boolean validLogin = false;
            for(User x : userList){
                if(x.username.equals(inputUser) && x.password.equals(inputPass)){
                    loggedID = x.id;
                    validLogin = true;
                    break;
                }
            }

            // Login Output
            if(!validLogin){
                System.out.println("Username/Password salah.");
            }
            else if(userList.get(loggedID).role.equals("admin")){
                System.out.println("Welcome!");
                // Admin Menu
                while(true){

                    // Input Command
                    System.out.print("Enter your command: ");
                    String commandStr = sc.nextLine();
                    String[] commandArr = commandStr.split(" ");
                    boolean commandSuccess = false;

                    if(commandArr[0].equals("help")){
                        System.out.println("[admin] reg user [username] [password]");
                        System.out.println("\tRegister user with username and password\n");
                        System.out.println("[admin] change userpassword [username] [newpassword]");
                        System.out.println("\tChange password from other users\n");
                        System.out.println("[admin] change role [username] [new role]");
                        System.out.println("\tChange user role: ADMIN or REGULAR\n");
                        System.out.println("change password [oldpassword] [newpassword]");
                        System.out.println("\tChange Password\n");
                        System.out.println("new [description]");
                        System.out.println("\tCreate new appointment\n");
                        System.out.println("cancel [no.app]");
                        System.out.println("\tCancel appointment\n");
                        System.out.println("peek -a");
                        System.out.println("\tSee all upcoming appointments\n");
                        System.out.println("peek -ad");
                        System.out.println("\tSee today upcoming appointments\n");
                        System.out.println("see [no.app]");
                        System.out.println("\tSee an appointment\n");
                        System.out.println("export");
                        System.out.println("\tDownload appointments in a file\n");
                        commandSuccess = true;
                    }

                    // Add User - reg user [username] [pass]
                    if(commandArr[0].equals("reg") && commandArr[1].equals("user") && commandArr.length == 4){
                        userList.add(new User(currentIDCount,commandArr[2],commandArr[3],"regular",UUID.randomUUID().toString()));
                        currentIDCount++;
                        commandSuccess = true;
                        updateUserDatabase(userList);
                    }

                    // Change Password - change userpassword [user] [newpassword]
                    if(commandArr[0].equals("change") && commandArr[1].equals("userpassword") && commandArr.length == 4){
                        int userIndex = -1;

                        // Find User
                        for(User x : userList){
                            if(x.username.equals(commandArr[2])){
                                userIndex = x.id;
                                break;
                            }
                        }

                        // Output
                        if(userIndex == -1){
                            System.out.println("User tidak ditemukan!");
                        }
                        else{
                            userList.get(userIndex).password = commandArr[3];
                            System.out.println("Update password successfully.");
                        }
                        commandSuccess = true;
                        updateUserDatabase(userList);
                    }

                    // Change Role - change role [user] [newrole]
                    if(commandArr[0].equals("change") && commandArr[1].equals("role") && commandArr.length == 4){
                        int userIndex = -1;

                        // Find User
                        for(User x : userList){
                            if(x.username.equals(commandArr[2])){
                                userIndex = x.id;
                                break;
                            }
                        }

                        // Output
                        if(userIndex == -1){
                            System.out.println("User tidak ditemukan!");
                        }
                        else{
                            userList.get(userIndex).role = commandArr[3];
                            System.out.println("Update role successfully.");
                        }
                        commandSuccess = true;
                        updateUserDatabase(userList);
                    }

                    // Change Password - change password [oldpassword] [newpassword]
                    if(commandArr[0].equals("change") && commandArr[1].equals("password") && commandArr.length == 4){
                        if(userList.get(loggedID).password.equals(commandArr[2])){
                            userList.get(loggedID).password = commandArr[3];
                            System.out.println("Update password successfully.");
                        }
                        else{
                            System.out.println("Password lama salah!");
                        }
                        commandSuccess = true;
                        updateUserDatabase(userList);
                    }


                    // Command Tidak Dikenali
                    if(!commandSuccess){
                        System.out.println("Command tidak dikenali!");
                    }
                }
            }
            else{
                System.out.println("Welcome!");
                // User Menu
                while(true){

                    // Input Command
                    System.out.print("Enter your command: ");
                    String commandStr = sc.nextLine();
                    String[] commandArr = commandStr.split(" ");
                    boolean commandSuccess = false;

                    if(commandArr.length == 1){
                        if(commandArr[0].equals("help")){
                            System.out.println("[admin] reg user [username] [password]");
                            System.out.println("\tRegister user with username and password\n");
                            System.out.println("[admin] change userpassword [username] [newpassword]");
                            System.out.println("\tChange password from other users\n");
                            System.out.println("[admin] change role [username] [new role]");
                            System.out.println("\tChange user role: ADMIN or REGULAR\n");
                            System.out.println("change password [oldpassword] [newpassword]");
                            System.out.println("\tChange Password\n");
                            System.out.println("new [description]");
                            System.out.println("\tCreate new appointment\n");
                            System.out.println("cancel [no.app]");
                            System.out.println("\tCancel appointment\n");
                            System.out.println("peek -a");
                            System.out.println("\tSee all upcoming appointments\n");
                            System.out.println("peek -ad");
                            System.out.println("\tSee today upcoming appointments\n");
                            System.out.println("see [no.app]");
                            System.out.println("\tSee an appointment\n");
                            System.out.println("export");
                            System.out.println("\tDownload appointments in a file\n");
                            commandSuccess = true;
                        }
                    }
                    else{
                        // Change Password - change password [oldpassword] [newpassword]
                        if(commandArr[0].equals("change") && commandArr[1].equals("password") && commandArr.length == 4){
                            if(userList.get(loggedID).password.equals(commandArr[2])){
                                userList.get(loggedID).password = commandArr[3];
                                System.out.println("Update password successfully.");
                            }
                            else{
                                System.out.println("Password lama salah!");
                            }
                            commandSuccess = true;
                            updateUserDatabase(userList);
                        }

                        // New Agenda - new [description]
                        if(commandArr[0].equals("new") && commandArr.length > 1){

                            // Get Description
                            String description = "";
                            for(int i=1;i<commandArr.length;i++){
                                if(i > 1){description += " ";}
                                description += commandArr[i];
                            }

                            // Get Data
                            System.out.print("Enter date (format: dd-mm-yyyy): ");
                            String thisDateStr = sc.nextLine();
                            System.out.print("Enter start time (format: hh:mm): ");
                            String starttimeStr = sc.nextLine();
                            System.out.print("Enter end time (format: hh:mm): ");
                            String endtimeStr = sc.nextLine();


                            try{
                                // Convert Data
                                java.util.Date thisDate = new SimpleDateFormat("dd-MM-yyyy").parse(thisDateStr);
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                Time starttime = new java.sql.Time(sdf.parse(starttimeStr).getTime());
                                Time endtime = new java.sql.Time(sdf.parse(endtimeStr).getTime());

                                // Insert Data Appointment(int id, String description, Date thisDate, Timestamp starttime, Timestamp endtime, boolean state, String createdby,String realID)
                                appointmentList.add(new Appointment(currentAIDCount,description,thisDate, starttime, endtime, false, userList.get(loggedID).username,UUID.randomUUID().toString()));
                                currentAIDCount++;
                            }
                            catch(ParseException e){
                                System.out.println("Exception: "+e);
                            }
                            commandSuccess = true;
                            updateAppointmentDatabase(appointmentList);
                        }

                        // Peek All - peek -a
                        if(commandArr[0].equals("peek") && commandArr[1].equals("-a")){
                            for(Appointment x : appointmentList){
                                System.out.println(x.realID.substring(0,5)+" - "+x.description);
                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                SimpleDateFormat dftime = new SimpleDateFormat("HH:mm");
                                System.out.println(df.format(x.thisDate)+" "+dftime.format(x.starttime)+" - "+dftime.format(x.endtime));
                                if(x.state == false){
                                    System.out.println("Status: ON");
                                }
                                else{
                                    System.out.println("Status: CANCELLED");
                                }
                                System.out.println();
                            }
                            commandSuccess = true;
                        }

                        // Peek Today - peek -ad
                        if(commandArr[0].equals("peek") && commandArr[1].equals("-ad")){
                            for(Appointment x : appointmentList){
                                java.util.Date currentDate = new Date();
                                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                                if(df.format(currentDate).equals(df.format(x.thisDate))){ // if Date today
                                    System.out.println(x.realID.substring(0,5)+" - "+x.description);
                                    SimpleDateFormat dfd = new SimpleDateFormat("dd-MM-yyyy");
                                    SimpleDateFormat dftime = new SimpleDateFormat("HH:mm");
                                    System.out.println(dfd.format(x.thisDate)+" "+dftime.format(x.starttime)+" - "+dftime.format(x.endtime));
                                    if(x.state == false){
                                        System.out.println("Status: ON");
                                    }
                                    else{
                                        System.out.println("Status: CANCELLED");
                                    }
                                    System.out.println();
                                }
                            }
                            commandSuccess = true;
                        }

                        // See ID - see [id app]
                        if(commandArr[0].equals("see") && commandArr.length == 2){
                            for(Appointment x : appointmentList){
                                if(x.realID.substring(0,5).equals(commandArr[1])){ // if Date today
                                    System.out.println(x.realID.substring(0,5)+" - "+x.description);
                                    SimpleDateFormat dfd = new SimpleDateFormat("dd-MM-yyyy");
                                    SimpleDateFormat dftime = new SimpleDateFormat("hh:mm");
                                    System.out.println(dfd.format(x.thisDate)+" "+dftime.format(x.starttime)+" - "+dftime.format(x.endtime));
                                    if(x.state == false){
                                        System.out.println("Status: ON");
                                    }
                                    else{
                                        System.out.println("Status: CANCELLED");
                                    }
                                    System.out.println();
                                }
                            }
                            commandSuccess = true;
                        }

                        // Cancel ID - cancel [id app]
                        if(commandArr[0].equals("cancel") && commandArr.length == 2){
                            for(Appointment x : appointmentList){
                                if(x.realID.substring(0,5).equals(commandArr[1])){ // if Date today
                                    if(x.state == true){
                                        System.out.println("Appointment already CANCELLED.");
                                    }
                                    else{
                                        x.state = true;
                                    }
                                }
                            }
                            commandSuccess = true;
                            updateAppointmentDatabase(appointmentList);
                        }
                    }

                }

            }
        }
    }

}

