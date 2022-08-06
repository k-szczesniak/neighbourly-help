INSERT INTO account (id, creation_date, modification_date, version, contact_number, email, enabled,
                     failed_login_attempts_counter, firstname, language, last_failed_login_date,
                     last_failed_login_ip_address, lastname, last_successful_login_date,
                     last_successful_login_ip_address, locked, password, theme_color, created_by, modified_by)
VALUES (-1, now(), null, 1, '123123123', 'adam@nowak.pl', true, 0, 'Adam', 'pl', null, null, 'Nowak', null, null, false,
        '$2a$12$O5GP5oVwDgWEaufF0wf/buMTaT5UnZJriWTKEYvajt0TJADGi3zZm', 'DARK', -1, null),
       (-2, now(), null, 1, '321321321', 'piotr@nowak.pl', true, 0, 'Piotr', 'pl', null, null, 'Nowak', null, null,
        false, '$2a$12$s/0MB4mwIgi2FOCImxFUEObn86dm0JSbP0T3NqY/OTzBcmoIzzGIy', 'LIGHT', -1, null),
       (-3, now(), null, 1, '687123654', 'adam@amc.pl', true, 0, 'Adam', 'pl', null, null, 'Admocl', null, null, false,
        '$2a$12$sHmwC0KZCpyRhV.sOlSyz.ncyGr.WeLkYDFmkFw5cZqjg7eHoEISK', 'DARK', -1, null),
       (-4, now(), null, 1, '987654321', 'klient1@klient.pl', true, 0, 'Klient', 'pl', null, null, 'Pierwszy', null,
        null, false,
        '$2a$10$RPPc4BROiNyQ.mPmohi2MOEtJfzdZ.P15/4v0W39RzbKBr038MZxu', 'DARK', -1, null),
       (-5, now(), null, 1, '987654322', 'klient2@klient.pl', true, 0, 'Klient', 'pl', null, null, 'Drugi', null, null,
        false,
        '$2a$10$TjeuQwfSjxPLUdPfkqO84ug2Gvn9aUV.GuqLivGYKIMDUb8n56BZq', 'DARK', -1, null);

INSERT INTO role(access_level, id, creation_date, modification_date, version, enabled, created_by, modified_by,
                 account_id)
VALUES ('ADMIN', -1, now(), null, 0, true, -1, null, -1),
       ('CLIENT', -2, now(), null, 0, true, -1, null, -2),
       ('ADMIN', -3, now(), null, 0, true, -1, null, -3),
       ('MODERATOR', -4, now(), null, 0, true, -1, null, -3),
       ('CLIENT', -5, now(), null, 0, true, -1, null, -3),
       ('CLIENT', -6, now(), null, 0, true, -1, null, -4),
       ('CLIENT', -7, now(), null, 0, true, -1, null, -5);

INSERT INTO city(id, creation_date, modification_date, version, name, created_by, modified_by)
VALUES (-1, now(), null, 0, 'Łódź', -1, null),
       (-2, now(), null, 0, 'Białystok', -1, null);

INSERT INTO loyalty_point(id, creation_date, modification_date, version, blocked_points, total_points, created_by,
                          modified_by)
VALUES (-1, now(), null, 0, 5, 15, -1, null),
       (-2, now(), null, 0, 0, 20, -1, null),
       (-3, now(), null, 0, 5, 15, -1, null),
       (-4, now(), null, 0, 5, 15, -1, null);

INSERT INTO admin_data(id)
values (-1);
INSERT INTO client_data(id, loyalty_point_id)
values (-2, -1);
INSERT INTO admin_data(id)
values (-3);
INSERT INTO moderator_data(id, city_id)
VALUES (-4, -1);
INSERT INTO client_data(id, loyalty_point_id)
values (-5, -2);
INSERT INTO client_data(id, loyalty_point_id)
values (-6, -3);
INSERT INTO client_data(id, loyalty_point_id)
values (-7, -4);

INSERT INTO advert(id, creation_date, modification_date, version, approved, category, description, prize,
                   publication_date, title, created_by, modified_by, city_id, publisher_id)
VALUES (-1, now(), null, 0, true, 0, 'Szukam osoby która posprząta mi biurko. Będę niezwykle wdzięczny za szybką pomoc',
        5, now(), 'Sprzątanie biurka', -5, null, -1, -5),
       (-2, now(), null, 0, false, 1, 'Szukam osoby która wypieli mi ogródek',
        10, now(), 'Pielenie ogrodu', -1, null, -1, -1),
       (-3, now(), null, 0, true, 2, 'Szukam osoby która zaopiekuje się moim dzieckiem w piątek wieczorem.',
        15, now(), 'Opieka nad dzieckiem', -1, null, -1, -1),
       (-4, now(), null, 0, false, 3, 'Szukam osoby będzie wyprowadzać mojego psa.',
        3, now(), 'Wyprowadzanie psa', -1, null, -1, -1),
       (-5, now(), null, 0, true, 3, 'Zadanie aktywne testowe dla użytkownika klient1.',
        3, now(), 'Klient aktywne', -2, null, -1, -1),
       (-6, now(), null, 0, false, 3, 'Zadanie nieaktywne testowe dla uzytkownika klient1.',
        3, now(), 'Klient nieaktywne', -2, null, -1, -1),
       (-7, now(), null, 0, true, 3, 'Zadanie do konkurencyjnego podjęcia przez 2 użytkowników',
        3, now(), 'Wyprowadzanie psa', -2, null, -1, -1),
       (-8, now(), null, 0, true, 3, 'Ogłoszenie wykorzystywane do utworzenia testowego kontraktu pozytywnego.',
        3, now(), 'Do kontraktu', -2, null, -1, -1),
       (-9, now(), null, 0, true, 3, 'Ogłoszenie wykorzystywane do utworzenia testowego kontraktu negatywnego.',
        3, now(), 'Do kontraktu', -2, null, -1, -1),
       (-10, now(), null, 0, true, 3, 'Ogłoszenie powiązane z kontraktem toApprove.',
        5, now(), 'Do kontraktu', -2, null, -1, -2),
       (-11, now(), null, 0, true, 3, 'Ogloszenie do usuniecia',
        5, now(), 'Do usuniecia', -4, null, -1, -4);

INSERT INTO contract(id, creation_date, modification_date, version, finish_date, start_date, status, created_by,
                     modified_by, advert_id, contract_ended_by, executor_id)
VALUES (-1, now(), null, 0, null, null, 0, -2, null, -8, null, -4),
       (-2, now(), null, 1, null, now(), 1, -2, null, -9, null, -4),
       (-3, now(), now(), 4, now(), now() - INTERVAL '1 week', 2, -5, -5, -10, -5, -4),
       (-4, now(), now(), 4, now(), now() - INTERVAL '2 week', 2, -2, -2, -1, -2, -4);