import com.sun.jdi.event.ExceptionEvent;
/*
    Adam Miles
    101266150
    COMP 3005 final project version 2
 */


import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
public class Main{

    public static final String PRIVATE_session = "private session";
    private final Connection connection;
    private final Scanner input;

    public Main(String url, String user, String password) throws Exception{
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            input = new Scanner(System.in);

        }
        catch(Exception e){
            throw e;
        }
    }

    // creates a profile for a new member
    public void addMember() throws Exception{
        try{
            boolean shouldExit = true;
            String username;
            PreparedStatement statement;
            String queryString = "SELECT username FROM users WHERE username = ?";
            do{
                System.out.println("Enter your new username: ");
                username = input.nextLine();
                statement = connection.prepareStatement(queryString);
                statement.setString(1,username);
                statement.executeQuery();
                ResultSet resultSet = statement.getResultSet();
                if(resultSet.next()){
                    System.out.println("Username already used, enter a different username");
                    shouldExit = false;
                }
                else{
                    shouldExit = true;
                }
            }while(!shouldExit);

            System.out.println("Enter your first name: ");
            String name = input.nextLine();

            System.out.println("Enter your password: ");
            String password = input.nextLine();


            System.out.println("Welcome to our gym, " + name+"!");
            System.out.println("We would like to get some information about you.");
            System.out.println("Describe your exercise routine");
            String routine = input.nextLine();

            System.out.println("Enter your goal 5k time: ");
            String goal_time = input.nextLine();
            System.out.println("Enter your goal body weight (in lbs): ");
            int goal_weight = Integer.parseInt(input.nextLine());
            System.out.println("Enter what weight you would like to lift (in lbs)");
            int goal_lift = Integer.parseInt(input.nextLine());


            System.out.println("Enter your current 5k time: ");
            String curr_time = input.nextLine();
            System.out.println("Enter your current body weight (in lbs): ");
            int curr_weight = input.nextInt();
            System.out.println("Enter the heaviest weight that you can lift (in lbs)");
            int curr_lift = input.nextInt();



            String insert_user_code = "INSERT INTO users (username, name, password, role)  VALUES (?,?,?,?)";
            String add_member_info_code = "INSERT INTO members(member_id, routine, goal_time, " +
                    "goal_weight, goal_max_lift, cur_time, cur_weight, cur_max_lift) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(insert_user_code);
            statement.setString(1, username);
            statement.setString(2, name);
            statement.setString(3, password);
            statement.setString(4, "member");
            statement.executeUpdate();

            statement = connection.prepareStatement(add_member_info_code);
            statement.setString(1, username);
            statement.setString(2, routine);
            statement.setString(3, goal_time);
            statement.setInt(4, goal_weight);
            statement.setInt(5, goal_lift);
            statement.setString(6, curr_time);
            statement.setInt(7, goal_weight);
            statement.setInt(8, goal_lift);
            statement.executeUpdate();


        }
        catch (Exception e){
            throw e;
        }

        System.out.println("Your account has been created, please log in to view your profile or book a session");

    }

    // prompts user for their username and password and signs in the appropriate type of account if it is valid.
    public void authenticate() throws Exception{

        try{
            String username;
            PreparedStatement statement;
            String queryString = "SELECT * FROM users WHERE username = ?";

            System.out.println("Enter your username: ");
            username = input.nextLine();
            statement = connection.prepareStatement(queryString);
            statement.setString(1,username);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();

            if(resultSet.next()){

                String real_password = resultSet.getString("password");
                String user_type = resultSet.getString("role");
                System.out.println("Enter your password");
                String entered_password = input.nextLine();


                if(real_password.equals(entered_password)){

                    if(user_type.equals("member")){
                        this.useMemberAccount(username);
                    }

                    else if(user_type.equals("trainer")){
                        this.useTrainerAccount(username);
                    }

                    else if (user_type.equals("admin")){
                        this.useAdminAccount(username);
                    }

                }
                else{
                    System.out.println("Username or password is incorrect");
                }

            }
            else{
                System.out.println("Username is incorrect");
            }
        }

        catch (Exception e){
            throw e;
        }


    }

    // calls the functions corresponding to the action that the user would like to perform
    public void useMemberAccount(String username) throws Exception{
        try{

            PreparedStatement statement;
            String queryString = "SELECT * FROM users WHERE username = ?";
            statement = connection.prepareStatement(queryString);
            statement.setString(1,username);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            System.out.println("Hello " + resultSet.getString("name") + "!");
            int choice = 0;

            do{
                System.out.println("Enter 1 to display your dashboard");
                System.out.println("Enter 2 to update your profile");
                System.out.println("Enter 3 to schedule a session");
                System.out.println("Enter 0 to log out");
                choice = input.nextInt();

                if(choice == 1){
                    this.displayProfile(username);
                }
                else if(choice == 2){
                    this.updateProfile(username);
                }
                else if (choice == 3) {
                    this.bookSessions(username);
                }

            }while(choice != 0);


        }

        catch (Exception e){
            throw e;
        }

    }

    // retrieves and displays the member's profile information (the member corresponding to that username)
    public void displayProfile(String username) throws Exception{
        try{
            PreparedStatement statement;
            String queryString = "SELECT * FROM users WHERE username = ?";
            statement = connection.prepareStatement(queryString);
            statement.setString(1,username);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();

            System.out.println("Name: " + resultSet.getString("name"));
            System.out.println();


            queryString = "SELECT * FROM members WHERE member_id = ?";
            statement = connection.prepareStatement(queryString);
            statement.setString(1,username);
            statement.executeQuery();
            resultSet = statement.getResultSet();
            resultSet.next();

            System.out.println("-------Goals--------");
            System.out.println("5k time: " +  resultSet.getString("goal_time"));
            System.out.println("Body weight: " +  resultSet.getInt("goal_weight") +"lbs");
            System.out.println("Maximum lift: " +  resultSet.getInt("goal_max_lift") +"lbs");

            System.out.println("-------Current fitness information--------");
            System.out.println("5k time: " +  resultSet.getString("cur_time"));
            System.out.println("Body weight: " +  resultSet.getInt("cur_weight") +"lbs");
            System.out.println("Maximum lift: " +  resultSet.getInt("cur_max_lift") +"lbs");

            System.out.println("-------Routine--------");
            System.out.println(resultSet.getString("routine"));


        }

        catch(Exception e){
            throw e;
        }


    }

    // update information in the users profile as specified by the user
    public void updateProfile(String member_id) throws Exception{
        String updated_col = " ";
        String newValue = " ";
        int numberValue =0;
        int choice =0;
        boolean isText = true;

        System.out.println("What would you like to update?");
        System.out.println("Enter 1 to update a goal");
        System.out.println("Enter 2 to update your health information");
        System.out.println("Enter 3 to change your routine");
        choice = input.nextInt();

        if(choice == 1){
            System.out.println("Which goal would you like to update");
            System.out.println("Enter 1 to update your 5k goal time");
            System.out.println("Enter 2 to update your body weight goal");
            System.out.println("Enter 3 to update your maximum lift goal");
            choice = input.nextInt();

            if(choice == 1){
                updated_col = "goal_time";
            }
            else if(choice == 2){
                updated_col = "goal_weight";
            }
            else if(choice == 3){
                updated_col = "goal_max_lift";
            }

        }
        else if(choice == 2){
            System.out.println("Which fitness stats would you like to update");
            System.out.println("Enter 1 to update your best 5k time");
            System.out.println("Enter 2 to update your body weight");
            System.out.println("Enter 3 to update your maximum lift ");
            choice = input.nextInt();

            if(choice == 1){
                updated_col = "curr_time";
            }
            else if(choice == 2){
                updated_col = "curr_weight";
            }
            else if(choice == 3){
                updated_col = "curr_max_lift";
            }

        }
        else if (choice ==3) {
            updated_col ="routine";
        }

        input.nextLine();
        System.out.println("Enter your new data");
        newValue = input.nextLine();

        if(updated_col.equals("goal_weight")|| updated_col.equals("goal_max_lift")||updated_col.equals("curr_weight")|| updated_col.equals("curr_max_lift")){
            isText = false;
            numberValue = Integer.parseInt(newValue);
        }

        String updateCode = "UPDATE members SET "+ updated_col+  " =? WHERE member_id =?";
        try{
            PreparedStatement statement = connection.prepareStatement(updateCode);
            if(isText){
                statement.setString(1, newValue);
            }
            else{
                statement.setInt(1,numberValue);
            }
            statement.setString(2, member_id);


            statement.executeUpdate();
        }

        catch (Exception e){
            throw e;
        }


    }

    //allow a member to book sessions
    public void bookSessions(String member_id) throws Exception{
        try{
            System.out.println("Here are the available groups ");

            String queryString = "SELECT * FROM sessions WHERE exercise_type != ?";
            PreparedStatement statement = connection.prepareStatement(queryString);
            statement.setString(1,PRIVATE_session);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();

            while(resultSet.next()){
                System.out.print("Instructor: "+ resultSet.getString("instructor_name")+" ");
                System.out.print("Time:" + resultSet.getTime("start_time")+" ");
                System.out.print("Exercise: " + resultSet.getString("exercise_type")+" ");
                System.out.println("Cost: $" +resultSet.getInt("cost"));
            }

            input.nextLine();
            System.out.println("Enter the name of the instructor for your session");
            String instructor_name = input.nextLine();
            System.out.println("Enter the time of your session");
            String time_str = input.nextLine();
            Time start_time = Time.valueOf(time_str);

            queryString = "SELECT * FROM sessions WHERE exercise_type != ? AND instructor_name =? AND start_time = ?";
            statement = connection.prepareStatement(queryString);
            statement.setString(1,PRIVATE_session);
            statement.setString(2, instructor_name);
            statement.setTime(3, start_time);
            statement.executeQuery();
            resultSet = statement.getResultSet();

            if(resultSet.next()){

                int session_id = resultSet.getInt("session_id");
                int cost = resultSet.getInt("cost");
                String session_type = resultSet.getString("exercise_type");

                String update_string = "INSERT INTO takes(member_id, session_id) VALUES (?,?)";
                statement = connection.prepareStatement(update_string);
                statement.setString(1,member_id);
                statement.setInt(2, session_id);
                statement.executeUpdate();

                int choice = 0;
                do{
                    System.out.println("That session cost you $" + cost);
                    System.out.println("Enter 1 to pay for your session now or 2 to pay at the desk at the gym");
                    choice = input.nextInt();
                    if(choice == 1){
                        System.out.println("Transaction successful!");
                    }
                }while(choice != 1 && choice != 2);

                if(choice == 2){
                    update_string = "INSERT INTO bills(member_id, amount_owed, session_name) VALUES (?,?,?)";
                    statement = connection.prepareStatement(update_string);
                    statement.setString(1,member_id);
                    statement.setInt(2, cost);
                    statement.setString(3, session_type);
                    statement.executeUpdate();
                }

            }
            else{
                System.out.println("Invalid session");
            }

        }

        catch (Exception e){
            throw e;
        }

    }

    //lets choose their desired action once logged in (search for members, set their schedule or log out)
    public void useTrainerAccount(String trainer_id) throws Exception{
        try {
            // get the trainers name
            String querry_string = "Select name from users where username = ? AND role = ?";
            PreparedStatement statement = connection.prepareStatement(querry_string);
            statement.setString(1, trainer_id);
            statement.setString(2, "trainer");
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();

            String name = resultSet.getString("name");



            System.out.println("Welcome " + name + "!");
            int choice = 0;
            do{
                System.out.println("Enter 1 to view the profiles of gym members");
                System.out.println("Enter 2 to schedule your availability");
                System.out.println("Enter 0 to log out");
                choice = input.nextInt();

                if(choice == 1){
                    this.searchMembers();
                }
                else if (choice ==2){
                    this.setAvailabilities(trainer_id, name);
                }

            }while(choice != 0);

        }
        catch (Exception e){
            throw e;
        }
    }

    //lets trainers search view profiles of members with a given name
    public void searchMembers() throws  Exception{
        try{
            input.nextLine();
            System.out.println("Enter the name of the member who's profile you would like to see");
            String name = input.nextLine();
            System.out.println("------------------------ Here are the members with the name  " + name + " ------------------------");
            String queryString = "SELECT username FROM users, members WHERE users.username = members.member_id AND users.name = ?";
            PreparedStatement statement = connection.prepareStatement(queryString);
            statement.setString(1, name);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();

            while(resultSet.next()){
                displayProfile(resultSet.getString("username"));
                System.out.println();
            }
            System.out.println("------------------------ End of list ------------------------");


        }
        catch (Exception e){
            throw e;
        }
    }

    // allows trainers with a given name and id to add or delete timeslots on which they have their sessions
    // this is provided that they do not already have a session booked at that time
    public void setAvailabilities(String trainer_id, String trainer_name) throws  Exception{
        try{

            String queryString = "Select available_time from trainer_availablities where trainer_id = ?";
            PreparedStatement statement = connection.prepareStatement(queryString);
            statement.setString(1,trainer_id);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            System.out.println("Here are your available times:");
            while(resultSet.next()){
                System.out.println(resultSet.getTime("available_time"));
            }

            System.out.println("Enter 1 to add or 2 to delete a timeslot");
            int choice = input.nextInt();
            input.nextLine();

            if(choice == 1){
                System.out.println("Enter the time you would like to add ");
                String time_str = input.nextLine();
                Time time = Time.valueOf(time_str);

                try{
                    String updateString = "insert into trainer_availablities (trainer_id, available_time) values (?,?)";
                    statement = connection.prepareStatement(updateString);
                    statement.setString(1, trainer_id);
                    statement.setTime(2,time);
                    statement.executeUpdate();
                }
                catch (Exception e){
                    System.out.println("Time already added");
                }

            }

            else if (choice == 2) {
                System.out.println("Enter the time you would like to delete ");
                String time_str = input.nextLine();
                Time time = Time.valueOf(time_str);
                // check that the trainer is not scheduled on the timeslot they want to remove
                boolean has_session;
                queryString = "select * from sessions where instructor_name = ? AND start_time = ?";
                statement = connection.prepareStatement(queryString);
                statement.setString(1, trainer_name);
                statement.setTime(2, time);
                statement.executeQuery();
                resultSet = statement.getResultSet();

                if(resultSet.next()){
                    System.out.println("You have a session scheduled at that time slot, you can not delete that.");
                }
                else{
                    String updateString = "delete from trainer_availablities where trainer_id = ? and available_time = ?";
                    statement = connection.prepareStatement(updateString);
                    statement.setString(1, trainer_id);
                    statement.setTime(2,time);
                    statement.executeUpdate();

                    System.out.println("Timeslot removed");
                }



            }


        }
        catch (Exception e){
            throw e;
        }
    }

    public void useAdminAccount(String admin_id) throws Exception{
        try{
            int choice =0;
            do {
                System.out.println("Hello admin!");
                System.out.println("Enter 1 to schedule a group session");
                System.out.println("Enter 2 to processes a payment");
                System.out.println("Enter 3 to book a room");
                System.out.println("Enter 4 to log an update to a machine");
                System.out.println("Enter 0 to log out");
                choice = input.nextInt();
                input.nextLine();
                if(choice == 1){
                    this.makeGroupSessions();
                } else if (choice == 2) {
                    this.processBills();
                }
                else if (choice == 3){
                    this.manageRooms();
                }
                else if(choice == 4){
                    this.updateEquipment();
                }
            } while (choice != 0);


        }
        catch (Exception e){
            throw e;
        }
    }

    // allows admin staff to schedule a group session with any trainer who is available and not
    // scheduled at the time in which they are creating the session
    public void makeGroupSessions() throws  Exception{
        try {
            System.out.println("Here are the instructors at the gym:");
            String query_str = "SELECT username, name FROM users WHERE role = ?";
            PreparedStatement statement = connection.prepareStatement(query_str);
            statement.setString(1, "trainer");
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                System.out.println("id: " + resultSet.getString("username") + " name: " + resultSet.getString("name"));
            }

            System.out.println();
            System.out.println("Enter the id for the instructor you would like to schedule: ");
            String instructor_id = input.nextLine();
            // get the name of the instructor
            statement = connection.prepareStatement("select name from users where username = ?");
            statement.setString(1, instructor_id);
            statement.executeQuery();
            resultSet = statement.getResultSet();
            resultSet.next();
            String instructor_name = resultSet.getString("name");


            System.out.println("Here are the instructors available timeslots");
            // create a list of available times for the instructor on which they do not have a session scheduled
            query_str = "Select available_time from trainer_availablities where trainer_id = ? AND available_time not in (Select start_time from teaches, sessions where trainer_id = ? AND teaches.session_id = sessions.session_id)";
            statement = connection.prepareStatement(query_str);
            statement.setString(1,instructor_id );
            statement.setString(2, instructor_id);
            statement.executeQuery();
            resultSet = statement.getResultSet();

            while(resultSet.next()){
                System.out.println(resultSet.getTime("available_time"));
            }
            System.out.println("Which time would you like to hold the session on?");
            String time_str = input.nextLine();
            Time time = Time.valueOf(time_str);

            System.out.println("What type of exercise will be done in the session?");
            String exercise_type = input.nextLine();

            System.out.println("How much would you like to charge for the session?");
            int cost = input.nextInt();

            // add the session to the sessions table
            String insert_to_sessions = "INSERT INTO sessions(instructor_name, start_time, exercise_type, cost) VALUES (?,?,?,?)";
            statement = connection.prepareStatement(insert_to_sessions,Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,instructor_name);
            statement.setTime(2,time);
            statement.setString(3,exercise_type);
            statement.setInt(4,cost);
            statement.executeUpdate();
            resultSet =statement.getGeneratedKeys();
            resultSet.next();
            int session_id = resultSet.getInt("session_id");


            // add the session id and instructor id to the teaches relation
            String insert_to_teaches = "insert into teaches (trainer_id, session_id) values (?,?)";
            statement = connection.prepareStatement(insert_to_teaches);
            statement.setString(1,instructor_id);
            statement.setInt(2, session_id);
            statement.executeUpdate();



            System.out.println("Session created!");


        }
        catch (Exception e){
            throw e;
        }
    }

    public void processBills() throws Exception{
        try{

            // allow the admin to search by name for the id needed to find the bill
            System.out.println("Enter the name for the costumer who is paying");
            String member_name = input.nextLine();
            System.out.println("Here are the ids for customers with that name:");
            String query_string =" Select username from users where role =? and name = ?";
            PreparedStatement statement = connection.prepareStatement(query_string);
            statement.setString(1,"member");
            statement.setString(2,member_name);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()){
                System.out.println(resultSet.getString("username"));
            }

            System.out.println("Enter the appropriate id");
            String member_id = input.nextLine();

            // find all bills that the member owes
            query_string = "select * from bills where member_id = ?";
            statement = connection.prepareStatement(query_string);
            statement.setString(1,member_id);
            statement.executeQuery();
            resultSet = statement.getResultSet();

            while (resultSet.next()){
                System.out.println(resultSet.getInt("bill_id")+" | $" + resultSet.getInt("amount_owed") + " | " + resultSet.getString("session_name"));
            }

            System.out.println("Enter the number associated with the payment you would like to process");
            int bill_id = input.nextInt();

            String deletion_str = "delete from bills where bill_id = ?";
            statement = connection.prepareStatement(deletion_str);
            statement.setInt(1,  bill_id);
            statement.executeUpdate();

            System.out.println("Transaction complete!");
        }
        catch (Exception e){
            throw e;
        }
    }

    public void manageRooms() throws Exception{
        try{
            System.out.println("Here is a list of rooms at the gym");
            String query = "select * from rooms";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();

            while(resultSet.next()){
                System.out.println(resultSet.getString("room_name"));
            }
            System.out.println("Enter the name of the room you would like to book");
            String room_booked = input.nextLine();

            System.out.println("That room was booked for the following times");
            query = "select time_booked from room_bookings where room_name = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1,room_booked);
            statement.executeQuery();
            resultSet = statement.getResultSet();

            while(resultSet.next()){
                System.out.println(resultSet.getTime("time_booked"));
            }

            System.out.println("Enter the time you would like to book the room for");
            String time_str = input.nextLine();
            Time time_booked = Time.valueOf(time_str);

            try{
                String insert_string = "insert into room_bookings (room_name, time_booked) values (?,?)";
                statement = connection.prepareStatement(insert_string);
                statement.setString(1, room_booked);
                statement.setTime(2, time_booked);
                statement.executeUpdate();
                System.out.println("Booking complete!");

            }
            catch(Exception e){
                System.out.println("Room already booked at that time");
            }


        }
        catch (Exception e){
            throw e;
        }
    }

    public void updateEquipment() throws Exception{
        try {
            System.out.println("Here are the machines with their most recent updates");
            String query_str = "Select * from machines";
            PreparedStatement statement = connection.prepareStatement(query_str);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()){
                System.out.println(resultSet.getString("machine_type") +" | " + resultSet.getDate("last_update"));
            }

            System.out.println("Enter 1 to update a machine, enter 0 to return to your admin menu");
            int choice = input.nextInt();
            input.nextLine();
            if(choice == 1){
                System.out.println("Enter the machine being updated");
                String updated_machine = input.nextLine();

                String update_str = "update machines set last_update = CURRENT_DATE where machine_type = ?";
                statement = connection.prepareStatement(update_str);
                statement.setString(1, updated_machine);
                statement.executeUpdate();
                System.out.println("Machine update recorded");

            }
            else{
                return;
            }

        }

        catch (Exception e){
            throw e;
        }
    }

    public static void main(String[] args){
        String databaseUser = "postgres";
        String databasePassword = "postgres";
        String url = "jdbc:postgresql://localhost:5432/Gym";
        try{
            Main access = new Main (url, databaseUser, databasePassword);

            boolean exit = false;
            int choice = 0;

            while(!exit){
                System.out.println("Enter 0 to exit, 1 to log in or 2 to create an account");
                choice = access.input.nextInt();
                access.input.nextLine();
                switch(choice){
                    case 0:
                        exit = true;
                        break;
                    case 1:
                        access.authenticate();
                        break;

                    case 2:
                        access.addMember();
                        break;
                }

            }

        }

        catch(Exception e){
            System.out.println(e);
        }
    }

}