INSERT INTO user (login_type, login_id, password, nickname, profile_image, gender, birth_date, is_deleted)
VALUES ('NORMAL', 'user1', 'password1', 'UserA', null, 'MALE', '1990-01-01', false),
       ('NORMAL', 'user2', 'password2', 'UserB', null, 'FEMALE', '1992-02-02', false),
       ('NORMAL', 'user3', 'password3', 'UserC', null, 'MALE', '1994-03-03', false),
       ('NORMAL', 'user4', 'password4', 'UserD', null, 'FEMALE', '1996-04-04', false),
       ('NORMAL', 'user5', 'password5', 'UserE', null, 'MALE', '1988-05-05', false),
       ('NORMAL', 'user6', 'password6', 'UserF', null, 'FEMALE', '1991-06-06', false),
       ('NORMAL', 'user7', 'password7', 'UserG', null, 'MALE', '1993-07-07', false),
       ('NORMAL', 'user8', 'password8', 'UserH', null, 'FEMALE', '1995-08-08', false),
       ('NORMAL', 'user9', 'password9', 'UserI', null, 'MALE', '1990-09-09', false),
       ('NORMAL', 'user10', 'password10', 'UserJ', null, 'FEMALE', '1991-10-10', false),
       ('NORMAL', 'user11', 'password11', 'UserK', null, 'MALE', '1992-11-11', false),
       ('NORMAL', 'user12', 'password12', 'UserL', null, 'FEMALE', '1993-12-12', false),
       ('NORMAL', 'user13', 'password13', 'UserM', null, 'MALE', '1994-01-13', false),
       ('NORMAL', 'user14', 'password14', 'UserN', null, 'FEMALE', '1995-02-14', false),
       ('NORMAL', 'user15', 'password15', 'UserO', null, 'MALE', '1996-03-15', false),
       ('NORMAL', 'user16', 'password16', 'UserP', null, 'FEMALE', '1997-04-16', false),
       ('NORMAL', 'user17', 'password17', 'UserQ', null, 'MALE', '1998-05-17', false),
       ('NORMAL', 'user18', 'password18', 'UserR', null, 'FEMALE', '1999-06-18', false),
       ('NORMAL', 'user19', 'password19', 'UserS', null, 'MALE', '2000-07-19', false),
       ('NORMAL', 'user20', 'password20', 'UserT', null, 'FEMALE', '2001-08-20', false),
       ('NORMAL', 'user21', 'password21', 'UserU', null, 'MALE', '2002-09-21', false),
       ('NORMAL', 'user22', 'password22', 'UserV', null, 'FEMALE', '2003-10-22', false),
       ('NORMAL', 'user23', 'password23', 'UserW', null, 'MALE', '2004-11-23', false),
       ('NORMAL', 'user24', 'password24', 'UserX', null, 'FEMALE', '2005-12-24', false),
       ('NORMAL', 'user25', 'password25', 'UserY', null, 'MALE', '2006-01-25', false),
       ('NORMAL', 'user26', 'password26', 'UserZ', null, 'FEMALE', '2007-02-26', false),
       ('NORMAL', 'user27', 'password27', 'UserAA', null, 'MALE', '2008-03-27', false),
       ('NORMAL', 'user28', 'password28', 'UserBB', null, 'FEMALE', '2009-04-28', false),
       ('NORMAL', 'user29', 'password29', 'UserCC', null, 'MALE', '2010-05-29', false),
       ('NORMAL', 'user30', 'password30', 'UserDD', null, 'FEMALE', '2011-06-30', false),
       ('NORMAL', 'user31', 'password31', 'UserEE', null, 'MALE', '2005-02-24', false);

INSERT INTO party (title, content, image_url, max_capacity, deadline, is_closed, is_deleted, host_id)
VALUES ('PartyA', 'Party Content A', null, 6, '2024-12-31T23:59:59', false, false, 1),
       ('PartyB', 'Party Content B', null, 5, '2024-11-15T18:00:00', false, false, 2),
       ('PartyC', 'Party Content C', null, 4, '2024-11-30T12:00:00', false, false, 3),
       ('PartyD', 'Party Content D', null, 4, '2024-11-11T11:11:11', true, false, 1),
       ('PartyE', 'Party Content E', null, 4, '2024-12-12T12:12:12', true, true, 4);

INSERT INTO inquiry (user_id, title, content, is_public, created_date, last_modified_date)
VALUES (1, 'InquiryA', 'ContentA', true, '2024-12-01T10:00:00', '2024-12-01T10:10:00'),
       (2, 'InquiryB', 'ContentB', false, '2024-12-02T11:00:00', '2024-12-02T11:10:00'),
       (3, 'InquiryC', 'ContentC', true, '2024-12-03T12:00:00', '2024-12-03T12:10:00'),
       (1, 'InquiryD', 'ContentD', false, '2024-12-04T13:00:00', '2024-12-04T13:10:00'),
       (4, 'InquiryE', 'ContentE', true, '2024-12-05T14:00:00', '2024-12-05T14:10:00');

INSERT INTO notice (user_id, title, content, views, created_date, last_modified_date)
VALUES (1, 'noticeA', 'ContentA', 0, '2024-12-01T10:00:00', '2024-12-01T10:10:00'),
       (2, 'noticeB', 'ContentB', 0, '2024-12-02T11:00:00', '2024-12-02T11:10:00'),
       (3, 'noticeC', 'ContentC', 0, '2024-12-03T12:00:00', '2024-12-03T12:10:00'),
       (1, 'noticeD', 'ContentD', 0, '2024-12-04T13:00:00', '2024-12-04T13:10:00'),
       (4, 'noticeE', 'ContentE', 0, '2024-12-05T14:00:00', '2024-12-05T14:10:00');