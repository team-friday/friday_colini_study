set mode MYSQL;

DROP TABLE IF EXISTS CHANNEL;
DROP TABLE IF EXISTS CHANNEL_MEMBER;

CREATE TABLE CHANNEL (
  channelId BIGINT NOT NULL AUTO_INCREMENT,
  userName VARCHAR(255) NOT NULL,
  channelName VARCHAR(255) NOT NULL,
  createdAt TIMESTAMP NOT NULL default now(),
  PRIMARY KEY (channelId)
);

CREATE TABLE CHANNEL_MEMBER (
  id BIGINT NOT NULL AUTO_INCREMENT,
  channelId BIGINT NOT NULL,
  userName VARCHAR(255) NOT NULL,
  authority VARCHAR(1) NOT NULL, --  authority is M,W,R (MANAGER, WRITER, READER)
  registerAt TIMESTAMP NOT NULL default now(),
  modifiedAt TIMESTAMP NOT NULL default now(),
);


alter table CHANNEL add constraint UK_CHANNEL_USER
  unique key (userName, channelId);


alter table CHANNEL_MEMBER add constraint FK_CHANNEL_MEMBER
  foreign key (channelId)
  references CHANNEL;