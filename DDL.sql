CREATE TABLE users(
	username TEXT PRIMARY KEY,
	name TEXT,
	password TEXT,
	role TEXT
);

CREATE TABLE trainer_availablities(
	trainer_id TEXT,
	available_time TIME,
	unique (trainer_id, available_time),
	FOREIGN KEY (trainer_id)
		REFERENCES users(username)
);



CREATE TABLE sessions(
	session_id SERIAL PRIMARY KEY,
	instructor_name TEXT,
	start_time TIME,
	exercise_type TEXT,
	cost INT
);


CREATE TABLE teaches(
	trainer_id TEXT,
	session_id INT,
	FOREIGN KEY (trainer_id)
		REFERENCES users(username),
	FOREIGN KEY (session_id)
		REFERENCES sessions(session_id)
);

CREATE TABLE members(
	member_id TEXT PRIMARY KEY,
	routine TEXT,
	
	
	goal_time TEXT,
	goal_weight INT,
	goal_max_lift INT,
	
	cur_time TEXT,
	cur_weight INT,
	cur_max_lift INT,
	
	FOREIGN KEY (member_id)
		REFERENCES users(username)
	
);

CREATE TABLE takes(
	member_id TEXT,
	session_id INT,
	
	FOREIGN KEY (member_id)
		REFERENCES members(member_id),
	FOREIGN KEY (session_id)
		REFERENCES sessions(session_id)
);

CREATE TABLE bills(
	bill_id SERIAL PRIMARY KEY, 
	member_id TEXT,
	amount_owed INT,
	session_name TEXT,
	FOREIGN KEY (member_id)
		REFERENCES members(member_id)
);

CREATE TABLE rooms(
	room_name TEXT PRIMARY KEY
);

CREATE TABLE room_bookings(
	room_name TEXT,
	time_booked TIME,
	FOREIGN KEY (room_name)
		REFERENCES rooms(room_name),	
	unique (room_name, time_booked)

	
);

create table machines(
	machine_type TEXT,
	last_update DATE,
	PRIMARY KEY(machine_type)
);