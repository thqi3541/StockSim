# StockSim

## Table of Contents

- [StockSim](#stocksim)
  - [Table of Contents](#table-of-contents)
  - [Overview](#overview)
  - [Requirements](#requirements)
    - [Development Requirements](#development-requirements)
    - [System Requirements](#system-requirements)
  - [How to Install](#how-to-install)
    - [Build from Source](#build-from-source)
    - [Download](#download)
  - [Key Features](#key-features)
  - [Highlights](#highlights)
  - [Credits](#credits)

## Overview

StockSim is a sophisticated desktop trading simulator designed to replicate real-world market conditions with high fidelity.

Built with a focus on educational value and practical experience, it provides business students, aspiring traders, and financial professionals with a risk-free environment to develop and refine their trading strategies.

The platform offers real-time market data integration through Finnhub API and supports various trading operations including long positions, short selling, and portfolio management.

## Requirements

### Development Requirements

- Java Development Kit (JDK) 21 or higher
- Apache Maven 3.8.8 or higher

### System Requirements

- Internet connection for real-time market data

## How to Install

### Build from Source

1. Clone this repository
2. Apply for a API from [Finnhub](https://finnhub.io/)
3. Set up `STOCK_API_KEY` in `.env.local`
4. Request for MongoDB token via issues, then set up `MONGODB_API_KEY` in `.env.local`
5. Run the main app

### Download

1. Download the latest release from [here](https://github.com/ivorkchan/StockSim/releases)
2. To run the jar file, run `java -jar StockSim.jar`

## Key Features

For detailed information about use cases and user stories, please refer to [this page](/docs/Project%20Overview.md).

1. Sign Up

![sign-up](/assets/images/3.0-snapshot-sign-up.png)

2. Log In

![log-in](/assets/images/3.0-snapshot-log-in.png)

3. Dashboard

![dashboard](/assets/images/3.0-snapshot-dashboard.png)

4. Trade Simulation

![trade-simulation](/assets/images/3.0-snapshot-trade-simulation.png)

5. Transaction History

![history](/assets/images/3.0-snapshot-history.png)

## Highlights

1. SOLID and Clean Architecture
2. Scheduled Market Update
3. Multi-user Support and Persistent Data in MongoDB
4. UI and UX Design: [View Prototype in Figma](https://www.figma.com/proto/tm5D32ALPuOvfL2lvpir9c/StockSim?page-id=89%3A589&node-id=89-603&node-type=canvas&viewport=351%2C190%2C0.14&t=2N4BKpzFNg0XUjDq-1&scaling=min-zoom&content-scaling=fixed&starting-point-node-id=89%3A603)

## Credits

| Name          | GitHub Username                                         |
| ------------- | ------------------------------------------------------- |
| Angel Chen    | [`AngelChen09`](https://github.com/AngelChen09)         |
| Yue Cheng     | [`ivorkchan`](https://github.com/ivorkchan)             |
| Corrine Xiang | [`TheGreatCorrine`](https://github.com/TheGreatCorrine) |
| Raine Yang    | [`Raine-Yang-UofT`](https://github.com/Raine-Yang-UofT) |
| Jifeng Qiu    | [`thqi3541`](https://github.com/thqi3541)               |
