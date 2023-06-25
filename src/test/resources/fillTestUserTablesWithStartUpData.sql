Insert Into role (name) VALUES('ROLE_ADMIN');
Insert Into role (name) VALUES('ROLE_JOURNALIST');
Insert Into role (name) VALUES('ROLE_SUBSCRIBER');

Insert Into users (name, password, email) VALUES('Admin', '{bcrypt}$2a$12$7XwDKymAX8auZloalU7KA.davchaW0QU5Q7CuCJ1PrFOObS.ALz9W', 'admin@mail.ru');
Insert Into users (name, password, email) VALUES('Ivan', '{bcrypt}$2a$12$zkw1GKvvx6hiEi4lp7Gw7.6qM2SsmiCnSOeLGht5kSaMf0yyKvm4S', 'ivan@mail.ru');
Insert Into users (name, password, email) VALUES('Mark', '{bcrypt}$2a$12$MJnItlkWxMctljrdtYrRhesD7wpepV2Xe/AS6HtwTUiQv3up8Ukiy', 'mark@yandex.ru');

Insert Into users_role(user_id, role_id) VALUES(1, 1);
Insert Into users_role(user_id, role_id) VALUES(2, 2);
Insert Into users_role(user_id, role_id) VALUES(3, 3);
