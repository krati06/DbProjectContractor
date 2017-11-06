create table contractor(
	contractor_id varchar(20),
	name	varchar(20),
	primary key(contractor_id)
);

create table hostel(
	hostel_id	varchar(20),
	name	varchar(20),
	contractor_id	varchar(20),
	primary key(hostel_id),
	foreign key (contractor_id) references contractor on delete cascade
);

create table student(
	student_id	varchar(20),
	name	varchar(50) not null,
	dept_name varchar(20),
	hostel_id	varchar(20),
	foreign key (hostel_id) references hostel on delete cascade,
	primary key(student_id)
);

create table student_password(
	student_id 	varchar(20),
	password	varchar(20),
	foreign key (student_id) references student on delete cascade
);

create table contractor_password(
	contractor_id	varchar(20),
	password	varchar(20),
	foreign key (contractor_id) references contractor on delete cascade
);



create table waitlist(
	student_id	varchar(20),
	hostel_id	varchar(20),
	waitlist_number	numeric,
	week_id	numeric,
	foreign key (student_id) references student on delete cascade,
        foreign key (week_id) references week_interval on delete cascade,
	foreign key (hostel_id) references hostel on delete cascade
);

create table registered(
	student_id	varchar(20),
	hostel_id	varchar(20),
	week_id	numeric,
	foreign key (student_id) references student on delete cascade,
        foreign key (week_id) references week_interval on delete cascade,
	foreign key (hostel_id) references hostel on delete cascade
);

create table feedback(
	student_id	varchar(20),
	hostel_id	varchar(20),
	text	varchar(200),
	time	timestamp,	
	foreign key (student_id) references student on delete cascade,
	foreign key (hostel_id) references hostel on delete cascade
);

create table dishes(
	dish	varchar(20),	
	description varchar(40),
	primary key(dish)
);

create table menu(
	hostel_id	varchar(20),
	day	date,
	timeslot	varchar(20),
	student_limit	numeric,
	dish	varchar(20),	
        week_id numeric,
	foreign key (dish) references dishes on delete cascade,
        foreign key (week_id) references week_interval on delete cascade,
	foreign key (hostel_id) references hostel on delete cascade,
	primary key(hostel_id,day,timeslot)
);

create table week_interval(
	week_id	numeric,
	start_week_no	numeric,
	start_week_year	numeric,
	nd_week_no	numeric,
	end_week_year	numeric,	
	primary key(week_id)
);

create table mess_rating(
	student_id	varchar(20),
        contractor_id	varchar(20),
        week_id numeric,
	rating	numeric,	
	foreign key (student_id) references student on delete cascade,
        foreign key (contractor_id) references contractor on delete cascade,
        foreign key (week_id) references week_interval on delete cascade
);

create table token(
	student_id varchar(20),
	token varchar(20),
	foreign key (student_id) references student on delete cascade
);

CREATE SEQUENCE waitlist_number START 1;
CREATE SEQUENCE week_id START 1;

