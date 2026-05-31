-- Create Employee table
CREATE TABLE employee (
                          id SERIAL PRIMARY KEY,
                          username VARCHAR(50) UNIQUE NOT NULL,
                          email VARCHAR(100) UNIQUE NOT NULL,
                          password VARCHAR(255) NOT NULL
);

-- Create Role table
CREATE TABLE role (
                      id SERIAL PRIMARY KEY,
                      role VARCHAR(50) UNIQUE NOT NULL
);

-- Create Employee_Role junction table (Many-to-Many relationship)
CREATE TABLE employee_role (
                               employee_id INT REFERENCES employee(id) ON DELETE CASCADE,
                               role_id INT REFERENCES role(id) ON DELETE CASCADE,
                               PRIMARY KEY (employee_id, role_id)
);


INSERT INTO role (role) VALUES
                            ('ROLE_ADMIN'),
                            ('ROLE_MANAGER'),
                            ('ROLE_EMPLOYEE');

INSERT INTO employee (username, email, password) VALUES
                                                     ('user_1', 'user1@example.com', '$2b$12$EXAMPLEHASH12345'),
                                                     ('user_2', 'user2@example.com', '$2b$12$EXAMPLEHASH67890'),
                                                     ('user_3', 'user3@example.com', '$2b$12$EXAMPLEHASH54321');

INSERT INTO employee_role (employee_id, role_id) VALUES
                                                     (1, 3), -- User 1 is an Employee
                                                     (2, 2), -- User 2 is a Manager
                                                     (2, 3), -- User 2 is also an Employee
                                                     (3, 1); -- User 3 is an Admin

CREATE TABLE todo (
                      id SERIAL PRIMARY KEY,
                      employee_id INT NOT NULL REFERENCES employee(id) ON DELETE CASCADE,
                      title VARCHAR(100) NOT NULL,
                      description TEXT,
                      is_completed BOOLEAN DEFAULT FALSE,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO todo (employee_id, title, description) VALUES
            (1, 'Setup environment', 'Install Docker and PostgreSQL locally.'),
            (1, 'Review PR #42', 'Check the authentication code changes.'),
            (2, 'Update database backup', 'Configure weekly automated snapshots.'),
            (2, 'Write documentation', 'Document the new database schema API.'),
            (3, 'Audit user roles', 'Verify that all security permissions are correct.');
