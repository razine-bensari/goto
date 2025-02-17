# goto
Interview for GoTo

## pre-requisite
- docker (to run docker compose file for mongodb and docker image if needs be)
- java version 21
- gradle (8.12.1)

## How to run locally
- Run the docker compose file first to setup mongodb
- Then you can either run as a docker image
- Or, run it within intellij
- Or, by running `./gradlew bootRun` in the root project dir.


### Assumptions
- I am assuming the sorting for suits is as follows: hearts, spades, clubs, and diamonds. This is because this is how it was shown in the doc/requirements
- A game can be started, can be ended, but there is no time limit (can last forever essentially). But if we wanted to, we could add a TTL for a game.
- I have not added how to select a winner since it was not part of requirements. I left it as is, but we definitely can add the PlayerOperations api and extend this further
- I have not added the actions a player can do since it was not part of the requirements. Therefore, the rest API is not fully complete, in the sense it cannot be used to fully play a game.
- It seems we want to be able to create a deck. But a deck always lives within a game. I am assuming from the requirements that we want to have deck as its own resource with its own controller. I think this is not necessary but this is my assumption.

Some things worth noting:
- Ideally, if we had the full scope of requirements, we could represent the game as a state machine. Each state could dictate a possible list of operations by the player or the game. That would be really nice to design and code.
- Some rest apis have structured response and error response type to be gracefully handled in a UI. I did not implement that for time reason. But worth noting it is a good practice.
- I did not implement authentication. Obviously, a game REST API needs to be properly protected, as well as authorization implemented, so authenticated players can only interact with the game they are registered in.
- I had plans to implement unit testing as well in groovy with spock, but I decided to do it manually (manual testing using the api.http file - intellij's own http client) - I lacked time to add unit tests
