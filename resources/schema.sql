CREATE TABLE clients (
                         client_id SERIAL PRIMARY KEY,
                         first_name VARCHAR(50) NOT NULL,
                         last_name VARCHAR(50) NOT NULL,
                         email VARCHAR(100) UNIQUE NOT NULL,
                         registered_at DATE DEFAULT CURRENT_DATE
);

CREATE TABLE freelancers (
                             freelancer_id SERIAL PRIMARY KEY,
                             first_name VARCHAR(50) NOT NULL,
                             last_name VARCHAR(50) NOT NULL,
                             email VARCHAR(100) UNIQUE NOT NULL,
                             rating DECIMAL(2,1) CHECK (rating BETWEEN 0 AND 5),
                             joined_at DATE NOT NULL,
                             phone VARCHAR(20)
);

CREATE TABLE projects (
                          project_id SERIAL PRIMARY KEY,
                          client_id INT NOT NULL,
                          title VARCHAR(100) NOT NULL,
                          budget DECIMAL(10,2) CHECK (budget > 0),
                          created_at DATE DEFAULT CURRENT_DATE,
                          FOREIGN KEY (client_id) REFERENCES clients(client_id)
);

CREATE TABLE bids (
                      bid_id SERIAL PRIMARY KEY,
                      project_id INT NOT NULL,
                      freelancer_id INT NOT NULL,
                      bid_amount DECIMAL(10,2) CHECK (bid_amount > 0),
                      bid_date DATE NOT NULL,
                      FOREIGN KEY (project_id) REFERENCES projects(project_id),
                      FOREIGN KEY (freelancer_id) REFERENCES freelancers(freelancer_id),
                      UNIQUE (project_id, freelancer_id)
    -- composite constraint (Advanced)
);

CREATE TABLE contracts (
                           contract_id SERIAL PRIMARY KEY,
                           project_id INT NOT NULL,
                           freelancer_id INT NOT NULL,
                           start_date DATE NOT NULL,
                           end_date DATE,
                           status VARCHAR(20) CHECK (status IN ('active', 'completed', 'cancelled')),
                           FOREIGN KEY (project_id) REFERENCES projects(project_id),
                           FOREIGN KEY (freelancer_id) REFERENCES freelancers(freelancer_id)
);

CREATE TABLE reviews (
                         review_id SERIAL PRIMARY KEY,
                         contract_id INT UNIQUE,
                         rating INT CHECK (rating BETWEEN 1 AND 5),
                         comment VARCHAR(255),
                         FOREIGN KEY (contract_id) REFERENCES contracts(contract_id)
);

-- =========================
-- SECTION B: INSERT DATA
-- =========================

INSERT INTO clients (first_name, last_name, email, registered_at) VALUES
                                                                      ('Айдос','Жанабаев','aidos@mail.com','2024-01-10'),
                                                                      ('Аружан','Серикова','aruzhan@mail.com','2024-01-12'),
                                                                      ('Нурлан','Ахметов','nurlan@mail.com','2024-01-15'),
                                                                      ('Диана','Касымова','diana@mail.com','2024-01-18'),
                                                                      ('Ержан','Тлеубаев','yerzhan@mail.com','2024-01-20'),
                                                                      ('Мадина','Омарова','madina@mail.com','2024-01-22'),
                                                                      ('Алихан','Садыков','alikhan@mail.com','2024-01-25'),
                                                                      ('Жансая','Муратова','zhansaya@mail.com','2024-01-28');

INSERT INTO freelancers (first_name,last_name,email,rating,joined_at,phone) VALUES
                                                                                ('Бекзат','Ибраев','bekzat@mail.com',4.8,'2023-11-01','+77010000001'),
                                                                                ('Айгерим','Нурбекова','aigerim@mail.com',4.6,'2023-11-05','+77010000002'),
                                                                                ('Данияр','Куанышев','daniyar@mail.com',4.3,'2023-11-10','+77010000003'),
                                                                                ('Сандугаш','Ермекова','sandukash@mail.com',4.9,'2023-11-12','+77010000004'),
                                                                                ('Руслан','Байжанов','ruslan@mail.com',4.0,'2023-11-15','+77010000005'),
                                                                                ('Алия','Турсынова','aliya@mail.com',4.7,'2023-11-18','+77010000006'),
                                                                                ('Мирас','Абдрахманов','miras@mail.com',4.2,'2023-11-20','+77010000007'),
                                                                                ('Асель','Калиева','assel@mail.com',4.8,'2023-11-22','+77010000008');

INSERT INTO projects (client_id,title,budget,created_at) VALUES
                                                             (1,'Website Landing Page',950,'2024-02-01'),
                                                             (2,'Mobile App UI Design',1350,'2024-02-02'),
                                                             (3,'E-commerce Backend',2800,'2024-02-03'),
                                                             (4,'Logo Branding',750,'2024-02-04'),
                                                             (5,'SEO Optimization',850,'2024-02-05'),
                                                             (6,'Blog Platform',1700,'2024-02-06'),
                                                             (7,'API Integration',1250,'2024-02-07'),
                                                             (8,'Data Analysis Script',1100,'2024-02-08');

INSERT INTO bids (project_id,freelancer_id,bid_amount,bid_date) VALUES
                                                                    (1,1,900,'2024-02-01'),
                                                                    (1,2,920,'2024-02-01'),
                                                                    (2,3,1300,'2024-02-02'),
                                                                    (2,4,1320,'2024-02-02'),
                                                                    (3,1,2700,'2024-02-03'),
                                                                    (3,5,2600,'2024-02-03'),
                                                                    (3,6,2750,'2024-02-03'),
                                                                    (4,2,720,'2024-02-04'),
                                                                    (4,7,740,'2024-02-04'),
                                                                    (5,8,820,'2024-02-05'),
                                                                    (5,3,830,'2024-02-05'),
                                                                    (6,4,1650,'2024-02-06'),
                                                                    (6,6,1680,'2024-02-06'),
                                                                    (7,1,1200,'2024-02-07'),
                                                                    (8,5,1050,'2024-02-08');

INSERT INTO contracts (project_id,freelancer_id,start_date,end_date,status) VALUES
                                                                                (1,1,'2024-02-02','2024-02-10','completed'),
                                                                                (2,4,'2024-02-03',NULL,'active'),
                                                                                (3,6,'2024-02-04',NULL,'active'),
                                                                                (4,2,'2024-02-05','2024-02-12','completed'),
                                                                                (6,4,'2024-02-07',NULL,'active');

INSERT INTO reviews (contract_id,rating,comment) VALUES
                                                     (1,5,'Excellent work'),
                                                     (4,4,'Good quality');
