-- password is 123456 - for all users

insert into user
values (1, 1, 'Anna', '$2a$10$RLAPaYiPp50zHM0RwVh.N.0zovs9.mB/PCtfXof3bHLEiWs3P9YI.', 'Karamyan', 'aaa@gmail.com');

insert into user
values (2, 1, 'Manager', '$2a$10$RLAPaYiPp50zHM0RwVh.N.0zovs9.mB/PCtfXof3bHLEiWs3P9YI.', 'Manageryan', 'manager@gmail.com')

insert into authority
values (1, 0), (2, 1);

insert into user_authorities
values (1,1), (2,2);

