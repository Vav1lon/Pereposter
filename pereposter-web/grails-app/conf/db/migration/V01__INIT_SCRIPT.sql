BEGIN;

CREATE TABLE SITE_ROLE (
  id        INT8         NOT NULL,
  version   INT8         NOT NULL,
  authority VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE SITE_USER (
  id               INT8         NOT NULL,
  version          INT8         NOT NULL,
  account_expired  BOOLEAN      NOT NULL,
  account_locked   BOOLEAN      NOT NULL,
  date_created     TIMESTAMP    NOT NULL,
  email            VARCHAR(255) NOT NULL,
  enabled          BOOLEAN      NOT NULL,
  last_updated     TIMESTAMP    NOT NULL,
  "password"       VARCHAR(512) NOT NULL,
  password_expired BOOLEAN      NOT NULL,
  username         VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE SITE_USER_ROLE (
  role_id INT8 NOT NULL,
  user_id INT8 NOT NULL,
  PRIMARY KEY (role_id, user_id)
);

CREATE TABLE SOCIAL_ACCOUNT (
  id                    INT8         NOT NULL,
  version               INT8         NOT NULL,
  create_date_last_post TIMESTAMP    NOT NULL,
  date_created          TIMESTAMP    NOT NULL,
  enabled               BOOLEAN      NOT NULL,
  last_post_id          VARCHAR(255) NOT NULL,
  last_updated          TIMESTAMP    NOT NULL,
  name                  VARCHAR(255) NOT NULL,
  "password"            VARCHAR(255) NOT NULL,
  social_network        INT4         NOT NULL,
  social_user_id        VARCHAR(255) NOT NULL,
  user_id               INT8         NOT NULL,
  username              VARCHAR(255) NOT NULL,
  accounts_idx          INT4,
  PRIMARY KEY (id)
);

ALTER TABLE SITE_ROLE
ADD CONSTRAINT UK_tjmh5jdtfjglsovdcbp8rnv0n UNIQUE (authority);

ALTER TABLE SITE_USER
ADD CONSTRAINT UK_qmwwe3ehtdpk3ceowvd387bq2 UNIQUE (email);

ALTER TABLE SITE_USER_ROLE
ADD CONSTRAINT FK_8hf2hq4284e3l3nie4nwv2v5o
FOREIGN KEY (role_id)
REFERENCES SITE_ROLE;

ALTER TABLE SITE_USER_ROLE
ADD CONSTRAINT FK_ap16f0f52rs7amg0tlc7pkfv0
FOREIGN KEY (user_id)
REFERENCES SITE_USER;

ALTER TABLE SOCIAL_ACCOUNT
ADD CONSTRAINT FK_8ll98t0t72rwinpmfkr31xs57
FOREIGN KEY (user_id)
REFERENCES SITE_USER;

CREATE SEQUENCE role_seq;

CREATE SEQUENCE social_account_seq;

CREATE SEQUENCE user_seq;


COMMIT;
