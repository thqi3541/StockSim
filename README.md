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
- User Story: As a new user, I want to create a new account, so that I can log into the app and start trading. To sign up/register, I need to choose a userID and a password.
  - Interactor: NewUser
  - Controller: RegistrationController
  - Presenter: RegistrationView
- Steps:
  - The user selects "Register" and inputs the required information (Interactor to Controller).
  - The RegistrationController processes the registration info, verifies data compliance, and creates a new account (Controller).
  - If registration is successful, the RegistrationController instructs the RegistrationView to display a success message and guide the user to log in (Controller to Presenter).
  - If registration fails, the RegistrationController instructs the RegistrationView to display an error message (Controller to Presenter).

Use Case 2: User Login
- User Story: As a returning user, I want to log into my account with my username, and password [credentials], so that I can resume all trading activities, balance, etc.
  - Interactor: ReturningUser
  - Controller: LoginController
  - Presenter: LoginView
- Steps:
  - The user inputs their login information and submits (Interactor to Controller).
  - The LoginController verifies credentials and processes the login (Controller).
  - If the login is successful, the LoginController loads the user data and directs the LoginView to the trading interface (Controller to Presenter).
  - If the login fails, the LoginController instructs the LoginView to display an error message (Controller to Presenter).

Use Case 3: Execute Trades - BUY
- User Story: As a user, I want to buy some shares of a stock, so that I will hold the long position of the stock. For example, I can buy 10 shares of APPL(Apple Inc.) at $5 per share.
- Buy/Sell
  - Interactor: CurrentUser
  - Controller: TradeController
  - Presenter: TradeView
- Steps:
  - The user selects a ticker(symbol of the stock) and quantity to buy, and submits a buy order in the trading window (Controller to Interactor).
  - The TradeInteractor processes the order, updating the account balance and portfolio (Interactor).
  - The TradeInteractor returns the trade results to the TradeView, displaying success or error messages (Interactor to Presenter).

Use Case 4: Execute Trades - SELL
- User Story: As a user, I want to sell some shares of a stock that I am currently holding, so that the position of the stock will decrease and I will get some money. For example, I have 10 shares of APPL(Apple Inc.), so I can sell 5 shares.
- Buy/Sell
  - Interactor: CurrentUser
  - Controller: TradeController
  - Presenter: TradeView
- Steps:
  - The user selects a ticker(symbol of the stock) and quantity to buy, and submits a sell order in the trading window (Controller to Interactor).
  - The TradeInteractor processes the order, updating the account balance and portfolio (Interactor).
  - The TradeInteractor returns the trade results to the TradeView, displaying success or error messages (Interactor to Presenter).

Use Case 5: Execute Trades - SHORTSELL
- User Story: As a user, I am not holding any shares of APPL(Apple). I want to short-sell 20 shares of Apple so that I can earn some money. Even though my current position at Apple is 0, I can 'borrow' some shares of Apple from someone else and sell the shares I borrowed.
- Buy/Sell
  - Interactor: CurrentUser
  - Controller: TradeController
  - Presenter: TradeView
- Steps:
  - The user selects a ticker(symbol of the stock) and quantity to short-sell, and submits a sell order in the trading window (Controller to Interactor).
  - The TradeInteractor processes the order, updating the account balance and portfolio (Interactor).
  - The TradeInteractor returns the trade results to the TradeView, displaying success or error messages (Interactor to Presenter).
    
  
Use Case 6: View Transaction History
- User Story:

Below are some other use cases we might consider implementing:

Use Case 6: View Ranking

Use Case 7: Password Reset + Email Verification
  We can either use JavaMail API
  
Use Case 8: Feedback / Support
  Users can report technical issues
  
Use Case 9: Minimum number of trades

Ask TA/prof where to store data
