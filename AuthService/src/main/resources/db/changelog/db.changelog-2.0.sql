--liquibase formatted sql

--changeset dshparko:1

insert into roles(role_name)
values ('USER');
insert into roles(role_name)
values ('MODERATOR');
insert into roles(role_name)
values ('ADMIN');