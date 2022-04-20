delete from user_role;
delete from usr;

insert into usr(id, username, password, active)
values
    (1,'Nick', '22', true ),
    (2,'w0', 'w', true ),
    (3,'Alex', '307', true ),
    (4,'Admin', 'passwordUnProtected', true );

insert into user_role(user_id, roles)
values
      (1, 'SUPERADMIN'),
      (2, 'MODERATOR'),
      (3, 'USER'),
      (4, 'ADMIN');