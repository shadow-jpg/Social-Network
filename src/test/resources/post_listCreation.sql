delete from message;

insert into message(id, tag, text, visibility, user_id) values
        (1, 'firstMessageEver', 'hello, i am admin!',true, 1),
        (2, 'Life', 'I had a beautifully day',true, 1),
        (3, 'moderatorHere', 'No dirty words...',true, 2),
        (4, 'fourthHackage', 'another in this universe. its me mario',true, 3);
