# Project Changelog

## Released

## Unreleased

### New Features

- Added `SessionManager` to validate client request by credential and manage active sessions
- Added GUI components for trading page

### Internal Changes

- Created documentation for GitHub workflow and pull request template
- Added maven workspace and configuration
- Added Use Cases 4 and 5
- Implemented entities for stock and stock market
- Implemented entities for user, portfolio, and transaction
- Added synchronization checks and null checks in `StockMarket`
- Modified `Portfolio` constructor to match the optional return value of `StockMarket.getStock`
- Modified `Portfolio` constructor to take `Map<String, UserStock>` as parameter and remove synchronization with `StockMarket` data
- Updated `UserStock`: Rename `PurchasePrice` to `cost`
- Implemented controller and presenter for execute buy use case
- Implemented framework for view component update
- Added GUI components for trading page
- Implemented buy use case interactor and set up unit tests
- Removed outdated classes and methods that are incompatible with current codebase version
- Updated maven configuration with unit test and api request dependencies
- Implemented the frontend framework

### Bug Fixes
