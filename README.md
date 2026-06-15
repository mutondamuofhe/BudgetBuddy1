Budget Buddy
Overview
Budget Buddy is a mobile budgeting application developed using Kotlin and Android Studio. The purpose of the application is to help users manage their personal finances by tracking expenses, setting budget goals, and monitoring spending habits.
Many people struggle to keep track of where their money goes each month. Budget Buddy provides a simple and user-friendly solution by allowing users to record expenses, organise them into categories, and compare their spending against budget goals.
The application was developed as part of a Mobile Application Development project and demonstrates the use of Android development concepts such as Room Database, user authentication, data persistence, RecyclerViews, image handling, and gamification features.

Target Users
Budget Buddy is aimed at
•	Students who need to manage limited budgets.
•	Young professionals who want to monitor their spending.
•	Individuals looking for a simple offline budgeting tool.
•	Anyone interested in developing better financial habits.

Main Features
User Registration and Login
Users can create an account and log into the application using their credentials. This ensures that each user can securely access their budgeting information.
Expense Tracking
Users can add expenses by entering
* Amount spent
* Category
* Date
* Start and end time
* Description
Users can also attach a photo to an expense entry if they wish to keep a visual record of their purchases.
Expense Categories
Expenses are organised into categories such as Food, Transport, Shopping, and Bills. This helps users understand where most of their money is being spent.
Budget Goals

The application allows users to set:
* A minimum monthly spending goal
* A maximum monthly spending limit
The app then compares the user’s spending against these goals and provides feedback.

Expense Filtering
Users can filter expenses using a selected date range, making it easier to review spending over a specific period.
Spending Analysis
The application automatically calculates the total amount spent and displays totals for each category, helping users analyse their spending patterns.

Spin Wheel Feature
To make the application more engaging, a spin wheel feature was added. Users can spin the wheel to receive financial tips, motivational messages, and spending advice.

Design Decisions
Several design decisions were made during development to improve usability and functionality.

Simple User Interface
The interface was designed to be clean and easy to navigate. We wanted users to be able to access important features without unnecessary complexity. Material Design principles were considered when designing the user interface (Material Design, 2023).

Offline Functionality
Room Database was selected because it allows data to be stored locally on the device. This means users can continue using the application even without an internet connection (Android Developers, 2023).
Budget Monitoring
Budget goals were included because simply recording expenses is often not enough. Users should also be able to compare their spending against personal targets.
Gamification
The spin wheel was introduced to make the application more interactive and enjoyable. Research shows that gamification can improve user engagement and motivation (Hamari, Koivisto & Sarsa, 2014).

GitHub and GitHub Actions
GitHub Usage
GitHub was used throughout the project to manage source code and collaborate as a team.
GitHub allowed team members to:
* Share code efficiently
* Track changes made to the project
* Work on different features simultaneously
* Maintain a backup of the project
* Resolve issues during development

Using GitHub helped ensure that the project remained organised and that all team members could contribute effectively.
GitHub Actions
GitHub Actions was used to automate testing and continuous integration.
Whenever code is pushed to the repository, GitHub Actions automatically runs the configured tests and checks whether the application builds successfully. This helps identify problems early and improves the reliability of the project.

video link
https://www.youtube.com/watch?v=eVMzNs_QXj8

Custom Feature 1: Dark Mode
One of the custom features implemented in Budget Buddy is a Dark Mode option. This feature allows users to switch between light and dark themes depending on their preference.
The purpose of this feature is to improve user experience by providing a more comfortable viewing option, especially in low-light environments. Dark Mode can also help reduce eye strain during prolonged use of the application and provides a modern appearance that many users prefer.
Custom Feature 2: Spin Wheel Gamification

The second custom feature is the Spin Wheel.
The spin wheel introduces a fun and interactive element to the application. Users can spin the wheel to receive financial tips, motivational messages, and spending advice.
The purpose of this feature is to improve user engagement and encourage users to interact with the application more regularly.

 How to Use Budget Buddy

Getting Started

Step 1: Register an Account

When you open Budget Buddy for the first time, create an account by selecting the Register option.

Simply enter your details and create a username and password. Once your information has been entered successfully, your account will be created and you can proceed to log in.

Step 2: Log In

After registering, enter your username and password on the login screen and select Login.

If your credentials are correct, you will be taken to the Dashboard, which is the main screen of the application.

Dashboard

The Dashboard gives you a quick overview of your finances and provides easy access to all the application’s features.

At the top of the screen, you can view:

* Balance – Your available balance.
* Expenses – The total amount you have spent.
* Budget – Your budget information.

Below this section, you will find the Quick Actions menu, which allows you to navigate to different features of the app.

Adding an Expense

To record a new expense:

1. Select Add Expense from the Dashboard.
2. Enter the expense details, including:
    * Amount spent
    * Category
    * Date
    * Start and end time
    * Description
3. If you would like to keep a record of a receipt or purchase, you can optionally add a photo.
4. Select Save Expense.

Your expense will then be stored and added to your expense history.
Viewing Expenses

To see your recorded expenses:

1. Select View Expenses from the Dashboard.
2. Choose a start date and end date if you would like to view expenses from a specific period.
3. Select Filter.

The application will display all expenses that match your selected date range, including any attached images.

Setting Budget Goals

Budget Buddy allows you to set personal spending goals.

1. Select Budget Goals from the Dashboard.
2. Enter:
    * Your minimum monthly spending goal
    * Your maximum monthly spending limit
3. Save your changes.

The application will compare your spending against these goals and provide feedback on your budgeting progress.

Viewing Reports

The Reports section helps you understand your spending habits.

From the Dashboard:

1. Select Reports.
2. View your total spending and category totals.
3. Use the information provided to identify areas where you may be overspending or saving effectively.

Rewards Feature

To make budgeting more enjoyable, Budget Buddy includes a rewards feature.

1. Select Rewards from the Dashboard.
2. Press Spin.
3. The wheel will spin and stop on a random result.
4. You will receive a financial tip, motivational message, or reward.

Profile

The Profile section allows you to view your account information and application settings.

From here, you can manage your preferences and personalise your experience within the application.

Tips for Using Budget Buddy

* Record expenses as soon as you make them.
* Set realistic budget goals that suit your lifestyle.
* Review your reports regularly to monitor spending habits.
* Use categories to keep your expenses organised.
* Take advantage of the rewards feature for extra motivation while managing your finances.

Budget Buddy was designed to make personal finance management simple, organised, and engaging for everyday users.




References
Android Developers, 2023. RecyclerView overview. Available at: https://developer.android.com/guide/topics/ui/layout/recyclerview [Accessed 15 June 2026].

Android Developers, 2023. Room persistence library. Available at: https://developer.android.com/training/data-storage/room [Accessed 15 June 2026].

Hamari, J., Koivisto, J. and Sarsa, H., 2014. Does gamification work? A literature review of empirical studies on gamification. Proceedings of the 47th Hawaii International Conference on System Sciences, pp.3025–3034.
Material Design, 2023. Material Design Guidelines. Available at: https://m3.material.io/ [Accessed 15 June 2026].

