CREATE TABLE app.agents (
  agent_id VARCHAR(50) PRIMARY KEY , 
  agent_type VARCHAR(50) NOT NULL,
  agent_status CHAR NOT NULL,
  activity_id VARCHAR(50),
  registration_id VARCHAR(512)
 );
 
CREATE TABLE app.activities (
  activity_id VARCHAR(50) PRIMARY KEY , 
  name VARCHAR(50),
  description VARCHAR(512),
  activity_type VARCHAR(50) NOT NULL,
  start_time TIMESTAMP,
  end_time TIMESTAMP,
  estimate_time INTEGER
);