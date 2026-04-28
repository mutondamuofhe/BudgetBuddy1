Budget Buddy
Budget Buddy is an Android application designed to help users manage their personal finances effectively. The application enables users to track expenses, set budget goals, and analyse spending patterns through interactive and user-friendly features. The system incorporates data persistence, filtering, and gamification to improve user engagement and financial awareness (Hamari, Koivisto & Sarsa, 2014).
Features
User Authentication
1.	Secure user registration and login functionality
2.	Basic validation of user credentials
3.	Local storage used to manage user sessions
Expense Tracking
1.	Add expenses including:
2.	Amount
3.	Category
4.	Date
5.	Start and end time
6.	Description
7.	Optional image attachment for each expense
8.	View expenses using a selectable date range
9.	Data stored locally using Room database (Android Developers, 2023)
Category Management
1.	Predefined categories such as Food, Transport, Shopping, and Bills
2.	Expenses grouped and analysed per category
3.	Category-based totals calculated automatically
Budget Management
1.	Set a minimum monthly spending goal
2.	Set a maximum monthly spending limit
3.	Real-time feedback:
4.	Within budget
5.	Over budget
6.	Below minimum target
7.	Budget values stored using SharedPreferences (Android Developers, 2023)
Visual Reports & Analysis
1.	Display total spending for a selected period
2.	Breakdown of spending per category
3.	Helps users identify spending habits and trends
Rewards System (Gamification)
1.	Interactive spin wheel feature
2.	Provides random financial advice and feedback
3.	Encourages user engagement and behavioural change, which aligns with gamification principles shown to improve user interaction (Hamari, Koivisto & Sarsa, 2014)
Technical Stack
1.	Programming Language: Kotlin
2.	Architecture: Model-View-ViewModel (MVVM), which promotes separation of concerns and maintainability (Google Developers, 2023)
3.	Database: Room Persistence Library (SQLite), used for structured local data storage (Android Developers, 2023)
4.	Data Storage: SharedPreferences (for budget goals) (Android Developers, 2023)
5.	Asynchronous Processing: Kotlin Coroutines
6.	User Interface Components:
7.	RecyclerView (for dynamic lists with images) (Android Developers, 2023)
8.	Material Design components for consistent UI/UX (Material Design, 2023)
Functional Requirements
     The application satisfies the following requirements:
1.	Implementation of multiple layouts for user interface design
2.	Use of standard input components such as EditText, Button, Spinner, and ImageView
3.	Navigation between activities using intents
4.	Storage of data using a local Room database (offline capability) (Android Developers, 2023)
5.	Ability to attach and display images for expense entries
6.	Filtering of expenses based on user-selected dates
7.	Calculation of totals per category
8.	Budget comparison and feedback system
9.	Interactive feature (spin wheel) to enhance user engagement (Hamari, Koivisto & Sarsa, 2014)
Screenshots
Screenshots of the application interface should be included here to demonstrate:
1.	Login and Registration screens
2.	Dashboard
3.	Add Expense screen
4.	Expense List with images
5.	Budget Goals screen
6.	Spin Wheel feature
Installation
1. Download or clone the repository
2. Open the project in Android Studio
3. Allow Gradle to sync dependencies
4. Run the application on an emulator or physical Android device
Author
Mutonda Muofhe, Rifa junior Nkanyane, Tavasi Mawayi, Bridget Muganedi
Diploma in Software Development
Rosebank College
Notes
This project was developed as part of an academic assignment focusing on mobile application development, user interface design, data persistence, and user engagement strategies. The integration of gamification elements improves user motivation and interaction (Hamari, Koivisto & Sarsa, 2014).
References
Hamari, J., Koivisto, J. and Sarsa, H., 2014. Does gamification work? A literature review of empirical studies on gamification. Proceedings of the 47th Hawaii International Conference on System Sciences, pp.3025–3034.
Google Developers, 2023. Guide to app architecture. Available at: https://developer.android.com/topic/architecture [Accessed 28 Apr. 2026].
Android Developers, 2023. Room persistence library. Available at: https://developer.android.com/training/data-storage/room [Accessed 28 Apr. 2026].
Android Developers, 2023. SharedPreferences overview. Available at: https://developer.android.com/training/data-storage/shared-preferences [Accessed 28 Apr. 2026].
Android Developers, 2023. RecyclerView overview. Available at: https://developer.android.com/guide/topics/ui/layout/recyclerview [Accessed 28 Apr. 2026].
Material Design, 2023. Material Design guidelines. Available at: https://m3.material.io/ [Accessed 28 Apr. 2026].
