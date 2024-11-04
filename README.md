# StockSim

## Members

| Name          | GitHub Username                                         |
| ------------- | ------------------------------------------------------- |
| Angel Chen    | [`AngelChen09`](https://github.com/AngelChen09)         |
| Yue Cheng     | [`ivorkchan`](https://github.com/ivorkchan)             |
| Corrine Xiang | [`TheGreatCorrine`](https://github.com/TheGreatCorrine) |
| Raine Yang    | [`Raine-Yang-UofT`](https://github.com/Raine-Yang-UofT) |
| Jifeng Qiu    | [`thqi3541`](https://github.com/thqi3541)               |

## Use cases

Use Case 1: User Registration
- User Story: new user, sign up/register, username, password, confirm password, set initial balance, initialize everything(portfolio, etc.)
  - Interactor: NewUser
  - Controller: RegistrationController
  - Presenter: RegistrationView
- Steps:
  - The user selects "Register" and inputs required information (Interactor to Controller).
  - The RegistrationController processes the registration info, verifies data compliance, and creates a new account (Controller).
  - If registration is successful, the RegistrationController instructs the RegistrationView to display a success message and guide the user to log in (Controller to Presenter).
  - If registration fails, the RegistrationController instructs the RegistrationView to display an error message (Controller to Presenter).

Use Case 2: User Login
- User Story: returning user, log in with username, password [credientials] to access the account, resume trading activities/balance, etc.
  - Interactor: ReturningUser
  - Controller: LoginController
  - Presenter: LoginView
- Steps:
  - The user inputs their login information and submits (Interactor to Controller).
  - The LoginController verifies credentials and processes the login (Controller).
  - If the login is successful, the LoginController loads the user data and directs the LoginView to the trading interface (Controller to Presenter).
  - If the login fails, the LoginController instructs the LoginView to display an error message (Controller to Presenter).

Use Case 3: Execute Trades
- User Story: Buy/Sell
  - Interactor: CurrentUser
  - Controller: TradeController
  - Presenter: TradeView
- Steps:
  - The user selects a stock, quantity, and submits a buy or sell order in the trading window (Interactor to Controller).
  - The TradeController processes the order, updating account balance and portfolio (Controller).
  - The TradeController returns the trade results to the TradeView, displaying success or error messages (Controller to Presenter).

Use Case 4: View Portfolio(Position)
- User Story:
  - As a trader, I want to access my current stock holdings and check bought price, quantity, current market price, profit/loss per share, and total profit/loss so I can make better trades.
  - As a trader, I want to sort by column and filter my current stock holdings to display select rows and quantity so I can view certain trades.
- Implementation:
  - Interactor: UserPortfolio
  - Controller: PortfolioController
  - Presenter: PortfolioView
- Steps:
  - The user selects a page and/or rows per page for pagination-based item selection from the selection provided near the bottom (Interactor to Controller).
  - The PortfolioController filters all user trades to the select rows, quantity, and column sort (Controller).
  - The PortfolioController returns the filtered data to the PortfolioView, displaying the new view of current stock holdings and its respective information.
  
Use Case 5: View Transaction History
- User Story:
  - As a trader, I want to access my transaction history and check bought and/or sold price, quantity, current market price, profit/loss per share, total profit/loss, and [Optional for sold stocks] current mp profit/loss per share, current mp total profit/loss so I can learn from past trades. 
  - As a trader, I want to sort by column and filter my current stock holdings to display select rows and quantity so I can view certain trades.
- Implementation:
  - Interactor: UserHistory
  - Controller: HistoryController
  - Presenter: HistoryView
- Steps:
  - The user selects a page and/or rows per page for pagination-based item selection from the selection provided near the bottom (Interactor to Controller).
  - The HistoryController filters all user trades to the select rows, quantity, and column sort (Controller).
  - The HistoryController returns the filtered data to the HistoryView, displaying the new view of current stock holdings and its respective information.

Below are some other use cases we might consider implementing:

Use Case 6: View Ranking

Use Case 7: Password Reset + Email Verification
  We can either use JavaMail API
  
Use Case 8: Feedback / Support
  Users can report technical issues
  
Use Case 9: Minimum number of trades

Ask TA/prof where to store data
