INSERT INTO account (id, creation_date, modification_date, version, contact_number, email, enabled,
                     failed_login_attempts_counter, firstname, language, last_failed_login_date,
                     last_failed_login_ip_address, lastname, last_successful_login_date,
                     last_successful_login_ip_address, locked, password, created_by, modified_by)
VALUES (-1, now(), null, 1, '123123123', 'adam@nowak.pl', true, 0, 'Adam', 'pl', null, null, 'Nowak', null, null, false,
        '$2a$12$O5GP5oVwDgWEaufF0wf/buMTaT5UnZJriWTKEYvajt0TJADGi3zZm', -1, null),
       (-2, now(), null, 1, '321321321', 'piotr@nowak.pl', true, 0, 'Piotr', 'pl', null, null, 'Nowak', null, null,
        false, '$2a$12$s/0MB4mwIgi2FOCImxFUEObn86dm0JSbP0T3NqY/OTzBcmoIzzGIy', -1, null);

INSERT INTO role(access_level, id, creation_date, modification_date, version, enabled, created_by, modified_by,
                 account_id)
VALUES ('ADMIN', -1, now(), null, 1, true, -1, null, -1),
       ('CLIENT', -2, now(), null, 1, true, -1, null, -2);

INSERT INTO admin_data(id) values (-1);
INSERT INTO client_data(id) values (-2);