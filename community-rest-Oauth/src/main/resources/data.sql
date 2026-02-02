insert into user_details(id, username, birth_date, email_address, phone_number, is_male)
values(10001, 'Odung', current_date(), 'odung@gmail.com', '010-5959-5252', true);

insert into user_details(id, username, birth_date, email_address, phone_number, is_male)
values(10002, 'Bbidung', current_date(), 'bbidung@gmail.com', '010-5252-5252', false);

insert into user_details(id, username, birth_date, email_address, phone_number, is_male)
values(10003, 'Tube', current_date(), 'tube@gmail.com', '010-8888-3333', true);

insert into post(id, username, description, upload_date, user_id) 
values(20001, 'Odung', 'I want to go home', current_date(), 10001);

insert into post(id,username,description,upload_date, user_id) 
values(20002, 'Bbidung', 'I want some money!!', current_date(), 10002);

insert into post(id,username,description,upload_date, user_id) 
values(20003, 'Tube', 'I don''t like my small feet', current_date(), 10003);

insert into post(id, username, description, upload_date, user_id)
values(20004, 'Odung', 'I''d like to go to the concert', current_date(), 10001);