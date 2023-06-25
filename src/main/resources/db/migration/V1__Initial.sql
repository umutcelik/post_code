CREATE TABLE post_code (
  post_code VARCHAR(10) PRIMARY KEY,
  latitude DOUBLE,
  longitude DOUBLE,
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);