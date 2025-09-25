Rewards Service API

Project Purpose
The Rewards Service calculates customer reward points based on transactions.
Each transaction earns points based on amount thresholds:
0 points for transactions ≤ $50
1 point per $1 for transactions > $50 and ≤ $100
2 points per $1 for transactions > $100
Rewards are calculated monthly per customer.
Total points and transaction amounts are summarized.


Tech Stack
Java 17+
Spring Boot 3
Spring Data JPA
H2
Maven
Java Streams for calculations
Global Exception Handling via @ControllerAdvice

Build & Run
Clone the repository:
git clone <repository-url>
cd rewards-service


Build the project:
mvn clean install

Run the application:
mvn spring-boot:run


Application runs on default port 8080.
You can customize this in application.properties.

1. Calculate rewards for all transactions
Request:
GET http://localhost:8080/api/rewards
Response

[
    {
        "customerName": "Bob",
        "monthlyPoints": [
            {
                "month": "2025-01",
                "points": 0,
                "amount": 45.00
            },
            {
                "month": "2025-02",
                "points": 70,
                "amount": 110.00
            },
            {
                "month": "2025-03",
                "points": 10,
                "amount": 60.00
            }
        ],
        "totalPoints": 80,
        "totalAmount": 215.00
    },
    {
        "customerName": "Alice",
        "monthlyPoints": [
            {
                "month": "2025-01",
                "points": 90,
                "amount": 120.00
            },
            {
                "month": "2025-02",
                "points": 25,
                "amount": 75.00
            },
            {
                "month": "2025-03",
                "points": 250,
                "amount": 200.00
            }
        ],
        "totalPoints": 365,
        "totalAmount": 395.00
    },
    {
        "customerName": "Charlie",
        "monthlyPoints": [
            {
                "month": "2025-01",
                "points": 110,
                "amount": 130.00
            },
            {
                "month": "2025-02",
                "points": 45,
                "amount": 95.00
            },
            {
                "month": "2025-06",
                "points": 5,
                "amount": 55.00
            }
        ],
        "totalPoints": 160,
        "totalAmount": 280.00
    }
]

Error Handling
Invalid transaction amounts or empty transaction lists throw RewardsCalculationException.
Global exception handling ensures consistent JSON error responses:

{
    "message": "Transactions must not be null",
    "timestamp": "2025-09-25T01:19:28.7644803",
    "status": 500
}
