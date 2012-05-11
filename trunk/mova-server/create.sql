CREATE TABLE app.itemTypes  (
  name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE app.activityTypes  (
  name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE app.agentTypes  (
  name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE app.agents (
  agent_id VARCHAR(50) PRIMARY KEY , 
  agent_type VARCHAR(50) NOT NULL,
  agent_status CHAR NOT NULL,
  activity_id VARCHAR(50),
  registration_id VARCHAR(512),
  FOREIGN KEY (agent_type)
  REFERENCES app.agentTypes(name)
 );
 
CREATE TABLE app.activities (
  activity_id VARCHAR(50) PRIMARY KEY , 
  name VARCHAR(50),
  description VARCHAR(512),
  activity_type VARCHAR(50) NOT NULL,
  activity_state VARCHAR(50) NOT NULL,
  start_time TIMESTAMP,
  end_time TIMESTAMP,
  estimate_time INTEGER,
  FOREIGN KEY (activity_type)
  REFERENCES app.activityTypes(name)
);

CREATE TABLE app.items (
  item_id VARCHAR(50) PRIMARY KEY , 
  item_type VARCHAR(50) NOT NULL,
  item_state VARCHAR(50) NOT NULL,
  agent_id VARCHAR(50),
  FOREIGN KEY (item_type)
  REFERENCES app.itemTypes(name)
);


CREATE TABLE app.activityTypeItems  (
  activity_id VARCHAR(50),
  item_type VARCHAR(50),
  number_of_items INTEGER,
  PRIMARY KEY (activity_id, item_type),
  FOREIGN KEY (activity_id) 
  REFERENCES app.activities(activity_id) ON DELETE CASCADE,
  FOREIGN KEY (item_type) 
  REFERENCES app.itemTypes(name) ON DELETE CASCADE
);

CREATE TABLE app.activityTypeAgents  (
  activity_id VARCHAR(50),
  agent_type VARCHAR(50),
  number_of_agents INTEGER,
  PRIMARY KEY (activity_id, agent_type),
  FOREIGN KEY (activity_id) 
  REFERENCES app.activities(activity_id) ON DELETE CASCADE,
  FOREIGN KEY (agent_type) 
  REFERENCES app.agentTypes(name) ON DELETE CASCADE
);