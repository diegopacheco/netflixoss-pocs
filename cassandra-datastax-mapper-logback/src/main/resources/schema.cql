CREATE KEYSPACE IF NOT EXISTS datastax_mapper_test WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': '1'
};

USE datastax_mapper_test;

CREATE TABLE IF NOT EXISTS users (
  user_id uuid,
  year int,
  name text,
  email text,
  PRIMARY KEY ((user_id))
);