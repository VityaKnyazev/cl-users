CREATE TABLE IF NOT EXISTS users(
id BIGSERIAL NOT NULL,
name CHARACTER VARYING(30) NOT NULL,
password CHARACTER VARYING(68) NOT NULL,
email CHARACTER VARYING(30) NOT NULL,
enabled BOOLEAN NOT NULL DEFAULT TRUE,

CHECK(email ~* '^[\w_-]{0,}@[a-z]+\.\w+$'),

PRIMARY KEY(id),

UNIQUE(name),
UNIQUE(email)
);

CREATE TABLE IF NOT EXISTS role(
id BIGSERIAL NOT NULL,
name CHARACTER VARYING(20) NOT NULL,

PRIMARY KEY(id),

UNIQUE(name)
);

CREATE TABLE IF NOT EXISTS users_role(
user_id BIGSERIAL NOT NULL,
role_id BIGSERIAL NOT NULL,

PRIMARY KEY(user_id, role_id),

CONSTRAINT fk_user
FOREIGN KEY (user_id)
REFERENCES users (id)
ON DELETE NO ACTION ON UPDATE NO ACTION,

CONSTRAINT fk_role
FOREIGN KEY (role_id)
REFERENCES role (id)
ON DELETE NO ACTION ON UPDATE NO ACTION
);