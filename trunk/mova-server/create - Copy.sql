CREATE TABLE itemTypes  (
  name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE activityTypes  (
  name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE agentTypes  (
  name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE agents (
  agent_id VARCHAR(50) PRIMARY KEY , 
  agent_type VARCHAR(50) NOT NULL,
  agent_status CHAR NOT NULL,
  activity_id VARCHAR(50),
  registration_id VARCHAR(512),
  FOREIGN KEY (agent_type)
  REFERENCES agentTypes(name)
 );
 
CREATE TABLE activities (
  activity_id VARCHAR(50) PRIMARY KEY , 
  name VARCHAR(50),
  description VARCHAR(512),
  activity_type VARCHAR(50) NOT NULL,
  activity_state VARCHAR(50) NOT NULL,
  start_time TIMESTAMP,
  end_time TIMESTAMP,
  estimate_time INTEGER,
  actual_start_time TIMESTAMP,
  actual_end_time TIMESTAMP,
  FOREIGN KEY (activity_type)
  REFERENCES activityTypes(name)
);

CREATE TABLE items (
  item_id VARCHAR(50) PRIMARY KEY , 
  item_type VARCHAR(50) NOT NULL,
  item_state VARCHAR(50) NOT NULL,
  agent_id VARCHAR(50),
  FOREIGN KEY (item_type)
  REFERENCES itemTypes(name)
);


CREATE TABLE activityTypeItems  (
  activity_id VARCHAR(50),
  item_type VARCHAR(50),
  number_of_items INTEGER,
  PRIMARY KEY (activity_id, item_type),
  FOREIGN KEY (activity_id) 
  REFERENCES activities(activity_id) ON DELETE CASCADE,
  FOREIGN KEY (item_type) 
  REFERENCES itemTypes(name) ON DELETE CASCADE
);

CREATE TABLE activityTypeAgents  (
  activity_id VARCHAR(50),
  agent_type VARCHAR(50),
  number_of_agents INTEGER,
  PRIMARY KEY (activity_id, agent_type),
  FOREIGN KEY (activity_id) 
  REFERENCES activities(activity_id) ON DELETE CASCADE,
  FOREIGN KEY (agent_type) 
  REFERENCES agentTypes(name) ON DELETE CASCADE
);

CREATE TABLE activityAgents  (
	activity_id VARCHAR(50),
	agent_id VARCHAR(50),
	PRIMARY KEY (activity_id, agent_id),
  	FOREIGN KEY (activity_id) 
  	REFERENCES activities(activity_id) ON DELETE CASCADE,
  	FOREIGN KEY (agent_id) 
  	REFERENCES agents(agent_id) ON DELETE CASCADE
);

CREATE TABLE activityItems  (
	activity_id VARCHAR(50),
	item_id VARCHAR(50),
	PRIMARY KEY (activity_id, item_id),
  	FOREIGN KEY (activity_id) 
  	REFERENCES activities(activity_id) ON DELETE CASCADE,
  	FOREIGN KEY (item_id) 
  	REFERENCES items(item_id) ON DELETE CASCADE
);

CREATE TABLE agentLocations  (
	agent_id VARCHAR(50) PRIMARY KEY,
	latitude INTEGER,
	longitude INTEGER,
	FOREIGN KEY (agent_id) 
  	REFERENCES agents(agent_id) ON DELETE CASCADE
);

CREATE TABLE itemLocations  (
	item_id VARCHAR(50) PRIMARY KEY,
	latitude INTEGER,
	longitude INTEGER,
	FOREIGN KEY (item_id) 
  	REFERENCES items(item_id) ON DELETE CASCADE
);

