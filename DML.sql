INSERT INTO users (username, name, password, role)
VALUES
('adam-admin', 'Adam', 'let me in', 'admin'),
('Philomena-member', 'Philomena', 'Philomena', 'member'),
('Paul-member', 'Paul', 'Paul', 'member'),
('Sean-trainer','Sean', 'Sean', 'trainer'),
('Carol-trainer', 'Carol', 'Carol', 'trainer');

INSERT INTO trainer_availablities(trainer_id, available_time)
VALUES
('Sean-trainer', '02:00:00'),
('Sean-trainer', '03:00:00'),
('Carol-trainer', '10:00:00' ),
('Carol-trainer', '11:00:00');

INSERT INTO sessions(instructor_name, start_time, exercise_type, cost)
VALUES
('Sean', '02:00:00', 'Running', 50),
('Carol', '11:00:00', 'Lifting', 35);

INSERT INTO teaches(trainer_id, session_id)
VALUES
('Sean-trainer', 1),
('Carol-trainer', 2);

INSERT INTO members(member_id, routine, goal_time, 
goal_weight, goal_max_lift, cur_time, cur_weight, cur_max_lift)
VALUES
('Philomena-member', 'Run 4 laps and 3 sets of bench press','25 minutes', 
160, 100, '30 minutes', 150, 125),
('Paul-member', '10 jumping jacks and 5 push ups','30 minutes', 
200, 80, '35 minutes', 210, 100);

INSERT INTO takes(member_id, session_id)
VALUES
('Philomena-member', 1),
('Paul-member', 2);


INSERT INTO bills(member_id, amount_owed, session_name)
VALUES
('Paul-member', 35, 'Lifting');

INSERT INTO rooms(room_name)
VALUES
('Weight room'),
('Pool'),
('Basketball court');

INSERT INTO room_bookings(room_name, time_booked)
VALUES
('Weight room', '04:00:00');

INSERT INTO machines(machine_type, last_update)
VALUES
('Treadmill', '2022-01-20'),
('Bench', '2024-03-15'),
('Bike', '2023-04-01');