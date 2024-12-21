CREATE SCHEMA room_service AUTHORIZATION quizapp_user;
CREATE SCHEMA question_service AUTHORIZATION quizapp_user;
CREATE SCHEMA leaderboard_service AUTHORIZATION quizapp_user;

INSERT INTO question_service.question (uuid, text, options, correct_option, level, created_at)
VALUES
('1e7d1e2e-1c3b-4a2b-8a1e-1e7d1e2e1c3b', 'What is the capital of France?', '{"1": "Paris", "2": "London", "3": "Berlin", "4": "Madrid"}', 1, 1, '2023-01-01 10:00:00'),
('2e7d1e2e-2c3b-4a2b-8a1e-2e7d1e2e2c3b', 'Who wrote "To Kill a Mockingbird"?', '{"1": "Harper Lee", "2": "Mark Twain", "3": "Ernest Hemingway", "4": "F. Scott Fitzgerald"}', 1, 1, '2023-01-02 11:00:00'),
('3e7d1e2e-3c3b-4a2b-8a1e-3e7d1e2e3c3b', 'What is the chemical symbol for gold?', '{"1": "Au", "2": "Ag", "3": "Pb", "4": "Fe"}', 1, 1, '2023-01-03 12:00:00'),
('4e7d1e2e-4c3b-4a2b-8a1e-4e7d1e2e4c3b', 'What is the largest planet in our solar system?', '{"1": "Earth", "2": "Mars", "3": "Jupiter", "4": "Saturn"}', 3, 1, '2023-01-04 13:00:00'),
('5e7d1e2e-5c3b-4a2b-8a1e-5e7d1e2e5c3b', 'What is the speed of light?', '{"1": "300,000 km/s", "2": "150,000 km/s", "3": "450,000 km/s", "4": "600,000 km/s"}', 1, 1, '2023-01-05 14:00:00'),
('6e7d1e2e-6c3b-4a2b-8a1e-6e7d1e2e6c3b', 'What is the capital of Germany?', '{"1": "Berlin", "2": "Munich", "3": "Frankfurt", "4": "Hamburg"}', 1, 1, '2023-01-06 15:00:00'),
('7e7d1e2e-7c3b-4a2b-8a1e-7e7d1e2e7c3b', 'Who wrote "1984"?', '{"1": "George Orwell", "2": "Aldous Huxley", "3": "Ray Bradbury", "4": "J.K. Rowling"}', 1, 1, '2023-01-07 16:00:00'),
('8e7d1e2e-8c3b-4a2b-8a1e-8e7d1e2e8c3b', 'What is the chemical symbol for water?', '{"1": "H2O", "2": "O2", "3": "CO2", "4": "NaCl"}', 1, 1, '2023-01-08 17:00:00'),
('9e7d1e2e-9c3b-4a2b-8a1e-9e7d1e2e9c3b', 'What is the largest ocean on Earth?', '{"1": "Pacific Ocean", "2": "Atlantic Ocean", "3": "Indian Ocean", "4": "Arctic Ocean"}', 1, 1, '2023-01-09 18:00:00'),
('10e7d1e2-e10c-3b4a-2b8a-1e10e7d1e2e1', 'Who wrote "Pride and Prejudice"?', '{"1": "Jane Austen", "2": "Charlotte Bronte", "3": "Emily Bronte", "4": "Mary Shelley"}', 1, 1, '2023-01-10 19:00:00'),
('11e7d1e2-e11c-3b4a-2b8a-1e11e7d1e2e1', 'What is the capital of Italy?', '{"1": "Rome", "2": "Milan", "3": "Naples", "4": "Venice"}', 1, 1, '2023-01-11 10:00:00'),
('12e7d1e2-e12c-3b4a-2b8a-1e12e7d1e2e1', 'Who wrote "Moby-Dick"?', '{"1": "Herman Melville", "2": "Nathaniel Hawthorne", "3": "Edgar Allan Poe", "4": "Mark Twain"}', 1, 1, '2023-01-12 11:00:00');
