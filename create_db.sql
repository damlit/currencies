-- SELECT 'CREATE DATABASE currencies'
-- WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'currencies');
CREATE DATABASE currencies;
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1000;
CREATE SEQUENCE IF NOT EXISTS my_seq_gen START 1000;