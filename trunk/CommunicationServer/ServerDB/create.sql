CREATE TABLE app.agents (
  agent_id VARCHAR(50) PRIMARY KEY , 
  agent_type VARCHAR(50) NOT NULL,
  agent_status CHAR NOT NULL,
  ip VARCHAR(50),
  registration_id VARCHAR(512)
 );