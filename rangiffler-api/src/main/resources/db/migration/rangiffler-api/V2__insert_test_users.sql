insert into `user` (username, firstname, lastName, country_id)
values
    ('testuser1', 'John', 'Doe', (select id from `country` where code = 'US')),
    ('testuser2', 'Jane', 'Smith', (select id from `country` where code = 'GB')),
    ('testuser3', 'Ivan', 'Petrov', (select id from `country` where code = 'RU')),
    ('testuser4', 'Anna', 'Müller', (select id from `country` where code = 'DE')),
    ('testuser5', 'Pierre', 'Dubois', (select id from `country` where code = 'FR')),
    ('admin', 'Admin', 'User', (select id from `country` where code = 'US')),
    ('guest', 'Guest', 'User', (select id from `country` where code = 'GB')),
    ('testuser8', 'Maria', 'Garcia', (select id from `country` where code = 'ES')),
    ('testuser9', 'Luca', 'Rossi', (select id from `country` where code = 'IT')),
    ('testuser10', 'Yuki', 'Tanaka', (select id from `country` where code = 'JP'));