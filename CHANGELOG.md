
# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased] - yyyy-mm-dd

Here we write upgrading notes for brands. It's a team effort to make them as
straightforward as possible.

## 2021-04-27
### Added
- GameBoard now shows winner and resets game board when there was a winner

### Changed
- field indexes are now declared by constants (GameWinnerResolver and GameBoard)

## 2021-04-26
### Added
- added GameWinnerResolver
- GameBoard now executes basic validation
- GameBoard calls GameWinnerResolver

