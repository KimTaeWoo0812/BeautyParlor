
DROP TABLE IF EXISTS user;
CREATE TABLE user (
num int NOT NULL auto_increment,
phone_Id varchar(200),
beautyNum varchar(20) NOT NULL,
id varchar(20) NOT NULL,
name varchar(20) NOT NULL, 
sex varchar(20) NOT NULL, 
isMaster varchar(20) NOT NULL, 
joindate varchar(20) NOT NULL,
contact_date varchar(20) NOT NULL, 
penalty_count int NOT NULL,
penalty_create_date varchar(40) NOT NULL,
temp varchar(20) NOT NULL, 
PRIMARY KEY(num));

DROP TABLE IF EXISTS beauty;
CREATE TABLE beauty (
num int NOT NULL auto_increment,
title varchar(20) NOT NULL,
working_time_from varchar(20) NOT NULL, 
working_time_to varchar(20) NOT NULL, 
loc varchar(60) NOT NULL, 
booking_method varchar(20) NOT NULL, 
booking_delay_time varchar(20) NOT NULL,
booking_delay_time_for_cut varchar(20) NOT NULL,
master_id varchar(20) NOT NULL, 
master_name varchar(20) NOT NULL, 
master_phone_id varchar(200) NOT NULL, 
phone varchar(20) NOT NULL, 
create_date varchar(20) NOT NULL, 
cumulative_booking_count int NOT NULL, 
dayOff  varchar(200) NOT NULL, 
isNotice varchar(10) NOT NULL, 
notice varchar(512) NOT NULL, 
PRIMARY KEY(num));

insert into beauty values(NULL,'컬러링헤어','10:00','20:00','포항시 남구 오천읍 구정리','1','30','10','01020085676','한채연','fkr9GWAeQ9M:APA91bFqku1hMZ2P8HxARu-zUft35gRKbSQ_vzTlm7nTEViuNU0GCKt20akQRYwW-I2MPRE7FTqJ19Dsl6Q9Gh5IxdeZJT9-7xkUXukPDHxZKgHC0UFVKIaDHyU_116AmBvqRij9jqZP','0542928867','2017-01-27','0','매주 화요일 휴무','0','ㅁㅁ');
insert into booking values(NULL,'1','test미용실','+821052047170','김태우','남자','2017-01-24','10:00','10:30','2017-01-24','memo','기타','0');
insert into booking values(NULL,'1','컬러링헤어','01052047170','김태우','남자','2017-01-30','11:00','11:50','2017-01-30','memo','기타','0');
update user set isMaster=0;




DROP TABLE IF EXISTS booking;
CREATE TABLE booking (
num int NOT NULL auto_increment,
beauty_num int NOT NULL,
beauty_title varchar(40) NOT NULL,
user_id varchar(20) NOT NULL,
user_name varchar(20) NOT NULL, 
user_sex varchar(20) NOT NULL, 
booking_date varchar(20) NOT NULL, 
booking_time varchar(20) NOT NULL, 
end_time varchar(20) NOT NULL, 
create_date varchar(20) NOT NULL,
memo varchar(512) NOT NULL, 
hair_style varchar(40) NOT NULL, 
isDone varchar(20) NOT NULL, 
PRIMARY KEY(num));




