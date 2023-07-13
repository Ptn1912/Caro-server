CREATE TABLE ta_lpn_user(
    I_ID int AUTO_INCREMENT PRIMARY KEY,
    T_UserName varchar(255) UNIQUE,
    T_Pass varchar(255),
    T_NickName varchar(255),
    avatar varchar(255),
    numberOfGame int DEFAULT 0,
    numberOfWin int DEFAULT 0,
    numberOfDraw int DEFAULT 0,
    IsOnline int DEFAULT 0,
    IsPlaying int DEFAULT 0
);
CREATE TABLE ta_lpn_banned_user(
    I_ID_User int PRIMARY KEY NOT NULL,
    FOREIGN KEY (I_ID_User) REFERENCES `ta_lpn_user`(I_ID)
);