Insert Into role (name) VALUES('ROLE_ADMIN');
Insert Into role (name) VALUES('ROLE_JOURNALIST');
Insert Into role (name) VALUES('ROLE_SUBSCRIBER');

Insert Into users (name, password, email) VALUES('Admin', '{bcrypt}$2a$12$7XwDKymAX8auZloalU7KA.davchaW0QU5Q7CuCJ1PrFOObS.ALz9W', 'admin@mail.ru');
Insert Into users (name, password, email) VALUES('Ivan', '{bcrypt}$2a$12$zkw1GKvvx6hiEi4lp7Gw7.6qM2SsmiCnSOeLGht5kSaMf0yyKvm4S', 'ivan@mail.ru');
Insert Into users (name, password, email) VALUES('Mark', '{bcrypt}$2a$12$MJnItlkWxMctljrdtYrRhesD7wpepV2Xe/AS6HtwTUiQv3up8Ukiy', 'mark@yandex.ru');
Insert Into users (name, password, email) VALUES('Andrey', '{bcrypt}$2a$12$3defehgG1lEg/nXsJknxXuOy.mKTH09Yq4AqnBDm4HwEqYoFU5eL.', 'andrey@yandex.ru');
Insert Into users (name, password, email) VALUES('Anton', '{bcrypt}$2a$12$nEUIdYxMlIwY.92uVNl/U.GAx4e98TZq9xsu375Ik.dPYiY1cgksK', 'anton@mail.ru');
Insert Into users (name, password, email) VALUES('Vasya', '{bcrypt}$2a$12$zDJ.whWGZm2O1TevGPlD8OF1KW2VV3CThWitcxKuI46ybhVSKY72e', 'vasya@mail.ru');

Insert Into users_role(user_id, role_id) VALUES(1, 1);
Insert Into users_role(user_id, role_id) VALUES(2, 2);
Insert Into users_role(user_id, role_id) VALUES(3, 2);
Insert Into users_role(user_id, role_id) VALUES(4, 3);
Insert Into users_role(user_id, role_id) VALUES(5, 3);
Insert Into users_role(user_id, role_id) VALUES(6, 3);