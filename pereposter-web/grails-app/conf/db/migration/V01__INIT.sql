BEGIN;

create table ROLES (id int8 not null, version int8 not null, authority varchar(255) not null unique, primary key (id));
create table SOCIAL_ACCOUNT (id int8 not null, version int8 not null, create_date_last_post timestamp, enabled bool not null, last_post_id varchar(255), name varchar(255) not null, password varchar(255) not null, social_network int4 not null, social_user_id varchar(255), user_id int8 not null, username varchar(255) not null, primary key (id));
create table USERS (id int8 not null, version int8 not null, account_expired bool not null, account_locked bool not null, active bool not null, enabled bool not null, "password" varchar(255) not null, password_expired bool not null, username varchar(255) not null unique, primary key (id));
create table USER_ROLE (role_id int8 not null, user_id int8 not null, version int8 not null, primary key (role_id, user_id));
alter table SOCIAL_ACCOUNT add constraint FKCA320B3BD9F8CC5F foreign key (user_id) references USERS;
alter table USER_ROLE add constraint FKBC16F46A34CE087F foreign key (role_id) references ROLES;
alter table USER_ROLE add constraint FKBC16F46AD9F8CC5F foreign key (user_id) references USERS;
create sequence role_seq;
create sequence social_account_seq;
create sequence user_seq;


COMMIT;