INSERT INTO user (login_id, password, nickname, profile_image, email, is_deleted)
VALUES ('user1', 'password1', 'UserA', null, 'user1@nolgoat.site', false),
       ('user2', 'password2', 'UserB', null, 'user2@nolgoat.site', false),
       ('user3', 'password3', 'UserC', null, 'user3@nolgoat.site', false),
       ('user4', 'password4', 'UserD', null, 'user4@nolgoat.site', false),
       ('user5', 'password5', 'UserE', null, 'user5@nolgoat.site', false),
       ('user6', 'password6', 'UserF', null, 'user6@nolgoat.site', false),
       ('user7', 'password7', 'UserG', null, 'user7@nolgoat.site', false),
       ('user8', 'password8', 'UserH', null, 'user8@nolgoat.site', false),
       ('user9', 'password9', 'UserI', null, 'user9@nolgoat.site', false),
       ('user10', 'password10', 'UserJ', null, 'user10@nolgoat.site', false),
       ('user11', 'password11', 'UserK', null, 'user11@nolgoat.site', false),
       ('user12', 'password12', 'UserL', null, 'user12@nolgoat.site', false),
       ('user13', 'password13', 'UserM', null, 'user13@nolgoat.site', false),
       ('user14', 'password14', 'UserN', null, 'user14@nolgoat.site', false),
       ('user15', 'password15', 'UserO', null, 'user15@nolgoat.site', false),
       ('user16', 'password16', 'UserP', null, 'user16@nolgoat.site', false),
       ('user17', 'password17', 'UserQ', null, 'user17@nolgoat.site', false),
       ('user18', 'password18', 'UserR', null, 'user18@nolgoat.site', false),
       ('user19', 'password19', 'UserS', null, 'user19@nolgoat.site', false),
       ('user20', 'password20', 'UserT', null, 'user20@nolgoat.site', false),
       ('user21', 'password21', 'UserU', null, 'user21@nolgoat.site', false),
       ('user22', 'password22', 'UserV', null, 'user22@nolgoat.site', false),
       ('user23', 'password23', 'UserW', null, 'user23@nolgoat.site', false),
       ('user24', 'password24', 'UserX', null, 'user24@nolgoat.site', false),
       ('user25', 'password25', 'UserY', null, 'user25@nolgoat.site', false),
       ('user26', 'password26', 'UserZ', null, 'user26@nolgoat.site', false),
       ('user27', 'password27', 'UserAA', null, 'user27@nolgoat.site', false),
       ('user28', 'password28', 'UserBB', null, 'user28@nolgoat.site', false),
       ('user29', 'password29', 'UserCC', null, 'user29@nolgoat.site', false),
       ('user30', 'password30', 'UserDD', null, 'user30@nolgoat.site', false),
       ('user31', 'password31', 'UserEE', null, 'user31@nolgoat.site', false);

INSERT INTO party (version, title, content, image_url, max_capacity, current_count, deadline, administrative_district,
                   is_closed,
                   is_deleted,
                   host_id)
VALUES (0, 'PartyA', 'Party Content A', null, 6, 1, '2024-12-31T23:59:59', 'GANGNAM_GU', false, false, 1),
       (0, 'PartyB', 'Party Content B', null, 6, 1, '2024-12-31T23:59:59', 'GANGNAM_GU', false, false, 1),
       (0, 'PartyC', 'Party Content C', null, 4, 1, '2024-11-30T12:00:00', 'MAPO_GU', false, false, 3),
       (0, 'PartyD', 'Party Content D', null, 4, 1, '2024-11-11T11:11:11', 'GANGNAM_GU', true, false, 1),
       (0, 'PartyE', 'Party Content E', null, 4, 1, '2024-12-12T12:12:12', 'SEOCHO_GU', true, true, 4),
       (0, 'PartyF', 'Party Content F', null, 5, 1, '2024-11-15T18:00:00', 'SEOCHO_GU', false, false, 2);

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