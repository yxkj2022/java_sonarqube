CREATE TABLE "ACTIVITIES" (
  "ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1),
  "LOG_KEY" VARCHAR(250),
  "PROFILE_KEY" VARCHAR(255),
  "CREATED_AT" TIMESTAMP,
  "USER_LOGIN" VARCHAR(255),
  "LOG_TYPE" VARCHAR(250),
  "LOG_ACTION" VARCHAR(250),
  "LOG_MESSAGE" VARCHAR(250),
  "DATA_FIELD" CLOB(2147483647)
);
