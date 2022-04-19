delete from user_role;
delete from usr;

insert into usr(id, username, password, active) values
                                                    (1,'Nick', '22', true ),
                                                    (2,'w0', 'w', true ),
                                                    (3,'Alex', '307', true );

insert into user_role(user_id, roles) values
                                          (1, 'ADMIN'), (1, 'USER'),
                                          (2, 'MODERATOR'), (2, 'USER'),
                                          (3, 'USER');