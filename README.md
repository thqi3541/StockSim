<div align="center">
	<h1 style="font-size: 48px;">StockSim</h1>
</div>

<div align="center" style="display: flex; justify-content: center; gap: 8px; align-items: center;">
	<img width="48" src="https://user-images.githubusercontent.com/25181517/117201156-9a724800-adec-11eb-9a9d-3cd0f67da4bc.png" alt="Java" title="Java"/>
	<img width="48" src="https://user-images.githubusercontent.com/25181517/117207242-07d5a700-adf4-11eb-975e-be04e62b984b.png" alt="Maven" title="Maven"/>
	<img width="48" src="https://user-images.githubusercontent.com/25181517/182884177-d48a8579-2cd0-447a-b9a6-ffc7cb02560e.png" alt="mongoDB" title="mongoDB"/>
	<img width="48" src="https://user-images.githubusercontent.com/25181517/189715289-df3ee512-6eca-463f-a0f4-c10d94a06b2f.png" alt="Figma" title="Figma"/>
	<img width="48" src="https://user-images.githubusercontent.com/25181517/192108374-8da61ba1-99ec-41d7-80b8-fb2f7c0a4948.png" alt="GitHub" title="GitHub"/>
	<img width="48" src="https://user-images.githubusercontent.com/25181517/183868728-b2e11072-00a5-47e2-8a4e-4ebbb2b8c554.png" alt="CI/CD" title="CI/CD"/>
</div>

## Table of Contents

- [Table of Contents](#table-of-contents)
- [Overview](#overview)
- [Requirements](#requirements)
  - [Development Requirements](#development-requirements)
  - [System Requirements](#system-requirements)
- [How to Install](#how-to-install)
  - [Build from Source](#build-from-source)
  - [Download](#download)
- [API Tokens](#api-tokens)
  - [Finnhub API Key (For Stock Data)](#finnhub-api-key-for-stock-data)
  - [MongoDB Username and Password (For User Data)](#mongodb-username-and-password-for-user-data)
- [Key Features](#key-features)
  - [Registration](#registration)
  - [Log In](#log-in)
  - [Dashboard](#dashboard)
  - [Trade Simulation](#trade-simulation)
  - [Transaction History](#transaction-history)
- [How to Contribute](#how-to-contribute)
- [Financial Terminologies](#financial-terminologies)
- [Accessibility](#accessibility)
- [Credits](#credits)

## Overview

StockSim is a sophisticated desktop trading simulator designed to replicate
real-world market conditions with high fidelity.

Built with a focus on educational value and practical experience, it provides
business students, aspiring traders, and financial professionals with a
risk-free environment to develop and refine their trading strategies.

The platform offers real-time market data integration through Finnhub API and
supports various trading operations including long positions, short selling, and
portfolio management.

## Requirements

### Development Requirements

- Java Development Kit (JDK) 21 or higher
- Apache Maven 3.8.8 or higher

### System Requirements

- Internet connection for real-time market data

## How to Install

### Build from Source

1. Clone this repository
2. Set up environmental variables in `.env.local`
3. Build the main app
   ```bash
   mvn clean package
   java -jar target/StockSim.jar
   ```

### Download

1. Download the latest release from
   [releases](https://github.com/StockSim/StockSim/releases)
2. Run the jar file:
   ```bash
   java -jar StockSim.jar
   ```

## API Tokens

### Finnhub API Key (For Stock Data)

- Create a Finnhub account at https://finnhub.io/.
- Click "Get free API key" to generate your API key.
- Add the key to your `.env.local` file as: `STOCK_API_KEY=your_finnhub_api_key`

### MongoDB Username and Password (For User Data)

- Create a MongoDB Atlas account at https://www.mongodb.com/.
- Share your MongoDB username and IP address with our team to be added to the
  project's cluster. (Note: All University of Toronto IP addresses are
  pre-whitelisted.)
- Once added, you'll receive the MongoDB API key.
- Add the key to your `.env.local` file as:
  `MONGODB_API_KEY=your_mongodb_api_key`

## Key Features

For detailed information about use cases and user stories, please refer to
[this page](/docs/Project%20Overview.md).

### Registration

![registration](/assets/images/3.0-snapshot-sign-up.png)

### Log In

![log-in](/assets/images/3.0-snapshot-log-in.png)

### Dashboard

![dashboard](/assets/images/3.0-snapshot-dashboard.png)

### Trade Simulation

On this view, user can view real-time stock information, sort and filter them by
ticker, company, and industry, then execute trades.

User's assets and portfolio will be displayed to provide information.

![trade-simulation](/assets/images/3.0-snapshot-trade-simulation.png)

### Transaction History

This view shows all transactions of the user.

![history](/assets/images/3.0-snapshot-history.png)

## How to Contribute

1. Fork the repository
2. Open issues for api keys
3. Create a new branch
4. Make changes and commit
5. Create a pull request

## Financial Terminologies

- `Balance`: The amount of cash currently available in your account.
- `Portfolio`: The collection of all stocks you currently hold. For example, if
  you own 100 shares of AAPL (Apple Inc.) and -50 shares of AMZN (Amazon), these
  positions comprise your portfolio.
  - Portfolio Value: The total market value of all stocks in your portfolio. For
    example, if AAPL trades at $10 per share and AMZN at $5 per share, your
    portfolio value would be (100 × $10) + (-50 × $5) = $750
- `Assets`: Your total wealth, calculated as the sum of your cash balance and
  portfolio value.
- `Position`: The quantity of shares you hold in a particular stock. A position
  can be:
  - Positive (Long Position): Owning shares, e.g., +100 shares of AAPL
  - Zero: No current position
  - Negative (Short Position): Borrowed shares that must be returned, e.g., -100
    shares of AAPL
- `Order Entry`: The process of submitting trade orders to the market:
  - `Buy` (+): Purchasing shares by paying cash, increasing your position
  - `Sell` (-): Selling shares to receive cash, decreasing your position

## Accessibility

Please refer to the [Accessibility Report](/docs/Accessibility%20Report.md).

## Credits

| Name          | GitHub Username                                         |
| ------------- | ------------------------------------------------------- |
| Angel Chen    | [`AngelChen09`](https://github.com/AngelChen09)         |
| Yue Cheng     | [`ivorkchan`](https://github.com/ivorkchan)             |
| Corrine Xiang | [`TheGreatCorrine`](https://github.com/TheGreatCorrine) |
| Raine Yang    | [`Raine-Yang-UofT`](https://github.com/Raine-Yang-UofT) |
| Jifeng Qiu    | [`thqi3541`](https://github.com/thqi3541)               |
