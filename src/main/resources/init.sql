INSERT INTO account (id, creation_date, modification_date, version, contact_number, email, enabled,
                     failed_login_attempts_counter, firstname, language, last_failed_login_date,
                     last_failed_login_ip_address, lastname, last_successful_login_date,
                     last_successful_login_ip_address, locked, password, theme_color, created_by, modified_by)
VALUES (-1, now(), null, 1, '123123123', 'adam@nowak.pl', true, 0, 'Adam', 'pl', null, null, 'Nowak', null, null, false,
        '$2a$12$O5GP5oVwDgWEaufF0wf/buMTaT5UnZJriWTKEYvajt0TJADGi3zZm', 'DARK', -1, null),
       (-2, now(), null, 1, '321321321', 'piotr@nowak.pl', true, 0, 'Piotr', 'pl', null, null, 'Nowak', null, null,
        false, '$2a$12$s/0MB4mwIgi2FOCImxFUEObn86dm0JSbP0T3NqY/OTzBcmoIzzGIy', 'LIGHT', -1, null);

INSERT INTO role(access_level, id, creation_date, modification_date, version, enabled, created_by, modified_by,
                 account_id)
VALUES ('ADMIN', -1, now(), null, 1, true, -1, null, -1),
       ('CLIENT', -2, now(), null, 1, true, -1, null, -2);

INSERT INTO admin_data(id) values (-1);
INSERT INTO client_data(id) values (-2);

INSERT INTO city(id, creation_date, modification_date, version, name, simply_name, created_by, modified_by)
VALUES (-1, now(), null, 0, 'Łódź', 'lodz', -1, null),
       (-2, now(), null, 0, 'Białystok', 'bialystok', -1, null);

INSERT INTO advert(id, creation_date, modification_date, version, category, delete, description, publication_date, title, created_by, modified_by, city_id, publisher_id)
VALUES (-1, now(), null, 0, 1, false, 'Szukam osoby która posprząta mi biurko. Będę niezwykle wdzięczny za szybką pomoc',
        now(), 'Sprzątanie biurka', -1, null, -1, -1);