# MetallumBot

A simple Telegram bot written in java to retrieve upcoming albums and random bands from [Metal Archives](https://www.metal-archives.com/)

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
