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
- User Story: Check
  
Use Case 5: Game Over Monitor
- User Story:

Below are some other use cases we might consider implementing:
Use Case 6: Password Reset
Use Case 7: Email Verification
  We can either use JavaMail API
Use Case 8: Feedback / Support
  Users can report technical issues
Use Case 9: Minimum number of trades

Ask TA/prof where to store data
