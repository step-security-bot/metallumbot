# MetallumBot

A simple Telegram bot written in java to retrieve upcoming albums and random bands from [Metal Archives](https://www.metal-archives.com/)

![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/errebenito/metallumbot/build.yml) ![Codacy grade](https://img.shields.io/codacy/grade/f90964df13e64e36a0c90e3542c108fd) ![Snyk Vulnerabilities for GitHub Repo](https://img.shields.io/snyk/vulnerabilities/github/errebenito/metallumbot) ![Codecov](https://img.shields.io/codecov/c/github/errebenito/metallumbot)

## Requirements

- Java 17+

- Maven

## Features

- Retrieve the link to a random band's page

- Retrieve the first 10 upcoming albums from the upcoming albums list.

## Configuration
The bot requires the following two environment variables to exist:

- METALLUM_BOT_TOKEN: Your telegram bot token.

- METALLUM_BOT_NAME: Your telegram bot username.

## Building the bot

- Clone this repository .

    `git clone https://github.com/errebenito/metallumbot` or Download this repo as zip and unzip

- Go to the metallumbot folder.

- In your command line of choice, type the following:

    `mvn clean package` (optionally add `-DskipTests` to skip execution of tests)

This will build metallumbot-X.Y.Z.jar in the target directory, where X.Y.Z is the current version number for the bot.

License
-------
Released under the GNU General Public License version 3 or, at your option, any later version.
See [LICENSE.md][license] for details.

[license]: LICENSE.md
