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


CREATE TABLE todo (
                      id SERIAL PRIMARY KEY,
                      employee_id INT NOT NULL REFERENCES employee(id) ON DELETE CASCADE,
                      title VARCHAR(100) NOT NULL,
                      description TEXT,
                      is_completed BOOLEAN DEFAULT FALSE,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


INSERT INTO role (role) VALUES
                            ('ROLE_ADMIN'),
                            ('ROLE_MANAGER'),
                            ('ROLE_EMPLOYEE');

-- Register 3 users

INSERT INTO employee_role (employee_id, role_id) VALUES
                                                     (1, 1),
                                                     (2, 2),
                                                     (3, 3),
                                                     (4, 3),
                                                     (5, 3);

INSERT INTO todo (employee_id, title, description) VALUES
            (1, 'Setup environment', 'Install Docker and PostgreSQL locally.'),
            (1, 'Review PR #42', 'Check the authentication code changes.'),
            (2, 'Update database backup', 'Configure weekly automated snapshots.'),
            (2, 'Write documentation', 'Document the new database schema API.'),
            (3, 'Audit user roles', 'Verify that all security permissions are correct.');
