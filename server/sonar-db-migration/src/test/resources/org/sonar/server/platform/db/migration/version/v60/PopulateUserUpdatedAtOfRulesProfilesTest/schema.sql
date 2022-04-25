CREATE TABLE "ACTIVITIES" (
  "ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1),
  "LOG_KEY" VARCHAR(250),
  "PROFILE_KEY" VARCHAR(255) NOT NULL,
  "CREATED_AT" TIMESTAMP,
  "USER_LOGIN" VARCHAR(255),
  "LOG_TYPE" VARCHAR(250),
  "LOG_ACTION" VARCHAR(250),
  "LOG_MESSAGE" VARCHAR(250),
  "DATA_FIELD" CLOB(2147483647)
);

CREATE TABLE "RULES_PROFILES" (
  "ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1),
  "NAME" VARCHAR(100) NOT NULL,
  "LANGUAGE" VARCHAR(20),
  "KEE" VARCHAR(255) NOT NULL,
  "PARENT_KEE" VARCHAR(255),
  "RULES_UPDATED_AT" VARCHAR(100),
  "IS_DEFAULT" BOOLEAN NOT NULL DEFAULT FALSE,
  "CREATED_AT" TIMESTAMP,
  "UPDATED_AT" TIMESTAMP,
  "LAST_USED" BIGINT,
  "USER_UPDATED_AT" BIGINT
);
